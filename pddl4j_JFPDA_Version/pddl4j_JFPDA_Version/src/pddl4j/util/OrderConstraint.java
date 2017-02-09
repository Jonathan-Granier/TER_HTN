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
 * This class implements an order constraint in HTN planning
 * 
 * @author A.Ramoul
 * @version 0.8 - 06.2015
 */
public class OrderConstraint
implements Constraint {
	
	/**
	 * The first part of the order constraint
	 */
	private TaskList firstTaskList;
	
	/**
	 * The second part of the order constraint
	 */
	private TaskList secondTaskList;
	
	/**
	 * Constructor of the order constraint with an other order constraint
	 * 
	 * @param orderCon The affected order constraint
	 */
	public OrderConstraint(final OrderConstraint orderCon) {
		if (orderCon == null) {
			throw new NullPointerException("Order constraint is empty");
		}
		firstTaskList = new TaskList(orderCon.getFirstTask());
		secondTaskList = new TaskList(orderCon.getSecondTask());
	}
	
	/**
	 * Constructor of OrderConstraint with two TaskLists
	 * 
	 * @param fTaskList The first task list of the constraint
	 * @param sTaskList The second task list of the constraint
	 */
	public OrderConstraint(final TaskList fTaskList, final TaskList sTaskList) {
		if (fTaskList == null || sTaskList == null) {
			throw new NullPointerException("one of the task lists is empty");
		}
		firstTaskList = new TaskList(fTaskList);
		secondTaskList = new TaskList(sTaskList);
	}
	
	/**
	 * Constructor of OrderConstraint with two tasks
	 * 
	 * @param firstTask The first task of the constraint
	 * @param secondTask The second task of the constraint
	 */
	public OrderConstraint(final int firstTask, final int secondTask) {
		firstTaskList = new TaskList(true, firstTask);
		secondTaskList = new TaskList(true, secondTask);
	}
	
	/**
	 * Getter of firstTaskList
	 * 
	 * @return a list of task indexes
	 */
	public TaskList getFirstTask() {
		return firstTaskList;
	}
	
	/**
	 * Getter of secondTaskList
	 * 
	 * @return a list of task indexes
	 */
	public TaskList getSecondTask() {
		return secondTaskList;
	}
	
	/**
	 * Setter of firstTaskLIst
	 * 
	 * @param fTaskList The task list to set
	 */
	public void setFirstTaskList(final TaskList fTaskList) {
		if (fTaskList == null) {
			throw new NullPointerException("the task list is empty");
		}
		firstTaskList = fTaskList;
	}
	
	/**
	 * Setter of secondTaskLIst
	 * 
	 * @param sTaskList The task list to set
	 */
	public void setSecondTaskList(final TaskList sTaskList) {
		if (sTaskList == null) {
			throw new NullPointerException("the task list is empty");
		}
		secondTaskList = sTaskList;
	}
	
	/**
	 * Setter of firstTaskList with one task
	 * 
	 * @param fTask The task to set
	 */
	public void setFirstTask(final int fTask) {
		firstTaskList.setLabel(true);
		firstTaskList.setTask(fTask);
	}
	
	/**
	 * Setter of secondTaskList with one task
	 * 
	 * @param sTask The task to set
	 */
	public void setSecondTask(final int sTask) {
		secondTaskList.setLabel(true);
		secondTaskList.setTask(sTask);
	}
	
	/**
	 * Says if an order constraint is primitive or not. Primitive means that it does not contain a task list
	 * 
	 * @return <code> true </code> if the two side of the constrain contain one task and <code> false </code> otherwise.
	 */
	public boolean isPrimitiveConstraint() {
		if (getFirstTask().isOneTask() && getSecondTask().isOneTask()) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Relativize absolute task indexes in an order constraint
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
	 * Returns the hash code value of the OrderConstraint.
	 * 
	 * @return the hash code value of the order constraint.
	 */
	@Override
	public int hashCode() {
		final String orderConString = "" + firstTaskList.hashCode() + "/" + secondTaskList.hashCode();
		final int hashCode = orderConString.hashCode();
		return hashCode;
	}
	
	/**
	 * Return if a specified object is equals to this order constraint. The specified object is equal to the order constraint if and only if the object is an
	 * instance of the class <code>OrderConstraint</code> and it has the same first task list and the same second task list
	 * 
	 * @param obj the specified object to compared.
	 * @return <code>true</code> if the specified object is equal to the order constraint; <code>false</code> otherwise.
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj != null && obj instanceof OrderConstraint) {
			final OrderConstraint other = (OrderConstraint) obj;
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
	 * Gives a string representation of an order constraint
	 * 
	 * @param relevantTasks Table of relevant tasks
	 * @return a string representation of the order constraint
	 */
	public String toString(final List<IntExp> relevantTasks, final List<String> constants, final List<String> types, final List<String> predicates, final List<String> functions, final List<String> tasks) {
		final StringBuffer str = new StringBuffer();
		str.append("SERIES (");
		str.append(firstTaskList.toString(relevantTasks, constants, types, predicates, functions, tasks));
		str.append(", ");
		str.append(secondTaskList.toString(relevantTasks, constants, types, predicates, functions, tasks));
		str.append(")");
		return str.toString();
	}
	
	public String toString(final TaskNetwork taskNetwork, final CodedProblem problem) {
		final StringBuffer str = new StringBuffer();
		str.append("SERIES (");
		str.append(firstTaskList.toString(problem));
		str.append(", ");
		str.append(secondTaskList.toString(problem));
		str.append(")");
		return str.toString();
	}
	
}
