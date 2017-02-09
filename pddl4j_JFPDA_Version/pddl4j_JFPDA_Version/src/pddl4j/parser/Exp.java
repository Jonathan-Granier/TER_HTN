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

package pddl4j.parser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * This class implements a parser node used in PDDL expressions.
 * <p>
 * Modifications:
 * <ul>
 * <li>Add method <code>isLiteral()</code> 11.12.2012.</li>
 * <li>Add attribute <code>tag()</code> 20.03.2015.</li>
 * </ul>
 * </p>
 * 
 * @author D. Pellier
 * @version 1.0 - 28.01.2010
 */
public class Exp
implements Serializable {
	
	/**
	 * The serial version id of the class.
	 */
	private static final long serialVersionUID = 1943664302879209785L;
	
	/**
	 * The type of the node.
	 */
	private Connective connective;
	
	/**
	 * only for parsing: the variable in quantifiers
	 */
	private List<TypedSymbol> variables;
	
	/**
	 * AND, OR, NOT, WHEN => NULL ALL, EX => the quantified variable with its type ATOM => the atom as predicate->param1->param2->...
	 */
	private List<Symbol> atom;
	
	/**
	 * (a) for AND, OR this is the list of sons(a AND b AND c...), (b) for the rest this is the son, e.g. a subtree that is negated (c) for WHEN, the first son
	 * is the condition and the next son is the effect
	 */
	private List<Exp> children;
	
	/**
	 * The value associate to this node.
	 */
	private Double value;
	
	/**
	 * The name of the preference.
	 */
	private Symbol pref_name;
	
	/**
	 * The tag of the expression.
	 */
	private Symbol tag;
	
	/**
	 * The variable.
	 */
	private Symbol variable;
	
	/**
	 * Creates a new expression from a other one.
	 * 
	 * @param other the other expression.
	 * @throws NullPointerException if other is null.
	 */
	public Exp(final Exp other) {
		if (other == null) {
			throw new NullPointerException("other == null");
		}
		connective = other.getConnective();
		if (other.getAtom() != null) {
			atom = new ArrayList<Symbol>();
			for (final Symbol symbol : other.getAtom()) {
				atom.add(new Symbol(symbol));
			}
		}
		if (other.getChildren() != null) {
			children = new ArrayList<Exp>();
			for (final Exp exp : other.getChildren()) {
				children.add(new Exp(exp));
			}
		}
		pref_name = other.getPrefName();
		if (other.getVariables() != null) {
			variables = new ArrayList<TypedSymbol>();
			for (final Symbol var : other.getVariables()) {
				variables.add(new TypedSymbol(var));
			}
		}
		if (other.getVariable() != null) {
			variable = new Symbol(other.getVariable());
		}
		if (other.getVariable() != null) {
			value = other.getValue();
		}
		tag = other.getTag();
	}
	
	/**
	 * Creates a new planning node.
	 */
	private Exp() {
		super();
		connective = Connective.AND;
		atom = null;
		children = new ArrayList<Exp>();
		pref_name = null;
		variables = null;
		value = null;
		tag = null;
	}
	
	/**
	 * Creates a new planning node with a specified connective.
	 * 
	 * @param connective the connective.
	 * @throws NullPointerException if the specified connective is null.
	 */
	public Exp(final Connective connective)
	throws NullPointerException {
		this();
		if (connective == null) {
			throw new NullPointerException();
		}
		this.connective = connective;
	}
	
	/**
	 * Adds a son to this node.
	 * 
	 * @param exp the son to add.
	 * @return <code>true</code< if the node was added; <code>false</code> otherwise.
	 * @throws NullPointerException if the specified node is null.
	 */
	public boolean addChild(final Exp exp)
	throws NullPointerException {
		if (exp == null) {
			throw new NullPointerException();
		}
		return children.add(exp);
	}
	
	/**
	 * Sets the parse variable of this node, i.e., the var args in quantifiers.
	 * 
	 * @param variables the parse variables.
	 */
	public void setVariables(final List<TypedSymbol> variables) {
		this.variables = variables;
	}
	
	/**
	 * Returns the variable of this parser node.
	 * 
	 * @return the variable of this parser node.
	 */
	public final Symbol getVariable() {
		return variable;
	}
	
	/**
	 * Sets a new variable to this parser node.
	 * 
	 * @param variable the new variable to set.
	 */
	public void setVariable(final Symbol variable) {
		this.variable = variable;
	}
	
	/**
	 * Sets the atom of this node.
	 * 
	 * @param atom the atom of this node.
	 */
	public final void setAtom(final List<Symbol> atom) {
		this.atom = atom;
	}
	
	/**
	 * Set the connective of this node.
	 * 
	 * @param connective the connective.
	 * @throws NullPointerException if the specified connective is null.
	 */
	public void setConnective(final Connective connective)
	throws NullPointerException {
		if (connective == null) {
			throw new NullPointerException();
		}
		this.connective = connective;
	}
	
	/**
	 * Set the value of this node.
	 * 
	 * @param value the value of this node.
	 */
	public final void setValue(final double value) {
		this.value = value;
	}
	
	/**
	 * Sets a name to the preference.
	 * 
	 * @param name the name of the preference to set.
	 */
	public final void setPrefName(final Symbol name) {
		pref_name = name;
	}
	
	/**
	 * Sets a tag to the expression.
	 * 
	 * @param tag the tag of the expression.
	 */
	public final void setTag(final Symbol tag) {
		this.tag = tag;
	}
	
	/**
	 * Returns the list of child of this parser node.
	 * 
	 * @return the list of child of this parser node.
	 */
	public final List<Exp> getChildren() {
		return children;
	}
	
	/**
	 * Returns the name of the preference.
	 * 
	 * @return the name of the preference or <code>null</code> if the preference name was not initialized.
	 */
	public final Symbol getPrefName() {
		return pref_name;
	}
	
	/**
	 * Returns the tag of the expression.
	 * 
	 * @return the tag of the expression or <code>null</code> if the preference name was not initialized.
	 */
	public final Symbol getTag() {
		return tag;
	}
	
	/**
	 * Returns the connective of this parser node.
	 * 
	 * @return the connective of this parser node.
	 */
	public final Connective getConnective() {
		return connective;
	}
	
	/**
	 * Returns the list of variables of this parser node. This list of variable is used to store the quantified variable of the PDDL logical expression.
	 * 
	 * @return the list of variables of this parser node.
	 */
	public final List<TypedSymbol> getVariables() {
		return variables;
	}
	
	/**
	 * Returns if this expression is a literal. A literal is an atomic formula or a negated atomic formula.
	 * 
	 * @return <code>true</code> if this expression is a literal <code>false</code> otherwise.
	 */
	public final boolean isLiteral() {
		return getConnective().equals(Connective.ATOM) || (getConnective().equals(Connective.NOT) && getChildren().size() == 1 && getChildren().get(0).getConnective().equals(Connective.ATOM));
	}
	
	/**
	 * Returns the atom of this parser node.
	 * 
	 * @return the atom
	 */
	public final List<Symbol> getAtom() {
		return atom;
	}
	
	/**
	 * Returns the value of this parser node.
	 * 
	 * @return the value of this parser node.
	 */
	public final double getValue() {
		return value;
	}
	
	/**
	 * Returns if this parser node is a preference.
	 * 
	 * @return <code>true</code> if this parser node is a preference; <code>false</code> otherwise.
	 */
	public final boolean isPreference() {
		return pref_name != null;
	}
	
	/**
	 * Renames the variables contained in the expression. The variable renames have the form ?X1, ..., ?Xn.
	 */
	public void renameVariables() {
		this.renameVariables(new LinkedHashMap<String, String>());
	}
	
	/**
	 * Renames the variables contained in the expression with a specified symbol, i.e., the variable already renamed. The variable renames have the form ?X1,
	 * ..., ?Xn.
	 * 
	 * @param context the images of the renamed variable.
	 */
	public void renameVariables(final Map<String, String> context) {
		switch (getConnective()) {
			case ATOM:
			case FN_HEAD:
			case EQUAL_ATOM:
			case TASK:
				for (int i = 0; i < getAtom().size(); i++) {
					getAtom().get(i).renameVariables(context);
				}
				break;
			case AND:
			case OR:
			case EXPANSION:
				for (int i = 0; i < getChildren().size(); i++) {
					getChildren().get(i).renameVariables(context);
				}
				break;
			case FORALL:
			case EXISTS:
				final Map<String, String> newContext = new LinkedHashMap<String, String>(context);
				for (int i = 0; i < getVariables().size(); i++) {
					final TypedSymbol variable = getVariables().get(i);
					final String image = variable.renameVariables(newContext.size() + i);
					newContext.put(image, variable.getImage());
				}
				getChildren().get(0).renameVariables(newContext);
				break;
			case F_EXP_T:
				if (!getChildren().isEmpty()) {
					getChildren().get(0).renameVariables(context);
				}
				break;
			case EQUAL:
			case FN_ATOM:
			case WHEN:
			case DURATION_ATOM:
			case LESS:
			case LESS_OR_EQUAL:
			case GREATER:
			case GREATER_OR_EQUAL:
			case MUL:
			case DIV:
			case MINUS:
			case PLUS:
			case ASSIGN:
			case INCREASE:
			case DECREASE:
			case SCALE_UP:
			case SCALE_DOWN:
				getChildren().get(0).renameVariables(context);
				getChildren().get(1).renameVariables(context);
				break;
			case AT_START:
			case AT_END:
			case MINIMIZE:
			case MAXIMIZE:
			case UMINUS:
			case NOT:
			case ALWAYS:
			case OVER_ALL:
			case SOMETIME:
			case AT_MOST_ONCE:
			case F_EXP:
			case AFTER:
			case BEFORE:
			case BETWEEN:
				getChildren().get(0).renameVariables(context);
				break;
			case HOLD_AFTER:
			case WITHIN:
				getChildren().get(1).renameVariables(context);
				break;
			case ALWAYS_WITHIN:
				getChildren().get(1).renameVariables(context);
				getChildren().get(2).renameVariables(context);
				break;
			case HOLD_DURING:
				getChildren().get(2).renameVariables(context);
				break;
			case IS_VIOLATED:
			case NUMBER:
			case TIME_VAR:
			case SERIES:
			case TASK_TAG:
			default:
				break;
		}
	}
	
	/**
	 * Moves the negation inward the expression.
	 */
	public void moveNegationInward() {
		switch (connective) {
			case AND:
			case OR:
				for (int i = 0; i < children.size(); i++) {
					children.get(i).moveNegationInward();
				}
				break;
			case FORALL:
			case EXISTS:
			case AT_START:
			case AT_END:
			case OVER_ALL:
			case ALWAYS:
			case SOMETIME:
			case AT_MOST_ONCE:
			case AFTER:
			case BEFORE:
			case BETWEEN:
				getChildren().get(0).moveNegationInward();
				break;
			case WHEN:
			case SOMETIME_AFTER:
			case SOMETIME_BEFORE:
			case HOLD_AFTER:
			case WITHIN:
				getChildren().get(0).moveNegationInward();
				getChildren().get(1).moveNegationInward();
				break;
			case NOT:
				negate();
				break;
			case ALWAYS_WITHIN:
				getChildren().get(1).moveNegationInward();
				getChildren().get(2).moveNegationInward();
				break;
			case HOLD_DURING:
				getChildren().get(2).moveNegationInward();
				break;
			case ATOM:
			case FN_HEAD:
			case EQUAL_ATOM:
			case F_EXP:
			case NUMBER:
			case F_EXP_T:
			case TIME_VAR:
			case FN_ATOM:
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
			case UMINUS:
			case MINIMIZE:
			case MAXIMIZE:
			case IS_VIOLATED:
			case SERIES:
			case TASK_TAG:
			case TASK:
			case EXPANSION:
				break;
			default:
				break;
		}
	}
	
	/**
	 * Negates the expression.
	 */
	private void negate() {
		final Exp exp = getChildren().get(0);
		switch (exp.getConnective()) {
			case FORALL:
				setConnective(Connective.EXISTS);
				Exp negation = new Exp(Connective.NOT);
				negation.addChild(exp.getChildren().get(0));
				negation.moveNegationInward();
				children.set(0, negation);
				break;
			case EXISTS:
				setConnective(Connective.FORALL);
				setVariables(exp.getVariables());
				negation = new Exp(Connective.NOT);
				negation.addChild(exp.getChildren().get(0));
				negation.moveNegationInward();
				children.set(0, negation);
				break;
			case AND:
				setConnective(Connective.OR);
				children.clear();
				for (int i = 0; i < exp.getChildren().size(); i++) {
					negation = new Exp(Connective.NOT);
					negation.addChild(exp.getChildren().get(i));
					negation.moveNegationInward();
					children.add(negation);
				}
				break;
			case OR:
				setConnective(Connective.AND);
				children.clear();
				for (int i = 0; i < exp.getChildren().size(); i++) {
					negation = new Exp(Connective.NOT);
					negation.addChild(exp.children.get(i));
					negation.moveNegationInward();
					children.add(negation);
				}
				break;
			case NOT:
				final Exp neg = exp.getChildren().get(0);
				atom = neg.getAtom();
				children = neg.getChildren();
				connective = neg.getConnective();
				pref_name = neg.getPrefName();
				value = neg.getValue();
				variable = neg.getVariable();
				variables = neg.getVariables();
				moveNegationInward();
				break;
			default:
				for (int i = 0; i < exp.getChildren().size(); i++) {
					exp.children.get(i).negate();
				}
		}
	}
	
	/**
	 * Return if this expression is equal to another object.
	 * 
	 * @param object the other object.
	 * @return <tt>true</tt> if this expression is equal to <tt>object</tt>, i.e., <tt>other</tt> is not null and is an instance of <tt>Exp</tt> and it has the
	 *         same connective, children, atom, value, preference name, variable and value; otherwise return <tt>false</tt>.
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(final Object object) {
		if (object != null && object instanceof Exp) {
			final Exp other = (Exp) object;
			return connective.equals(other.connective) && ((atom == null && other.atom == null) || (atom.equals(other.atom))) && children.equals(other.children) && ((pref_name == null && other.pref_name == null) || (pref_name.equals(other.pref_name))) && ((variables == null && other.variables == null) || (variables.equals(other.variables))) && ((value == null && other.value == null) || (value.equals(other.value))) && ((tag == null && other.tag == null) || (tag.equals(other.tag)));
		}
		return false;
	}
	
	/**
	 * Returns the hash code value of this expression.
	 * 
	 * @return the hash code value of this expression.
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((atom == null) ? 0 : atom.hashCode());
		result = prime * result + ((children == null) ? 0 : children.hashCode());
		result = prime * result + ((connective == null) ? 0 : connective.hashCode());
		result = prime * result + ((pref_name == null) ? 0 : pref_name.hashCode());
		long temp;
		temp = Double.doubleToLongBits(value);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((variable == null) ? 0 : variable.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		result = prime * result + ((tag == null) ? 0 : tag.hashCode());
		return result;
	}
	
	/**
	 * Returns if a specified expression is contains, i.e., is a sub-expression of this expression. More formally, returns <code>true</code> if and only if this
	 * expression contains at least one subexpression <code>s</code> such that <code>s.equals(exp)</code>.
	 * 
	 * @param exp the expression to test.
	 * @return <code>true</code> if the specified expression <code>exp</code> is a sub-expression of this expression; <code>false</code> otherwise.
	 */
	public final boolean contains(final Exp exp) {
		final Iterator<Exp> it = getChildren().iterator();
		while (it.hasNext()) {
			final Exp s = it.next();
			if (s.equals(exp) || s.contains(exp)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Removes all the occurrences of a specified expression contained in this expression and returns <code>true</code> if and only if at least one occurrence
	 * was removed.
	 * 
	 * @param exp the expression to remove.
	 * @return <code>true</code> if the specified expression <code>exp</code> was removed; <code>false</code> otherwise.
	 */
	public final boolean remove(final Exp exp) {
		boolean removed = false;
		final Iterator<Exp> it = getChildren().iterator();
		while (it.hasNext()) {
			final Exp s = it.next();
			if (s.equals(exp)) {
				it.remove();
				removed = true;
			} else {
				removed = removed || s.remove(exp);
			}
		}
		return removed;
	}
	
	/**
	 * Replaces all the occurrences of a specified expression contained in this expression and returns <code>true</code> if and only if at least one occurrence
	 * was replaced.
	 * 
	 * @param e1 the expression to be replaced.
	 * @param e2 the expression to replaced.
	 * @return <code>true</code> if the specified expression <code>exp</code> was replaced; <code>false</code> otherwise.
	 */
	public final boolean replace(final Exp e1, final Exp e2) {
		boolean replaced = false;
		for (int i = 0; i < getChildren().size(); i++) {
			final Exp s = getChildren().get(i);
			if (s.equals(e1)) {
				getChildren().set(i, e2);
				replaced = true;
			} else {
				replaced = s.replace(e1, e2) || replaced;
			}
		}
		return replaced;
	}
	
	/**
	 * Returns a string representation of this node.
	 * 
	 * @return a string representation of this node.
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return this.toString("");
	}
	
	/**
	 * Returns a string representation of this parser node.
	 * 
	 * @param offset the offset white space from the left used for indentation.
	 * @return a string representation of this parser node.
	 */
	private String toString(String offset) {
		final StringBuffer str = new StringBuffer();
		switch (connective) {
			case ATOM:
			case TASK:
			case FN_HEAD:
				str.append("(");
				for (int i = 0; i < atom.size() - 1; i++) {
					str.append(atom.get(i).toString());
					str.append(" ");
				}
				str.append(atom.get(atom.size() - 1).toString());
				str.append(")");
				break;
			case EQUAL_ATOM:
				str.append("(");
				str.append(getConnective().getImage());
				str.append(" ");
				for (int i = 0; i < atom.size() - 1; i++) {
					str.append(atom.get(i).toString());
					str.append(" ");
				}
				str.append(atom.get(atom.size() - 1).toString());
				str.append(")");
				break;
			case AND:
			case OR:
				offset += "  ";
				str.append("(");
				str.append(getConnective().getImage());
				str.append(" ");
				for (int i = 0; i < children.size() - 1; i++) {
					str.append(children.get(i).toString(offset) + "\n" + offset);
				}
				str.append(children.get(children.size() - 1).toString(offset));
				str.append(")");
				offset = offset.substring(0, offset.length() - 2);
				break;
			case EXPANSION:
				offset += "  ";
				str.append("(");
				for (int i = 0; i < children.size() - 1; i++) {
					str.append("(tag ");
					str.append(children.get(i).getTag().toString());
					str.append(" ");
					str.append(children.get(i).toString(offset) + "\n" + offset);
				}
				str.append("(tag ");
				str.append(children.get(children.size() - 1).getTag().toString());
				str.append(" ");
				str.append(children.get(children.size() - 1).toString(offset) + ")" + offset);
				offset = offset.substring(0, offset.length() - 2);
				break;
			case FORALL:
			case EXISTS:
				offset += offset + "  ";
				str.append(" (");
				str.append(getConnective().getImage());
				str.append(" (");
				for (int i = 0; i < variables.size() - 1; i++) {
					str.append(variables.get(i).toString());
					str.append(", ");
				}
				str.append(variables.get(variables.size() - 1).toString());
				str.append(")\n" + offset);
				str.append(children.get(0).toString(offset));
				str.append(")");
				offset = offset.substring(0, offset.length() - 2);
				break;
			case NUMBER:
				str.append(value);
				break;
			case F_EXP:
				str.append(children.get(0).toString(offset));
				break;
			case F_EXP_T:
				if (children.isEmpty()) {
					str.append(getVariable());
				} else {
					str.append("(");
					str.append(getConnective().getImage());
					str.append(" ");
					str.append(getVariable());
					str.append(" ");
					str.append(children.get(0).toString(offset));
				}
				break;
			case TIME_VAR:
				str.append(getVariable());
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
				str.append(getConnective().getImage());
				str.append(" ");
				str.append(children.get(0).toString(offset));
				str.append(" ");
				str.append(children.get(1).toString(offset));
				str.append(")");
				break;
			case NOT:
			case UMINUS:
			case AT_START:
			case AT_END:
			case OVER_ALL:
			case ALWAYS:
			case SOMETIME:
			case AT_MOST_ONCE:
				str.append("(");
				str.append(getConnective().getImage());
				str.append(" ");
				str.append(getChildren().get(0).toString(offset));
				str.append(")");
				break;
			case MINIMIZE:
			case MAXIMIZE:
				str.append(getConnective().getImage());
				str.append(" ");
				str.append(getChildren().get(0).getValue());
				str.append(")");
			case IS_VIOLATED:
				str.append("(");
				str.append(getConnective().getImage());
				str.append(")");
				break;
			case HOLD_AFTER:
			case WITHIN:
				str.append("(");
				str.append(getConnective().getImage());
				str.append(" ");
				str.append(getChildren().get(0).getValue());
				str.append(" ");
				str.append(getChildren().get(1).toString(offset));
				str.append(")");
				break;
			case ALWAYS_WITHIN:
				str.append("(");
				str.append(getConnective().getImage());
				str.append(" ");
				str.append(getChildren().get(0).getValue());
				str.append(" ");
				str.append(getChildren().get(1).toString(offset));
				str.append(" ");
				str.append(getChildren().get(2).toString(offset));
				str.append(")");
				break;
			case HOLD_DURING:
				str.append("(");
				str.append(getConnective().getImage());
				str.append(" ");
				str.append(getChildren().get(0).getValue());
				str.append(" ");
				str.append(getChildren().get(1).getValue());
				str.append(" ");
				str.append(getChildren().get(2).toString(offset));
				str.append(")");
				break;
			case AFTER:
			case BEFORE:
			case BETWEEN:
			case SERIES:
				str.append("(");
				str.append(getConnective().getImage());
				for (final Exp child : getChildren()) {
					str.append(" ");
					str.append(child.toString(offset));
				}
				str.append(")");
				break;
			case FIRST:
			case LAST:
				str.append(getConnective().getImage());
				str.append("(");
				for (final Exp child : getChildren()) {
					str.append(" ");
					str.append(child.toString(offset));
				}
				str.append(")");
				break;
			case TASK_TAG:
				str.append(tag.toString());
				
			default:
				break;
				
		}
		return str.toString();
	}
}
