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
 * This class implements a two tasks (between) constraint in HTN planning
 * 
 * @author A.Ramoul
 * @version 0.8 - 06.2015
 */
public class TwoTasksConstraint
implements Constraint {
	
	/**
	 * The ground predicate of the constraint
	 */
	private int groundPredicate;
	
	/**
	 * The first task list of the constraint
	 */
	private TaskList firstTaskList;
	
	/**
	 * The second task list of the constraint
	 */
	private TaskList secondTaskList;
	
	/**
	 * Constructor of TwoTasksConstraint with an other TwoTasksConstraint
	 * 
	 * @param constraint The constraint used to create the new one
	 */
	public TwoTasksConstraint(final TwoTasksConstraint constraint) {
		if (constraint == null) {
			throw new NullPointerException("Task list is empty");
		}
		groundPredicate = constraint.groundPredicate;
		firstTaskList = new TaskList(constraint.getFirstTaskList());
		secondTaskList = new TaskList(constraint.getSecondTaskList());
	}
	
	/**
	 * Constructor of TwoTasksConstraint with a ground predicate and two taskLists
	 * 
	 * @param literal the ground predicate of the constraint
	 * @param firstTaskList The first taskList of the constraint
	 * @param secondTaskList The second taskList of the constraint
	 */
	public TwoTasksConstraint(final int literal, final TaskList firstTaskList, final TaskList secondTaskList) {
		if (firstTaskList == null || secondTaskList == null) {
			throw new NullPointerException("One of the task lists is empty");
		}
		groundPredicate = literal;
		firstTaskList.setLabel(false);
		secondTaskList.setLabel(true);
		this.firstTaskList = new TaskList(firstTaskList);
		this.secondTaskList = new TaskList(secondTaskList);
	}
	
	/**
	 * Constructor of TwoTasksConstraint with a ground predicate and two tasks
	 * 
	 * @param literal the ground predicate of the constraint
	 * @param firstTask The first task of the constraint
	 * @param secondTask The second task of the constraint
	 */
	public TwoTasksConstraint(final int literal, final int firstTask, final int secondTask) {
		
		groundPredicate = literal;
		firstTaskList = new TaskList(false, firstTask);
		secondTaskList = new TaskList(true, secondTask);
	}
	
	/**
	 * Constructor of TwoTasksConstraint with a ground predicate, one task and one task list
	 * 
	 * @param literal the ground predicate of the constraint
	 * @param firstTask The first task of the constraint
	 * @param secondTaskList The second task list of the constraint
	 */
	public TwoTasksConstraint(final int literal, final int firstTask, final TaskList secondTaskList) {
		if (secondTaskList == null) {
			throw new NullPointerException("task lists is empty");
		}
		groundPredicate = literal;
		firstTaskList = new TaskList(true, firstTask);
		this.secondTaskList = new TaskList(secondTaskList);
	}
	
	/**
	 * Constructor of TwoTasksConstraint with a ground predicate, one task and one task list
	 * 
	 * @param literal the ground predicate of the constraint
	 * @param firstTaskList The first task list of the constraint
	 * @param secondTask The second task of the constraint
	 */
	public TwoTasksConstraint(final int literal, final TaskList firstTaskList, final int secondTask) {
		if (firstTaskList == null) {
			throw new NullPointerException("task lists is empty");
		}
		groundPredicate = literal;
		this.firstTaskList = new TaskList(firstTaskList);
		secondTaskList = new TaskList(true, secondTask);
	}
	
	/**
	 * Getter of firstTaskList
	 * 
	 * @return a list of task indexes
	 */
	public TaskList getFirstTaskList() {
		return firstTaskList;
	}
	
	/**
	 * Getter of secondTaskList
	 * 
	 * @return a list of task indexes
	 */
	public TaskList getSecondTaskList() {
		return secondTaskList;
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
	 * Setter of firstTaskList
	 * 
	 * @param taskList The task list to set
	 */
	public void setFirstTaskList(final TaskList taskList) {
		if (taskList == null) {
			throw new NullPointerException("the task list is empty");
		}
		firstTaskList = taskList;
	}
	
	/**
	 * Setter of secondTaskList
	 * 
	 * @param taskList The task list to set
	 */
	public void setSecondTaskList(final TaskList taskList) {
		if (taskList == null) {
			throw new NullPointerException("the task list is empty");
		}
		secondTaskList = taskList;
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
	 * Setter of firstTaskList with one task
	 * 
	 * @param task The task to set
	 */
	public void setFirstTask(final int task) {
		firstTaskList.setLabel(true);
		firstTaskList.setTask(task);
	}
	
	/**
	 * Setter of secondTaskList with one task
	 * 
	 * @param task The task to set
	 */
	public void setSecondTask(final int task) {
		secondTaskList.setLabel(true);
		secondTaskList.setTask(task);
	}
	
	/**
	 * Relativize absolute task indexes in two tasks constraint
	 * 
	 * @param absoluteExpansion The list of absolute indexes of the expansion
	 * @param relativeExpansion The list of relative indexes of the expansion
	 */
	public void relativize(final List<Integer> absoluteExpansion, final List<Integer> relativeExpansion) {
		for (int i = 0; i < firstTaskList.getTaskList().size(); i++) {
			final int taskIndex = absoluteExpansion.indexOf(firstTaskList.getTaskList().get(i));
			firstTaskList.getTaskList().set(i, relativeExpansion.get(taskIndex));
		}
		for (int i = 0; i < secondTaskList.getTaskList().size(); i++) {
			final int taskIndex = absoluteExpansion.indexOf(secondTaskList.getTaskList().get(i));
			secondTaskList.getTaskList().set(i, relativeExpansion.get(taskIndex));
		}
	}
	
	/**
	 * Returns the hash code value of the TwoTasksConstraint.
	 * 
	 * @return the hash code value of the TwoTasksConstraint.
	 */
	@Override
	public int hashCode() {
		final String orderConString = "" + groundPredicate + "/" + firstTaskList.hashCode() + "/" + secondTaskList.hashCode();
		final int hashCode = orderConString.hashCode();
		return hashCode;
	}
	
	/**
	 * Return if a specified object is equals to this two tasks constraint. The specified object is equal to the two tasks constraint if and only if the object
	 * is an instance of the class <code>TwoTasksConstraint</code> and it has the same predicate and the same task lists
	 * 
	 * @param obj the specified object to compared.
	 * @return <code>true</code> if the specified object is equal to the two tasks constraint; <code>false</code> otherwise.
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj != null && obj instanceof TwoTasksConstraint) {
			final TwoTasksConstraint other = (TwoTasksConstraint) obj;
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
	 * Gives a string representation of a Two tasks Constraint
	 * 
	 * @param relevantTasks Table of relevant tasks
	 * @param revelantFacts Table of relevant facts
	 * @return a string representation of the two tasks constraint
	 */
	
	public String toString(final List<IntExp> relevantTasks, final List<IntExp> relevantFacts, final List<String> constants, final List<String> types, final List<String> predicates, final List<String> functions, final List<String> tasks) {
		final StringBuffer str = new StringBuffer();
		str.append("(");
		str.append(relevantFacts.get(groundPredicate).toString(constants, types, predicates, functions, tasks) + " ");
		str.append(firstTaskList.toString(relevantTasks, constants, types, predicates, functions, tasks) + " ");
		str.append(secondTaskList.toString(relevantTasks, constants, types, predicates, functions, tasks));
		str.append(")");
		return str.toString();
	}
	
	public String toString(final TaskNetwork taskNetwork, final CodedProblem problem) {
		final StringBuffer str = new StringBuffer();
		str.append("(");
		str.append(problem.toString(problem.getRelevantFacts().get(groundPredicate)) + " ");
		str.append(firstTaskList.toString(problem) + " ");
		str.append(secondTaskList.toString(problem));
		str.append(")");
		return str.toString();
	}
	
}
