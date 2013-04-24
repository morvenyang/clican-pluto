/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.fsm.engine;

import com.clican.pluto.fsm.model.Event;

public interface EventHandler {

	/**
	 * 处理当前上来的<code>Event</code>
	 * 
	 * @param event
	 */
	public void handle(Event event);
}

// $Id$