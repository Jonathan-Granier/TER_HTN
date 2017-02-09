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
 * The <code>Eval</code> class implements the different methods evaluation.
 * <br>
 * Modifications:
 * <li>12/01/05: add member operator.The member operator can be apply even if
 * the expression is not ground.</li>
 * </br>
 *
 * @author Pellier Damien
 * @version 1.0, 14/01/03
 */
final class Eval {

    /**
     * Creates an empty <tt>Eval</tt> instance.
     */
    private Eval() { }

    /**
     * Returns the result of an evaluation.
     *
     * @param op the operator of evalution.
     * @param operand1 the first operand.
     * @param operand2 the second operand.
     * @return a <tt>Constant</tt> defining the evaluation result.
     * @exception IllegalArgumentException if op is not a recognized operator or
     *            the operands are not valid according to op.
     */
    static Constant evaluate(final String op, final Term operand1,
            final Term operand2) throws IllegalArgumentException {
        if (op.equals("+") && operand1 instanceof Constant
                && operand2 instanceof Constant) {
            return addition(operand1, operand2);
        } else if (op.equals("-")) {
            return subtraction(operand1, operand2);
        } else if (op.equals("*")) {
            return multiplication(operand1, operand2);
        } else if (op.equals("/")) {
            return division(operand1, operand2);
        } else if (op.equals(">")) {
            return greater(operand1, operand2);
        } else if (op.equals(">=")) {
            return greaterEqual(operand1, operand2);
        } else if (op.equals("<")) {
            return less(operand1, operand2);
        } else if (op.equals("<=")) {
            return lessEqual(operand1, operand2);
        } else if (op.equalsIgnoreCase("=")) {
            return equal(operand1, operand2);
        } else if (op.equalsIgnoreCase("!=")) {
            return notEqual(operand1, operand2);
        } else if (op.equals("max")) {
            return maximum(operand1, operand2);
        } else if (op.equals("min")) {
            return minimum(operand1, operand2);
        } else if (op.equals("member")) {
            return member(operand1, operand2);
        } else if (op.equals("cat")) {
            return cat(operand1, operand2);
        } else {
            throw new IllegalArgumentException(
                    "Assertion: evaluation expression (call " + op + " "
                            + operand1 + " " + operand2 + "): "
                            + "parameter <op> not recognized");
        }
    }

    /**
     * Returns the result of the addition function evaluation.
     *
     * @param operand1 the first operand.
     * @param operand2 the second operand.
     * @return the result of the addition function evaluation.
     * @exception IllegalArgumentException if !operand1.isNumerical() or
     *            !operand2.isNumerical().
     */
    private static Constant addition(final Term operand1, final Term operand2)
            throws IllegalArgumentException {
        if (!(operand1 instanceof Constant)) {
            throw new IllegalArgumentException(
                "Assertion: evaluation expression (call + " + operand1
                    + " " + operand2 + "): "
                    + "parameter <operand1> is not a constant");
        }
        final Constant op1 = (Constant) operand1;
        if (!(operand2 instanceof Constant)) {
            throw new IllegalArgumentException(
                "Assertion: evaluation expression (call + " + operand1
                + " " + operand2 + "): "
                + "parameter <operand2> is not a constant");
        }
        final Constant op2 = (Constant) operand2;
        if (!op2.isNumerical()) {
            throw new IllegalArgumentException(
                "Assertion: evaluation expression (call + "
                + operand1
                + " "
                + operand2
                + "): "
                + "parameter <operand2> is not a numerical constant");
        }
        if (!op2.isNumerical()) {
            throw new IllegalArgumentException(
                    "Assertion: evaluation expression (call + "
                    + operand1
                    + " "
                    + operand2
                    + "): "
                    + "parameter <operand2> is not a numerical constant");
        }
        final int sum = Integer.parseInt(op1.getSymbol())
                + Integer.parseInt(op2.getSymbol());
        final Constant result = new Constant(new Integer(sum).toString());
        result.setNumerical(true);
        return result;
    }

