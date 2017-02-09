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
public class TaskNetwork {
	
	/**
	 * The list of task indexes contained in the expansion
	 */
	protected List<Integer> tasks;
	
	/**
	 * The list of ordering constraints on the Task Network
	 */
	protected List<OrderConstraint> ordering;
	
	/**
	 * The list of before constraints on the Task Network
	 */
	protected List<OneTaskConstraint> before;
	
	/**
	 * The list of after constraints on the Task Network
	 */
	protected List<OneTaskConstraint> after;
	
	/**
	 * The list of between constraints on the Task Network
	 */
	protected List<TwoTasksConstraint> between;
	
	/**
	 * Default constructor of the TN
	 */
	public TaskNetwork() {
		tasks = new ArrayList<Integer>();
		ordering = new ArrayList<OrderConstraint>();
		before = new ArrayList<OneTaskConstraint>();
		after = new ArrayList<OneTaskConstraint>();
		between = new ArrayList<TwoTasksConstraint>();
	}
	
	/**
	 * The constructor of TaskNetwork
	 * 
	 * @param tn the Task Network used to create the new TN
	 */
	public TaskNetwork(final TaskNetwork tn) {
		if (tn.tasks == null) {
			throw new NullPointerException("The expansion on the Task Network is empty");
		}
		tasks = new ArrayList<Integer>(tn.tasks);
		ordering = new ArrayList<OrderConstraint>(tn.ordering);
		before = new ArrayList<OneTaskConstraint>(tn.before);
		after = new ArrayList<OneTaskConstraint>(tn.after);
		between = new ArrayList<TwoTasksConstraint>(tn.between);
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
	public TaskNetwork(final List<Integer> tasks, final List<OrderConstraint> ordering, final List<OneTaskConstraint> before, final List<OneTaskConstraint> after, final List<TwoTasksConstraint> between) {
		this.tasks = new ArrayList<Integer>(tasks);
		this.ordering = new ArrayList<OrderConstraint>(ordering);
		this.before = new ArrayList<OneTaskConstraint>(before);
		this.after = new ArrayList<OneTaskConstraint>(after);
		this.between = new ArrayList<TwoTasksConstraint>(between);
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
	 * Getter of ordering
	 * 
	 * @return The list of order constraints
	 */
	public final List<OrderConstraint> getOrderingConstraints() {
		return ordering;
	}
	
	/**
	 * The setter of ordering
	 * 
	 * @param ordering The list of order constraints to set
	 */
	public final void setOrderingConstraints(final List<OrderConstraint> ordering) {
		this.ordering = ordering;
	}
	
	/**
	 * Getter of before
	 * 
	 * @return The list of before constraints
	 */
	public final List<OneTaskConstraint> getBeforeConstraints() {
		return before;
	}
	
	/**
	 * The getter of before
	 * 
	 * @param before The list of before constraints to set
	 */
	public final void setBeforeConstraints(final List<OneTaskConstraint> before) {
		this.before = before;
	}
	
	/**
	 * The getter of after
	 * 
	 * @return The list of after constraints
	 */
	public final List<OneTaskConstraint> getAfterConstraints() {
		return after;
	}
	
	/**
	 * The setter of after
	 * 
	 * @param after the list of after constraints to set
	 */
	public final void setAfterConstraints(final List<OneTaskConstraint> after) {
		this.after = after;
	}
	
	/**
	 * The getter of between
	 * 
	 * @return The list of between constraints
	 */
	public final List<TwoTasksConstraint> getBetweenConstraints() {
		return between;
	}
	
	/**
	 * The setter of between
	 * 
	 * @param between The list of between parameters to set
	 */
	public final void setBetweenConstraints(final List<TwoTasksConstraint> between) {
		this.between = between;
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
	
	public String toString(final CodedProblem problem) {
		final StringBuffer str = new StringBuffer();
		str.append("\tExpansion:\n");
		for (final int task : tasks) {
			final int absoluteTask = problem.getAbsoluteIndex(task);
			final int occurrence = problem.getOccurrence(task);
			str.append("\t\t" + problem.toString(problem.getRelevantTasks().get(absoluteTask)) + occurrence + "\n");
		}
		str.append("\tOrder Constraints:\n");
		for (final OrderConstraint orderCon : ordering) {
			str.append("\t\t" + orderCon.toString(this, problem) + " / " + orderCon.hashCode() + "\n");
		}
		
		str.append("\tBefore Constraints:\n");
		for (final OneTaskConstraint beforeCon : before) {
			str.append("\t\t" + beforeCon.toString(problem) + "\n");
		}
		
		str.append("\tAfter Constraints:\n");
		for (final OneTaskConstraint afterCon : after) {
			str.append("\t\t" + afterCon.toString(problem) + "\n");
		}
		
		str.append("\tBetween Constraints:\n");
		for (final TwoTasksConstraint betweenCon : between) {
			str.append("\t\t" + betweenCon.toString(this, problem) + "\n");
		}
		
		return str.toString();
	}
	
	public String printTasks(final CodedProblem problem) {
		final StringBuffer str = new StringBuffer();
		str.append("\tTasks:\n");
		for (final int task : tasks) {
			str.append("\t\t" + problem.printTask(task) + "\n");
		}
		return str.toString();
		
	}
	
}
