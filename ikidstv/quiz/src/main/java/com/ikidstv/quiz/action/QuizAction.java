package com.ikidstv.quiz.action;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.security.Restrict;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage.Severity;

import com.ikidstv.quiz.bean.ContentTree;
import com.ikidstv.quiz.bean.Identity;
import com.ikidstv.quiz.enumeration.QuizStatus;
import com.ikidstv.quiz.enumeration.TemplateId;
import com.ikidstv.quiz.model.Bingo;
import com.ikidstv.quiz.model.Image;
import com.ikidstv.quiz.model.LearningPoint;
import com.ikidstv.quiz.model.Matching;
import com.ikidstv.quiz.model.Metadata;
import com.ikidstv.quiz.model.MultiChoice;
import com.ikidstv.quiz.model.Quiz;
import com.ikidstv.quiz.model.QuizLearningPointRel;
import com.ikidstv.quiz.model.Template;

@Scope(ScopeType.PAGE)
@Name("quizAction")
@Restrict(value = "#{identity.isLoggedIn(true)}")
public class QuizAction extends BaseAction {

	private final static Log log = LogFactory.getLog(QuizAction.class);

	@In(required = true)
	FacesMessages statusMessages;

	private ContentTree contentTree;
	private ContentTree selectedContentTree;
	private List<Quiz> quizBySelectedContent;

	private Map<String, List<LearningPoint>> learningPointTreeMap;
	private Map<Long, LearningPoint> learningPointIdMap;

	private List<Template> templates;
	private Template selectedTemplate;

	private String learningPoint;
	private Long learningPointId;
	private List<LearningPoint> subLearningPoints;
	private List<LearningPoint> selectedLearningPoints;

	private List<Image> pictures;
	private int pictureIndex;

	private Quiz quiz;
	private Metadata metadata;

	private boolean auditing = false;

	@In
	private Identity identity;

	public void listQuizs() {
		contentTree = this.getContentService().getContentTree();
		quizBySelectedContent = this.getQuizService().findQuizByUserId(
				identity.getUser().getId(), null);
		this.learningPointTreeMap = this.getLearningPointService()
				.getLearningPointWithTreeMap();
		this.learningPointIdMap = new HashMap<Long, LearningPoint>();
		for (List<LearningPoint> ps : learningPointTreeMap.values()) {
			for (LearningPoint lp : ps) {
				this.learningPointIdMap.put(lp.getId(), lp);
			}
		}
		this.templates = this.getTemplateService().getAllTemplates();
		this.auditing = false;
	}

	public void listAuditingQuizs() {
		quizBySelectedContent = this.getQuizService().findAuditingQuiz();
		this.learningPointTreeMap = this.getLearningPointService()
				.getLearningPointWithTreeMap();
		this.learningPointIdMap = new HashMap<Long, LearningPoint>();
		for (List<LearningPoint> ps : learningPointTreeMap.values()) {
			for (LearningPoint lp : ps) {
				this.learningPointIdMap.put(lp.getId(), lp);
			}
		}
		this.auditing = true;
	}

	public void selectContent(ContentTree contentTree) {
		this.selectedContentTree = contentTree;
		quizBySelectedContent = this.getQuizService().findQuizByUserId(
				identity.getUser().getId(), selectedContentTree.getContentId());
	}

	public List<Quiz> getQuizBySelectedContent() {
		return quizBySelectedContent;
	}

	public void addQuiz() {
		this.quiz = new Quiz();
		this.quiz.setStatus(QuizStatus.INIT.getStatus());
		this.quiz.setUser(this.identity.getUser());
		this.quiz.setEpisode(this.selectedContentTree.getName());
		this.quiz.setEpisodeId(this.selectedContentTree.getEpisonId());
		this.quiz.setSeason(this.selectedContentTree.getParent().getName());
		this.quiz.setSeasonId(this.selectedContentTree.getParent()
				.getSeasonId());
		this.learningPoint = learningPointTreeMap.keySet().iterator().next();
		this.subLearningPoints = this.learningPointTreeMap
				.get(this.learningPoint);
		this.selectedLearningPoints = new ArrayList<LearningPoint>();
		this.pictures = this.getImageService().getImageByContent(
				this.selectedContentTree.getSeasonId());
		this.quiz.setBackgroundImage(this.selectedContentTree
				.getBackgroundImage());
		this.quiz.setFrontImage(this.selectedContentTree.getFrontImage());
		this.selectedTemplate = null;
	}