    /**
     * Returns the result of the subtraction function evaluation.
     *
     * @param operand1 the first operand.
     * @param operand2 the second operand.
     * @return the result of the subtraction function evaluation.
     * @exception IllegalArgumentException if !operand1.isNumerical() or
     *            !operand2.isNumerical().
     */
    private static Constant subtraction(final Term operand1,
            final Term operand2) throws IllegalArgumentException {
        if (!(operand1 instanceof Constant)) {
            throw new IllegalArgumentException(
                    "Assertion: evaluation expression (call - " + operand1
                    + " " + operand2 + "): "
                    + "parameter <operand1> is not a constant");
        }
        final Constant op1 = (Constant) operand1;
        if (!(operand2 instanceof Constant)) {
            throw new IllegalArgumentException(
                    "Assertion: evaluation expression (call - " + operand1
                    + " " + operand2 + "): "
                    + "parameter <operand2> is not a constant");
        }
        final Constant op2 = (Constant) operand2;
        if (!op1.isNumerical()) {
            throw new IllegalArgumentException(
                    "Assertion: evaluation expression (call - "
                    + operand1
                    + " "
                    + operand2
                    + "): "
                    + "parameter <operand1> is not a numerical constant");
        }
        if (!op2.isNumerical()) {
            throw new IllegalArgumentException(
                    "Assertion: evaluation expression (call - "
                    + operand1
                    + " "
                    + operand2
                    + "): "
                    + "parameter <operand2> is not a numerical constant");
        }
        final int sum = Integer.parseInt(op1.getSymbol())
                - Integer.parseInt(op2.getSymbol());
        final Constant result = new Constant(new Integer(sum).toString());
        result.setNumerical(true);
        return result;
    }

    /**
     * Returns the result of the multiplication function evaluation.
     *
     * @param operand1 the first operand.
     * @param operand2 the second operand.
     * @return the result of the multiplication function evaluation.
     * @exception IllegalArgumentException if !operand1.isNumerical() or
     *            !operand2.isNumerical().
     */
    private static Constant multiplication(final Term operand1,
            final Term operand2) throws IllegalArgumentException {
        if (!(operand1 instanceof Constant)) {
            throw new IllegalArgumentException(
                    "Assertion: evaluation expression (call * " + operand1
                    + " " + operand2 + "): "
                    + "parameter <operand1> is not a constant");
        }
        final Constant op1 = (Constant) operand1;
        if (!(operand2 instanceof Constant)) {
            throw new IllegalArgumentException(
                    "Assertion: evaluation expression (call * " + operand1
                    + " " + operand2 + "): "
                    + "parameter <operand2> is not a constant");
        }
        final Constant op2 = (Constant) operand2;
        if (!op1.isNumerical()) {
            throw new IllegalArgumentException(
                    "Assertion: evaluation expression (call * "
                    + operand1
                    + " "
                    + operand2
                    + "): "
                    + "parameter <operand1> is not a numerical constant");
        }
        if (!op2.isNumerical()) {
            throw new IllegalArgumentException(
                    "Assertion: evaluation expression (call * "
                    + operand1
                    + " "
                    + operand2
                    + "): "
                    + "parameter <operand2> is not a numerical constant");
        }
        final int sum = Integer.parseInt(op1.getSymbol())
                * Integer.parseInt(op2.getSymbol());
        final Constant result = new Constant(new Integer(sum).toString());
        result.setNumerical(true);
        return result;
    }

