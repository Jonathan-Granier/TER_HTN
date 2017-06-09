/*
 * Copyright (c) 2010 by Damien Pellier <Damien.Pellier@imag.fr>.
 *
 * This file is part of PDDL4J library.
 *
 * PDDL4J is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PDDL4J is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with PDDL4J.  If not, see <http://www.gnu.org/licenses/>
 */

package pddl4j.preprocessing;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import pddl4j.parser.Connective;
import pddl4j.parser.DerivedPredicate;
import pddl4j.parser.Domain;
import pddl4j.parser.Exp;
import pddl4j.parser.Method;
import pddl4j.parser.NamedTypedList;
import pddl4j.parser.Op;
import pddl4j.parser.Problem;
import pddl4j.parser.RequireKey;
import pddl4j.parser.Symbol;
import pddl4j.parser.TypedSymbol;
import pddl4j.util.IntExp;

/**
 * This class implements all the methods needed to encode operators, goal and initial state loaded from the parser in its integer representation.
 * 
 * @author D. Pellier
 * @version 1.0 - 08.06.2010
 */
final class IntEncoding {
	
	/**
	 * The default constructor with a private access to prevent instance creation.
	 */
	private IntEncoding() {
	}
	
	/**
	 * Encodes all the types of a specified domain.
	 * 
	 * @param domain the domain.
	 */
	static void encodeTypes(final Domain domain) {
		final List<TypedSymbol> types = domain.getTypes();
		final int nbTypes = types.size();
		Preprocessing.tableOfTypes = new ArrayList<String>(nbTypes);
		Preprocessing.tableOfDomains = new ArrayList<Set<Integer>>(nbTypes);
		for (final TypedSymbol type : types) {
			Preprocessing.tableOfTypes.add(type.getImage());
			Preprocessing.tableOfDomains.add(new LinkedHashSet<Integer>());
		}
	}
	
	/**
	 * Collects composite type, i.e., type of the form (either t1 t2), through a specified domain and problem and creates their respective domain.
	 * 
	 * @param domain the domain.
	 * @param problem the problem.
	 */
	static void encodeEitherTypes(final Domain domain, final Problem problem) {
		// Collect the types from the predicates declaration
		for (final NamedTypedList predicate : domain.getPredicates()) {
			IntEncoding.encodeTypes(predicate.getArguments());
		}
		// Collect the types from the functions declaration
		for (final NamedTypedList function : domain.getFunctions()) {
			IntEncoding.encodeTypes(function.getArguments());
		}
		// Collect the types from the constraints declaration of the domain
		if (domain.getConstraints() != null) {
			IntEncoding.encodeTypes(domain.getConstraints());
		}
		// Collect the types from the derived predicates
		for (final DerivedPredicate axiom : domain.getDerivesPredicates()) {
			IntEncoding.encodeTypes(axiom.getHead().getArguments());
			IntEncoding.encodeTypes(axiom.getBody());
		}
		// Collect the type from the operators
		for (final Op op : domain.getOperators()) {
			IntEncoding.encodeTypes(op.getParameters());
			if (op.getDuration() != null) {
				IntEncoding.encodeTypes(op.getDuration());
			}
			IntEncoding.encodeTypes(op.getPreconditions());
			IntEncoding.encodeTypes(op.getEffects());
		}
		// Collect the types from the constraints declaration of the problem
		if (problem.getGoalTasksConstraints() != null) {
			IntEncoding.encodeTypes(problem.getGoalTasksConstraints());
		}
		// Collect the types from the goal declaration of the problem
		if (!problem.getRequirements().contains(RequireKey.HTN)) {
			IntEncoding.encodeTypes(problem.getGoal());
		}
		
	}
	
