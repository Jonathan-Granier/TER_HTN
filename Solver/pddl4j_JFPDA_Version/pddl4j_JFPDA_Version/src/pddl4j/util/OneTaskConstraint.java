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

import java.util.List;
import pddl4j.preprocessing.CodedProblem;

/**
 * This class implements a one task (before/after) constraint in HTN planning
 * 
 * @author A.Ramoul
 * @version 0.8 - 06.2015
 */
public class OneTaskConstraint
implements Constraint {
	
	/**
	 * The ground predicate of the constraint
	 */
	private int groundPredicate;
	
	/**
	 * The task list of the constraint
	 */
	private TaskList taskList;
	
	/**
	 * Constructor of OneTaskConstraint with a oneTaskConstraint
	 * 
	 * @param constraint the Constraint to copy
	 */
	public OneTaskConstraint(final OneTaskConstraint constraint) {
		if (constraint == null) {
			throw new NullPointerException("Task list is empty");
		}
		groundPredicate = constraint.groundPredicate;
		taskList = new TaskList(constraint.getTaskList());
	}
	
	/**
	 * Constructor of OneTaskConstraint with a ground predicate and a taskList
	 * 
	 * @param literal the ground predicate of the constraint
	 * @param taskList The taskLIst of the constraint
	 */
	public OneTaskConstraint(final int literal, final TaskList taskList) {
		if (taskList == null) {
			throw new NullPointerException("Task list is empty");
		}
		groundPredicate = literal;
		this.taskList = new TaskList(taskList);
	}
	
	/**
	 * Constructor of OneTaskConstraint with a ground predicate and a task
	 * 
	 * @param literal the ground predicate of the constraint
	 * @param task The task of the constraint
	 */
	public OneTaskConstraint(final int literal, final int task) {
		
		groundPredicate = literal;
		taskList = new TaskList(true, task);
	}
	
	/**
	 * Getter of TaskList
	 * 
	 * @return a list of task indexes
	 */
	public TaskList getTaskList() {
		return taskList;
	}
	
	/**
	 * Getter of grounfPredicate
	 * 
	 * @return the index of the ground predicate in the ground predicates table
	 */
	public int getGroundPredicate() {
		return groundPredicate;
	}
	
	/**
	 * Setter of TaskList
	 * 
	 * @param taskList The task list to set
	 */
	public void setTaskList(final TaskList taskList) {
		if (taskList == null) {
			throw new NullPointerException("the task list is empty");
		}
		this.taskList = taskList;
	}
	
	/**
	 * Setter of groundPredicate
	 * 
	 * @param literalIndex The index in the ground predicates table of the literal to set
	 */
	public void setGroundPredicate(final int literalIndex) {
		groundPredicate = literalIndex;
	}
	
	/**
	 * Setter of taskList with one task
	 * 
	 * @param task The task to set
	 */
	public void setTask(final int task) {
		taskList.setLabel(true);
		taskList.setTask(task);
	}
	
	/**
	 * Relativize absolute task indexes in a one task constraint
	 * 
	 * @param absoluteExpansion The list of absolute indexes of the expansion
	 * @param relativeExpansion The list of relative indexes of the expansion
	 */
	public void relativize(final List<Integer> absoluteExpansion, final List<Integer> relativeExpansion) {
		for (int i = 0; i < taskList.getTaskList().size(); i++) {
			final int absoluteTask = taskList.getTaskList().get(i);
			final int taskIndex = absoluteExpansion.indexOf(absoluteTask);
			taskList.getTaskList().set(i, relativeExpansion.get(taskIndex));
		}
	}
	
	/**
	 * Returns the hash code value of the OneTaskConstraint.
	 * 
	 * @return the hash code value of the OneTaskConstraint.
	 */
	@Override
	public int hashCode() {
		final String orderConString = "" + groundPredicate + "/" + taskList.hashCode();
		final int hashCode = orderConString.hashCode();
		return hashCode;
	}
	
	/**
	 * Return if a specified object is equals to this one task constraint. The specified object is equal to the one task constraint if and only if the object is
	 * an instance of the class <code>OneTaskConstraint</code> and it has the same predicate and the same task list
	 * 
	 * @param obj the specified object to compared.
	 * @return <code>true</code> if the specified object is equal to the one task constraint; <code>false</code> otherwise.
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj != null && obj instanceof OneTaskConstraint) {
			final OneTaskConstraint other = (OneTaskConstraint) obj;
			if (hashCode() == other.hashCode()) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	/**
	 * Gives a string representation of a One task Constraint
	 * 
	 * @param relevantTasks Table of relevant tasks
	 * @param revelantFacts Table of relevant facts
	 * @return a string representation of the one task constraint
	 */
	
	public String toString(final List<IntExp> relevantTasks, final List<IntExp> relevantFacts, final List<String> constants, final List<String> types, final List<String> predicates, final List<String> functions, final List<String> tasks) {
		final StringBuffer str = new StringBuffer();
		str.append("(");
		str.append(relevantFacts.get(groundPredicate).toString(constants, types, predicates, functions, tasks) + " ");
		str.append(taskList.toString(relevantTasks, constants, types, predicates, functions, tasks));
		str.append(")");
		return str.toString();
	}
	
	public String toString(final CodedProblem problem) {
		final StringBuffer str = new StringBuffer();
		str.append("(");
		str.append(problem.toString(problem.getRelevantFacts().get(groundPredicate)) + " ");
		str.append(taskList.toString(problem));
		str.append(")");
		return str.toString();
	}
	
}
