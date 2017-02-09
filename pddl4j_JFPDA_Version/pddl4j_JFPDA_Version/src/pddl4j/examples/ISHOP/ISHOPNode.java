package pddl4j.examples.ISHOP;

import java.util.ArrayList;
import java.util.List;
import pddl4j.preprocessing.CodedProblem;
import pddl4j.util.BitExp;
import pddl4j.util.BitOp;
import pddl4j.util.BitState;
import pddl4j.util.BitVector;

/**
 * This class implements a node of the tree search.
 * 
 * @author A. Ramoul
 * @version 1.0 - 10.04.2015
 */
public class ISHOPNode {
	
	/**
	 * The state of the shop node
	 */
	private BitVector _state = new BitVector();
	
	/**
	 * The expansion of the node.
	 */
	private final List<Integer> _expansion;
	
	/**
	 * The position of the last task affecting the node state
	 */
	private int _statePosition;
	
	private final boolean COMMENT = false;
	
	/**
	 * Creates a new task network node
	 */
	public ISHOPNode() {
		_expansion = new ArrayList<>();
		_state = new BitVector();
		_statePosition = 0;
	}
	
	/**
	 * Creates a new task network node from an ordered method and a BitVector
	 * 
	 * @param tn The task network of the node
	 * @param state The state of the node
	 */
	public ISHOPNode(final List<Integer> expansion, final BitVector state) {
		_expansion = expansion;
		_state = new BitVector(state);
		_statePosition = 0;
	}
	
	/**
	 * Creates a new task network node from an ordered method and a BitState
	 * 
	 * @param tn The task network of the node
	 * @param state The state of the node
	 */
	public ISHOPNode(final List<Integer> expansion, final BitState state) {
		_expansion = expansion;
		_state = new BitVector(state);
		_statePosition = 0;
	}
	
	/**
	 * Creates a new task network node from an ordered method and a BitExp
	 * 
	 * @param expansion The list of tasks contained in the node of the node
	 * @param state The state of the node
	 */
	public ISHOPNode(final List<Integer> expansion, final BitExp state) {
		_expansion = expansion;
		_state = new BitVector(state.getPositive());
		_statePosition = 0;
	}
	
	/**
	 * Creates a new task network node from a shopNode
	 * 
	 * @param tn The shop node
	 */
	public ISHOPNode(final ISHOPNode tn) {
		_expansion = new ArrayList<>(tn.getExpansion());
		_state = new BitVector(tn._state);
		_statePosition = tn._statePosition;
	}
	
	/**
	 * Adds a list of tasks and modify the state of a SHOP node
	 * 
	 * @param taskList The task list to add
	 * @param position The position where to add taskList
	 * @param tasksNumber The number of tasks in the problem
	 * @param problem The CodedProblem
	 * @return <code> true </code> if added and <code> false </code> otherwise
	 */
	public boolean addTasks(final List<Integer> taskList, final int position, final int tasksNumber, final CodedProblem problem) {
		//The affecting task is false if the task of the current state position is not primitive
		boolean affectingTask = true;
		_expansion.addAll(position, taskList);
		final int size = _expansion.size();
		while (_statePosition < size && affectingTask) {
			final int absoluteTask = problem.getAbsoluteIndex(_expansion.get(_statePosition));
			if (problem.isPrimitive(absoluteTask)) {
				final BitOp action = problem.getRelevantOperators(absoluteTask).get(0);
				if (!evaluateAction(action)) {
					//					final BitVector satisfied = new BitVector(_state);
					//					final BitVector unsatisfied = new BitVector(action.getPreconditions().getPositive());
					//					satisfied.and(unsatisfied);
					//					unsatisfied.xor(satisfied);
					//					for (final int proposition : unsatisfied.getAllSetBits()) {
					//						ExplanationManager.addRejectedAction(proposition, absoluteTask);
					//					}
					return false;
				}
				_statePosition++;
			} else {
				affectingTask = false;
			}
		}
		return true;
	}
	
	/**
	 * removes a task in a task network node
	 * 
	 * @param task The task to remove
	 * @return <code> true </code> if removed and <code> false </code> otherwise
	 */
	public boolean removeTask(final int task) {
		boolean removed = false;
		removed = _expansion.remove((Integer) task);
		return removed;
	}
	
