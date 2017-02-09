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
 * The <code>Domain</code> class implements a planning domain. This class
 * stores the domain definition that consists of a set of methods, operators
 * and axioms.
 *
 * @author Pellier Damien
 * @version 1.0, 14/01/03
 */
public final class Domain {

    /**
     * The name of the planning <tt>Domain</tt>.
     */
    private String name;

    /**
     * The list of <tt>Axiom</tt> s of this planning <tt>Domain</tt>.
     */
    private AxiomSet axioms;

    /**
     * The list of <tt>Operator</tt> s of this planning <tt>Domain</tt>.
     */
    private OperatorSet operators;

    /**
     * The list of <tt>Method</tt> s of this planning <tt>Domain</tt>.
     */
    private MethodSet methods;

    /**
     * Create an empty <tt>Domain</tt>.
     */
    public Domain() {
        super();
    }

    /**
     * Sets the name of this planning <tt>Domain</tt>.
     *
     * @param n the name of this planning <tt>Domain</tt>.
     */
    public void setName(final String n) {
        assert n != null : "Assertion: parameter <n> == null";
        this.name = n;
    }

    /**
     * Returns the name of this planning <tt>Domain</tt>.
     *
     * @return the name of this planning <tt>Domain</tt>.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the <tt>AxiomSet</tt> of this planning <tt>Domain</tt>.
     *
     * @param as the <tt>AxiomSet</tt> of this planning
     *        <tt>Domain</tt>.
     */
    public void setAxioms(final AxiomSet as) {
        assert as != null : "Assertion: parameter <as> == null";
        this.axioms = as;
    }

    /**
     * Returns the <tt>AxiomSet</tt> of this planning <tt>Domain</tt>.
     *
     * @return the <tt>AxiomSet</tt> of this planning <tt>Domain</tt>.
     */
    public AxiomSet getAxioms() {
        return this.axioms;
    }

    /**
     * Sets the <tt>OperatorSet</tt> of this planning <tt>Domain</tt>.
     *
     * @param os the <tt>OperatorSet</tt> of this planning
     *        <tt>Domain</tt>.
     */
    public void setOperators(final OperatorSet os) {
        assert os != null : "Assertion: parameter <os> == null";
        this.operators = os;
    }

    /**
     * Returns the <tt>OperatorSet</tt> of this planning <tt>Domain</tt>.
     *
     * @return the <tt>OperatorSet</tt> of this planning <tt>domain</tt>.
     */
    public OperatorSet getOperators() {
        return this.operators;
    }

    /**
     * Sets the <tt>MethodSet</tt> of this planning <tt>Domain</tt>.
     *
     * @param ms the <tt>MethodSet</tt> of this planning
     *        <tt>Domain</tt>.
     */
    public void setMethods(final MethodSet ms) {
        assert ms != null : "Assertion: parameter <ms> == null";
        this.methods = ms;
    }

    /**
     * Returns the <tt>MethodSet</tt> of this planning <tt>Domain</tt>.
     *
     * @return the <tt>MethodSet</tt> of this planning <tt>Domain</tt>.
     */
    public MethodSet getMethods() {
        return this.methods;
    }

    /**
     * Returns if this <tt>Domain</tt> is equal to an <tt>Object</tt>.
     *
     * @param obj the <tt>Object</tt> to be compared.
     * @return <tt>true</tt> the specific <tt>Object</tt> is equal to this
     *         <tt>Domain</tt>, <tt>false</tt> otherwise.
     */
    public boolean equals(final Object obj) {
        if (obj instanceof Domain) {
            final Domain cmp = (Domain) obj;
            return cmp.getName().equals(this.getName());
        }
        return false;
    }

    /**
     * Returns the hash code value of this <tt>Domain</tt>.
     *
     * @return the hash code value.
     */
    public int hashCode() {
        return this.getName().hashCode();
    }

    /**
     * Returns a <tt>String</tt> representation of this planning
     * <tt>Domain</tt>.
     *
     * @return a <tt>String</tt> representation of this planning
     *         <tt>Domain</tt>.
     */
    public String toString() {
       final StringBuilder str = new StringBuilder();
       str.append("(make-domain ");
       str.append(name);
       str.append(" \n");
       str.append(axioms.toString());
       str.append(operators.toString());
       str.append(methods.toString());
       str.append(")\n");
       return str.toString();
    }

}