	/**
	 * Encoded all the type from a specified list of typed symbols.
	 * 
	 * @param list the list of typed symbol.
	 */
	private static void encodeTypes(final List<TypedSymbol> list) {
		for (final TypedSymbol elt : list) {
			final List<Symbol> types = elt.getTypes();
			if (types.size() > 1) {
				String newType = "either";
				final Set<Integer> newTypeDomain = new LinkedHashSet<Integer>();
				for (final Symbol type : types) {
					final String image = type.getImage();
					newType += "~" + image;
					final int typeIndex = Preprocessing.tableOfTypes.indexOf(image);
					final Set<Integer> typeDomain = Preprocessing.tableOfDomains.get(typeIndex);
					newTypeDomain.addAll(typeDomain);
				}
				final int index = Preprocessing.tableOfTypes.indexOf(newType);
				if (index == -1) {
					Preprocessing.tableOfDomains.add(new LinkedHashSet<Integer>(newTypeDomain));
					Preprocessing.tableOfTypes.add(newType);
				}
			}
		}
	}
	
	/**
	 * Encodes all the type from a specified expression.
	 * 
	 * @param exp the expression.
	 */
	private static void encodeTypes(final Exp exp) {
		switch (exp.getConnective()) {
			case AND:
			case OR:
				for (final Exp ei : exp.getChildren()) {
					IntEncoding.encodeTypes(ei);
				}
				break;
			case FORALL:
			case EXISTS:
				IntEncoding.encodeTypes(exp.getVariables());
				IntEncoding.encodeTypes(exp.getChildren().get(0));
				break;
			case F_EXP_T:
				if (!exp.getChildren().isEmpty()) {
					IntEncoding.encodeTypes(exp.getChildren().get(0));
				}
				break;
			case EQUAL:
			case FN_ATOM:
			case WHEN:
			case DURATION_ATOM:
			case LESS:
			case LESS_OR_EQUAL:
			case GREATER:
			case GREATER_OR_EQUAL:
			case MUL:
			case DIV:
			case MINUS:
			case PLUS:
			case ASSIGN:
			case INCREASE:
			case DECREASE:
			case SCALE_UP:
			case SCALE_DOWN:
				IntEncoding.encodeTypes(exp.getChildren().get(0));
				IntEncoding.encodeTypes(exp.getChildren().get(1));
				break;
			case AT_START:
			case AT_END:
			case MINIMIZE:
			case MAXIMIZE:
			case UMINUS:
			case NOT:
			case ALWAYS:
			case OVER_ALL:
			case SOMETIME:
			case AT_MOST_ONCE:
			case F_EXP:
				IntEncoding.encodeTypes(exp.getChildren().get(0));
				break;
			case HOLD_AFTER:
			case WITHIN:
				IntEncoding.encodeTypes(exp.getChildren().get(1));
				break;
			case ALWAYS_WITHIN:
				IntEncoding.encodeTypes(exp.getChildren().get(1));
				IntEncoding.encodeTypes(exp.getChildren().get(2));
				break;
			case HOLD_DURING:
				IntEncoding.encodeTypes(exp.getChildren().get(2));
				break;
			case IS_VIOLATED:
			case NUMBER:
			case ATOM:
			case FN_HEAD:
			case TIME_VAR:
			default:
				break;
		}
	}
	
	/**
	 * Encodes all the constants of the specified domain and the problem.
	 * 
	 * @param domain the domain.
	 * @param problem the problem.
	 */
	static void encodeConstants(final Domain domain, final Problem problem) {
		final List<TypedSymbol> constants = domain.getConstants();
		Preprocessing.tableOfConstants = new ArrayList<String>(domain.getConstants().size());
		constants.addAll(problem.getObjects());
		for (final TypedSymbol constant : constants) {
			int ic = Preprocessing.tableOfConstants.indexOf(constant.getImage());
			if (ic == -1) {
				ic = Preprocessing.tableOfConstants.size();
				Preprocessing.tableOfConstants.add(constant.getImage());
			}
			final LinkedList<Symbol> types = new LinkedList<Symbol>(constant.getTypes());
			while (!types.isEmpty()) {
				final Symbol type = types.poll();
				final int it = Preprocessing.tableOfTypes.indexOf(type.getImage());
				types.addAll(domain.getType(type).getTypes());
				Preprocessing.tableOfDomains.get(it).add(ic);
			}
		}
	}
	
