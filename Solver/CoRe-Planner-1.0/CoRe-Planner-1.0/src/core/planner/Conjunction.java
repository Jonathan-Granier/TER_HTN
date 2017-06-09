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

/**
 * The <code>Conjunction</code> class implements a Conjunction. The class can
 * contain a list of literal objects. It can be seen as a form: (P1 P2 ... Pn)
 * or (:first P1 P2 ... Pn) where the Pi's are literals.
 *
 * @author Pellier Damien
 * @version 1.0, 14/01/03
 */
public final class Conjunction extends ArrayList<Literal>
        implements Standardizable, Substituable, Cloneable {

    /**
     * <code>serialVersionUID</code>.
     */
    private static final long serialVersionUID = 3257005466684044596L;

    /**
     * If this is a <tt>Conjunction</tt>, then label containing the string
     * "first", otherwise label is "".
     */
    private String label;

    /**
     * The name of the <tt>Conjunction</tt>.
     */
    private String name;

    /**
     * The value is <tt>true</tt> if the whole list is to be constructed from a
     * <tt>Variable</tt> that contains a list of <tt>LogicalAtom</tt>s.
     */
    private boolean isVarList;

    /**
     * Create an empty <tt>Conjunction</tt>.
     */
    public Conjunction() {
        super();
        this.setLabel(new String());
        this.setName(new String());
        this.setVarList(false);
    }

    /**
     * Sets a label to this <tt>Conjunction</tt>.
     *
     * @param l the label to set.
     */
    public void setLabel(final String l) {
        assert l != null : "Assertion: parameter <l> == null";
        this.label = l;
    }

    /**
     * Returns the label of this <tt>Conjunction</tt>.
     *
     * @return the label of this <tt>Conjunction</tt>.
     */
    public String getLabel() {
        return this.label;
    }

    /**
     * Sets the name of this <tt>Conjunction</tt>.
     *
     * @param n the name to set.
     */
    public void setName(final String n) {
        assert n != null : "Assertion: parameter <n> == null";
        this.name = n;
    }

    /**
     * Returns the name of this <tt>Conjunction</tt>.
     *
     * @return the name of this <tt>Conjunction</tt>.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the varlist flag of this <tt>Conjunction</tt>.
     *
     * @param flag <tt>true</tt> if this <tt>Conjunction</tt> is a varlist,
     *        <tt>false</tt> otherwise.
     */
    public void setVarList(final boolean flag) {
        this.isVarList = flag;
    }

    /**
     * Returns if this <tt>Conjunction</tt> is a varList.
     *
     * @return <tt>true</tt> if this <tt>Conjunction</tt> is a varList,
     *         <tt>false</tt> otherwise.
     */
    public boolean isVarList() {
        return isVarList;
    }

    /**
     * Returns all the literals of the <tt>Conjuction</tt> except
     * the first one.
     *
     * @return all the literals of the <tt>Conjuction</tt> except
     *         the first one.
     */
    public Conjunction cdr() {
        final Conjunction cdr = (Conjunction) this.clone();
        if (!super.isEmpty()) {
            cdr.remove(0);
        }
        return cdr;
    }

    /**
     * Returns the first <tt>Literal</tt> in this <tt>Conjunction</tt>.
     *
     * @return the first <tt>Literal</tt> in this <tt>conjunction</tt> or
     *         <tt>null</tt> if this <tt>Conjunction</tt> is empty
     */
    public Literal car() {
        return super.isEmpty() ? null : super.get(0);
    }

    /**
     * Returns a new <tt>Conjunction</tt> such that the variables
     * appearing in the <tt>Substitution</tt> are replaced with the
     * corresponding <tt>Term</tt> of the <tt>Substitution</tt>.
     *
     * @param alpha the <tt>Substitution</tt> to apply.
     * @param replaceVar flag to indicate if the <tt>Substitution</tt> allows
     *        to instantiate <tt>Variable</tt> s not in the
     *        <tt>Substitution</tt> by a <tt>Constant</tt> with the
     *        <tt>Variable</tt> name.
     * @return a substituted copy of the <tt>Conjunction</tt>.
     * @exception SubstitutionException if an error occur during substitution.
     */
    public Object applySubstitution(final Substitution alpha,
            final boolean replaceVar) throws SubstitutionException {
        assert alpha != null : "parameter <alpha> == null";
        final Conjunction c = new Conjunction();
        if (this.isVarList()) {
            final Literal literal = (Literal) this.get(0);
            final Variable variable = (Variable) literal.get(0);
            final Function function = (Function) alpha.instance(variable);
            Term sf = (Term) function.applySubstitution(alpha, replaceVar);
            while (!sf.getSymbol().equals("nil")) {
                final Term elt = ((Function) sf).get(0);
                final Literal l = new Literal(elt.getSymbol());
                if (elt instanceof Function) {
                    final Function f = (Function) elt;
                    for (Term t : f) {
                        l.add(t);
                    }
                }
                c.add(l);
                sf = ((Function) sf).get(1);
            }
        } else {
            for (Literal literal : this) {
                final Literal sl = (Literal) literal.applySubstitution(
                        alpha, replaceVar);
                c.add(sl);
            }
        }
        return c;
    }

    /**
     * Returns a new <tt>Conjunction</tt> such that the variables
     * appearing in the <tt>Substitution</tt> are replaced with the
     * corresponding <tt>Term</tt> of the <tt>Substitution</tt>.
     *
     * @param alpha the <tt>Substitution</tt> to apply.
     * @return a substituted copy of the <tt>Conjunction</tt>.
     * @exception SubstitutionException if an error occur during substitution.
     */
    public Object applySubstitution(final Substitution alpha)
            throws SubstitutionException {
        return this.applySubstitution(alpha, false);
    }

    /**
     * Returns if this <tt>Conjunction</tt> is equal to an <tt>Object</tt>.
     *
     * @param obj the <tt>Object</tt> to be compared.
     * @return <tt>true</tt> the specific <tt>Object</tt> is equal to this
     *         <tt>Conjunction</tt>,<tt>false</tt> otherwise./
     */
    public boolean equals(final Object obj) {
        if (obj instanceof Conjunction) {
            final Conjunction cmp = (Conjunction) obj;
            if (cmp.getName().equals(this.getName())
                    && cmp.getLabel().equals(this.getLabel())
                    && cmp.isVarList() == this.isVarList()) {
                return super.equals(cmp);
            }
        }
        return false;
    }

    /**
     * Returns the hash code value of this <tt>Conjunction</tt>.
     *
     * @return the hash code value.
     */
    public int hashCode() {
        int hash = this.getName().hashCode() + this.getLabel().hashCode();
        for (Literal l : this) {
            hash += l.hashCode();
        }
        return this.isVarList() ? hash++ : hash;
    }

    /**
     * Returns a deep copy of this <tt>Conjunction</tt>.
     *
     * @return a deep copy of this <tt>Conjunction</tt>.
     */
    public Object clone() {
        final Conjunction copy = new Conjunction();
        copy.setName(this.getName());
        copy.setLabel(this.getLabel());
        copy.setVarList(this.isVarList);
        for (Literal l : this) {
            copy.add((Literal) l.clone());
        }
        return copy;
    }

    /**
     * Standardize the variable name contained in this <tt>Conjunction</tt>.
     *
     * @return a copy of this <tt>Conjunction</tt> standardized.
     * @see core.planner.Standardizable#standardize()
     */
    public Object standardize() {
        final Conjunction copy = new Conjunction();
        copy.setName(this.getName());
        copy.setLabel(this.getLabel());
        copy.setVarList(this.isVarList);
        for (Literal l : this) {
            copy.add((Literal) l.standardize());
        }
        return copy;
    }

    /**
     * Returns a <tt>String</tt> representation of this <tt>Conjunction</tt>.
     *
     * @return a <tt>String</tt> representation of this <tt>Conjunction</tt>.
     */
    public String toString() {
        final StringBuilder str = new StringBuilder();
        if (super.isEmpty()) {
            str.append("()");
        } else if (isVarList()) {
            final Literal literal = super.get(0);
            final Variable variable = (Variable) literal.get(0);
            str.append("(");
            str.append(variable.toString());
            str.append(")");
        } else {
            str.append("(");
            for (Literal l : this) {
                str.append(l.toString());
            }
            str.append(" )  ");
        }
        return str.toString();
    }

}
