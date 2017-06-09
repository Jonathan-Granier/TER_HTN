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
 * The <code>Problem</code> class implements a planning problem. This class
 * stores the initial state and the task list to be accomplished.
 *
 * @author Pellier Damien
 * @version 1.0, 14/01/03
 */
public final class Problem {

    /**
     * The name of the planing <tt>Problem</tt>.
     */
    private String name;

    /**
     * The name of the <tt>Domain</tt> this planning <tt>Problem</tt> belong
     * to.
     */
    private String domainName;

    /**
     * The initial <tt>State</tt>.
     */
    private State state;

    /**
     * The <tt>TaskList</tt> to be accomplished.
     */
    private TaskList tasks;

    /**
     * Create an empty <tt>Problem</tt>.
     */
    public Problem() {
        this(new String());
    }

    /**
     * Create a <tt>Problem</tt>.
     *
     * @param n the name of the <tt>Problem</tt>.
     */
    public Problem(final String n) {
        this(n, new String());
    }

    /**
     * Create a <tt>Problem</tt>.
     *
     * @param n the name of the <tt>Problem</tt>.
     * @param dn the name of the <tt>Domain</tt> attached to this
     *  <tt>Problem</tt>.
     */
    public Problem(final String n, final String dn) {
        this.setName(n);
        this.setDomainName(dn);
        this.state = new State();
        this.tasks = new TaskList();
    }

    /**
     * Sets the name of this planning <tt>Problem</tt>.
     *
     * @param n the name of this planning <tt>Problem</tt>.
     */
    public void setName(final String n) {
        assert n != null : "Assertion: parameter <n> == null";
        this.name = n;
    }

    /**
     * Returns the name of this planning <tt>Problem</tt>.
     *
     * @return the name of this planning <tt>Problem</tt>.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the name of the planning <tt>Domain</tt> belong to.
     *
     * @param dn the name of the planning <tt>Domain</tt> belong to.
     */
    public void setDomainName(final String dn) {
        assert dn != null : "Assertion: parameter <dn> == null";
        this.domainName = dn;
    }

    /**
     * Returns the name of the planning <tt>Domain</tt> belong to.
     *
     * @return the name of the planning <tt>Domain</tt> belong to.
     */
    public String getDomainName() {
        return this.domainName;
    }

    /**
     * Sets the initial <tt>State</tt> of this planning <tt>Problem</tt>.
     *
     * @param s the initial <tt>State</tt>.
     */
    public void setState(final State s) {
        assert s != null : "Assertion: parameter <s> == null";
        this.state = s;
    }

    /**
     * Returns the <tt>State</tt> of this planning <tt>Problem</tt>.
     *
     * @return the <tt>State</tt> of this planning <tt>Problem</tt>.
     */
    public State getState() {
        return this.state;
    }

    /**
     * Sets the <tt>TaskList</tt> of this planning <tt>Problem</tt>.
     *
     * @param tl the <tt>TaskList</tt> to be accomplised.
     */
    public void setTasks(final TaskList tl) {
        assert tl != null : "Assertion: parameter <tl> == null";
        this.tasks = tl;
    }

    /**
     * Returns the <tt>TaskList</tt> to be accomplished.
     *
     * @return the <tt>TaskList</tt> to be accomplished.
     */
    public TaskList getTasks() {
        return this.tasks;
    }

    /**
     * Returns if this <tt>Problem</tt> is equal to an <tt>Object</tt>.
     * <br>
     * <tt>p1.equals(p2)</tt> is equivalent to
     * <tt>p1.getName().equals(p2.getName())</tt>.
     *
     * @param obj the <tt>Object</tt> to be compared.
     * @return <tt>true</tt> the specific <tt>Object</tt> is equal to this
     *         <tt>Problem</tt>,<tt>false</tt> otherwise.
     */
    public boolean equals(final Object obj) {
        if (obj instanceof Problem) {
            final Problem cmp = (Problem) obj;
            return cmp.getName().equals(this.getName());
        }
        return false;
    }

    /**
     * Returns the hash code value of this <tt>Problem</tt>.
     *
     * @return the hash code value.
     */
    public int hashCode() {
        return this.getName().hashCode();
    }

    /**
     * Returns a <tt>String</tt> representation of this planning
     * <tt>Problem</tt>.
     *
     * @return a <tt>String</tt> representation of this planning
     *         <tt>Problem</tt>.
     */
    public String toString() {
        final StringBuilder str = new StringBuilder();
        str.append("(make-problem ");
        str.append(name);
        str.append(" ");
        str.append(domainName);
        str.append("\n");
        str.append(state.toString());
        str.append(tasks.toString());
        str.append(" )\n");
        return str.toString();
    }

}
