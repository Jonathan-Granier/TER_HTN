/*
 * Copyright (c) 2010 by Abdeldjalil Ramoul <abdeldjalil.ramoul@imag.fr>.
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
import java.util.Stack;

/**
 * This class implements an adjacency matrix to represent constraints and order
 * graphs
 * 
 * @author Ramoul
 *
 */
public class AdjacencyMatrix {

	/**
	 * The size of the adjacency matrix
	 */
	public int			size;

	/**
	 * The adjacency matrix
	 */
	private int[][]	matrix;

	/**
	 * Creates a new adjacency matrix of size <code> size </code>
	 * 
	 * @param size The size of the adjacency matrix
	 */
	public AdjacencyMatrix(int size) {
		this.size = size;
		this.matrix = new int[size][size];
	}

	/**
	 * Adds a new positive link (+1) in the adjacency matrix
	 * 
	 * @param i the vertical index of the added link
	 * @param j the horizontal index of the added link
	 * @return <code> true </code> if the link is added and
	 *         <code> false </false> otherwise
	 */
	public boolean addPositiveLink(int i, int j) {
		boolean success = true;
		this.matrix[i][j] = 1;
		success = testCircuit(i);
		if (!success)
			return true;
		else {
			this.matrix[i][j] = 0;
			return false;
		}
	}

	/**
	 * Adds a new negative link (-1) in the adjacency matrix
	 * 
	 * @param i the vertical index of the added link
	 * @param j the horizontal index of the added link
	 * @return <code> true </code> if the link is added and
	 *         <code> false </false> otherwise
	 */
	public boolean addNegativeLink(int i, int j) {
		boolean success = true;

		this.matrix[i][j] = -1;
		success = testCircuit(i);
		if (success)
			return true;
		else {
			this.matrix[i][j] = 0;
			return false;
		}
	}

	public void removeLink(int i, int j) {
		this.matrix[i][j] = 0;
	}

