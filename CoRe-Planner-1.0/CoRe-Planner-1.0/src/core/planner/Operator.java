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
 * The <code>Operator</code> class implements an operator. This class stores
 * the preconditions the delete effects and the add effects.
 *
 * @author Pellier Damien
 * @version 1.0, 14/01/03
 */
public final class Operator implements Standardizable, Cloneable {

    /**
     * The head of this <tt>Operator</tt>.
     */
    private TaskAtom head;

    /**
     * The precondition of this <tt>Operator</tt>.
     */
    private Conjunction preconditions;

    /**
     * The delete effects of this <tt>Operator</tt>.
     */
    private Conjunction delEffects;

    /**
     * The add effect of this <tt>Operator</tt>.
     */
    private Conjunction addEffects;

    /**
     * The cost of this <tt>Operator</tt>.
     */
    private double cost;

    /**
     * Create an empty <tt>Operator</tt>.
     */
    public Operator() {
        super();
    }

    /**
     * Sets the head of this <tt>Operator</tt>.
     *
     * @param h the head of this <tt>Operator</tt>.
     */
    public void setHead(final TaskAtom h) {
        assert h != null : "Assertion: parameter <h> == null";
        this.head = h;
    }

    /**
     * Returns the head of this <tt>Operator</tt>.
     *
     * @return the head of this <tt>Operator</tt>.
     */
    public TaskAtom getHead() {
        return this.head;
    }

    /**
     * Sets the preconditions of this <tt>Operator</tt>.
     *
     * @param precond the preconditions of this <tt>Operator</tt>.
     */
    public void setPreconditions(final Conjunction precond) {
        assert precond != null : "Assertion: parameter <precond> == null";
        this.preconditions = precond;
    }

    /**
     * Returns the preconditions of this <tt>Operator</tt>.
     *
     * @return the preconditions of this <tt>Operator</tt>.
     */
    public Conjunction getPreconditions() {
        return this.preconditions;
    }

    /**
     * Sets the delele effects of this <tt>Operator</tt>.
     *
     * @param del the delete effects of this <tt>Operator</tt>.
     */
    public void setDelEffects(final Conjunction del) {
        assert del != null : "Assertion: parameter <del> == null";
        this.delEffects = del;
    }

    /**
     * Returns the delete effects of this <tt>Operator</tt>.
     *
     * @return the delele effects of this <tt>Operator</tt>.
     */
    public Conjunction getDelEffect() {
        return this.delEffects;
    }

    /**
     * Sets the add effects of this <tt>Operator</tt>.
     *
     * @param add the add effects of this <tt>Operator</tt>.
     */
    public void setAddEffects(final Conjunction add) {
        assert add != null : "Assertion: parameter <add> == null";
        this.addEffects = add;
    }

    /**
     * Returns the add effects of this <tt>Operator</tt>.
     *
     * @return the add effects of this <tt>Operator</tt>.
     */
    public Conjunction getAddEffect() {
        return this.addEffects;
    }

    /**
     * Sets the cost of this <tt>Operator</tt>.
     *
     * @param c the cost of this <tt>Operator</tt>.
     */
    public void setCost(final double c) {
        this.cost = c;
    }

    /**
     * Returns the cost of this <tt>Operator</tt>.
     *
     * @return the cost of this <tt>Operator</tt>.
     */
    public double getCost() {
        return this.cost;
    }

    /**
     * Returns a deep copy of this <tt>Operator</tt>.
     *
     * @return a deep copy of this <tt>Operator</tt>.
     */
    public Object clone() {
        final Operator copy = new Operator();
        copy.setHead((TaskAtom) this.getHead().clone());
        copy.setPreconditions((Conjunction) this.getPreconditions().clone());
        copy.setDelEffects((Conjunction) this.getDelEffect().clone());
        copy.setAddEffects((Conjunction) this.getAddEffect().clone());
        return copy;
    }

    /**
     * Standardize the variable name contained in this <tt>Operator</tt>.
     *
     * @return a copy of this <tt>Operator</tt> standardized.
     * @see core.planner.Standardizable#standardize()
     */
    public Object standardize() {
        final Operator copy = new Operator();
        copy.setHead((TaskAtom) this.getHead().standardize());
        copy.setPreconditions(
                (Conjunction) this.getPreconditions().standardize());
        copy.setDelEffects((Conjunction) this.getDelEffect().standardize());
        copy.setAddEffects((Conjunction) this.getAddEffect().standardize());
        copy.setCost(this.getCost());
        return copy;
    }

    /**
     * Returns if this <tt>Operator</tt> is equal to an <tt>Object</tt>.
     * <br>
     * <tt>o1.equals(o2)</tt> is equivalent to
     * <tt>o1.getHead().equals(o2.getHead())</tt>.
     *
     * @param obj the <tt>Object</tt> to be compared.
     * @return <tt>true</tt> the specific <tt>Object</tt> is equal to this
     *         <tt>Operator</tt>,<tt>false</tt> otherwise.
     */
    public boolean equals(final Object obj) {
        if (obj instanceof Operator) {
            final Operator cmp = (Operator) obj;
            return cmp.getHead().equals(this.getHead());
        }
        return false;
    }

    /**
     * Returns the hash code value of this <tt>Operator</tt>.
     *
     * @return the has code associated to this <tt>Operator</tt>.
     */
    public int hashCode() {
        return this.getHead().hashCode();
    }

    /**
     * Returns a <tt>String</tt> representation of this <tt>Operator</tt>.
     *
     * @return a <tt>String</tt> representation of this <tt>Operator</tt>.
     */
    public String toString() {
        final StringBuilder str = new StringBuilder();
        str.append("(:Operator ");
        str.append(head.toString());
        str.append("\n");
        str.append(preconditions.toString());
        str.append("\n-: ");
        str.append(delEffects.toString());
        str.append("\n+: ");
        str.append(addEffects.toString());
        str.append("\n");
        str.append(cost);
        str.append(" \n)");
        return str.toString();
    }

}
