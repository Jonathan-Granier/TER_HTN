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
 * Defines the description of an agent in terms of skills and beliefs. This
 * class is used in a multi-agent context.
 *
 * @author Damien Pellier
 * @version 1.0, 14/01/03
 */
public final class AgentDescription {

    /**
     * The skills of the agent.
     */
    private Domain domain;

    /**
     * The beliefs list of the agent.
     */
    private ProblemSet problems;

    /**
     * Create an empty <tt>AgentDescription</tt>.
     */
    public AgentDescription() {
        this(new Domain());
    }

    /**
     * Create a new <tt>AgentDescription</tt>.
     *
     * @param d the <tt>Domain</tt> of the agent.
     */
    public AgentDescription(final Domain d) {
        this.setDomain(d);
        this.problems = new ProblemSet();
    }

    /**
     * Returns the name of the agent.
     *
     * @return the name.
     */
    public String getName() {
        return this.domain.getName();
    }

    /**
     * Returns the <tt>Domain</tt> of the agent.
     *
     * @return the domain.
     */
    public Domain getDomain() {
        return this.domain;
    }

    /**
     * Sets the <tt>Domain</tt> of the agents.
     *
     * @param d the <tt>Domain</tt> to set.
     */
    public void setDomain(final Domain d) {
        assert d != null : "Assertion: parameter <d> == null";
        this.domain = d;
    }

    /**
     * Add a <tt>Problem</tt> to the <tt>AgentDescription</tt>.
     *
     * @param p the <tt>Problem</tt> to add.
     */
    public void addProblem(final Problem p) {
        assert p != null : "Assertion: parameter <p> == null";
        this.problems.add(p);
    }

    /**
     * Returns the number of <tt>Problem</tt> s contained in this
     * <tt>AgentDescription</tt>.
     *
     * @return the number of <tt>Problem</tt> s contained in this
     *         <tt>AgentDescription</tt>.
     */
    public int getNumberOfProblems() {
        return this.problems.size();
    }

    /**
     * Returns if this <tt>AgentDescription</tt> is equal to another
     * <tt>Object</tt>.
     *
     * @param o the <tt>Object</tt> to be compared.
     * @return <tt>true</tt> if the specific <tt>Object</tt> is equal to
     *         this <tt>AgentDescription</tt>, <tt>false</tt> otherwise.
     */
    public boolean equals(final Object o) {
        if (o instanceof AgentDescription) {
            final AgentDescription cmp = (AgentDescription) o;
            return cmp.getName().equals(this.getName())
                && cmp.getDomain().equals(this.getDomain())
                && cmp.problems.equals(this.problems);
        }
        return false;
    }

    /**
     * Returns the hash code value of this <tt>Literal</tt>.
     *
     * @return the hash code value.
     */
    public int hashCode() {
        return this.getName().hashCode()
            + this.getDomain().hashCode()
            + this.problems.hashCode();
    }

    /**
     * Returns a <tt>String</tt> representation of this
     * <tt>AgentDescription</tt>.
     *
     * @return a <tt>String</tt> representation of this
     *         <tt>AgentDescription</tt>.
     */
    public String toString() {
        final StringBuilder str = new StringBuilder();
        str.append(this.domain.toString());
        str.append("\n");
        for (Problem p : this.problems) {
            str.append(p.toString());
            str.append("\n");
        }
        return str.toString();
    }

}
