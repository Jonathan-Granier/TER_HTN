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
 * Implements the usefull method to manage <tt>Term</tt>s,<tt>Atom</tt> s
 * and <tt>Substitution</tt>s.
 *
 * @author Damien Pellier
 * @version 1.0, 14/01/03
 */
public final class TheoremProver {

    /**
     * Create a new <tt>TheoremProver</tt>.
     * <br>
     * This constructor is never used.
     */
    private TheoremProver() { }

    /**
     * Finds the possible <tt>Substitution</tt> s for a task.
     *
     * @param conjunction the <tt>Conjunction</tt> of <tt>Literal</tt>s.
     * @param state the current <tt>State</tt> of the world.
     * @param alpha the previous <tt>Substitution</tt> done.
     * @param axioms the <tt> AxiomSet</tt>.
     * @param all the flag indicating that all <tt>Substitution</tt> s must be
     *        found.
     * @param negation the flag indicating that negation are used to define
     *        <tt>Problem</tt>.
     * @return the possible <tt>Substitution</tt> s for the preconditions of a
     *         task.
     */
    public static SubstitutionList findSubstitutions(
            final Conjunction conjunction,
            final State state,
            final Substitution alpha,
            final AxiomSet axioms,
            final boolean all,
            final boolean negation) {

        assert conjunction != null : "parameter <conjunction> == null";
        assert state != null : "parameter <state> == null";
        assert alpha != null : "parameter <alpha> == null";
        assert axioms != null : "parameter <axioms> == null";

        final Conjunction conjunctionCopy = (Conjunction) conjunction.clone();
        final SubstitutionList substitutions = recurciveFindSubstitutions(
                conjunction, state, alpha, axioms, all, negation);
        return substitutions;
    }

