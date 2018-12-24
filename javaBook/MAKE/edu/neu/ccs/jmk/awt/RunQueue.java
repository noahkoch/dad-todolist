// $Id: RunQueue.java,v 1.1.1.1 2000/09/26 11:20:35 ramsdell Exp $

// A queue for runnable tasks

/*
 * Copyright 1998 by John D. Ramsdell
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

package edu.neu.ccs.jmk.awt;

import java.util.Vector;

/**
 * A queue for runnable tasks.
 * The run method sequentially invokes tasks in the queue.
 * The run queue is meant to be run in its own thread.
 * @version January 1998
 * @author John D. Ramsdell
 */
public class RunQueue
implements Runnable
{
  // A queue of runnables
  private final Vector queue = new Vector();

  // Indicates that tasks extracted from the queue should not be started
  private volatile boolean interrupt = false;

  // Indicates that the run method should exit
  private volatile boolean poisoned = false;

  /**
   * Stop the run method.
   * To stop the run method, poison it, 
   * and then interrupt the thread running it.
   */
  public void setPoisoned(boolean poisoned) {
    this.poisoned = poisoned;
  }

  /**
   * Add a task to the run queue.
   * @param task a runnable task
   */
  public synchronized void add(Runnable task) {
    queue.addElement(task);
    notify();
  }

  /**
   * Remove all tasks from the run queue.
   */
  public synchronized void removeAll() {
    interrupt = true;		// Stop all tasks that 
    queue.removeAllElements();	// have not been started
  }

  /**
   * Runs tasks in a task queue.
   */
  public void run() {
    Vector q;
    while (!poisoned) {
      try {
	synchronized (this) {
	  while (queue.isEmpty())
	    wait();
	  q = (Vector)queue.clone(); // Extract tasks from the queue
	  queue.removeAllElements();
	  interrupt = false;
	}
      }
      catch (InterruptedException ie) {
	continue;		// q is null so just continue loop
      }
      // Abort loop when the interrupt is enabled
      for (int i = 0; i < q.size() && !interrupt; i++) {
	Runnable task = (Runnable)q.elementAt(i);
	try {
	  task.run();
	}
	catch (Throwable t) {
	  System.err.println("Internal error: " + t.getMessage());
	  t.printStackTrace();
	}
      }
      q = null;			// Recycle queue.
    }
  }
}