	public void submitQuiz() {
		if (!this.submitValidate()) {
			return;
		}
		this.saveQuiz();
		this.quiz.setStatus(QuizStatus.AUDITING.getStatus());
		this.getQuizService().updateQuiz(quiz);
	}

	public void saveQuiz() {
		if (!this.auditing) {
			if (this.quiz.getCreateTime() == null) {
				this.quiz.setCreateTime(new Date());
			}
			// learning points and template can be set by teacher but not admin
			Set<QuizLearningPointRel> learningPointRelSet = new HashSet<QuizLearningPointRel>();
			for (LearningPoint lp : this.selectedLearningPoints) {
				QuizLearningPointRel rel = new QuizLearningPointRel();
				rel.setLearningPoint(lp);
				rel.setQuiz(this.quiz);
				learningPointRelSet.add(rel);
			}
			this.quiz.setLearningPointRelSet(learningPointRelSet);
			this.quiz.setTemplate(this.selectedTemplate);
		}
		this.getQuizService().saveQuiz(quiz, this.metadata);
		if (this.auditing) {
			quizBySelectedContent = this.getQuizService().findAuditingQuiz();
		} else {
			if (selectedContentTree != null) {
				quizBySelectedContent = this.getQuizService().findQuizByUserId(
						identity.getUser().getId(),
						selectedContentTree.getContentId());
			} else {
				quizBySelectedContent = this.getQuizService().findQuizByUserId(
						identity.getUser().getId(), null);
			}
		}
	}

	public void passQuiz() {
		this.saveQuiz();
		this.quiz.setStatus(QuizStatus.PUBLISHED.getStatus());
		this.quiz.setAuditUser(identity.getUser());
		this.quiz.setPublishTime(new Date());
		this.getQuizService().updateQuiz(quiz);
	}

	public void rejectQuiz() {
		this.saveQuiz();
		this.quiz.setStatus(QuizStatus.REJECTED.getStatus());
		this.quiz.setAuditUser(identity.getUser());
		this.getQuizService().updateQuiz(quiz);
	}

	public void editQuiz(Quiz quiz) {
		this.quiz = this.getQuizService().findQuizById(quiz.getId());
		this.selectedLearningPoints = new ArrayList<LearningPoint>();
		this.selectedTemplate = this.quiz.getTemplate();
		for (QuizLearningPointRel rel : this.quiz.getLearningPointRelSet()) {
			this.selectedLearningPoints.add(rel.getLearningPoint());
		}
		this.metadata = this.getQuizService().getMetadataForQuiz(this.quiz);
		this.learningPoint = learningPointTreeMap.keySet().iterator().next();
		this.subLearningPoints = this.learningPointTreeMap
				.get(this.learningPoint);
	}

	public void auditQuiz(Quiz quiz) {
		this.editQuiz(quiz);
	}

	public void deleteQuiz(Quiz quiz) {
		this.getQuizService().deleteQuiz(quiz);
		quizBySelectedContent = this.getQuizService().findQuizByUserId(
				identity.getUser().getId(), selectedContentTree.getContentId());
	}

	public void previewQuiz() {
	}

	private boolean submitValidate() {
		boolean validated=true;
		if (this.selectedTemplate == null) {
			this.statusMessages.addToControlFromResourceBundle(
					"templateSelect", Severity.ERROR,
					"quizTemplateRequired");
			validated = false;
		}
		if (this.selectedLearningPoints == null
				|| this.selectedLearningPoints.size() == 0) {
			this.statusMessages.addToControlFromResourceBundle(
					"selectedLearningPointGrid", Severity.ERROR,
					"quizLearningPointRequired");
			validated = false;
		}
		
		if (this.metadata == null) {
			this.statusMessages.addToControlFromResourceBundle(
					"metadataMessage", Severity.ERROR, "quizSubmitValidate");
			return false;
		}
		try {
			Method[] methods = this.metadata.getClass().getMethods();
			for (Method method : methods) {
				if (method.isAnnotationPresent(Column.class)&&(method.getName().contains("getWord")||method.getName().contains("getPicture"))) {
					String value = (String) method.invoke(this.metadata,
							new Object[] {});
					if (StringUtils.isEmpty(value)) {
						this.statusMessages.addToControlFromResourceBundle(
								"metadataMessage", Severity.ERROR,
								"quizSubmitValidate");
						return false;
					}
				}
			}
		} catch (Exception e) {
			log.error("", e);
		}
		return validated;

	}

