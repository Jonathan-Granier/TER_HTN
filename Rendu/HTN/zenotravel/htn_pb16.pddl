(define (problem ZTRAVEL-5-15 )
(:domain zeno-travel )
	(:requirements :strips :typing :negative-preconditions :htn :equality)

(:objects
	plane1 - aircraft
	plane2 - aircraft
	plane3 - aircraft
	plane4 - aircraft
	plane5 - aircraft
	person1 - person
	person2 - person
	person3 - person
	person4 - person
	person5 - person
	person6 - person
	person7 - person
	person8 - person
	person9 - person
	person10 - person
	person11 - person
	person12 - person
	person13 - person
	person14 - person
	person15 - person
	city0 - city
	city1 - city
	city2 - city
	city3 - city
	city4 - city
	city5 - city
	city6 - city
	city7 - city
	city8 - city
	city9 - city
	city10 - city
	city11 - city
	city12 - city
	city13 - city
	fl0 - flevel
	fl1 - flevel
	fl2 - flevel
	fl3 - flevel
	fl4 - flevel
	fl5 - flevel
	fl6 - flevel
	 )
(:init
	(at plane1 city6 )
	(fuel-level plane1 fl2 )
	(at plane2 city0 )
	(fuel-level plane2 fl3 )
	(at plane3 city10 )
	(fuel-level plane3 fl5 )
	(at plane4 city4 )
	(fuel-level plane4 fl4 )
	(at plane5 city1 )
	(fuel-level plane5 fl6 )
	(at person1 city8 )
	(at person2 city12 )
	(at person3 city0 )
	(at person4 city4 )
	(at person5 city13 )
	(at person6 city7 )
	(at person7 city1 )
	(at person8 city2 )
	(at person9 city1 )
	(at person10 city2 )
	(at person11 city10 )
	(at person12 city7 )
	(at person13 city6 )
	(at person14 city1 )
	(at person15 city13 )
	(next fl0 fl1 )
	(next fl1 fl2 )
	(next fl2 fl3 )
	(next fl3 fl4 )
	(next fl4 fl5 )
	(next fl5 fl6 )
 )
(:goal
	:tasks  (
			
			(tag t1 (do_carry_person person1 city3))
			(tag t2 (do_carry_person person2 city4))
			(tag t3 (do_carry_person person3 city11))
			(tag t4 (do_carry_person person4 city13))
			(tag t5 (do_carry_person person5 city11))
			(tag t6 (do_carry_person person6 city7))
			(tag t7 (do_carry_person person7 city1))
			(tag t8 (do_carry_person person8 city11))
			(tag t9 (do_carry_person person9 city2))
			(tag t10 (do_carry_person person10 city6))
			(tag t11 (do_carry_person person11 city0))
			(tag t12 (do_carry_person person12 city12))
			(tag t13 (do_carry_person person13 city13))
			(tag t14 (do_carry_person person14 city4))
			(tag t15 (do_carry_person person15 city4)) 
			(tag t16 (do_fly plane2 city12)) 
			(tag t17 (do_fly plane3 city6))
			
			
		)
	:constraints(and
			(after (and
							(at plane2 city12 )
							(at plane3 city6 )
							(at person1 city3 )
							(at person2 city4 )
							(at person3 city11 )
							(at person4 city13 )
							(at person5 city11 )
							(at person6 city7 )
							(at person7 city1 )
							(at person8 city11 )
							(at person9 city2 )
							(at person10 city6 )
							(at person11 city0 )
							(at person12 city12 )
							(at person13 city13 )
							(at person14 city4 )
							(at person15 city4 )
	 )
				 t1)
		)
))
