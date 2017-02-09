/*
 * Copyright (c) 2015 by 	Abdeldjalil Ramoul <abdeldjalil.ramoul@imag.fr>
 *
 * This file is part of PDDL4J library.
 *
 * PDDL4J is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PDDL4J is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with PDDL4J.  If not, see <http://www.gnu.org/licenses/>
 */

package pddl4j.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import pddl4j.preprocessing.CodedProblem;

/**
 * This class implements a task list in HTN planning.
 * 
 * @author A. Ramoul
 * @version 0.8 - 02.2015
 */

public class TaskList
implements Serializable {
	
	/**
	 * The serial version id of the class.
	 */
	private static final long serialVersionUID = -1847379734071195380L;
	
	/**
	 * The type of task collection <code> true </code> for FIRST and <code> false </code> for LAST
	 */
	private boolean label;
	
	/**
	 * The list of task indexes
	 */
	private List<Integer> taskList;
	
	/**
	 * Create a new taskList
	 */
	public TaskList() {
		label = true;
		taskList = new ArrayList<Integer>();
	}
	
	/**
	 * Create a task collection with a TaskList
	 * 
	 * @param taskList
	 */
	public TaskList(final TaskList taskList) {
		label = taskList.getLabel();
		this.taskList = new ArrayList<Integer>(taskList.getTaskList());
	}
	
	/**
	 * Create a task collection with a list of tasks
	 * 
	 * @param label
	 * @param taskList
	 */
	public TaskList(final boolean label, final List<Integer> taskList) {
		if (taskList == null) {
			throw new NullPointerException("taskList == null");
		}
		this.label = label;
		this.taskList = new ArrayList<Integer>(taskList);
	}
	
	/**
	 * Create a task collection with one task
	 * 
	 * @param label
	 * @param taskList
	 */
	public TaskList(final boolean label, final int taskList) {
		this.taskList = new ArrayList<Integer>();
		this.label = label;
		this.taskList.add(taskList);
	}
	
	/**
	 * Getter of label
	 * 
	 * @return Return the label of the task collection <code> true </code> = First and <code> false </code> = Last
	 */
	public boolean getLabel() {
		return label;
	}
	
	/**
	 * Getter of taskList
	 * 
	 * @return the task list contained in the task collection
	 */
	public List<Integer> getTaskList() {
		return taskList;
	}
	
	/**
	 * Get one if the size of task list equals 1
	 * 
	 * @return the task contained in the task collection
	 */
	public int getUniqueTask() {
		if (taskList.size() == 1) {
			return taskList.get(0);
		} else {
			return -99;
		}
	}
	
	/**
	 * Get the task with the specified index in the taskCollection
	 * 
	 * @return the task contained in the task collection
	 */
	public int getTaskWithIndex(final int index) {
		return taskList.get(index);
	}
	
	/**
	 * Pop the task with the specified index in the taskCollection
	 * 
	 * @return the task contained in the task collection
	 */
	public int popTaskWithIndex(final int index) {
		final int task = taskList.get(index);
		taskList.remove(index);
		return task;
	}
	
	/**
	 * Get the indexes of a specified task in the taskCollection
	 * 
	 * @return a list of indexes for a specified task
	 */
	public List<Integer> indexesOf(final int task) {
		final List<Integer> correspondingTasksIndexes = new ArrayList<Integer>();
		for (int i = 0; i < taskList.size(); i++) {
			if (taskList.get(i) == task) {
				correspondingTasksIndexes.add(i);
			}
		}
		return correspondingTasksIndexes;
	}
	
	/**
	 * Get the size of task List
	 * 
	 * @return The number of tasks in the collection
	 */
	public int size() {
		return taskList.size();
	}
	
	/**
	 * Return if a task collection contains one task or not
	 * 
	 * @return <code> true </code> if the collection contains one task <code> false </code> otherwise
	 */
	public boolean isOneTask() {
		return (taskList.size() == 1);
	}
	
	/**
	 * Setter of label
	 * 
	 * @param label The affected to task Collection it can be <code> true </code> = First or <code> false </code> = Last
	 */
	public void setLabel(final boolean label) {
		this.label = label;
	}
	
	/**
	 * Setter of TaskList with a task list
	 * 
	 * @param taskList The list of tasks to set into the taskList
	 */
	public void setTaskList(final List<Integer> taskList) {
		this.taskList = taskList;
	}
	
	/**
	 * Setter of TaskList with one task
	 * 
	 * @param task The task to set into the taskList
	 */
	public void setTask(final int task) {
		taskList.clear();
		taskList.add(task);
	}
	
	/**
	 * Add a new task to the collection
	 * 
	 * @param task The task to add
	 */
	public void addTask(final int task) {
		taskList.add(task);
	}
	
	/**
	 * Add a new task list to the task collection
	 * 
	 * @param taskCollection The list of tasks to add
	 */
	public void addTaskCollection(final List<Integer> taskCollection) {
		taskList.addAll(taskCollection);
	}
	
	/**
	 * Test if a task list contains only primitive tasks
	 * 
	 * @param primitiveTasks The list of primitive tasks of task network
	 * @return <code> true </code> if the task list contains only primitive tasks and <code> false </code> otherwise
	 */
	public boolean isPrimitive(final List<Integer> primitiveTasks) {
		for (final int task : getTaskList()) {
			if (!primitiveTasks.contains(task)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Returns the hash code value of the task list.
	 * 
	 * @return the hash code value of the task list.
	 */
	@Override
	public int hashCode() {
		String taskListString = "";
		if (getTaskList().size() > 1) {
			taskListString = "" + label + ":";
		}
		for (final int task : taskList) {
			taskListString = taskListString + "/" + task;
		}
		final int hashCode = taskListString.hashCode();
		return hashCode;
	}
	
	/**
	 * Return if a specified object is equals to this taskList. The specified object is equal to the taskList if and only if the object is an instance of the
	 * class <code>TaskLIst</code> and it has the same label and the same list of tasks.
	 * 
	 * @param obj the specified object to compared.
	 * @return <code>true</code> if the specified object is equal to the taskList; <code>false</code> otherwise.
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj != null && obj instanceof TaskList) {
			final TaskList other = (TaskList) obj;
			if (hashCode() == other.hashCode()) {
				return false;
			} else {
				for (int i = 0; i < taskList.size(); i++) {
					if (taskList.get(i) != other.taskList.get(i)) {
						return false;
					}
				}
			}
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Gives a string representation of a taskList
	 * 
	 * @param relevantTasks Table of relevant tasks
	 * @return a string representation of the task list
	 */
	public String toString(final List<IntExp> relevantTasks, final List<String> constants, final List<String> types, final List<String> predicates, final List<String> functions, final List<String> tasks) {
		final StringBuffer str = new StringBuffer();
		if (label) {
			str.append("FIRST (");
		} else {
			str.append("LAST (");
		}
		for (final int task : taskList) {
			str.append(relevantTasks.get(task).toString(constants, types, predicates, functions, tasks) + " ");
		}
		str.append(")");
		return str.toString();
	}
	
	public String toString(final CodedProblem problem) {
		final StringBuffer str = new StringBuffer();
		if (label) {
			str.append("FIRST (");
		} else {
			str.append("LAST (");
		}
		for (final int task : taskList) {
			str.append(problem.printTask(task));
		}
		str.append(")");
		return str.toString();
	}
}
