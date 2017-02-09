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
 * The <code>MethodSet</code> class implements a list of methods. This class
 * stores all the methods defined in a planning domain.
 *
 * @author Pellier Damien
 * @version 1.0, 14/01/03
 */
public final class MethodSet extends LinkedHashSet<Method>
        implements Standardizable, Cloneable {

    /**
     * <code>serialVersionUID</code>.
     */
    private static final long serialVersionUID = 3256727260311466041L;

    /**
     * Create an empty <tt>MethodSet</tt>.
     */
    public MethodSet() {
        super();
    }

    /**
     * Returns if an <tt>Method</tt> with a specific head is contained in this
     * <tt>MethodSet</tt>.
     *
     * @param h the head of the <tt>Method</tt> to be tested.
     * @return <tt>true</tt> if an <tt>Method</tt> with a specific head is
     *         contained in this <tt>MethodSet</tt>,<tt>false</tt>
     *         otherwise.
     */
    public boolean containsMethodWithHead(final TaskAtom h) {
        return this.getMethodWithHead(h) == null ? false : true;
    }

    /**
     * Returns the <tt>Method</tt> that has a specific head.
     *
     * @param h the head of the <tt>Method</tt> to get.
     * @return the <tt>Method</tt> that has a specific head <tt>null</tt> if
     *         no <tt>Method</tt> has this head.
     */
    public Method getMethodWithHead(final TaskAtom h) {
        assert h != null : "parameter <h> == null";
        final Iterator<Method> i = this.iterator();
        while (i.hasNext()) {
            final Method m = i.next();
            if (m.getHead().equals(h)) {
                return m;
            }
        }
        return null;
    }

    /**
     * Return a deep copy of this <tt>MethodSet</tt>.
     *
     * @return a deep copy of this <tt>MethodSet</tt>.
     */
    public Object clone() {
        final MethodSet copy = new MethodSet();
        for (Method m : this) {
            copy.add((Method) m.clone());
        }
        return copy;
    }

    /**
     * Standardize this variable name contained in this <tt>MethodSet</tt>.
     *
     * @return a standardized copy of this <tt>MethodSet</tt>.
     */
    public Object standardize() {
        final MethodSet copy = new MethodSet();
        for (Method m : this) {
            copy.add((Method) m.standardize());
        }
        return copy;
    }

    /**
     * Returns a <tt>String</tt> representation of this <tt>MethodSet</tt>.
     *
     * @return a <tt>String</tt> representation of this <tt>MethodSet</tt>.
     */
    public String toString() {
        final StringBuilder str = new StringBuilder();
        for (Method m : this) {
            str.append(m.toString());
        }
        return str.toString();
    }

}
