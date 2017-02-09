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

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * The <code>Substitution</code> class implements a substitution. This class
 * represents for a list of dotted pairs of the form ((x1.t1) (x2.t2) ...
 * (xn.tn)). It is used as a list of current variables bindings. When two terms
 * are unified this object contains the variable binding.
 *
 * @author Pellier Damien
 * @version 1.0, 14/01/03
 */
public final class Substitution implements Standardizable {

    /**
     * The <tt>Map</tt> that stores the <tt>Substitution</tt>.
     */
    private Map<Variable, Term> map;

    /**
     * The list of binding literals computed.
     */
    private Set<Literal> bindingLiterals;

    /**
     * Creates an empty <tt>Substitution</tt>.
     */
    public Substitution() {
        this.map = new LinkedHashMap<Variable, Term>();
        this.bindingLiterals = new HashSet<Literal>();
    }

    /**
     * Associates the specified <tt>Term</tt> with the specified
     * <tt>Variable</tt> in this <tt>Substitution</tt>. If the
     * <tt>Substitution</tt> previously contained a mapping <tt>Term</tt>
     * for this <tt>Variable</tt>, the old <tt>Term</tt> value is replaced.
     *
     * @param variable the <tt>Variable</tt> with which the specified
     *        <tt>Term</tt> is to be associated.
     * @param term the <tt>Term</tt> to be associated with the specified
     *        <tt>Variable</tt>.
     * @return the previous <tt>Term</tt> value associated with specified
     *         <tt>Variable</tt>, or <tt>null</tt> if there was no mapping
     *         <tt>Term</tt> for <tt>Variable</tt>.
     */
    public Term compose(final Variable variable, final Term term) {
        assert variable != null : "parameter <variable> == null";
        assert term != null : "parameter <term> == null";
        return this.map.put(variable, term);
    }

    /**
     * Returns the <tt>Term</tt> to which this <tt>Substitution</tt> maps
     * the specified <tt>Term</tt>.
     *
     * @param variable the <tt>Variable</tt> whose associated <tt>Term</tt>
     *        is to be returned.
     * @return <tt>null</tt> if the <tt>Substitution</tt> contains no
     *         mapping for this <tt>Variable</tt>.
     */
    public Term instance(final Variable variable) {
        assert variable != null : "parameter <variable> == null";
        return this.map.get(variable);
    }

    /**
     * Compose this <tt>alpha</tt> with a specific one.
     *
     * @param alpha the <tt>substitution</tt> to merge with.
     */
    public void compose(final Substitution alpha) {
        assert alpha != null : "parameter <alpha> == null";
        for (Term t : this.map.values()) {
            final Term st = (Term) t.applySubstitution(alpha);
        }

        final Set<Variable> vSet = this.map.keySet();
        for (Map.Entry<Variable, Term> e : alpha.map.entrySet()) {
            final Variable v = e.getKey();
            final Term t = e.getValue();
            if (!vSet.contains(v)) {
                this.map.put(v, t);
            }
        }

        for (Literal l : alpha.getBindingLiterals()) {
            this.addBindingLiteral((Literal) l.clone());
        }
    }

    /**
     * Adds an binding <tt>Literal</tt> to this <tt>Substitution</tt>.
     *
     * @param bl the binding <tt>Literal</tt> to add.
     * @return <tt>true</tt> if the <tt>bl</tt> was added, <tt>false</tt>
     *         otherwise.
     */
    public boolean addBindingLiteral(final Literal bl) {
        assert bl != null : "parameter <bl> == null";
        return this.bindingLiterals.add(bl);
    }

    /**
     * Remove a binding <tt>literal</tt> to this <tt>Substitution</tt>.
     *
     * @param bl the binding <tt>Literal</tt> to remove.
     * @return <tt>true</tt> if the specific binding <tt>Literal</tt> was
     *         removed, <tt>false</tt> otherwise.
     */
    public boolean removeBindingLiteral(final Literal bl) {
        assert bl != null : "parameter <bl> == null";
        return this.bindingLiterals.remove(bl);
    }

