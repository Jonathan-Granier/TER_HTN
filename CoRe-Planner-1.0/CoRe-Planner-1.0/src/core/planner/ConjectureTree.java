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
import java.util.List;

/**
 * The <code>ConjectureTree</code> class implements the conjecture tree.
 *
 * @author Pellier Damien
 * @version 1.0, 14/01/03
 */
final class ConjectureTree {

    /**
     * The <tt>Domain</tt> of the <tt>ConjectureTree</tt>.
     */
    private Domain domain;

    /**
     * The root <tt>Node</tt>.
     */
    private Node root;

    /**
     * The list of <tt>Node</tt>s.
     */
    private List<Node> nodes;

    /**
     * The assumption window.
     */
    private int window;

    /**
     * Flag indicating that the <tt>Problem</tt> use negative facts.
     */
    private boolean negation;

    /**
     * The list of solution <tt>Node</tt>.
     */
    private List<Node> solutionNodes;

    /**
     * The size max of the assumption window.
     */
    private int windowSize;

    /**
     * The number of <tt>Conjecture</tt> s to find.
     */
    private int nbConjectures;

    /**
     * Create a empty <tt>ConjectureTree</tt>.
     *
     * @param d the <tt>Domain</tt> associated to this <tt>ConjectureTree</tt>.
     * @param p the <tt>Problem</tt> associated to this
     *        <tt>ConjectureTree</tt>.
     */
    ConjectureTree(final Domain d, final Problem p) {
        this.domain = d;
        this.root = Node.createRootNode(p.getState(), p.getTasks());
        this.nodes = new ArrayList<Node>();
        this.solutionNodes = new ArrayList<Node>();
        nodes.add(root);
    }

    /**
     * Returns the root <tt>Node</tt> of this <tt>ConjectureTree</tt>.
     *
     * @return the root <tt>Node</tt> of this <tt>ConjectureTree</tt>.
     */
    Node getRootNode() {
        return this.root;
    }

    /**
     * Extract the <tt>Conjecture</tt> s from the <tt>ConjectureTree</tt>.
     *
     * @return the <tt>Conjecture</tt> s extracted or <tt>null</tt> if no
     *         solution is found.
     */
    Conjecture[] extractConjectures() {
        if (this.solutionNodes.isEmpty()) {
            return null;
        }
        // Extract the conjectures from the conjecture tree.
        final List<Conjecture> extractedConjectures =
            new ArrayList<Conjecture>();
        for (Node solution : solutionNodes) {
            final Conjecture c = extractConjecture(solution);
            extractedConjectures.add(c);
        }
        // Remove included conjectures
        final List<Conjecture> completedConjectures =
            new ArrayList<Conjecture>();
        for (int i = 0; i < extractedConjectures.size(); i++) {
            final Conjecture ci = (Conjecture) extractedConjectures.get(i);
            boolean include = false;
            for (int j = 0; j < extractedConjectures.size(); j++) {
                final Conjecture cj = (Conjecture) extractedConjectures.get(j);
                if (i != j && cj.includes(ci)) {
                    include = true;
                    break;
                }
            }
            if (!include) {
                completedConjectures.add(ci);
            }
        }
        // Prepare answer
        final Conjecture[] conjectures =
            new Conjecture[extractedConjectures.size()];
        for (int i = 0; i < extractedConjectures.size(); i++) {
            conjectures[i] = (Conjecture) extractedConjectures.get(i);
        }
        return conjectures;
    }

    /**
     * Extract the <tt>Conjecture</tt> s from the <tt>ConjectureTree</tt>.
     *
     * @param sn the solution <tt>Node</tt> from which to extract.
     * @return the <tt>Conjectures</tt> extracted or <tt>null</tt> if no
     *         solution is found.
     */
    private Conjecture extractConjecture(final Node sn) {
        final Conjecture conjecture = new Conjecture();
        final List<Assumption> assumptions = new ArrayList<Assumption>();

        if (sn == null) {
            return null;
        }

        Node node = sn;
        Action lastAction = null;
        while (!node.isRootNode()) {
            if (node.getHead().isPrimitive()) {
                if (assumptions.size() > 0) {
                    lastAction.addAssumptions(assumptions);
                    assumptions.clear();
                }
                Action action = new Action(node.getHead(), node
                        .getAssumptions());
                conjecture.addFirst(action);
                lastAction = action;
            } else {
                assumptions.addAll(node.getAssumptions());
            }
            node = node.getRoot();
        }
        if (assumptions.size() > 0 && lastAction != null) {
            lastAction.addAssumptions(assumptions);
        }
        return conjecture;
    }

