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
 * The <code>TaskAtom</code> class implements a task in <tt>Planner</tt>.
 *
 * @author Pellier Damien
 * @version 1.0, 14/01/03
 */
public final class TaskAtom extends Atom {

    /**
     * <code>serialVersionUID</code>.
     */
    private static final long serialVersionUID = 3691038768493048115L;

    /**
     * The primitive flag indicating if the <tt>TaskAtom</tt> represents a
     * primitive task.
     */
    private boolean isPrimitive;

    /**
     * Create an empty pritive <tt>TaskAtom</tt>.
     */
    public TaskAtom() {
        this(new String());
    }

    /**
     * Create an <tt>TaskAtom</tt> with a specific symbol.
     * <br> By default the task is primitive.
     *
     * @param s the symbol of the <tt>TaskAtom</tt> created.
     */
    public TaskAtom(final String s) {
        this(s, true);
    }

    /**
     * Create an <tt>TaskAtom</tt> with a specific symbol.
     *
     * @param s the symbol of the <tt>TaskAtom</tt> created.
     * @param flag indicate if the <tt>TaskAtom</tt> define a primitive task.
     */
    public TaskAtom(final String s, final boolean flag) {
        super(s, false);
        this.setPrimitive(flag);
    }

    /**
     * Returns if this <tt>TaskAtom</tt> is a primitive.
     *
     * @return <tt>true</tt> if this <tt>TaskAtom</tt> is a primitive,
     *         <tt>false</tt> otherwise.
     */
    public boolean isPrimitive() {
        return this.isPrimitive;
    }

    /**
     * Sets the primitive flag of this <tt>TaskAtom</tt>.
     *
     * @param flag <tt>true</tt> if this <tt>TaskAtom</tt> is a primitive,
     *        <tt>false</tt> otherwise.
     */
    public void setPrimitive(final boolean flag) {
        this.isPrimitive = flag;
    }

    /**
     * Returns if this <tt>TaskAtom</tt> is a compound.
     *
     * @return <tt>true</tt> if this <tt>TaskAtom</tt> is a compound,
     *         <tt>false</tt> otherwise.
     */
    public boolean isCompound() {
        return !this.isPrimitive();
    }

    /**
     * Sets the compound flag of this <tt>TaskAtom</tt>.
     *
     * @param flag <tt>true</tt> if this <tt>TaskAtom</tt> is a compound
     *        task, <tt>false</tt> otherwise.
     */
    public void setCompound(final boolean flag) {
        this.setPrimitive(!flag);
    }

    /**
     * Returns the most general unifier of two task atoms. Prior binding
     * variables contained in the <tt>Substitution</tt> parameter
     * are considered.
     *
     * @param a the first <tt>TaskAtom</tt> to unify.
     * @return the most general unifier or <tt>null</tt> if it does not exist.
     *         An empty mgu means that there is no binding <tt>Variable</tt>
     *         between <tt>this</tt> and <tt>a</tt>.
     * @exception UnificationException if <tt>a</tt> is not an instance of
     *            <tt>TaskAtom</tt>.
     */
    public Substitution unify(final Atom a) throws UnificationException {
        return unify(a, new Substitution());
    }
    /**
     * Returns the most general unifier of two task atoms. Prior binding
     * variables contained in the <tt>Substitution</tt> parameter
     * are considered.
     *
     * @param a the first <tt>TaskAtom</tt> to unify.
     * @param alpha the prior binding variables.
     * @return the most general unifier or <tt>null</tt> if it does not exist.
     *         An empty mgu means that there is no binding <tt>Variable</tt>
     *         between <tt>this</tt> and <tt>a</tt>.
     * @exception UnificationException if <tt>a</tt> is not an instance of
     *            <tt>TaskAtom</tt>.
     */
    public Substitution unify(final Atom a, final Substitution alpha)
            throws UnificationException {
        if (a instanceof TaskAtom) {
            final TaskAtom t = (TaskAtom) a;
            if (t.isPrimitive() == this.isPrimitive()) {
                return unifyAtom(a, alpha);
            } else {
                return null;
            }
        } else {
            throw new UnificationException(this.toString()
                    + " " + a.toString());
        }
    }

    /**
     * Returns a new <tt>TaskAtom</tt> such that the variables
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

        final TaskAtom sta = new TaskAtom();
        sta.setSymbol(this.getSymbol());
        sta.setPrimitive(this.isPrimitive());

        for (Term term : this) {
            final Term st = (Term) term.applySubstitution(alpha, replaceVar);
            if (st instanceof Constant) {
                sta.setGround(true);
            } else if (st instanceof Variable) {
               sta.setGround(false);
            } else if (st instanceof Function) {
                final Function f = (Function) st;
                sta.setGround(f.isGround());
            } else {
                throw new SubstitutionException("Substitutions term "
                        + term.toString() + " by " + alpha.toString());
            }
            sta.add(st);
        }
        return sta;
    }

    /**
     * Returns a new <tt>TaskAtom</tt> such that the variables
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
     * Returns if this <tt>TaskAtom</tt> is equals to another <tt>Object</tt>.
     *
     * @param obj the <tt>Object</tt> to be compared.
     * @return <tt>true</tt> if the <tt>Object</tt> to be compared is equal
     *         to this <tt>TaskAtom</tt>.
     */
    public boolean equals(final Object obj) {
        if (obj instanceof TaskAtom) {
            final TaskAtom cmp = (TaskAtom) obj;
            if (cmp.getSymbol().equals(this.getSymbol())
                    && cmp.isPrimitive() == isPrimitive()) {
                return super.equals(cmp);
            }
        }
        return true;
    }

    /**
     * Returns the hash code value of this <tt>TaskAtom</tt>.
     *
     * @return the hash code value.
     */
    public int hashCode() {
        int hash =  this.getSymbol().hashCode();
        for (Term t : this) {
            hash += t.hashCode();
        }
        return this.isPrimitive() ? hash++ : hash;
    }

    /**
     * Returns a deep copy of this <tt>TaskAtom</tt>.
     *
     * @return a deep copy of this <tt>TaskAtom</tt>.
     */
    public Object clone() {
        final TaskAtom copy = new TaskAtom();
        copy.setSymbol(this.getSymbol());
        copy.setGround(this.isGround());
        copy.setPrimitive(this.isPrimitive());
        for (Term t : this) {
            copy.add((Term) t.clone());
        }
        return copy;
    }

    /**
     * Standardize this <tt>TaskAtom</tt>.
     *
     * @return a standardizable copy of this <tt>TaskAtom</tt>.
     */
    public Object standardize() {
        final TaskAtom copy = new TaskAtom();
        copy.setSymbol(this.getSymbol());
        copy.setGround(this.isGround());
        copy.setPrimitive(this.isPrimitive());
        for (Term t : this) {
            copy.add((Term) t.standardize());
        }
        return copy;
    }

    /**
     * Returns a <tt>String</tt> representation of this <tt>LogicalAtom</tt>.
     *
     * @return a <tt>String</tt> representation of this <tt>LogicalAtom</tt>.
     */
    public String toString() {
        final StringBuilder str = new StringBuilder();
        if (this.size() == 0) {
            str.append("(");
            str.append(getSymbol());
            str.append(")");
        } else {
            str.append("(");
            str.append(getSymbol());
            str.append(" ");
            for (Term t : this) {
                str.append(t.toString());
            }
            str.append(" ) ");
        }
        return str.toString();
    }

}
