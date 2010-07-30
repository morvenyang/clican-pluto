/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.dataprocess.enumeration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.clican.pluto.dataprocess.bean.IBatisExecBean;
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.exception.DataProcessSqlException;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * IBatis处理操作的枚举类型
 * 
 * @author clican, jing.tian
 * 
 */
public enum IBatisExecType {

	/**
	 * 向数据库插数据
	 */
	INSERT {

		@SuppressWarnings("unchecked")
		
		public void execute(SqlMapClient sqlMapClient, IBatisExecBean execBean, ProcessorContext context) throws DataProcessSqlException {
			Object param = execBean.getParam(context);
			try {
				if (param != null && param instanceof Collection && execBean.isBatch()) {
					Collection coll = (Collection) param;
					for (Object obj : coll) {
						sqlMapClient.insert(execBean.getStatement(), obj);
					}
				} else {
					sqlMapClient.insert(execBean.getStatement(), param);
				}
			} catch (Exception ex) {
				throw new DataProcessSqlException("insert data error[" + execBean.getStatement() + "]", ex);
			}
		}
	},

	/**
	 * 更新数据
	 */
	UPDATE {
		@SuppressWarnings("unchecked")
		
		public void execute(SqlMapClient sqlMapClient, IBatisExecBean execBean, ProcessorContext context) throws DataProcessSqlException {
			Object param = execBean.getParam(context);
			try {
				if (param != null && param instanceof Collection && execBean.isBatch()) {
					Collection coll = (Collection) param;
					for (Object obj : coll) {
						sqlMapClient.update(execBean.getStatement(), obj);
					}
				} else {
					sqlMapClient.update(execBean.getStatement(), param);
				}
			} catch (Exception ex) {
				throw new DataProcessSqlException("update data error[" + execBean.getStatement() + "]", ex);
			}
		}
	},

	INSERT_OR_UPDATE {
		@SuppressWarnings("unchecked")
		
		public void execute(SqlMapClient sqlMapClient, IBatisExecBean execBean, ProcessorContext context) throws DataProcessSqlException {
			Object param = execBean.getParam(context);
			try {
				if (param != null && param instanceof Collection && execBean.isBatch()) {
					Collection coll = (Collection) param;
					for (Object obj : coll) {
						int updateRow = sqlMapClient.update(execBean.getUpdateStatement(), obj);
						if (updateRow == 0) {
							sqlMapClient.insert(execBean.getInsertStatement(), obj);
						}
					}
				} else {
					int updateRow = sqlMapClient.update(execBean.getUpdateStatement(), param);
					if (updateRow == 0) {
						sqlMapClient.insert(execBean.getInsertStatement(), param);
					}
				}
			} catch (Exception ex) {
				throw new DataProcessSqlException("insert or update data error[" + execBean.getInsertStatement() + "," + execBean.getUpdateStatement() + "]",
						ex);
			}
		}
	},
	/**
	 * 删除数据
	 */
	DELETE {
		@SuppressWarnings("unchecked")
		
		public void execute(SqlMapClient sqlMapClient, IBatisExecBean execBean, ProcessorContext context) throws DataProcessSqlException {
			Object param = execBean.getParam(context);
			try {
				if (param != null && param instanceof Collection && execBean.isBatch()) {
					Collection coll = (Collection) param;
					for (Object obj : coll) {
						int row = sqlMapClient.delete(execBean.getStatement(), obj);
						log.debug("delete row=" + row);
					}
				} else {
					int row = sqlMapClient.delete(execBean.getStatement(), param);
					log.debug("delete row=" + row);
				}
			} catch (Exception ex) {
				throw new DataProcessSqlException("delete data error[" + execBean.getStatement() + "]", ex);
			}
		}
	},

	/**
	 * 查询一组数据
	 */
	SELECT_LIST {
		@SuppressWarnings("unchecked")
		
		public void execute(SqlMapClient sqlMapClient, IBatisExecBean execBean, ProcessorContext context) throws DataProcessSqlException {
			Object param = execBean.getParam(context);
			try {
				List result = sqlMapClient.queryForList(execBean.getStatement(), param);
				context.setAttribute(execBean.getResultName(), result);
			} catch (Exception ex) {
				throw new DataProcessSqlException("select list error[" + execBean.getStatement() + "]with param", ex);
			}
		}
	},

	/**
	 * 查询一组数据，转换到MAP返回。
	 */
	SELECT_LIST_AS_MAP {
		@SuppressWarnings("unchecked")
		private Map convertBeanAsMap(Object bean) {
			BeanMap beanMap = new BeanMap(bean);
			Map ret = new HashMap();
			ret.putAll(beanMap);
			return ret;
		};

		@SuppressWarnings("unchecked")
		
		public void execute(SqlMapClient sqlMapClient, IBatisExecBean execBean, ProcessorContext context) throws DataProcessSqlException {
			Object param = execBean.getParam(context);
			try {
				List result = sqlMapClient.queryForList(execBean.getStatement(), param);

				List<Map> resultAsMap = new ArrayList<Map>(result.size());
				for (Object o : result) {
					resultAsMap.add(convertBeanAsMap(o));
				}

				context.setAttribute(execBean.getResultName(), resultAsMap);
			} catch (Exception ex) {
				throw new DataProcessSqlException("select list error[" + execBean.getStatement() + "]with param", ex);
			}
		}
	},

	/**
	 * 查询单个数据
	 */
	SELECT_OBJECT {
		
		public void execute(SqlMapClient sqlMapClient, IBatisExecBean execBean, ProcessorContext context) throws DataProcessSqlException {
			Object param = execBean.getParam(context);
			try {
				Object result = sqlMapClient.queryForObject(execBean.getStatement(), param);
				context.setAttribute(execBean.getResultName(), result);
			} catch (Exception ex) {
				throw new DataProcessSqlException("select object error[" + execBean.getStatement() + "]with param", ex);
			}
		}
	},

	/**
	 * 把查询得到的单个数据转换成一个Map
	 */
	SELECT_MAP {
		@SuppressWarnings("unchecked")
		
		public void execute(SqlMapClient sqlMapClient, IBatisExecBean execBean, ProcessorContext context) throws DataProcessSqlException {
			Object param = execBean.getParam(context);
			try {
				Map result = sqlMapClient.queryForMap(execBean.getStatement(), param, execBean.getKeyProp(), execBean.getValueProp());
				context.setAttribute(execBean.getResultName(), result);
			} catch (Exception ex) {
				throw new DataProcessSqlException("select map error[" + execBean.getStatement() + "]with param", ex);
			}
		}
	};

	public static IBatisExecType convert(String expr) {
		for (IBatisExecType member : values()) {
			if (member.name().equalsIgnoreCase(expr)) {
				return member;
			}
		}
		return null;
	}

	private final static Log log = LogFactory.getLog(IBatisExecType.class);

	public abstract void execute(SqlMapClient sqlMapClient, IBatisExecBean execBean, ProcessorContext context) throws DataProcessSqlException;
}

// $Id: IBatisExecType.java 14238 2010-06-06 07:57:12Z wei.zhang $