package com.chinatelecom.xysq.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.chinatelecom.xysq.bean.JsonLabelValueId;
import com.chinatelecom.xysq.bean.SpringProperty;
import com.chinatelecom.xysq.model.Store;
import com.chinatelecom.xysq.service.StoreService;

@Controller
public class ServerController {

	private final static Log log = LogFactory.getLog(ServerController.class);

	private SpringProperty springProperty;

	public void setSpringProperty(SpringProperty springProperty) {
		this.springProperty = springProperty;
	}

	private StoreService storeService;

	public void setStoreService(StoreService storeService) {
		this.storeService = storeService;
	}

	@RequestMapping("/autoCompleteStores")
	public void autoCompleteStores(
			@RequestParam(value = "ownerId", required = false) Long ownerId,
			@RequestParam(value = "keyword", required = true) String keyword,
			HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		List<Store> stores = storeService.findStores(ownerId, keyword);
		List<JsonLabelValueId> results = new ArrayList<JsonLabelValueId>();
		for(Store store:stores){
			JsonLabelValueId j = new JsonLabelValueId();
			j.setId(store.getId());
			j.setLabel(store.getName());
			j.setValue(store.getName());
			results.add(j);
		}
		String result = JSONArray.fromObject(results).toString();
		try {
			resp.setContentType("application/json");
			resp.getOutputStream().write(result.getBytes("utf-8"));
		} catch (Exception e) {
			log.error("", e);
		}
	}
}
