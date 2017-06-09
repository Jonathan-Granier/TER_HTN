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

import java.util.LinkedList;

/**
 * The <code>Conjecture</code> class implements a conjecture. A conjecture is
 * a list of actions.
 *
 * @author Pellier Damien
 * @version 1.0, 14/01/03
 */
public final class Conjecture extends LinkedList<Action> {

    /**
     * <code>serialVersionUID</code>.
     */
    private static final long serialVersionUID = 3544956549752566069L;

    /**
     * The number of assumptions done by this <tt>Conjecture</tt>.
     */
    private int numberOfAssumptions;

    /**
     * Creates an empty <tt>Conjecture</tt>.
     */
    public Conjecture() {
        super();
        this.numberOfAssumptions = 0;
    }

    /**
     * Returns the number of assumptions made by this
     * <tt>Conjecture</tt>.
     *
     * @return the number of assumptions made by this
     *         <tt>Conjecture</tt>.
     */
    public int getNumberOfAssumptions() {
        return this.numberOfAssumptions;
    }

    /**
     * Return if a specific <tt>Conjecture</tt> is include in this
     * <tt>Conjecture</tt>.
     *
     * @param c the <tt>Conjecture</tt> to be tested.
     * @return <tt>true</tt> if <tt>c</tt> is included in this
     *         <tt>Conjecture</tt>,<tt>false</tt> otherwise.
     */
    public boolean includes(final Conjecture c) {
        assert c != null : "Assertion: parameter <c> == null";
        if (c.isEmpty() || this.size() < c.size()) {
            return false;
        }
        int index = this.indexOf(c.get(0));
        if (index == -1 || (this.size() - index) < c.size()) {
            return false;
        }
        for (int i = 0; i < c.size(); i++, index++) {
            final Action a = (Action) this.get(index);
            if (!a.equals((Action) c.get(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns a <tt>String</tt> representation of this <tt>Conjecture</tt>.
     *
     * @return a <tt>String</tt> representation of this <tt>Conjecture</tt>.
     */
    public String toString() {
        final StringBuilder str = new StringBuilder();
        int index = 1;
        for (Action a : this) {
            str.append(index);
            str.append(". ");
            str.append(a.toString());
            str.append("\n");
            index++;
        }
        return str.toString();
    }

}
