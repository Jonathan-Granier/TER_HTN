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
 * The <code>Atom</code> class implements an atom.
 *
 * @author Pellier Damien
 * @version 1.0, 14/01/03
 */
public abstract class Atom extends ArrayList<Term>
    implements Standardizable, Substituable, Cloneable {

    /**
     * The predicate symbol of this <tt>Atom</tt>.
     */
    private String symbol;

    /**
     * Flag indicating if the <tt>Atom</tt> is ground.
     */
    private boolean isGround;

    /**
     * Create a <tt>Atom</tt> of a specific symbol.
     */
    protected Atom() {
        this(new String());
    }

    /**
     * Create a <tt>Atom</tt> of a specific symbol.
     *
     * @param s the <tt>Atom</tt> symbol.
     */
    protected Atom(final String s) {
        this(s, false);
    }

    /**
     * Create a <tt>Atom</tt> of a specific symbol.
     *
     * @param s the <tt>Atom</tt> symbol.
     * @param flag indicates if the <tt>Atom</tt> is ground.
     */
    protected Atom(final String s, final boolean flag) {
        super();
        this.setSymbol(s);
        this.isGround = flag;
    }

    /**
     * Sets the symbol of this <tt>Atom</tt>.
     *
     * @param s the symbol belong to this <tt>Atom</tt>.
     */
    public final void setSymbol(final String s) {
        assert s != null : "parameter <s> == null";
        this.symbol = s;
    }

    /**
     * Returns the symbol belong to this <tt>Atom</tt>.
     *
     * @return the symbol belong to this <tt>Atom</tt>.
     */
    public final String getSymbol() {
        return this.symbol;
    }

    /**
     * Return if this <tt>Atom</tt> is ground.
     *
     * @return <tt>true</tt> if this <tt>Atom</tt> is ground, <tt>false</tt>
     *         otherwise.
     */
    public final boolean isGround() {
        return this.isGround;

    }

    /**
     * Set the ground flag of this <tt>Atom</tt> to a specific value.
     *
     * @param flag <tt>true</tt> to indicate that this <tt>Atom</tt> is
     *        ground, <tt>false</tt> otherwise.
     */
    protected final void setGround(final boolean flag) {
        this.isGround = flag;
    }

    /**
     * Returns the most general unifier of two atoms.
     *
     * @param a the second <tt>Atom</tt> to unify.
     * @return the most general unifier or <tt>null</tt> if it does not exist.
     *         An empty mgu means that there is no binding <tt>Variable</tt>
     *         between <tt>this</tt> and <tt>a</tt>.
     * @exception UnificationException if <tt>a</tt> is not an instance of
     *            <tt>Atom</tt>.
     */
    public abstract Substitution unify(final Atom a)
            throws UnificationException;

    /**
     * Returns the most general unifier of two atoms. Prior binding
     * variables contained in the <tt>Substitution</tt> parameter
     * are considered.
     *
     * @param a the first <tt>Atom</tt> to unify.
     * @param alpha the prior binding variables.
     * @return the most general unifier or <tt>null</tt> if it does not exist.
     *         An empty mgu means that there is no binding <tt>Variable</tt>
     *         between <tt>this</tt> and <tt>a</tt>.
     * @exception UnificationException if <tt>a</tt> is not an instance of
     *            <tt>Atom</tt>.
     */
    public abstract Substitution unify(final Atom a,
            final Substitution alpha) throws UnificationException;

    /**
     * Returns the most general unifier of two atoms. Prior binding
     * variables contained in the <tt>Substitution</tt> parameter
     * are considered.
     *
     * @param a the first <tt>Atom</tt> to unify.
     * @param alpha the prior binding variables.
     * @return the most general unifier or <tt>null</tt> if it does not exist.
     *         An empty mgu means that there is no binding <tt>Variable</tt>
     *         between <tt>this</tt> and <tt>a</tt>.
     */
    protected final Substitution unifyAtom(final Atom a,
            final Substitution alpha) {
        assert a != null : "parameter <a> == null";
        assert alpha != null : "parameter <alpha> == null";

        Substitution unifier = null;
        if ((this.getSymbol().equals(a.getSymbol()))
                && (this.size() == a.size())) {
            unifier = new Substitution();
            final Substitution alphaCopy = (Substitution) alpha.clone();
            for (int i = 0; i < this.size(); i++) {
                final Term ta1 = this.get(i);
                final Term ta2 = a.get(i);
                final Substitution beta = ta1.unify(ta2, alphaCopy);
                if (beta == null) {
                    return null;
                }
                unifier.compose(beta);
                alphaCopy.compose(beta);
            }
        }
        return unifier;
    }

    /**
     * Returns a <tt>String</tt> representation of this <tt>Atom</tt>.
     *
     * @return the <tt>string</tt> representation of this <tt>Atom</tt>.
     */
    public abstract String toString();

}
