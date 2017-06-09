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

package pddl4j.preprocessing;

import java.util.Arrays;

import pddl4j.parser.Connective;
import pddl4j.util.AbstractCodedOp;
import pddl4j.util.IntExp;

/**
 * This class implements an method. This class is used to store compact representation of method
 * during the instantiation process.
 * 
 * @author D. Pellier
 * @version 1.0 - 20.03.2015 
 */
final class IntMeth extends AbstractCodedOp {

	/**
	 * The serial version id of the class.
	 */
	private static final long serialVersionUID = 1836381612155894522L;

	/**
	 * The expression that represents the expansion of the method.
	 */
	private IntExp expansion;

	/**
	 * The expression that represents the constraints of the method.
	 */
	private IntExp constraints;
	
	/**
	 * The list of undeclared parameters of the method. The integer value correspond to the type of the
	 * parameters.
	 */
	private int[] undeclaredParameters;
	
	/**
	 * The values that represents the instantiated undeclared parameters of the operator.
	 */
	private int[] undeclaredParametersInstantiations;

	/**
	 * Create a new method from a specified method. This constructor create a deep copy of the
	 * specified method.
	 * 
	 * @param other the other method.
	 * @throws NullPointerException if other == null.
	 */
	public IntMeth(final IntMeth other) {
		super(other.getName(), 
				Arrays.copyOf(other.parameters, other.getArity()), 
				Arrays.copyOf(other.instantiations, other.getArity()));		
		this.setDummy(other.isDummy());
		this.expansion = new IntExp(other.getExpansion());
		this.constraints = new IntExp(other.getConstraints());
		for(int i=0; i<other.getParameters().length; i++){
			this.undeclaredParameters[i] = other.getUndeclaredParameters()[i];
		}
		for(int i=0; i<other.getUndeclaredParametersInstantiations().length; i++){
			this.undeclaredParametersInstantiations[i] = other.getUndeclaredParametersInstantiations()[i];
		}
	}
	
	/**
	 * Create a new method with a specified name.
	 * 
	 * @param name the name of the method.
	 * @param arity the arity of the method.
	 * @throws NullPointerException if name == null.
	 * @throws IllegalArgumentException if arity < 0.
	 */
	public IntMeth(final String name, final int arity) throws IllegalArgumentException,
			NullPointerException {
		super(name, arity);
		if (arity < 0)
			throw new IllegalArgumentException("arity < 0");
		this.parameters = new int[arity];
		Arrays.fill(this.parameters, -1);
		this.instantiations = new int[arity];
		Arrays.fill(this.instantiations, -1);
		this.undeclaredParameters = new int[0];
		this.undeclaredParametersInstantiations = new int[0];
		this.expansion = new IntExp(Connective.EXPANSION);
		this.constraints = new IntExp(Connective.OR);
	}
	
	/**
	 * Create a new method with a specified name.
	 * 
	 * @param name the name of the method.
	 * @param arity the arity of the method.
	 * @param undeclaredLength the number of undeclared parameters in the method
	 * @throws NullPointerException if name == null.
	 * @throws IllegalArgumentException if arity < 0 or undeclaredLegth < 0.
	 */
	public IntMeth(final String name, final int arity, final int undeclaredLength) throws IllegalArgumentException,
			NullPointerException {
		super(name, arity);
		if (arity < 0 || undeclaredLength < 0)
			throw new IllegalArgumentException("arity < 0 or undeclaredLength < 0");
		this.parameters = new int[arity];
		Arrays.fill(this.parameters, -1);
		this.instantiations = new int[arity];
		Arrays.fill(this.instantiations, -1);
		this.undeclaredParameters = new int[undeclaredLength];
		Arrays.fill(this.undeclaredParameters, -1);
		this.undeclaredParametersInstantiations = new int[undeclaredLength];
		Arrays.fill(this.undeclaredParametersInstantiations, -1);
		this.expansion = new IntExp(Connective.EXPANSION);
		this.constraints = new IntExp(Connective.OR);
	}
	
	/**
	 * Return if the operator is ready instantiate with the specified value.
	 * 
	 * @param value the value. 	 
	 * @return <code>true</code> if the operator is ready instantiate with the specified value;
	 * 			 <code>false</code> otherwise. 
	 */
	public final boolean isAlreadyInstantiatedWith(final int value) {
		int i = 0;
		boolean instantiated = false;
		while (i < this.instantiations.length && !instantiated) {
			if (this.instantiations[i] == value)
				instantiated = true;
			i++;
		}
		i = 0;
		while (i < this.undeclaredParametersInstantiations.length && !instantiated) {
			if (this.undeclaredParametersInstantiations[i] == value)
				instantiated = true;
			i++;
		}
		return instantiated;
	}

	/**
	 * Return the expansion of the method.
	 * 
	 * @return the expansion of the method.
	 */
	public final IntExp getExpansion() {
		return this.expansion;
	}

