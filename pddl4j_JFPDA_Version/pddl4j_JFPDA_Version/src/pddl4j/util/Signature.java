
/*
 * Copyright (c) 2016 by Abdeldjalil Ramoul <abdeldjalil.ramoul@imag.fr>.
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

import java.util.Arrays;
import java.util.List;

/**
 * This class implements a task signature which contain the task name and the arguments.
 * 
 * @author A. Ramoul
 * @version 1.0 - 13.04.2016
 */
public class Signature {
	
	/**
	 * The name of the task.
	 */
	protected String _name;
	
	/**
	 * The list of parameters of the task. The integer value correspond to the index of the parameters.
	 */
	protected int[] _parameters;
	
	/**
	 * Create a new expression from an other one. This constructor make a deep copy of the specified expression.
	 * 
	 * @param name the name of the task.
	 * @param parameters the list of task parameters indexes
	 */
	public Signature(final String name, final int[] parameters) {
		_name = name;
		_parameters = Arrays.copyOf(parameters, parameters.length);
	}
	
	/**
	 * Create a new expression from an other one. This constructor make a deep copy of the specified expression.
	 * 
	 * @param name the name of the task.
	 * @param parameter the task parameter index
	 */
	public Signature(final String name, final int parameter) {
		_name = name;
		_parameters = new int[] {
			parameter
		};
	}
	
	public String getName() {
		return _name;
	}
	
	public void setName(final String name) {
		_name = name;
	}
	
	public int[] getParameters() {
		return _parameters;
	}
	
	public void setParameters(final int[] parameters) {
		_parameters = parameters;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_name == null) ? 0 : _name.hashCode());
		result = prime * result + Arrays.hashCode(_parameters);
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
		final Signature other = (Signature) obj;
		if (_name == null) {
			if (other._name != null) {
				return false;
			}
		} else if (!_name.equals(other._name)) {
			return false;
		}
		if (!Arrays.equals(_parameters, other._parameters)) {
			return false;
		}
		return true;
	}
	
	public String toString(final List<String> constants) {
		final StringBuffer str = new StringBuffer();
		for (int i = 0; i < _parameters.length; i++) {
			str.append(" " + constants.get(_parameters[i]));
		}
		return "(" + _name + " " + str + ")";
	}
	
}
