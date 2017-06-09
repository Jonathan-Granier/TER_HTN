/*
 * Copyright (c) 2015 by:
 * 	Damien Pellier <Damien.Pellier@imag.fr>.
 * 	Abdeldjalil Ramoul <abdeldjalil.ramoul@imag.fr>.
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

package pddl4j.preprocessing;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import pddl4j.parser.RequireKey;
import pddl4j.parser.Symbol;
import pddl4j.parser.TypedSymbol;
import pddl4j.util.BitExp;
import pddl4j.util.BitOp;
import pddl4j.util.CondBitExp;
import pddl4j.util.IntExp;
import pddl4j.util.OneTaskConstraint;
import pddl4j.util.OrderConstraint;
import pddl4j.util.OrderedMethod;
import pddl4j.util.TaskList;
import pddl4j.util.TwoTasksConstraint;

/**
 * This class implements the methods needed to convert the compact encoded objects used in the preprocessing into string representation.
 * 
 * @author D. Pellier
 * @version 1.0 - 11.06.2010
 */
public final class StringEncoder {
	
	/**
	 * The default constructor with a private access to prevent instance creation.
	 */
	public StringEncoder() {
	}
	
	static String afterConstraintToString(final int[] constraint) {
		final StringBuffer str = new StringBuffer();
		str.append("(");
		str.append(Preprocessing.toString(Preprocessing.tableOfRevelantFacts.get(constraint[0])));
		str.append(" < ");
		str.append(Preprocessing.toString(Preprocessing.tableOfRelevantTasks.get(constraint[1])));
		str.append(")");
		return str.toString();
	}
	
	//	static String afterConstraintToString(int index) {
	//		return StringEncoder.afterConstraintToString(Preprocessing.getConstraint(index));
	//	}
	
	static String beforeConstraintToString(final int[] constraint) {
		final StringBuffer str = new StringBuffer();
		str.append("(");
		str.append(Preprocessing.toString(Preprocessing.tableOfRevelantFacts.get(constraint[0])));
		str.append(" > ");
		str.append(Preprocessing.toString(Preprocessing.tableOfRelevantTasks.get(constraint[1])));
		str.append(")");
		return str.toString();
	}
	
	static String orderingConstraintToString(final int[] constraint) {
		final StringBuffer str = new StringBuffer();
		str.append("(");
		str.append(Preprocessing.toString(Preprocessing.tableOfRelevantTasks.get(constraint[0])));
		str.append(" < ");
		str.append(Preprocessing.toString(Preprocessing.tableOfRelevantTasks.get(constraint[1])));
		str.append(")");
		return str.toString();
	}
	
	/**
	 * Returns a short string representation of the specified operator, i.e., its name and its instantiated parameters.
	 * 
	 * @param op the operator.
	 * @param constants the table of constants.
	 * @param task the table of tasks symbols.
	 * @return a string representation of the specified operator.
	 */
	static String toShortString(final IntOp op, final List<String> constants, final List<String> tasks) {
		final StringBuffer str = new StringBuffer();
		str.append(op.getName());
		for (int i = 0; i < op.getArity(); i++) {
			final int index = op.getValueOfParameter(i);
			if (index == -1) {
				str.append(" ?");
			} else {
				str.append(" " + constants.get(index));
			}
		}
		return str.toString();
	}
	
	/**
	 * Returns a short string representation of the specified method, i.e., its name and its instantiated parameters.
	 * 
	 * @param meth the method.
	 * @param constants the table of constants.
	 * @param task the table of tasks symbols.
	 * @return a string representation of the specified method.
	 */
	static String toShortString(final IntMeth meth, final List<String> constants, final List<String> tasks) {
		final StringBuffer str = new StringBuffer();
		str.append(meth.getName());
		for (int i = 0; i < meth.getArity(); i++) {
			final int index = meth.getValueOfParameter(i);
			if (index == -1) {
				str.append(" ?");
			} else {
				str.append(" " + constants.get(index));
			}
		}
		return str.toString();
	}
	
