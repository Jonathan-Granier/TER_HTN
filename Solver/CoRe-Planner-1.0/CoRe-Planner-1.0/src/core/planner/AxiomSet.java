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
 * The <code>AxiomSet</code> class implements a list of axioms. The class
 * stores all the axioms defined in the planning domain.
 *
 * @author Pellier Damien
 * @version 1.0, 14/01/03
 */
public final class AxiomSet extends LinkedHashSet<Axiom>
        implements Standardizable, Cloneable {

    /**
     * <code>serialVersionUID</code>.
     */
    private static final long serialVersionUID = 3832907637082567993L;

    /**
     * Create an empty <tt>AxiomSet</tt>.
     */
    public AxiomSet() {
        super();
    }

    /**
     * Returns if an <tt>Axiom</tt> with a specific head is contained in this
     * <tt>AxiomSet</tt>.
     *
     * @param h the head of the <tt>Axiom</tt> to be tested.
     * @return <tt>true</tt> if an <tt>Axiom</tt> with a specific head is
     *         contained in this <tt>AxiomSet</tt>,<tt>false</tt>
     *         otherwise.
     */
    public boolean containsAxiomWithHead(final Literal h) {
        return this.getAxiomWithHead(h) == null ? false : true;
    }

    /**
     * Returns the <tt>Axiom</tt> that has a specific head.
     *
     * @param h the head of the <tt>Axiom</tt> to get.
     * @return the <tt>Axiom</tt> that has a specific head <tt>null</tt> if
     *         no <tt>Axiom</tt> has this head.
     */
    public Axiom getAxiomWithHead(final Literal h) {
        assert h != null : "Assertion: parameter <h> == null";
        final Iterator<Axiom> i = this.iterator();
        while (i.hasNext()) {
            final Axiom a = i.next();
            if (a.getHead().equals(h)) {
                return a;
            }
        }
        return null;
    }

    /**
     * Return a deep copy of this <tt>AxiomSet</tt>.
     *
     * @return a deep copy of this <tt>AxiomSet</tt>.
     */
    public Object clone() {
        final AxiomSet copy = new AxiomSet();
        for (Axiom a : this) {
            copy.add((Axiom) a.clone());
        }
        return copy;
    }

    /**
     * Standardize this variable name contained in this <tt>AxiomSet</tt>.
     *
     * @return a standardized copy of this <tt>AxiomSet</tt>.
     */
    public Object standardize() {
        final AxiomSet copy = new AxiomSet();
        for (Axiom a : this) {
            copy.add((Axiom) a.standardize());
        }
        return copy;
    }

    /**
     * Returns a <tt>String</tt> representation of this <tt>AxiomSet</tt>.
     *
     * @return a <tt>String</tt> representation of this <tt>AxiomSet</tt>.
     */
    public String toString() {
        final StringBuilder str = new StringBuilder();
        for (Axiom a : this) {
            str.append(a.toString());
        }
        return str.toString();
    }

}
