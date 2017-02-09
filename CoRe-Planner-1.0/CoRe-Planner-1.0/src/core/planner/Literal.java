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

/**
 * The <code>Literal</code> class implements a literal which is any of the
 * folowing: a logical atom a; an expression of the form (not a) where a is a
 * logical atom (the intended meaning is that the expression is true if a is
 * false).
 *
 * @author Pellier Damien
 * @version 1.0, 14/01/03
 */
public final class Literal extends Atom {

    /**
     * <code>serialVersionUID</code>.
     */
    private static final long serialVersionUID = 3256441421547779895L;

    /**
     * The flag indicating if this <tt>Literal</tt> can be a legal
     * <tt>Assumption</tt>.
     */
    private boolean isHypothetical;

    /**
     * The flag indicated if the <tt>Literal</tt> is a negation.
     */
    private boolean isNegative;

    /**
     * Create an empty positive <tt>Literal</tt>.
     */
    public Literal() {
        this(new String());
    }

    /**
     * Create a positive <tt>Literal</tt> of a specific symbol.
     *
     * @param s the <tt>Literal</tt> symbol.
     */
    public Literal(final String s) {
        super(s, false);
        this.setHypothetical(false);
        this.setNegative(false);
    }

    /**
     * Create a positive <tt>Literal</tt> of a specific symbol.
     *
     * @param s the <tt>Literal</tt> symbol.
     * @param flag indicates if the <tt>Literal</tt> is ground.
     */
    public Literal(final String s, final boolean flag) {
        super(s, flag);
        this.setHypothetical(false);
        this.setNegative(false);
    }

    /**
     * Returns if the <tt>Literal</tt> can be evaluated.
     *
     * @return <tt>true</tt> if the <tt>Literal</tt> can be evaluated,
     *         <tt>false</tt> otherwise.
     */
    public boolean isEval() {
        return this.getSymbol().equals("call");
    }

    /**
     * Returns if this <tt>Literal</tt> can be a legal <tt>Assumption</tt>.
     *
     * @return <tt>true</tt> if this <tt>Literal</tt> can be a legal
     *         <tt>Assumption</tt>,<tt>false</tt> otherwise.
     */
    public boolean isHypothetical() {
        return this.isHypothetical;
    }

    /**
     * Sets the legal assumption flag of this <tt>Literal</tt>.
     *
     * @param flag <tt>true</tt> if this <tt>Literal</tt> can be a legal
     *        <tt>Assumption</tt>,<tt>false</tt> otherwise.
     */
    public void setHypothetical(final boolean flag) {
        this.isHypothetical = flag;
    }

    /**
     * Sets the negation flag of this <tt>Literal</tt>.
     *
     * @param flag the negation flag of this <tt>Literal</tt>.
     */
    public void setNegative(final boolean flag) {
        this.isNegative = flag;
    }

    /**
     * Returns if this <tt>Literal</tt> is negative.
     *
     * @return <tt>true</tt> if the <tt>Listeral</tt> is negative,
     *         <tt>false</tt> otherwise.
     */
    public boolean isNegative() {
        return isNegative;
    }

    /**
     * Returns the most general unifier of two literals. Prior binding
     * variables contained in the <tt>Substitution</tt> parameter
     * are considered.
     *
     * @param a the first <tt>Literal</tt> to unify.
     * @return the most general unifier or <tt>null</tt> if it does not exist.
     *         An empty mgu means that there is no binding <tt>Variable</tt>
     *         between <tt>this</tt> and <tt>a</tt>.
     * @exception UnificationException if <tt>a</tt> is not an instance of
     *            <tt>Literal</tt>.
     */
    public Substitution unify(final Atom a) throws UnificationException {
        return unify(a, new Substitution());
    }

    /**
     * Returns the most general unifier of two literals. Prior binding
     * variables contained in the <tt>Substitution</tt> parameter
     * are considered.
     *
     * @param a the first <tt>Literal</tt> to unify.
     * @param alpha the prior binding variables.
     * @return the most general unifier or <tt>null</tt> if it does not exist.
     *         An empty mgu means that there is no binding <tt>Variable</tt>
     *         between <tt>this</tt> and <tt>a</tt>.
     * @exception UnificationException if <tt>a</tt> is not an instance of
     *            <tt>Literal</tt>.
     */
    public Substitution unify(final Atom a, final Substitution alpha)
            throws UnificationException {
        if (a instanceof Literal) {
            final Literal l = (Literal) a;
            if (this.isNegative() != l.isNegative()) {
                return null;
            } else {
                return unifyAtom(a, alpha);
            }
        } else {
            throw new UnificationException(this.toString()
                    + " " + a.toString());
        }
    }

