package com.ikidstv.quiz.action;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.jboss.seam.security.Identity;

import com.ikidstv.quiz.bean.Constants;
import com.ikidstv.quiz.bean.SpringProperty;
import com.ikidstv.quiz.service.ContentService;
import com.ikidstv.quiz.service.ImageService;
import com.ikidstv.quiz.service.LearningPointService;
import com.ikidstv.quiz.service.QuizService;
import com.ikidstv.quiz.service.TemplateService;
import com.ikidstv.quiz.service.UserService;

public class SpringBeanAction {

	protected Identity getIdentity() {
		HttpSession session = ((HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest())
				.getSession();
		Identity identity = (Identity) session
				.getAttribute("org.jboss.seam.security.identity");
		return identity;
	}

	protected SpringProperty getSpringProperty() {
		return (SpringProperty) Constants.ctx.getBean("springProperty");
	}

	protected UserService getUserService() {
		return (UserService) Constants.ctx.getBean("userService");
	}

	protected ContentService getContentService() {
		return (ContentService) Constants.ctx.getBean("contentService");
	}

	protected ImageService getImageService() {
		return (ImageService) Constants.ctx.getBean("imageService");
	}

	protected QuizService getQuizService() {
		return (QuizService) Constants.ctx.getBean("quizService");
	}

	protected LearningPointService getLearningPointService() {
		return (LearningPointService) Constants.ctx
				.getBean("learningPointService");
	}
	
	protected TemplateService getTemplateService() {
		return (TemplateService) Constants.ctx
				.getBean("templateService");
	}
}
