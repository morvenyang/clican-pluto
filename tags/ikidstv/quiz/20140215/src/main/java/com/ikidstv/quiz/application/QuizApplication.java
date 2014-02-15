package com.ikidstv.quiz.application;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Startup;

import com.ikidstv.quiz.bean.Constants;
import com.ikidstv.quiz.bean.SpringProperty;

@Scope(ScopeType.APPLICATION)
@Name("QuizApplication")
@Install(precedence = Install.APPLICATION)
@Startup
public class QuizApplication {

	private final static Log log = LogFactory.getLog(QuizApplication.class);

    @Out(scope = ScopeType.APPLICATION, required = true)
    private SpringProperty springProperty;

    @Out(scope = ScopeType.APPLICATION, required = false)
    private String contextPath = "/quiz";

    

    @Create
    public void createApplication() {
        if (log.isInfoEnabled()) {
            log.info("Startup Quiz Application");
        }
        springProperty = (SpringProperty) Constants.ctx.getBean("springProperty");
        contextPath = springProperty.getContextPath();
        if (log.isInfoEnabled()) {
            log.info("contextPath=[" + contextPath + "]");
        }
    }

    public SpringProperty getSpringProperty() {
        return springProperty;
    }

    public String getContextPath() {
        return contextPath;
    }

    

   
}
