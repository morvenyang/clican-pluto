package com.ikidstv.quiz.dao.impl;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.ikidstv.quiz.dao.QuizDao;
import com.ikidstv.quiz.model.Metadata;
import com.ikidstv.quiz.model.Quiz;

public class QuizDaoImpl extends HibernateDaoSupport implements QuizDao {

	@SuppressWarnings("unchecked")
	public List<Quiz> findQuizByUserId(Long userId) {
		List<Quiz> result = this.getHibernateTemplate().findByNamedParam( "from Quiz where user.id = :userId", "userId", userId);
		return result;
	}

	public void saveQuiz(Quiz quiz) {
		this.getHibernateTemplate().save(quiz);
	}

	public void saveMetadata(Metadata metadata) {
		this.getHibernateTemplate().save(metadata);
	}

}
