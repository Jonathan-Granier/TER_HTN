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
 * The <code>Node</code> class implements the a node of the ConjectureTree. A
 * node is a tuple (S, T, w) where S is a state, T a task list and w the number
 * of assumption put forward from the root node of the conjecture tree.
 *
 * @author Pellier Damien
 * @version 1.0, 14/01/03
 */
final class Node {

    /**
     * The <tt>Node</tt> counter.
     */
    private static int counter = -1;

    /**
     * The head of the <tt>Node</tt>.
     */
    private TaskAtom head;

    /**
     * The state of the <tt>Node</tt>.
     */
    private State state;

    /**
     * The <tt>TAskList</tt> of this <tt>Node</tt>.
     */
    private TaskList tasks;

    /**
     * The weight of this <tt>Node</tt>.
     */
    private double weight;

    /**
     * The list of <tt>Assumption</tt> s of this <tt>Node</tt>.
     */
    private List<Assumption> assumptions;

    /**
     * The flag that indicate if the <tt>Node</tt> was already explored.
     */
    private boolean isExplored;

    /**
     * The child <tt>Node</tt> list of this <tt>Node</tt>.
     */
    private List<Node> childs;

    /**
     * The root <tt>Node</tt> associated.
     */
    private Node root;

    /**
     * Flag indicating if this <tt>Node</tt> was built due to a first
     * decomposition.
     */
    private boolean isFirst;

    /**
     * The number of this <tt>Node</tt>.
     */
    private int num;

    /**
     * The deep of the <tt>Node</tt>.
     */
    private int deep;

    /**
     * Creates a new <tt>Node</tt>.
     *
     * @param h the head of this <tt>Node</tt>.
     * @param s the <tt>State</tt> (i.e., a reachable world state).
     * @param t the <tt>TaskList</tt> belongs to this <tt>Node</tt>.
     * @param w the weight in terms of <tt>Assumption</tt>s.
     * @param r the root <tt>Node</tt> of this <tt>Node</tt>.
     */
    Node(final TaskAtom h, final State s, final TaskList t, final double w,
            final Node r) {
        this.setHead(h);
        this.setState(s);
        this.tasks = new TaskList();
        this.addTasks(t);
        this.setWeight(w);
        this.childs = new ArrayList<Node>();
        this.assumptions = new ArrayList<Assumption>();
        this.isExplored = false;
        this.setRoot(r);
        this.num = counter++;
    }

    /**
     * Creates an empty <tt>Node</tt>.
     */
    private Node() { }

    /**
     * Creates a new <tt>Node</tt>.
     *
     * @param s the <tt>State</tt> (i.e., a reachable world state).
     * @param t the <tt>TaskList</tt> belongs to this <tt>Node</tt>.
     * @return the root <tt>Node</tt> created.
     */
    static Node createRootNode(final State s, final TaskList t) {
        final Node root = new Node();
        root.head = null;
        root.setState(s);
        root.tasks = new TaskList();
        root.addTasks(t);
        root.setWeight(0.0);
        root.childs = new ArrayList<Node>();
        root.assumptions = new ArrayList<Assumption>();
        root.isExplored = false;
        root.root = null;
        root.num = -1;
        root.deep = 0;
        return root;
    }

    /**
     * Returns if this <tt>Node</tt> is a root <tt>Node</tt>.
     *
     * @return <tt>true</tt> if this <tt>Node</tt> is a root <tt>Node</tt>.
     */
    boolean isRootNode() {
        return head == null && root == null;
    }

    /**
     * Sets the head associated to this <tt>Node</tt>.
     *
     * @param h the head of this <tt>Node</tt>.
     */
    void setHead(final TaskAtom h) {
        assert h != null : "parameter <h> == null";
        this.head = h;
    }

    /**
     * Returns the head associated to this <tt>Node</tt>.
     *
     * @return the head associated to this <tt>Node</tt>.
     */
    TaskAtom getHead() {
        return this.head;
    }

    /**
     * Sets the <tt>State</tt> of this <tt>Node</tt>.
     *
     * @param s the <tt>State</tt> of the world belongs to this
     *        <tt>Node</tt>.
     */
    void setState(final State s) {
        assert s != null : "parameter <s> == null";
        this.state = s;
    }

    /**
     * Returns the <tt>State</tt> belong to this <tt>Node</tt>.
     *
     * @return the <tt>State</tt> belong to this <tt>Node</tt>.
     */
    State getState() {
        return this.state;
    }

    /**
     * Adds a <tt>TaskList</tt> to the nodes <tt>TaskList</tt>.
     *
     * @param t the <tt>TaskList</tt> to appends.
     */
    void addTasks(final TaskList t) {
        assert t != null : "parameter <t> == null";
        this.tasks.addAll(0, t);
    }

    /**
     * Return the first task contained in the nodes <tt>TaskList</tt>.
     *
     * @return the first task or <tt>null</tt> if the <tt>TaskList</tt> is
     *         empty.
     */
    TaskAtom popTask() {
        return tasks.isEmpty() ? null : tasks.get(0);
    }