	/**
	 * Returns a string representation of the specified operator.
	 * 
	 * @param op the operator to print.
	 * @param constants the table of constants.
	 * @param types the table of types.
	 * @param predicates the table of predicates.
	 * @param functions the table of functions.
	 * @param task the table of tasks symbols.
	 * @return a string representation of the specified operator.
	 */
	static String toString(final IntOp op, final List<String> constants, final List<String> types, final List<String> predicates, final List<String> functions, final List<String> tasks) {
		final StringBuffer str = new StringBuffer();
		str.append("Op " + op.getName() + "\n");
		str.append("Instantiations:\n");
		for (int i = 0; i < op.getArity(); i++) {
			final int index = op.getValueOfParameter(i);
			final String type = types.get(op.getTypeOfParameters(i));
			if (index == -1) {
				str.append(Symbol.DEFAULT_VARIABLE_SYMBOL + i + " - " + type + " : ? \n");
			} else {
				str.append(Symbol.DEFAULT_VARIABLE_SYMBOL + i + " - " + type + " : " + constants.get(index) + " " + "\n");
			}
		}
		str.append("Preconditions:\n");
		str.append(StringEncoder.toString(op.getPreconditions(), constants, types, predicates, functions) + "\n");
		str.append("Effects:\n");
		str.append(StringEncoder.toString(op.getEffects(), constants, types, predicates, functions) + "\n");
		return str.toString();
	}
	
	/**
	 * Returns a string representation of the specified method.
	 * 
	 * @param meth the method to print.
	 * @param constants the table of constants.
	 * @param types the table of types.
	 * @param predicates the table of predicates.
	 * @param functions the table of functions.
	 * @param task the table of tasks symbols.
	 * @return a string representation of the specified operator.
	 */
	static String toString(final IntMeth meth, final List<String> constants, final List<String> types, final List<String> predicates, final List<String> functions, final List<String> tasks) {
		final StringBuffer str = new StringBuffer();
		
		str.append("Method " + meth.getName() + "\n");
		str.append("Instantiations:\n");
		str.append("Declared parameters:\n");
		for (int i = 0; i < meth.getArity(); i++) {
			final int index = meth.getValueOfParameter(i);
			final String type = types.get(meth.getTypeOfParameters(i));
			if (index == -1) {
				str.append(Symbol.DEFAULT_VARIABLE_SYMBOL + i + " - " + type + " : ? \n");
			} else {
				str.append(Symbol.DEFAULT_VARIABLE_SYMBOL + i + " - " + type + " : " + constants.get(index) + " " + "\n");
			}
		}
		str.append("Undeclared parameters:\n");
		for (int i = 0; i < meth.getUndeclaredParameters().length; i++) {
			final int index = meth.getValueOfUndeclaredParameter(i);
			final String type = types.get(meth.getTypeOfUndeclaredParameters(i));
			if (index == -1) {
				str.append(Symbol.DEFAULT_VARIABLE_SYMBOL + (meth.getArity() + i) + " - " + type + " : ? \n");
			} else {
				str.append(Symbol.DEFAULT_VARIABLE_SYMBOL + (meth.getArity() + i) + " - " + type + " : " + constants.get(index) + " " + "\n");
			}
		}
		str.append("Expansion:\n");
		str.append(StringEncoder.toString(meth.getExpansion(), constants, types, predicates, functions, tasks) + "\n");
		str.append("Constraints:\n");
		str.append(StringEncoder.toString(meth.getConstraints(), constants, types, predicates, functions, tasks) + "\n");
		return str.toString();
	}
	
	/**
	 * Returns a string representation of an expression.
	 * 
	 * @param exp the expression.
	 * @param constants the table of constants.
	 * @param types the table of types.
	 * @param predicates the table of predicates.
	 * @param functions the table of functions.
	 * @return a string representation of the specified expression.
	 */
	static String toString(final IntExp exp, final List<String> constants, final List<String> types, final List<String> predicates, final List<String> functions) {
		return StringEncoder.toString(exp, constants, types, predicates, functions, new ArrayList<String>(), " ");
	}
	