	//	/**
	//	 * Tests if the preconditions of the relevant method are verified
	//	 *
	//	 * @param relevantMethod The method tested
	//	 * @param problem The coded problem
	//	 * @return <code> true </code> if the preconditions are verified and <code> false </code> otherwise
	//	 */
	//	public boolean testRelevantMethod(final Method relevantMethod, final List<Integer> rejectPropositions, final CodedProblem problem) {
	//		final List<OneTaskConstraint> preconditions = relevantMethod.getExpansion().getBeforeConstraints();
	//		if (evaluateMethodPreconditions(preconditions, rejectPropositions, problem)) {
	//			return true;
	//		} else {
	//			return false;
	//		}
	//	}
	//
	//	/**
	//	 * Tests if method preconditions are verified in the node state
	//	 *
	//	 * @param preconditions The precondition to verify
	//	 * @param problem The CodedProblem
	//	 * @return <code> true </code> if the preconditions are verified and <code> false </code> otherwise
	//	 */
	//	public boolean evaluateMethodPreconditions(final List<OneTaskConstraint> preconditions, final List<Integer> rejectPropositions, final CodedProblem problem) {
	//
	//		for (final OneTaskConstraint precondition : preconditions) {
	//			final int literal = precondition.getGroundPredicate();
	//			if (!problem.isPositiveLiteral(literal)) {
	//				if (_state.get(literal - 1)) {
	//					//					ExplanationManager.addRejectedMethod(literal, methodIndex);
	//					rejectPropositions.add(literal);
	//					return false;
	//				}
	//			} else {
	//				if (!_state.get(literal)) {
	//					//					ExplanationManager.addRejectedMethod(literal, methodIndex);
	//					rejectPropositions.add(literal);
	//					return false;
	//				}
	//			}
	//		}
	//		return true;
	//	}
	
	/**
	 * Tests if action preconditions are verified and add its effects to the node state
	 * 
	 * @param action The evaluated operator
	 * @return <code> true </code> if the preconditions are verified and <code> false </code> otherwise
	 */
	public boolean evaluateAction(final BitOp action) {
		
		if (!_state.include(action.getPreconditions().getPositive())) {
			return false;
		} else {
			_state.or(action.getUnconditionalEffects().getPositive());
			_state.andNot(action.getUnconditionalEffects().getNegative());
			return true;
		}
	}
	
	/**
	 * @return the _state
	 */
	public BitVector get_state() {
		return _state;
	}
	
	/**
	 * @param _state the _state to set
	 */
	public void set_state(final BitVector _state) {
		this._state = _state;
	}
	
	/**
	 * @return the _statePosition
	 */
	public int get_statePosition() {
		return _statePosition;
	}
	
	/**
	 * @param _statePosition the _statePosition to set
	 */
	public void set_statePosition(final int _statePosition) {
		this._statePosition = _statePosition;
	}
	
	public List<Integer> getExpansion() {
		return _expansion;
	}
	
	/**
	 * Calculates the relative indexes of specified tasks in a task list
	 * 
	 * @param tasks The specified tasks list
	 * @param tasksNumber The number of tasks in the domain
	 * @return the relative indexes of the tasks.
	 */
	public List<Integer> getRelativeIndexes(final List<Integer> tasks, final int tasksNumber) {
		final List<Integer> relativeTasks = new ArrayList<>(tasks.size());
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
		final int occurrence = getMaxOccurrence(task, _expansion, tasksNumber);
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
	 * Gives a string representation of a SHOP node
	 * 
	 * @param problem The coded problem
	 * @return a string representation of a shop node
	 */
	public String toString(final CodedProblem problem) {
		final StringBuffer str = new StringBuffer();
		str.append("\t----------- iSHOP Node ----------\n");
		str.append("\tNode state:\n");
		for (final int predicate : _state.getAllSetBits()) {
			str.append("\t\t" + problem.toString(problem.getRelevantFacts().get(predicate)) + "\n");
		}
		str.append("\tExpansion:\n");
		for (final int task : _expansion) {
			final int absoluteTask = problem.getAbsoluteIndex(task);
			final int occurrence = problem.getOccurrence(task);
			str.append("\t\t" + problem.toString(problem.getRelevantTasks().get(absoluteTask)) + occurrence + "\n");
		}
		return str.toString();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (COMMENT ? 1231 : 1237);
		result = prime * result + ((_expansion == null) ? 0 : _expansion.hashCode());
		result = prime * result + ((_state == null) ? 0 : _state.hashCode());
		result = prime * result + _statePosition;
		return result;
	}
	
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final ISHOPNode other = (ISHOPNode) obj;
		if (COMMENT != other.COMMENT) {
			return false;
		}
		if (_expansion == null) {
			if (other._expansion != null) {
				return false;
			}
		} else if (!_expansion.equals(other._expansion)) {
			return false;
		}
		if (_state == null) {
			if (other._state != null) {
				return false;
			}
		} else if (!_state.equals(other._state)) {
			return false;
		}
		if (_statePosition != other._statePosition) {
			return false;
		}
		return true;
	}
	
}
