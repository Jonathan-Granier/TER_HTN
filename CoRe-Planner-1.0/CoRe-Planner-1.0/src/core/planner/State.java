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

import java.util.LinkedHashSet;
import java.util.List;

/**
 * The <code>State</code> class implements a <tt>State</tt> of the world.
 *
 * @author Pellier Damien
 * @version 1.0, 14/01/03
 */
public final class State extends LinkedHashSet<Literal> implements Cloneable {

    /**
     * Comment for <code>serialVersionUID</code>.
     */
    private static final long serialVersionUID = 3690756193954378289L;

    /**
     * Create an empty <tt>State</tt>.
     */
    public State() {
        super();
    }

    /**
     * Tests if some conditions can be infered from this <tt>State</tt> and
     * <tt>AxiomSet</tt> modulo a <tt>Substitution</tt> alpha.
     *
     * @param decomposition the <tt>Decomposition</tt> to be tested.
     * @param alpha the previously computed <tt>Substitution</tt>.
     * @param axioms the <tt>AxiomSet</tt>.
     * @param negation <tt>true</tt> to indicate that the <tt>Problem</tt>
     *        use negation facts.
     * @return If some conditions can be infered, it returns all of the
     *         matching <tt>Substitution</tt>s else it returns an empty
     *         <tt>Substitutionlist</tt>.
     */
    public SubstitutionList satisfiesAll(
            final Decomposition decomposition,
            final Substitution alpha,
            final AxiomSet axioms,
            final boolean negation) {

        assert decomposition != null : "parameter <decomposition> == null";
        assert alpha != null : "parameter <alpha> == null";
        assert axioms != null : "parameter <axioms> == null";

        final boolean all = !decomposition.isFirst();
        final SubstitutionList answers = new SubstitutionList();
        final SubstitutionList satisfiers = TheoremProver.findSubstitutions(
                decomposition.getPreconditions(), this, alpha, axioms, all,
                negation);
        for (int i = 0; i < satisfiers.size(); i++) {
            final Substitution beta = satisfiers.get(i);
            beta.compose((Substitution) alpha.clone());
            answers.add(beta);
        }
        return answers;

    }

    /**
     * It searches all <tt>Atom</tt> s in this matching a <tt>Literal</tt>
     * modulo the <tt>Substitution</tt> alpha.
     *
     * @param literal the matching <tt>Atom</tt>.
     * @param alpha the <tt>Substitution</tt>.
     * @param negation <tt>true</tt> to unify using discriminent negation.
     * @return If such an <tt>Atom</tt> is found, the matching
     *         <tt>Substitution</tt> s are returned, otherwise, it returns an
     *         empty <tt>SubstitutionList</tt>.
     */
    public SubstitutionList satisfies(
            final Literal literal,
            final Substitution alpha,
            final boolean negation) {

        assert literal != null : "parameter <literal> == null";
        assert alpha != null : "parameter <alpha> == null";

        final SubstitutionList satifiers = new SubstitutionList();
        for (Literal l : this) {
            final Substitution substitution = literal.unify(l, alpha);
            if (substitution != null) {
                satifiers.add(substitution);
            }
        }
        return satifiers;
    }

    /**
     * Returns the <tt>State</tt> generated after the application of a
     * specific <tt>Operator</tt>.
     *
     * @param operator the <tt>Operator</tt> to apply.
     * @param alpha the current <tt>Substitution</tt> for <tt>Operator</tt>.
     * @param assumptions the list of assumption to add.
     * @return the <tt>State</tt> modified.
     */
    public State applyOperator(
            final Operator operator,
            final Substitution alpha,
            final List<Assumption> assumptions) {

        assert operator != null : "parameter <operator> == null";
        assert alpha != null : "parameter <alpha> == null";
        assert assumptions != null : "parameter <assumptions> == null";

        // Udapte the state with the assumptions done
        final State result = update(assumptions);

        // Delete the effects
        final Conjunction opDelEffect = operator.getDelEffect();
        final Conjunction delEffect =
            (Conjunction) opDelEffect.applySubstitution(alpha);
        result.removeAll(delEffect);

        // Add the effects
        final Conjunction opAddEffect = operator.getAddEffect();
        final Conjunction addEffect =
            (Conjunction) opAddEffect.applySubstitution(alpha);
        result.addAll(addEffect);

        Logger.log(3, "Matching Operator : (" + ":Operator "
                + operator.getHead().applySubstitution(alpha)
                + "\n-: " + delEffect + "\n+: " + addEffect + "\n?: "
                + assumptions + "\n)");

        Logger.log(4, "Resulting state : \n" + result
                + "\n--------------------------");

        return result;
    }

    /**
     * Returns the <tt>State</tt> generated after the application of a
     * specific <tt>Method</tt>.
     *
     * @param method the <tt>Method</tt> to apply.
     * @param alpha the current <tt>Substitution</tt> for <tt>Method</tt>.
     * @param decomposition the <tt>Decomposition</tt> of the
     *        <tt>Method</tt>to be applied.
     * @param assumptions the list of assumption to add.
     * @return the <tt>State</tt> modified.
     */
    public State applyMethod(
            final Method method,
            final Substitution alpha,
            final Decomposition decomposition,
            final List<Assumption> assumptions) {

        assert method != null : "parameter <method> == null";
        assert alpha != null : "parameter <alpha> == null";
        assert decomposition != null : "parameter <decomposition> == null";
        assert assumptions != null : "parameter <assumptions> == null";

        // Udapte the state with the assumptions done
        final State result = update(assumptions);

        Logger.log(3, "Matching Method : ("
                + ":Method "
                + method.getHead().applySubstitution(alpha)
                + " "
                + decomposition.getName()
                + "\nt: "
                + decomposition.getTasks().applySubstitution(alpha).toString()
                + "\n?: " + assumptions
                + "\n)");

        Logger.log(4, "Resulting state : \n" + result
                + "\n--------------------------");

        return result;
    }

    /**
     * Return a deep copy of this <tt>State</tt>.
     *
     * @return a deep copy of this <tt>State</tt>.
     */
    public Object clone() {
        final State copy = new State();
        for (Literal l : this) {
            copy.add((Literal) l.clone());
        }
        return copy;
    }

    /**
     * Update the <tt>State</tt> according to a set of assumptions.
     *
     * @param assumptions the list of assumptions done.
     * @return a copy of the state updated.
     */
    private State update(final List<Assumption> assumptions) {
        assert assumptions != null : "parameter <assumptions> == null";
        final State state = (State) this.clone();
        for (Assumption a : assumptions) {
            state.add(a.getLiteral());
        }
        return state;
    }

}
