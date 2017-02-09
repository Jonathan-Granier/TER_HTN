package pddl4j.examples.ISHOP;

import java.util.List;
import java.util.Scanner;
import pddl4j.preprocessing.CodedProblem;
import pddl4j.util.OrderedMethod;

/**
 * TODO
 * 
 * @since
 */

/**
 * <p>
 * This class implements the Interaction manager which contains methods and structures used to manage interaction while processing.
 * </p>
 * 
 * @author A. Ramoul
 * @version 1.0 - 22.03.2016
 */

public final class InteractionManager {
	
	/**
	 * The level of interaction defined by the user TODO Define all interaction levels
	 */
	static int _interactionLevel;
	private static Scanner sc;
	
	/**
	 * Private constructor of the final class "InteractionManager"
	 */
	private InteractionManager() {
	
	}
	
	/**
	 * Runs the Interaction Manger
	 */
	static void run() {
		_interactionLevel = 0;
	}
	
	/**
	 * Runs the Interaction Manger
	 * 
	 * @param interactionLevel The level of interaction defined by the user
	 */
	static void run(final int interactionLevel) {
		_interactionLevel = interactionLevel;
	}
	
	/**
	 * Lets the user chose which method to decompose TODO Add multi methods choice
	 * 
	 * @param methodsList The list of relevant methods for a task
	 * @param task the task to decompose
	 * @param problem The Coded Problem
	 * @return The list of methods to decompose
	 */
	static List<OrderedMethod> askForMethods(final List<OrderedMethod> methodsList, final int task, final CodedProblem problem) {
		if (_interactionLevel == 5) {
			final int size = methodsList.size();
			System.out.println("* " + size + " are available to decompose the task: " + problem.toString(problem.getRelevantTasks().get(task)));
			int i = 0;
			for (final OrderedMethod method : methodsList) {
				System.out.println(i + "/ " + problem.printMethod(method));
				i++;
			}
			sc = new Scanner(System.in);
			int response = sc.nextInt();
			final OrderedMethod newMethod = new OrderedMethod(methodsList.get(response));
			while (!methodsList.contains(newMethod)) {
				System.out.println("The input response don't corresponde to a method index, please retry with: \n" + methodsList);
				response = sc.nextInt();
			}
			methodsList.clear();
			methodsList.add(newMethod);
		}
		return methodsList;
	}
	
}
