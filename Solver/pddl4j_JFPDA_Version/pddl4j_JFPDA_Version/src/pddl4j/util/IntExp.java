/*
 * Copyright (c) 2010 by Damien Pellier <Damien.Pellier@imag.fr>.
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pddl4j.parser.Connective;
import pddl4j.parser.Symbol;

/**
 * This class is used to encode expressions in compact encoding, i.e., with integer representation.
 * 
 * @author D. Pellier
 * @version 1.0 - 07.04.2010
 */
public class IntExp implements Serializable {

	/**
	 * The constant used to encode the specific predicate equal.
	 */
	public static final int EQUAL_PREDICATE = -1;

	/**
	 * The connective of the expression.
	 */
	private Connective connective;

	/**
	 * The integer representation of the predicate.
	 */
	private int predicate;

	/**
	 * The list of arguments of the expression. This attribute is used to store the argument of the
	 * atomic expression.
	 */
	private int[] arguments;

	/**
	 * The children of the expression.
	 */
	private List<IntExp> children;

	/**
	 * The variable is used by quantified expression.
	 */
	private int variable;

	/**
	 * The type of the variable used by a quantified expression.
	 */
	private int type;

	/**
	 * The value of the expression. This attribute is used to store value of number expression.
	 */
	private double value;

	/**
	 * Create a new expression from an other one. This constructor make a deep copy of the specified
	 * expression.
	 * 
	 * @param other the expression.
	 */
	public IntExp(final IntExp other) {
		this.connective = other.getConnective();
		this.predicate = other.getPredicate();
		this.arguments = other.getArguments();
		if (this.arguments != null) {
			this.arguments = Arrays.copyOf(other.getArguments(), other.getArguments().length);
		}
		List<IntExp> otherChildren = other.getChildren();
		this.children = new ArrayList<IntExp>(otherChildren.size());
		for (IntExp child : otherChildren) {
			this.children.add(new IntExp(child));
		}
		this.variable = other.getVariable();
		this.type = other.getType();
		this.value = other.getValue();
	}

	/**
	 * Create a new expression with a specified connective.
	 * 
	 * @param connective the connective of the expression.
	 */
	public IntExp(final Connective connective) {
		this.connective = connective;
		this.arguments = null;
		this.children = new ArrayList<IntExp>();
	}
	
	/**
	 * Return the connective of the expression.
	 * 
	 * @return the connective of the expression.
	 */
	public final Connective getConnective() {
		return this.connective;
	}

	/**
	 * Set a new connective to this expression.
	 * 
	 * @param connective the new connective to set.
	 * @throws NullPointerException if connective == null.
	 */
	public final void setConnective(final Connective connective) throws NullPointerException {
		if (connective == null)
			throw new NullPointerException("connective == null");
		this.connective = connective;
	}

	/**
	 * Return the list of children of this expression.
	 * 
	 * @return the list of children of this expression.
	 */
	public final List<IntExp> getChildren() {
		return this.children;
	}

	/**
	 * Return the predicate of this expression.
	 * 
	 * @return the predicate
	 */
	public final int getPredicate() {
		return predicate;
	}

	/**
	 * Set a new predicate to this expression.
	 * 
	 * @param predicate the new predicate to set
	 */
	public final void setPredicate(int predicate) {
		this.predicate = predicate;
	}

	/**
	 * Returns the list of argument of this expression.
	 * 
	 * @return the arguments the list of arguments of this expression.
	 */
	public final int[] getArguments() {
		return this.arguments;
	}

	/**
	 * Set the arguments of the expression.
	 * 
	 * @param args the arguments to set.
	 */
	public final void setArguments(final int[] args) {
		this.arguments = args;
	}
		
	/**
	 * Return the variable of the expression.
	 * 
	 * @return the variable of the expression.
	 */
	public final int getVariable() {
		return this.variable;
	}

	/**
	 * Set a new quantified variable to the expression.
	 * 
	 * @param variable the new quantified variable to set
	 */
	public final void setVariable(final int variable) {
		this.variable = variable;
	}

	/**
	 * Returns the type of the quantified variable of the expression.
	 * 
	 * @return the type of the quantified variable of the expression.
	 */
	public final int getType() {
		return this.type;
	}

	/**
	 * Set a new type to the quantified variable of the expression.
	 * 
	 * @param type the type to the quantified variable to set.
	 */
	public final void setType(final int type) {
		this.type = type;
	}

	/**
	 * Returns the value of this expression.
	 * 
	 * @return the value of this expression.
	 */
	public final double getValue() {
		return this.value;
	}

	/**
	 * Set a new value to the expression.
	 * 
	 * @param value the new value to set
	 */
	public final void setValue(double value) {
		this.value = value;
	}

