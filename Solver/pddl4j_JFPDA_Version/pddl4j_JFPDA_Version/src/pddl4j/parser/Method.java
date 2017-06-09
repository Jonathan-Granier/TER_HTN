/*
 * Copyright (c) 2015 by Abdeldjalil Ramoul <abdeldjalil.ramoul@imag.fr>
 *                       Damien Pellier <Damien.Pellier@imag.fr>.
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * This class implements a decomposition method in HTN planning.
 * 
 * @author A. Ramoul, D. Pellier
 * @version 0.8 - 02.2015
 */

public class Method implements Serializable {

	/**
	 * The serial version id of the class.
	 */
	private static final long	serialVersionUID	= 4762675339324969434L;

	/**
	 * The name of the method which is the same name of the task decomposed by the
	 * method
	 */
	private Symbol						name;

	/**
	 * The parameters of the method
	 */
	private List<TypedSymbol>	parameters;

	/**
	 * The undeclared parameters of the method
	 */
	private List<TypedSymbol>	undeclaredParameters;

	/**
	 * A list of taskNetworks available for this method
	 */
	private Exp								expansion;

	/**
	 * An expression which contain the constraints of the the method
	 */
	private Exp								constraints;

	/**
	 * Creates a new empty method
	 */
	public Method() {
		super();
		this.name = null;
		this.parameters = null;
		this.undeclaredParameters = null;
		this.expansion = null;
		this.constraints = null;
	}

	/**
	 * Creates a new method.
	 * 
	 * @param name The name of the method.
	 * @param parameters the list of the method parameters
	 * @param expansions An expression containing The expansion of the method
	 * @param constraints An expression containing the constraints of the method
	 * @throws NullPointerException
	 */
	public Method(final Symbol name, final List<TypedSymbol> parameters, final Exp expansions, final Exp constraints)
			throws NullPointerException {
		if (name == null || parameters == null || expansions == null || constraints == null)
			throw new NullPointerException();
		this.name = name;
		this.parameters = parameters;
		this.undeclaredParameters = new ArrayList<TypedSymbol>();
		this.expansion = expansions;
		this.constraints = constraints;
	}

	/**
	 * Setter of name
	 * 
	 * @param name
	 */
	public void setName(final Symbol name) {
		this.name = name;
	}

	/**
	 * Setter of parameters
	 * 
	 * @param parameters
	 */
	public void setParameters(final List<TypedSymbol> parameters) {
		this.parameters = parameters;
	}

	/**
	 * Setter of undeclared parameters
	 * 
	 * @param parameters
	 */
	public void setUndeclaredParameters(final List<TypedSymbol> undeclaredParameters) {
		this.undeclaredParameters = undeclaredParameters;
	}

	/**
	 * Setter of expansions
	 * 
	 * @param decompositions
	 */
	public void setExpansion(final Exp decompositions) {
		this.expansion = decompositions;
	}

	/**
	 * Setter of constraints
	 * 
	 * @param constraints
	 */
	public void setConstraints(final Exp constraints) {
		this.constraints = constraints;
	}

	/**
	 * Getter of name
	 * 
	 * @return Symbol name
	 */
	public Symbol getName() {
		return this.name;
	}

	/**
	 * Getter of parameters
	 * 
	 * @return List<TypedSymbol> parameters
	 */
	public List<TypedSymbol> getParameters() {
		return this.parameters;
	}

	/**
	 * Getter of undeclared parameters
	 * 
	 * @return List<TypedSymbol> parameters
	 */
	public List<TypedSymbol> getUndeclaredParameters() {
		return this.undeclaredParameters;
	}

	/**
	 * Returns the declared type in the method of a passed parameter
	 * 
	 * @param parameter The parameter of which we desire the type
	 * @return <code> The parameter's types</code> if it's defined or
	 *         <code> null </code> otherwise
	 */
	public final List<Symbol> getParameterTypes(Symbol parameter) {
		int index = -1;
		//Search for the parameter in the method parameters
		index = this.parameters.indexOf(parameter);
		if (index != -1) {
			return parameters.get(index).getTypes();
		}
		//Search for the parameter in method undeclared parameters
		index = this.undeclaredParameters.indexOf(parameter);
		if (index != -1) {
			return undeclaredParameters.get(index).getTypes();
		}
		return null;
	}

	/**
	 * Adds a parameter to undeclared parameters List
	 * 
	 * @param undeclaredPar The parameter to add
	 */
	public void addUndeclaredParameter(final TypedSymbol undeclaredPar) {
		if (undeclaredPar == null)
			throw new NullPointerException("Undeclared parameter to add is umpty");
		else
			this.undeclaredParameters.add(undeclaredPar);
	}