    /**
     * Returns the <tt>TaskList</tt> of this <tt>Node</tt>.
     *
     * @return the <tt>TaskList</tt> of this <tt>Node</tt>.
     */
    TaskList getTasks() {
        return this.tasks;
    }

    /**
     * Returns if the <tt>TaskList</tt> contains no more task.
     *
     * @return <tt>true</tt> the <tt>TaskList</tt> contains no more task,
     *         <tt>false</tt> otherwise.
     */
    boolean noMoreTask() {
        return tasks.isEmpty();
    }

    /**
     * Sets the assumptions associated to this <tt>Node</tt>.
     *
     * @param al the list of assumptions made by this
     *        <tt>Node</tt>.
     */
    void setAssumptions(final List<Assumption> al) {
        assert al != null : "parameter <al> == null";
        this.assumptions = al;
    }

    /**
     * Returns the <tt>Assumption</tt> list of this <tt>Node</tt>.
     *
     * @return the <tt>Assumption</tt> list of this <tt>Node</tt>
     */
     List<Assumption> getAssumptions() {
        return this.assumptions;
    }

    /**
     * Sets the weight of this <tt>Node</tt>.
     *
     * @param w the weight belongs to this <tt>Node</tt>.
     */
    void setWeight(final double w) {
        assert w >= 0.0 : "parameter <w> < 0.0";
        this.weight = w;
    }

    /**
     * Returns the weight of this <tt>Node</tt> (i.e., the number of
     * assumption makes by this node).
     *
     * @return the number of <tt>Assumption</tt> makes by this <tt>Node</tt>.
     */
    double getWeight() {
        return this.weight;
    }

    /**
     * Add a child to this <tt>Node</tt>.
     *
     * @param c the child <tt>Node</tt> to add.
     */
    void addChild(final Node c) {
        assert c != null : "parameter <c> == null";
        c.setRoot(this);
        this.childs.add(c);
    }

    /**
     * Sets the explored flag of the <tt>Node</tt> to a specific value.
     *
     * @param flag the flag value.
     */
    void setExplored(final boolean flag) {
        this.isExplored = flag;
    }

    /**
     * Returns if this <tt>Node</tt> was already explored.
     *
     * @return <tt>true</tt> if this <tt>Node</tt> was already explored,
     *         <tt>false</tt> otherwise.
     */
    boolean isExplored() {
        return this.isExplored;
    }

    /**
     * Sets the root <tt>Node</tt> of this <tt>Node</tt>.
     *
     * @param r the root <tt>Node</tt> of this <tt>Node</tt>.
     */
    void setRoot(final Node r) {
        assert r != null : "parameter <r> == null";
        this.root = r;
        this.deep = r.getDeep() + 1;
    }

    /**
     * Returns the root <tt>Node</tt> of this <tt>Node</tt>.
     *
     * @return the root <tt>Node</tt> of this <tt>Node</tt> or <tt>null</tt>
     *         if this <tt>Node</tt> is the root <tt>Node</tt> of a
     *         <tt>ConjectureTree</tt>.
     */
    Node getRoot() {
        return this.root;
    }

    /**
     * Sets the flag to indicate that this <tt>Node</tt> is a <tt>Node</tt>
     * due to a first <tt>Decomposition</tt>. That mean first choses in the
     * exploration <tt>ConjectureTree</tt>.
     *
     * @param flag the flag to indicate that this <tt>Node</tt> is a
     *        <tt>Node</tt> due to a first <tt>Decomposition</tt>
     */
    void setFirst(final boolean flag) {
        this.isFirst = flag;
    }

    /**
     * Returns if this <tt>Node</tt> is a first <tt>Node</tt>. That mean first
     * choses in the exploration <tt>ConjectureTree</tt>.
     *
     * @return <tt>true</tt> if this <tt>Node</tt> is a first <tt>Node</tt>.
     */
    boolean isFirst() {
        return this.isFirst;
    }

    /**
     * Returns the deep of this <tt>Node</tt>.
     *
     * @return the deep of this <tt>Node</tt>.
     */
    int getDeep() {
        return this.deep;
    }

    /**
     * Returns the <tt>Node</tt> id.
     *
     * @return the <tt>Node</tt> id.
     */
    int getID() {
        return this.num;
    }

    /**
     * Returns a <tt>String</tt> representation of this <tt>Node</tt>.
     *
     * @return a <tt>String</tt> representation of this <tt>Node</tt>.
     */
    public String toString() {
        final StringBuilder str = new StringBuilder();
        str.append("Name : ");
        str.append(getHead());
        str.append("\nState:\n");
        str.append(state.toString());
        str.append("\nAssumptions:\n");
        str.append(assumptions);
        str.append("\nTask list:\n");
        str.append(tasks.toString());
        str.append("\nWeight:\n");
        str.append(weight);
        str.append("\nID:\n");
        str.append(num);
        str.append("\nDeep:\n");
        str.append(deep);
        str.append("\n");
        return str.toString();
    }

}