    /**
     * Expand recurcively all the <tt>ConjectureTree</tt> from a specific
     * <tt>Node</tt>.
     *
     * @param node the <tt>Node</tt> from which the expansion starts.
     * @param win the assumption window size.
     * @param neg <tt>true</tt> to indicate that the <tt>Problem</tt>
     *        use negation facts.
     */
    void expandAll(final Node node, final int win,
            final boolean neg) {

        this.nbConjectures = Integer.MAX_VALUE;
        this.window = win;
        this.negation = neg;
        this.solutionNodes = recurciveExpand(node, true);
    }

    /**
     * Expand recurcively the <tt>ConjectureTree</tt> from a specific
     * <tt>Node</tt>.
     *
     * @param node the <tt>Node</tt> from which the expansion starts.
     * @param nbConj the number of <tt>Conjecture</tt> s to find.
     * @param win the size of the assumptions window.
     * @param neg to indicate that the <tt>Problem</tt> use negative
     *        facts.
     */
    void expand(final Node node, final int nbConj, final int win,
            final boolean neg) {

        this.nbConjectures = nbConj;
        this.window = win;
        this.negation = neg;
        this.solutionNodes = recurciveExpand(node, true);
    }

    /**
     * Expand recurcively the <tt>ConjectureTree</tt> from a specific
     * <tt>Node</tt>.
     *
     * @param all indicate that many <tt>Conjecture</tt> s must be found.
     * @param node the <tt>Node</tt> from which the expansion starts.
     * @return the <tt>ArrayList</tt> of <tt>Node</tt> s expanded.
     */
    private List<Node> recurciveExpand(final Node node, final boolean all) {
        boolean matchOperator = false;
        final boolean matchMethod = false;
        final List<Node> sNodes = new ArrayList<Node>();

        // All the tasks are achieved then the conjecture expansion return.
        if (node.noMoreTask()) {
            Logger.log(2, "Returning successfully from conjecture tree ",
                    "expansion : No more tasks to plan");
            sNodes.add(node);
            Logger.log(5, "Conjecture found:\n", extractConjecture(node));
            // Try to find the other solution if it exists in the same window
            // Node bestNode = getBestNode(this.window);
            // if (bestNode != null) expand(bestNode);
            return sNodes;
        }

        // Otherwise extand the tree swith the first task contains in the node
        final TaskAtom task = node.popTask();
        Logger.log(2, "Searching for a plan: ", task);

        // The primitive task case
        if (task.isPrimitive()) {

            // Look for each operator contained in the domain if it exists some
            // applicable operator
            final OperatorSet operators = domain.getOperators();

            for (Operator o : operators) {
                final Operator operator = (Operator) o.standardize();

                // Test if a specific operator is applicable for the task
                final Substitution substitution =
                    operator.getHead().unify(task);

                 // Case where the operator is applicable to accomplish the task
                 if (substitution != null) {

                     Logger.log(5, "Operator head ", operator.getHead(),
                             " matching");

                     final Operator op = (Operator) operator.standardize();
                     Substitution sub =
                         (Substitution) substitution.standardize();
                     Variable.incrementStandardizationCounter();
                     matchOperator = true;

                     // Compute the possible substitutions for operator
                     // according to the current state
                     final SubstitutionList satisfiers =
                         TheoremProver.findSubstitutions(
                                 operator.getPreconditions(),
                                 node.getState(),
                                 sub,
                                 domain.getAxioms(),
                                 all,
                                 false);

                     if (!satisfiers.isEmpty()) {
                         final double weight = sub.getNumberOfBindingLiterals()
                             + node.getWeight();

                         if (weight > window) {
                             Logger.log(4, "Operator ", operator.getHead(),
                                     " not matched without making assumption");
                             return sNodes;
                         }
                         // Add 04/02/05
                         sub = satisfiers.get(0);
                         // // End
                         final TaskAtom head =
                             (TaskAtom) task.applySubstitution(sub);
                         final List<Assumption> assumptions =
                             computeAssumptions(node.getState(), sub);
                         final State state = node.getState().applyOperator(
                                 operator, sub, assumptions);
                         final TaskList restTasks =
                             (TaskList) node.getTasks().clone();
                         restTasks.remove(0);
                         final Node newNode = new Node(
                                 head,
                                 state,
                                 restTasks,
                                 weight,
                                 node);
                         newNode.setAssumptions(assumptions);
                         node.addChild(newNode);
                         nodes.add(newNode);
                         final List<Node> result =
                             recurciveExpand(newNode, all);
                         if (result.size() >= nbConjectures && all) {
                             return result;
                         }
                         sNodes.addAll(result);
                     }
                 } else {
                     Logger.log(5, "Operator ", operator.getHead(),
                             " not matched");
                 }
             }
             if (!matchOperator) {
                 Logger.error("Conjecture tree expansion fails: no operator "
                         + "found to resolve task " + task);
             }
        } else {
            // The compound task case
            final List decompositionNodes = findDecompositions(task, node);

            for (int i = 0; i < decompositionNodes.size(); i++) {
                final Node dn = (Node) decompositionNodes.get(i);
                if (dn.getWeight() > window) {
                    Logger.log(4, "Decomposition ", dn.getHead(),
                            " not matched without making assumption");
                    return sNodes;
                }
                node.addChild(dn);
                nodes.add(dn);
                final List<Node> result = recurciveExpand(dn, all);
                if (result.size() >= nbConjectures && all) {
                    return result;
                }
                sNodes.addAll(result);
            }
        }
        return sNodes;
    }

