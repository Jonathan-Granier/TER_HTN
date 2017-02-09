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
 * The <code>Method</code> class implements a method. The class represents a
 * method defined in the planning domain. A method have the form: (:method h C1
 * T1 C2 T2 ... Cn Tn).
 *
 * @author Pellier Damien
 * @version 1.0, 14/01/03
 */
public final class Method implements Cloneable, Standardizable {

    /**
     * The head of this <tt>Method</tt>.
     */
    private TaskAtom head;

    /**
     * The <tt>DecompositionList</tt> of this <tt>Method</tt>.
     */
    private DecompositionList tail;

    /**
     * Create an empty <tt>Method</tt>.
     */
    public Method() {
        super();
    }

    /**
     * Sets the head of this <tt>Method</tt>.
     *
     * @param h the head of this <tt>Method</tt>.
     */
    public void setHead(final TaskAtom h) {
        assert h != null : "parameter <h> == null";
        this.head = h;
    }

    /**
     * Returns the head of this <tt>Method</tt>.
     *
     * @return the head of this <tt>Method</tt>.
     */
    public TaskAtom getHead() {
        return this.head;
    }

    /**
     * Sets the tail of this <tt>Method</tt>.
     *
     * @param t the <tt>DecompositionList</tt> of this <tt>Method</tt>.
     */
    public void setTail(final DecompositionList t) {
        assert t != null : "parameter <t> == null";
        this.tail = t;
    }

    /**
     * Returns the tail of this <tt>Method</tt>.
     *
     * @return the tail of this <tt>Method</tt>.
     */
    public DecompositionList getTail() {
        return this.tail;
    }

    /**
     * Sets the name of this <tt>Method</tt>.
     *
     * @param n the name of this <tt>Method</tt>.
     */
    public void setName(final String n) {
        assert n != null : "parameter <n> == null";
        for (int i = 0; i < tail.size(); i++) {
            final Decomposition decomposition = tail.get(i);
            decomposition.setName(n + decomposition.getName());
        }
    }

    /**
     * Returns a deep copy of this <tt>Operator</tt>.
     *
     * @return a deep copy of this <tt>Operator</tt>.
     */
    public Object clone() {
        final Method copy = new Method();
        copy.setHead((TaskAtom) this.getHead().clone());
        copy.setTail((DecompositionList) this.getTail().clone());
        return copy;
    }

    /**
     * Standardize the variable name contained in this <tt>Method</tt>.
     *
     * @return a copy of this <tt>Method</tt> standardized.
     * @see core.planner.Standardizable#standardize()
     */
    public Object standardize() {
        final Method copy = new Method();
        copy.setHead((TaskAtom) this.getHead().standardize());
        copy.setTail((DecompositionList) this.getTail().standardize());
        return copy;
    }

    /**
     * Returns if this <tt>Method</tt> is equal to an <tt>Object</tt>.
     * <br>
     * <tt>m1.equals(m2)</tt> is equivalent to
     * <tt>m1.getHead().equals(m2.getHead())</tt>.
     *
     * @param obj the <tt>Object</tt> to be compared.
     * @return <tt>true</tt> the specific <tt>Object</tt> is equal to this
     *         <tt>Method</tt>,<tt>false</tt> otherwise.
     */
    public boolean equals(final Object obj) {
        if (obj instanceof Method) {
            final Method cmp = (Method) obj;
            return cmp.getHead().equals(this.getHead());
        }
        return false;
    }

    /**
     * Returns the hash code value of this <tt>Method</tt>.
     *
     * @return the has code associated to this <tt>Method</tt>.
     */
    public int hashCode() {
        return this.getHead().hashCode();
    }

    /**
     * Returns a <tt>String</tt> represenatation of this <tt>Method</tt>.
     *
     * @return a <tt>String</tt> represenatation of this <tt>Method</tt>.
     */
    public String toString() {
        final StringBuilder str = new StringBuilder();
        str.append("(:Method ");
        str.append(head.toString());
        str.append(tail.toString());
        str.append("\n)\n");
        return str.toString();
    }

}