	/**
	 * Returns a string representation of an expression.
	 * 
	 * @param exp the expression.
	 * @param constants the table of constants.
	 * @param types the table of types.
	 * @param predicates the table of predicates.
	 * @param functions the table of functions.
	 * @param tasks the table of tasks symbols.
	 * @return a string representation of the specified expression.
	 */
	static String toString(final IntExp exp, final List<String> constants, final List<String> types, final List<String> predicates, final List<String> functions, final List<String> tasks) {
		return StringEncoder.toString(exp, constants, types, predicates, functions, tasks, " ");
	}
	
	/**
	 * Returns a string representation of an expression.
	 * 
	 * @param exp the expression.
	 * @param constants the table of constants.
	 * @param types the table of types.
	 * @param predicates the table of predicates.
	 * @param functions the table of functions.
	 * @param tasks the table of tasks symbols.
	 * @param separator the string separator between predicate symbol and arguments.
	 * @return a string representation of the specified expression.
	 */
	static String toString(final IntExp exp, final List<String> constants, final List<String> types, final List<String> predicates, final List<String> functions, final List<String> tasks, final String separator) {
		return StringEncoder.toString(exp, constants, types, predicates, functions, tasks, "", separator);
	}
	
	/**
	 * Returns a string representation of an expression.
	 * 
	 * @param exp the expression.
	 * @param constants the table of constants.
	 * @param types the table of types.
	 * @param predicates the table of predicates.
	 * @param functions the table of functions.
	 * @param tasks the table of tasks symbols.
	 * @param offset the offset white space from the left used for indentation.
	 * @param separator the string separator between predicate symbol and arguments.
	 * @return a string representation of the specified expression node.
	 */
	private static String toString(final IntExp exp, final List<String> constants, final List<String> types, final List<String> predicates, final List<String> functions, final List<String> tasks, String offset, final String separator) {
		final StringBuffer str = new StringBuffer();
		switch (exp.getConnective()) {
			case ATOM:
				str.append("(");
				str.append(predicates.get(exp.getPredicate()));
				int[] args = exp.getArguments();
				for (int i = 0; i < args.length; i++) {
					final int index = args[i];
					if (index < 0) {
						str.append(" " + Symbol.DEFAULT_VARIABLE_SYMBOL + (-index - 1));
					} else {
						str.append(" " + constants.get(index));
					}
				}
				str.append(")");
				break;
			case TASK:
				str.append("(");
				str.append(tasks.get(exp.getPredicate()));
				args = exp.getArguments();
				for (int i = 0; i < args.length; i++) {
					final int index = args[i];
					if (index < 0) {
						str.append(" " + Symbol.DEFAULT_VARIABLE_SYMBOL + (-index - 1));
					} else {
						str.append(" " + constants.get(index));
					}
				}
				str.append(")");
				break;
			case FN_HEAD:
				str.append("(");
				str.append(functions.get(exp.getPredicate()));
				args = exp.getArguments();
				for (int i = 0; i < args.length; i++) {
					final int index = args[i];
					if (index < 0) {
						str.append(" " + Symbol.DEFAULT_VARIABLE_SYMBOL + (-index - 1));
					} else {
						str.append(" " + constants.get(index));
					}
				}
				str.append(")");
				break;
			case EQUAL_ATOM:
				str.append("(");
				str.append("=");
				args = exp.getArguments();
				for (int i = 0; i < args.length; i++) {
					final int index = args[i];
					if (index < 0) {
						str.append(" " + Symbol.DEFAULT_VARIABLE_SYMBOL + (-index - 1));
					} else {
						str.append(" " + constants.get(index));
					}
				}
				str.append(")");
				break;
			case AND:
			case OR:
				offset += "  ";
				str.append("(");
				str.append(exp.getConnective().getImage());
				str.append(" ");
				if (!exp.getChildren().isEmpty()) {
					for (int i = 0; i < exp.getChildren().size() - 1; i++) {
						str.append(StringEncoder.toString(exp.getChildren().get(i), constants, types, predicates, functions, tasks, offset) + "\n" + offset);
					}
					str.append(StringEncoder.toString(exp.getChildren().get(exp.getChildren().size() - 1), constants, types, predicates, functions, tasks, offset));
				}
				str.append(")");
				offset = offset.substring(0, offset.length() - 2);
				break;
			case FORALL:
			case EXISTS:
				offset += offset + "  ";
				str.append(" (");
				str.append(exp.getConnective().getImage());
				str.append(" (");
				str.append(Symbol.DEFAULT_VARIABLE_SYMBOL + (-exp.getVariable() - 1));
				str.append(" - ");
				str.append(types.get(exp.getType()));
				str.append(")\n" + offset);
				if (exp.getChildren().size() == 1) {
					str.append(StringEncoder.toString(exp.getChildren().get(0), constants, types, predicates, functions, tasks, offset));
				}
				str.append(")");
				offset = offset.substring(0, offset.length() - 2);
				break;
			case NUMBER:
				str.append(exp.getValue());
				break;
			case F_EXP:
				str.append(StringEncoder.toString(exp.getChildren().get(0), constants, types, predicates, functions, tasks, offset));
				break;
			case F_EXP_T:
				str.append(exp.getConnective());
				/*
				 * if (this.children.isEmpty()) { str.append(this.getVariable()); } else
				 * { str.append("("); str.append(this.getConnective()); str.append(" ");
				 * str.append(this.getVariable()); str.append(" ");
				 * str.append(this.toString(cn.getChildren().get(0))); }
				 */
				break;
			case TIME_VAR:
				// str.append(cn.getVariable());
				break;
			case TRUE:
			case FALSE:
				str.append(exp.getConnective());
				break;
			case FN_ATOM:
			case WHEN:
			case DURATION_ATOM:
			case LESS:
			case LESS_OR_EQUAL:
			case EQUAL:
			case GREATER:
			case GREATER_OR_EQUAL:
			case ASSIGN:
			case INCREASE:
			case DECREASE:
			case SCALE_UP:
			case SCALE_DOWN:
			case MUL:
			case DIV:
			case MINUS:
			case PLUS:
			case SOMETIME_AFTER:
			case SOMETIME_BEFORE:
				str.append("(");
				str.append(exp.getConnective().getImage());
				str.append(" ");
				str.append(StringEncoder.toString(exp.getChildren().get(0), constants, types, predicates, functions, tasks, offset));
				str.append(" ");
				str.append(StringEncoder.toString(exp.getChildren().get(1), constants, types, predicates, functions, tasks, offset));
				str.append(")");
				break;
			case AT_START:
			case AT_END:
			case OVER_ALL:
			case MINIMIZE:
			case MAXIMIZE:
			case UMINUS:
			case NOT:
			case ALWAYS:
				str.append("(");
				str.append(exp.getConnective().getImage());
				str.append(" ");
				str.append(StringEncoder.toString(exp.getChildren().get(0), constants, types, predicates, functions, tasks, offset));
				str.append(")");
				break;
			case IS_VIOLATED:
				str.append("(");
				str.append(exp.getConnective().getImage());
				str.append(")");
				break;
			case BEFORE:
			case AFTER:
			case BETWEEN:
			case SERIES:
			case FIRST:
			case LAST:
				str.append("(");
				str.append(exp.getConnective().getImage());
				for (int i = 0; i < exp.getChildren().size(); i++) {
					str.append(" ");
					str.append(StringEncoder.toString(exp.getChildren().get(i), constants, types, predicates, functions, tasks, offset));
				}
				str.append(")");
				break;
			case EXPANSION:
				str.append("(");
				for (int i = 0; i < exp.getChildren().size(); i++) {
					str.append(StringEncoder.toString(exp.getChildren().get(i), constants, types, predicates, functions, tasks, offset));
					if (i < exp.getChildren().size() - 1) {
						str.append(" ");
					}
				}
				str.append(")");
				break;
			default:
				break;
		}
		return str.toString();
	}
	
