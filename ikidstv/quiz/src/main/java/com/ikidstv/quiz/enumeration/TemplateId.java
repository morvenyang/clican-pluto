package com.ikidstv.quiz.enumeration;

import java.util.HashMap;
import java.util.Map;

import com.ikidstv.quiz.model.Bingo;
import com.ikidstv.quiz.model.CatchIt;
import com.ikidstv.quiz.model.FindDifference;
import com.ikidstv.quiz.model.Matching;
import com.ikidstv.quiz.model.MultiChoice;
import com.ikidstv.quiz.model.Sequence;
import com.ikidstv.quiz.model.StoryTelling;
import com.ikidstv.quiz.model.WordSearch;

public enum TemplateId {

	Matching(new Long[]{1L},Matching.class),
	
	Bingo(new Long[]{2L},Bingo.class),
	
	Multi_Choice1(new Long[]{3L},MultiChoice.class),
	
	Multi_Choice2(new Long[]{4L},MultiChoice.class),
	
	Multi_Choice3(new Long[]{5L},MultiChoice.class),
	
	Catch_It_Word(new Long[]{6L},CatchIt.class),
	
	Catch_It_Sentence(new Long[]{7L},CatchIt.class),
	
	Word_Search(new Long[]{8L},WordSearch.class),
	
	Sequence(new Long[]{9L},Sequence.class),
	
	Find_Difference(new Long[]{10L},FindDifference.class),
	
	Story_Telling(new Long[]{11L},StoryTelling.class);
	
	private Long[] ids;
	private Class<?> clazz;
	private static Map<Long,Class<?>> idClassMap = new HashMap<Long,Class<?>>();
	private static Map<Long,TemplateId> idTemplateIdMap = new HashMap<Long,TemplateId>();
	
	static{
		for(TemplateId member:values()){
			for(Long id:member.getIds()){
				idClassMap.put(id, member.getClazz());
				idTemplateIdMap.put(id, member);
			}
		}
	}
	private TemplateId(Long[] ids,Class<?> clazz){
		this.ids = ids;
		this.clazz = clazz;
	}

	public Long[] getIds() {
		return ids;
	}

	public void setIds(Long[] ids) {
		this.ids = ids;
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}
	
	public static Class<?> getClazzById(Long id){
		return idClassMap.get(id);
	}
	
	public static TemplateId getTemplateIdById(Long id){
		return idTemplateIdMap.get(id);
	}
	
}
