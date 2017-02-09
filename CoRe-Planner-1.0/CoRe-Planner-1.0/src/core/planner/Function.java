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

import java.util.ArrayList;
import java.util.Iterator;

/**
 * The <code>Function</code> class implements a function.
 *
 * @author Pellier Damien
 * @version 1.0, 14/01/03
 */
public final class Function extends ArrayList<Term> implements Term {

    /**
     * <code>serialVersionUID</code>.
     */
    private static final long serialVersionUID = 3544949944109642807L;

    /**
     * The symbol of the <tt>Function</tt>.
     */
    private String symbol;

    /**
     * This flag indicating if this <tt>Function</tt> can be evaluated. This
     * flag is <tt>true</tt> for call term.
     */
    private boolean isEval;

    /**
     * The flag indicating if this <tt>Function</tt> can be a legal
     * <tt>Assumption</tt>.
     */
    private boolean isHypothetical;

    /**
     * Indicate if this <tt>Function</tt> is ground that means composed only
     * of <tt>Constant</tt>.
     */
    private boolean isGround;

    /**
     * Create a new <tt>Function</tt>.
     */
    public Function() {
        this(new String());
    }

    /**
     * Create a <tt>Function</tt> of a specific symbol.
     *
     * @param s the <tt>Function</tt> symbol.
     */
    public Function(final String s) {
        super();
        this.setSymbol(s);
        this.setEval(false);
        this.setHypothetical(false);
        this.setGround(true);
    }

    /**
     * Sets the symbol of this <tt>Function</tt>.
     *
     * @param s the symbol belong to this <tt>Function</tt>.
     */
    public void setSymbol(final String s) {
        assert s != null : "Assertion parameter <s> == null";
        this.symbol = s;
    }

