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

import java.util.ArrayList;

/**
 * The <code>TaskList</code> class implements a list of <tt>TaskAtom</tt>s.
 *
 * @author Pellier Damien
 * @version 1.0, 14/01/03
 */
public final class TaskList extends ArrayList<TaskAtom>
        implements Standardizable, Substituable {

    /**
     * <code>serialVersionUID</code>.
     */
    private static final long serialVersionUID = 3617014152397470000L;

    /**
     * Create an empty <tt>TaskList</tt>.
     */
    public TaskList() {
        super();
    }

    /**
     * Returns a new <tt>TaskList</tt> such that the variables
     * appearing in the <tt>Substitution</tt> are replaced with the
     * corresponding <tt>Term</tt> s of the <tt>Substitution</tt>.
     *
     * @param alpha the <tt>Substitution</tt> to apply.
     * @return a substituted copy of the <tt>TaskList</tt>.
     * @exception SubstitutionException if an error occur during substitution.
     */
    public Object applySubstitution(final Substitution alpha)
            throws SubstitutionException {
        return this.applySubstitution(alpha, false);
    }

    /**
     * Returns a new <tt>TaskList</tt> such that the variables
     * appearing in the <tt>Substitution</tt> are replaced with the
     * corresponding <tt>Term</tt> of the <tt>Susbstitution</tt>.
     *
     * @param alpha the <tt>Substitution</tt> to apply.
     * @param replaceVar flag to indicate if the <tt>Substitution</tt> allows
     *        to instantiate <tt>Variable</tt> s not in the
     *        <tt>Substitution</tt> by a <tt>Constant</tt> with the
     *        <tt>Variable</tt> name.
     * @return a substituted copy of the <tt>Literal</tt>.
     * @exception SubstitutionException if an error occur during substitution.
     */
    public Object applySubstitution(final Substitution alpha,
            final boolean replaceVar) throws SubstitutionException {
        assert alpha != null : "parameter <alpha> == null";
        final TaskList stal = new TaskList();
        for (TaskAtom ta : this) {
            final TaskAtom sta =
                (TaskAtom) ta.applySubstitution(alpha, replaceVar);
            stal.add(sta);
        }
        return stal;
    }

    /**
     * Returns a depp copy of this <tt>TaskList</tt>.
     *
     * @return a deep copy of this <tt>TaskList</tt>.
     */
    public Object clone() {
        final TaskList copy = new TaskList();
        for (TaskAtom t : this) {
            copy.add((TaskAtom) t.clone());
        }
        return copy;
    }

    /**
     * Standardize the variable name contained in this <tt>TaskList</tt>.
     *
     * @return a standardized copy of this <tt>TaskList</tt>.
     * @see core.planner.Standardizable#standardize()
     */
    public Object standardize() {
        final TaskList copy = new TaskList();
        for (TaskAtom t : this) {
            copy.add((TaskAtom) t.standardize());
        }
        return copy;
    }

    /**
     * Returns a <tt>String</tt> representation of this <tt>TaskList</tt>.
     *
     * @return a <tt>String</tt> representation of this <tt>TaskList</tt>.
     */
    public String toString() {
        final StringBuilder str = new StringBuilder();
        str.append("(");
        for (TaskAtom t : this) {
            str.append(t.toString());
        }
        str.append(")");
        return str.toString();
    }

}