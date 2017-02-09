;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Logistics
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(define (domain LOGISTICS)
	(:requirements :strips :typing :htn :negative-preconditions :universal-preconditions :conditional-effects)
	(:types physobj - object
	  obj vehicle - physobj
	  truck airplane - vehicle
	  location city - object
	  airport - location)
	(:predicates	(at ?x - physobj ?l - location)
	       			(in ?x - obj ?t - vehicle)
	       			(in-city ?l - location ?c - city)
	)

;;; Operators
;;;;;;;;;;;;;;;;;

	(:action load 
	    :parameters (?obj - obj ?airplane - vehicle ?loc - location) 
	    :precondition (and (at ?obj ?loc) (at ?airplane ?loc)) 
	    :effect (and (in ?obj ?airplane))
	)
  	
  	(:action unload 
	    :parameters (?obj - obj ?airplane - vehicle ?loc - location) 
	    :precondition (and (in ?obj ?airplane) (at ?airplane ?loc)) 
	    :effect (and (not (in ?obj ?airplane)))
	)

  	(:action drive-truck 
	   	:parameters (?truck - truck ?x - obj ?loc-from ?loc-to - location ?city - city)
	    :precondition (and 	(at ?truck ?loc-from) 
				 			(in-city ?loc-from ?city) 
				 			(in-city ?loc-to ?city))
	    :effect  (and	(at ?truck ?loc-to) 
			    		(not (at ?truck ?loc-from))	
			    		(at ?x ?loc-to)	  
			      		(not (at ?x ?loc-from))
				)
	)
	
  	(:action fly-airplane
   		:parameters (?plane - airplane ?x - obj ?loc-from ?loc-to - airport)
   		:precondition (and (at ?plane ?loc-from) )
   		:effect	(and	(at ?plane ?loc-to) 
		      			(not (at ?plane ?loc-from))	
		      			(at ?x ?loc-to)	  
			      		(not (at ?x ?loc-from))
				)
	)
	
	(:action nop
	     :parameters ()
	     :precondition (and )
	     :effect (and )
	)                  


;;; Methods
;;;;;;;;;;;;;;;;;

	(:method in-city-transport
		:parameters (?obj - obj ?from ?to - location)
		:expansion (
					(tag t1 (load ?obj ?v ?from))
					(tag t2 (drive-truck ?v ?obj ?from ?to ?city))
					(tag t3 (unload ?obj ?v ?to))
					)
		:constraints
					(and
						(series t1 t2 t3)
						(before (and (in-city ?from ?city) (in-city ?to ?city) (at ?v ?from) (at ?obj ?from)) t1)
						(after (and (at ?obj ?to) (not(in ?obj ?v))) t3)
						(between (and (in ?obj ?v)) t1 t3)
					)
	)
	
	(:method inter-airport-transport
		:parameters (?obj - obj ?from ?to - airport)
		:expansion (
					(tag t1 (load ?obj ?a ?from))
					(tag t2 (fly-airplane ?a ?obj ?from ?to))
					(tag t3 (unload ?obj ?a ?to))
					)
		:constraints
					(and
						(series t1 t2 t3)
						(before (and (in-city ?from ?city1) (in-city ?to ?city2) (at ?a ?from) (at ?obj ?from)) t1)
						(after (and (at ?obj ?to) (not(in ?obj ?a))) t3)
						(between (and (in ?obj ?a)) t1 t3)
					)
	)
	
	(:method inter-city-transport
		:parameters (?obj - obj ?from ?to - airport)
		:expansion (
					(tag t1 (inter-airport-transport ?obj ?from ?to))
					)
		:constraints
					(and
						
					)
	)
	
	(:method inter-city-transport
		:parameters (?obj - obj ?from ?to - location)
		:expansion	(
						(tag t1 (in-city-transport ?obj ?from ?airport1))
						(tag t2 (inter-airport-transport ?obj ?airport1 ?airport2))
						(tag t3 (in-city-transport ?obj ?airport2 ?to))
					)
		:constraints
					( and
						(before (and (in-city ?from ?city-from) (in-city ?airport1 ?city-from) (in-city ?to ?city-to) (in-city ?airport2 ?city-to)) t1)
						(before (and (at ?obj ?airport1)) t2)
						(before (and (at ?obj ?airport2)) t3)
						(after (and (at ?obj ?to)) last(t1 t2 t3))
					)
	)
	
	(:method inter-city-transport
		:parameters (?obj - obj ?from - airport ?to - location)
		:expansion	(
						(tag t1 (inter-airport-transport ?obj ?from ?airport2))
						(tag t2 (in-city-transport ?obj ?airport2 ?to))
					)
		:constraints
					( and
						(before (and (in-city ?from ?city-from) (in-city ?to ?city-to) (in-city ?airport2 ?city-to) (at ?obj ?from)) t1)
						(before (and (at ?obj ?airport2)) t2)
						(after (and (at ?obj ?to)) last(t1 t2))
					)
	)
	
	(:method inter-city-transport
		:parameters (?obj - obj ?from - location ?to - airport)
		:expansion	(
						(tag t1 (in-city-transport ?obj ?from ?airport1))
						(tag t2 (inter-airport-transport ?obj ?airport1 ?to))
					)
		:constraints
					( and
						(before (and (in-city ?from ?city-from) (in-city ?airport1 ?city-from) (in-city ?to ?city-to)) t1)
						(before (and (at ?obj ?airport1)) t2)
						(after (and (at ?obj ?to)) last(t1 t2))
					)
	)
)