    /**
     * Finds the possible <tt>Substitution</tt> s for a task.
     *
     * @param conjunction the <tt>Conjunction</tt> of <tt>Literal</tt>s.
     * @param state the current <tt>State</tt> of the world.
     * @param alpha the previous <tt>Substitution</tt> done.
     * @param axioms the <tt>AxiomSet</tt>.
     * @param all the flag indicating that all <tt>Substitution</tt> s must be
     *        found.
     * @param negation the flag indicating that negation are used to define
     *        <tt>Problem</tt>.
     * @return the possible <tt>Substitution</tt> s for the preconditions of a
     *         task.
     */
    private static SubstitutionList recurciveFindSubstitutions(
            final Conjunction conjunction,
            final State state,
            final Substitution alpha,
            final AxiomSet axioms,
            final boolean all,
            final boolean negation) {

        boolean axiomSuccess = false;
        boolean atomSuccess = false;
        SubstitutionList answers = new SubstitutionList();

        // ////////////////////////////////////////////////////////////////////
        // END OF THE RECURSION

        if (conjunction.isEmpty()) {
            answers.add((Substitution) alpha.clone());

            Logger.log(6, "Returning successfully from find-substitutions: ",
                    "No more goals to satisfy");
            Logger.log(8, "Result:\n", answers);

            return answers;
        }

        final Literal l = conjunction.car();
        final Conjunction b = conjunction.cdr();

        Logger.log(6, "Trying finding substitution for ", l);
        Logger.log(8, "Binding variables: \n", alpha);

        // ////////////////////////////////////////////////////////////////////
        // CASE: TREATEMENT OF THE EXPRESSION OF TYPE NOT

        if (l.isNegative()) {

            // Adding to take into negation in the problem definition
            if (negation) {
                final SubstitutionList unifiers =
                    state.satisfies(l, alpha, true);

                if (unifiers.isEmpty()) {
                    Logger.log(8, "Substitution failure for ", l);
                } else {
                    Logger.log(8, "Substitution succeeded for ", l);
                }

                for (int i = 0; i < unifiers.size(); i++) {
                    final Substitution u = unifiers.get(i);
                    final Substitution alphaCu = (Substitution) alpha.clone();
                    alphaCu.compose(u);
                    final SubstitutionList substitutions =
                        recurciveFindSubstitutions(b, state, alphaCu, axioms,
                                all, negation);
                    for (int j = 0; j < substitutions.size(); j++) {
                        final Substitution v = substitutions.get(j);
                        final Substitution uCv = (Substitution) u.clone();
                        uCv.compose(v);
                        answers.add(uCv);
                        if (answers.size() == 1 && !all) {
                            return answers;
                        }
                    }
                }
            }
            // End add

            final Literal neg = (Literal) l.clone();
            neg.setNegative(false);
            final Conjunction e = new Conjunction();
            e.add(neg);
            final SubstitutionList satisfiers =
                recurciveFindSubstitutions(e, state, alpha, axioms, false,
                        negation);

            if (satisfiers.isEmpty()) {
                Logger.log(8, "Substitution failure for ", l);
            } else {
                Logger.log(8, "Substitution succeeded for ", l);
            }

            if (satisfiers.isEmpty()) {
                answers = recurciveFindSubstitutions(b, state, alpha, axioms,
                        all, negation);
                return answers;
            } else {

                // Add to treat assumption
                if (l.isHypothetical()) {
                    alpha.addBindingLiteral(l);
                    Logger.log(6, "Assumption done: ", l);
                    answers = recurciveFindSubstitutions(b, state, alpha,
                            axioms, all, negation);
                    return answers;
                }
                // End add

                answers = new SubstitutionList();
                Logger.log(8, "Substitution failure for ", l);
                return answers;
            }
        } else if (l.isEval()) {

            // ////////////////////////////////////////////////////////////////
            // CASE: TREATEMENT OF THE EXPRESSION OF TYPE CALL

            final Function callTerm = (Function) l.get(0);
            final Function subCallTerm =
                (Function) callTerm.applySubstitution(alpha);
            if (subCallTerm.isGround()) {
                final Constant evaluation =
                    Eval.evaluate(subCallTerm.getSymbol(),
                            subCallTerm.get(0), subCallTerm.get(1));
                Logger.log(9, "Evaluation of ", subCallTerm, ": ",
                        evaluation);
                // Evaluation fails
                if (evaluation.getSymbol().equals("nil")) {

                    // Add to treat assumption
                    if (callTerm.isHypothetical()) {
                        alpha.addBindingLiteral(l);
                        Logger.log(6, "Assumption done: ", l);
                        answers = recurciveFindSubstitutions(b, state, alpha,
                                axioms, all, negation);
                        return answers;
                    }
                    // End add

                    Logger.log(8, "Substitution failure for ", l);
                    answers = new SubstitutionList();
                    return answers;
                } else { // Evaluation succeed (evaluation.getSymbol() == "()")
                    Logger.log(8, "Substitution succeeded for ", l);
                    answers = recurciveFindSubstitutions(b, state, alpha,
                            axioms, all, negation);
                    return answers;
                }
            }

            // Add to treat assumption
            if (callTerm.isHypothetical()) {
                alpha.addBindingLiteral(l);
                Logger.log(6, "Assumption done: ", l);
                answers = recurciveFindSubstitutions(b, state, alpha, axioms,
                        all, negation);
                return answers;
            }
            // End add

            return answers;
        }

        // ////////////////////////////////////////////////////////////////////
        // AXIOM TREATEMENT

        Logger.log(6, "Try to apply axioms");
        for (Axiom a : axioms) {
            Variable.incrementStandardizationCounter();
            final Axiom axiom = (Axiom) a.standardize();
            final Substitution u =
                axiom.getHead().unify(l, alpha);
            // Axioms not match
            if (u == null) {
                Logger.log(6, "Axiom ", axiom.getHead(), " not matched");
                Variable.decrementStandardizationCounter();
            } else { // Axiom match
                Logger.log(7, "Axiom head ", axiom.getHead(), " matching");
                final ConjunctionList tailAxiom =
                    (ConjunctionList) axiom.getTail().clone();
                for (int j = 0; j < tailAxiom.size(); j++) {
                    final Conjunction d = tailAxiom.get(j);
                    final Conjunction dAb = new Conjunction();
                    dAb.addAll(d);
                    dAb.addAll(b);
                    final Substitution alphaCu = (Substitution) alpha.clone();
                    alphaCu.compose(u);
                    final SubstitutionList subtitutions =
                        recurciveFindSubstitutions(dAb, state, alphaCu, axioms,
                                all, negation);

                    if (subtitutions.isEmpty()) {
                        Logger.log(8, "Substitution failure for ", l);
                    } else {
                        Logger.log(8, "Substitution succeeded for ", l);
                        axiomSuccess = true;
                    }

                    if (!subtitutions.isEmpty()) {
                        for (int k = 0; k < subtitutions.size(); k++) {
                            final Substitution v =
                                subtitutions.get(k);
                            final Substitution uCv = (Substitution) u.clone();
                            uCv.compose(v);
                            answers.add(uCv);
                            if (answers.size() == 0 && !all) {
                                return answers;
                            }
                        }
                        break;
                    }
                }
            }
        }

        // ////////////////////////////////////////////////////////////////////
        // ATOM TREATEMENT
        final SubstitutionList unifiers = state.satisfies(l, alpha, negation);

        if (unifiers.isEmpty()) {
            Logger.log(8, "Substitution failure for ", l);
        } else {
            Logger.log(8, "Substitution succeeded for ", l);
            atomSuccess = true;
        }

        for (Substitution u : unifiers) {
            final Substitution alphaCu = (Substitution) alpha.clone();
            alphaCu.compose(u);
            final SubstitutionList substitutions = recurciveFindSubstitutions(
                    b, state, alphaCu, axioms, all, negation);
            for (Substitution v : substitutions) {
                final Substitution uCv = (Substitution) u.clone();
                uCv.compose(v);
                answers.add(uCv);
                if (answers.size() == 1 && !all) {
                    return answers;
                }
            }
        }

        // Add to treat assumption
        if (l.isHypothetical()) {
            if (axiomSuccess || atomSuccess) {
                return answers;
            } else {
                alpha.addBindingLiteral(l);
                Logger.log(6, "Assumption done: ", l);
                answers = recurciveFindSubstitutions(b, state, alpha, axioms,
                        all, negation);
            }
        }
        return answers;
    }

}
