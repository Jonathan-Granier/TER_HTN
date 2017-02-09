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
 * The <code>Constant</code> class implements a constant.
 *
 * @author Pellier Damien
 * @version 1.0, 14/01/03
 */
public final class Constant implements Term {

    /**
     * The symbol of the <tt>Constant</tt>.
     */
    private String symbol;

    /**
     * The flag indicated if this is a numerical <tt>Constant</tt>.
     */
    private boolean isNumerical;

    /**
     * Create a new <tt>Constant</tt>.
     * <br>
     * By default, the constant create is not numerical.
     */
    public Constant() {
        this(new String());
    }

    /**
     * Create a new <tt>Constant</tt> of a specific symbol.
     * <br>
     * By default, the constant create is not numerical.
     *
     * @param s the <tt>Constant</tt> symbol.
     */
    public Constant(final String s) {
        this.setSymbol(s);
        this.setNumerical(false);
    }

    /**
     * Returns if the <tt>Term</tt> is a <tt>Variable</tt>.
     *
     * @return always <tt>false</tt>.
     */
    public boolean isVariable() {
        return false;
    }

    /**
     * Returns if the <tt>Term</tt> is of type <tt>Constant</tt>.
     *
     * @return always <tt>true</tt>.
     */
    public boolean isConstant() {
        return true;
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
     * Sets the symbol of this <tt>Constant</tt>.
     *
     * @param s the symbol belong to this <tt>Constant</tt>.
     */
    public void setSymbol(final String s) {
        assert s != null : "Assertion: parameter <s> == null";
        this.symbol = s;
    }

    /**
     * Returns the symbol belong to this <tt>Constant</tt>.
     *
     * @return the symbol belong to this <tt>Constant</tt>.
     */
    public String getSymbol() {
        return this.symbol;
    }

    /**
     * Sets the flag numerical to this <tt>Constant</tt>.
     *
     * @param flag the numerical flag.
     */
    public void setNumerical(final boolean flag) {
        this.isNumerical = flag;
    }

    /**
     * Returns if this <tt>Constant</tt> is numerical.
     *
     * @return <tt>true</tt> if this <tt>Constant</tt> is a numerical
     *         <tt>Constant</tt>,<tt>false</tt> otherwise.
     */
    public boolean isNumerical() {
        return this.isNumerical;
    }

    /**
     * Returns the most general unifier of two terms.
     *
     * @param t the <tt>Term</tt> to unify.
     * @return the most general unifier or <tt>null</tt> if it does not exist.
     *         An empty mgu means that there is no binding <tt>Variable</tt>
     *         between <tt>t</tt> and this term.
     * @exception UnificationException if <tt>t</tt> is not an instance of
     *            <tt>Constant</tt>.
     */
    public Substitution unify(final Term t) throws UnificationException {
        assert t != null : "parameter <t> == null";
        return this.unify(t, new Substitution());
    }

    /**
     * Returns the most general unifier of two terms. Prior binding
     * variables contained in the <tt>Substitution</tt> parameter
     * are considered.
     *
     * @param t the <tt>Term</tt> to unify.
     * @param alpha the prior binding variables.
     * @return the most general unifier or <tt>null</tt> if it does not exist.
     *         An empty mgu means that there is no binding <tt>Variable</tt>
     *         between <tt>t</tt> and this term.
     * @exception UnificationException if <tt>t</tt> is not an instance of
     *            <tt>Constant</tt>.
     */
    public Substitution unify(final Term t, final Substitution alpha)
            throws UnificationException {
        assert t != null : "parameter <t> == null";
        assert alpha != null : "parameter <alpha> == null";

        if (t instanceof Constant) {
            if (this.equals(t)) {
                return new Substitution();
            } else {
                return null;
            }
        } else if (t instanceof Variable) {
            return t.unify(this, alpha);
        } else if (t instanceof Function) {
            return t.unify(this, alpha);
        } else {
            throw new UnificationException(this.toString()
                    + " " + t.toString());
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
        if (replaceVar) {
            final String symb = this.getSymbol();
            if (symb.charAt(0) == '#') {
                final String newSymbol = "?"
                    + symb.substring(1, symb.length());
                final Variable var = new Variable(newSymbol);
                final Term t =
                    (Term) var.applySubstitution(alpha, replaceVar);
                return t;
            }
        }
        return (Constant) this.clone();
    }

    /**
     * Returns a new <tt>Term</tt> such that the variables appearing in the
     * <tt>Substitution</tt> are replaced with the corresponding <tt>Term</tt>
     * in the <tt>Substitution</tt>.
     *
     * @param alpha the <tt>Substitution</tT> to apply.
     * @return a substituted copy of the <tt>Term</tt>.
     */
    public Object applySubstitution(final Substitution alpha) {
        return this.applySubstitution(alpha, false);
    }

    /**
     * Returns if an <tt>Object</tt> is equals to this <tt>Constant</tt>.
     *
     * @param obj the <tt>Object</tt> to be compared.
     * @return <tt>true</tt> if the <tt>Object</tt> to be compared is equal
     *         to this <tt>Constant</tt>, <tt>false</tt> otherwise.
     */
    public boolean equals(final Object obj) {
        if (obj instanceof Constant) {
            final Constant cmp = (Constant) obj;
            return this.getSymbol().equals(cmp.getSymbol());
        }
        return false;
    }

    /**
     * Returns the hash code value of this <tt>Constant</tt>.
     *
     * @return the hash code value of this <tt>Constant</tt>.
     */
    public int hashCode() {
        return this.getSymbol().hashCode();
    }

    /**
     * Returns a deep copy of this <tt>Constant</tt>.
     *
     * @return a deep copy of this <tt>Constant</tt>.
     */
    public Object clone() {
        final Constant copy = new Constant(this.getSymbol());
        copy.setNumerical(this.isNumerical());
        return copy;
    }

    /**
     * Standardize this <tt>Constant</tt>.
     *
     * @return a standardizable copy of this <tt>Constant</tt>.
     * @see core.planner.Term#standardize()
     */
    public Object standardize() {
        return this.clone();
    }

    /**
     * Returns a <tt>String</tt> representation of this <tt>Constant</tt>.
     *
     * @return a <tt>String</tt> representation of this <tt>Constant</tt>.
     */
    public String toString() {
        final StringBuilder str = new StringBuilder();
        str.append(getSymbol());
        str.append(" ");
        return str.toString();
    }

}
