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

import java.util.ArrayList;
import java.util.List;
import pddl4j.preprocessing.CodedProblem;

/**
 * This class implements a task network in HTN planning.
 * 
 * @author A. Ramoul
 * @version 0.8 - 02.2015
 */
public abstract class Generic_TN {
	
	/**
	 * The list of task indexes contained in the expansion
	 */
	protected List<Integer> tasks;
	
	/**
	 * Default constructor of the TN
	 */
	public Generic_TN() {
		tasks = new ArrayList<Integer>();
	}
	
	/**
	 * The constructor of TaskNetwork
	 * 
	 * @param tn the Task Network used to create the new TN
	 */
	public Generic_TN(final Generic_TN tn) {
		if (tn.tasks == null) {
			throw new NullPointerException("The expansion on the Task Network is empty");
		}
		tasks = new ArrayList<>(tn.tasks);
	}
	
	/**
	 * The constructor of TaskNetwork
	 * 
	 * @param tasks Contained in the expansion
	 * @param ordering The list of ordering Constraints
	 * @param before The list of before Constraints
	 * @param after The list of after Constraints
	 * @param between The list of between Constraints
	 */
	public Generic_TN(final List<Integer> tasks, final List<OrderConstraint> ordering, final List<OneTaskConstraint> before, final List<OneTaskConstraint> after, final List<TwoTasksConstraint> between) {
		this.tasks = new ArrayList<Integer>(tasks);
	}
	
	/**
	 * Getter of tasks
	 * 
	 * @return the list of task indexes contained in the expansion
	 */
	public final List<Integer> getTasks() {
		return tasks;
	}
	
	/**
	 * Setter of tasks
	 * 
	 * @param tasks The list of task indexes to set
	 */
	public final void setTasks(final List<Integer> tasks) {
		this.tasks = tasks;
	}
	
	/**
	 * Gets the max index in the task list
	 * 
	 * @param currentTask The index of current Task which has not been removed yet from order constraints
	 * @return the max index contained in the task List
	 */
	public int getMaxTaskIndex(final int currentTask) {
		int maxIndex = currentTask;
		for (final int index : tasks) {
			if (maxIndex < index) {
				maxIndex = index;
			}
		}
		return maxIndex;
	}
	
	/**
	 * Adds a list of tasks to task network tasks
	 * 
	 * @param tasks The specified tasks list
	 * @param tasksNumber The number of tasks in the domain
	 * @return <code> true </code> if the tasks were added and <code> false </code> otherwise.
	 */
	public boolean addTasks(final List<Integer> tasks, final int tasksNumber) {
		return this.tasks.addAll(getRelativeIndexes(tasks, tasksNumber));
	}
	
	/**
	 * Calculates the relative indexes of specified tasks in a task list
	 * 
	 * @param tasks The specified tasks list
	 * @param tasksNumber The number of tasks in the domain
	 * @return the relative indexes of the tasks.
	 */
	public List<Integer> getRelativeIndexes(final List<Integer> tasks, final int tasksNumber) {
		final List<Integer> relativeTasks = new ArrayList<Integer>(tasks.size());
		for (final int task : tasks) {
			relativeTasks.add(getRelativeIndex(task, tasksNumber));
		}
		return relativeTasks;
	}
	
	/**
	 * Calculates the relative index of a specified task in a task list
	 * 
	 * @param task The specified task
	 * @param tasksNumber The number of tasks in the domain
	 * @return the relative index of the task.
	 */
	public int getRelativeIndex(final int task, final int tasksNumber) {
		final int occurrence = getMaxOccurrence(task, tasks, tasksNumber);
		return (occurrence * tasksNumber) + task;
	}
	
	/**
	 * Find the max number of occurrence for a specified task
	 * 
	 * @param task The task for which we search occurrence
	 * @param targetTasksList The tasks list in which we search occurrence
	 * @param tasksNumber The number of tasks in the domain it correspond to the size of relevant tasks table
	 * @return the occurrence number of the specified task in the task list
	 */
	public int getMaxOccurrence(final int task, final List<Integer> targetTasksList, final int tasksNumber) {
		int maxOccurrence = 0;
		for (final int realTargetTask : targetTasksList) {
			final int modTargetTask = realTargetTask % tasksNumber;
			final int occurrenceTargetTask = realTargetTask / tasksNumber;
			if (task == modTargetTask) {
				maxOccurrence = (maxOccurrence <= occurrenceTargetTask) ? occurrenceTargetTask + 1 : maxOccurrence;
			}
		}
		return maxOccurrence;
	}
	
	/**
	 * Calculate the reference index for a task. It corresponds to the index of the task in the relevant tasks table
	 * 
	 * @param task The task for which we search the reference index
	 * @param tasksNumber The number of tasks in the domain it correspond to the size of relevant tasks table
	 * @return The reference index for the specified task
	 */
	public int getAbsoluteIndex(final int task, final int tasksNumber) {
		return task % tasksNumber;
	}
	
	/**
	 * Calculate the occurrence of a task.
	 * 
	 * @param task The task for which we search the occurrence
	 * @param tasksNumber The number of tasks in the domain it correspond to the size of relevant tasks table
	 * @return The occurrence for the specified task
	 */
	public int getOccurrence(final int task, final int tasksNumber) {
		return task / tasksNumber;
	}
	
	public String toString(final CodedProblem problem) {
		final StringBuffer str = new StringBuffer();
		str.append("\tExpansion:\n");
		for (final int task : tasks) {
			final int absoluteTask = getAbsoluteIndex(task, problem.getRelevantTasks().size());
			final int occurrence = getOccurrence(task, problem.getRelevantTasks().size());
			str.append("\t\t" + problem.toString(problem.getRelevantTasks().get(absoluteTask)) + occurrence + "\n");
		}
		return str.toString();
	}
	
	public String printTasks(final CodedProblem problem) {
		final StringBuffer str = new StringBuffer();
		str.append("\tTasks:\n");
		for (final int task : tasks) {
			final int absoluteTask = getAbsoluteIndex(task, problem.getRelevantTasks().size());
			final int occurrence = getOccurrence(task, problem.getRelevantTasks().size());
			str.append("\t\t" + problem.toString(problem.getRelevantTasks().get(absoluteTask)) + occurrence + "\n");
		}
		return str.toString();
		
	}
	
}