    /**
     * Returns the symbol belong to this <tt>Term</tt>.
     *
     * @return the symbol belong to this <tt>Term</tt>.
     */
    public String getSymbol() {
        return this.symbol;
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
     * @return always <tt>false</tt>.
     */
    public boolean isConstant() {
        return false;
    }

    /**
     * Returns if the <tt>Term</tt> is of type <tt>Function</tt>.
     *
     * @return always <tt>true</tt>.
     */
    public boolean isFunction() {
        return true;
    }

    /**
     * Returns if the <tt>Function</tt> is an eval.
     *
     * @return <tt>true</tt> if this <tt>Function</tt> is evaluable,
     *         <tt>false</tt> otherwise.
     */
    public boolean isEval() {
        return this.isEval;
    }

    /**
     * Sets the eval flag of this <tt>Function</tt>.
     *
     * @param flag <tt>true</tt> if this <tt>Function</tt> is an evaluable,
     *        <tt>false</tt> otherwise.
     */
    public void setEval(final boolean flag) {
        this.isEval = flag;
    }

    /**
     * Returns if this <tt>Function</tt> can be a legal <tt>Assumption</tt>.
     *
     * @return <tt>true</tt> if this <tt>Function</tt> can be a legal
     *         <tt>Assumption</tt>,<tt>false</tt> otherwise.
     */
    public boolean isHypothetical() {
        return this.isHypothetical;
    }

    /**
     * Sets the legal <tt>Assumption</tt> flag of this <tt>Function</tt>.
     *
     * @param flag <tt>true</tt> if this <tt>Function</tt> can be a legal
     *        <tt>Assumption</tt>,<tt>false</tt> otherwise.
     */
    public void setHypothetical(final boolean flag) {
        this.isHypothetical = flag;
    }

    /**
     * Returns if a specific <tt>Term</tt> occurs in this <tt>Function</tt>.
     *
     * @param t whose presence in the <tt>TermList</tt> of this
     *        <tt>Function</tt> is to be tested.
     * @return <tt>true</tt> if the specified <tt>Term</tt> is present,
     *         <tt>false</tt> otherwise.
     */
    public boolean occurs(final Term t) {
        assert t != null : "Assertion parameter <t> == null";
        boolean occur = false;
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
        return occur;
    }

    /**
     * Returns if this <tt>Function</tt> is ground.
     *
     * @return <tt>true</tt> if this <tt>Function</tt> is ground,
     *         <tt>false</tt> otherwise.
     */
    public boolean isGround() {
        return this.isGround;
    }

    /**
     * Sets the ground flag of this <tt>Function</tt>.
     *
     * @param flag <tt>true</tt> if this <tt>Function</tt> is ground,
     *        <tt>false</tt> otherwise.
     */
    public void setGround(final boolean flag) {
        this.isGround = flag;
    }

    /**
     * Returns the most general unifier of two terms.
     *
     * @param t the <tt>Term</tt> to unify.
     * @return the most general unifier or <tt>null</tt> if it does not exist.
     *         An empty mgu means that there is no binding <tt>Variable</tt>
     *         between <tt>t</tt> and this term.
     * @exception UnificationException if <tt>t</tt> is not an instance of
     *            <tt>Function</tt>.
     */
    public Substitution unify(final Term t) throws UnificationException {
        assert t != null : "parameter <t> == null";
        return unify(t, new Substitution());
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
     *            <tt>Function</tt>.
     */
    public Substitution unify(final Term t, final Substitution alpha)
            throws UnificationException {

        assert t != null : "parameter <t> == null";
        assert alpha != null : "parameter <alpha> == null";

        Substitution unifier = null;

        // Add to take account of arithmetique call
        if (t instanceof Constant) {
            if (this.isEval()) {
                final Term st = (Term) this.applySubstitution(alpha);
                if (st instanceof Constant) {
                    unifier = st.unify(t);
                }
            }
        } else if (t instanceof Variable) {
            unifier = t.unify(this);
        } else if (t instanceof Function) {
            final Function f1 = this;
            final Function f2 = (Function) t;
            if (f1.getSymbol().equals(f2.getSymbol())
                    && f1.size() == f2.size()) {
                final Substitution alphaCopy = (Substitution) alpha.clone();
                unifier = new Substitution();
                for (int i = 0; i < f1.size(); i++) {
                    final Term f1Term = f1.get(i);
                    final Term f2Term = f2.get(i);
                    final Substitution beta =
                        f1Term.unify(f2Term, alphaCopy);
                    if (beta == null) {
                        return null;
                    }
                    unifier.compose(beta);
                    alphaCopy.compose(beta);
                }
            }
        } else {
            throw new UnificationException(this.toString() + " "
                    + t.toString());
        }
        return unifier;
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
        final Function function = new Function();
        function.setSymbol(this.getSymbol());
        function.setEval(this.isEval());
        function.setHypothetical(this.isHypothetical());
        function.setGround(true);
        for (Term t : this) {
            final Term subTerm =
                (Term) t.applySubstitution(alpha, replaceVar);
            if (function.isGround()) {
                if ((function.getSymbol().equals("+")
                        || function.getSymbol().equals("-")
                        || function.getSymbol().equals("*")
                        || function.getSymbol().equals("/"))
                        && subTerm instanceof Constant
                        && subTerm.getSymbol().charAt(0) == '#') {
                    function.setGround(false);
                }
                if (subTerm instanceof Variable) {
                    function.setGround(false);
                } else if (subTerm instanceof Function) {
                    function.setGround(((Function) subTerm).isGround());
                }
            }
            function.add(subTerm);
        }
        if (function.isGround()
                && (function.getSymbol().equals("+")
                        || function.getSymbol().equals("-")
                        || function.getSymbol().equals("*")
                        || function.getSymbol().equals("/")
                        || function.getSymbol().equals("cat"))) {
            return Eval.evaluate(function.getSymbol(), function.get(0),
                    function.get(1));
        }
        return function;
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
     * Returns if an <tt>Object</tt> is equal to this <tt>Function</tt>.
     *
     * @param obj the <tt>Object</tt> to be compared.
     * @return <tt>true</tt> if <tt>Object</tt> to be compared is equal to
     *         this <tt>Function</tt>,<tt>false</tt> otherwise.
     */
    public boolean equals(final Object obj) {
        if (obj instanceof Function) {
            final Function cmp = (Function) obj;
            if (this.getSymbol().equals(cmp.getSymbol())) {
                return super.equals(cmp);
            }
        }
        return false;
    }

    /**
     * Returns the hash code value of this <tt>Function</tt>.
     *
     * @return the hash code value of this <tt>Function</tt>.
     */
    public int hashCode() {
        int hash = this.getSymbol().hashCode();
        for (Term t : this) {
            hash += t.hashCode();
        }
        return hash;
    }

    /**
     * Returns a deep copy of this <tt>Function</tt>.
     *
     * @return a deep copy of this <tt>Function</tt>.
     */
    public Object clone() {
        final Function copy = new Function();
        copy.setEval(this.isEval());
        copy.setGround(this.isGround());
        copy.setHypothetical(this.isHypothetical());
        copy.setSymbol(this.getSymbol());
        for (Term t : this) {
            copy.add((Term) t.clone());
        }
        return copy;
    }

    /**
     * Standardize this <tt>Function</tt>.
     *
     * @return a standardizable copy of this <tt>Function</tt>.
     * @see core.planner.Term#standardize()
     */
    public Object standardize() {
        final Function copy = new Function();
        copy.setEval(this.isEval());
        copy.setGround(this.isGround());
        copy.setHypothetical(this.isHypothetical());
        copy.setSymbol(this.getSymbol());
        for (Term t : this) {
            copy.add((Term) t.standardize());
        }
        return copy;
    }

    /**
     * Returns a <tt>String</tt> representation of this <tt>Function</tt>.
     *
     * @return a <tt>String</tt> representation of this <tt>Function</tt>.
     */
    public String toString() {
        final StringBuilder str = new StringBuilder();
        str.append("(");
        if (getSymbol().equals(".")) {
            str.append("list ");
            str.append(toStringList(this));
        } else {
            if (isEval()) {
                str.append("call ");
            }
            str.append(getSymbol());
            str.append(" ");
            for (Term t : this) {
                str.append(t.toString());
            }
            str.append(")");
        }
        return str.toString();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Private methods

    /**
     * Returns a <tt>SringBuilder</tt> representation of list of terms.
     *
     * @param t the first <tt>Term</tt> of the list.
     * @return a <tt>Sring</tt> representation of list of terms.
     */
    private StringBuilder toStringList(final Term t) {
        final StringBuilder str = new StringBuilder();
        if (t.getSymbol().equals("nil")) {
            str.append(") ");
        } else if (t instanceof Variable) {
            str.append(" . ");
            str.append(t.toString());
            str.append(") ");
        } else {
            final Function function = (Function) t;
            final Term current = function.get(0);
            str.append(current.toString());
            str.append(" ");
            final Term next = function.get(1);
            str.append(toStringList(next));
        }
        return str;
    }

}
