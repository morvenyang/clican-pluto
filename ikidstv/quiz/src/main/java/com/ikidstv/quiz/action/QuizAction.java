package com.ikidstv.quiz.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;

import org.apache.commons.io.FileUtils;
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
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

import com.ikidstv.quiz.bean.ContentTree;
import com.ikidstv.quiz.bean.Identity;
import com.ikidstv.quiz.enumeration.QuizStatus;
import com.ikidstv.quiz.enumeration.TemplateId;
import com.ikidstv.quiz.model.Bingo;
import com.ikidstv.quiz.model.CatchIt;
import com.ikidstv.quiz.model.FindDifference;
import com.ikidstv.quiz.model.Image;
import com.ikidstv.quiz.model.LearningPoint;
import com.ikidstv.quiz.model.Matching;
import com.ikidstv.quiz.model.Metadata;
import com.ikidstv.quiz.model.MultiChoice;
import com.ikidstv.quiz.model.Quiz;
import com.ikidstv.quiz.model.QuizLearningPointRel;
import com.ikidstv.quiz.model.Sequence;
import com.ikidstv.quiz.model.StoryTelling;
import com.ikidstv.quiz.model.Template;
import com.ikidstv.quiz.model.WordSearch;

@Scope(ScopeType.PAGE)
@Name("quizAction")
@Restrict(value = "#{identity.isLoggedIn(true)}")
public class QuizAction extends BaseAction {

	private final static Log log = LogFactory.getLog(QuizAction.class);

	private final static int PAGE_TYPE_QUIZ = 1;
	private final static int PAGE_TYPE_AUDIT = 2;
	private final static int PAGE_TYPE_PLACEMENT_TEST = 3;

	private final String TITLE_RECORD = "titleRecord";
	private final String RECORD = "record";

	@In(required = true)
	FacesMessages statusMessages;

	private ContentTree contentTree;
	private Map<String, ContentTree> seasonMap;
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
	private Map<String, List<Image>> picturesByEpisodes;

	private String picturePropertyPath;

	private Quiz quiz;
	private Metadata metadata;

	private int pageType = PAGE_TYPE_QUIZ;

	private List<Quiz> placementTestQuizs;

	private boolean folder;

	private String audioPropertyPath;

	private String tempAudioFilePath;

	private String tempTitleAudioFilePath;
	private Map<String, String> tempAudioFilePaths;
	private int uploadCount = 0;

	@In
	private Identity identity;

	public void listQuizs() {
		pageType = PAGE_TYPE_QUIZ;
		contentTree = this.getContentService().getContentTree();
		seasonMap = new HashMap<String, ContentTree>();
		for (ContentTree seasonNode : contentTree.getSubTree()) {
			seasonMap.put(seasonNode.getSeasonId(), seasonNode);
		}
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
	}

	public void listPlacementTestQuizs() {
		pageType = PAGE_TYPE_PLACEMENT_TEST;
		placementTestQuizs = this.getQuizService().findPlacementQuiz();
		this.learningPointTreeMap = this.getLearningPointService()
				.getLearningPointWithTreeMap();
		this.learningPointIdMap = new HashMap<Long, LearningPoint>();
		for (List<LearningPoint> ps : learningPointTreeMap.values()) {
			for (LearningPoint lp : ps) {
				this.learningPointIdMap.put(lp.getId(), lp);
			}
		}
		this.templates = this.getTemplateService().getAllTemplates();
	}

	public void listAuditingQuizs() {
		pageType = PAGE_TYPE_AUDIT;
		quizBySelectedContent = this.getQuizService().findAuditingQuiz();
		this.learningPointTreeMap = this.getLearningPointService()
				.getLearningPointWithTreeMap();
		this.learningPointIdMap = new HashMap<Long, LearningPoint>();
		for (List<LearningPoint> ps : learningPointTreeMap.values()) {
			for (LearningPoint lp : ps) {
				this.learningPointIdMap.put(lp.getId(), lp);
			}
		}
	}

	public void selectContent(ContentTree contentTree) {
		this.selectedContentTree = contentTree;
		quizBySelectedContent = this.getQuizService().findQuizByUserId(
				identity.getUser().getId(), selectedContentTree.getContentId());
	}

