package com.ikidstv.quiz.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.ikidstv.quiz.dao.QuizDao;
import com.ikidstv.quiz.enumeration.Device;
import com.ikidstv.quiz.enumeration.QuizStatus;
import com.ikidstv.quiz.enumeration.TemplateId;
import com.ikidstv.quiz.model.Metadata;
import com.ikidstv.quiz.model.Quiz;

public class QuizDaoImpl extends HibernateDaoSupport implements QuizDao {

	@SuppressWarnings("unchecked")
	public List<Quiz> findQuizByUserId(Long userId) {
		List<Quiz> result = this.getHibernateTemplate().findByNamedParam(
				"from Quiz where user.id = :userId and placementTest = false",
				"userId", userId);
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<Quiz> findAuditingQuiz() {
		List<Quiz> result = this.getHibernateTemplate().findByNamedParam(
				"from Quiz where (status = :status1 or status = :status2)  and placementTest = false",
				new String[]{"status1","status2"}, new Object[]{QuizStatus.AUDITING.getStatus(),QuizStatus.PUBLISHED.getStatus()});
		return result;
	}

	public void saveQuiz(Quiz quiz) {
		this.getHibernateTemplate().saveOrUpdate(quiz);
	}

	public void saveMetadata(Metadata metadata) {
		this.getHibernateTemplate().saveOrUpdate(metadata);
	}

	public Quiz findQuizById(Long id) {
		return (Quiz) this.getHibernateTemplate().get(Quiz.class, id);
	}

	public Metadata getMetadata(TemplateId templateId, Long metadataId) {
		return (Metadata) this.getHibernateTemplate().get(
				templateId.getClazz(), metadataId);
	}

	public void deleteQuiz(Quiz quiz) {
		this.getHibernateTemplate().delete(quiz);
	}

	public void deleteMetadata(Metadata metadata) {
		this.getHibernateTemplate().delete(metadata);
	}

	public void deleteQuizLearningPointRel(final Quiz quiz) {
		this.getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session
						.createQuery("delete QuizLearningPointRel where quiz.id = :quizId");
				query.setParameter("quizId", quiz.getId());
				return query.executeUpdate();
			}
		});
	}

	@SuppressWarnings("unchecked")
	public List<Quiz> findPlacementQuiz() {
		List<Quiz> result = this.getHibernateTemplate().find(
				"from Quiz where placementTest = true");
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<Quiz> findQuizBySeason(String seasonId,String level, Device device) {
		String hsql = "from Quiz where status=3 and seasonId = :seasonId and level = :level";
		if (device == Device.IPhone) {
			hsql += " and template.iphone = true";
		} else {
			hsql += " and template.ipad = true";
		}
		List<Quiz> result = this.getHibernateTemplate().findByNamedParam(hsql,
				new String[] { "seasonId", level },
				new Object[] { seasonId, level });
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<Quiz> findQuizByEpisode(String episodeId, String level, Device device) {
		String hsql = "from Quiz where status=3 and episodeId = :episodeId and level = :level";
		if (device == Device.IPhone) {
			hsql += " and template.iphone = true";
		} else {
			hsql += " and template.ipad = true";
		}
		List<Quiz> result = this.getHibernateTemplate().findByNamedParam(hsql,
				new String[] { "episodeId", "level"},
				new Object[] { episodeId, level});
		return result;
	}

}
