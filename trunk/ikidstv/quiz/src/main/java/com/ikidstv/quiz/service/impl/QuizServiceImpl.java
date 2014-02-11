package com.ikidstv.quiz.service.impl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Id;

import net.sf.json.JSONArray;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ikidstv.quiz.bean.SpringProperty;
import com.ikidstv.quiz.dao.QuizDao;
import com.ikidstv.quiz.enumeration.Device;
import com.ikidstv.quiz.enumeration.TemplateId;
import com.ikidstv.quiz.model.Metadata;
import com.ikidstv.quiz.model.Quiz;
import com.ikidstv.quiz.service.ContentService;
import com.ikidstv.quiz.service.QuizService;

public class QuizServiceImpl implements QuizService {

	private QuizDao quizDao;

	private ContentService contentService;

	private SpringProperty springProperty;

	private final static Log log = LogFactory.getLog(QuizServiceImpl.class);

	public void setQuizDao(QuizDao quizDao) {
		this.quizDao = quizDao;
	}

	public void setContentService(ContentService contentService) {
		this.contentService = contentService;
	}

	public void setSpringProperty(SpringProperty springProperty) {
		this.springProperty = springProperty;
	}

	public List<Quiz> findQuizByUserId(Long userId, String contentId) {
		List<Quiz> dbResult = quizDao.findQuizByUserId(userId);
		List<Quiz> result = new ArrayList<Quiz>();
		Set<String> episodeIds = contentService.findEpisodeIds(contentId);
		for (Quiz quiz : dbResult) {
			if (episodeIds.contains(quiz.getEpisodeId())) {
				result.add(quiz);
			}
		}
		return result;
	}

	public List<Quiz> findAuditingQuiz() {
		return quizDao.findAuditingQuiz();
	}

	public void saveQuiz(Quiz quiz, Metadata metadata) {
		this.quizDao.saveMetadata(metadata);
		quiz.setMetadataId(metadata.getId());
		this.quizDao.deleteQuizLearningPointRel(quiz);
		this.quizDao.saveQuiz(quiz);
	}

	public void updateQuiz(Quiz quiz) {
		this.quizDao.saveQuiz(quiz);
	}

	public Quiz findQuizById(Long id) {
		return this.quizDao.findQuizById(id);
	}

	public Metadata getMetadataForQuiz(Quiz quiz) {
		TemplateId templateId = quiz.getTemplate().getTemplateId();
		return this.quizDao.getMetadata(templateId, quiz.getMetadataId());
	}

	public void deleteQuiz(Quiz quiz) {
		Metadata metadata = this.getMetadataForQuiz(quiz);
		if (metadata != null) {
			this.quizDao.deleteMetadata(metadata);
		}
		this.quizDao.deleteQuiz(quiz);
	}

	public String checkQuizExistence(String seasonId, Integer minAge,
			Integer maxAge, String level, Device device, String version) {
		List<Quiz> quizList = this.quizDao.findQuizBySeason(seasonId, minAge,
				maxAge, level, device);
		Set<String> episodeIds = contentService.findEpisodeIds(seasonId);
		Map<String, Boolean> existenceMap = new HashMap<String, Boolean>();
		for (Quiz quiz : quizList) {
			existenceMap.put(quiz.getEpisodeId(), true);
			episodeIds.remove(quiz.getEpisodeId());
		}
		for (String episodeId : episodeIds) {
			existenceMap.put(episodeId, false);
		}
		JSONArray object = JSONArray.fromObject(existenceMap);
		String result = "{result:" + object.toString() + "}";
		return result;
	}

	public String findQuizByEpisode(String episodeId, Integer minAge,
			Integer maxAge, String level, Device device, String version) {
		List<Quiz> quizList = this.quizDao.findQuizByEpisode(episodeId, minAge,
				maxAge, level, device);
		List<Map<String, Object>> quizObjList = new ArrayList<Map<String, Object>>();
		for (Quiz quiz : quizList) {
			Map<String, Object> quizObjMap = new HashMap<String, Object>();
			quizObjMap.put("id", quiz.getId());
			quizObjMap.put("type", quiz.getTemplate().getTemplateId().name());
			Metadata metadata = quizDao.getMetadata(quiz.getTemplate()
					.getTemplateId(), quiz.getMetadataId());
			try {
				Method[] methods = metadata.getClass().getMethods();
				for (Method method : methods) {
					if (method.isAnnotationPresent(Column.class)
							&& !method.isAnnotationPresent(Id.class)) {
						String name = method.getName().replace("get", "");
						name = name.substring(0, 1).toLowerCase()
								+ name.substring(1);
						Object value = method.invoke(metadata, new Object[] {});
						if (name.indexOf("record") != -1) {
							value = springProperty.getServerUrl()
									+ springProperty.getContextPath()
									+ "/recordView.do?recordPath=" + value;
						} else if (name.indexOf("picture") != -1) {
							value = springProperty.getServerUrl()
									+ springProperty.getContextPath()
									+ "/imageView.do?imagePath=" + value;
						}
						quizObjMap.put(name, value);
					}
				}
				quizObjList.add(quizObjMap);
			} catch (Exception e) {
				log.error("", e);
			}
		}
		JSONArray object = JSONArray.fromObject(quizObjList);
		String result = "{result:" + object.toString() + "}";
		return result;
	}

	public List<Quiz> findPlacementQuiz() {
		// TODO Auto-generated method stub
		return null;
	}

}