	/**
	 * Encodes all the predicates of a specified domain.
	 * 
	 * @param domain the domain.
	 */
	static void encodePredicates(final Domain domain) {
		final List<NamedTypedList> predicates = domain.getPredicates();
		final int nbPredicates = predicates.size();
		Preprocessing.tableOfPredicates = new ArrayList<String>(nbPredicates);
		Preprocessing.tableOfTypedPredicates = new ArrayList<List<Integer>>(nbPredicates);
		for (final NamedTypedList predicate : predicates) {
			Preprocessing.tableOfPredicates.add(predicate.getName().getImage());
			final List<TypedSymbol> arguments = predicate.getArguments();
			final List<Integer> argType = new ArrayList<Integer>(arguments.size());
			for (final TypedSymbol arg : arguments) {
				final List<Symbol> types = arg.getTypes();
				if (types.size() > 1) {
					final StringBuffer image = new StringBuffer("either");
					for (final Symbol type : types) {
						image.append("~");
						image.append(type.getImage());
					}
					argType.add(Preprocessing.tableOfTypes.indexOf(image.toString()));
				} else {
					argType.add(Preprocessing.tableOfTypes.indexOf(types.get(0).getImage()));
				}
			}
			Preprocessing.tableOfTypedPredicates.add(argType);
		}
	}
	
	/**
	 * Encodes all the function of a specified domain.
	 * 
	 * @param domain the domain.
	 */
	static void encodeFunctions(final Domain domain) {
		final List<NamedTypedList> functions = domain.getFunctions();
		Preprocessing.tableOfFunctions = new ArrayList<String>(functions.size());
		Preprocessing.tableOfTypedFunctions = new ArrayList<List<Integer>>(functions.size());
		for (final NamedTypedList function : functions) {
			Preprocessing.tableOfFunctions.add(function.getName().getImage());
			final List<TypedSymbol> arguments = function.getArguments();
			final List<Integer> argType = new ArrayList<Integer>(arguments.size());
			for (int j = 0; j < arguments.size(); j++) {
				final List<Symbol> types = arguments.get(j).getTypes();
				if (types.size() > 1) {
					final StringBuffer type = new StringBuffer("either");
					for (int k = 0; k < types.size(); k++) {
						type.append("~");
						type.append(types.get(k).getImage());
					}
					argType.add(Preprocessing.tableOfTypes.indexOf(type.toString()));
				} else {
					argType.add(Preprocessing.tableOfTypes.indexOf(types.get(0).getImage()));
				}
			}
			Preprocessing.tableOfTypedFunctions.add(argType);
			
		}
	}
	
	/**
	 * Encodes a specified list of operators into its integer representation.
	 * 
	 * @param ops the list of operators to encode.
	 * @return encoded the list of operators encoded.
	 */
	static List<IntOp> encodeOperators(final List<Op> ops) {
		final List<IntOp> intOps = new ArrayList<IntOp>();
		for (final Op op : ops) {
			intOps.add(IntEncoding.encodeOperator(op));
		}
		return intOps;
	}
	
	/**
	 * Encodes a specified initial state into its integer representation.
	 * 
	 * @param init the initial state to encode.
	 * @return the initial state encoded.
	 */
	static Set<IntExp> encodeInit(final List<Exp> init) {
		final Set<IntExp> intInit = new LinkedHashSet<IntExp>();
		for (final Exp fact : init) {
			intInit.add(IntEncoding.encodeExp(fact));
		}
		return intInit;
	}
	
	/**
	 * Encodes a specified goal into its integer representation.
	 * 
	 * @param goal the goal to encode.
	 * @return the goal encoded.
	 */
	static IntExp encodeGoal(final Exp goal) {
		return IntEncoding.encodeExp(goal);
	}
	
	/**
	 * Encode an operator into its integer representation.
	 * 
	 * @param problem the coded problem that contains the tables of symbols.
	 * @param op the operator to encode.
	 * @return encoded operator.
	 */
	private static IntOp encodeOperator(final Op op) {
		final String name = op.getName().getImage();
		final IntOp intOp = new IntOp(name, op.getArity());
		// Encode the parameters of the operator
		final List<String> variables = new ArrayList<String>(op.getArity());
		for (int i = 0; i < op.getArity(); i++) {
			final TypedSymbol parameter = op.getParameters().get(i);
			final String typeImage = IntEncoding.toStringType(parameter.getTypes());
			final int type = Preprocessing.tableOfTypes.indexOf(typeImage);
			intOp.setTypeOfParameter(i, type);
			variables.add(parameter.getImage());
		}
		// Encode the preconditions of the operator
		final IntExp preconditions = IntEncoding.encodeExp(op.getPreconditions(), variables);
		intOp.setPreconditions(preconditions);
		// Encode the effects of the operator
		final IntExp effects = IntEncoding.encodeExp(op.getEffects(), variables);
		intOp.setEffects(effects);
		return intOp;
	}
	
