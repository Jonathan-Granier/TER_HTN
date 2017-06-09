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
 * The <code>Decomposition</code> class implements a decomposition of method.
 * A Decomposition has the form: (C T). C (ifPart) is a conjunction and T
 * (thenPart) is a list of tasks.
 *
 * @author Pellier Damien
 * @version 1.0, 14/01/03
 */
public final class Decomposition implements Cloneable, Substituable,
         Standardizable {

    /**
     * Flag indicating if this <tt>Decomposition</tt> must be first evaluated.
     */
    private boolean isFirst;

    /**
     * The <tt>Conjunction</tt> part of the <tt>Decomposition</tt>.
     */
    private Conjunction preconditions;

    /**
     * The task list of the <tt>Decomposition</tt>.
     */
    private TaskList tasks;

    /**
     * The name of the <tt>Decomposition</tt>.
     */
    private String name;

    /**
     * Create an empty <tt>Decomposition</tt>.
     */
    public Decomposition() {
        super();
    }

    /**
     * Sets the preconditions of this <tt>Decomposition</tt>.
     *
     * @param p the preconditions of this <tt>Decomposition</tt>.
     */
    public void setPreconditions(final Conjunction p) {
        assert p != null : "Assertion: parameter <p> == null";
        this.preconditions = p;
    }

    /**
     * Returns the preconditions of this <tt>Decomposition</tt>.
     *
     * @return the preconditions of this <tt>Decomposition</tt>.
     */
    public Conjunction getPreconditions() {
        return this.preconditions;
    }

    /**
     * Sets the <tt>TaskList</tt> of this <tt>Decomposition</tt>.
     *
     * @param t the <tt>TaskList</tt> of this <tt>Decomposition</tt>.
     */
    public void setTasks(final TaskList t) {
        assert t != null : "Assertion: parameter <t> == null";
        this.tasks = t;
    }

    /**
     * Returns the <tt>TaskList</tt> of this <tt>Decomposition</tt>.
     *
     * @return the <tt>TaskList</tt> of this <tt>Decomposition</tt>.
     */
    public TaskList getTasks() {
        return this.tasks;
    }

    /**
     * Sets the name of this <tt>Decomposition</tt>.
     *
     * @param n the name to set.
     */
    public void setName(final String n) {
        assert n != null : "Assertion: parameter <n> == null";
        this.name = n;
    }

    /**
     * Returns the name of this <tt>Decomposition</tt>.
     *
     * @return the name of this <tt>Decomposition</tt>.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the flag first to indicate that is <tt>Decomposition</tt> must be
     * firts explored by the Planner algorithm.
     *
     * @param flag the value of the flag first.
     */
    public void setFirst(final boolean flag) {
        this.isFirst = flag;
    }

    /**
     * Returns if this <tt>Decomposition</tt> is a first
     * <tt>Decomposition</tt>. That means if this <tt>Decomposition</tt>
     * must be first explored by the Planner algorithm.
     *
     * @return <tt>true</tt> if this <tt>Decomposition</tt> is first,
     *         <tt>false</tt> otherwise.
     */
    public boolean isFirst() {
        return this.isFirst;
    }

    /**
     * Returns a new <tt>Decomposition</tt> such that the variables
     * appearing in the <tt>Substitution</tt> are replaced with the
     * corresponding <tt>Term</tt> s of the <tt>Substitution</tt>.
     *
     * @param alpha the <tt>Substitution</tt> to apply.
     * @return a substituted copy of the <tt>Decomposition</tt>.
     * @exception SubstitutionException if an error occur during substitution.
     */
    public Object applySubstitution(final Substitution alpha)
            throws SubstitutionException {
        return this.applySubstitution(alpha, false);
    }

    /**
     * Returns a new <tt>Decomposition</tt> such that the variables
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
        final Decomposition sd = new Decomposition();
        sd.setName(this.getName());
        sd.setFirst(this.isFirst());
        final Conjunction p = this.getPreconditions();
        sd.setPreconditions((Conjunction) p.applySubstitution(
                alpha, replaceVar));
        final TaskList t = this.getTasks();
        sd.setTasks((TaskList) t.applySubstitution(alpha, replaceVar));
        return sd;
    }
    /**
     * Returns if this <tt>Decomposition</tt> is equal to an <tt>Object</tt>.
     *
     * @param obj the <tt>Object</tt> to be compared.
     * @return <tt>true</tt> the specific <tt>Object</tt> is equal to this
     *         <tt>Decompostion</tt>,<tt>false</tt> otherwise.
     */
    public boolean equals(final Object obj) {
        if (obj instanceof Decomposition) {
            final Decomposition cmp = (Decomposition) obj;
            return cmp.isFirst() == this.isFirst()
                && cmp.getName().equals(this.getName())
                && cmp.getPreconditions().equals(this.getPreconditions())
                && cmp.getTasks().equals(this.getTasks());
        }
        return false;
    }

    /**
     * Returns the hash code value of this <tt>Decomposition</tt>.
     *
     * @return the has code associated to this <tt>Decomposition</tt>.
     */
    public int hashCode() {
        int hash = this.getName().hashCode()
            + this.getPreconditions().hashCode()
            + this.getTasks().hashCode();
        return this.isFirst() ? hash++ : hash;
    }

    /**
     * Return a deep copy of this <tt>Decomposition</tt>.
     *
     * @return a deep copy of this <tt>Decomposition</tt>.
     */
    public Object clone() {
        final Decomposition copy = new Decomposition();
        copy.setFirst(this.isFirst());
        copy.setName(new String(this.getName()));
        copy.setPreconditions((Conjunction) this.getPreconditions().clone());
        copy.setTasks((TaskList) this.getTasks().clone());
        return copy;
    }

    /**
     * Standardize the <tt>Variable</tt> name contained in this
     * <tt>Decomposition</tt>.
     *
     * @return a standardized copy of this <tt>Decomposition</tt>.
     * @see core.planner.Standardizable#standardize()
     */
    public Object standardize() {
        final Decomposition copy = new Decomposition();
        copy.setFirst(this.isFirst());
        copy.setName(new String(this.getName()));
        copy.setPreconditions(
                (Conjunction) this.getPreconditions().standardize());
        copy.setTasks((TaskList) this.getTasks().standardize());
        return copy;
    }

    /**
     * Returns a <tt>String</tt> representation of this <tt>Decomposition</tt>.
     *
     * @return a <tt>String</tt> representation of this <tt>Decomposition</tt>.
     */
    public String toString() {
        final StringBuilder str = new StringBuilder();
        str.append("\nName : ");
        str.append(name);
        str.append("\n");
        str.append(preconditions.toString());
        str.append("\n");
        str.append(tasks.toString());
        return str.toString();
    }

}
