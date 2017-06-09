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

/**
 * The <code>Axiom</code> class implements a axiom. The class represents an
 * axiom defined in the planning domain. The expected form is: (:- [n1] C1 [n2]
 * C2 [n3] C3 ... [nn] Cn).
 *
 * @author Pellier Damien
 * @version 1.0, 14/01/03
 */
public final class Axiom implements Standardizable, Cloneable {

    /**
     * The head of this <tt>Axiom</tt>.
     */
    private Literal head;

    /**
     * The tail of this <tt>Axiom</tt>.
     */
    private ConjunctionList tail;

    /**
     * Create an empty <tt>Axiom</tt>.
     */
    public Axiom() {
        this(new Literal(), new ConjunctionList());
    }

    /**
     * Create an <tt>Axiom</tt> with a specific head.
     *
     * @param h the head of the <tt>Atom</tt>.
     */
    public Axiom(final Literal h) {
        this(h, new ConjunctionList());
    }

    /**
     * Create an <tt>Axiom</tt> with a specific head.
     *
     * @param h the head of the <tt>Atom</tt>.
     * @param t the tail of the <tt>Atom</tt>.
     */
    public Axiom(final Literal h, final ConjunctionList t) {
        this.setHead(h);
        this.setTail(t);
    }

    /**
     * Sets the head of this <tt>Axiom</tt>.
     *
     * @param h the tail of this <tt>Axiom</tt>.
     */
    public void setHead(final Literal h) {
        assert h != null : "Assertion: parameter <h> == null";
        this.head = h;
    }

    /**
     * Returns the head of this <tt>Axiom</tt>.
     *
     * @return the head of this <tt>Axiom</tt>.
     */
    public Literal getHead() {
        return this.head;
    }

    /**
     * Sets the tail of this <tt>Axiom</tt>.
     *
     * @param t the tail of this <tt>Axiom</tt>.
     */
    public void setTail(final ConjunctionList t) {
        assert t != null : "Assertion: parameter <t> == null";
        this.tail = t;
    }

    /**
     * Returns the tail of this <tt>Axiom</tt>.
     *
     * @return the tail of this <tt>Axiom</tt>.
     */
    public ConjunctionList getTail() {
        return this.tail;
    }

    /**
     * Sets the name of this <tt>Axiom</tt>.
     *
     * @param n the name of this <tt>Axiom</tt>.
     */
    public void setName(final String n) {
        assert n != null : "Assertion: parameter <n> == null";
        for (int i = 0; i < tail.size(); i++) {
            final Conjunction conjunction = tail.get(i);
            conjunction.setName(n + conjunction.getName());
        }
    }

    /**
     * Returns if this <tt>Axiom</tt> is equal to another <tt>Object</tt>.
     * <br>
     * <tt>a1.equals(a2)</tt> is equivalent to
     * <tt>a1.getHead().equals(a2.getHead())</tt>.
     *
     * @param obj the compared <tt>Object</tt>.
     * @return <tt>true</tt> if <tt>object</tt> is equal to this <tt>Axiom</tt>,
     *         <tt>false</tt> otherwise.
     */
    public boolean equals(final Object obj) {
        if (obj instanceof Axiom) {
            final Axiom cmp = (Axiom) obj;
            return this.head.equals(cmp.getHead());
        }
        return false;
    }

    /**
     * Returns the hash code value of this <tt>Axiom</tt>.
     *
     * @return the hash code value.
     */
    public int hashCode() {
        return head.hashCode();
    }

    /**
     * Return a clone of this <tt>Axiom</tt>.
     *
     * @return a deep copy of this <tt>Axiom</tt>.
     */
    public Object clone() {
        final Axiom copy = new Axiom();
        copy.setHead((Literal) this.getHead().clone());
        copy.setTail((ConjunctionList) this.getTail().clone());
        return copy;
    }

    /**
     * Returns a <tt>String</tt> representation of this <tt>Axiom</tt>.
     *
     * @return a <tt>String</tt> representation of this <tt>Axiom</tt>.
     */
    public String toString() {
        final StringBuilder str = new StringBuilder();
        str.append("(:- ");
        str.append(head.toString());
        str.append(tail.toString());
        str.append("\n)");
        return str.toString();
    }

    /**
     * Standardize the variable name contained in this <tt>Axiom</tt>.
     *
     * @return a standardized copy of this <tt>Axiom</tt>.
     */
    public Object standardize() {
        final Axiom copy = new Axiom();
        copy.setHead((Literal) head.standardize());
        copy.setTail((ConjunctionList) tail.standardize());
        return copy;
    }
}