	/**
	 * Encodes a specified list of methods into its integer representation.
	 * 
	 * @param meths the list of methods to encode.
	 * @return encoded the list of methods encoded.
	 */
	static List<IntMeth> encodeMethods(final List<Method> meths) {
		final List<IntMeth> intMeths = new ArrayList<IntMeth>();
		for (final Method meth : meths) {
			intMeths.add(IntEncoding.encodeMethod(meth));
		}
		return intMeths;
	}
	
	/**
	 * Encode an method into its integer representation.
	 * 
	 * @param problem the coded problem that contains the tables of symbols.
	 * @param meth the method to encode.
	 * @return encoded method.
	 */
	private static IntMeth encodeMethod(final Method meth) {
		final String name = meth.getName().getImage();
		final IntMeth intMeth = new IntMeth(name, meth.getArity(), meth.getUndeclaredParameters().size());
		// Encode the parameters of the method
		final List<String> variables = new ArrayList<String>(meth.getArity() + meth.getUndeclaredParameters().size());
		for (int i = 0; i < meth.getArity(); i++) {
			final TypedSymbol parameter = meth.getParameters().get(i);
			final String typeImage = IntEncoding.toStringType(parameter.getTypes());
			final int type = Preprocessing.tableOfTypes.indexOf(typeImage);
			intMeth.setTypeOfParameter(i, type);
			variables.add(parameter.getImage());
		}
		for (int i = 0; i < meth.getUndeclaredParameters().size(); i++) {
			final TypedSymbol parameter = meth.getUndeclaredParameters().get(i);
			final String typeImage = IntEncoding.toStringType(parameter.getTypes());
			final int type = Preprocessing.tableOfTypes.indexOf(typeImage);
			intMeth.setTypeOfUndeclaredParameter(i, type);
			variables.add(parameter.getImage());
		}
		// Encode the expansion of the method
		final IntExp expansion = IntEncoding.encodeExp(meth.getExpansion(), variables);
		intMeth.setExpansion(expansion);
		// Remove task tag from the constraints
		final Exp constraints = new Exp(meth.getConstraints());
		for (final Exp e : meth.getExpansion().getChildren()) {
			final Exp tag = new Exp(Connective.TASK_TAG);
			tag.setTag(e.getTag());
			constraints.replace(tag, e);
		}
		// Encode the constraints of the method
		intMeth.setConstraints(IntEncoding.encodeExp(constraints, variables));
		return intMeth;
	}
	
	/**
	 * Encode the goal task into its integer representation.
	 * 
	 * @param tasks the expression that defines the goal tasks.
	 * @return the expression in integer representation.
	 */
	static IntExp encodeGoalTasks(final Exp tasks) {
		return IntEncoding.encodeExp(tasks, new ArrayList<String>());
	}
	
	/**
	 * Encode the goal task constraints in its integer representation.
	 * 
	 * @param tasks the expression that defines the goal tasks.
	 * @param constraints the goal task to be encoded.
	 * @return the constraints encoded.
	 */
	static IntExp encodeGoalTasksConstraints(final Exp tasks, final Exp constraints) {
		// Remove task tag from the constraints
		for (final Exp e : tasks.getChildren()) {
			final Exp tag = new Exp(Connective.TASK_TAG);
			tag.setTag(e.getTag());
			constraints.replace(tag, e);
		}
		// Encode and return the constraints
		return IntEncoding.encodeExp(constraints, new ArrayList<String>());
	}
	
