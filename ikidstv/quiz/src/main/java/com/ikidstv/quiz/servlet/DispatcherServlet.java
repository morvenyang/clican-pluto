package com.ikidstv.quiz.servlet;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.handler.AbstractUrlHandlerMapping;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter;

public class DispatcherServlet extends
		org.springframework.web.servlet.DispatcherServlet {

	 
	/**
	 * 
	 */
	private static final long serialVersionUID = -5894814953860889893L;

	protected Object createDefaultStrategy(ApplicationContext context,
			@SuppressWarnings("rawtypes") Class clazz) throws BeansException {
		Object obj = context.getAutowireCapableBeanFactory().createBean(clazz);
		if (obj instanceof AbstractUrlHandlerMapping) {
			((AbstractUrlHandlerMapping) obj).setAlwaysUseFullPath(true);
		} else if (obj instanceof AnnotationMethodHandlerAdapter) {
			((AnnotationMethodHandlerAdapter) obj).setAlwaysUseFullPath(true);
		}
		return obj;
	}
}