	/**
	 * Returns a short string representation of the specified operator, i.e., its name and its instantiated parameters.
	 * 
	 * @param op the operator.
	 * @param constants the table of constants.
	 * @return a string representation of the specified operator.
	 */
	static String toShortString(final BitOp op, final List<String> constants) {
		final StringBuffer str = new StringBuffer();
		str.append(op.getName());
		for (int i = 0; i < op.getArity(); i++) {
			final int index = op.getValueOfParameter(i);
			if (index == -1) {
				str.append(" ?");
			} else {
				str.append(" " + constants.get(index));
			}
		}
		return str.toString();
	}
	
	/**
	 * Returns a string representation of a specified operator.
	 * 
	 * @param op the operator.
	 * @param constants the table of constants.
	 * @param types the table of types.
	 * @param predicates the table of predicates.
	 * @param functions the table of functions.
	 * @param facts the table of relevant facts.
	 * @return a string representation of the specified operator.
	 */
	static String toString(final BitOp op, final List<String> constants, final List<String> types, final List<String> predicates, final List<String> functions, final List<IntExp> facts, final String blank) {
		final StringBuffer str = new StringBuffer();
		str.append(blank + "(:action " + op.getName() + "\n");
		str.append(blank + "\t:parameters (");
		for (int i = 0; i < op.getArity(); i++) {
			final int index = op.getValueOfParameter(i);
			str.append(constants.get(index));
			if (i < op.getArity() - 1) {
				str.append(" ");
			}
		}
		str.append(")\n");
		str.append(blank + "\t:precondition ");
		str.append(StringEncoder.toString(op.getPreconditions(), constants, types, predicates, functions, facts));
		str.append("\n");
		str.append(blank + "\t:effect ");
		for (final CondBitExp condExp : op.getCondEffects()) {
			str.append(StringEncoder.toString(condExp, constants, types, predicates, functions, facts));
			str.append("\n");
		}
		if (op.getCondEffects().size() == 0) {
			str.append("(and)\n");
		}
		
		str.append(blank + ")\n");
		return str.toString();
	}
	
