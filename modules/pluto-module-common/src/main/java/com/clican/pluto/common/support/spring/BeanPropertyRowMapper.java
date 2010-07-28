/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.common.support.spring;

import java.beans.PropertyDescriptor;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.NotWritablePropertyException;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.util.Assert;

@SuppressWarnings("unchecked")
public class BeanPropertyRowMapper implements RowMapper {

	/** Logger available to subclasses */
	protected final Log logger = LogFactory.getLog(RowMapper.class);

	/** The class we are mapping to */

	private Class mappedClass;

	/** Whether we're strictly validating */
	private boolean checkFullyPopulated = false;

	/** Map of the fields we provide mapping for */
	private Map mappedFields;

	/** Set of bean properties we provide mapping for */
	private Set mappedProperties;

	/**
	 * Create a new BeanPropertyRowMapper for bean-style configuration.
	 * 
	 * @see #setMappedClass
	 * @see #setCheckFullyPopulated
	 */
	public BeanPropertyRowMapper() {
	}

	/**
	 * Create a new BeanPropertyRowMapper, accepting unpopulated properties in
	 * the target bean.
	 * 
	 * @param mappedClass
	 *            the class that each row should be mapped to
	 */
	public BeanPropertyRowMapper(Class mappedClass) {
		initialize(mappedClass);
	}

	/**
	 * Create a new BeanPropertyRowMapper.
	 * 
	 * @param mappedClass
	 *            the class that each row should be mapped to
	 * @param checkFullyPopulated
	 *            whether we're strictly validating that all bean properties
	 *            have been mapped from corresponding database fields
	 */
	public BeanPropertyRowMapper(Class mappedClass, boolean checkFullyPopulated) {
		initialize(mappedClass);
		this.checkFullyPopulated = checkFullyPopulated;
	}

	/**
	 * Set the class that each row should be mapped to.
	 */
	public void setMappedClass(Class mappedClass) {
		if (this.mappedClass == null) {
			initialize(mappedClass);
		} else {
			if (!this.mappedClass.equals(mappedClass)) {
				throw new InvalidDataAccessApiUsageException("The mapped class can not be reassigned to map to " + mappedClass
						+ " since it is already providing mapping for " + this.mappedClass);
			}
		}
	}

	/**
	 * Initialize the mapping metadata for the given class.
	 * 
	 * @param mappedClass
	 *            the mapped class.
	 */
	protected void initialize(Class mappedClass) {
		this.mappedClass = mappedClass;
		this.mappedFields = new HashMap();
		this.mappedProperties = new HashSet();
		PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(mappedClass);
		for (int i = 0; i < pds.length; i++) {
			PropertyDescriptor pd = pds[i];
			if (pd.getWriteMethod() != null) {
				this.mappedFields.put(pd.getName().toLowerCase(), pd);
				String underscoredName = underscoreName(pd.getName());
				if (!pd.getName().toLowerCase().equals(underscoredName)) {
					this.mappedFields.put(underscoredName, pd);
				}
				this.mappedProperties.add(pd.getName());
			}
		}
	}

	/**
	 * Convert a name in camelCase to an underscored name in lower case. Any
	 * upper case letters are converted to lower case with a preceding
	 * underscore.
	 * 
	 * @param name
	 *            the string containing original name
	 * @return the converted name
	 */
	private String underscoreName(String name) {
		StringBuffer result = new StringBuffer();
		if (name != null && name.length() > 0) {
			result.append(name.substring(0, 1).toLowerCase());
			boolean reset = false;
			for (int i = 1; i < name.length(); i++) {
				String s = name.substring(i, i + 1);
				if (s.equals(s.toUpperCase()) && !StringUtils.isNumeric(s)) {
					if (reset) {
						reset = false;
						result.append("_");
						result.append(s.toLowerCase());
					} else {
						result.append(s.toLowerCase());
					}
				} else {
					reset = true;
					result.append(s.toLowerCase());
				}
			}
		}
		return result.toString();
	}