    /**
     * Returns the number of binding literals contained in this
     * <tt>Substitution</tt>.
     *
     * @return the number of binding literals contained in this
     *         <tt>Substitution</tt>.
     */
    public int getNumberOfBindingLiterals() {
        return this.bindingLiterals.size();
    }

    /**
     * Returns the <tt>Set</tt> of literals conatined in this
     * <tt>Substitution</tt>.
     *
     * @return the <tt>Set</tt> of literals conatined in this
     *         <tt>Substitution</tt>.
     */
    public Set<Literal> getBindingLiterals() {
        return this.bindingLiterals;
    }

    /**
     * Returns the list of variables contained in this <tt>Substitution</tt>.
     *
     * @return the list of <tt>Variable</tt>s instantiated in this
     *         <tt>substitution</tt>.
     */
    public Set<Variable> getVariables() {
        return this.map.keySet();
    }

    /**
     * Returns if this <tt>Substitution</tt> is equal to an <tt>Object</tt>.
     *
     * @param obj the <tt>Object</tt> to be compared.
     * @return <tt>true</tt> the specific <tt>Object</tt> is equal to this
     *         <tt>Substitution</tt>,<tt>false</tt> otherwise.
     */
    public boolean equals(final Object obj) {
        if (obj instanceof Substitution) {
            final Substitution cmp = (Substitution) obj;
            if (cmp.getNumberOfBindingLiterals()
                    == this.getNumberOfBindingLiterals()
                    && cmp.getBindingLiterals().equals(
                            this.getBindingLiterals())) {
                return super.equals(cmp);
            }
        }
        return false;
    }

    /**
     * Returns the hash code value of this <tt>Substitution</tt>.
     *
     * @return the hash code value.
     */
    public int hashCode() {
        return this.map.hashCode() + this.bindingLiterals.hashCode();
    }

    /**
     * Returns a deep copy of this <tt>Substitution</tt>.
     *
     * @return a deep copy of this <tt>Substitution</tt>.
     */
    public Object clone() {
        final Substitution copy = new Substitution();
        for (Map.Entry<Variable, Term> e : this.map.entrySet()) {
            final Variable v = (Variable) e.getKey().clone();
            final Term t = (Term) e.getValue().clone();
            copy.compose(v, t);
        }
        for (Literal l : bindingLiterals) {
            copy.addBindingLiteral((Literal) l.clone());
        }
        return copy;
    }

    /**
     * Standardize the variable name contained in this <tt>Substitution</tt>.
     *
     * @return a standardized copy of this <tt>Substitution</tt>.
     * @see core.planner.Standardizable#standardize()
     */
    public Object standardize() {
        final Substitution copy = new Substitution();
        for (Map.Entry<Variable, Term> e : this.map.entrySet()) {
            final Variable v = (Variable) e.getKey().clone();
            final Term t = (Term) e.getValue().standardize();
            copy.compose(v, t);
        }
        for (Literal l : bindingLiterals) {
            copy.addBindingLiteral((Literal) l.standardize());
        }
        return copy;
    }

    /**
     * Returns a <tt>String</tt> representation of this <tt>Substitution</tt>.
     *
     * @return the <tt>String</tt> representation associated to this
     *         <tt>Substitution</tt>.
     */
    public String toString() {
        final StringBuilder str = new StringBuilder();
        str.append("f:");
        if (this.map.entrySet().isEmpty()) {
            str.append("()");
        } else {
            for (Map.Entry<Variable, Term> e : this.map.entrySet()) {
                str.append("(");
                str.append(e.getKey().toString());
                str.append("-> ");
                str.append(e.getValue().toString());
                str.append(") ");
            }
        }
        str.append("\n?:");
        if (bindingLiterals.isEmpty()) {
            str.append("()");
        } else {
            for (Literal l : bindingLiterals) {
                str.append(l.toString());
            }
        }
        return str.toString();
    }

}
