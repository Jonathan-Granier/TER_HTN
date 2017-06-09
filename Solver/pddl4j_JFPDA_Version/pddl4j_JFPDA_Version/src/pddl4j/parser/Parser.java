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

package pddl4j.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import pddl4j.parser.Symbol.Kind;
import pddl4j.parser.lexer.Lexer;
import pddl4j.util.AdjacencyMatrix;

/**
 * Implements the <tt>Parser</tt> of the PDD4L library. The parser accepts only PDDL3.0 language. See BNF Description of PDDL3.0 - Alfonso Gerevini and Derek
 * Long for more details.
 * <p>
 * A simple example of how to use the parser:
 * 
 * <pre>
 * public static void main(String[] args) {
 * 
 * 	if (args.length == 2 &amp;&amp; args[0].equals(&quot;-p&quot;)) {
 * 		Parser parser = new Parser();
 * 		try {
 * 			parser.parse(args[1]);
 * 		} catch (FileNotFoundException e) {
 * 			System.out.println(e.getMessage());
 * 		}
 * 		if (!parser.getErrorManager().isEmpty()) {
 * 			parser.getErrorManager().printAll();
 * 		}
 * 	} else if (args.length == 4 &amp;&amp; args[0].equals(&quot;-o&quot;) &amp;&amp; args[2].equals(&quot;-f&quot;)) {
 * 		Parser parser = new Parser();
 * 		try {
 * 			parser.parse(args[1], args[3]);
 * 		} catch (FileNotFoundException e) {
 * 			System.out.println(e.getMessage());
 * 		}
 * 		if (!parser.getErrorManager().isEmpty()) {
 * 			parser.mgr.printAll();
 * 		}
 * 	} else {
 * 		System.out.println(&quot;\nusage of parser:\n&quot;);
 * 		System.out.println(&quot;OPTIONS   DESCRIPTIONS\n&quot;);
 * 		System.out.println(&quot;-p &lt;str&gt;    path for operator and fact file&quot;);
 * 		System.out.println(&quot;-o &lt;str&gt;    operator file name&quot;);
 * 		System.out.println(&quot;-f &lt;str&gt;    fact file name\n&quot;);
 * 	}
 * }
 * </pre>
 * </p>
 * 
 * @author D Pellier
 * @version 1.0 - 28.01.10
 */
public final class Parser {
	
	/**
	 * The specific symbol object.
	 */
	public static final Symbol OBJECT = new Symbol(Symbol.Kind.TYPE, "object");
	
	/**
	 * The specific symbol number.
	 */
	public static final Symbol NUMBER = new Symbol(Symbol.Kind.TYPE, "number");
	
	/**
	 * The specific symbol total-costs.
	 */
	public static final Symbol TOTAL_COST = new Symbol(Symbol.Kind.FUNCTOR, "total-cost");
	
	/**
	 * The specific symbol total-costs.
	 */
	public static final Symbol TOTAL_TIME = new Symbol(Symbol.Kind.FUNCTOR, "total-time");
	
	/**
	 * The error manager of the parser.
	 */
	private final ErrorManager mgr;
	
	/**
	 * The lexer used.
	 */
	private Lexer lexer;
	
	/**
	 * The planning domain parsed.
	 */
	private Domain domain;
	
	/**
	 * The planning problem parsed.
	 */
	private Problem problem;
	
	/**
	 * The list of methods signatures (Name + Typed Parameters)
	 */
	private List<NamedTypedList> methodsSignatures;
	
	/**
	 * The list of operators signatures (Name + Typed Parameters)
	 */
	private List<NamedTypedList> operatorsSignatures;
	
	/**
	 * Create a new <tt>Parser</tt>.
	 */
	public Parser() {
		super();
		mgr = new ErrorManager();
	}
	
	/**
	 * Parses a planning domain and a planning problem from the specified file.
	 * 
	 * @param problem the path of the file that contains the planning domains and problem.
	 * @throws FileNotFoundException if the specified problem file does not exist.
	 */
	public void parse(final String problem)
	throws FileNotFoundException {
		this.parse(new File(problem));
	}
	
	/**
	 * Parses a planning domain and a planning problem from the specified file.
	 * 
	 * @param problem the file that contains the planning domains and problem.
	 * @throws FileNotFoundException if the specified problem file does not exist.
	 */
	public void parse(final File problem)
	throws FileNotFoundException {
		if (!problem.exists()) {
			throw new FileNotFoundException("File  \"" + problem.getName() + "\" does not exist.");
		}
		try {
			lexer = new Lexer(new FileInputStream(problem));
			lexer.setErrorManager(mgr);
			lexer.setFile(problem);
			lexer.domain_and_problem();
			domain = lexer.getDomain();
			this.problem = lexer.getProblem();
			checkTypesDeclaration();
			checkConstantsDeclaration();
			checkPredicatesDeclaration();
			checkFunctionsDeclaration();
			checkOperatorsDeclaration();
			checkDerivedPredicatesDeclaration();
		} catch (final Throwable e) {
			// System.out.println("\nUnexpected error:");
			// e.printStackTrace(System.out);
		}
	}
	
	/**
	 * Parses a planning domain from a specific file.
	 * 
	 * @param domain the file that contains the planning domains.
	 * @throws FileNotFoundException if the specified domain or problem file does not exist.
	 */
	public void parseDomain(final String domain)
	throws FileNotFoundException {
		final File d = new File(domain);
		if (!d.exists()) {
			throw new FileNotFoundException("File  \"" + d.getName() + "\" does not exist.");
		}
		try {
			// Parse and check the domain
			lexer = new Lexer(new FileInputStream(d));
			lexer.setErrorManager(mgr);
			lexer.setFile(d);
			lexer.domain();
			this.domain = lexer.getDomain();
			checkTypesDeclaration();
			checkConstantsDeclaration();
			checkPredicatesDeclaration();
			checkFunctionsDeclaration();
			checkDomainConstraints();
			checkOperatorsDeclaration();
			checkDerivedPredicatesDeclaration();
		} catch (final Throwable e) {
			System.out.println("\nUnexpected error:");
			e.printStackTrace(System.out);
		}
	}
	
	/**
	 * Parses a planning problem from a specific file.
	 * 
	 * @param problem the file that contains the planning problem.
	 * @throws FileNotFoundException if the specified domain or problem file does not exist.
	 */
	public void parseProblem(final File problem)
	throws FileNotFoundException {
		if (!problem.exists()) {
			throw new FileNotFoundException("File  \"" + problem.getName() + "\" does not exist.");
		}
		try {
			// Parse and check the domain
			lexer = new Lexer(new FileInputStream(problem));
			lexer.setErrorManager(mgr);
			lexer.setFile(problem);
			lexer.problem();
			this.problem = lexer.getProblem();
			checkDomainName();
			checkObjectsDeclaration();
			checkInitialFacts();
			checkGoal();
			checkProblemConstraints();
			checkMetric();
		} catch (final Throwable e) {
			// System.out.println("\nUnexpected error:");
			// e.printStackTrace(System.out);
		}
	}
	
	/**
	 * Parses a planning domain and a planning problem from their respective files.
	 * 
	 * @param domain the path of the file that contains the planning domains.
	 * @param problem the path of the file that contains the planning problem.
	 * @throws FileNotFoundException if the specified domain or problem file does not exist.
	 */
	public void parse(final String domain, final String problem)
	throws FileNotFoundException {
		this.parse(new File(domain), new File(problem));
	}
	