    /**
     * Returns the result of the division function evaluation.
     *
     * @param operand1 the first operand.
     * @param operand2 the second operand.
     * @return the result of the dividion function evaluation.
     * @exception IllegalArgumentException if !operand1.isNumerical() or
     *            !operand2.isNumerical().
     */
    private static Constant division(final Term operand1, final Term operand2)
            throws IllegalArgumentException {
        if (!(operand1 instanceof Constant)) {
            throw new IllegalArgumentException(
                    "Assertion: evaluation expression (call / " + operand1
                    + " " + operand2 + "): "
                    + "parameter <operand1> is not a constant");
        }
        final Constant op1 = (Constant) operand1;
        if (!(operand2 instanceof Constant)) {
            throw new IllegalArgumentException(
                    "Assertion: evaluation expression (call / " + operand1
                    + " " + operand2 + "): "
                    + "parameter <operand2> is not a constant");
        }
        final Constant op2 = (Constant) operand2;
        if (!op1.isNumerical()) {
            throw new IllegalArgumentException(
                    "Assertion: evaluation expression (call / "
                    + operand1
                    + " "
                    + operand2
                    + "): "
                    + "parameter <operand1> is not a numerical constant");
        }
        if (!op2.isNumerical()) {
            throw new IllegalArgumentException(
                    "Assertion: evaluation expression (call / "
                    + operand1
                    + " "
                    + operand2
                    + "): "
                    + "parameter <operand2> is not a numerical constant");
        }
        final int sum = Integer.parseInt(op1.getSymbol())
                / Integer.parseInt(op2.getSymbol());
        final Constant result = new Constant(new Integer(sum).toString());
        result.setNumerical(true);
        return result;
    }

    /**
     * Returns the result of the greater function evaluation.
     *
     * @param operand1 the first operand.
     * @param operand2 the second operand.
     * @return the result of the greater function evaluation: the constant nil
     *         the evaluation fails constant "()" otherwise.
     * @exception IllegalArgumentException if !operand1.isNumerical() or
     *            !operand2.isNumerical().
     */
    private static Constant greater(final Term operand1, final Term operand2)
            throws IllegalArgumentException {
        if (!(operand1 instanceof Constant)) {
            throw new IllegalArgumentException(
                    "Assertion: evaluation expression (call > " + operand1
                     + " " + operand2 + "): "
                     + "parameter <operand1> is not a constant");
        }
        final Constant op1 = (Constant) operand1;
        if (!(operand2 instanceof Constant)) {
            throw new IllegalArgumentException(
                    "Assertion: evaluation expression (call > " + operand1
                    + " " + operand2 + "): "
                    + "parameter <operand2> is not a constant");
        }
        final Constant op2 = (Constant) operand2;
        if (!op1.isNumerical()) {
            throw new IllegalArgumentException(
                    "Assertion: evaluation expression (call > "
                    + operand1
                    + " "
                    + operand2
                    + "): "
                    + "parameter <operand1> is not a numerical constant");
        }
        if (!op1.isNumerical()) {
            throw new IllegalArgumentException(
                    "Assertion: evaluation expression (call > "
                    + operand1
                    + " "
                    + operand2
                    + "): "
                    + "parameter <operand2> is not a numerical constant");
        }
        final double d1 = Double.parseDouble(op1.getSymbol());
        final double d2 = Double.parseDouble(op2.getSymbol());
        if (d1 > d2) {
            return new Constant("()");
        }
        return new Constant("nil");
    }

