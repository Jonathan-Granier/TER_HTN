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

import java.util.Iterator;
import java.util.LinkedHashSet;

/**
 * The <code>DomainSet</code> class implements a set of domains.
 *
 * @author Pellier Damien
 * @version 1.0, 14/01/03
 */
public final class DomainSet extends LinkedHashSet<Domain> {

    /**
     * <code>serialVersionUID</code>.
     */
    private static final long serialVersionUID = 3256725069744320819L;

    /**
     * Create an empty <tt>DomainSet</tt>.
     */
    public DomainSet() {
        super();
    }

    /**
     * Returns if a <tt>Domain</tt> with a specific name id contains in this
     * <tt>DomainSet</tt>.
     *
     * @param n the name of the <tt>Domain</tt> to be tested.
     * @return <tt>true</tt> if a <tt>Domain</tt> with a specific name is
     *         contained in this <tt>DomainSet</tt>, <tt>false</tt>
     *         otherwise.
     */
    public boolean containsDomainWithName(final String n) {
        return this.getDomainWithName(n) == null ? false : true;
    }

    /**
     * Returns a <tt>Domain</tt> with a specific name.
     *
     * @param n the name of the <tt>Domain</tt> to be returned.
     * @return the <tt>Domain</tt> or <tt>null</tt> if it doesn't exist.
     */
    public Domain getDomainWithName(final String n) {
        assert n != null : "Assertion: parameter <n> == null";
        final Iterator<Domain> i = this.iterator();
        while (i.hasNext()) {
            final Domain d = i.next();
            if (d.getName().equals(n)) {
                return d;
            }
        }
        return null;
    }

    /**
     * Returns a <tt>String</tt> representation of this <tt>DomainSet</tt>.
     *
     * @return a <tt>String</tt> representation of this <tt>DomainSet</tt>.
     */
    public String toString() {
        final StringBuilder str = new StringBuilder();
        for (Domain d : this) {
            str.append(d.toString());
        }
        return str.toString();
    }

}
