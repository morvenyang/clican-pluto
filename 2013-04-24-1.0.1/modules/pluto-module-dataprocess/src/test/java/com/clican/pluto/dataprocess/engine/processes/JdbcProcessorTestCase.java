/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.dataprocess.engine.processes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.api.Invocation;
import org.jmock.lib.action.CustomAction;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

import com.clican.pluto.common.support.spring.BeanPropertyRowMapper;
import com.clican.pluto.common.support.spring.MapAndNestedPropertySqlParameterSource;
import com.clican.pluto.dataprocess.bean.JdbcExecBean;
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.engine.impl.ProcessorContextImpl;
import com.clican.pluto.dataprocess.engine.processes.JdbcProcessor;
import com.clican.pluto.dataprocess.testbean.BeanA;

public class JdbcProcessorTestCase extends TestCase {

	private Mockery context = new Mockery();

	public void testSelect1() throws Exception {
		JdbcProcessor p = new JdbcProcessor();
		final NamedParameterJdbcOperations jdbcTemplate = context.mock(NamedParameterJdbcOperations.class);
		p.setJdbcTemplate(jdbcTemplate);
		final JdbcExecBean bean = new JdbcExecBean();
		bean.setSql("select * from Test");
		bean.setSingleRow(false);
		bean.setResultName("testList");
		List<JdbcExecBean> beanList = new ArrayList<JdbcExecBean>();
		beanList.add(bean);
		p.setJdbcExecBeanList(beanList);
		ProcessorContext ctx = new ProcessorContextImpl();
		context.checking(new Expectations() {
			{
				one(jdbcTemplate).queryForList(with(equal(bean.getSql())),
						(MapAndNestedPropertySqlParameterSource) with(a(MapAndNestedPropertySqlParameterSource.class)));
				this.will(new CustomAction("") {
					public Object invoke(Invocation invocation) throws Throwable {
						return new ArrayList<Object>();
					}
				});
			}
		});
		p.process(ctx);
	}

	public void testSelect2() throws Exception {
		JdbcProcessor p = new JdbcProcessor();
		final NamedParameterJdbcOperations jdbcTemplate = context.mock(NamedParameterJdbcOperations.class);
		p.setJdbcTemplate(jdbcTemplate);
		final JdbcExecBean bean = new JdbcExecBean();
		bean.setSql("select * from Test");
		bean.setSingleRow(false);
		bean.setResultName("testList");
		bean.setClazz(Date.class);
		List<JdbcExecBean> beanList = new ArrayList<JdbcExecBean>();
		beanList.add(bean);
		p.setJdbcExecBeanList(beanList);
		ProcessorContext ctx = new ProcessorContextImpl();
		context.checking(new Expectations() {
			{
				one(jdbcTemplate).query(with(equal(bean.getSql())),
						(MapAndNestedPropertySqlParameterSource) with(a(MapAndNestedPropertySqlParameterSource.class)),
						(SingleColumnRowMapper) with(a(SingleColumnRowMapper.class)));
				this.will(new CustomAction("") {
					public Object invoke(Invocation invocation) throws Throwable {
						List<Date> result = new ArrayList<Date>();
						result.add(new Date());
						result.add(new Date());
						return result;
					}
				});
			}
		});
		p.process(ctx);
	}

	public void testSelect3() throws Exception {
		JdbcProcessor p = new JdbcProcessor();
		final NamedParameterJdbcOperations jdbcTemplate = context.mock(NamedParameterJdbcOperations.class);
		p.setJdbcTemplate(jdbcTemplate);
		final JdbcExecBean bean = new JdbcExecBean();
		bean.setSql("select * from Test");
		bean.setSingleRow(false);
		bean.setResultName("testList");
		bean.setClazz(BeanA.class);
		List<JdbcExecBean> beanList = new ArrayList<JdbcExecBean>();
		beanList.add(bean);
		p.setJdbcExecBeanList(beanList);
		ProcessorContext ctx = new ProcessorContextImpl();
		context.checking(new Expectations() {
			{
				one(jdbcTemplate).query(with(equal(bean.getSql())),
						(MapAndNestedPropertySqlParameterSource) with(a(MapAndNestedPropertySqlParameterSource.class)),
						(BeanPropertyRowMapper) with(a(BeanPropertyRowMapper.class)));
				this.will(new CustomAction("") {
					public Object invoke(Invocation invocation) throws Throwable {
						List<BeanA> result = new ArrayList<BeanA>();
						result.add(new BeanA());
						result.add(new BeanA());
						return result;
					}
				});
			}
		});
		p.process(ctx);
	}

	public void testSelect4() throws Exception {
		JdbcProcessor p = new JdbcProcessor();
		final NamedParameterJdbcOperations jdbcTemplate = context.mock(NamedParameterJdbcOperations.class);
		p.setJdbcTemplate(jdbcTemplate);
		final JdbcExecBean bean = new JdbcExecBean();
		bean.setSql("select * from Test");
		bean.setSingleRow(true);
		bean.setResultName("testList");
		List<JdbcExecBean> beanList = new ArrayList<JdbcExecBean>();
		beanList.add(bean);
		p.setJdbcExecBeanList(beanList);
		ProcessorContext ctx = new ProcessorContextImpl();
		context.checking(new Expectations() {
			{
				one(jdbcTemplate).queryForList(with(equal(bean.getSql())),
						(MapAndNestedPropertySqlParameterSource) with(a(MapAndNestedPropertySqlParameterSource.class)));
				this.will(new CustomAction("") {
					public Object invoke(Invocation invocation) throws Throwable {
						return new ArrayList<Object>();
					}
				});
			}
		});
		p.process(ctx);
	}

