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
 * The <code>ConjunctionList</code> class implements a list of conjuncts. The
 * class can represent a structure of the form: (C1 C2 C3 ... Cn) where each Ci
 * is a conjunct.
 *
 * @author Pellier Damien
 * @version 1.0, 14/01/03
 */
public final class ConjunctionList extends ArrayList<Conjunction>
        implements Cloneable, Standardizable {

    /**
     * <code>serialVersionUID</code>.
     */
    private static final long serialVersionUID = 3257564027298328884L;

    /**
     * Create an empty <tt>ConjunctionList</tt>.
     */
    public ConjunctionList() {
        super();
    }

    /**
     * Return a deep copy of this <tt>ConjunctionList</tt>.
     *
     * @return a deep copy of this <tt>ConjunctionList</tt>.
     */
    public Object clone() {
        final ConjunctionList copy = new ConjunctionList();
        for (Conjunction c : this) {
            copy.add((Conjunction) c.clone());
        }
        return copy;
    }

    /**
     * Standardize this variable name contained in this
     * <tt>ConjunctionList</tt>.
     *
     * @return a standardized copy of this <tt>ConjunctionList</tt>.
     */
    public Object standardize() {
        final ConjunctionList copy = new ConjunctionList();
        for (Conjunction c : this) {
            copy.add((Conjunction) c.standardize());
        }
        return copy;
    }

    /**
     * Returns a <tt>String</tt> representation of this
     * <tt>ConjunctionList</tt>.
     *
     * @return a <tt>String</tt> representation of this
     *         <tt>ConjunctionList</tt>.
     */
    public String toString() {
        final StringBuilder str = new StringBuilder();
        for (Conjunction c : this) {
            str.append("\n");
            str.append(c.getName());
            str.append("\n");
            str.append(c.toString());
        }
        return str.toString();
    }

}