    /**
     * Returns the result of the greater equal function evaluation.
     *
     * @param operand1 the first operand.
     * @param operand2 the second operand.
     * @return the result of the greater equal function evaluation: the constant
     *         nil the evaluation fails constant "()" otherwise.
     * @exception IllegalArgumentException if !operand1.isNumerical() or
     *            !operand2.isNumerical().
     */
    private static Constant greaterEqual(final Term operand1,
            final Term operand2) throws IllegalArgumentException {
        if (!(operand1 instanceof Constant)) {
            throw new IllegalArgumentException(
                    "Assertion: evaluation expression (call >= " + operand1
                    + " " + operand2 + "): "
                    + "parameter <operand1> is not a constant");
        }
        final Constant op1 = (Constant) operand1;
        if (!(operand2 instanceof Constant)) {
            throw new IllegalArgumentException(
                    "Assertion: evaluation expression (call >= " + operand1
                    + " " + operand2 + "): "
                    + "parameter <operand2> is not a constant");
        }
        final Constant op2 = (Constant) operand2;
        if (!op1.isNumerical()) {
            throw new IllegalArgumentException(
                    "Assertion: evaluation expression (call >= "
                     + operand1
                     + " "
                     + operand2
                     + "): "
                     + "parameter <operand1> is not a numerical constant");
        }
        if (!op2.isNumerical()) {
            throw new IllegalArgumentException(
                    "Assertion: evaluation expression (call >= "
                    + operand1
                    + " "
                    + operand2
                    + "): "
                    + "parameter <operand2> is not a numerical constant");
        }
        final double d1 = Double.parseDouble(op1.getSymbol());
        final double d2 = Double.parseDouble(op2.getSymbol());
        if (d1 >= d2) {
            return new Constant("()");
        }
        return new Constant("nil");
    }

    /**
     * Returns the result of the less function evaluation.
     *
     * @param operand1 the first operand.
     * @param operand2 the second operand.
     * @return the result of the less function evaluation: the constant nil the
     *         evaluation fails constant "()" otherwise.
     * @exception IllegalArgumentException if !operand1.isNumerical() or
     *            !operand2.isNumerical().
     */
    private static Constant less(final Term operand1, final Term operand2)
            throws IllegalArgumentException {
        if (!(operand1 instanceof Constant)) {
            throw new IllegalArgumentException(
                    "Assertion: evaluation expression (call < " + operand1
                    + " " + operand2 + "): "
                    + "parameter <operand1> is not a constant");
        }
        final Constant op1 = (Constant) operand1;
        if (!(operand2 instanceof Constant)) {
            throw new IllegalArgumentException(
                    "Assertion: evaluation expression (call < " + operand1
                    + " " + operand2 + "): "
                    + "parameter <operand2> is not a constant");
        }
        final Constant op2 = (Constant) operand2;
        if (!op1.isNumerical()) {
            throw new IllegalArgumentException(
                    "Assertion: evaluation expression (call < "
                    + operand1
                    + " "
                    + operand2
                    + "): "
                    + "parameter <operand1> is not a numerical constant");
        }
        if (!op2.isNumerical()) {
            throw new IllegalArgumentException(
                    "Assertion: evaluation expression (call < "
                    + operand1
                    + " "
                    + operand2
                    + "): "
                    + "parameter <operand2> is not a numerical constant");
        }
        final double d1 = Double.parseDouble(op1.getSymbol());
        final double d2 = Double.parseDouble(op2.getSymbol());
        if (d1 < d2) {
            return new Constant("()");
        }
        return new Constant("nil");
    }

    /**
     * Returns the result of the less equal function evaluation.
     *
     * @param operand1 the first operand.
     * @param operand2 the second operand.
     * @return the result of the less equal function evaluation: the constant
     *         nil the evaluation fails constant "()" otherwise.
     * @exception IllegalArgumentException if !operand1.isNumerical() or
     *            !operand2.isNumerical().
     */
    private static Constant lessEqual(final Term operand1, final Term operand2)
            throws IllegalArgumentException {
        if (!(operand1 instanceof Constant)) {
            throw new IllegalArgumentException(
                    "Assertion: evaluation expression (call <= " + operand1
                    + " " + operand2 + "): "
                    + "parameter <operand1> is not a constant");
        }
        final Constant op1 = (Constant) operand1;
        if (!(operand2 instanceof Constant)) {
            throw new IllegalArgumentException(
                    "Assertion: evaluation expression (call <= " + operand1
                    + " " + operand2 + "): "
                    + "parameter <operand2> is not a constant");
        }
        final Constant op2 = (Constant) operand2;
        if (!op1.isNumerical()) {
            throw new IllegalArgumentException(
                    "Assertion: evaluation expression (call <= "
                    + operand1
                    + " "
                    + operand2
                    + "): "
                    + "parameter <operand1> is not a numerical constant");
        }
        if (!op2.isNumerical()) {
            throw new IllegalArgumentException(
                    "Assertion: evaluation expression (call <= "
                    + operand1
                    + " "
                    + operand2
                    + "): "
                    + "parameter <operand2> is not a numerical constant");
        }
        final double d1 = Double.parseDouble(op1.getSymbol());
        final double d2 = Double.parseDouble(op2.getSymbol());
        if (d1 <= d2) {
            return new Constant("()");
        }
        return new Constant("nil");
    }