    /**
     * Look for each <tt>Method</tt> contained in the <tt>Domain</tt> if it
     * exists some applicable <tt>Method</tt>.
     *
     * @param task the task to execute.
     * @param node the current <tt>Node</tt>.
     * @return the list of <tt>Decomposition</tt> applicable.
     */
    private List findDecompositions(final TaskAtom task, final Node node) {
        final List<Node> answers = new ArrayList<Node>();
        final MethodSet methods = domain.getMethods();

        for (Method method : methods) {

            // Test if a specific method is applicable for the task
            Logger.log(4, "Testing method ", method.getHead());
            final TaskAtom mHead = (TaskAtom) method.getHead().standardize();
            final Substitution substitution = mHead.unify(task);

            // Case where the method is not applicable to accomplish the task
            if (substitution == null) {
                Logger.log(3, "Method ", method.getHead(), " not matched");
            } else {
                // Case where the method is applicable to accomplish the task
                Logger.log(4, "Method head ", method.getHead(), " matching");

                // For each decomposition of the method
                final DecompositionList tail = method.getTail();
                final DecompositionList decompositions =
                    (DecompositionList) tail.standardize();

                for (int j = 0; j < decompositions.size(); j++) {
                    final Decomposition decomposition =
                        decompositions.get(j);
                    Logger.log(5, "Testing decomposition preconditions: ",
                            decomposition.getName());

                    final State state = node.getState();
                    final SubstitutionList satisfiers = state.satisfiesAll(
                            decomposition, substitution, domain.getAxioms(),
                            negation);

                    if (!satisfiers.isEmpty()) {
                        Variable.incrementStandardizationCounter();
                        for (Substitution s : satisfiers) {
                            if (decomposition.getTasks().isEmpty()
                                    && !answers.isEmpty()) {
                                continue;
                            }
                            final TaskList restTasks =
                                (TaskList) node.getTasks().clone();
                            restTasks.remove(0);
                            final List<Assumption> assumptions =
                                computeAssumptions(state, s);
                            final State newState =
                                state.applyMethod(method, s, decomposition,
                                        assumptions);
                            final double weight = s.getNumberOfBindingLiterals()
                                    + node.getWeight();
                            final Node newNode = new Node(
                                    task,
                                    newState,
                                    restTasks,
                                    weight,
                                    node);
                            final TaskList tasks = (TaskList)
                                decomposition.getTasks().applySubstitution(s);
                            newNode.addTasks(tasks);
                            newNode.setFirst(decomposition.isFirst());
                            newNode.setAssumptions(assumptions);
                            answers.add(newNode);
                            if (decomposition.isFirst()) {
                                return answers;
                            }
                        }
                        Logger.log(5, "Decomposition ",
                                decomposition.getName(), " matching");
                    }
                    Logger.log(5, "Decomposition ", decomposition.getName(),
                            " not matched");
                }
            }
        }
        Logger.log(5, "No method found to resolve ", task);
        return answers;
    }

    /**
     * Compute the <tt>Assumption</tt> s that must be done. This method is
     * called at the recursion end of the findSubstitution method to get most
     * of variables instantiation.
     *
     * @param state the <tt>State</tt> from which the assumptions must be
     *        computed.
     * @param alpha the <tt>Substitution</tt> computed containing the
     *        <tt>Assumption</tt> s not instantiated.
     * @return return the <tt>List</tt> of assumption instantiated.
     */
    private List<Assumption> computeAssumptions(final State state,
            final Substitution alpha) {
        final List<Assumption> al = new ArrayList<Assumption>();

        for (Literal a : alpha.getBindingLiterals()) {

            // /////////////////////////////////////////////////////////////
            // Assumption of type constraint violation
            // Note: do not work with negation

            if (a.isEval()) {
                final List<Assumption> cva =
                    computeConstraintViolation(a, alpha);
                al.addAll(cva);
            } else {
                ///////////////////////////////////////////////////////////////
                // Assumption of type predicate
                final Assumption assumption =
                    computePredicateAssumption(a, alpha);
                final Assumption neg = (Assumption) assumption.clone();
                final Literal l = assumption.getLiteral();
                neg.getLiteral().setNegative(!l.isNegative());
                if (state.contains(neg.getLiteral())) {
                    assumption.setType(Assumption.Type.NEGATION_FACT);
                } else {
                    assumption.setType(Assumption.Type.FACT);
                }
                //System.out.println(assumption);
                al.add(assumption);
            }
        }
        //System.out.println((Substitution)alpha.clone());
        return al;
    }