	/**
	 * Returns a string representation of a specified method.
	 * 
	 * @param meth the method.
	 * @param constants the table of constants.
	 * @param blank the indentation space.
	 * @return a string representation of the specified method.
	 */
	static String toString(final OrderedMethod meth, final List<String> constants, final List<String> types, final List<String> predicates, final List<String> functions, final List<IntExp> fact, final List<IntExp> task, final String blank) {
		final StringBuffer str = new StringBuffer();
		str.append(blank + "(:OrderedMethod " + meth.getName() + "\n");
		str.append(blank + "\t:parameters (");
		for (int i = 0; i < meth.getArity(); i++) {
			final int index = meth.getValueOfParameter(i);
			str.append(constants.get(index));
			if (i < meth.getArity() - 1) {
				str.append(" ");
			}
		}
		str.append(")\n");
		str.append(blank + "\t:expansion(\n");
		final List<Integer> tasks = meth.getExpansion();
		for (final int i : tasks) {
			str.append(blank + "\t\t(tag " + "t" + i + " " + toString(Preprocessing.tableOfRelevantTasks.get(i)) + ")\n");
		}
		str.append(blank + "\t\t)\n");
		str.append(blank + "\t:preconditions\n");
		str.append(blank + "\t\t(and\n");
		for (final int factIndex : meth.getPositivePreconditions().getAllSetBits()) {
			str.append(blank + "\t\t\t" + toString(fact.get(factIndex)) + "\n");
		}
		for (final int factIndex : meth.getNegativePreconditions().getAllSetBits()) {
			str.append(blank + "\t\t\t" + toString(fact.get(factIndex + 1)) + "\n");
		}
		str.append(blank + "\t\t)\n");
		str.append(blank + ")\n");
		return str.toString();
	}
	