	public void selectEpisode(String episodeName) {
		this.folder = false;
		this.pictures = this.picturesByEpisodes.get(episodeName);
	}

	public List<Quiz> getQuizBySelectedContent() {
		return quizBySelectedContent;
	}

	public void addQuiz() {
		this.quiz = new Quiz();
		this.quiz.setStatus(QuizStatus.INIT.getStatus());
		this.quiz.setUser(this.identity.getUser());
		if (this.pageType == PAGE_TYPE_QUIZ) {
			this.quiz.setEpisode(this.selectedContentTree.getName());
			this.quiz.setEpisodeId(this.selectedContentTree.getEpisonId());
			this.quiz.setSeason(this.selectedContentTree.getParent().getName());
			this.quiz.setSeasonId(this.selectedContentTree.getParent()
					.getSeasonId());
			this.learningPoint = learningPointTreeMap.keySet().iterator()
					.next();
			this.subLearningPoints = this.learningPointTreeMap
					.get(this.learningPoint);
			this.selectedLearningPoints = new ArrayList<LearningPoint>();
			List<Image> allPictures = this.getImageService().getImageByContent(
					this.selectedContentTree.getSeasonId());
			this.picturesByEpisodes = new LinkedHashMap<String, List<Image>>();
			Map<String, String> episodeIdNameMap = new HashMap<String, String>();
			for (ContentTree esipsodeNode : this.selectedContentTree
					.getParent().getSubTree()) {
				episodeIdNameMap.put(esipsodeNode.getEpisonId(),
						esipsodeNode.getName());
				this.picturesByEpisodes.put(esipsodeNode.getName(),
						new ArrayList<Image>());
			}
			for (Image image : allPictures) {
				String episodeId = image.getEpisode();
				String episodeName = episodeIdNameMap.get(episodeId);
				if (StringUtils.isNotEmpty(episodeName)) {
					List<Image> images = this.picturesByEpisodes
							.get(episodeName);
					if (images != null) {
						images.add(image);
					}
				}
			}
		}
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
		if (pageType != PAGE_TYPE_AUDIT) {
			if (this.quiz.getCreateTime() == null) {
				this.quiz.setCreateTime(new Date());
			}
			this.quiz.setTemplate(this.selectedTemplate);
		}

		if (pageType != PAGE_TYPE_PLACEMENT_TEST) {
			// learning points and template can be set by teacher but not
			// admin
			Set<QuizLearningPointRel> learningPointRelSet = new HashSet<QuizLearningPointRel>();
			for (LearningPoint lp : this.selectedLearningPoints) {
				QuizLearningPointRel rel = new QuizLearningPointRel();
				rel.setLearningPoint(lp);
				rel.setQuiz(this.quiz);
				learningPointRelSet.add(rel);
			}
			this.quiz.setLearningPointRelSet(learningPointRelSet);
		}

		this.getQuizService().saveQuiz(quiz, this.metadata);
		if (pageType == PAGE_TYPE_AUDIT) {
			quizBySelectedContent = this.getQuizService().findAuditingQuiz();
		} else if (pageType == PAGE_TYPE_PLACEMENT_TEST) {
			placementTestQuizs = this.getQuizService().findPlacementQuiz();
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
		if (!this.passValidate()) {
			return;
		}
		this.saveQuiz();
		this.quiz.setStatus(QuizStatus.PUBLISHED.getStatus());
		this.quiz.setAuditUser(identity.getUser());
		this.quiz.setPublishTime(new Date());
		this.getQuizService().updateQuiz(quiz);
		quizBySelectedContent = this.getQuizService().findAuditingQuiz();
	}

	public void rejectQuiz() {
		this.saveQuiz();
		this.quiz.setStatus(QuizStatus.REJECTED.getStatus());
		this.quiz.setAuditUser(identity.getUser());
		this.getQuizService().updateQuiz(quiz);
		quizBySelectedContent = this.getQuizService().findAuditingQuiz();
	}

	public void editQuiz(Quiz quiz) {
		this.quiz = this.getQuizService().findQuizById(quiz.getId());
		if (this.pageType != PAGE_TYPE_PLACEMENT_TEST) {
			this.selectedLearningPoints = new ArrayList<LearningPoint>();
			this.selectedTemplate = this.quiz.getTemplate();
			for (QuizLearningPointRel rel : this.quiz.getLearningPointRelSet()) {
				this.selectedLearningPoints.add(rel.getLearningPoint());
			}
			this.learningPoint = learningPointTreeMap.keySet().iterator()
					.next();
			this.subLearningPoints = this.learningPointTreeMap
					.get(this.learningPoint);

			if (this.pageType == PAGE_TYPE_QUIZ) {
				List<Image> allPictures = this.getImageService()
						.getImageByContent(this.quiz.getSeasonId());
				this.picturesByEpisodes = new LinkedHashMap<String, List<Image>>();
				Map<String, String> episodeIdNameMap = new HashMap<String, String>();

				for (ContentTree esipsodeNode : this.seasonMap.get(
						quiz.getSeasonId()).getSubTree()) {
					episodeIdNameMap.put(esipsodeNode.getEpisonId(),
							esipsodeNode.getName());
					this.picturesByEpisodes.put(esipsodeNode.getName(),
							new ArrayList<Image>());
				}
				for (Image image : allPictures) {
					String episodeId = image.getEpisode();
					String episodeName = episodeIdNameMap.get(episodeId);
					if (StringUtils.isNotEmpty(episodeName)) {
						List<Image> images = this.picturesByEpisodes
								.get(episodeName);
						if (images != null) {
							images.add(image);
						}
					}
				}
			}

		}
		this.metadata = this.getQuizService().getMetadataForQuiz(this.quiz);
	}

	public void auditQuiz(Quiz quiz) {
		this.editQuiz(quiz);
	}

	public void deleteQuiz(Quiz quiz) {
		this.getQuizService().deleteQuiz(quiz);
		if (this.pageType == PAGE_TYPE_QUIZ) {
			if (this.selectedContentTree == null) {
				quizBySelectedContent = this.getQuizService().findQuizByUserId(
						identity.getUser().getId(), null);
			} else {
				quizBySelectedContent = this.getQuizService().findQuizByUserId(
						identity.getUser().getId(),
						selectedContentTree.getContentId());
			}
		} else {
			placementTestQuizs = this.getQuizService().findPlacementQuiz();
		}

	}

	public void previewQuiz() {
	}

	private boolean passValidate() {
		boolean validated = true;
		try {
			Method[] methods = this.metadata.getClass().getMethods();
			for (Method method : methods) {
				if (method.isAnnotationPresent(Column.class)) {
					if ((method.getName().contains("Record"))) {
						String value = (String) method.invoke(this.metadata,
								new Object[] {});
						if (StringUtils.isEmpty(value)) {
							validated = false;
							break;
						}
					}
				}
			}
			if (!validated) {
				this.statusMessages.addToControlFromResourceBundle(
						"metadataMessage", Severity.ERROR, "quizPassValidate");
				return false;
			}
		} catch (Exception e) {
			log.error("", e);
		}
		return validated;

	}

	private boolean submitValidate() {
		boolean validated = true;
		if (this.selectedTemplate == null) {
			this.statusMessages.addToControlFromResourceBundle(
					"templateSelect", Severity.ERROR, "quizTemplateRequired");
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
			boolean xyValided = true;
			if (this.selectedTemplate.getTemplateId() == TemplateId.Find_Difference) {
				xyValided = false;
			}
			boolean wptValidated = true;
			if (this.metadata instanceof MultiChoice) {
				MultiChoice mc = (MultiChoice) this.metadata;
				int answerCount = 0;
				if (mc.isAnswer1()) {
					answerCount++;
				}
				if (mc.isAnswer2()) {
					answerCount++;
				}
				if (mc.isAnswer3()) {
					answerCount++;
				}
				if (answerCount != 1) {
					this.statusMessages.addToControlFromResourceBundle(
							"metadataMessage", Severity.ERROR,
							"quizAnswerValidate");
					validated = false;
				}
			}

			for (Method method : methods) {
				if (method.isAnnotationPresent(Column.class)) {
					if ((method.getName().contains("getWord") || method
							.getName().contains("getPicture"))
							|| method.getName().equals("getTitle")) {
						if (this.selectedTemplate.getTemplateId() == TemplateId.Multi_Choice1) {
							if (!method.getName().contains("getWord")) {
								continue;
							}
						} else if (this.selectedTemplate.getTemplateId() == TemplateId.Multi_Choice2) {
							if (!method.getName().contains("getPicture")) {
								continue;
							}
						} else if (this.selectedTemplate.getTemplateId() == TemplateId.Multi_Choice3) {
							if (!method.getName().contains("getWord")) {
								continue;
							}
						}
						String value = (String) method.invoke(this.metadata,
								new Object[] {});
						if (StringUtils.isEmpty(value)
								&& this.selectedTemplate.getTemplateId() != TemplateId.Story_Telling) {
							wptValidated = false;
						}
					} else if (method.getName().contains("getX")
							|| method.getName().contains("getY")) {
						Integer value = (Integer) method.invoke(this.metadata,
								new Object[] {});
						if (value > 0) {
							xyValided = true;
						}
					}
				}
			}

			if (!wptValidated) {
				this.statusMessages
						.addToControlFromResourceBundle("metadataMessage",
								Severity.ERROR, "quizSubmitValidate");
				validated = false;
			}
			if (!xyValided) {
				this.statusMessages.addToControlFromResourceBundle(
						"metadataMessage", Severity.ERROR,
						"quizFindDifferenceValidate");
				validated = false;
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
			if (this.selectedTemplate.getTemplateId() == TemplateId.Multi_Choice1
					|| this.selectedTemplate.getTemplateId() == TemplateId.Multi_Choice2
					|| this.selectedTemplate.getTemplateId() == TemplateId.Multi_Choice3) {
				if(selectedLearningPoints.size()>=1){
					this.statusMessages
					.addToControlFromResourceBundle("selectedLearningPointGrid",
							Severity.ERROR, "quizOnlyOneLearningPointSelected");
					return;
				}
			}
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
		} else if (templateId == TemplateId.Multi_Choice1
				|| templateId == TemplateId.Multi_Choice2
				|| templateId == TemplateId.Multi_Choice3) {
			this.metadata = new MultiChoice();
		} else if (templateId == TemplateId.Catch_It_Sentence
				|| templateId == TemplateId.Catch_It_Word) {
			this.metadata = new CatchIt();
		} else if (templateId == TemplateId.Word_Search) {
			this.metadata = new WordSearch();
		} else if (templateId == TemplateId.Sequence) {
			this.metadata = new Sequence();
		} else if (templateId == TemplateId.Find_Difference) {
			this.metadata = new FindDifference();
		} else if (templateId == TemplateId.Story_Telling) {
			this.metadata = new StoryTelling();
		}
	}

	public void setPictureIndex(int index) {
		this.folder = true;
		this.picturePropertyPath = "Picture" + index;
	}

	public void uploadPicture(String picturePropertyPath) {
		this.folder = true;
		this.picturePropertyPath = picturePropertyPath;
	}

	public List<String> getFolders() {
		return new ArrayList<String>(this.picturesByEpisodes.keySet());
	}

	public void selectPicture(Image picture) {
		try {
			Method method = metadata.getClass().getMethod(
					"set" + this.picturePropertyPath, String.class);
			method.invoke(metadata, picture.getPath());
		} catch (Exception e) {
			log.error("", e);
		}
	}

	public void setAudioIndex(int index) {
		this.audioPropertyPath = "Record" + index;
		uploadCount = 0;
		this.tempAudioFilePath = null;
		this.tempTitleAudioFilePath = null;
		this.tempAudioFilePaths = null;
	}

	public void uploadAudio(String audioPropertyPath) {
		this.audioPropertyPath = audioPropertyPath;
		uploadCount = 0;
		this.tempAudioFilePath = null;
		this.tempTitleAudioFilePath = null;
		this.tempAudioFilePaths = null;
	}

	public void saveAudio() {
		try {
			if (uploadCount > 1) {
				if (StringUtils.isNotEmpty(this.tempTitleAudioFilePath)) {
					Method method = metadata.getClass().getMethod(
							"setTitleRecord", String.class);
					method.invoke(metadata, this.tempTitleAudioFilePath);
				}
				for (String key : this.tempAudioFilePaths.keySet()) {
					Method method = metadata.getClass().getMethod(
							"set" + key.substring(0, 1).toUpperCase()
									+ key.substring(1), String.class);
					method.invoke(metadata, this.tempAudioFilePaths.get(key));
				}
			} else {
				Method method = metadata.getClass().getMethod(
						"set" + this.audioPropertyPath, String.class);
				method.invoke(metadata, this.tempAudioFilePath);
			}

		} catch (Exception e) {
			log.error("", e);
		}
	}

	public synchronized void play(OutputStream out, Object data)
			throws IOException {
		String filePath = (String) data;
		try {
			String fullPath = this.getSpringProperty().getRecordingPath() + "/"
					+ filePath;
			byte[] byteData = FileUtils.readFileToByteArray(new File(fullPath));
			out.write(byteData);
		} catch (Exception e) {
			log.error("", e);
		}
	}

	public synchronized void fileUploadListener(UploadEvent event) {
		List<UploadItem> itemList = event.getUploadItems();
		uploadCount++;
		for (int i = 0; i < itemList.size(); i++) {
			UploadItem item = itemList.get(i);
			File file = item.getFile();
			InputStream is = null;
			byte[] content = null;
			try {
				is = new FileInputStream(file);
				content = new byte[is.available()];
				is.read(content);
			} catch (FileNotFoundException e1) {
				log.error("", e1);
			} catch (IOException e2) {
				log.error("", e2);
			} finally {
				try {
					if (is != null) {
						is.close();
					}
				} catch (IOException e) {
					log.error("", e);
				}
			}
			String name = item.getFileName();
			int last = name.lastIndexOf(".");
			String suffix = name.substring(last + 1);
			String recordingPath = com.ikidstv.quiz.util.StringUtils
					.generateFilePathByDate()
					+ "/"
					+ UUID.randomUUID().toString() + "." + suffix;
			String path = this.getSpringProperty().getRecordingPath() + "/"
					+ recordingPath;
			File recordingFile = new File(path);
			if (!recordingFile.exists()) {
				recordingFile.getParentFile().mkdirs();
			}
			try {
				FileUtils.writeByteArrayToFile(recordingFile, content);
			} catch (IOException e2) {
				log.error("", e2);
			}
			if (this.uploadCount > 1) {
				if (name.toLowerCase().contains(TITLE_RECORD.toLowerCase())) {
					this.tempTitleAudioFilePath = recordingPath;
				} else if (name.toLowerCase().contains(RECORD.toLowerCase())) {
					this.tempAudioFilePaths.put(name.substring(0, last),
							recordingPath);
				}
			} else if (this.uploadCount == 1) {
				this.tempAudioFilePaths = new HashMap<String, String>();
				if (name.toLowerCase().contains(TITLE_RECORD.toLowerCase())) {
					this.tempTitleAudioFilePath = recordingPath;
				} else if (name.toLowerCase().contains(RECORD.toLowerCase())) {
					this.tempAudioFilePaths.put(name.substring(0, last),
							recordingPath);
				}
				this.tempAudioFilePath = recordingPath;
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

	public List<Quiz> getPlacementTestQuizs() {
		return placementTestQuizs;
	}

	public void setPlacementTestQuizs(List<Quiz> placementTestQuizs) {
		this.placementTestQuizs = placementTestQuizs;
	}

	public Map<String, List<Image>> getPicturesByEpisodes() {
		return picturesByEpisodes;
	}

	public void setPicturesByEpisodes(
			Map<String, List<Image>> picturesByEpisodes) {
		this.picturesByEpisodes = picturesByEpisodes;
	}

	public boolean isFolder() {
		return folder;
	}

	public void setFolder(boolean folder) {
		this.folder = folder;
	}

	public String getAudioPropertyPath() {
		return audioPropertyPath;
	}

	public void setAudioPropertyPath(String audioPropertyPath) {
		this.audioPropertyPath = audioPropertyPath;
	}

}
