package com.ikidstv.quiz.enumeration;

import java.util.HashMap;
import java.util.Map;

import com.ikidstv.quiz.model.Bingo;
import com.ikidstv.quiz.model.Matching;
import com.ikidstv.quiz.model.MultiChoice;

public enum TemplateId {

	Matching(new Long[]{1L},Matching.class),
	
	Bingo(new Long[]{2L},Bingo.class),
	
	Multi_Choice1(new Long[]{3L},MultiChoice.class);
	
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