    /**
     * Returns the result of the equal function evaluation.
     *
     * @param operand1 the first operand.
     * @param operand2 the second operand.
     * @return the result of the equal function evaluation: the constant nil the
     *         evaluation fails constant "()" otherwise.
     * @exception IllegalArgumentException if (!operand1.isNumerical() and
     *            operand2.isNumerical()) or (operand1.isNumerical() and
     *            !operand2.isNumerical())
     */
    private static Constant equal(final Term operand1, final Term operand2)
            throws IllegalArgumentException {
        if (!(operand1 instanceof Constant)) {
            throw new IllegalArgumentException(
                    "Assertion: evaluation expression (call equal " + operand1
                    + " " + operand2 + "): "
                    + "parameter <operand1> is not a constant");
        }
        final Constant op1 = (Constant) operand1;
        if (!(operand2 instanceof Constant)) {
            throw new IllegalArgumentException(
                    "Assertion: evaluation expression (call equal " + operand1
                    + " " + operand2 + "): "
                    + "parameter <operand2> is not a constant");
        }
        final Constant op2 = (Constant) operand2;
        if ((!op1.isNumerical() && op2.isNumerical())
                || (op1.isNumerical() && !op2.isNumerical())) {
            throw new IllegalArgumentException(
                    "Assertion: evaluation expression (call equal "
                    + operand1
                    + " "
                    + operand2
                    + "): "
                    + "cannot compare a numerical constant to a string "
                    + "constant");
        }
        if (op1.isNumerical() && op2.isNumerical()) {
            final double d1 = Double.parseDouble(op1.getSymbol());
            final double d2 = Double.parseDouble(op2.getSymbol());
            if (d1 == d2) {
                return new Constant("()");
            }
            return new Constant("nil");
        } else {
            if (op1.getSymbol().equalsIgnoreCase(op2.getSymbol())) {
                return new Constant("()");
            }
            return new Constant("nil");
        }
    }

    /**
     * Returns the result of the not equal function evaluation.
     *
     * @param operand1 the first operand.
     * @param operand2 the second operand.
     * @return the result of the not equal function evaluation: the constant nil
     *         the evaluation fails constant "()" otherwise.
     * @exception IllegalArgumentException if (!operand1.isNumerical() and
     *            operand2.isNumerical()) or (operand1.isNumerical() and
     *            !operand2.isNumerical())
     */
    private static Constant notEqual(final Term operand1, final Term operand2)
            throws IllegalArgumentException {
        if (!(operand1 instanceof Constant)) {
            throw new IllegalArgumentException(
                    "Assertion: evaluation expression (call notequal "
                    + operand1 + " " + operand2 + "): "
                    + "parameter <operand1> is not a constant");
        }
        final Constant op1 = (Constant) operand1;
        if (!(operand2 instanceof Constant)) {
            throw new IllegalArgumentException(
                    "Assertion: evaluation expression (call notequal "
                    + operand1 + " " + operand2 + "): "
                    + "parameter <operand2> is not a constant");
        }
        final Constant op2 = (Constant) operand2;
        if ((!op1.isNumerical() && op2.isNumerical())
                || (op1.isNumerical() && !op2.isNumerical())) {
            throw new IllegalArgumentException(
                    "Assertion: evaluation expression (call notequal "
                    + operand1
                    + " "
                    + operand2
                    + "): "
                    + "cannot compare a numerical constant to a string "
                    + "constant");
        }
        if (op1.isNumerical() && op2.isNumerical()) {
            final double d1 = Double.parseDouble(op1.getSymbol());
            final double d2 = Double.parseDouble(op2.getSymbol());
            if (d1 != d2) {
                return new Constant("()");
            }
            return new Constant("nil");
        } else {
            if (!op1.getSymbol().equalsIgnoreCase(op2.getSymbol())) {
                return new Constant("()");
            }
            return new Constant("nil");
        }
    }

