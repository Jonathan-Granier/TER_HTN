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

import java.util.Iterator;
import java.util.LinkedHashSet;

/**
 * The <code>OperatorSet</code> class implements a list of operators. This
 * class stores all the operators defined in a planning domain.
 *
 * @author Pellier Damien
 * @version 1.0, 14/01/03
 */
public final class OperatorSet extends LinkedHashSet<Operator>
        implements Standardizable, Cloneable {

    /**
     * <code>serialVersionUID</code>.
     */
    private static final long serialVersionUID = 3256727260311466041L;

    /**
     * Create an empty <tt>OperatorSet</tt>.
     */
    public OperatorSet() {
        super();
    }

    /**
     * Returns if an <tt>Operator</tt> with a specific head is contained in this
     * <tt>OperatorSet</tt>.
     *
     * @param h the head of the <tt>Operator</tt> to be tested.
     * @return <tt>true</tt> if an <tt>Operator</tt> with a specific head is
     *         contained in this <tt>OperatorSet</tt>,<tt>false</tt>
     *         otherwise.
     */
    public boolean containsOperatorWithHead(final TaskAtom h) {
        return this.getOperatorWithHead(h) == null ? false : true;
    }

    /**
     * Returns the <tt>Operator</tt> that has a specific head.
     *
     * @param h the head of the <tt>Operator</tt> to get.
     * @return the <tt>Operator</tt> that has a specific head <tt>null</tt> if
     *         no <tt>Operator</tt> has this head.
     */
    public Operator getOperatorWithHead(final TaskAtom h) {
        assert h != null : "Assertion: parameter <h> == null";
        final Iterator<Operator> i = this.iterator();
        while (i.hasNext()) {
            final Operator o = i.next();
            if (o.getHead().equals(h)) {
                return o;
            }
        }
        return null;
    }

    /**
     * Return a deep copy of this <tt>OperatorSet</tt>.
     *
     * @return a deep copy of this <tt>OperatorSet</tt>.
     */
    public Object clone() {
        final OperatorSet copy = new OperatorSet();
        for (Operator c : this) {
            copy.add((Operator) c.clone());
        }
        return copy;
    }

    /**
     * Standardize this variable name contained in this <tt>OperatorSet</tt>.
     *
     * @return a standardized copy of this <tt>OperatorSet</tt>.
     */
    public Object standardize() {
        final OperatorSet copy = new OperatorSet();
        for (Operator c : this) {
            copy.add((Operator) c.standardize());
        }
        return copy;
    }

    /**
     * Returns a <tt>String</tt> representation of this <tt>OperatorSet</tt>.
     *
     * @return a <tt>String</tt> representation of this <tt>OperatorSet</tt>.
     */
    public String toString() {
        final StringBuilder str = new StringBuilder();
        for (Operator o : this) {
            str.append(o.toString());
            str.append("\n");
        }
        return str.toString();
    }

}