	/**
	 * Affect this expression to an other. After affectation this expression and the other are
	 * equal. No copy of the content of the other expression is done.
	 * 
	 * @param other expression.
	 */
	public final void affect(final IntExp other) {
		this.connective = other.getConnective();
		this.predicate = other.getPredicate();
		this.arguments = other.getArguments();
		this.children = other.getChildren();
		this.variable = other.getVariable();
		this.type = other.getType();
		this.value = other.getValue();
	}
	
	/** 
	 * Return if the expression is equal to an other object.
	 * 
	 * @param object the other object.
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(final Object object) {
		if (object != null && object instanceof IntExp) {
			final IntExp other = (IntExp) object;
			return this.connective.equals(other.connective)
					&& this.predicate == other.predicate
					&& Arrays.equals(this.arguments, other.arguments)
					&& this.value == other.value
					&& this.variable == other.variable
					&& this.type == other.type
					&& this.children.equals(other.children);
		}
		return false;
	}

	/**
	 * Return the hash code value of the expression.
	 * 
	 * @return the hash code value of the expression.
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(this.arguments);
		result = prime * result + this.children.hashCode();
		result = prime * result + this.connective.hashCode();
		result = prime * result + this.predicate;
		result = prime * result + this.type;
		long temp;
		temp = Double.doubleToLongBits(this.value);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + this.variable;
		return result;
	}
	
	/**
	 * Returns a string representation of an expression.
	 * 
	 * @param constants the table of constants.
	 * @param types the table of types.
	 * @param predicates the table of predicates.
	 * @param functions the table of functions.
	 * @param tasks the table of tasks symbols.
	 * @return a string representation of the specified expression.
	 */
	public String toString(final List<String> constants,
			final List<String> types, final List<String> predicates,
			final List<String> functions, final List<String> tasks) {
		return this.toString(this, constants, types, predicates,
				functions, tasks, " ");
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
	public String toString(final IntExp exp, final List<String> constants,
			final List<String> types, final List<String> predicates,
			final List<String> functions, final List<String> tasks,
			final String separator) {
		return this.toString(exp, constants, types, predicates,
				functions, tasks, "", separator);
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
	public String toString(final IntExp exp, final List<String> constants,
			final List<String> types, final List<String> predicates, final List<String> functions,
			final List<String> tasks, String offset, final String separator) {
		final StringBuffer str = new StringBuffer();
		switch (exp.getConnective()) {
		case ATOM:
			str.append("(");
			str.append(predicates.get(exp.getPredicate()));
			int[] args = exp.getArguments();
			for (int i = 0; i < args.length; i++) {
				int index = args[i];
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
				int index = args[i];
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
				int index = args[i];
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
				int index = args[i];
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
					str.append(this.toString(exp.getChildren().get(i), constants, types, predicates, functions, tasks, offset) + "\n"
							+ offset);
				}
				str.append(this.toString(exp.getChildren().get(
						exp.getChildren().size() - 1), constants, types, predicates, functions, tasks, offset));
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
			if (exp.getChildren().size() ==  1) {
				str.append(this.toString(exp.getChildren().get(0), constants, types, predicates, functions, tasks, offset));
			}
			str.append(")");
			offset = offset.substring(0, offset.length() - 2);
			break;
		case NUMBER:
			str.append(exp.getValue());
			break;
		case F_EXP:
			str.append(this.toString(exp.getChildren().get(0), constants, types, predicates, functions, tasks, offset));
			break;
		case F_EXP_T:
			str.append(exp.getConnective());
			/*
			 * if (this.children.isEmpty()) { str.append(this.getVariable()); } else {
			 * str.append("("); str.append(this.getConnective()); str.append(" ");
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
			str.append(this.toString(exp.getChildren().get(0), constants, types, predicates, functions, tasks, offset));
			str.append(" ");
			str.append(this.toString(exp.getChildren().get(1), constants, types, predicates, functions, tasks, offset));
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
			str.append(this.toString(exp.getChildren().get(0), constants, types, predicates, functions, tasks, offset));
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
				str.append(this.toString(exp.getChildren().get(i), constants, types, predicates, functions, tasks, offset));
			}
			str.append(")");
			break;
		case EXPANSION:
			str.append("(");
			for (int i = 0; i < exp.getChildren().size(); i++) {
				str.append(this.toString(exp.getChildren().get(i), constants, types, predicates, functions, tasks, offset));
				if (i <  exp.getChildren().size() - 1) str.append(" ");
			}
			str.append(")");
			break;
		default:
			break;
		}
		return str.toString();
	}

	
}
