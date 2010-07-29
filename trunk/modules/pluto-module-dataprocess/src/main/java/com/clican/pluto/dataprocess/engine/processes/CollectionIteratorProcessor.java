/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang, jing.tian
 *
 */
package com.clican.pluto.dataprocess.engine.processes;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.clican.pluto.dataprocess.engine.DataProcessor;
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.engine.impl.BaseDataProcessor;
import com.clican.pluto.dataprocess.exception.DataProcessException;

/**
 * 
 * 
 * @author jerry.tian
 * 
 */
public class CollectionIteratorProcessor extends BaseDataProcessor {

	private List<DataProcessor> iteratorProcessors;

	private String collectionName;

	private String elementName;

	private boolean stepCommit;

	public String getCollectionName() {
		return collectionName;
	}

	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
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

	public boolean isStepCommit() {
		return stepCommit;
	}

	public void setStepCommit(boolean stepCommit) {
		this.stepCommit = stepCommit;
	}

	public void doWithoutCommit(DataProcessor processor, ProcessorContext context) throws DataProcessException {
		processor.beforeProcess(context);
		processor.process(context);
		processor.afterProcess(context);
	}

	@SuppressWarnings("unchecked")
	
	public void process(ProcessorContext context) throws DataProcessException {

		Collection collection = (Collection) context.getAttribute(collectionName);
		int size = collection.size();
		Iterator it = collection.iterator();
		int count = 0;
		while (it.hasNext()) {
			count++;
			Object obj = it.next();
			ProcessorContext subContext;
			if (this.cloneContext) {
				subContext = context.getCloneContext();
			} else {
				subContext = context;
			}
			subContext.setAttribute(elementName, obj);
			if (log.isDebugEnabled()) {
				log.debug("ProcessorGroup[" + context.getProcessorGroupName() + "],开始循环处理[" + count + "/" + size + "]" + elementName + "=" + obj);
			}
			for (DataProcessor iteratorProcessor : iteratorProcessors) {
				if (stepCommit) {
					dataProcessTransaction.doInCommit(iteratorProcessor, subContext);
				} else {
					doWithoutCommit(iteratorProcessor, subContext);
				}
			}
			if (this.propagations != null && this.propagations.size() > 0) {
				for (String propagation : propagations) {
					context.setAttribute(propagation, subContext.getAttribute(propagation));
				}
			}
		}
	}

}

// $Id: CollectionIteratorProcessor.java 14913 2010-06-17 03:00:38Z wei.zhang $