	/**
	 * Returns a string representation of the domain instantiated methods.
	 * 
	 * @param methList the methods list.
	 * @param constants the table of constants.
	 * @param types the table of types.
	 * @param predicates the table of predicates.
	 * @param functions the table of functions.
	 * @param facts the table of relevant facts.
	 * @param tasks the table of relevant tasks.
	 * @return a string representation of the domain instantiated methods.
	 */
	static String toString(final List<OrderedMethod> methList, final List<String> constants, final List<String> types, final List<String> predicates, final List<String> functions, final List<IntExp> fact, final List<IntExp> task, final String blank) {
		final StringBuffer str = new StringBuffer();
		str.append("\n;;;;;;;;;;;;;;;;;;;;;;;;;;;\n");
		str.append(";;;;;;;;;; Methods ;;;;;;;;\n");
		str.append(";;;;;;;;;;;;;;;;;;;;;;;;;;;\n\n");
		for (final OrderedMethod meth : methList) {
			str.append(toString(meth, constants, types, predicates, functions, fact, task, blank));
		}
		str.append(")");
		return str.toString();
	}
	
	/**
	 * Returns a string representation of the domain instantiated operators.
	 * 
	 * @param opList the method.
	 * @param constants the table of constants.
	 * @param types the table of types.
	 * @param predicates the table of predicates.
	 * @param functions the table of functions.
	 * @param facts the table of relevant facts.
	 * @param tasks the table of relevant tasks.
	 * @return a string representation of the domain instantiated operators.
	 */
	static String toStringOps(final List<BitOp> opList, final List<String> constants, final List<String> types, final List<String> predicates, final List<String> functions, final List<IntExp> fact, final List<IntExp> task, final String blank) {
		final StringBuffer str = new StringBuffer();
		str.append("\n;;;;;;;;;;;;;;;;;;;;;;;;;;;\n");
		str.append(";;;;;;;;;; Operators ;;;;;;\n");
		str.append(";;;;;;;;;;;;;;;;;;;;;;;;;;;\n\n");
		for (final BitOp op : opList) {
			str.append(toString(op, constants, types, predicates, functions, fact, blank));
		}
		return str.toString();
	}
	
	/**
	 * Returns a string representation of a bit expression.
	 * 
	 * @param exp the expression.
	 * @param constants the table of constants.
	 * @param types the table of types.
	 * @param predicates the table of predicates.
	 * @param functions the table of functions.
	 * @param relevants the table of relevant facts.
	 * @return a string representation of the specified expression.
	 */
	static String toString(final BitExp exp, final List<String> constants, final List<String> types, final List<String> predicates, final List<String> functions, final List<IntExp> relevants) {
		final StringBuffer str = new StringBuffer("(and");
		final BitSet positive = exp.getPositive();
		for (int i = positive.nextSetBit(0); i >= 0; i = positive.nextSetBit(i + 1)) {
			str.append(" ");
			str.append(StringEncoder.toString(relevants.get(i), constants, types, predicates, functions));
			str.append("");
		}
		final BitSet negative = exp.getNegative();
		for (int i = negative.nextSetBit(0); i >= 0; i = negative.nextSetBit(i + 1)) {
			str.append(" (not ");
			str.append(StringEncoder.toString(relevants.get(i), constants, types, predicates, functions));
			str.append(")");
		}
		str.append(")");
		return str.toString();
	}
	
