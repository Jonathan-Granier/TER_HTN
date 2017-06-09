(define (problem HTN_lgtcs_pb_2_2)
(:domain LOGISTICS)
(:requirements :strips :typing :htn :negative-preconditions)
(:objects 	p1 p2 - obj
            london paris - city
            truck1 truck2 - truck
            plane - airplane
            north south - location 
	    	lhr cdg - airport)
(:init
		(in-city cdg paris) 
  	 	(in-city lhr london) 
	 	(in-city north paris) 
	 	(in-city south paris) 
		(at plane lhr) 
		(at truck1 north)
		(at truck2 cdg)
		(at p1 north) 
		(at p2 lhr)
)

(:goal
       :tasks(
       			(tag t1 (inter-city-transport p1 north lhr))
				(tag t2 (inter-city-transport p2 lhr south))
       	     )
       :constraints (and 
       					(after (and (at p1 lhr) (at p2 south)) last(t1 t2))
					)
)
