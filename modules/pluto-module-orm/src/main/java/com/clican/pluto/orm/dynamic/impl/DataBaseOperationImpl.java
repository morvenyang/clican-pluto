/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wezhang
 *
 */
package com.clican.pluto.orm.dynamic.impl;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.Mapping;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.ForeignKey;
import org.hibernate.mapping.Table;

import com.clican.pluto.common.exception.DDLException;
import com.clican.pluto.common.exception.PlutoException;
import com.clican.pluto.common.type.CustomListType;
import com.clican.pluto.common.type.Type;
import com.clican.pluto.orm.desc.ModelDescription;
import com.clican.pluto.orm.desc.PropertyDescription;
import com.clican.pluto.orm.dialect.DialectExtention;
import com.clican.pluto.orm.dynamic.inter.IDataBaseOperation;
import com.clican.pluto.orm.tool.DatabaseMetadata;
import com.clican.pluto.orm.tool.ForeignKeyMetadata;
import com.clican.pluto.orm.tool.TableMetadata;

public class DataBaseOperationImpl implements IDataBaseOperation {

	private final static Log log = LogFactory
			.getLog(DataBaseOperationImpl.class);

	private Dialect dialect;

	private DataSource dataSource;

	public void setDialect(Dialect dialect) {
		this.dialect = dialect;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@SuppressWarnings("unchecked")
	public void alterTable(Configuration cfg, ModelDescription oldOne,
			ModelDescription newOne) {
		Connection conn = null;
		DatabaseMetadata meta = null;
		String defaultCatalog = cfg.getProperties().getProperty(
				Environment.DEFAULT_CATALOG);
		String defaultSchema = cfg.getProperties().getProperty(
				Environment.DEFAULT_SCHEMA);
		List<String> alterSqls = new ArrayList<String>();
		try {
			conn = dataSource.getConnection();
			meta = new DatabaseMetadata(conn, dialect);
			Mapping mapping = cfg.buildMapping();
			// Alter table name;
			if (!oldOne.getName().equals(newOne.getName())) {
				String alterTableName = "alter table " + dialect.openQuote()
						+ oldOne.getName().toUpperCase() + dialect.closeQuote()
						+ "rename to " + dialect.openQuote()
						+ newOne.getName().toUpperCase() + dialect.closeQuote();
				executeSql(conn, alterTableName);
			}

			List<PropertyDescription> oldPropertyDescriptionList = oldOne
					.getPropertyDescriptionList();
			List<PropertyDescription> currentPropertyDescriptionList = newOne
					.getPropertyDescriptionList();
			List<PropertyDescription> removePropertyList = new ArrayList<PropertyDescription>();
			List<PropertyDescription> addPropertyList = new ArrayList<PropertyDescription>(
					currentPropertyDescriptionList);
			Map<PropertyDescription, PropertyDescription> pdMap = new HashMap<PropertyDescription, PropertyDescription>();
			for (PropertyDescription pd1 : oldPropertyDescriptionList) {
				boolean remove = true;
				for (PropertyDescription pd2 : currentPropertyDescriptionList) {
					if (pd1.getId().equals(pd2.getId())) {
						addPropertyList.remove(pd2);
						if (!pd1.equals(pd2)) {
							pdMap.put(pd2, pd1);
						}
						remove = false;
						break;
					}
				}
				if (remove) {
					removePropertyList.add(pd1);
				}
			}

			Iterator<Table> tableIter = cfg.getTableMappings();
			while (tableIter.hasNext()) {
				Table table = tableIter.next();
				TableMetadata tableInfo = meta.getTableMetadata(
						table.getName(),
						(table.getSchema() == null) ? defaultSchema : table
								.getSchema(),
						(table.getCatalog() == null) ? defaultCatalog : table
								.getCatalog(), table.isQuoted()

				);
				if (tableInfo == null) {
					alterSqls.add(table.sqlCreateString(dialect, mapping,
							defaultCatalog, defaultSchema));
				} else {
					if (!table.getName().equalsIgnoreCase(newOne.getName())) {
						continue;
					}
					for (PropertyDescription removeProperty : removePropertyList) {
						if (removeProperty.getControl().isSupportMutil()
								&& removeProperty.getControl().isDynamic()) {
							alterSqls.add("drop table " + dialect.openQuote()
									+ newOne.getName().toUpperCase() + "_"
									+ removeProperty.getName().toUpperCase()
									+ "_RELATION" + dialect.closeQuote());
						} else {
							if (((DialectExtention) dialect)
									.needDropForeignKeyBeforeDropColumn()) {
								ForeignKeyMetadata fkm = tableInfo
										.getForeignKeyMetadataByColumnNames(new String[] { removeProperty
												.getName().toUpperCase() });
								if (fkm != null) {
									alterSqls.add("alter table "
											+ dialect.openQuote()
											+ newOne.getName().toUpperCase()
											+ dialect.closeQuote() + " "
											+ dialect.getDropForeignKeyString()
											+ " " + dialect.openQuote()
											+ fkm.getName()
											+ dialect.closeQuote());
								}
							}
							alterSqls.add("alter table " + dialect.openQuote()
									+ newOne.getName().toUpperCase()
									+ dialect.closeQuote() + " drop column "
									+ dialect.openQuote()
									+ removeProperty.getName().toUpperCase()
									+ dialect.closeQuote());
						}
					}
					StringBuffer root = new StringBuffer("alter table ")
							.append(
									table.getQualifiedName(dialect,
											defaultCatalog, defaultSchema))
							.append(' ').append(dialect.getAddColumnString());
					Iterator<Column> iter = table.getColumnIterator();
					while (iter.hasNext()) {
						Column column = iter.next();
						PropertyDescription pd = null;
						if ((pd = contains(column.getName(), addPropertyList)) != null) {
							StringBuffer alter = new StringBuffer(root
									.toString())
									.append(' ')
									.append(column.getQuotedName(dialect))
									.append(' ')
									.append(column.getSqlType(dialect, mapping));

							String defaultValue = column.getDefaultValue();
							if (defaultValue != null) {
								alter.append(" default ").append(defaultValue);

								if (column.isNullable()) {
									alter.append(dialect.getNullColumnString());
								} else {
									alter.append(" not null");
								}

							}

							boolean useUniqueConstraint = column.isUnique()
									&& dialect.supportsUnique()
									&& (!column.isNullable() || dialect
											.supportsNotNullUnique());
							if (useUniqueConstraint) {
								alter.append(" unique");
							}

							if (column.hasCheckConstraint()
									&& dialect.supportsColumnCheck()) {
								alter.append(" check(").append(
										column.getCheckConstraint())
										.append(")");
							}

							String columnComment = column.getComment();
							if (columnComment != null) {
								alter.append(dialect
										.getColumnComment(columnComment));
							}

							alterSqls.add(alter.toString());
						} else if ((pd = contains(column.getName(), pdMap)) != null) {
							PropertyDescription newPd = pd;
							PropertyDescription oldPd = pdMap.get(pd);
							if (!oldPd.getName().equalsIgnoreCase(
									newPd.getName())) {
								StringBuffer renameColumn = new StringBuffer(
										"alter table ")
										.append(
												table.getQualifiedName(dialect,
														defaultCatalog,
														defaultSchema))
										.append(' ')
										.append(
												((DialectExtention) dialect)
														.getRenameColumnString(
																oldPd
																		.getName()
																		.toUpperCase(),
																newPd
																		.getName()
																		.toUpperCase()));
								if (((DialectExtention) dialect)
										.isAddColumnDefinitionWhenRename()) {
									renameColumn.append(" ");
									renameColumn.append(column.getSqlType(
											dialect, mapping));
								}
								executeSql(conn, renameColumn.toString());
							}

							StringBuffer alterColumn = new StringBuffer(
									"alter table ")
									.append(
											table.getQualifiedName(dialect,
													defaultCatalog,
													defaultSchema))
									.append(' ')
									.append(
											((DialectExtention) dialect)
													.getModifyColumnString(column))
									.append(' ').append(
											column.getQuotedName(dialect));
							alterColumn.append(" ");
							alterColumn.append(column.getSqlType(dialect,
									mapping));
							String defaultValue = column.getDefaultValue();
							if (defaultValue != null) {
								alterColumn.append(" default ").append(
										defaultValue);

								if (column.isNullable()) {
									alterColumn.append(dialect
											.getNullColumnString());
								} else {
									alterColumn.append(" not null");
								}

							}

							boolean useUniqueConstraint = column.isUnique()
									&& dialect.supportsUnique()
									&& (!column.isNullable() || dialect
											.supportsNotNullUnique());
							if (useUniqueConstraint) {
								alterColumn.append(" unique");
							}

							if (column.hasCheckConstraint()
									&& dialect.supportsColumnCheck()) {
								alterColumn.append(" check(").append(
										column.getCheckConstraint())
										.append(")");
							}

							String columnComment = column.getComment();
							if (columnComment != null) {
								alterColumn.append(dialect
										.getColumnComment(columnComment));
							}
							alterSqls.add(alterColumn.toString());
						}
					}
				}
			}
			tableIter = cfg.getTableMappings();
			while (tableIter.hasNext()) {
				Table table = tableIter.next();
				Iterator<ForeignKey> subIter = table.getForeignKeyIterator();
				while (subIter.hasNext()) {
					ForeignKey fk = (ForeignKey) subIter.next();
					if (fk.isPhysicalConstraint()) {
						TableMetadata tableInfo = meta.getTableMetadata(table
								.getName(),
								(table.getSchema() == null) ? defaultSchema
										: table.getSchema(), (table
										.getCatalog() == null) ? defaultCatalog
										: table.getCatalog(), table.isQuoted()

						);
						if (tableInfo == null) {
							String[] cols = new String[fk.getColumnSpan()];
							String[] refcols = new String[fk.getColumnSpan()];
							int i = 0;
							Iterator<Column> refiter = null;
							if (fk.isReferenceToPrimaryKey()) {
								refiter = fk.getReferencedTable()
										.getPrimaryKey().getColumnIterator();
							} else {
								refiter = fk.getReferencedColumns().iterator();
							}

							Iterator<Column> columnIter = fk.getColumnIterator();
							while (columnIter.hasNext()) {
								cols[i] = ((Column) columnIter.next())
										.getQuotedName(dialect);
								refcols[i] = ((Column) refiter.next())
										.getQuotedName(dialect);
								i++;
							}
							String result = dialect
									.getAddForeignKeyConstraintString(fk
											.getName(), cols, fk
											.getReferencedTable()
											.getQualifiedName(dialect,
													defaultCatalog,
													defaultSchema), refcols, fk
											.isReferenceToPrimaryKey());
							StringBuffer createFK = new StringBuffer(
									"alter table ")
									.append(
											table.getQualifiedName(dialect,
													defaultCatalog,
													defaultSchema))
									.append(
											dialect.supportsCascadeDelete() ? result
													+ " on delete cascade"
													: result);
							alterSqls.add(createFK.toString());
						}
					}
				}
			}
			this.executeSqls(conn, alterSqls);
		} catch (Exception e) {
			throw new PlutoException(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e) {
					log.error("", e);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void createTable(Configuration cfg) {
		Connection conn = null;
		DatabaseMetadata meta = null;
		String defaultCatalog = cfg.getProperties().getProperty(
				Environment.DEFAULT_CATALOG);
		String defaultSchema = cfg.getProperties().getProperty(
				Environment.DEFAULT_SCHEMA);
		List<String> createSqls = new ArrayList<String>();
		try {
			conn = dataSource.getConnection();
			meta = new DatabaseMetadata(conn, dialect);
			Iterator<Table> tableIter = cfg.getTableMappings();
			Mapping mapping = cfg.buildMapping();
			while (tableIter.hasNext()) {
				Table table = (Table) tableIter.next();
				TableMetadata tableInfo = meta.getTableMetadata(
						table.getName(),
						(table.getSchema() == null) ? defaultSchema : table
								.getSchema(),
						(table.getCatalog() == null) ? defaultCatalog : table
								.getCatalog(), table.isQuoted()

				);
				if (tableInfo == null) {
					createSqls.add(table.sqlCreateString(dialect, mapping,
							defaultCatalog, defaultSchema));
				}
			}
			tableIter = cfg.getTableMappings();
			while (tableIter.hasNext()) {
				Table table = (Table) tableIter.next();
				Iterator<ForeignKey> subIter = table.getForeignKeyIterator();
				while (subIter.hasNext()) {
					ForeignKey fk = (ForeignKey) subIter.next();
					if (fk.isPhysicalConstraint()) {
						TableMetadata tableInfo = meta.getTableMetadata(table
								.getName(),
								(table.getSchema() == null) ? defaultSchema
										: table.getSchema(), (table
										.getCatalog() == null) ? defaultCatalog
										: table.getCatalog(), table.isQuoted()

						);
						if (tableInfo == null) {
							String[] cols = new String[fk.getColumnSpan()];
							String[] refcols = new String[fk.getColumnSpan()];
							int i = 0;
							Iterator<Column> refiter = null;
							if (fk.isReferenceToPrimaryKey()) {
								refiter = fk.getReferencedTable()
										.getPrimaryKey().getColumnIterator();
							} else {
								refiter = fk.getReferencedColumns().iterator();
							}

							Iterator<Column> columnIter = fk.getColumnIterator();
							while (columnIter.hasNext()) {
								cols[i] = ((Column) columnIter.next())
										.getQuotedName(dialect);
								refcols[i] = ((Column) refiter.next())
										.getQuotedName(dialect);
								i++;
							}
							String result = dialect
									.getAddForeignKeyConstraintString(fk
											.getName(), cols, fk
											.getReferencedTable()
											.getQualifiedName(dialect,
													defaultCatalog,
													defaultSchema), refcols, fk
											.isReferenceToPrimaryKey());
							StringBuffer createFK = new StringBuffer(
									"alter table ")
									.append(
											table.getQualifiedName(dialect,
													defaultCatalog,
													defaultSchema))
									.append(
											dialect.supportsCascadeDelete() ? result
													+ " on delete cascade"
													: result);
							createSqls.add(createFK.toString());
						}
					}
				}
			}
			this.executeSqls(conn, createSqls);
		} catch (DDLException e) {
			throw e;
		} catch (Exception e) {
			throw new PlutoException(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e) {
					log.error("", e);
				}
			}
		}

	}

	public void dropTable(ModelDescription modelDescription) {

		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			List<String> dropSqls = new ArrayList<String>();
			String dropTemplateModelRelationTable = "drop table "
					+ dialect.openQuote() + "TEMPLATE_"
					+ modelDescription.getName().toUpperCase() + "_SITE_RELATION"
					+ dialect.closeQuote();
			dropSqls.add(dropTemplateModelRelationTable);
			for (PropertyDescription pd : modelDescription
					.getPropertyDescriptionList()) {
				Type type = pd.getType();
				if (type instanceof CustomListType) {
					String dropModelModelRelationTable = "drop table "
							+ dialect.openQuote()
							+ modelDescription.getName().toUpperCase() + "_"
							+ pd.getName().toUpperCase() + "_RELATION"
							+ dialect.closeQuote();
					dropSqls.add(dropModelModelRelationTable);
				}
			}
			String dropModelTable = "drop table " + dialect.openQuote()
					+ modelDescription.getName().toUpperCase()
					+ dialect.closeQuote();
			dropSqls.add(dropModelTable);
			this.executeSqls(conn, dropSqls);
		} catch (DDLException e) {
			throw e;
		} catch (Exception e) {
			throw new PlutoException(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e) {
					log.error("", e);
				}
			}
		}
	}

	private PropertyDescription contains(String columnName,
			List<PropertyDescription> propertyDescriptionList) {
		for (PropertyDescription pd : propertyDescriptionList) {
			if (pd.getName().equalsIgnoreCase(columnName)) {
				return pd;
			}
		}
		return null;
	}

	private PropertyDescription contains(String columnName,
			Map<PropertyDescription, PropertyDescription> pdMap) {
		for (PropertyDescription pd : pdMap.keySet()) {
			if (pd.getName().equalsIgnoreCase(columnName)) {
				return pd;
			}
		}
		return null;
	}

	private void executeSqls(Connection conn, List<String> sqlList) {
		for (String sql : sqlList) {
			executeSql(conn, sql);
		}
	}

	private void executeSql(Connection conn, String sql) {
		Statement stat = null;
		try {
			stat = conn.createStatement();
			stat.execute(sql);
		} catch (Exception e) {
			throw new DDLException(sql, e);
		} finally {
			if (stat != null) {
				try {
					stat.close();
				} catch (Exception e) {
					log.error("", e);
				}
			}

		}
	}
}

// $Id$