	/**
	 * Encodes all the task symbols of a specified domain.
	 * 
	 * @param domain the domain.
	 */
	static void encodeOperatorAndMethodSymbols(final Domain domain) {
		Preprocessing.TableOfTaskSymbols = new ArrayList<String>();
		Preprocessing.TableOfTaskSymbols.add("initTask");
		final List<Method> meths = domain.getMethods();
		for (final Method meth : meths) {
			final String img = meth.getName().getImage();
			if (!Preprocessing.TableOfTaskSymbols.contains(img)) {
				Preprocessing.TableOfTaskSymbols.add(img);
			}
		}
		final List<Op> ops = domain.getOperators();
		for (final Op op : ops) {
			final String img = op.getName().getImage();
			if (!Preprocessing.TableOfTaskSymbols.contains(img)) {
				Preprocessing.TableOfTaskSymbols.add(img);
			}
		}
	}
	
	/**
	 * Encodes an specified expression into its integer representation.
	 * 
	 * @param problem the coded problem that contains the tables of symbols.
	 * @param exp the expression to encode.
	 * @return the integer representation of the specified expression.
	 */
	private static IntExp encodeExp(final Exp exp) {
		return IntEncoding.encodeExp(exp, new ArrayList<String>());
	}
	
	/**
	 * Encodes an specified expression into its integer representation.
	 * <p>
	 * Notes:
	 * <ul>
	 * <li>equal predicate used specified value of -1.</li>
	 * <li>variables used negative values in [-1,-infinity[.</li>
	 * </ul>
	 * 
	 * @param problem the coded problem that contains the tables of symbols.
	 * @param exp the expression to encode.
	 * @param variables the list of variable already encoded.
	 * @return the integer representation of the specified expression.
	 */
	private static IntExp encodeExp(final Exp exp, final List<String> variables) {
		final IntExp intExp = new IntExp(exp.getConnective());
		switch (exp.getConnective()) {
			case EQUAL_ATOM:
				intExp.setPredicate(IntExp.EQUAL_PREDICATE);
				int[] args = new int[exp.getAtom().size()];
				for (int i = 0; i < exp.getAtom().size(); i++) {
					final Symbol argument = exp.getAtom().get(i);
					if (argument.getKind().equals(Symbol.Kind.VARIABLE)) {
						args[i] = -variables.indexOf(argument.getImage()) - 1;
					} else {
						args[i] = Preprocessing.tableOfConstants.indexOf(argument.getImage());
					}
				}
				intExp.setArguments(args);
				break;
			case FN_HEAD:
				final String functor = exp.getAtom().get(0).getImage();
				intExp.setPredicate(Preprocessing.tableOfFunctions.indexOf(functor));
				args = new int[exp.getAtom().size() - 1];
				for (int i = 1; i < exp.getAtom().size(); i++) {
					final Symbol argument = exp.getAtom().get(i);
					if (argument.getKind().equals(Symbol.Kind.VARIABLE)) {
						args[i - 1] = -variables.indexOf(argument.getImage()) - 1;
					} else {
						args[i - 1] = Preprocessing.tableOfConstants.indexOf(argument.getImage());
					}
				}
				intExp.setArguments(args);
				break;
			case ATOM:
				final String predicate = exp.getAtom().get(0).getImage();
				intExp.setPredicate(Preprocessing.tableOfPredicates.indexOf(predicate));
				args = new int[exp.getAtom().size() - 1];
				for (int i = 1; i < exp.getAtom().size(); i++) {
					final Symbol argument = exp.getAtom().get(i);
					if (argument.getKind().equals(Symbol.Kind.VARIABLE)) {
						args[i - 1] = -variables.indexOf(argument.getImage()) - 1;
					} else {
						args[i - 1] = Preprocessing.tableOfConstants.indexOf(argument.getImage());
					}
				}
				intExp.setArguments(args);
				break;
			case TASK:
				final String task = exp.getAtom().get(0).getImage();
				intExp.setPredicate(Preprocessing.TableOfTaskSymbols.indexOf(task));
				args = new int[exp.getAtom().size() - 1];
				for (int i = 1; i < exp.getAtom().size(); i++) {
					final Symbol argument = exp.getAtom().get(i);
					if (argument.getKind().equals(Symbol.Kind.VARIABLE)) {
						args[i - 1] = -variables.indexOf(argument.getImage()) - 1;
					} else {
						args[i - 1] = Preprocessing.tableOfConstants.indexOf(argument.getImage());
					}
				}
				intExp.setArguments(args);
				break;
			case AND:
			case OR:
				for (int i = 0; i < exp.getChildren().size(); i++) {
					intExp.getChildren().add(IntEncoding.encodeExp(exp.getChildren().get(i), variables));
				}
				break;
			case FORALL:
			case EXISTS:
				final List<String> newVariables = new ArrayList<String>(variables);
				final List<TypedSymbol> qvar = exp.getVariables();
				final String type = IntEncoding.toStringType(qvar.get(0).getTypes());
				final int typeIndex = Preprocessing.tableOfTypes.indexOf(type);
				intExp.setType(typeIndex);
				intExp.setVariable(-variables.size() - 1);
				newVariables.add(qvar.get(0).getImage());
				if (qvar.size() == 1) {
					intExp.getChildren().add(IntEncoding.encodeExp(exp.getChildren().get(0), newVariables));
				} else {
					qvar.remove(0);
					intExp.getChildren().add(IntEncoding.encodeExp(exp, newVariables));
				}
				break;
			case F_EXP_T:
				if (!exp.getChildren().isEmpty()) {
					intExp.getChildren().add(IntEncoding.encodeExp(exp.getChildren().get(0), variables));
				}
				break;
			case FN_ATOM:
			case WHEN:
			case DURATION_ATOM:
			case LESS:
			case LESS_OR_EQUAL:
			case EQUAL:
			case GREATER:
			case GREATER_OR_EQUAL:
			case ASSIGN:
			case INCREASE:
			case DECREASE:
			case SCALE_UP:
			case SCALE_DOWN:
			case MUL:
			case DIV:
			case MINUS:
			case PLUS:
			case SOMETIME_AFTER:
			case SOMETIME_BEFORE:
			case WITHIN:
			case HOLD_AFTER:
				intExp.getChildren().add(IntEncoding.encodeExp(exp.getChildren().get(0), variables));
				intExp.getChildren().add(IntEncoding.encodeExp(exp.getChildren().get(1), variables));
				break;
			case AT_START:
			case AT_END:
			case MINIMIZE:
			case MAXIMIZE:
			case UMINUS:
			case NOT:
			case ALWAYS:
			case OVER_ALL:
			case SOMETIME:
			case AT_MOST_ONCE:
			case F_EXP:
				intExp.getChildren().add(IntEncoding.encodeExp(exp.getChildren().get(0), variables));
				break;
			case NUMBER:
				intExp.setValue(exp.getValue());
				break;
			case ALWAYS_WITHIN:
			case HOLD_DURING:
				intExp.getChildren().add(IntEncoding.encodeExp(exp.getChildren().get(0), variables));
				intExp.getChildren().add(IntEncoding.encodeExp(exp.getChildren().get(1), variables));
				intExp.getChildren().add(IntEncoding.encodeExp(exp.getChildren().get(2), variables));
				break;
			case TIME_VAR:
			case IS_VIOLATED:
				// Do nothing
				break;
			case BEFORE:
			case AFTER:
			case BETWEEN:
			case SERIES:
			case EXPANSION:
			case FIRST:
			case LAST:
				for (int i = 0; i < exp.getChildren().size(); i++) {
					intExp.getChildren().add(IntEncoding.encodeExp(exp.getChildren().get(i), variables));
				}
				if (exp.getConnective().equals(Connective.FIRST)) {
					break;
				}
			default:
				break;
		}
		return intExp;
	}
	
	/**
	 * Returns the string representation of a list of types.
	 * 
	 * @param types the list of types.
	 * @return the string representation of this type.
	 */
	private static String toStringType(final List<Symbol> types) {
		final StringBuffer str = new StringBuffer();
		if (types.size() > 1) {
			str.append("either");
			for (final Symbol type : types) {
				str.append("~");
				str.append(type.getImage());
			}
		} else {
			str.append(types.get(0).getImage());
		}
		return str.toString();
	}
}