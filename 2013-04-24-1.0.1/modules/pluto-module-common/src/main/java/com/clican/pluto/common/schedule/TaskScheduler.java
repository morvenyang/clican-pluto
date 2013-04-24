/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.common.schedule;

import java.util.Date;
import java.util.TimerTask;

public interface TaskScheduler {
    /**
     * Schedule a task associated with a identified id.
     * 
     * @param id a string id used to identify this set of tasks
     * @param task
     */
    public void schedule(String id, ScheduledTask task);

    /**
     * Schedule a one time execution task
     * 
     * @param task
     * @param time
     */
    public void schedule(TimerTask task, Date time);

    /**
     * Cancel the tasks associated with identified id
     * 
     * @param id a string id used to identify this set of tasks
     */
    public void cancel(String id);

    /**
     * Start the scheduler
     */
    public void start();

    /**
     * Shutdown the scheduler
     */
    public void shutdown();
}

// $Id$
