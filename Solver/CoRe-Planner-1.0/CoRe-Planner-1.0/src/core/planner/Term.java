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
 * The <code>Term</code> class implements a term. A term is either a variable
 * a symbol, a constant, or an expression of either of the form: (list t1 t2 ...
 * tn) or (f t1 t2 ... tn) where f is a function symbol and each ti is a term.
 * Also a call-term is an expression of the form: (call f t1 t2 ... tn) where f
 * is the name of the attached procedure and each ti is a term or a call-term.
 * The <code>Term</code> class implements these two definitions. The
 * <code>Term</code> class has some flags that identify whether its content is
 * a constant, a variable, a function or a call-term.
 *
 * @author Pellier Damien
 * @version 1.0, 14/01/03
 */
public interface Term extends Standardizable, Substituable, Cloneable {

    /**
     * Sets the symbol of this <tt>Term</tt>.
     *
     * @param s the symbol belong to this <tt>Term</tt>.
     */
    void setSymbol(final String s);

    /**
     * Returns the symbol belong to this <tt>Variable</tt>.
     *
     * @return the symbol belong to this <tt>Variable</tt>.
     */
    String getSymbol();

    /**
     * Returns the most general unifier of two terms.
     *
     * @param t the <tt>Term</tt> to unify.
     * @return the most general unifier or <tt>null</tt> if it does not exist.
     *         An empty mgu means that there is no binding <tt>Variable</tt>
     *         between <tt>t</tt> and this term.
     * @exception UnificationException if <tt>t</tt> is not an instance of
     *            <tt>Term</tt>.
     */
    Substitution unify(final Term t) throws UnificationException;

    /**
     * Returns the most general unifier of two terms. Prior binding variables
     * contained in the <tt>Substitution</tt> parameter are considered.
     *
     * @param t the <tt>Term</tt> to unify.
     * @param alpha the prior binding variables.
     * @return the most general unifier or <tt>null</tt> if it does not exist.
     *         An empty mgu means that there is no binding <tt>Variable</tt>
     *         between <tt>t</tt> and this term.
     * @exception UnificationException if <tt>t</tt> is not an instance of
     *            <tt>Term</tt>.
     */
    Substitution unify(final Term t, final Substitution alpha)
            throws UnificationException;

    /**
     * Returns if an <tt>Object</tt> is equal to this <tt>Term</tt>.
     *
     * @param object the <tt>Object</tt> to be compared.
     * @return <tt>true</tt> if the <tt>Object</tt> to be compared is equal
     *         to this <tt>Term</tt>,<tt>false</tt> otherwise.
     */
    boolean equals(final Object object);

    /**
     * Returns the hash code value of this <tt>Term</tt>.
     *
     * @return the hash code value of this <tt>Term</tt>.
     */
    int hashCode();

    /**
     * Returns a deep copy of this <tt>Term</tt>.
     *
     * @return a deep copy of this <tt>Term</tt>.
     */
    Object clone();

    /**
     * Returns a <tt>String</tt> representation of this <tt>Term</tt>.
     *
     * @return a <tt>String</tt> representation of this <tt>Term</tt>.
     */
    String toString();

}