    /**
     * Computes the constraint violation assumptions. Note: do not
     * work with negation and for simple constraint of kind (call boolean-op x
     * y) where y is (call aritmetic-op) || constant).
     *
     * @param a the <tt>Literal</tt> of type call (i.e., a contraint).
     * @param alpha the current <tt>Substitution</tt>.
     * @return the constraint violation assumptions list or
     *         <tt>null</tt> if there are inconsistant.
     */
    private static List<Assumption> computeConstraintViolation(
            final Literal a,
            final Substitution alpha) {

        Literal sa = (Literal) a.applySubstitution(alpha);
        if (!sa.isGround()) {
            sa = (Literal) a.applySubstitution(alpha, true);
        }

        final Function callTerm = (Function) sa.get(0);

        // Compute the list of variables to modify
        final List<Variable> aVar = new ArrayList<Variable>();
        for (Variable var : alpha.getVariables()) {
            if (a.occurs(var)) {
                for (Literal l : alpha.getBindingLiterals()) {
                    if (l.isHypothetical()
                            && !l.equals(a) && l.occurs(var)) {
                        if (!aVar.contains(var)) {
                            aVar.add(var);
                        }
                    }
                }
            }
        }

        // compute the constraints
        final Conjunction constraints = new Conjunction();
        for (int j = 0; j < aVar.size(); j++) {
            final Variable var = (Variable) aVar.get(j);
            final Term instance = alpha.instance(var);
            final Constant varCons = new Constant("#"
                    + var.getSymbol().substring(1, var.getSymbol().length()));
            alpha.compose(var, varCons);
            for (Literal l : alpha.getBindingLiterals()) {
                if (l.isHypothetical() && l.occurs(var)) {
                    final Literal c = (Literal) l.applySubstitution(alpha);
                    // System.out.println("Add constraints : "+c);
                    constraints.add(c);
                }
            }
            alpha.compose(var, callTerm.get(1));
            final Literal ia = new Literal("call");
            final Function fia = new Function("::=");
            fia.setEval(true);
            fia.add(varCons);
            fia.add(instance);
            fia.add(callTerm.get(1));
            ia.add(fia);
            constraints.add(ia);
        }
        // Create the constraint violation assumption
        final Assumption cv = new Assumption();
        cv.setType(Assumption.Type.CONSTAINT_VIOLATION);
        for (int j = 0; j < constraints.size(); j++) {
            final Literal constraint = constraints.get(j);
            if (constraint.isEval()) {
                cv.addConstraint(constraint);
            }
        }
        // Create The list of constraints violations
        final List<Assumption> al = new ArrayList<Assumption>();
        for (int j = 0; j < aVar.size(); j++) {
            final Variable var = (Variable) aVar.get(j);
            for (Literal l : alpha.getBindingLiterals()) {
                if (l.isHypothetical() && !l.equals(a) && l.occurs(var)) {
                    final Assumption newCV = (Assumption) cv.clone();
                    final Literal sl = (Literal) l.applySubstitution(alpha);
                    // System.out.println(alpha);
                    // System.out.println("Add assumption : "+al);
                    newCV.setLiteral(sl);
                    al.add(newCV);
                }
            }
        }

        // Check if predicate assumption already exist if there is remove it
        for (Literal aj : alpha.getBindingLiterals()) {
            for (int k = 0; k < al.size(); k++) {
                final Assumption ak = al.get(k);
                if (ak.getLiteral().getSymbol().equals(aj.getSymbol())
                        && ak.getLiteral().size() == aj.size()) {
                    alpha.removeBindingLiteral(aj);
                }
            }
        }
        return al;
    }

    /**
     * Compute predicate <tt>Assumption</tt>.
     *
     * @param a the <tt>Assumption</tt> to compute.
     * @param alpha the current <tt>Substitution</tt>.
     * @return the <tt>Assumption</tt> computed.
     */
    private static Assumption computePredicateAssumption(
            final Literal a,
            final Substitution alpha) {

        Assumption assumption = null;
        Literal sa = (Literal) a.applySubstitution(alpha);
        if (sa.isGround()) {
            if (sa.isNegative()) {
                final Literal neg = (Literal) sa.clone();
                neg.setNegative(false);
                alpha.removeBindingLiteral(neg);
                // System.out.println("CASE 4");
            }
            // System.out.println("CASE 5"+sa);
            assumption = new Assumption();
            assumption.setLiteral(sa);
        } else {
            sa = (Literal) a.applySubstitution(alpha, true);
            // System.out.println("CASE 6");
            assumption = new Assumption();
            assumption.setLiteral(sa);
        }
        return assumption;
    }

}