	/**
	 * Get the class that we are mapping to.
	 */
	public final Class getMappedClass() {
		return this.mappedClass;
	}

	/**
	 * Set whether we're strictly validating that all bean properties have been
	 * mapped from corresponding database fields.
	 * <p>
	 * Default is <code>false</code>, accepting unpopulated properties in the
	 * target bean.
	 */
	public void setCheckFullyPopulated(boolean checkFullyPopulated) {
		this.checkFullyPopulated = checkFullyPopulated;
	}

	/**
	 * Return whether we're strictly validating that all bean properties have
	 * been mapped from corresponding database fields.
	 */
	public boolean isCheckFullyPopulated() {
		return this.checkFullyPopulated;
	}

	/**
	 * Extract the values for all columns in the current row.
	 * <p>
	 * Utilizes public setters and result set metadata.
	 * 
	 * @see java.sql.ResultSetMetaData
	 */
	public Object mapRow(ResultSet rs, int rowNumber) throws SQLException {
		Assert.state(this.mappedClass != null, "Mapped class was not specified");
		Object mappedObject = BeanUtils.instantiateClass(this.mappedClass);
		BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(mappedObject);
		initBeanWrapper(bw);

		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();
		Set populatedProperties = (isCheckFullyPopulated() ? new HashSet() : null);

		for (int index = 1; index <= columnCount; index++) {
			String column = JdbcUtils.lookupColumnName(rsmd, index).toLowerCase();
			PropertyDescriptor pd = (PropertyDescriptor) this.mappedFields.get(column);
			if (pd != null) {
				try {
					Object value = getColumnValue(rs, index, pd);
					if (logger.isDebugEnabled() && rowNumber == 0) {
						logger.debug("Mapping column '" + column + "' to property '" + pd.getName() + "' of type " + pd.getPropertyType());
					}
					bw.setPropertyValue(pd.getName(), value);
					if (populatedProperties != null) {
						populatedProperties.add(pd.getName());
					}
				} catch (NotWritablePropertyException ex) {
					throw new DataRetrievalFailureException("Unable to map column " + column + " to property " + pd.getName(), ex);
				}
			}
		}

		if (populatedProperties != null && !populatedProperties.equals(this.mappedProperties)) {
			throw new InvalidDataAccessApiUsageException("Given ResultSet does not contain all fields " + "necessary to populate object of class ["
					+ this.mappedClass + "]: " + this.mappedProperties);
		}

		return mappedObject;
	}

	/**
	 * Initialize the given BeanWrapper to be used for row mapping. To be called
	 * for each row.
	 * <p>
	 * The default implementation is empty. Can be overridden in subclasses.
	 * 
	 * @param bw
	 *            the BeanWrapper to initialize
	 */
	protected void initBeanWrapper(BeanWrapper bw) {
	}

	/**
	 * Retrieve a JDBC object value for the specified column.
	 * <p>
	 * The default implementation calls
	 * {@link JdbcUtils#getResultSetValue(java.sql.ResultSet, int, Class)}.
	 * Subclasses may override this to check specific value types upfront, or to
	 * post-process values return from <code>getResultSetValue</code>.
	 * 
	 * @param rs
	 *            is the ResultSet holding the data
	 * @param index
	 *            is the column index
	 * @param pd
	 *            the bean property that each result object is expected to match
	 *            (or <code>null</code> if none specified)
	 * @return the Object value
	 * @throws SQLException
	 *             in case of extraction failure
	 * @see org.springframework.jdbc.support.JdbcUtils#getResultSetValue(java.sql.ResultSet,
	 *      int, Class)
	 */
	protected Object getColumnValue(ResultSet rs, int index, PropertyDescriptor pd) throws SQLException {
		Object obj = JdbcUtils.getResultSetValue(rs, index, pd.getPropertyType());
		if (obj instanceof Date) {
			return new Date(((Date) obj).getTime());
		} else {
			return obj;
		}
	}

}

// $Id$