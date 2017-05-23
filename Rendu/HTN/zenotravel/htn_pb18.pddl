(define (problem ZTRAVEL-5-20 )
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
	person16 - person
	person17 - person
	person18 - person
	person19 - person
	person20 - person
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
	city14 - city
	city15 - city
	city16 - city
	city17 - city
	fl0 - flevel
	fl1 - flevel
	fl2 - flevel
	fl3 - flevel
	fl4 - flevel
	fl5 - flevel
	fl6 - flevel
	 )
(:init
	(at plane1 city11 )
	(fuel-level plane1 fl3 )
	(at plane2 city12 )
	(fuel-level plane2 fl3 )
	(at plane3 city12 )
	(fuel-level plane3 fl0 )
	(at plane4 city14 )
	(fuel-level plane4 fl0 )
	(at plane5 city14 )
	(fuel-level plane5 fl6 )
	(at person1 city7 )
	(at person2 city16 )
	(at person3 city9 )
	(at person4 city6 )
	(at person5 city6 )
	(at person6 city0 )
	(at person7 city6 )
	(at person8 city8 )
	(at person9 city13 )
	(at person10 city14 )
	(at person11 city7 )
	(at person12 city15 )
	(at person13 city4 )
	(at person14 city14 )
	(at person15 city10 )
	(at person16 city5 )
	(at person17 city10 )
	(at person18 city11 )
	(at person19 city9 )
	(at person20 city3 )
	(next fl0 fl1 )
	(next fl1 fl2 )
	(next fl2 fl3 )
	(next fl3 fl4 )
	(next fl4 fl5 )
	(next fl5 fl6 )
 )
(:goal
	:tasks  (
			
			(tag t1 (do_carry_person person1 city2))
			(tag t2 (do_carry_person person2 city17))
			(tag t3 (do_carry_person person3 city7))
			(tag t4 (do_carry_person person4 city14))
			(tag t5 (do_carry_person person5 city9))
			(tag t6 (do_carry_person person6 city13))
			(tag t7 (do_carry_person person7 city6))
			(tag t8 (do_carry_person person8 city8))
			(tag t9 (do_carry_person person9 city4))
			(tag t10 (do_carry_person person10 city16))
			(tag t11 (do_carry_person person11 city6))
			(tag t12 (do_carry_person person12 city4))
			(tag t13 (do_carry_person person13 city14))
			(tag t14 (do_carry_person person14 city2))
			(tag t15 (do_carry_person person15 city9)) 
			(tag t16 (do_carry_person person16 city9)) 
			(tag t17 (do_carry_person person17 city15)) 
			(tag t18 (do_carry_person person18 city6)) 
			(tag t19 (do_carry_person person19 city2)) 
			(tag t20 (do_carry_person person20 city17)) 
			(tag t21 (do_fly plane2 city6)) 

			
			
		)
	:constraints(and
			(after (and
							(at plane2 city6 )
							(at person1 city2 )
							(at person2 city17 )
							(at person3 city7 )
							(at person4 city14 )
							(at person5 city9 )
							(at person6 city13 )
							(at person7 city6 )
							(at person8 city8 )
							(at person9 city4 )
							(at person10 city16 )
							(at person11 city6 )
							(at person12 city4 )
							(at person13 city14 )
							(at person14 city2 )
							(at person15 city9 )
							(at person16 city9 )
							(at person17 city15 )
							(at person18 city6 )
							(at person19 city2 )
							(at person20 city17 )
	 )
				 t1)
		)
))