	public void clean() {
		for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j++)
				this.matrix[i][j] = 0;
	}

	/**
	 * Tests if the link creates a circuit in the graph represented by the
	 * adjacency matrix
	 * 
	 * @param indexJ The horizontal index of the tested link
	 * @return <code> true </code> if the link creates a circuit and
	 *         <code> false </false> otherwise
	 */
	public boolean testCircuit(final int indexJ) {
		ArrayList<Integer> inIndexes = new ArrayList<Integer>();
		for (int i = 0; i < this.matrix.length; i++)
			if (this.matrix[i][indexJ] == 1)
				inIndexes.add(i);
		if (inIndexes.isEmpty())
			return false;
		else
			return testPath(inIndexes, indexJ);
	}

	/**
	 * Tests if the in links of the node creating the tested link ares reachable
	 * from its out links
	 * 
	 * @param inIndexes the in links of the node creating the tested link
	 * @param indexI The index of the node creating the tested link
	 * @return <code> true </code> if the in links are reachable and
	 *         <code> false </false> otherwise
	 */
	public boolean testPath(final List<Integer> inIndexes, final int indexI) {
		boolean circuit = false;
		ArrayList<Integer> outIndexes = new ArrayList<Integer>();
		for (int j = 0; j < this.matrix.length; j++)
			if (this.matrix[indexI][j] == 1)
				if (inIndexes.contains(j))
					return true;
				else
					outIndexes.add(j);
		for (int index : outIndexes) {
			circuit = testPath(inIndexes, index);
			// if there is a circuit break the loop and return false
			if (circuit)
				return true;
		}
		return circuit;
	}

	/**
	 * Gets the indexes of predecessors of a specified task index (A column in the
	 * adjacencyMatrix)
	 * 
	 * @param taskIndex The index of the task in question
	 * @return the list of predecessors tasks indexes
	 */
	public List<Integer> getPredecessors(int taskIndex) {
		ArrayList<Integer> predecessors = new ArrayList<Integer>();
		for (int i = 0; i < this.size; i++) {
			if (this.matrix[i][taskIndex] == 1)
				predecessors.add(i);
		}
		return predecessors;
	}

	/**
	 * Gets the indexes of direct successors of a specified task index (A line in
	 * the adjacencyMatrix)
	 * 
	 * @param taskIndex The index of the task in question
	 * @return the list of successors tasks indexes
	 */
	public List<Integer> getSuccessors(int taskIndex) {
		ArrayList<Integer> successors = new ArrayList<Integer>();
		for (int j = 0; j < this.size; j++) {
			if (this.matrix[taskIndex][j] == 1)
				successors.add(j);
		}
		return successors;
	}

	/**
	 * Gets the indexes of direct predecessors of a specified task index in a set
	 * of task indexes
	 * 
	 * @param taskIndex The index of the task in question
	 * @param in The task list in which we look for predecessors
	 * @return the list of predecessors tasks indexes
	 */
	public List<Integer> getPredecessorsIn(int taskIndex, List<Integer> in) {
		ArrayList<Integer> successors = new ArrayList<Integer>();
		for (int i : in) {
			if (this.matrix[i][taskIndex] == 1)
				successors.add(i);
		}
		return successors;
	}

	/**
	 * Gets the indexes of direct successors of a specified task index in a set of
	 * task indexes
	 * 
	 * @param taskIndex The index of the task in question
	 * @param in The task list in which we look for successors
	 * @return the list of successors tasks indexes
	 */
	public List<Integer> getSuccessorsIn(int taskIndex, List<Integer> in) {
		ArrayList<Integer> successors = new ArrayList<Integer>();
		for (int j : in) {
			if (this.matrix[taskIndex][j] == 1)
				successors.add(j);
		}
		return successors;
	}

	/**
	 * Says if Some node in firstList has successor in secondList
	 * 
	 * @param firstList The first tasks list
	 * @param secondList The second tasks list
	 * @return <code> true </code> if there is at least a link between a node in
	 *         firstList and secondList and <code> false </code> otherwise
	 */
	public boolean someSuccessorsIn(List<Integer> firstList, List<Integer> secondList) {
		for (int firstTask : firstList)
			if (!this.getSuccessorsIn(firstTask, secondList).isEmpty())
				return true;
		return false;
	}

	/**
	 * Gets the tasks of A0 which don't have predecessors in A0 union A1
	 * 
	 * @param A0 The first task list in an order constraint
	 * @param A1 The second task list in an order constraint
	 * @return The set Oi containing tasks without predecessors in A0 union A1
	 */
	public List<Integer> getOi(List<Integer> A0, List<Integer> A1) {
		ArrayList<Integer> Oi = new ArrayList<Integer>();
		ArrayList<Integer> A = new ArrayList<Integer>(A0);
		A.addAll(A1);
		for (int task : A0) {
			if (this.getPredecessorsIn(task, A).isEmpty()) {
				Oi.add(task);
			}
		}
		return Oi;
	}

	/**
	 * Gets the tasks of A0 which don't have successors in A0 union A1
	 * 
	 * @param A0 The first task list in an order constraint
	 * @param A1 The second task list in an order constraint
	 * @return The set Li containing tasks without successors in A0 union A1
	 */
	public List<Integer> getLi(List<Integer> A0, List<Integer> A1) {
		ArrayList<Integer> Li = new ArrayList<Integer>();
		ArrayList<Integer> A = new ArrayList<Integer>(A0);
		A.addAll(A1);
		for (int task : A0) {
			if (this.getSuccessorsIn(task, A).isEmpty()) {
				Li.add(task);
			}
		}
		return Li;
	}

	/**
	 * Gets the tasks of A0 which don't have predecessors in A1 and there exists
	 * at least one node in A1 that is not their predecessor.
	 * 
	 * @param A0 The first task list in an order constraint
	 * @param A1 The second task list in an order constraint
	 * @return The set Bi containing tasks as defined in UMCP
	 */
	public List<Integer> getBi(List<Integer> A0, List<Integer> A1) {
		ArrayList<Integer> Bi = new ArrayList<Integer>();
		for (int task : A0) {
			if (this.getPredecessorsIn(task, A1).isEmpty() && this.getPredecessorsIn(task, A0).size() < A0.size() - 1) {
				Bi.add(task);
			}
		}
		return Bi;
	}

	/**
	 * Gets the tasks of A0 which don't have successors in A1 and there exists at
	 * least one node in A1 that is not their successor.
	 * 
	 * @param A0 The first task list in an order constraint
	 * @param A1 The second task list in an order constraint
	 * @return The set Ci containing tasks as defined in UMCP
	 */
	public List<Integer> getCi(List<Integer> A0, List<Integer> A1) {
		ArrayList<Integer> Ci = new ArrayList<Integer>();
		for (int task : A0) {
			if (this.getSuccessorsIn(task, A1).isEmpty() && this.getSuccessorsIn(task, A0).size() < A0.size() - 1) {
				Ci.add(task);
			}
		}
		return Ci;
	}

	/**
	 * Tests if a task is ordered after an other task
	 * 
	 * @param secondTask The task tested to be after first task
	 * @param firstTask The task tested to be before second task
	 * @return <code> true </code> if secondTask is ordered after first task
	 *         <code> false </code> otherwise
	 */
	public boolean isOrderedAfter(final int secondTask, final int firstTask) {
		boolean orderedAfter = false;
		Stack<Integer> successorsStack = new Stack<Integer>();
		successorsStack.addAll(this.getSuccessors(firstTask));
		while (!successorsStack.isEmpty()) {
			int successor = successorsStack.pop();
			if (successor == secondTask)
				return true;
			else
				successorsStack.addAll(this.getSuccessors(successor));
		}
		return orderedAfter;
	}

	/**
	 * get all tasks ordered after a specified task
	 * 
	 * @param task The specified task
	 * @return The list of tasks ordered after the specified task
	 */
	public List<Integer> allTasksOrderedAfter(final int task) {
		List<Integer> orderedAfterTasks = new ArrayList<Integer>();
		Stack<Integer> successorsStack = new Stack<Integer>();
		successorsStack.addAll(this.getSuccessors(task));
		while (!successorsStack.isEmpty()) {
			int successor = successorsStack.pop();
			orderedAfterTasks.add(successor);
			successorsStack.addAll(this.getSuccessors(successor));
		}
		return orderedAfterTasks;
	}

	/**
	 * get all tasks ordered before a specified task
	 * 
	 * @param task The specified task
	 * @return The list of tasks ordered before the specified task
	 */
	public List<Integer> allTasksOrderedBefore(final int task) {
		List<Integer> orderedBeforeTasks = new ArrayList<Integer>();
		Stack<Integer> predecessorsStack = new Stack<Integer>();
		predecessorsStack.addAll(this.getPredecessors(task));
		while (!predecessorsStack.isEmpty()) {
			int successor = predecessorsStack.pop();
			orderedBeforeTasks.add(successor);
			predecessorsStack.addAll(this.getSuccessors(successor));
		}
		return predecessorsStack;
	}

	/**
	 * Computes the list of shadowed tasks -> tasks ordered between a landmark
	 * task and another task in the task list
	 * 
	 * @param taskList The list of tasks
	 * @param task The landmark task
	 * @return The list of shadowed tasks
	 */
	public List<Integer> shadowedTasksList(List<Integer> taskList, final int task) {

		List<Integer> shadowedTasksList = new ArrayList<Integer>();
		for (int shadowerTask : taskList) {
			for (int shadowedTask : taskList) {
				if (shadowerTask != shadowedTask && this.isOrderedBetween(shadowerTask, shadowedTask, task)) {
					shadowedTasksList.add(shadowedTask);
				}
			}
		}
		return shadowedTasksList;
	}

	/**
	 * Tests if a task is ordered before an other task
	 *
	 * @param firstTask The task tested to be before second task
	 * @param secondTask The task tested to be after first task
	 * @return <code> true </code> if first is ordered before secondTask task
	 *         <code> false </code> otherwise
	 */
	public boolean isOrderedBefore(int firstTask, int secondTask) {

		if (secondTask != firstTask)
			return isOrderedAfter(firstTask, secondTask) ? false : true;
		else
			return false;
	}

	/**
	 * Tests if a task is ordered between two other tasks
	 * 
	 * @param task The task tested to be between the other tasks
	 * @param firstTask The tested to be the first task
	 * @param secondTask The task tested to be the second task
	 * @return <code> true </code> if task is ordered between firstTask and
	 *         secondTask <code> false </code> otherwise
	 */
	public boolean isOrderedBetween(int task, int firstTask, int secondTask) {
		if (this.isOrderedAfter(task, firstTask) && this.isOrderedAfter(secondTask, task)) {
			return true;
		} else
			return false;
	}

	/**
	 * Evaluates a FIRST/LAST task list and removes tasks that are not first/last
	 * 
	 * @param taskList The treated task list
	 * @return <code> true </code> if the task list is updated and
	 *         <code> false </code> otherwise
	 */
	public boolean evaluateTaskList(TaskList taskList) {
		boolean updated = false;
		TaskList tempTaskList = new TaskList(taskList);
		if (tempTaskList.getLabel()) {
			for (int task : tempTaskList.getTaskList()) {
				if (!this.getPredecessorsIn(task, tempTaskList.getTaskList()).isEmpty()) {
					taskList.getTaskList().remove((Integer) task);
					updated = true;
				}
			}
		} else {
			for (int task : tempTaskList.getTaskList()) {
				if (!this.getSuccessorsIn(task, tempTaskList.getTaskList()).isEmpty()) {
					taskList.getTaskList().remove((Integer) task);
					updated = true;
				}
			}
		}
		return updated;
	}

	/**
	 * Represents an adjacency matrix in a string form
	 * 
	 * @return A String representation of the adjacency matrix
	 */
	@Override
	public String toString() {
		StringBuffer str = new StringBuffer();
		str.append("Adjacency Matrix: \n");
		for (int i = 0; i < this.matrix.length; i++) {
			str.append("\n");
			for (int j = 0; j < this.matrix.length; j++)
				str.append(this.matrix[i][j] + "|");
		}
		return str.toString();
	}

}