	/**
	 * Set the expansion of the method.
	 * 
	 * @param expansion the expansion to set.
	 * @throws NullPointerException if expansion == null.
	 */
	public final void setExpansion(final IntExp expansion) {
		if (expansion == null) 
			throw new NullPointerException("expansion == null");
		this.expansion = expansion;
	}

	/**
	 * Return the constraints of the method.
	 * 
	 * @return the constraints of the method.
	 */
	public final IntExp getConstraints() {
		return this.constraints;
	}

	/**
	 * Set the new constraints of the method.
	 * 
	 * @param constraints the constraints to set
	 * @throws NullPointerException if constraints == null.
	 */
	public final void setConstraints(final IntExp constraints) {
		if (constraints == null) 
			throw new NullPointerException("constraints == null");
		this.constraints = constraints;
	}

	/**
	 * Get the undeclared parameters
	 * @return the undeclaredParameters
	 */
	public int[] getUndeclaredParameters() {
		return undeclaredParameters;
	}
	
	/**
	 * Returns the type of the undeclared parameter at the specified index.
	 * 
	 * @param index the index of the undeclared parameter.
	 * @return the type of the undeclared parameter at the specified index.
	 * @throws ArrayIndexOutOfBoundsException if 0 <= index < arity does not hold. 
	 */
	public final int getTypeOfUndeclaredParameters(final int index) throws ArrayIndexOutOfBoundsException {
		return this.undeclaredParameters[index];
	}
	
	/**
	 * Set a new type the undeclared parameter at a specified index.
	 * 
	 * @param index the index of the undeclared parameter.
	 * @param type the type to set.
	 * @throws ArrayIndexOutOfBoundsException if 0 <= index < arity does not hold.
	 * @throws IllegalArgumentException if type < 0.
	 */
	public final void setTypeOfUndeclaredParameter(final int index, final int type)
			throws ArrayIndexOutOfBoundsException, IllegalArgumentException {
		if (type < 0)
			throw new IllegalArgumentException("type < 0");
		this.undeclaredParameters[index] = type;
	}
	
	/**
	 * Returns the value of the undeclared parameter at a specified index.
	 * 
	 * @param index the index.
	 * @return the value of the undeclared parameter.
	 * @throws ArrayIndexOutOfBoundsException if 0 <= index < arity does not hold. 
	 */
	public final int getValueOfUndeclaredParameter(final int index) throws ArrayIndexOutOfBoundsException {
		return this.undeclaredParametersInstantiations[index];
	}
	
	/**
	 * Instantiate an undeclared parameter of the operator at a specified index with a value.
	 * 
	 * <p>
	 * The assumption is made that different operator parameters are instantiated with different
	 * constants, i.e., the planner never generates actions like move(a,a) because we consider this
	 * as a bad domain representation that should be revised. In fact, in operators with identical
	 * constant parameters, all but one of the constants are superfluous and can be skipped from the
	 * representation without loss of information.
	 * </p>
	 * 
	 * @param index the index of the undeclared parameter to instantiate.
	 * @param value the value of instantiation.
	 * @throws ArrayIndexOutOfBoundsException if 0 <= index < arity does not hold.
	 * @throws IllegalArgumentException if value < 0.
	 */
	public final void setValueOfUndeclaredParameter(final int index, final int value)
			throws ArrayIndexOutOfBoundsException, IllegalArgumentException {
		if (value < 0)
			throw new IllegalArgumentException("value < 0");
		this.undeclaredParametersInstantiations[index] = value;
	}

	/**
	 * Set the undeclared parameters
	 * @param undeclaredParameters the undeclaredParameters to set
	 */
	public void setUndeclaredParameters(int[] undeclaredParameters) {
		this.undeclaredParameters = undeclaredParameters;
	}

	/**
	 * @return the undeclaredParametersInstantiations
	 */
	public int[] getUndeclaredParametersInstantiations() {
		return undeclaredParametersInstantiations;
	}

	/**
	 * @param undeclaredParametersInstantiations the undeclaredParametersInstantiations to set
	 */
	public void setUndeclaredParametersInstantiations(
			int[] undeclaredParametersInstantiations) {
		this.undeclaredParametersInstantiations = undeclaredParametersInstantiations;
	}

	/**
	 * Return if the method is equal to another object.
	 * 
	 * @param obj the other object.
	 * @return <code>true</code> if the specified object is an instance of the
	 *         class <code>IntMeth</code> and it has the same name and
	 *         instantiated parameters; otherwise <code>false</code>.
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(final Object obj) {
		if (obj != null && obj instanceof IntOp) {
			final IntMeth other = (IntMeth) obj;
			return this.name.equals(other.name)
					&& this.instantiations.equals(other.instantiations);
		}
		return false;
	}
	
	/**
	 * Return the hash code value of the operator.
	 * 
	 * @return the hash code value of the operator.
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + instantiations.hashCode();
		result = prime * result + this.name.hashCode();
		return result;
	}
	
}
   