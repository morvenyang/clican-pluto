/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wezhang
 *
 */
package com.clican.pluto.cms.dao.hibernate;

import java.sql.SQLException;
import java.util.List;

import javax.persistence.Entity;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.clican.pluto.cms.dao.TemplateDao;
import com.clican.pluto.orm.annotation.DynamicModel;
import com.clican.pluto.orm.desc.ModelDescription;
import com.clican.pluto.orm.dynamic.inter.IDataModel;
import com.clican.pluto.orm.dynamic.inter.IDirectory;
import com.clican.pluto.orm.dynamic.inter.ITemplate;
import com.clican.pluto.orm.dynamic.inter.ITemplateModelSiteRelation;

public class TemplateDaoHibernateImpl extends BaseDao implements TemplateDao {

	@SuppressWarnings("unchecked")
	public List<ITemplate> getTemplates() {
		return getHibernateTemplate().find("from Template");
	}

	@SuppressWarnings("unchecked")
	public List<ITemplate> getSelectedTemplates(IDataModel dataModel) {
		String hsql;
		if (!(dataModel instanceof IDirectory)) {
			String modelName = dataModel.getClass().getAnnotation(Entity.class).name();
			StringBuffer sql = new StringBuffer();
			sql.append("select t from ");
			sql.append(modelName);
			sql.append(" m,");
			sql.append("Template t,");
			sql.append("Template");
			sql.append(modelName);
			sql.append("Relation r where r.template=t and r.dataModel=m and m.id = :id");
			hsql = sql.toString();
		} else {
			hsql = "select t from Directory d,Template t, TemplateDirectoryRelation r where r.template=t and r.directory=d and d.id = :id";
		}
		return getHibernateTemplate().findByNamedParam(hsql, "id", dataModel.getId());
	}

	public void deleteTemplateSiteRelation(final IDataModel dataModel) {
		final String hsql;
		if (!(dataModel instanceof IDirectory)) {
			String modelName = dataModel.getClass().getAnnotation(Entity.class).name();
			StringBuffer sql = new StringBuffer();
			sql.append("delete from ");
			sql.append("Template");
			sql.append(modelName);
			sql.append("SiteRelation r where r.dataModel.id = :id");
			hsql = sql.toString();
		} else {
			hsql = "delete from TemplateDirectorySiteRelation r where r.directory.id= :id";
		}
		getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createQuery(hsql);
				query.setParameter("id", dataModel.getId());
				int row = query.executeUpdate();
				return row;
			}

		});
	}

	@SuppressWarnings("unchecked")
	public List<ITemplateModelSiteRelation> getTemplateModelSiteRelations(
			IDataModel dataModel) {
		String modelName = dataModel.getClass().getAnnotation(DynamicModel.class).name();
		StringBuffer sql = new StringBuffer();
		sql.append("select r from ");
		sql.append(modelName);
		sql.append(" m,");
		sql.append("Template t,");
		sql.append("Template");
		sql.append(modelName);
		sql.append("SiteRelation r where r.template=t and r.dataModel=m and m.id = ");
		sql.append(dataModel.getId());
		return getHibernateTemplate().find(sql.toString());
	}

	@SuppressWarnings("unchecked")
	public List<ITemplateModelSiteRelation> getTemplateModelSiteRelations(List<IDataModel> dataModels, ModelDescription modelDescription) {
		String modelName = modelDescription.getFirstCharUpperName();
		StringBuffer sql = new StringBuffer();
		sql.append("select r from ");
		sql.append(modelName);
		sql.append(" m,");
		sql.append("Template t,");
		sql.append("Template");
		sql.append(modelName);
		sql.append("SiteRelation r where r.template=t and r.dataModel=m and m.id in ");
		sql.append(getInString(dataModels));
		return getHibernateTemplate().find(sql.toString());
	}

	@SuppressWarnings("unchecked")
	public List<ITemplateModelSiteRelation> getTemplateModelSiteRelations(ModelDescription modelDescription, final String pathExpression, final int firstResult,
			final int maxResults) {
		String modelName = modelDescription.getFirstCharUpperName();
		final StringBuffer sql = new StringBuffer();
		sql.append("select r from ");
		sql.append(modelName);
		sql.append(" m,");
		sql.append("Template t,");
		sql.append("Template");
		sql.append(modelName);
		sql.append("SiteRelation r where r.template=t and r.dataModel=m and m.parent.path like :pathExpression");
		return getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createQuery(sql.toString());
				query.setFirstResult(firstResult);
				query.setMaxResults(maxResults);
				query.setParameter("pathExpression", pathExpression);
				return query.list();
			}
		});
	}

	public int getTemplateModelSiteRelationCount(ModelDescription modelDescription, String pathExpression) {
		String modelName = modelDescription.getFirstCharUpperName();
		StringBuffer sql = new StringBuffer();
		sql.append("select count(r.*) from ");
		sql.append(modelName);
		sql.append(" m,");
		sql.append("Template t,");
		sql.append("Template");
		sql.append(modelName);
		sql.append("SiteRelation r where r.template=t and r.dataModel=m and m.parent.path like :pathExpression");
		return (Integer) getHibernateTemplate().findByNamedParam(sql.toString(), "pathExpression", "pathExpression").get(0);
	}

}

// $Id$