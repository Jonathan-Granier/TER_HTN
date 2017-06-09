/*
 * Copyright (c) 2010 by Abdeldjalil Ramoul <abdeldjalil.ramoul@imag.fr>.
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
 * This class implements two list of nodes linked to one node
 * 
 * @author Ramoul
 */
public class LinkedNodes {
	
	/**
	 * The list of out nodes
	 */
	private ArrayList<Integer> _outNodes;
	
	/**
	 * The list of in nodes
	 */
	private ArrayList<Integer> _inNodes;
	
	/**
	 * Creates a new LinkedNodes object
	 */
	public LinkedNodes() {
		_outNodes = new ArrayList<Integer>();
		_inNodes = new ArrayList<Integer>();
	}
	
	/**
	 * Creates a new LinkedNodes object from two ArrayLists
	 */
	public LinkedNodes(final ArrayList<Integer> outNodes, final ArrayList<Integer> inNodes) {
		_outNodes = new ArrayList<Integer>(outNodes);
		_inNodes = new ArrayList<Integer>(inNodes);
	}
	
	/**
	 * Creates a new LinkedNodes object from two Integers
	 */
	public LinkedNodes(final int out, final int in) {
		_outNodes = new ArrayList<Integer>();
		if (out >= 0) {
			_outNodes.add(out);
		}
		_inNodes = new ArrayList<Integer>();
		if (in >= 0) {
			_inNodes.add(in);
		}
	}
	
	/**
	 * Adds a new out node to the LinkedNodes if does not already exist
	 * 
	 * @param out The index of the out node
	 * @return <code>True</code> if the node is added and <code>False</code> otherwise
	 */
	public boolean addOutNode(final Integer out) {
		if (!_outNodes.contains(out)) {
			return _outNodes.add(out);
		} else {
			return false;
		}
	}
	
	/**
	 * Adds a new in node to the LinkedNodes if does not already exist
	 * 
	 * @param in The index of the in node
	 * @return <code>True</code> if the node is added and <code>False</code> otherwise
	 */
	public boolean addInNode(final Integer in) {
		if (!_inNodes.contains(in)) {
			return _inNodes.add(in);
		} else {
			return false;
		}
	}
	
	/**
	 * Adds a list of new out nodes to the LinkedNodes
	 * 
	 * @param out The indexes of the out nodes
	 */
	public void addOutNodes(final List<Integer> outs) {
		for (final int out : outs) {
			addOutNode(out);
		}
	}
	
	/**
	 * Adds a list of in nodes to the LinkedNodes
	 * 
	 * @param in The indexes of the in nodes
	 */
	public void addInNodes(final List<Integer> ins) {
		for (final int in : ins) {
			addInNode(in);
		}
	}
	
	/**
	 * Removes an out node from the LinkedNodes
	 * 
	 * @param out The node to remove
	 * @return <code>True</code> if the node was removed and <code>False</code> otherwise
	 */
	public boolean removeOutNode(final int out) {
		return _outNodes.remove((Integer) out);
	}
	
	/**
	 * Removes an in node from the LinkedNodes
	 * 
	 * @param in The node to remove
	 * @return <code>True</code> if the node was removed and <code>False</code> otherwise
	 */
	public boolean removeInNode(final int in) {
		return _inNodes.remove((Integer) in);
	}
	
	/**
	 * @return the _outNodes
	 */
	public ArrayList<Integer> get_outNodes() {
		return _outNodes;
	}
	
	/**
	 * @param _outNodes the _outNodes to set
	 */
	public void set_outNodes(final ArrayList<Integer> _outNodes) {
		this._outNodes = _outNodes;
	}
	
	/**
	 * @return the _inNodes
	 */
	public ArrayList<Integer> get_inNodes() {
		return _inNodes;
	}
	
	/**
	 * @param _inNodes the _inNodes to set
	 */
	public void set_inNodes(final ArrayList<Integer> _inNodes) {
		this._inNodes = _inNodes;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_inNodes == null) ? 0 : _inNodes.hashCode());
		result = prime * result + ((_outNodes == null) ? 0 : _outNodes.hashCode());
		return result;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
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
		final LinkedNodes other = (LinkedNodes) obj;
		if (_inNodes == null) {
			if (other._inNodes != null) {
				return false;
			}
		} else if (!_inNodes.equals(other._inNodes)) {
			return false;
		}
		if (_outNodes == null) {
			if (other._outNodes != null) {
				return false;
			}
		} else if (!_outNodes.equals(other._outNodes)) {
			return false;
		}
		return true;
	}
	
	/**
	 * Represents a linked nodes in a string form
	 * 
	 * @return A String representation of the linked nodes
	 */
	@Override
	public String toString() {
		final StringBuffer str = new StringBuffer();
		str.append("OutNodes: " + _outNodes + " InNodes: " + _inNodes);
		return str.toString();
	}
	
}