	/**
	 * Returns a string representation of a conditional bit expression.
	 * 
	 * @param exp the conditional expression.
	 * @param constants the table of constants.
	 * @param types the table of types.
	 * @param predicates the table of predicates.
	 * @param functions the table of functions.
	 * @param relevants the table of relevant facts.
	 * @return a string representation of the specified expression.
	 */
	static String toString(final CondBitExp exp, final List<String> constants, final List<String> types, final List<String> predicates, final List<String> functions, final List<IntExp> relevants) {
		final StringBuffer str = new StringBuffer();
		if (exp.getCondition().isEmpty()) {
			str.append(StringEncoder.toString(exp.getEffects(), constants, types, predicates, functions, relevants));
		} else {
			str.append("(when ");
			str.append(StringEncoder.toString(exp.getCondition(), constants, types, predicates, functions, relevants));
			str.append("\n");
			str.append(StringEncoder.toString(exp.getEffects(), constants, types, predicates, functions, relevants));
			str.append(")");
		}
		return str.toString();
	}
	
	/**
	 * Returns a String representation of TaskList
	 * 
	 * @param taskList the list of tasks
	 * @return a string representation of the taskList
	 */
	public static String toString(final TaskList taskList, final Boolean printLabel, final Boolean printTag) {
		final StringBuffer str = new StringBuffer();
		if (printLabel) {
			if (taskList.getLabel()) {
				str.append("first (");
			} else {
				str.append("last (");
			}
		}
		if (printTag) {
			for (final int task : taskList.getTaskList()) {
				str.append("t" + task + " ");
			}
		} else {
			for (final int task : taskList.getTaskList()) {
				str.append(toString(Preprocessing.tableOfRelevantTasks.get(task)) + " ");
			}
		}
		
		if (printLabel) {
			str.append(")");
		}
		return str.toString();
	}
	
	/**
	 * Returns a String representation of an Order constraint
	 * 
	 * @param orderCon the order constraint to transform
	 * @param printLabel A boolean to say if the label first or last will appear in the taskList
	 * @param printTag A boolean to say if the task will appear as a tag or a textual representation
	 * @return a String representation of the order constraint
	 */
	public static String toString(final OrderConstraint orderCon, final Boolean printTag) {
		final StringBuffer str = new StringBuffer();
		str.append("(series ");
		str.append(toString(orderCon.getFirstTask(), false, printTag));
		str.append(" ");
		str.append(toString(orderCon.getSecondTask(), false, printTag));
		str.append(")");
		return str.toString();
	}
	
	/**
	 * Returns a String representation of a One task constraint
	 * 
	 * @param oneCon The constraint to transform
	 * @param bOrA A boolean to say if it's a before or an after constraint <code>true</code> for before, <code>false</code> for after
	 * @param printLabel A boolean to say if the label first or last will appear in the taskList
	 * @param printTag A boolean to say if the task will appear as a tag or a textual representation
	 * @return a string representation of a one task constraint
	 */
	public static String toString(final OneTaskConstraint oneCon, final Boolean bOrA, final Boolean printLabel, final Boolean printTag) {
		final StringBuffer str = new StringBuffer();
		if (bOrA) {
			str.append("(before ");
		} else {
			str.append("(after ");
		}
		str.append(toString(Preprocessing.tableOfRevelantFacts.get(oneCon.getGroundPredicate())) + " ");
		str.append(toString(oneCon.getTaskList(), printLabel, printTag));
		str.append(")");
		return str.toString();
	}
	
	/**
	 * Returns a String representation of a Two tasks constraint
	 * 
	 * @param twoCon The constraint to transform
	 * @param bOrC A boolean to say if it's a between or a protect constraint <code>true</code> for between, <code>false</code> for protect
	 * @param printTag A boolean to say if the task will appear as a tag or a textual representation
	 * @return a string representation of a two tasks constraint
	 */
	public static String toString(final TwoTasksConstraint twoCon, final Boolean bOrC, final Boolean printTag) {
		final StringBuffer str = new StringBuffer();
		if (bOrC) {
			str.append("(between ");
		} else {
			str.append("(protect ");
		}
		str.append(toString(Preprocessing.tableOfRevelantFacts.get(twoCon.getGroundPredicate())) + " ");
		str.append(toString(twoCon.getFirstTaskList(), false, printTag) + " " + toString(twoCon.getSecondTaskList(), false, printTag));
		str.append(")");
		return str.toString();
	}
	
