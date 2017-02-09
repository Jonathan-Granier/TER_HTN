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
 * This class implements what is an assumption.
 *
 * @author Damien Pellier
 * @version 1.0, 14/01/03
 */
public final class Assumption implements Cloneable {

    /**
     * Define the different type of an <tt>Assumption</tt>.
     */
    public static enum Type {
        /**
         * The constant that defines an <tt>Assumption</tt> of type
         * <tt>FACT</tt>.
         */
        FACT,
        /**
         * The constant that defines an <tt>Assumption</tt> of type
         * <tt>NEGATION_FACT</tt>.
         */
        NEGATION_FACT,
        /**
         * The constant that define an <tt>assumption</tt> of type
         * <tt>CONSTAINT_VIOLATION</tt>.
         */
        CONSTAINT_VIOLATION
    };

    /**
     * The type of this <tt>Assumption</tt>.
     */
    private Type type;

    /**
     * The literal representing the assumption.
     */
    private Literal literal;

    /**
     * The list of constraint.
     */
    private Conjunction constraints;

    /**
     * Create an <tt>Assumption</tt> from a specific <tt>Literal</tt>.
     *
     * @param l the <tt>Literal</tt> represented an <tt>Assumption</tt>.
     * @param t the type of the <tt>Assumption</tt>.
     */
    public Assumption(final Literal l, final Type t) {
        this.setLiteral(l);
        this.setType(t);
        this.constraints = new Conjunction();
    }

    /**
     * Create an <tt>Assumption</tt> from a specific <tt>Literal</tt>.
     * <br> The default type of the assumption is <tt>FACT</tt>.
     */
    public Assumption() {
        this(new Literal(), Assumption.Type.FACT);
    }

    /**
     * The <tt>Literal</tt> that represents the <tt>Assumption</tt>.
     * <br>
     * This method make a copy of the literal in parameter.
     *
     * @param l the <tt>Literal</tt> that represents fact.
     */
    public void setLiteral(final Literal l) {
        assert l != null : "Assertion: parameter <l> == null";
        this.literal = l;
    }

    /**
     * Return the <tt>Literal</tt> representing the <tt>Assumption</tt>.
     *
     * @return the <tt>Literal</tt> representing the <tt>Assumption</tt>.
     */
    public Literal getLiteral() {
        return this.literal;
    }

    /**
     * Return the type of this <tt>Assumption</tt>.
     *
     * @return the type of this <tt>Assumption</tt>.
     */
    public Assumption.Type getType() {
        return type;
    }

    /**
     * Set the type of this <tt>Assumption</tt>.
     *
     * @param t the type to set.
     */
    public void setType(final Assumption.Type t) {
        this.type = t;
    }

    /**
     * Add a constaint to this <tt>Assumption</tt>.
     *
     * @param c the constraint to add.
     */
    public void addConstraint(final Literal c) {
        assert c != null : "Assertion: parameter <c> == null";
        this.constraints.add(c);
    }
    /**
     * Returns if this <tt>Assumption</tt> is equal to another <tt>Object</tt>.
     *
     * @param obj the <tt>Object</tt> to be compared.
     * @return <tt>true</tt> if the specific <tt>Object</tt> is equal to
     *         this <tt>Assumption</tt>, <tt>false</tt> otherwise.
     */
    public boolean equals(final Object obj) {
        if (obj instanceof Assumption) {
            final Assumption cmp = (Assumption) obj;
            return cmp.getType() == this.getType()
                && cmp.getLiteral().equals(this.getLiteral());
        }
        return false;
    }

    /**
     * Returns the hash code value of this <tt>Literal</tt>.
     *
     * @return the hash code value.
     */
    public int hashCode() {
        return this.getType().hashCode() + this.getLiteral().hashCode();
    }

    /**
     * Returns a clone of this <tt>Assumption</tt>.
     *
     * @return a deep copy of this <tt>Assumption</tt>.
     */
    public Object clone() {
        final Assumption copy = new Assumption();
        copy.literal = (Literal) this.getLiteral().clone();
        copy.constraints = (Conjunction) this.constraints.clone();
        return copy;
    }

    /**
     * Return a <tt>String</tt> representation of this <tt>Assumption</tt>.
     *
     * @return a <tt>String</tt> representation of this <tt>Assumption</tt>.
     */
    public String toString() {
        final StringBuilder str = new StringBuilder();
        if (this.getType().equals(Assumption.Type.FACT)) {
            str.append("(fact ");
            str.append(this.getLiteral().toString());
            str.append(")");
        } else if (this.getType().equals(Assumption.Type.NEGATION_FACT)) {
            str.append("(negation-fact ");
            str.append(this.getLiteral().toString());
            str.append(")");
        } else {
            str.append("(constraint-violation ");
            str.append(this.getLiteral().toString());
            str.append("(list ");
            for (int i = 0; i < this.constraints.size(); i++) {
                str.append(this.constraints.get(i).toString());
            }
            str.append("))");
        }
        return str.toString();
    }

}
