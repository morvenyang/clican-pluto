/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.dataprocess.bean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.enumeration.IBatisExecType;
import com.clican.pluto.dataprocess.exception.DataProcessSqlException;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * 用来描述调用IBatis的类
 * 
 * @author clican, jing.tian
 * 
 */
public class IBatisExecBean extends ExecBean {

	private Log logger = LogFactory.getLog(getClass());
	/**
	 * 被调用的sql的名称
	 */
	private String statement;

	/**
	 * 被调用的insert sql的名称
	 */
	private String insertStatement;

	/**
	 * 被调用的update sql的名称
	 */
	private String updateStatement;

	/**
	 * 在IBatis中区分为insert,update,delete和select
	 */
	private IBatisExecType ibatisExecType;

	/**
	 * 如果从IBatis中查找一个Map对象的话，则该字符串表示了Map中key的属性名
	 */
	private String keyProp;

	/**
	 * 如果从IBatis中查找一个Map对象的话，则该字符串表示了Map中value的属性名
	 */
	private String valueProp;

	public String getStatement() {
		return statement;
	}

	public void setStatement(String statement) {
		this.statement = statement;
	}

	public IBatisExecType getIbatisExecType() {
		return ibatisExecType;
	}

	public void setIbatisExecType(IBatisExecType ibatisExecType) {
		this.ibatisExecType = ibatisExecType;
	}

	public void setIbatisExecTypeExpr(String ibatisExecTypeExpr) {
		this.ibatisExecType = IBatisExecType.convert(ibatisExecTypeExpr);
	}

	public String getKeyProp() {
		return keyProp;
	}

	public void setKeyProp(String keyProp) {
		this.keyProp = keyProp;
	}

	public String getValueProp() {
		return valueProp;
	}

	public void setValueProp(String valueProp) {
		this.valueProp = valueProp;
	}

	public String getInsertStatement() {
		return insertStatement;
	}

	public void setInsertStatement(String insertStatement) {
		this.insertStatement = insertStatement;
	}

	public String getUpdateStatement() {
		return updateStatement;
	}

	public void setUpdateStatement(String updateStatement) {
		this.updateStatement = updateStatement;
	}

	/**
	 * 执行ibatis的statement
	 * 
	 * @param sqlMapClient
	 *            ibatis的<code>SqlMapClient</code>
	 * @param context
	 *            执行的上下文对象
	 * @throws DataProcessSqlException
	 *             如果sql执行过程中出错则抛出该错误
	 */
	public void execute(SqlMapClient sqlMapClient, ProcessorContext context) throws DataProcessSqlException {
		try {
			logger.trace("start execution of statement:  " + getStatement());
			this.ibatisExecType.execute(sqlMapClient, this, context);
		} finally {
			logger.trace("finish execution of statement: " + getStatement());
		}
	}
}

// $Id: IBatisExecBean.java 16255 2010-07-16 08:31:23Z wei.zhang $