	/**
	 * Returns a string representation of a conditional bit expression.
	 * 
	 * @param exp the conditional expression.
	 * @return a string representation of the specified expression.
	 */
	public static final String toString(final IntExp exp) {
		return toString(exp, Preprocessing.tableOfConstants, Preprocessing.tableOfTypes, Preprocessing.tableOfPredicates, Preprocessing.tableOfFunctions, Preprocessing.TableOfTaskSymbols);
	}
	
	/**
	 * Makes a String representation of the domain's Header
	 * 
	 * @param problemName
	 * @param requireKeys
	 * @param typedConstants
	 * @return a String representation of the domain's header
	 */
	public static String printDomainHeader(final String problemName, final Set<RequireKey> requireKeys, final List<TypedSymbol> typedConstants, final List<TypedSymbol> typedTypes) {
		final StringBuffer str = new StringBuffer();
		str.append(";;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;\n");
		str.append(";;;;;;;;;; " + problemName.toUpperCase() + "\n");
		str.append(";;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;\n\n");
		str.append("(define (domain " + problemName + ")\n");
		str.append("    (:requirements ");
		for (final RequireKey require : requireKeys) {
			str.append(require + " ");
		}
		str.append(")\n");
		str.append(printTypes(typedTypes) + "\n");
		str.append(printConstants(typedConstants) + "\n");
		str.append("    (:predicates)\n\n");
		return str.toString();
	}
	
	/**
	 * Makes a String representation of typed constants
	 * 
	 * @param typedConstants The typed constants of the problem
	 * @return a String representation of domain typed constants
	 */
	public static String printConstants(final List<TypedSymbol> typedConstants) {
		final StringBuffer str = new StringBuffer();
		str.append("    (:constants ");
		final Hashtable<List<Symbol>, List<String>> hashConst = new Hashtable<List<Symbol>, List<String>>();
		for (final TypedSymbol constant : typedConstants) {
			if (hashConst.get(constant.getTypes()) == null) {
				final List<String> listString = new ArrayList<String>();
				listString.add(constant.getImage());
				hashConst.put(constant.getTypes(), listString);
			} else if (!hashConst.get(constant.getTypes()).contains(constant.getImage())) {
				hashConst.get(constant.getTypes()).add(constant.getImage());
			}
		}
		for (final List<Symbol> type : hashConst.keySet()) {
			for (final String constant : hashConst.get(type)) {
				str.append(constant + " ");
			}
			str.append("- " + type.get(0));
		}
		str.append(")");
		return str.toString();
	}
	
	/**
	 * Makes a String representation of typed types
	 * 
	 * @param typedTypes The Problem typed types
	 * @return a String representation of domain typed types
	 */
	public static String printTypes(final List<TypedSymbol> typedTypes) {
		final StringBuffer str = new StringBuffer();
		str.append("    (:types ");
		final Hashtable<List<Symbol>, List<String>> hashType = new Hashtable<List<Symbol>, List<String>>();
		
		for (int i = 1; i < typedTypes.size(); i++) {
			final TypedSymbol type = typedTypes.get(i);
			if (hashType.get(type.getTypes()) == null) {
				final List<String> listString = new ArrayList<String>();
				listString.add(type.getImage());
				hashType.put(type.getTypes(), listString);
			} else if (!hashType.get(type.getTypes()).contains(type.getImage())) {
				hashType.get(type.getTypes()).add(type.getImage());
			}
		}
		for (final List<Symbol> bigType : hashType.keySet()) {
			for (final String type : hashType.get(bigType)) {
				str.append(type + " ");
			}
			str.append("- " + bigType.get(0));
		}
		
		str.append(")");
		return str.toString();
		
	}
	
}
