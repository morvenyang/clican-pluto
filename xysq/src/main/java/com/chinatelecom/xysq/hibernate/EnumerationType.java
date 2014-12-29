package com.chinatelecom.xysq.hibernate;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.AssertionFailure;
import org.hibernate.HibernateException;
import org.hibernate.usertype.ParameterizedType;
import org.hibernate.usertype.UserType;

import com.chinatelecom.xysq.enumeration.LabelAndValue;

public class EnumerationType implements UserType, ParameterizedType {

	private final static Log log = LogFactory.getLog(EnumerationType.class);

    public static final String ENUM = "enumClass";
    public static final String TYPE = "type";

    @SuppressWarnings("rawtypes")
    private Class<? extends Enum> enumClass;

    private static Map<Class<?>, Object[]> enumValues = new HashMap<Class<?>, Object[]>();

    private int sqlType = Types.INTEGER; // before any guessing

    @Override
    public int[] sqlTypes() {
        return new int[] { sqlType };
    }

    @Override
    public Class<?> returnedClass() {
        return enumClass;
    }

    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        return x == y;
    }

    @Override
    public int hashCode(Object x) throws HibernateException {
        return x == null ? 0 : x.hashCode();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, Object owner) throws HibernateException, SQLException {
        Object object = rs.getObject(names[0]);
        if (rs.wasNull()) {
            if (log.isTraceEnabled()) {
                log.trace("Returning null as column " + names[0]);
            }
            return null;
        }
        if (object instanceof Number) {
            Object[] values = enumValues.get(enumClass);
            if (values == null)
                throw new AssertionFailure("enumValues not preprocessed: " + enumClass);
            int intVal = ((Number) object).intValue();
            for (Object value : values) {
                LabelAndValue lav = (LabelAndValue) value;
                if (lav.getValue()==intVal) {
                    return value;
                }
            }
            return null;
        } else {
            String name = (String) object;
            if (log.isTraceEnabled()) {
                log.trace("Returning '" + name + "' as column " + names[0]);
            }
            try {
                return Enum.valueOf(enumClass, name);
            } catch (IllegalArgumentException iae) {
                throw new IllegalArgumentException("Unknown name value for enum " + enumClass + ": " + name, iae);
            }
        }
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index) throws HibernateException, SQLException {
        if (value == null) {
            if (log.isTraceEnabled())
                log.trace("Binding null to parameter: " + index);
            st.setNull(index, sqlType);
        } else {
            st.setObject(index, ((LabelAndValue) value).getValue());
        }

    }

    @Override
    public Object deepCopy(Object value) throws HibernateException {
        return value;
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable) value;
    }

    @Override
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return cached;
    }

    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return original;
    }

    public void setParameterValues(Properties parameters) {
        String enumClassName = parameters.getProperty(ENUM);
        String type = parameters.getProperty(TYPE);
        if (StringUtils.isNotEmpty(type)) {
            if (type.equals("string")) {
                sqlType = Types.VARCHAR;
            }
        }
        try {
            enumClass = Class.forName(enumClassName).asSubclass(Enum.class);
        } catch (ClassNotFoundException exception) {
            throw new HibernateException("Enum class not found", exception);
        }
        // this is threadsafe to do it here, setParameterValues() is called sequencially
        initEnumValue();

    }

    private void initEnumValue() {
        Object[] values = enumValues.get(enumClass);
        if (values == null) {
            try {
                Method method = null;
                method = enumClass.getDeclaredMethod("values", new Class[0]);
                values = (Object[]) method.invoke(null, new Object[0]);
                enumValues.put(enumClass, values);
            } catch (Exception e) {
                throw new HibernateException("Error while accessing enum.values(): " + enumClass, e);
            }
        }
    }

}
