package com.ikidstv.quiz.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.ikidstv.quiz.dao.QuizDao;
import com.ikidstv.quiz.enumeration.QuizStatus;
import com.ikidstv.quiz.enumeration.TemplateId;
import com.ikidstv.quiz.model.Metadata;
import com.ikidstv.quiz.model.Quiz;

public class QuizDaoImpl extends HibernateDaoSupport implements QuizDao {

	@SuppressWarnings("unchecked")
	public List<Quiz> findQuizByUserId(Long userId) {
		List<Quiz> result = this.getHibernateTemplate().findByNamedParam( "from Quiz where user.id = :userId", "userId", userId);
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public List<Quiz> findAuditingQuiz() {
		List<Quiz> result = this.getHibernateTemplate().findByNamedParam( "from Quiz where status = :status", "status", QuizStatus.AUDITING.getStatus());
		return result;
	}

	public void saveQuiz(Quiz quiz) {
		this.getHibernateTemplate().saveOrUpdate(quiz);
	}

	public void saveMetadata(Metadata metadata) {
		this.getHibernateTemplate().saveOrUpdate(metadata);
	}

	public Quiz findQuizById(Long id) {
		return (Quiz)this.getHibernateTemplate().get(Quiz.class, id);
	}

	public Metadata getMetadata(TemplateId templateId, Long metadataId) {
		return (Metadata)this.getHibernateTemplate().get(templateId.getClazz(), metadataId);
	}

	public void deleteQuiz(Quiz quiz) {
		this.getHibernateTemplate().delete(quiz);
	}

	public void deleteMetadata(Metadata metadata) {
		this.getHibernateTemplate().delete(metadata);
	}

	public void deleteQuizLearningPointRel(final Quiz quiz) {
		this.getHibernateTemplate().execute(new HibernateCallback(){
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.createQuery("delete QuizLearningPointRel where quiz.id = :quizId");
				query.setParameter("quizId", quiz.getId());
				return query.executeUpdate();
			}
		});
	}
}
