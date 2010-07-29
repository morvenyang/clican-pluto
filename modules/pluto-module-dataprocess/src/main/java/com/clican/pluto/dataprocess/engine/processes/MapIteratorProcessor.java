/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang, jing.tian
 *
 */
package com.clican.pluto.dataprocess.engine.processes;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.clican.pluto.dataprocess.engine.DataProcessor;
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.engine.impl.BaseDataProcessor;
import com.clican.pluto.dataprocess.exception.DataProcessException;

/**
 * 
 * 遍历Map的键，针对值调用后续的processor。
 * 
 * @author jerry.tian
 * 
 */
public class MapIteratorProcessor extends BaseDataProcessor {

	private List<DataProcessor> iteratorProcessors;

	private String mapName;

	private String elementName;

	public String getMapName() {
		return mapName;
	}

	public void setMapName(String collectionName) {
		this.mapName = collectionName;
	}

	public String getElementName() {
		return elementName;
	}

	public void setElementName(String elementName) {
		this.elementName = elementName;
	}

	public List<DataProcessor> getIteratorProcessors() {
		return iteratorProcessors;
	}

	public void setIteratorProcessors(List<DataProcessor> iteratorProcessors) {
		this.iteratorProcessors = iteratorProcessors;
	}

	@SuppressWarnings("unchecked")
	
	public void process(ProcessorContext context) throws DataProcessException {
		Map map = (Map) context.getAttribute(mapName);

		Iterator it = map.keySet().iterator();

		while (it.hasNext()) {
			Object key = it.next();
			ProcessorContext subContext;
			if(this.isCloneContext()){
				subContext = context.getCloneContext();
			}else{
				subContext = context;
			}
			subContext.setAttribute(elementName, map.get(key));
			for (DataProcessor iteratorProcessor : iteratorProcessors) {
				iteratorProcessor.beforeProcess(subContext);
				iteratorProcessor.process(subContext);
				iteratorProcessor.afterProcess(subContext);
			}
			if (this.propagations != null && this.propagations.size() > 0) {
				for (String propagation : propagations) {
					context.setAttribute(propagation, subContext.getAttribute(propagation));
				}
			}
		}
	}

}

// $Id: MapIteratorProcessor.java 12410 2010-05-13 06:55:57Z wei.zhang $