	public void testSelect5() throws Exception {
		JdbcProcessor p = new JdbcProcessor();
		final NamedParameterJdbcOperations jdbcTemplate = context.mock(NamedParameterJdbcOperations.class);
		p.setJdbcTemplate(jdbcTemplate);
		final JdbcExecBean bean = new JdbcExecBean();
		bean.setSql("select * from Test");
		bean.setSingleRow(true);
		bean.setResultName("testList");
		List<JdbcExecBean> beanList = new ArrayList<JdbcExecBean>();
		beanList.add(bean);
		p.setJdbcExecBeanList(beanList);
		ProcessorContext ctx = new ProcessorContextImpl();
		context.checking(new Expectations() {
			{
				one(jdbcTemplate).queryForList(with(equal(bean.getSql())),
						(MapAndNestedPropertySqlParameterSource) with(a(MapAndNestedPropertySqlParameterSource.class)));
				this.will(new CustomAction("") {
					public Object invoke(Invocation invocation) throws Throwable {
						List<BeanA> result = new ArrayList<BeanA>();
						result.add(new BeanA());
						return result;
					}
				});
			}
		});
		p.process(ctx);
	}

	public void testInsert1() throws Exception {
		JdbcProcessor p = new JdbcProcessor();
		final NamedParameterJdbcOperations jdbcTemplate = context.mock(NamedParameterJdbcOperations.class);
		final JdbcOperations jdbcOperations = context.mock(JdbcOperations.class);
		p.setJdbcTemplate(jdbcTemplate);
		final JdbcExecBean bean = new JdbcExecBean();
		bean.setSql("insert into ...");
		bean.setBatch(true);
		bean.setParamName("list");
		List<JdbcExecBean> beanList = new ArrayList<JdbcExecBean>();
		beanList.add(bean);
		p.setJdbcExecBeanList(beanList);
		ProcessorContext ctx = getContext();

		context.checking(new Expectations() {
			{
				one(jdbcTemplate).getJdbcOperations();
				will(new CustomAction("") {
					public Object invoke(Invocation invocation) throws Throwable {
						return jdbcOperations;
					}
				});
				one(jdbcOperations).batchUpdate(with(equal(bean.getSql())), (BatchPreparedStatementSetter) with(a(BatchPreparedStatementSetter.class)));
			}
		});
		p.process(ctx);
	}

	public void testInsert2() throws Exception {
		JdbcProcessor p = new JdbcProcessor();
		final NamedParameterJdbcOperations jdbcTemplate = context.mock(NamedParameterJdbcOperations.class);
		p.setJdbcTemplate(jdbcTemplate);
		final JdbcExecBean bean = new JdbcExecBean();
		bean.setSql("insert into ...");
		bean.setBatch(false);
		bean.setParamName("list");
		List<JdbcExecBean> beanList = new ArrayList<JdbcExecBean>();
		beanList.add(bean);
		p.setJdbcExecBeanList(beanList);
		ProcessorContext ctx = getContext();

		context.checking(new Expectations() {
			{
				one(jdbcTemplate).update(with(equal(bean.getSql())),
						(MapAndNestedPropertySqlParameterSource) with(a(MapAndNestedPropertySqlParameterSource.class)));
				will(new CustomAction("") {
					public Object invoke(Invocation invocation) throws Throwable {
						return 1;
					}
				});
			}
		});
		p.process(ctx);
	}

	public void testInsertOrUpdate() throws Exception {
		JdbcProcessor p = new JdbcProcessor();
		final NamedParameterJdbcOperations jdbcTemplate = context.mock(NamedParameterJdbcOperations.class);
		p.setJdbcTemplate(jdbcTemplate);
		final JdbcExecBean bean = new JdbcExecBean();
		bean.setSql("update ...;insert into ...");
		bean.setBatch(false);
		bean.setParamName("list");
		List<JdbcExecBean> beanList = new ArrayList<JdbcExecBean>();
		beanList.add(bean);
		p.setJdbcExecBeanList(beanList);
		ProcessorContext ctx = getContext();

		context.checking(new Expectations() {
			{
				one(jdbcTemplate).update((String) with(anything()), (Map<?,?>) with(anything()));
				will(new CustomAction("") {
					public Object invoke(Invocation invocation) throws Throwable {
						return 0;
					}
				});
				one(jdbcTemplate).update((String) with(anything()), (Map<?,?>) with(anything()));
			}
		});
		p.process(ctx);
	}

	private ProcessorContext getContext() throws Exception {
		List<BeanA> list = new ArrayList<BeanA>();
		for (int i = 0; i < 10; i++) {
			BeanA test = new BeanA();
			test.setId(i);
			test.setName("" + i);
			list.add(test);
		}
		ProcessorContext context = new ProcessorContextImpl();
		context.setAttribute("list", list);
		return context;
	}
}

// $Id$