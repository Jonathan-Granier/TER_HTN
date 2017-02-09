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
 * The <code>Variable</code> class implements a variable.
 *
 * @author Pellier Damien
 * @version 1.0, 14/01/03
 */
public final class Variable implements Term {

    /**
     * The default value of the varCounter use to standardize variable.
     */
    private static final int DEFAULT_VAR_COUNTER = 0;

    /**
     * The static var counter used to standardize.
     */
    private static int varCounter = DEFAULT_VAR_COUNTER;

    /**
     * The symbol of the <tt>Variable</tt>.
     */
    private String symbol;

    /**
     * Create a new <tt>Variable</tt>.
     */
    public Variable() {
        this(new String());
    }

    /**
     * Create a <tt>Variable</tt> of a specific symbol.
     *
     * @param s the <tt>Variable</tt> symbol.
     */
    public Variable(final String s) {
        this.setSymbol(s);
    }

    /**
     * Sets the symbol of this <tt>Variable</tt>.
     *
     * @param s the symbol belong to this <tt>Variable</tt>.
     */
    public void setSymbol(final String s) {
        assert s != null : "parameter <s> == null";
        this.symbol = s;
    }

    /**
     * Returns the symbol belong to this <tt>Variable</tt>.
     *
     * @return the symbol belong to this <tt>Variable</tt>.
     */
    public String getSymbol() {
        return this.symbol;
    }

    /**
     * Returns if the <tt>Term</tt> is a <tt>Variable</tt>.
     *
     * @return always <tt>true</tt>.
     */
    public boolean isVariable() {
        return true;
    }

    /**
     * Returns if the <tt>Term</tt> is of type <tt>Constant</tt>.
     *
     * @return always <tt>false</tt>.
     */
    public boolean isConstant() {
        return false;
    }

    /**
     * Returns if the <tt>Term</tt> is of type <tt>Function</tt>.
     *
     * @return always <tt>false</tt>.
     */
    public boolean isFunction() {
        return false;
    }

    /**
     * Returns the most general unifier of two terms.
     *
     * @param t the <tt>Term</tt> to unify.
     * @return the most general unifier or <tt>null</tt> if it does not exist.
     *         An empty mgu means that there is no binding <tt>Variable</tt>
     *         between <tt>t</tt> and this term.
     * @exception UnificationException if <tt>t</tt> is not an instance of
     *            <tt>Variable</tt>.
     */
    public Substitution unify(final Term t) throws UnificationException {
        assert t != null : "parameter <t> == null";
        return this.unify(t, new Substitution());
    }

    /**
     * Returns the most general unifier of two terms. Prior binding variables
     * contained in the <tt>Substitution</tt> parameter are considered.
     *
     * @param t the <tt>Term</tt> to unify.
     * @param alpha the prior binding variables.
     * @return the most general unifier or <tt>null</tt> if it does not exist.
     *         An empty mgu means that there is no binding <tt>Variable</tt>
     *         between <tt>t</tt> and this term.
     * @exception UnificationException if <tt>t</tt> is not an instance of
     *            <tt>Variable</tt>.
     */
    public Substitution unify(final Term t, final Substitution alpha)
            throws UnificationException {
        assert t != null : "parameter <t> == null";
        assert alpha != null : "parameter <alpha> == null";

        final Term instance = alpha.instance(this);
        if (instance != null) {
            return instance.unify(t, alpha);
        }
        if (t instanceof Constant) {
            final Substitution unifier = new Substitution();
            unifier.compose(this, t);
            return unifier;
        } else if (t instanceof Variable) {
            final Substitution unifier = new Substitution();
            unifier.compose(this, t);
            return unifier;
        } else if (t instanceof Function) {
            final Substitution unifier = new Substitution();
            unifier.compose(this, t);
            return unifier;
        } else {
            throw new UnificationException(this.toString() + " "
                    + t.toString());
        }
    }

    /**
     * Returns a new <tt>Term</tt> such that the variables
     * appearing in the <tt>Substitution</tt> are replaced with the
     * corresponding <tt>Term</tt> in the <tt>Substitution</tt>.
     *
     * @param alpha the <tt>Substitution</tt> to apply.
     * @param replaceVar flag to indicate that if the <tt>Substitution</tt>
     *        not allows to instantiate all variables of the
     *        <tt>Term</tt> replace by a <tt>Constant</tt> with the
     *        <tt>Variable</tt> name.
     * @return the substituted copy of the <tt>Term</tt>.
     */
    public Object applySubstitution(final Substitution alpha,
            final boolean replaceVar) {
        assert alpha != null : "parameter <alpha> == null";
        final Term instance = alpha.instance(this);
        if (instance == null) {
            if (replaceVar) {
                final String symb = this.getSymbol();
                final String newSymbol = symb.substring(1, symb.length());
                final Constant c = new Constant("#" + newSymbol);
                alpha.compose(this, c);
                return c;
            } else {
                return (Variable) this.clone();
            }
         } else if (instance instanceof Constant) {
             return (Term) instance.clone();
         } else {
             return instance.applySubstitution(alpha);
         }
    }

    /**
     * Returns a new <tt>Term</tt> such that the variables appearing in
     * the <tt>Substitution</tt> are replaced with the corresponding
     * <tt>Term</tt> in the <tt>Substitution</tt>.
     *
     * @param alpha the <tt>Substitution</tT> to apply.
     * @return a substituted copy of the <tt>Term</tt>.
     */
    public Object applySubstitution(final Substitution alpha) {
        return this.applySubstitution(alpha, false);
    }

    /**
     * Returns if an <tt>Object</tt> is equals to this <tt>Variable</tt>.
     *
     * @param obj the <tt>Object</tt> to be compared.
     * @return <tt>true</tt> if the <tt>Object</tt> to be compared is equal
     *         to this <tt>Variable</tt>, <tt>false</tt> otherwise.
     */
    public boolean equals(final Object obj) {
        if (obj instanceof Variable) {
            final Variable cmp = (Variable) obj;
            return this.getSymbol().equals(cmp.getSymbol());
        }
        return false;
    }

    /**
     * Returns the hash code value of this <tt>Variable</tt>.
     *
     * @return the hash code value of this <tt>Variable</tt>.
     */
    public int hashCode() {
        return this.getSymbol().hashCode();
    }

    /**
     * Returns a deep copy of this <tt>Variable</tt>.
     *
     * @return a deep copy of this <tt>Variable</tt>.
     */
    public Object clone() {
        return new Variable(this.getSymbol());
    }

    /**
     * Standardize this <tt>Variable</tt>.
     *
     * @return a standardizable copy of this <tt>Variable</tt>.
     * @see core.planner.Term#standardize()
     */
    public Object standardize() {
        return new Variable(this.getSymbol() + String.valueOf(varCounter));
    }

    /**
     * Returns a <tt>String</tt> representation of this <tt>Variable</tt>.
     *
     * @return a <tt>String</tt> representation of this <tt>Variable</tt>.
     */
    public String toString() {
        final StringBuilder str = new StringBuilder();
        str.append(getSymbol());
        str.append(" ");
        return str.toString();
    }

    /**
     * Increments the standardization counter.
     */
    static void incrementStandardizationCounter() {
        varCounter = (varCounter + 1) % Integer.MAX_VALUE;
    }

    /**
     * Decrements the standardization counter.
     */
    static void decrementStandardizationCounter() {
        if (varCounter - 1 < 0) {
            varCounter = Integer.MAX_VALUE - (varCounter - 1);
        } else {
            varCounter = varCounter - 1;
        }
    }

}