	public void changeLearningPoint() {
		this.subLearningPoints = this.learningPointTreeMap
				.get(this.learningPoint);
	}

	public String getTemplateIdName() {
		if (this.selectedTemplate == null) {
			return null;
		}
		return this.selectedTemplate.getTemplateId().name();
	}

	public void addLearningPoint() {
		LearningPoint learningPoint = learningPointIdMap
				.get(this.learningPointId);
		if (learningPoint != null) {
			selectedLearningPoints.add(learningPoint);
		}
	}

	public void removeLearningPoint(LearningPoint lp) {
		selectedLearningPoints.remove(lp);
	}

	public void selectTemplate(Template template) {
		this.selectedTemplate = template;
		TemplateId templateId = template.getTemplateId();
		if (templateId == TemplateId.Matching) {
			this.metadata = new Matching();
		} else if (templateId == TemplateId.Bingo) {
			this.metadata = new Bingo();
		} else if (templateId == TemplateId.Multi_Choice) {
			this.metadata = new MultiChoice();
		}
	}

	public void setPictureIndex(int index) {
		this.pictureIndex = index;
	}

	public void selectPicture(Image picture) {
		if (pictureIndex == -1) {
			// background image
			this.quiz.setBackgroundImage(picture.getPath());
		} else if (pictureIndex == -2) {
			// front image
			this.quiz.setFrontImage(picture.getPath());
		} else if (pictureIndex > 0) {
			try {
				Method method = metadata.getClass().getMethod(
						"setPicture" + this.pictureIndex, String.class);
				method.invoke(metadata, picture.getPath());
			} catch (Exception e) {
				log.error("", e);
			}
		}
	}

	public Quiz getQuiz() {
		return quiz;
	}

	public void setQuiz(Quiz quiz) {
		this.quiz = quiz;
	}

	public ContentTree getContentTree() {
		return contentTree;
	}

	public ContentTree getSelectedContentTree() {
		return selectedContentTree;
	}

	public String getLearningPoint() {
		return learningPoint;
	}

	public void setLearningPoint(String learningPoint) {
		this.learningPoint = learningPoint;
	}

	public List<LearningPoint> getSelectedLearningPoints() {
		return selectedLearningPoints;
	}

	public void setSelectedLearningPoints(
			List<LearningPoint> selectedLearningPoints) {
		this.selectedLearningPoints = selectedLearningPoints;
	}

	public List<LearningPoint> getSubLearningPoints() {
		return subLearningPoints;
	}

	public void setSubLearningPoints(List<LearningPoint> subLearningPoints) {
		this.subLearningPoints = subLearningPoints;
	}

	public Long getLearningPointId() {
		return learningPointId;
	}

	public void setLearningPointId(Long learningPointId) {
		this.learningPointId = learningPointId;
	}

	public Map<String, List<LearningPoint>> getLearningPointTreeMap() {
		return learningPointTreeMap;
	}

	public void setLearningPointTreeMap(
			Map<String, List<LearningPoint>> learningPointTreeMap) {
		this.learningPointTreeMap = learningPointTreeMap;
	}

	public List<Template> getTemplates() {
		return templates;
	}

	public void setTemplates(List<Template> templates) {
		this.templates = templates;
	}

	public Template getSelectedTemplate() {
		return selectedTemplate;
	}

	public void setSelectedTemplate(Template selectedTemplate) {
		this.selectedTemplate = selectedTemplate;
	}

	public Metadata getMetadata() {
		return metadata;
	}

	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}

	public List<Image> getPictures() {
		return pictures;
	}

	public void setPictures(List<Image> pictures) {
		this.pictures = pictures;
	}

}