    /**
     * Returns the result of the max function evaluation.
     *
     * @param operand1 the first operand.
     * @param operand2 the second operand.
     * @return the result of the max function evaluation.
     * @exception IllegalArgumentException if (!operand1.isConstant() ||
     *            !operand1.isNumerical()) && (!operand2.isFunction() or
     *            !operand2.getSymbol().equals(".")) wher each term is a
     *            numerical constant
     */
    private static Constant maximum(final Term operand1, final Term operand2)
            throws IllegalArgumentException {
        if (!(operand1 instanceof Constant)) {
            throw new IllegalArgumentException(
                    "Assertion: evaluation expression (call max " + operand1
                     + " " + operand2 + "): "
                     + "parameter <operand1> must be a constant");
        }
        final Constant op1 = (Constant) operand1;
        if (!op1.isNumerical()) {
            throw new IllegalArgumentException(
                    "Assertion: evaluation expression (call max "
                     + operand1
                     + " "
                     + operand2
                     + "): "
                     + "parameter <operand2> must be a numerical constant");
        }
        final double d1 = Double.parseDouble(op1.getSymbol());
        if (operand2 instanceof Constant
                && operand2.getSymbol().equals("nil")) {
            return new Constant("()");
        }
        if (!(operand2 instanceof Function)
                && !operand2.getSymbol().equals(".")) {
            throw new IllegalArgumentException(
                    "Assertion: evaluation expression (call max " + operand1
                    + " " + operand2 + "): "
                    + "parameter <operand2> must be a list");
        }
        Term list = operand2;
        while (!list.getSymbol().equals("nil")) {
            final Term elt = ((Function) list).get(0);
            if (!(elt instanceof Constant)) {
                throw new IllegalArgumentException(
                        "Assertion: evaluation expression (call max "
                        + operand1
                        + " "
                        + operand2
                        + "): "
                        + "parameter <operand2> must only contain constant");
            }
            final Constant c = (Constant) elt;
            if (!c.isNumerical()) {
                throw new IllegalArgumentException(
                        "Assertion: evaluation expression (call max "
                        + operand1
                        + " "
                        + operand2
                        + "): "
                        + "parameter <operand2> must only contain numerical "
                        + "constant");
            }
            final double d2 = Double.parseDouble(c.getSymbol());
            if (d1 < d2) {
                return new Constant("nil");
            }
            list = ((Function) list).get(1);
        }
        return new Constant("()");
    }

