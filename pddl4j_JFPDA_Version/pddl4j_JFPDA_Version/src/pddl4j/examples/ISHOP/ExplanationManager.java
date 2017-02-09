package pddl4j.examples.ISHOP;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import pddl4j.preprocessing.CodedProblem;

/**
 * TODO
 * 
 * @since
 */

/**
 * <p>
 * This class implements the explanation manager which contains methods and structures used to manage processing explanation.
 * </p>
 * 
 * @author A. Ramoul
 * @version 1.0 - 22.03.2016
 */

public final class ExplanationManager {
	
	/**
	 * The list of unsatisfied propositions for each rejected action
	 */
	static Hashtable<Integer, List<Integer>> _actionsRejectList;
	
	/**
	 * The list of unsatisfied propositions for each rejected decomposition method
	 */
	//static Hashtable<Integer, List<Integer>> _methodsRejectList;
	
	/**
	 * The list of unsatisfied propositions for each composed task that can't be decomposed with all relevant methods
	 */
	static Hashtable<Integer, List<Integer>> _tasksRejectList;
	
	/**
	 * Private constructor of the final class "ExplanationManager"
	 */
	private ExplanationManager() {
	
	}
	
	/**
	 * Runs the Explanation Manger
	 */
	static void run() {
		_actionsRejectList = new Hashtable<>();
		//_methodsRejectList = new Hashtable<>();
		_tasksRejectList = new Hashtable<>();
	}
	
	/**
	 * Adds a rejected action to the actions rejection list
	 * 
	 * @param proposition The not satisfied precondition of the action corresponding to task
	 * @param task the task corresponding to the unsatisfied task
	 */
	static void addRejectedAction(final int proposition, final int task) {
		if (_actionsRejectList.containsKey(proposition)) {
			_actionsRejectList.get(proposition).add(task);
		} else {
			final List<Integer> newList = new ArrayList<>();
			newList.add(task);
			_actionsRejectList.put(proposition, newList);
		}
	}
	
	//	/**
	//	 * Adds a rejected method to the methods rejection list
	//	 * 
	//	 * @param proposition The not satisfied precondition of the method
	//	 * @param method The unsatisfied method
	//	 */
	//	static void addRejectedMethod(final int proposition, final int method) {
	//		if (_methodsRejectList.containsKey(proposition)) {
	//			_methodsRejectList.get(proposition).add(method);
	//		} else {
	//			final List<Integer> newList = new ArrayList<>();
	//			newList.add(method);
	//			_methodsRejectList.put(proposition, newList);
	//		}
	//	}
	
	/**
	 * Adds a rejected task to the tasks rejection list
	 * 
	 * @param propositions The not satisfied preconditions of the task
	 * @param task The task that can't be decomposed with all relevant methods
	 */
	static void addRejectedTask(final List<Integer> propositions, final int task) {
		for (final int proposition : propositions) {
			if (_tasksRejectList.containsKey(proposition)) {
				if (!_tasksRejectList.get(proposition).contains(task)) {
					_tasksRejectList.get(proposition).add(task);
				}
			} else {
				final List<Integer> newList = new ArrayList<>();
				newList.add(task);
				_tasksRejectList.put(proposition, newList);
			}
		}
	}
	
	/**
	 * Prints the search reject list
	 * 
	 * @param problem The Coded Problem
	 */
	static void printActionsRejectList(final CodedProblem problem) {
		System.out.println("----------------------------- Actions Reject List ----------------------------");
		for (final int key : _actionsRejectList.keySet()) {
			System.out.println("* [" + problem.toString(problem.getRelevantFacts().get(key)) + "] has rejected " + _actionsRejectList.get(key).size() + " actions: ");
			int index = 0;
			for (final int task : _actionsRejectList.get(key)) {
				if (index != 0 && (index % 3 == 0)) {
					System.out.println();
				}
				index++;
				System.out.print(problem.toString(problem.getRelevantTasks().get(task)));
			}
			System.out.println("\n");
		}
		System.out.println("------------------------------------------------------------------------------");
	}
	
	//	/**
	//	 * Prints the search reject list
	//	 * 
	//	 * @param problem The Coded Problem
	//	 */
	//	static void printMethodsRejectList(final CodedProblem problem) {
	//		System.out.println("----------------------------- Methods Reject List ----------------------------");
	//		for (final int key : _methodsRejectList.keySet()) {
	//			System.out.println("* [" + problem.toString(problem.getRelevantFacts().get(key)) + "] has rejected " + _methodsRejectList.get(key).size() + " methods: ");
	//			int index = 0;
	//			for (final int methodIndex : _methodsRejectList.get(key)) {
	//				if (index != 0 && (index % 3 == 0)) {
	//					System.out.println();
	//				}
	//				index++;
	//				System.out.print(problem.printMethod(problem.getMethods().get(methodIndex)));
	//			}
	//			System.out.println("\n");
	//		}
	//		System.out.println("------------------------------------------------------------------------------");
	//	}
	
	/**
	 * Prints the search reject list
	 * 
	 * @param problem The Coded Problem
	 */
	static void printTasksRejectList(final CodedProblem problem) {
		final int tasksNumber = problem.getRelevantTasks().size();
		int absoluteTask, occurrence;
		System.out.println("----------------------------- Tasks Reject List ----------------------------");
		for (final int key : _tasksRejectList.keySet()) {
			System.out.println("* [" + problem.toString(problem.getRelevantFacts().get(key)) + "] has rejected " + _tasksRejectList.get(key).size() + " tasks: ");
			int index = 0;
			for (final int task : _tasksRejectList.get(key)) {
				if (task >= tasksNumber) {
					absoluteTask = task % tasksNumber;
					occurrence = tasksNumber / task;
				} else {
					absoluteTask = task;
					occurrence = -1;
				}
				if (index != 0 && (index % 3 == 0)) {
					System.out.println();
				}
				index++;
				System.out.print(problem.toTaskString(problem.getRelevantTasks().get(absoluteTask)) + "(" + occurrence + ")  ");
			}
			System.out.println("\n");
		}
		System.out.println("------------------------------------------------------------------------------");
	}
	
}
