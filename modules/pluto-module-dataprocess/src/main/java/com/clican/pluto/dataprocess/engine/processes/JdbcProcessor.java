/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.dataprocess.engine.processes;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import com.clican.pluto.common.support.spring.BeanPropertyRowMapper;
import com.clican.pluto.common.support.spring.MapAndNestedPropertySqlParameterSource;
import com.clican.pluto.dataprocess.bean.JdbcExecBean;
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.engine.impl.BaseDataProcessor;
import com.clican.pluto.dataprocess.exception.DataProcessException;

/**
 * 用来处理sql的processor
 * 
 * @author clican
 * 
 */
public class JdbcProcessor extends BaseDataProcessor {

	/**
	 * 需要处理的Jdbc Sql的描述类
	 */
	private List<JdbcExecBean> jdbcExecBeanList;

	private NamedParameterJdbcOperations jdbcTemplate;

	public void setJdbcTemplate(NamedParameterJdbcOperations jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public void setJdbcExecBeanList(List<JdbcExecBean> jdbcExecBeanList) {
		this.jdbcExecBeanList = jdbcExecBeanList;
	}

	@SuppressWarnings({"rawtypes","unchecked"}) 
	public void process(ProcessorContext context) throws DataProcessException {
		// 遍历处理每个jdbcExecBean
		for (JdbcExecBean jdbcExecBean : jdbcExecBeanList) {
			String sql = jdbcExecBean.getSql().trim();
			// 如果sql是空的话则说明都不处理
			if (StringUtils.isEmpty(sql)) {
				log.warn("The sql is null, we ignore it");
				return;
			}
			boolean singleRow = jdbcExecBean.isSingleRow();
			Class<?> clazz = jdbcExecBean.getClazz();
			// 识别是select,insert,update,delete
			String command = sql.toLowerCase().substring(0, 6);
			// 返回参数
			Object temp = jdbcExecBean.getParam(context);
			Map<String, Object> param;
			if (temp instanceof Map) {
				param = (Map<String, Object>) temp;
			} else if (StringUtils.isNotEmpty(jdbcExecBean.getParamName())) {
				param = Collections.singletonMap(jdbcExecBean.getParamName(), temp);
			} else {
				throw new DataProcessException("参数错误");
			}
			if (command.equals("select")) {
				List<?> list;
				if (clazz == null) {
					list = jdbcTemplate.queryForList(sql, new MapAndNestedPropertySqlParameterSource(param));
				} else {
					if (clazz.isAssignableFrom(Date.class) || clazz.equals(Long.class) || clazz.equals(Integer.class) || clazz.equals(Float.class)
							|| clazz.equals(Double.class) || clazz.equals(String.class)) {
						list = jdbcTemplate.query(sql, new MapAndNestedPropertySqlParameterSource(param), new SingleColumnRowMapper(clazz) {

							
							protected Object getColumnValue(ResultSet rs, int index, Class requiredType) throws SQLException {
								Object obj = super.getColumnValue(rs, index, requiredType);
								if (obj instanceof Date) {
									return new Date(((Date) obj).getTime());
								} else {
									return obj;
								}
							}

							
							protected Object getColumnValue(ResultSet rs, int index) throws SQLException {
								Object obj = super.getColumnValue(rs, index);
								if (obj instanceof Date) {
									return new Date(((Date) obj).getTime());
								} else {
									return obj;
								}
							}

						});
					} else {
						list = jdbcTemplate.query(sql, new MapAndNestedPropertySqlParameterSource(param), new BeanPropertyRowMapper(clazz));
					}
				}
				if (log.isDebugEnabled()) {
					log.debug("jdbc set attribute[" + jdbcExecBean.getResultName() + "],size=" + list.size());
				}
				if (singleRow) {
					if (list.size() == 0) {
						context.setAttribute(jdbcExecBean.getResultName(), null);
					} else if (list.size() == 1) {
						context.setAttribute(jdbcExecBean.getResultName(), list.get(0));
					} else {
						throw new DataProcessException("对于单条查询得到多条记录");
					}
				} else {
					context.setAttribute(jdbcExecBean.getResultName(), list);
				}
			} else if (command.equals("insert") || command.equals("update") || command.equals("delete") || command.equals("trunca")) {
				if (sql.contains(";")) {
					String updateSql = sql.split(";")[0];
					String insertSql = sql.split(";")[1];
					int row = jdbcTemplate.update(updateSql, param);
					if (row == 0) {
						row = jdbcTemplate.update(insertSql, param);
					}
				} else {
					if (jdbcExecBean.isBatch() && temp instanceof List) {
						List tempList = (List) temp;
						SqlParameterSource[] sources = new SqlParameterSource[tempList.size()];
						for (int i = 0; i < tempList.size(); i++) {
							Object obj = tempList.get(i);
							SqlParameterSource namedParameters;
							if (obj instanceof Map) {
								namedParameters = new MapAndNestedPropertySqlParameterSource((Map) obj);
							} else {
								namedParameters = new BeanPropertySqlParameterSource(obj);
							}
							sources[i] = namedParameters;
						}
						new SimpleJdbcTemplate(jdbcTemplate.getJdbcOperations()).batchUpdate(sql, sources);
					} else {
						SqlParameterSource namedParameters;
						if (param instanceof Map) {
							namedParameters = new MapAndNestedPropertySqlParameterSource((Map) param);
						} else {
							namedParameters = new BeanPropertySqlParameterSource(param);
						}
						int row = jdbcTemplate.update(sql, namedParameters);
						if (log.isDebugEnabled()) {
							log.debug(command + "=" + row + " row");
						}
					}
				}
			} else {
				throw new DataProcessException("sql错误");
			}
		}
	}

}

// $Id: JdbcProcessor.java 16239 2010-07-16 06:03:12Z wei.zhang $