    /**
     * Returns a new <tt>Literal</tt> such that the variables
     * appearing in the <tt>Substitution</tt> are replaced with the
     * corresponding <tt>Term</tt> of the <tt>Susbstitution</tt>.
     *
     * @param alpha the <tt>Substitution</tt> to apply.
     * @param replaceVar flag to indicate if the <tt>Substitution</tt> allows
     *        to instantiate <tt>Variable</tt> s not in the
     *        <tt>Substitution</tt> by a <tt>Constant</tt> with the
     *        <tt>Variable</tt> name.
     * @return a substituted copy of the <tt>Literal</tt>.
     * @exception SubstitutionException if an error occur during substitution.
     */
    public Object applySubstitution(final Substitution alpha,
            final boolean replaceVar) throws SubstitutionException {
        assert alpha != null : "parameter <alpha> == null";

        final Literal sl = new Literal();
        sl.setSymbol(this.getSymbol());
        sl.setHypothetical(this.isHypothetical());
        sl.setNegative(this.isNegative());

        for (Term term : this) {
            final Term st = (Term) term.applySubstitution(alpha, replaceVar);
            if (st instanceof Constant) {
                sl.setGround(true);
            } else if (st instanceof Variable) {
               sl.setGround(false);
            } else if (st instanceof Function) {
                final Function f = (Function) st;
                sl.setGround(f.isGround());
            } else {
                throw new SubstitutionException("Substitutions term "
                        + term.toString() + " by " + alpha.toString());
            }
            sl.add(st);
        }
        return sl;
    }

    /**
     * Returns a new <tt>Literal</tt> such that the variables
     * appearing in the <tt>Substitution</tt> are replaced with the
     * corresponding <tt>Term</tt> of the <tt>Substitution</tt>.
     *
     * @param alpha the <tt>Substitution</tt> to apply.
     * @return a substituted copy of the <tt>Literal</tt>.
     * @exception SubstitutionException if an error occur during substitution.
     */
    public Object applySubstitution(final Substitution alpha)
            throws SubstitutionException {
        return this.applySubstitution(alpha, false);
    }

    /**
     * Return if a <tt>Term</tt> occurs in this <tt>Literal</tt>.
     *
     * @param t the <tt>Term</tt> to be tested.
     * @return <tt>true</tt> if a specific <tt>Term</tt> occurs in this
     *         <tt>Literal</tt>,<tt>false</tt> otherwise.
     */
    public boolean occurs(final Term t) {
        assert t != null : "parameter <t> == null";
        boolean occur = false;
        if (this.isEval()) {
            final Function func = (Function) this.get(0);
            occur = func.occurs(t);
        } else {
            final Iterator<Term> i = this.iterator();
            while (i.hasNext() && !occur) {
                final Term ti = i.next();
                if (t.equals(ti)) {
                    occur = true;
                } else if (t instanceof Function) {
                    final Function f = (Function) t;
                    occur = f.occurs(t);
                }
            }
        }
        return occur;
    }

    /**
     * Returns if this <tt>Literal</tt> is equal to another <tt>Object</tt>.
     *
     * @param obj the <tt>Object</tt> to be compared.
     * @return <tt>true</tt> if the specific <tt>Object</tt> is equal to
     *         this <tt>Literal</tt>, <tt>false</tt> otherwise.
     */
    public boolean equals(final Object obj) {
        if (obj instanceof Literal) {
            final Literal cmp = (Literal) obj;
            if (cmp.getSymbol().equals(this.getSymbol())
                    && cmp.isNegative() == this.isNegative()
                    && cmp.isHypothetical() == this.isHypothetical()) {
                return super.equals(cmp);
            }
        }
        return false;
    }

    /**
     * Returns the hash code value of this <tt>Literal</tt>.
     *
     * @return the hash code value.
     */
    public int hashCode() {
        int hash = this.getSymbol().hashCode();
        for (Term t : this) {
            hash += t.hashCode();
        }
        if (this.isNegative()) {
            hash++;
        }
        if (this.isHypothetical()) {
            hash++;
        }
        return hash;
    }

    /**
     * Returns a deep copy of this <tt>Literal</tt>.
     *
     * @return a deep copy of this <tt>Literal</tt>.
     */
    public Object clone() {
        final Literal copy = new Literal();
        copy.setGround(this.isGround());
        copy.setHypothetical(this.isHypothetical());
        copy.setNegative(this.isNegative());
        copy.setSymbol(this.getSymbol());
        for (Term t : this) {
           copy.add((Term) t.clone());
        }
        return copy;
    }

    /**
     * Standardize this <tt>Literal</tt>.
     *
     * @return a standardizable copy of this <tt>Literal</tt>.
     */
    public Object standardize() {
        final Literal copy = new Literal();
        copy.setGround(this.isGround());
        copy.setHypothetical(this.isHypothetical());
        copy.setNegative(this.isNegative());
        copy.setSymbol(this.getSymbol());
        for (Term t : this) {
            copy.add((Term) t.standardize());
        }
        return copy;
    }

    /**
     * Returns a <tt>String</tt> representation of this <tt>Literal</tt>.
     *
     * @return a <tt>String</tt> representation of this <tt>Literal</tt>.
     */
    public String toString() {
        final StringBuilder str = new StringBuilder();
        if (isNegative()) {
            str.append("(not ");
        }
        if (this.isEval()) {
            final Function function = (Function) this.get(0);
            str.append(function.toString());
        } else {
            if (this.isEmpty()) {
                str.append("(");
                str.append(getSymbol());
                str.append(")");
            } else {
                str.append("(");
                str.append(getSymbol());
                str.append(" ");
                for (int i = 0; i < this.size(); i++) {
                    final Term term = this.get(i);
                    str.append(term.toString());
                }
                str.append(" ) ");
            }
        }
        if (isNegative()) {
            str.append(" ) ");
        }
        return str.toString();
    }

}
