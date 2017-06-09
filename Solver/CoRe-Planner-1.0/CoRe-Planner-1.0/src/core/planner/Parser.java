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

import java.io.InputStream;

import core.planner.lexer.Lexer;
import core.planner.lexer.ParseException;

/**
 * Implements the <tt>Parser</tt> of core <tt>Planner</tt>.
 *
 * @author Damien Pellier
 * @version 1.0, 14/01/03
 */
public final class Parser {

    /**
     * The <tt>Lexer</tt>.
     */
    private Lexer lexer;

    /**
     * Create a new <tt>Parser</tt>.
     *
     * @param input the <tt>InputStream</tt> to read.
     */
    public Parser(final InputStream input) {
        assert input != null : "parameter <input> == null";
        this.lexer = new Lexer(input);
    }

    /**
     * Read a planning <tt>Domain</tt>.
     *
     * @return the planning <tt>Domain</tt> read.
     * @exception ParseException if an error occurs during reading.
     */
    public Domain readDomain() throws ParseException {
        return lexer.domain();
    }

    /**
     * Read a planning <tt>Problem</tt>.
     *
     * @return the planning <tt>Problem</tt> read.
     * @exception ParseException if an error occurs during reading.
     */
    public Problem readProblem() throws ParseException {
        return lexer.problem();
    }

    /**
     * Read a list of planning <tt>Problem</tt>.
     *
     * @return the list of planning <tt>Problem</tt> read.
     * @exception ParseException if an error occurs during reading.
     */
    public ProblemSet readProblems() throws ParseException {
        return lexer.problemSet();
    }

    /**
     * Read an <tt>AgentDescription</tt>.
     *
     * @return the <tt>AgentDescription</tt> read.
     * @exception ParseException if an error occurs during reading.
     */
    public AgentDescription readAgentDescription() throws ParseException {
        return lexer.agentDescription();
    }

}
