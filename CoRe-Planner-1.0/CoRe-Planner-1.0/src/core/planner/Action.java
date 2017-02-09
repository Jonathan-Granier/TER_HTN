/*
 * Copyright MAGMA TEAM Laboratory Leibniz.
 *
 * This software is a computer program whose purpose is to propose an
 * environment for multi-agent planning.
 *
 * This software is governed by the CeCILL  license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 *
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability.
 *
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or
 * data to be ensured and,  more generally, to use and operate it in the
 * same conditions as regards security.
 *
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL license and that you accept its terms.
 */

package core.planner;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * The <code>Action</code> class implements an action. An action is a couple
 * of the form: (A*,task) where A is a conjunction of assumptions and task a
 * taskAtom.
 *
 * @author Pellier Damien
 * @version 1.0, 14/01/03
 */
public final class Action {

    /**
     * The TaskAtom representing the task of this <tt>Action</tt>.
     */
    private TaskAtom task;

    /**
     * The assumption list associated to this <tt>Action</tt>.
     */
    private Set<Assumption> assumptions;

    /**
     * Creates an <tt>Action</tt> with for a specific task.
     *
     * @param t the <tt>TaskAtom</tt> representing this <tt>Action</tt>.
     * @param ac the collection of assumptions to do this <tt>Action</tt>.
     */
    public Action(final TaskAtom t, final Collection <Assumption> ac) {
        this.setTask(t);
        this.assumptions = new LinkedHashSet<Assumption>();
        this.addAssumptions(ac);
    }

    /**
     * Creates a <tt>Action</tt> with for a specific task.
     *
     * @param t the <tt>taskAtom</tt> representing this <tt>Action</tt>.
     */
    public Action(final TaskAtom t) {
        this(t, new LinkedHashSet<Assumption>());
    }

    /**
     * Sets the task of this <tt>Action</tt>.
     *
     * @param t the <tt>TaskAtom</tt> representing the task of this
     *        <tt>Action</tt>.
     */
    public void setTask(final TaskAtom t) {
        assert t != null : "Assertion: parameter <t> == null";
        this.task = t;
    }

    /**
     * Returns the task of this <tt>Action</tt>.
     *
     * @return the task of this <tt>Action</tt>.
     */
    public TaskAtom getTask() {
        return this.task;
    }

    /**
     * Adds an <tt>Assumption</tt> to this <tt>Action</tt>.
     *
     * @param a the <tt>Assumption</tt> to add.
     */
    public void addAssumption(final Assumption a) {
        assert a != null : "Assertion: parameter <a> == null";
        this.assumptions.add(a);
    }

    /**
     * Adds a collection of assumptions to this <tt>Action</tt>.
     *
     * @param ac the collection of assumptions to add.
     */
    public void addAssumptions(final Collection<Assumption> ac) {
        assert ac != null : "Assertion: parameter <ac> == null";
        this.assumptions.addAll(ac);
    }

    /**
     * Returns the set of assumptions done by this <tt>Action</tt>.
     *
     * @return the set of assumptions done by this <tt>Action</tt>.
     */
    public Set<Assumption> getAssumptions() {
        return this.assumptions;
    }

    /**
     * Returns if a specific <tt>Assumption</tt> is contained in this
     * <tt>Action</tt>.
     *
     * @param a the <tt>Assumption</tt> to be tested.
     * @return <tt>true</tt> if a specific <tt>Assumption</tt> is contained
     *         in this <tt>Action</tt> <tt>false</tt> otherwise.
     */
    public boolean makesAssumption(final Assumption a) {
        assert a != null : "Assertion: parameter <a> == null";
        return this.assumptions.contains(a);
    }

    /**
     * Returns the number of assumptions made by this <tt>Action</tt>.
     *
     * @return the number of assumptions made by this <tt>Action</tt>.
     */
    public int numberOfAssumptions() {
        return this.assumptions.size();
    }

    /**
     * Returns if this <tt>Action</tt> makes assumptions.
     *
     * @return <tt>true</tt> if this <tt>Action</tt> makes assumptions,
     *         <tt>false</tt> otherwise.
     */
    public boolean isHypothetical() {
        return !this.assumptions.isEmpty();
    }

    /**
     * Returns if a specific <tt>Object</tt> is equal to this <tt>Action</tt>.
     *
     * @param obj the <tt>Object</tt> to be compared.
     * @return <tt>true</tt> if <tt>object</tt> is equal to this
     *         <tt>Action</tt>,<tt>false</tt> otherwise.
     */
    public boolean equals(final Object obj) {
        if (obj instanceof Action) {
            final Action action = (Action) obj;
            return this.getTask().equals(action.getTask())
                && this.assumptions.equals(action.assumptions);
        }
        return false;
    }

    /**
     * Returns the hash code value of this <tt>Action</tt>.
     *
     * @return the has code associated to this <tt>Action</tt>.
     */
    public int hashCode() {
        return this.getTask().hashCode() + this.assumptions.hashCode();
    }

    /**
     * Returns a <tt>String</tt> representation of this <tt>Action</tt>.
     *
     * @return a <tt>String</tt> representation of this <tt>Action</tt>.
     */
    public String toString() {
        final StringBuilder str = new StringBuilder("[ ");
        if (assumptions.isEmpty()) {
            str.append("()");
        } else {
            for (Assumption a : this.assumptions) {
                str.append(a.toString());
            }
        }
        str.append(" , ");
        str.append(task);
        str.append(" ]");
        return str.toString();
    }

}
