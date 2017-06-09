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

import java.util.ArrayList;
import java.util.List;

/**
 * This class implements a compact representation for method based on <code>BitSet</code> structure.
 * 
 * @author D. Pellier
 * @version 1.0 - 26.03.2015
 */
public class OrderedMethod
extends AbstractCodedOp {
	
	/**
	 * The positive preconditions of the method.
	 */
	private BitVector _positivePreconditions;
	
	/**
	 * The negative preconditions of the method.
	 */
	private BitVector _negativePreconditions;
	
	/**
	 * The expansion of the method.
	 */
	private List<Integer> _expansion;
	
	/**
	 * Creates a new method from an other. This constructor is the copy constructor.
	 * 
	 * @param other the other method.
	 * @throws NullPointerException if <code>other == null</code>.
	 */
	public OrderedMethod(final OrderedMethod other) {
		super(other);
		_positivePreconditions = new BitVector(other._positivePreconditions);
		_negativePreconditions = new BitVector(other._negativePreconditions);
		_expansion = new ArrayList<>(other._expansion);
	}
	
	/**
	 * Creates a new method.
	 * 
	 * @param name the name of the method.
	 * @param arity the arity of the method.
	 */
	public OrderedMethod(final String name, final int arity) {
		super(name, arity);
		_positivePreconditions = new BitVector();
		_negativePreconditions = new BitVector();
		_expansion = new ArrayList<>();
	}
	
	/**
	 * Creates a new method.
	 * 
	 * @param name the name of the method.
	 * @param arity the arity of the method.
	 * @param positivePreconditions The positive preconditions of the method
	 * @param negativePreconditions The negative preconditions of the method
	 * @param expansion the taskNetwork of the method.
	 */
	public OrderedMethod(final String name, final int arity, final BitVector positivePreconditions, final BitVector negativePreconditions, final List<Integer> expansion) {
		this(name, arity);
		_positivePreconditions = new BitVector(positivePreconditions);
		_negativePreconditions = new BitVector(negativePreconditions);
		_expansion = new ArrayList<>(expansion);
	}
	
	public BitVector getPositivePreconditions() {
		return _positivePreconditions;
	}
	
	public BitVector getNegativePreconditions() {
		return _negativePreconditions;
	}
	
	public void setPositivePreconditions(final BitVector preconditions) {
		_positivePreconditions = preconditions;
	}
	
	public void setNegativePreconditions(final BitVector preconditions) {
		_negativePreconditions = preconditions;
	}
	
	public List<Integer> getExpansion() {
		return _expansion;
	}
	
	public void setExpansion(final List<Integer> expansion) {
		_expansion = expansion;
	}
	
}