	/**
	 * Parses a planning domain and a planning problem from their respective files.
	 * 
	 * @param domain the file that contains the planning domains.
	 * @param problem the file that contains the planning problem.
	 * @throws FileNotFoundException if the specified domain or problem file does not exist.
	 */
	private void parse(final File domain, final File problem)
	throws FileNotFoundException {
		if (!domain.exists()) {
			throw new FileNotFoundException("File  \"" + domain.getName() + "\" does not exist.");
		}
		if (!problem.exists()) {
			throw new FileNotFoundException("File  \"" + problem.getName() + "\" does not exist.");
		}
		try {
			// Parse and check the domain
			lexer = new Lexer(new FileInputStream(domain));
			lexer.setErrorManager(mgr);
			lexer.setFile(domain);
			lexer.domain();
			this.domain = lexer.getDomain();
			checkTypesDeclaration();
			checkConstantsDeclaration();
			checkPredicatesDeclaration();
			checkFunctionsDeclaration();
			checkDomainConstraints();
			checkOperatorsDeclaration();
			checkMethodsDeclaration();
			checkDerivedPredicatesDeclaration();
			// Parse and check the problem
			if (lexer == null) {
				lexer = new Lexer(new FileInputStream(problem));
			} else {
				lexer.ReInit(new FileInputStream(problem));
			}
			lexer.setFile(problem);
			lexer.problem();
			
			this.problem = lexer.getProblem();
			checkDomainName();
			checkObjectsDeclaration();
			checkInitialFacts();
			if (this.problem.getRequirements().contains(RequireKey.HTN)) {
				checkHTNGoal();
			} else {
				checkGoal();
				checkProblemConstraints();
			}
			checkMetric();
		} catch (final Throwable e) {
			System.err.println("\nUnexpected error:");
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns the domain parsed.
	 * 
	 * @return the domain parsed.
	 */
	public final Domain getDomain() {
		return domain;
	}
	
	/**
	 * Returns the problem parsed.
	 * 
	 * @return the problem parsed.
	 */
	public final Problem getProblem() {
		return problem;
	}
	
	/**
	 * Check if the metric declared in the domain is well formed.
	 * 
	 * @return <code>true</code> if the metric declared in the domain is well formed; <code>false<code> otherwise.
	 */
	private boolean checkMetric() {
		return (problem.getMetric() == null) ? true : checkParserNode(problem.getMetric(), new LinkedList<TypedSymbol>(), null);
	}
	
	/**
	 * Check if the constraints declared in the domain are well formed.
	 * 
	 * @return <code>true</code> if the constraints declared in the domain are well formed; <code>false<code> otherwise.
	 */
	private boolean checkDomainConstraints() {
		return (domain.getConstraints() == null) ? true : checkParserNode(domain.getConstraints(), new LinkedList<TypedSymbol>(), null);
	}
	
	/**
	 * Check if the constraints declared in the domain are well formed.
	 * 
	 * @return <code>true</code> if the constraints declared in the domain are well formed; <code>false<code> otherwise.
	 */
	private boolean checkProblemConstraints() {
		return (problem.getGoalTasksConstraints() == null) ? true : checkGroundedParserNode(problem.getGoalTasksConstraints());
	}
	
	/**
	 * Check if the goal is well formed.
	 * 
	 * @return <code>true</code> if the goal is well formed; <code>false<code> otherwise.
	 */
	private boolean checkGoal() {
		return checkGroundedParserNode(problem.getGoal());
	}
	
	/**
	 * Check if a specified ground PDDL expression is well formed.
	 * 
	 * @param exp The expression.
	 * @return <code>true</code> if a specified ground PDDL expression is well formed; <code>false<code> otherwise.
	 */
	private boolean checkGroundedParserNode(final Exp exp) {
		boolean checked = true;
		if (exp == null) {
			return checked;
		}
		final LinkedList<Exp> stackGD = new LinkedList<Exp>();
		final LinkedList<List<TypedSymbol>> stackCtx = new LinkedList<List<TypedSymbol>>();
		stackGD.add(exp);
		stackCtx.add(new LinkedList<TypedSymbol>());
		while (!stackGD.isEmpty()) {
			final Exp gd = stackGD.poll();
			final List<TypedSymbol> ctx = stackCtx.poll();
			final List<TypedSymbol> newCtx = new LinkedList<TypedSymbol>(ctx);
			switch (gd.getConnective()) {
				case ATOM:
				case FN_HEAD:
				case EQUAL_ATOM:
					boolean error = false;
					final List<Symbol> atom = gd.getAtom();
					final NamedTypedList atomSkeleton = new NamedTypedList(atom.get(0));
					for (int i = 1; i < atom.size(); i++) {
						final Symbol symbol = atom.get(i);
						final Iterator<TypedSymbol> j = ctx.iterator();
						TypedSymbol qvar = null;
						while (j.hasNext() && qvar == null) {
							final TypedSymbol vj = j.next();
							if (vj.equals(symbol)) {
								qvar = vj;
							}
						}
						if (symbol.getKind().equals(Symbol.Kind.VARIABLE) && qvar == null) {
							mgr.logParserError("variable \"" + symbol + "\" is undefined", lexer.getFile(), symbol.getBeginLine(), symbol.getBeginColumn());
							error = true;
						} else {
							TypedSymbol object = null;
							if (qvar != null) {
								object = qvar;
							}
							if (object == null) {
								object = problem.getObject(symbol);
							}
							if (object == null) {
								object = domain.getConstant(symbol);
							}
							if (object == null) {
								mgr.logParserError("object \"" + symbol + "\" is undefined", lexer.getFile(), symbol.getBeginLine(), symbol.getBeginColumn());
								error = true;
							} else {
								for (final Symbol type : object.getTypes()) {
									if (!domain.isDeclaredType(type)) {
										mgr.logParserError("type \"" + type.getImage() + "\" of the object \"" + object.getImage() + "\" is undefined", lexer.getFile(), type.getBeginLine(), type.getBeginColumn());
										error = true;
									}
								}
								atomSkeleton.add(object);
							}
						}
						checked = !error;
					}
					if (checked && gd.getConnective().equals(Connective.ATOM) && !isDeclaredPredicate(atomSkeleton)) {
						mgr.logParserError("predicate \"" + atomSkeleton.getName() + atomSkeleton.getArguments() + "\" is undefined", lexer.getFile(), atomSkeleton.getName().getBeginLine(), atomSkeleton.getName().getBeginColumn());
						checked = false;
					} else if (checked && gd.getConnective().equals(Connective.FN_HEAD) && !isDeclaredFunction(atomSkeleton)) {
						mgr.logParserError("function \"" + atomSkeleton.getName() + "/" + atomSkeleton.getArguments().size() + "\" is undefined", lexer.getFile(), atomSkeleton.getName().getBeginLine(), atomSkeleton.getName().getBeginColumn());
						checked = false;
					}
					break;
				case EXISTS:
				case FORALL:
					for (final TypedSymbol variable : gd.getVariables()) {
						error = false;
						for (final Symbol type : variable.getTypes()) {
							if (!domain.isDeclaredType(type)) {
								mgr.logParserError("type \"" + type.getImage() + "\" used in quantified expression is undefined", lexer.getFile(), type.getBeginLine(), type.getBeginColumn());
								error = true;
							}
						}
						checked = !error;
						if (checked) {
							newCtx.add(variable);
						}
					}
			}
			for (int i = 0; i < gd.getChildren().size(); i++) {
				stackCtx.add(newCtx);
				stackGD.add(gd.getChildren().get(i));
			}
		}
		return checked;
	}
	
	/**
	 * Check the initial facts of the problem.
	 * 
	 * @return <code>true</code> if the initial facts are well formed; <code>false</code> otherwise.
	 */
	private boolean checkInitialFacts() {
		boolean checked = true;
		final LinkedList<Exp> stackGD = new LinkedList<Exp>();
		stackGD.addAll(problem.getInit());
		while (!stackGD.isEmpty()) {
			final Exp gd = stackGD.poll();
			switch (gd.getConnective()) {
				case ATOM:
				case FN_ATOM:
					boolean error = false;
					List<Symbol> atom = gd.getAtom();
					if (atom == null) {
						atom = gd.getChildren().get(0).getAtom();
					}
					final NamedTypedList atomSkeleton = new NamedTypedList(atom.get(0));
					for (int i = 1; i < atom.size(); i++) {
						final Symbol symbol = atom.get(i);
						TypedSymbol object = problem.getObject(symbol);
						if (object == null) {
							object = domain.getConstant(symbol);
						}
						if (object == null) {
							mgr.logParserError("object \"" + atom.get(i) + "\" is undefined", lexer.getFile(), symbol.getBeginLine(), symbol.getBeginColumn());
							error = true;
						} else {
							for (final Symbol type : object.getTypes()) {
								if (!domain.isDeclaredType(type)) {
									mgr.logParserError("type \"" + type.getImage() + "\" of the object \"" + object.getImage() + "\" is undefined", lexer.getFile(), type.getBeginLine(), type.getBeginColumn());
									error = true;
								}
							}
							atomSkeleton.add(object);
						}
					}
					checked = !error;
					if (checked && gd.getConnective().equals(Connective.ATOM) && !isDeclaredPredicate(atomSkeleton)) {
						mgr.logParserError("predicate \"" + atomSkeleton.getName() + "/" + atomSkeleton.getArguments() + "\" is undefined", lexer.getFile(), atomSkeleton.getName().getBeginLine(), atomSkeleton.getName().getBeginColumn());
						checked = false;
					} else if (checked && gd.getConnective().equals(Connective.FN_ATOM) && !isDeclaredFunction(atomSkeleton)) {
						mgr.logParserError("function \"" + atomSkeleton.getName() + "/" + atomSkeleton.getArguments().size() + "\" is undefined", lexer.getFile(), atomSkeleton.getName().getBeginLine(), atomSkeleton.getName().getBeginColumn());
						checked = false;
					}
					break;
				case DURATION_ATOM:
					stackGD.add(gd.getChildren().get(1));
					break;
				case NOT:
					stackGD.add(gd.getChildren().get(0));
					break;
			}
		}
		
		return checked;
	}
	
	/**
	 * Check the objects declaration.
	 * 
	 * @return <code>true</code> if the objects are well declared; <code>false</code> otherwise.
	 */
	private boolean checkObjectsDeclaration() {
		boolean checked = true;
		final List<TypedSymbol> objects = problem.getObjects();
		for (final TypedSymbol object : objects) {
			for (final Symbol type : object.getTypes()) {
				if (!domain.isDeclaredType(type)) {
					mgr.logParserError("type \"" + type.getImage() + "\" of the object \"" + object.getImage() + "\" is undefined", lexer.getFile(), type.getBeginLine(), type.getBeginColumn());
					checked = false;
				}
			}
		}
		return checked;
	}
	
	/**
	 * Check if the domain name specified in the problem is the same use in the domain.
	 * 
	 * @return <code>true</code> if the domain name specified in the problem is the same use in the domain; <code>false</code> otherwise.
	 */
	private boolean checkDomainName() {
		boolean checked = true;
		if (domain.getName() != null && !problem.getDomain().equals(problem.getDomain())) {
			mgr.logParserWarning("domain name \"" + problem.getDomain() + "\" used in problem doest not match.", lexer.getFile(), problem.getDomain().getBeginLine(), problem.getDomain().getBeginColumn());
			checked = false;
		}
		return checked;
	}
	
	/**
	 * Checks the types declaration. The types are well formed if the hierarchy is consistent (multiple heritage is allowed).
	 * 
	 * @return <code>true</code> if the types declaration is consistent; <code>false</code> otherwise.
	 */
	private boolean checkTypesDeclaration() {
		
		final List<TypedSymbol> types = domain.getTypes();
		boolean checked = true;
		
		if (types.isEmpty()) {
			checked = true;
		} else if (types.size() == 1 && types.get(0).equals(Parser.OBJECT)) {
			checked = true;
		} else {
			boolean typed_object_declared = false;
			final Set<Symbol> set = new HashSet<Symbol>();
			final Set<Symbol> typesToAdd = new HashSet<Symbol>();
			for (final TypedSymbol type : types) {
				if (type.equals(Parser.OBJECT) && !type.getTypes().isEmpty()) {
					mgr.logParserError("type \"" + type.getImage() + "\" cannot be used as subtype", lexer.getFile(), type.getBeginLine(), type.getBeginColumn());
				}
				/*
				 * if (type.equals(Parser.NUMBER) && !type.getTypes().isEmpty()) {
				 * this.mgr.logParserError("type \"" + type.getImage() +
				 * "\" cannot be used as subtype", this.lexer.getFile(), type
				 * .getBeginLine(), type.getBeginColumn()); }
				 */
				if (set.contains(type)) {
					checked = false;
					mgr.logParserError("type \"" + type.getImage() + "\" already defined", lexer.getFile(), type.getBeginLine(), type.getBeginColumn());
				} else {
					set.add(type);
				}
				for (final Symbol superType : type.getTypes()) {
					typed_object_declared |= superType.equals(Parser.OBJECT);
					if (!set.contains(superType) && !typesToAdd.contains(superType)) {
						if (typed_object_declared) {
							checked = false;
							mgr.logParserError("type \"" + superType.getImage() + "\" undefined", lexer.getFile(), superType.getBeginLine(), superType.getBeginColumn());
						} else {
							typesToAdd.add(superType);
						}
					}
				}
			}
			for (final Symbol type : typesToAdd) {
				domain.getTypes().add(new TypedSymbol(type));
			}
		}
		return checked;
	}
	
	/**
	 * Checks the constants declaration. More precisely, checks if each constant is defined ones and if the domain is typed if the types of the constants was
	 * previously defined as types.
	 * 
	 * @return <code>true</code> if the constants declaration are well formed; <code>false</code> otherwise.
	 */
	private boolean checkConstantsDeclaration() {
		final List<TypedSymbol> constants = domain.getConstants();
		final Set<Symbol> set = new HashSet<Symbol>();
		boolean checked = true;
		for (final TypedSymbol constant : constants) {
			if (!set.add(constant)) {
				mgr.logParserError("constant \"" + constant.getImage() + "\" already defined", lexer.getFile(), constant.getBeginLine(), constant.getBeginColumn());
				checked = false;
			}
			for (final Symbol type : constant.getTypes()) {
				if (!domain.isDeclaredType(type)) {
					mgr.logParserError("type \"" + type.getImage() + "\" of the constant \"" + constant.getImage() + "\" is undefined", lexer.getFile(), constant.getBeginLine(), constant.getBeginColumn());
				}
				checked = false;
			}
		}
		return checked;
	}
	
	/**
	 * Checks the predicates declaration. More precisely, this method checks, if the domain is typed, if each types of the variables used in the predicates
	 * declaration are defined, if there is no duplicated predicate.
	 * 
	 * @return <code>true</code> if the predicates declaration are well formed; <code>false</code> otherwise.
	 */
	private boolean checkPredicatesDeclaration() {
		final List<NamedTypedList> predicates = domain.getPredicates();
		final Set<String> set = new HashSet<String>();
		boolean checked = true;
		for (final NamedTypedList predicate : predicates) {
			for (final TypedSymbol variable : predicate.getArguments()) {
				for (final Symbol type : variable.getTypes()) {
					if (!domain.isDeclaredType(type)) {
						mgr.logParserError("type \"" + type.getImage() + "\" of the variable \"" + variable.getImage() + "\" is undefined", lexer.getFile(), variable.getBeginLine(), variable.getBeginColumn());
						checked = false;
					}
				}
			}
			final Symbol predicate_symbol = predicate.getName();
			final String str = predicate_symbol.getImage() + "/" + predicate.getArguments().size();
			
			if (!set.add(str)) {
				mgr.logParserError("predicate \"" + str + "\" declared twice", lexer.getFile(), predicate_symbol.getBeginLine(), predicate_symbol.getBeginColumn());
				checked = false;
			}
		}
		return checked;
	}
	
	/**
	 * Checks the functions declaration. More precisely, this method checks, if the domain is typed, if each types of the variables used in the function
	 * declaration are defined, if there is no duplicated functions and conflict with predicates already defined.
	 * 
	 * @return <code>true</code> if the function declaration are well formed; <code>false</code> otherwise.
	 */
	private boolean checkFunctionsDeclaration() {
		
		final Set<String> predicates = new HashSet<String>();
		for (final NamedTypedList predicate : domain.getPredicates()) {
			final Symbol predicate_symbol = predicate.getName();
			final String str = predicate_symbol.getImage() + "/" + predicate.getArguments().size();
			predicates.add(str);
		}
		
		final List<NamedTypedList> functions = domain.getFunctions();
		final Set<String> set = new HashSet<String>();
		boolean checked = true;
		for (final NamedTypedList function : functions) {
			for (final TypedSymbol variable : function.getArguments()) {
				for (final Symbol type : variable.getTypes()) {
					if (!domain.isDeclaredType(type)) {
						mgr.logParserError("type \"" + type.getImage() + "\" of the variable \"" + variable.getImage() + "\" is undefined", lexer.getFile(), variable.getBeginLine(), variable.getBeginColumn());
						checked = false;
					}
				}
			}
			final Symbol function_symbol = function.getName();
			final String str = function_symbol.getImage() + "/" + function.getArguments().size();
			if (!set.add(str)) {
				mgr.logParserError("predicate \"" + str + "\" declared twice", lexer.getFile(), function_symbol.getBeginLine(), function_symbol.getBeginColumn());
				checked = false;
			}
			if (predicates.contains(str)) {
				mgr.logParserError("function \"" + str + "\" is ambiguous with a predicate already declared", lexer.getFile(), function_symbol.getBeginLine(), function_symbol.getBeginColumn());
				checked = false;
			}
		}
		return checked;
	}
	
	/**
	 * Checks if the declared derived predicates are well formed.
	 * 
	 * @return <code>true</code> if the declared derived predicates are well formed; <code>false</code> otherwise.
	 */
	private boolean checkDerivedPredicatesDeclaration() {
		boolean checked = true;
		for (final DerivedPredicate axiom : domain.getDerivesPredicates()) {
			final NamedTypedList head = axiom.getHead();
			for (final TypedSymbol argument : head.getArguments()) {
				for (final Symbol type : argument.getTypes()) {
					if (!domain.isDeclaredType(type)) {
						mgr.logParserError("type \"" + type.getImage() + "\" used in derived predicate", lexer.getFile(), type.getBeginLine(), type.getBeginColumn());
						checked = false;
					}
				}
			}
			if (checked && !isDeclaredPredicate(head)) {
				mgr.logParserError("predicate \"" + head.getName() + head.getArguments() + "\" is undefined", lexer.getFile(), head.getName().getBeginLine(), head.getName().getBeginColumn());
				checked = false;
			}
			if (checked) {
				checked = checkParserNode(axiom.getBody(), head.getArguments(), null);
			}
		}
		return checked;
	}
	
	/**
	 * Checks if the declared operators
	 * <ul>
	 * <li>have a unique name</li>
	 * <li>the type of the variables or constants used in their precondition, condition and effects are type previously declared.</li>
	 * <li>the variable used in their precondition, condition and effects are declared as parameters of the operators.</li>
	 * </ul>
	 * 
	 * @return <code>true</code> if the function declaration are well formed; <code>false</code> otherwise.
	 */
	private boolean checkOperatorsDeclaration() {
		boolean checked = checkOperatorsUniqueness();
		for (final Op op : domain.getOperators()) {
			if (checkOperatorsParameters(op)) {
				checked = checkParserNode(op.getPreconditions(), op.getParameters(), null);
				checked = checkParserNode(op.getEffects(), op.getParameters(), null);
				if (op.getDuration() != null) {
					checked = checkParserNode(op.getDuration(), op.getParameters(), null);
				}
			}
		}
		return checked;
	}
	
	private boolean checkMethodsDeclaration() {
		boolean noError = true;
		// Checking if the method is unique
		noError = checkMethodsUniqueness();
		if (!noError) {
			return false; // Stop checking if the noError is false
		}
		
		// Create two lists containing methods and operators signatures
		methodsSignatures = new ArrayList<NamedTypedList>();
		operatorsSignatures = new ArrayList<NamedTypedList>();
		
		for (final Method meth : domain.getMethods()) {
			final NamedTypedList signature = new NamedTypedList(meth.getName());
			signature.addArguments(meth.getParameters());
			methodsSignatures.add(signature);
		}
		for (final Op op : domain.getOperators()) {
			final NamedTypedList signature = new NamedTypedList(op.getName());
			signature.addArguments(op.getParameters());
			operatorsSignatures.add(signature);
		}
		
		// Checking all the methods
		for (final Method method : domain.getMethods()) {
			final Set<Symbol> tagSet = new HashSet<Symbol>();
			noError = checkMethodsParameters(method);
			if (!noError) {
				return false; // Stop checking if the noError is false
			}
			noError = checkMethodsExpansion(method, tagSet);
		}
		return noError;
	}
	
	/**
	 * Checks if a PDDL expression such as the preconditions, the effects and the duration of an operator is well formed. More precisely, check if all variables
	 * are well typed and are valid parameters of the operator or quantified variable and finally, if all atoms match a predicate previously declared.
	 * 
	 * @param exp The PDDL expression.
	 * @param context The symbolEncoding.
	 * @return <code>true</code> if the expression is well formed; <code>false</code> otherwise.
	 */
	private boolean checkParserNode(final Exp exp, final List<TypedSymbol> context, final Method method) {
		boolean checked = true;
		final LinkedList<Exp> stackGD = new LinkedList<Exp>();
		final LinkedList<List<TypedSymbol>> stackCtx = new LinkedList<List<TypedSymbol>>();
		stackGD.add(exp);
		stackCtx.add(context);
		while (!stackGD.isEmpty()) {
			final Exp gd = stackGD.poll();
			final List<TypedSymbol> ctx = stackCtx.poll();
			final List<TypedSymbol> newCtx = new LinkedList<TypedSymbol>(ctx);
			switch (gd.getConnective()) {
				case ATOM:
				case FN_HEAD:
					checked = checkAtom(gd, ctx, method);
					break;
				case EXISTS:
				case FORALL:
					for (final TypedSymbol variable : gd.getVariables()) {
						boolean error = false;
						for (final Symbol type : variable.getTypes()) {
							if (!domain.isDeclaredType(type)) {
								mgr.logParserError("type \"" + type.getImage() + "\" used in quantified expression is undefined", lexer.getFile(), type.getBeginLine(), type.getBeginColumn());
								error = true;
							}
						}
						checked = !error;
						if (checked) {
							newCtx.add(variable);
						}
					}
			}
			for (int i = 0; i < gd.getChildren().size(); i++) {
				stackCtx.add(0, newCtx);
				stackGD.add(0, gd.getChildren().get(i));
			}
		}
		return checked;
	}
	
	/**
	 * Check if an atom used is well typed and if it was previously declared in the predicates of the domain.
	 * 
	 * @param gd The atom goal description.
	 * @param parameters The parameters that defined the global symbolEncoding, e.g., parameters of an operator or the types variable of derived predicate.
	 * @param context The symbolEncoding, i.e., the quantified variables if any.
	 * @return <code>true</code> if an atom used in is well typed and if it was previously declared in the predicates of the domain; <code>false</code>
	 *         otherwise.
	 */
	private boolean checkAtom(final Exp gd, final List<TypedSymbol> context, final Method method) {
		List<TypedSymbol> undeclaredParameters = new ArrayList<TypedSymbol>();
		if (method != null) {
			undeclaredParameters = method.getUndeclaredParameters();
		}
		
		boolean checked = true;
		final List<Symbol> atom = gd.getAtom();
		final NamedTypedList atomSkeleton = new NamedTypedList(atom.get(0));
		for (int i = 1; i < atom.size(); i++) {
			final Symbol s = atom.get(i);
			if (s.getKind().equals(Symbol.Kind.VARIABLE)) {
				TypedSymbol param = null;
				final Iterator<TypedSymbol> itr = context.iterator();
				while (itr.hasNext() && param == null) {
					final TypedSymbol pi = itr.next();
					if (pi.equals(s)) {
						param = pi;
					}
				}
				if (param == null) {
					if (undeclaredParameters == null) {
						mgr.logParserError("variable \"" + s.getImage() + "\" is undefined", lexer.getFile(), s.getBeginLine(), s.getBeginColumn());
						checked = false;
					} else {
						Set<NamedTypedList> matchedPredicates = new HashSet<NamedTypedList>();
						final List<Symbol> atomParameters = new ArrayList<Symbol>();
						for (int j = 1; j < atom.size(); j++) {
							atomParameters.add(atom.get(j));
						}
						matchedPredicates = matchingNamedTypedList(atom.get(0), atomParameters, method, domain.getPredicates());
						if (matchedPredicates != null) {
							final TypedSymbol arg = new TypedSymbol(s);
							for (final NamedTypedList predicate : matchedPredicates) {
								arg.addAllTypes(predicate.getArguments().get(i - 1).getTypes());
							}
							
							atomSkeleton.add(arg);
							// Add the argument if it is not already in
							// undeclared parameters list
							if (undeclaredParameters.indexOf(arg) == -1) {
								undeclaredParameters.add(arg);
							}
						}
					}
				} else {
					final TypedSymbol arg = new TypedSymbol(s);
					for (final Symbol type : param.getTypes()) {
						arg.addType(type);
					}
					atomSkeleton.add(arg);
				}
			} else {
				final TypedSymbol constant = domain.getConstant(s);
				if (constant == null) {
					mgr.logParserError("constant \"" + s.getImage() + "\" is undefined", lexer.getFile(), s.getBeginLine(), s.getBeginColumn());
					checked = false;
				} else {
					atomSkeleton.add(constant);
				}
				
			}
		}
		
		// Add The undeclared parameters list to the method
		if (undeclaredParameters == null) {
			method.getUndeclaredParameters().addAll(undeclaredParameters);
		}
		
		if (checked && gd.getConnective().equals(Connective.ATOM) && !isDeclaredPredicate(atomSkeleton)) {
			mgr.logParserError("predicate \"" + atomSkeleton.getName() + atomSkeleton.getArguments() + "\" is undefined", lexer.getFile(), atomSkeleton.getName().getBeginLine(), atomSkeleton.getName().getBeginColumn());
			checked = false;
		} else if (checked && gd.getConnective().equals(Connective.FN_ATOM) && !isDeclaredFunction(atomSkeleton)) {
			mgr.logParserError("function \"" + atomSkeleton.getName() + "/" + atomSkeleton.getArguments().size() + "\" is undefined", lexer.getFile(), atomSkeleton.getName().getBeginLine(), atomSkeleton.getName().getBeginColumn());
			checked = false;
		}
		return checked;
	}
	
	/**
	 * Returns if this predicate was previously declared.
	 * 
	 * @param predicate the predicate.
	 * @return <code>true</code> if this predicate was previously declared; <code>false</code> otherwise.
	 */
	private boolean isDeclaredPredicate(final NamedTypedList predicate) {
		boolean checked = false;
		int i = 0;
		while (i < domain.getPredicates().size() && !checked) {
			final NamedTypedList p = domain.getPredicates().get(i);
			if (predicate.getName().equals(p.getName()) && predicate.getArguments().size() == p.getArguments().size()) {
				int j = 0;
				checked = true;
				while (j < predicate.getArguments().size() && checked) {
					final TypedSymbol arg1 = predicate.getArguments().get(j);
					final TypedSymbol arg2 = p.getArguments().get(j);
					checked = matchTypes(arg1, arg2);
					j++;
				}
			}
			i++;
		}
		return checked;
	}
	
	/**
	 * Returns if this function was previously declared.
	 * 
	 * @param function the function.
	 * @return <code>true</code> if this function was previously declared; <code>false</code> otherwise.
	 */
	private boolean isDeclaredFunction(final NamedTypedList function) {
		boolean checked = false;
		int i = 0;
		while (i < domain.getFunctions().size() && !checked) {
			final NamedTypedList p = domain.getFunctions().get(i);
			if (function.getName().equals(p.getName()) && function.getArguments().size() == p.getArguments().size()) {
				int j = 0;
				checked = true;
				while (j < function.getArguments().size() && checked) {
					final TypedSymbol arg1 = function.getArguments().get(j);
					final TypedSymbol arg2 = p.getArguments().get(j);
					checked = matchTypes(arg1, arg2);
					j++;
				}
			}
			i++;
		}
		return checked;
	}
	
	/**
	 * Returns if the types of two typed symbol matched, i.e., if the types of the first typed symbol can be viewed as a subtype of the second.
	 * 
	 * @param s1 the first typed symbol.
	 * @param s2 the second typed symbol.
	 * @return <code>true</code> if the types of the first typed symbol can be viewed as a subtype of the seconds. <code>false</code> otherwise.
	 */
	private boolean matchTypes(final TypedSymbol s1, final TypedSymbol s2) {
		List<Symbol> copy = new LinkedList<Symbol>(s1.getTypes());
		copy.retainAll(s2.getTypes());
		boolean isSubType = !copy.isEmpty();
		final Iterator<Symbol> i = s1.getTypes().iterator();
		while (i.hasNext() && !isSubType) {
			final TypedSymbol type = domain.getType(i.next());
			final LinkedList<TypedSymbol> stack = new LinkedList<TypedSymbol>();
			stack.push(type);
			while (!stack.isEmpty() && !isSubType) {
				final TypedSymbol t = stack.poll();
				copy = new LinkedList<Symbol>(t.getTypes());
				copy.retainAll(s2.getTypes());
				isSubType = !copy.isEmpty();
				for (final Symbol s : t.getTypes()) {
					if (!s.equals(Parser.OBJECT)) {
						stack.push(domain.getType(s));
					}
				}
			}
		}
		return isSubType;
	}
	
	/**
	 * Checks the operator parameters, i.e., if each parameter is single and its type was previously declared.
	 * 
	 * @param op the operator to check.
	 * @return <code>true</code> if the parameters of the specified operator are well formed; <code>false/<code> otherwise.
	 */
	private boolean checkOperatorsParameters(final Op op) {
		boolean checked = true;
		final Set<Symbol> set = new HashSet<Symbol>();
		for (final TypedSymbol parameter : op.getParameters()) {
			final Symbol s = parameter;
			if (!set.add(parameter)) {
				mgr.logParserError("parameter \"" + s + "\" is defined twice in the action \"" + op.getName() + "\"", lexer.getFile(), s.getBeginLine(), s.getBeginColumn());
				checked = false;
			}
			for (final Symbol type : parameter.getTypes()) {
				if (!domain.isDeclaredType(type)) {
					mgr.logParserError("type \"" + type.getImage() + "\" of the parameter \"" + s + "\" in the action \"" + op.getName() + "\" is undefined", lexer.getFile(), s.getBeginLine(), s.getBeginColumn());
					checked = false;
				}
			}
		}
		return checked;
	}
	
	/**
	 * Checks the method parameters, i.e., if each parameter is single and its type was previously declared.
	 * 
	 * @param method the method to check.
	 * @return <code>true</code> if the parameters of the specified method are well formed; <code>false/<code> otherwise.
	 */
	private boolean checkMethodsParameters(final Method method) {
		boolean checked = true;
		final Set<Symbol> set = new HashSet<Symbol>();
		
		// Check all the parameters in the method
		for (final TypedSymbol parameter : method.getParameters()) {
			final Symbol s = parameter;
			if (!set.add(parameter)) {
				mgr.logParserError("parameter \"" + s + "\" is defined twice in the method \"" + method.getName() + "\"", lexer.getFile(), s.getBeginLine(), s.getBeginColumn());
				checked = false;
			}
			
			// Check if the type was already defined in Types
			for (final Symbol type : parameter.getTypes()) {
				if (!domain.isDeclaredType(type)) {
					mgr.logParserError("type \"" + type.getImage() + "\" of the parameter \"" + s + "\" in the method \"" + method.getName() + "\" is undefined", lexer.getFile(), s.getBeginLine(), s.getBeginColumn());
					checked = false;
				}
			}
		}
		return checked;
	}
	
	/**
	 * Checks the uniqueness of the operator basing on the name and the parameter's types
	 * 
	 * @return <code>true</code> if all the operators are single; <code>false</code> otherwise.
	 */
	private boolean checkOperatorsUniqueness() {
		List<Op> operatorList = new ArrayList<Op>();
		for (final Op operator : domain.getOperators()) {
			operatorList = domain.getOperator(operator.getName(), operator.getParameters());
			if (operatorList.size() > 1) {
				mgr.logParserError("operator \"" + operator.getName() + " " + operator.getParameters() + "\" declared twice", lexer.getFile(), operator.getName().getBeginLine(), operator.getName().getBeginColumn());
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Checks the uniqueness of the method basing on the name and the parameter's types
	 * 
	 * @return <code>true</code> if all the methods are unique and <code> false </code> otherwise
	 */
	private boolean checkMethodsUniqueness() {
		List<Method> methodList = new ArrayList<Method>();
		List<Op> operatorList = new ArrayList<Op>();
		for (final Method method : domain.getMethods()) {
			methodList = domain.getMethod(method.getName(), method.getParameters());
			operatorList = domain.getOperator(method.getName(), method.getParameters());
			/* Checks if the method is declared twice */
			// if (methodList.size() > 1) {
			// this.mgr.logParserError("method \"" +
			// method.getName()+" "+method.getParameters() +
			// "\" declared twice", this.lexer
			// .getFile(), method.getName().getBeginLine(),
			// method.getName().getBeginColumn());
			// return false;
			// }
			/* Checks if the method is already declared as an operator */
			if (operatorList.size() > 0) {
				mgr.logParserError("method \"" + method.getName() + " " + method.getParameters() + "\" is already declared as an Operator", lexer.getFile(), method.getName().getBeginLine(), method.getName().getBeginColumn());
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Checks The methods expansion by verifying if the tags are unique and if the tasks and the parameters are already defined
	 * 
	 * @return <code>true</code> if the expansion is well defined and <code> false </code> otherwise
	 */
	private boolean checkMethodsExpansion(final Method method, final Set<Symbol> tagSet) {
		boolean noError = true;
		Symbol tag = null;
		Symbol task = null;
		List<Symbol> parameters = new ArrayList<Symbol>();
		
		// There is two loops to find undeclared variables in the expansion
		// constraints
		// before testing parameters declaration and task declaration
		for (final Exp tagTask : method.getExpansion().getChildren()) {
			// Decomposing the Exp tagTask into TAG/TASK/PARAMETERS
			tag = tagTask.getTag();
			task = tagTask.getAtom().get(0);
			parameters = tagTask.getAtom().subList(1, tagTask.getAtom().size());
			
			// Testing if the tag is uniquely defined in the method
			noError = checkTagsUniqueness(tag, tagSet);
			if (!noError) {
				return false;
			}
		}
		
		// Testing Expansion Constraints
		// The test is done here to add undeclared variables to the method
		noError = checkHtnConstraints(method.getConstraints(), tagSet, method);
		if (!noError) {
			return false; // Stop checking if the noError is false
		}
		for (final Exp tagTask2 : method.getExpansion().getChildren()) {
			
			// Decomposing the Exp tagTask into TAG/TASK/PARAMETERS
			tag = tagTask2.getTag();
			task = tagTask2.getAtom().get(0);
			parameters = tagTask2.getAtom().subList(1, tagTask2.getAtom().size());
			
			// Testing if the variables and the constants was already declared
			noError = checkParametersDeclaration(task, parameters, method);
			if (!noError) {
				return false;
			}
			
			// Testing if the task in the expansion is already declared as an
			// Operator or a Method
			noError = checkTaskDeclaration(task, parameters, method);
		}
		
		return noError;
	}
	
	/**
	 * Checks if the tags are uniquely defined in the method
	 * 
	 * @param tag the tag checked
	 * @param tagSet the set of the already declared tags
	 * @return <code>true</code> if the tag is unique in the method and <code> false </code> otherwise
	 */
	public boolean checkTagsUniqueness(final Symbol tag, final Set<Symbol> tagSet) {
		// if tag already exists in tagSet
		if (!tagSet.add(tag)) {
			mgr.logParserError("Tag \"" + tag + "\" declared twice", lexer.getFile(), tag.getBeginLine(), tag.getBeginColumn());
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Checks if the tags are already defined in the method
	 * 
	 * @param tag the tag checked
	 * @param tagSet the set of the already declared tags
	 * @return <code>true</code> if the tag is declared in the method and <code> false </code> otherwise
	 */
	public boolean checkTagDeclaration(final Symbol tag, final Set<Symbol> tagSet) {
		// if tag already exists in tagSet
		if (tagSet.add(tag)) {
			mgr.logParserError("Tag \"" + tag + "\" not declared", lexer.getFile(), tag.getBeginLine(), tag.getBeginColumn());
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Checks if the parameters of the task was already defined as a variable or as a constant and try to infer type from predicates and tasks otherwise
	 * 
	 * @param parameters The parameters of the task
	 * @param method The method were was declared the parameters
	 * @return <code>true</code> if all the parameters were declared as a variable or as a constant and <code> false </code> otherwise
	 */
	public boolean checkParametersDeclaration(final Symbol task, final List<Symbol> parameters, final Method method) {
		
		// Find all methods and operators corresponding to this task
		final List<NamedTypedList> signatures = new ArrayList<NamedTypedList>();
		signatures.addAll(methodsSignatures);
		signatures.addAll(operatorsSignatures);
		Set<NamedTypedList> matchSet = new HashSet<NamedTypedList>();
		matchSet = matchingNamedTypedList(task, parameters, method, signatures);
		// Test each parameter of the task
		for (int i = 0; i < parameters.size(); i++) {
			final Symbol parameter = parameters.get(i);
			// Create a typed symbol from a symbol using parameters declared in
			// the method
			final TypedSymbol ts = new TypedSymbol(parameter);
			if (parameter.getKind().equals(Kind.CONSTANT) && domain.getConstantType(parameter) == null) {
				mgr.logParserError("Constant \"" + parameter + "\" is not declared in the domain constants", lexer.getFile(), parameter.getBeginLine(), parameter.getBeginColumn());
				return false;
			} else if (parameter.getKind().equals(Kind.VARIABLE) && method.getParameterTypes(parameter) == null) {
				// Add the types of the corresponding parameter to the new ts
				if (matchSet.isEmpty()) {
					mgr.logParserError("Variable \"" + parameter + "\" is not in the method parameters and type inference is impossible", lexer.getFile(), parameter.getBeginLine(), parameter.getBeginColumn());
					return false;
				} else {
					for (final NamedTypedList signature : matchSet) {
						ts.addAllTypes(signature.getArguments().get(i).getTypes());
					}
					method.addUndeclaredParameter(ts);
				}
			}
		}
		return true;
	}
	
	/**
	 * Find all namedTypedLists corresponding to this name and known parameters
	 * 
	 * @param name The name of the task or the predicate
	 * @param parameters The parameters of the task or the predicate
	 * @param method The method in which the task or the predicate was declared
	 * @param signatures The list of predicates or methods and operators signatures
	 */
	public Set<NamedTypedList> matchingNamedTypedList(final Symbol name, final List<Symbol> parameters, final Method method, final List<NamedTypedList> signatures) {
		
		final Set<NamedTypedList> matchSet = new HashSet<NamedTypedList>();
		for (final NamedTypedList signature : signatures) {
			boolean correspond = false;
			if (name.equals(signature.getName()) && parameters.size() == signature.getArguments().size()) {
				correspond = true;
				for (int i = 0; i < parameters.size(); i++) {
					final Symbol s = parameters.get(i);
					final TypedSymbol ts = new TypedSymbol(s);
					if (method != null && method.getParameterTypes(s) != null && !s.getKind().equals(Kind.CONSTANT)) {
						ts.getTypes().addAll(method.getParameterTypes(s));
						if (!domain.isSubType(ts, signature.getArguments().get(i))) {
							if (method.getUndeclaredParameters().contains(s) && domain.isSubType(signature.getArguments().get(i), ts)) {
								final int index = method.getUndeclaredParameters().indexOf(s);
								ts.getTypes().clear();
								ts.getTypes().addAll(signature.getArguments().get(i).getTypes());
								method.getUndeclaredParameters().set(index, ts);
							} else {
								correspond = false;
							}
						}
					}
				}
			}
			if (correspond) {
				matchSet.add(signature);
			}
		}
		return matchSet;
	}
	
	/**
	 * Checks if the task declared in the expansion was already declared as a method or an operator
	 * 
	 * @param task The checked task
	 * @param parameters the parameters of the checked task
	 * @param method The method where is declared the checked task as a tagTask
	 * @return <code>true</code> if the task was already declared as a method or an operator and <code> false </code> otherwise
	 */
	public boolean checkTaskDeclaration(final Symbol task, final List<Symbol> parameters, final Method method) {
		
		// Creating List<TypedSymbol> from List<Symbol> with the corresponding
		// types in parameters declaration
		final List<TypedSymbol> typedParameters = new ArrayList<TypedSymbol>();
		for (final Symbol parameter : parameters) {
			final TypedSymbol typedParameter = new TypedSymbol(parameter);
			if (method != null) {
				// The parameter method equals not null if the function tests
				// the domain
				if (parameter.getKind().equals(Kind.VARIABLE)) {
					typedParameter.addType(method.getParameterTypes(parameter).get(0));
				} else if (parameter.getKind().equals(Kind.CONSTANT)) {
					typedParameter.addType(domain.getConstantType(parameter).get(0));
				}
			} else {
				// The parameter method equals null if the function tests the
				// problem
				if (parameter.getKind().equals(Kind.VARIABLE)) {
					mgr.logParserError("Parameter \"" + parameter + "\" must can't be a variable", lexer.getFile(), task.getBeginLine(), task.getBeginColumn());
				} else if (parameter.getKind().equals(Kind.CONSTANT)) {
					typedParameter.addType(problem.getObjectType(parameter).get(0));
				}
			}
			typedParameters.add(typedParameter);
		}
		
		// Testing if there is a method that corresponds to the task and its
		// parameters.
		if (domain.getMethod(task, typedParameters).size() + domain.getOperator(task, typedParameters).size() == 0) {
			mgr.logParserError("Task \"" + task + typedParameters + "\" is not declared as a method or an operator", lexer.getFile(), task.getBeginLine(), task.getBeginColumn());
			return false;
		}
		return true;
	}
	
	/**
	 * Checks if the method constraints are well declared
	 * 
	 * @param constraints The checked method constraints
	 * @param tagSet The set of tags declared in the method
	 * @param method The method where the constraint are declared
	 * @return <code>true</code> if the constraints are well declared and <code> false </code> otherwise
	 */
	public boolean checkHtnConstraints(final Exp conExp, final Set<Symbol> tagSet, final Method method) {
		boolean noError = true;
		final List<TypedSymbol> undeclaredParameters = new ArrayList<TypedSymbol>();
		final Stack<Exp> constraintsStack = new Stack<Exp>();
		constraintsStack.push(conExp);
		
		// Creating a HashMap with all method tags with an unique index key
		final Map<Symbol, Integer> tagMap = new HashMap<Symbol, Integer>();
		int index = 0;
		for (final Symbol tag : tagSet) {
			tagMap.put(tag, index);
			index++;
		}
		// Creating the adjacency matrix to test cycles in order constraints
		final AdjacencyMatrix matrix = new AdjacencyMatrix(tagSet.size());
		
		while (!constraintsStack.isEmpty()) {
			final Exp constraints = constraintsStack.pop();
			switch (constraints.getConnective()) {
				case AND:
					for (final Exp child : constraints.getChildren()) {
						constraintsStack.push(child);
					}
					break;
				case SERIES:
					for (int i = 0; i < constraints.getChildren().size(); i++) {
						// Verification of tag declaration
						final Exp child = constraints.getChildren().get(i);
						noError = checkTagDeclaration(child.getTag(), tagSet);
						if (!noError) {
							return false;
						}
						if (i > 0) {
							noError = matrix.addPositiveLink(tagMap.get(constraints.getChildren().get(i - 1).getTag()), tagMap.get(child.getTag()));
							if (!noError) {
								mgr.logParserError("Constraint " + constraints + " contains a circuit", lexer.getFile(), child.getTag().getBeginLine(), child.getTag().getBeginColumn());
								return false;
							}
						}
					}
					break;
				case BEFORE:
				case AFTER:
					for (int i = 0; i < constraints.getChildren().size(); i++) {
						final Exp child = constraints.getChildren().get(i);
						// Verification of predicates declaration
						if (i == 0) {
							// if method == null the constraints are problem
							// constraints
							if (method != null) {
								noError = checkParserNode(child, method.getParameters(), method);
							} else {
								noError = checkGroundedParserNode(child);
							}
							if (!noError) {
								return false;
							}
						}
						// Verification of tag declaration
						else {
							if (child.getConnective().equals(Connective.FIRST) || child.getConnective().equals(Connective.LAST)) {
								for (final Exp tag : child.getChildren()) {
									noError = checkTagDeclaration(tag.getTag(), tagSet);
								}
							} else {
								noError = checkTagDeclaration(child.getTag(), tagSet);
							}
							if (!noError) {
								return false;
							}
						}
						if (!noError) {
							return false;
						}
					}
					break;
				case BETWEEN:
					for (int i = 0; i < constraints.getChildren().size(); i++) {
						final Exp child = constraints.getChildren().get(i);
						// Verification of predicates declaration
						if (i == 0) {
							// if method == null the constraints are problem
							// constraints
							if (method != null) {
								noError = checkParserNode(child, method.getParameters(), method);
							} else {
								noError = checkGroundedParserNode(child);
							}
							if (!noError) {
								return false;
							}
						}
						// Verification of tag declaration
						else {
							noError = checkTagDeclaration(child.getTag(), tagSet);
							if (!noError) {
								return false;
							}
						}
					}
			}
		}
		return noError;
	}
	
	/**
	 * Checks if the problem is well declared
	 * 
	 * @return <code>true</code> if the problem is well declared and <code> false </code> otherwise
	 */
	public boolean checkHTNGoal() {
		if (problem == null) {
			throw new NullPointerException("The goal tasks list is empty");
		}
		boolean noError = true;
		Symbol tag = null;
		Symbol task = null;
		List<Symbol> parameters = new ArrayList<Symbol>();
		final Set<Symbol> tagSet = new HashSet<Symbol>();
		final Exp tasks = problem.getGoalTasks();
		if (tasks == null) {
			mgr.logParserError("The tasks list is empty", lexer.getFile(), -1, -1);
			return false;
		} else {
			for (final Exp tagTask : tasks.getChildren()) {
				
				// Decomposing the Exp tagTask into TAG/TASK/PARAMETERS
				tag = tagTask.getTag();
				task = tagTask.getAtom().get(0);
				parameters = tagTask.getAtom().subList(1, tagTask.getAtom().size());
				
				// Testing if the tag is uniquely defined in the method
				noError = checkTagsUniqueness(tag, tagSet);
				if (!noError) {
					return false;
				}
				
				// Testing if the parameters of the tasks were already declared in
				// the problem object list
				for (final Symbol parameter : parameters) {
					if (problem.getObjectType(parameter) == null) {
						mgr.logParserError("Object \"" + parameter + "\" is not declared in the problem objects", lexer.getFile(), parameter.getBeginLine(), parameter.getBeginColumn());
						return false;
					}
				}
				
				// Testing if the task in the expansion is already declared as an
				// Operator or a Method
				noError = checkTaskDeclaration(task, parameters, null);
				if (!noError) {
					return false;
				}
			}
			
			// Testing if the goal constraints are well formed
			noError = checkHtnConstraints(problem.getGoalTasksConstraints(), tagSet, null);
			return noError;
		}
	}
	
	/**
	 * Returns the error manager of the parser.
	 * 
	 * @return the error manager of the parser.
	 */
	public ErrorManager getErrorManager() {
		return mgr;
	}
	
	/**
	 * The main method of the parser example. The command line syntax is as follow:
	 * 
	 * <pre>
	 * usage of parser:
	 * 
	 * OPTIONS   DESCRIPTIONS
	 * 
	 * -p <str>    path for operator and fact file
	 * -o <str>    operator file name
	 * -f <str>    fact file name
	 * </pre>
	 * 
	 * @param args the arguments of the command line.
	 */
	public static void main(final String[] args) {
		
		if (args.length == 2 && args[0].equals("-p")) {
			System.out.print("parse problem " + "\"" + args[1] + "\": ");
			final Parser parser = new Parser();
			try {
				parser.parse(args[1]);
			} catch (final FileNotFoundException e) {
				System.out.println(e.getMessage());
			}
			if (parser.mgr.isEmpty()) {
			} else {
				System.out.println();
				parser.mgr.printAll();
			}
		} else if (args.length == 4 && args[0].equals("-o") && args[2].equals("-f")) {
			System.out.print("parse files " + "\"" + args[1] + "\" and " + "\"" + args[3] + "\": ");
			final Parser parser = new Parser();
			Domain domain;
			Problem problem;
			try {
				parser.parse(args[1], args[3]);
			} catch (final FileNotFoundException e) {
				System.out.println(e.getMessage());
			}
			if (parser.mgr.isEmpty()) {
				domain = parser.domain;
				problem = parser.problem;
			} else {
				parser.mgr.printAll();
			}
		} else {
			System.out.println("\nusage of parser:\n");
			System.out.println("OPTIONS   DESCRIPTIONS\n");
			System.out.println("-p <str>    path for operator and fact file");
			System.out.println("-o <str>    operator file name");
			System.out.println("-f <str>    fact file name\n");
		}
	}
}