    /**
     * Returns the result of the min function evaluation.
     *
     * @param operand1 the first operand.
     * @param operand2 the second operand.
     * @return the result of the min function evaluation.
     * @exception IllegalArgumentException if (!operand1.isConstant() ||
     *            !operand1.isNumerical()) && (!operand2.isFunction() or
     *            !operand2.getSymbol().equals(".")) wher each term is a
     *            numerical constant
     */
    private static Constant minimum(final Term operand1, final Term operand2)
            throws IllegalArgumentException {
        if (!(operand1 instanceof Constant)) {
            throw new IllegalArgumentException(
                    "Assertion: evaluation expression (call min " + operand1
                    + " " + operand2 + "): "
                    + "parameter <operand1> must be a constant");
        }
        final Constant op1 = (Constant) operand1;
        if (!op1.isNumerical()) {
            throw new IllegalArgumentException(
                    "Assertion: evaluation expression (call min "
                    + operand1
                    + " "
                    + operand2
                    + "): "
                    + "parameter <operand2> must be a numerical constant");
        }
        final double d1 = Double.parseDouble(op1.getSymbol());
        if (operand2 instanceof Constant
                && operand2.getSymbol().equals("nil")) {
            return new Constant("()");
        }
        if (!(operand2 instanceof Function)
                && !operand2.getSymbol().equals(".")) {
            throw new IllegalArgumentException(
                    "Assertion: evaluation expression (call min " + operand1
                    + " " + operand2 + "): "
                    + "parameter <operand2> must be a list");
        }
        Term list = operand2;
        while (!list.getSymbol().equals("nil")) {
            final Term elt = ((Function) list).get(0);
            if (!(elt instanceof Constant)) {
                throw new IllegalArgumentException(
                        "Assertion: evaluation expression (call min "
                        + operand1
                        + " "
                        + operand2
                        + "): "
                        + "parameter <operand2> must only contain constant");
            }
            final Constant c = (Constant) elt;
            if (!c.isNumerical()) {
                throw new IllegalArgumentException(
                        "Assertion: evaluation expression (call min "
                        + operand1
                        + " "
                        + operand2
                        + "): "
                        + "parameter <operand2> must only contain numerical "
                        + "constant");
            }
            final double d2 = Double.parseDouble(c.getSymbol());
            if (d1 > d2) {
                return new Constant("nil");
            }
            list = ((Function) list).get(1);
        }
        return new Constant("()");
    }

    /**
     * Returns the result of the member function evaluation.
     *
     * @param operand1 the first operand.
     * @param operand2 the second operand.
     * @return the result of the member function evaluation.
     * @exception IllegalArgumentException if !operand2.isFunction() or
     *            !operand2.getSymbol().equals(".").
     */
    private static Constant member(final Term operand1, final Term operand2)
            throws IllegalArgumentException {
        if (operand2 instanceof Constant
                && operand2.getSymbol().equals("nil")) {
            return new Constant("nil");
        }
        if (!(operand2 instanceof Function)
                && !operand2.getSymbol().equals(".")) {
            throw new IllegalArgumentException(
                    "Assertion: evaluation expression (call member " + operand1
                    + " " + operand2 + "): "
                    + "parameter <operand2> must be a list");
        }
        Term list = operand2;
        while (!list.getSymbol().equals("nil")) {
            final Term elt = ((Function) list).get(0);
            if (operand1.equals(elt)) {
                return new Constant("()");
            }
            list = ((Function) list).get(1);
        }
        return new Constant("nil");
    }

    /**
     * Returns the result of the concatenation function evaluation.
     *
     * @param operand1 the first operand.
     * @param operand2 the second operand.
     * @return the result of the concatenation function evaluation
     *         operand1+operand2
     * @exception IllegalArgumentException operand1 and operand2 are not
     *            constant.
     */
    private static Constant cat(final Term operand1, final Term operand2)
            throws IllegalArgumentException {
        if (!(operand1 instanceof Constant)) {
            throw new IllegalArgumentException(
                    "Assertion: evaluation expression (call cat " + operand1
                    + " " + operand2 + "): "
                    + "parameter <operand1> is not a constant");
        }
        final Constant op1 = (Constant) operand1;
        if (!(operand2 instanceof Constant)) {
            throw new IllegalArgumentException(
                    "Assertion: evaluation expression (call cat " + operand1
                    + " " + operand2 + "): "
                    + "parameter <operand2> is not a constant");
        }
        final Constant op2 = (Constant) operand2;
        return new Constant(operand1.getSymbol() + "-" + operand2.getSymbol());
    }

}