	/**
	 * Returns the parameter of the method that has a specified symbol.
	 * 
	 * @param symbol The symbol.
	 * @return the parameter of the method that has a specified symbol or
	 *         <code>null</code> if the operator has no such parameter.
	 */
	public final TypedSymbol getParameter(final Symbol symbol) {
		final int index = this.parameters.indexOf(symbol);
		return (index == -1) ? null : this.parameters.get(index);
	}

	/**
	 * Return the arity of the operator, i.e., the number of parameters of the
	 * operator.
	 * 
	 * @return the arity of the operator.
	 */
	public final int getArity() {
		return this.parameters.size();
	}

	/**
	 * Returns the expansion of the method.
	 * 
	 * @return the expansion of the method.
	 */
	public Exp getExpansion() {
		return this.expansion;
	}

	/**
	 * Returns the constraints of the method.
	 * 
	 * @return the constraints of the method.
	 */
	public Exp getConstraints() {
		return this.constraints;
	}

	/**
	 * Normalizes the methods.
	 * 
	 * @see pddl4j.parser.Exp#renameVariables()
	 * @see pddl4j.parser.Exp#moveNegationInward()
	 */
	public void normalize() {
		this.normalize(0);
	}

	/**
	 * Normalizes the methods.
	 * 
	 * @param i the index of the first variable, i.e., ?Xi.
	 * @see pddl4j.parser.Exp#renameVariables()
	 * @see pddl4j.parser.Exp#moveNegationInward()
	 */
	private void normalize(int i) {
		// Rename the parameters
		final Map<String, String> varCtx = new LinkedHashMap<String, String>();
		final List<TypedSymbol> parameters = this.getParameters();
		for (int j = 0; j < parameters.size(); j++) {
			final TypedSymbol parameter = parameters.get(j);
			final String image = parameter.renameVariables(i);
			varCtx.put(image, parameter.getImage());
			i++;
		}
		final List<TypedSymbol> undeclaredParameters = this.getUndeclaredParameters();
		for (int j = 0; j < undeclaredParameters.size(); j++) {
			final TypedSymbol undeclaredParameter = undeclaredParameters.get(j);
			final String image = undeclaredParameter.renameVariables(i);
			varCtx.put(image, undeclaredParameter.getImage());
			i++;
		}
		// Rename the expansion
		this.getExpansion().renameVariables(varCtx);
		// Rename the constraints
		this.getConstraints().renameVariables(varCtx);
		this.getConstraints().moveNegationInward();
	}

	/**
	 * Return if this method is equals to another object.
	 * 
	 * @param object the other object.
	 * @return <code>true</code> if <code>object</code> is not <code>null</code>,
	 *         is an instance of the class <code>Method</code>, and has the same
	 *         name and the same type of parameter; otherwise it returns
	 *         <code>false</code>.
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object object) {
		if (object != null && object instanceof Op) {
			final Method other = (Method) object;
			return this.name.equals(other.name) && this.parameters.equals(other.parameters);
		}
		return false;
	}

	/**
	 * Returns the hash code value of the operator.
	 * 
	 * @return the hash code value of the operator.
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return this.name.hashCode() + this.parameters.hashCode();
	}

	/**
	 * Returns a PDDL string representation of the method.
	 * 
	 * @return a string PDDL representation of the method.
	 */
	@Override
	public String toString() {
		final StringBuffer str = new StringBuffer();
		str.append("(:method " + this.name.toString() + "\n");
		str.append(":parameters (");
		for (int i = 0; i < this.parameters.size() - 1; i++) {
			str.append(this.parameters.get(i) + " ");
		}
		if (!this.parameters.isEmpty()) {
			str.append(this.parameters.get(this.parameters.size() - 1).toString());
		}
		str.append("\n:Undeclared parameters (");
		for (int i = 0; i < this.undeclaredParameters.size() - 1; i++) {
			str.append(this.undeclaredParameters.get(i) + " ");
		}
		if (!this.undeclaredParameters.isEmpty()) {
			str.append(this.undeclaredParameters.get(this.undeclaredParameters.size() - 1).toString());
		}
		str.append(")");
		str.append("\n:expansion ");
		str.append("\n  " + this.expansion.toString());
		str.append("\n:constraints ");
		str.append("\n  " + this.constraints.toString());
		str.append(")");
		return str.toString();
	}
}
