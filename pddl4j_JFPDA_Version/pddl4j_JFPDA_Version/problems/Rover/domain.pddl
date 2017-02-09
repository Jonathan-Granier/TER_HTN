;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Blocks world
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(define (domain ROVER)
	(:requirements :strips :typing :negative-preconditions :htn :equality)
	(:types rover waypoint store camera objective mode lander - object)
	(:constants )
	(:predicates	(available ?x - rover)
  					(at ?x - rover ?p - waypoint)
  					(visible ?p1 - waypoint ?p2 - waypoint)
  					(can_traverse ?x - rover ?p1 - waypoint ?p2 - waypoint)
  					(store_of ?s - store ?x - rover)
  					(empty ?s - store)
  					(full ?s - store)
  					(equipped_for_soil_analysis ?x - rover)
  					(at_soil_sample ?p - waypoint)
  					(have_soil_analysis ?x - rover ?p - waypoint)
  					(equipped_for_rock_analysis ?x - rover)
  					(at_rock_sample ?p - waypoint)
  					(have_rock_analysis ?x - rover ?p - waypoint)
  					(equipped_for_imaging ?x - rover) 
  					(calibration_target ?c - camera ?o - objective)
  					(visible_from ?o - objective ?p - waypoint) 
  					(on_board ?c - camera ?x - rover)
  					(calibrated ?c - camera ?x - rover)
  					(supports ?c - camera ?m - mode)
  					(have_image ?x - rover ?o - objective ?m - mode)
  					(at_lander ?l - lander ?p - waypoint)
  					(channel_free ?l - lander)
  					(communicated_soil_data ?p - waypoint) 
  					(communicated_rock_data ?p - waypoint) 
  					(communicated_image_data ?o - objective ?m - mode) 
  					(visited ?p - waypoint)
  	)


;;; Operators
;;;;;;;;;;;;;;;;;

	(:action navigate
	     :parameters (?x - rover ?p1 - waypoint ?p2 - waypoint)
	     :precondition (and (available ?x) (at ?x ?p1) (can_traverse ?x ?p1 ?p2) (visible ?p1 ?p2))
	     :effect (and
	     			(not(at ?x ?p1)) (at ?x ?p2)
	     		)
	)
          
	(:action sample_soil
	     :parameters (?x - rover ?s - store ?p - waypoint)
	     :precondition (and
	     					(at ?x ?p) (at_soil_sample ?p) (equipped_for_soil_analysis ?x) 
	     					(store_of ?s ?x) (empty ?s)
	     				)
	     :effect (and 
		   			(not(empty ?s)) (not(at_soil_sample ?p)) 
		   			(full ?s) (have_soil_analysis ?x ?p)
		   		)
	)
	
	(:action sample_rock
	     :parameters (?x - rover ?s - store ?p - waypoint)
	     :precondition (and
	     					(at ?x ?p) (at_rock_sample ?p) (equipped_for_rock_analysis ?x) 
	     					(store_of ?s ?x) (empty ?s)
	     				)
	     :effect (and 
		   			(not(empty ?s)) (not(at_rock_sample ?p)) 
		   			(full ?s) (have_rock_analysis ?x ?p)
		   		)
	)
          
  	(:action drop
	     :parameters (?x - rover ?s - store)
	     :precondition (and 
	     					(store_of ?s ?x) (full ?s)
	     				)
	     :effect (and 
	     			(not(full ?s)) (empty ?s)
				 )
	)
	
	(:action calibrate 
	     :parameters (?x - rover ?c - camera ?o - objective ?p - waypoint)
	     :precondition (and
	     					(equipped_for_imaging ?x) (calibration_target ?c ?o)
  							(at ?x ?p) (visible_from ?o ?p) (on_board ?c ?x)
	     				)
	     :effect (and 
  					(calibrated ?c ?x)
		   		)
	)
	
	(:action take_image
	     :parameters (?x - rover ?p - waypoint ?o - objective ?c - camera ?m - mode)
	     :precondition (and
	     					(calibrated ?c ?x) (on_board ?c ?x)
	     					(equipped_for_imaging ?x) (supports ?c ?m) 
	     					(at ?x ?p) (visible_from ?o ?p) 
	     				)
	     :effect (and 
  					(not(calibrated ?c ?x)) (have_image ?x ?o ?m)
		   		)
	)
	
	(:action communicate_soil_data
	     :parameters (?x - rover ?l - lander ?p1 - waypoint ?p2 - waypoint ?p3 - waypoint)
	     :precondition (and
	     					(at ?x ?p2) (at_lander ?l ?p3)
	     					(have_soil_analysis ?x ?p1) (visible ?p2 ?p3)
	     					(available ?x) (channel_free ?l)
	     				)
	     :effect (and 
  					 (communicated_soil_data ?p1) (available ?x)
		   		)
	)
	
	(:action communicate_soil_data
	     :parameters (?x - rover ?l - lander ?p1 - waypoint ?p2 - waypoint)
	     :precondition (and
	     					(at ?x ?p1) (at_lander ?l ?p2)
	     					(have_soil_analysis ?x ?p1) (visible ?p1 ?p2)
	     					(available ?x) (channel_free ?l)
	     				)
	     :effect (and 
  					 (communicated_soil_data ?p1) (available ?x)
		   		)
	)
	
	(:action communicate_rock_data
	     :parameters (?x - rover ?l - lander ?p1 - waypoint ?p2 - waypoint ?p3 - waypoint)
	     :precondition (and
	     					(at ?x ?p2) (at_lander ?l ?p3)
	     					(have_rock_analysis ?x ?p1) (visible ?p2 ?p3)
	     					(available ?x) (channel_free ?l)
	     				)
	     :effect (and 
  					(communicated_rock_data ?p1) (available ?x)
		   		)
	)
	
	(:action communicate_rock_data
	     :parameters (?x - rover ?l - lander ?p1 - waypoint ?p2 - waypoint)
	     :precondition (and
	     					(at ?x ?p1) (at_lander ?l ?p2)
	     					(have_rock_analysis ?x ?p1) (visible ?p1 ?p2)
	     					(available ?x) (channel_free ?l)
	     				)
	     :effect (and 
  					(communicated_rock_data ?p1) (available ?x)
		   		)
	)
	
	(:action communicate_image_data
	     :parameters (?x - rover ?l - lander ?o - objective ?m - mode ?p1 - waypoint ?p2 - waypoint)
	     :precondition (and
	     					(at ?x ?p1) (at_lander ?l ?p2)
	     					(have_image ?x ?o ?m) (visible ?p1 ?p2)
	     					(available ?x) (channel_free ?l)
	     				)
	     :effect (and 
  					(communicated_image_data ?o ?m) (available ?x)
  					(channel_free ?l)
		   		)
	)
	
	(:action visit
	     :parameters (?p - waypoint)
	     :precondition (and )
	     :effect (and (visited ?p))
	)  
	
	(:action unvisit
	     :parameters (?p - waypoint)
	     :precondition (and (visited ?p))
	     :effect (and (not(visited ?p)))
	) 
	
	(:action nop
	     :parameters ()
	     :precondition (and )
	     :effect (and )
	)                  

;------------------------------------------------------------------
;								Methods
;------------------------------------------------------------------

;								Navigation
;------------------------------------------------------------------

(:method do_navigate
		:parameters	(?x - rover ?to - waypoint)
		:expansion	(
					(tag t1 (nop))
			    	)
		:constraints
					(and 
			  			(before (at ?x ?to) t1)
			     	)
	)
		
(:method do_navigate
		:parameters	(?x - rover ?to - waypoint)
		:expansion	(
					(tag t1 (visit ?from))
					(tag t2 (do_navigate ?x ?from ?to))
					(tag t3 (unvisit ?from))
			    	)
		:constraints
					(and 
			  			(before (at ?x ?from) t1)
			     	)
	)
	
(:method do_navigate
		:parameters	(?x - rover ?from ?to - waypoint)
		:expansion	(
					(tag t1 (nop))
			    	)
		:constraints
					(and 
			  			(before (at ?x ?to) t1)
			     	)
	)
	
(:method do_navigate
		:parameters	(?x - rover ?from ?to - waypoint)
		:expansion	(
					(tag t1 (navigate ?x ?from ?to))
			    	)
		:constraints
					(and 
			  			(before (can_traverse ?x ?from ?to) t1)
			     	)
	)
	
(:method do_navigate
		:parameters	(?x - rover ?from ?to - waypoint)
		:expansion	(
					(tag t1 (navigate ?x ?from ?mid))
					(tag t2 (visit ?mid))
					(tag t3 (do_navigate ?x ?mid ?to))
					(tag t4 (unvisit ?mid))
			    	)
		:constraints
					(and 
			  			(before (and
			  						(not(can_traverse ?x ?from ?to)) 
			  						(not(visited ?mid))
			  						(can_traverse ?x ?from ?mid)
			  					) t1)
			     	)
	)
	
	
;				Collection and transmission of samples							
;------------------------------------------------------------------

(:method empty_store
		:parameters	(?s - store ?x - rover)
		:expansion	(
					(tag t1 (nop))
			    	)
		:constraints
					(and 
			  			(before (empty ?s) t1)
			     	)
	)
	
(:method empty_store
		:parameters	(?s - store ?x - rover)
		:expansion	(
					(tag t1 (drop ?x ?s))
			    	)
		:constraints
					(and 
			  			(before (not(empty ?s)) t1)
			     	)
	)
	
;				Soil samples							
;----------------------------------------

(:method get_soil_data
		:parameters	(?from - waypoint)
		:expansion	(
					(tag t1 (do_navigate ?x ?from))
					(tag t2 (empty_store ?s ?x))
					(tag t3 (sample_soil ?x ?s ?from))
					(tag t4 (send_soil_data ?x ?from))
			    	)
		:constraints
					(and 
			  			(before (and (store_of ?s ?x) (equipped_for_soil_analysis ?x)) t1)
			     	)
	)

;; TODO Add send_soil_data without navigation + (communicate_soil_data ?x ?l ?from ?from ?w2) + (before (visible ?from ?w2) t1)
	
(:method send_soil_data
		:parameters	(?x - rover ?from - waypoint)
		:expansion	(
					(tag t1 (do_navigate ?x ?w1))
					(tag t2 (communicate_soil_data ?x ?l ?from ?w1 ?w2))
			    	)
		:constraints
					(and 
			  			(before (and (at_lander ?l ?w2) (visible ?w1 ?w2)) t1)
			     	)
	)

(:method send_soil_data
		:parameters	(?x - rover ?from - waypoint)
		:expansion	(
					(tag t1 (communicate_soil_data ?x ?l ?from ?w1))
			    	)
		:constraints
					(and 
			  			(before (and (at_lander ?l ?w1) (visible ?from ?w1) (at ?x ?from)) t1)
			     	)
	)

;				Rock samples							
;----------------------------------------

(:method get_rock_data
		:parameters	(?from - waypoint)
		:expansion	(
					(tag t1 (do_navigate ?x ?from))
					(tag t2 (empty_store ?s ?x))
					(tag t3 (sample_rock ?x ?s ?from))
					(tag t4 (send_rock_data ?x ?from))
			    	)
		:constraints
					(and 
			  			(before (and (store_of ?s ?x) (equipped_for_rock_analysis ?x)) t1)
			     	)
	)
	
;; TODO Add send_rock_data without navigation + (communicate_rock_data ?x ?l ?from ?from ?w2) + (before (visible ?from ?w2) t1)

(:method send_rock_data
		:parameters	(?x - rover ?from - waypoint)
		:expansion	(
					(tag t1 (do_navigate ?x ?w1))
					(tag t2 (communicate_rock_data ?x ?l ?from ?w1 ?w2))
			    	)
		:constraints
					(and 
			  			(before (and (at_lander ?l ?w2) (visible ?w1 ?w2)) t1)
			     	)
	)
	
(:method send_rock_data
		:parameters	(?x - rover ?from - waypoint)
		:expansion	(
					(tag t1 (communicate_rock_data ?x ?l ?from ?w1))
			    	)
		:constraints
					(and 
			  			(before (and (at_lander ?l ?w1) (visible ?from ?w1) (at ?x ?from)) t1)
			     	)
	)

;				Image samples							
;----------------------------------------

(:method get_image_data
		:parameters	(?o - objective ?m - mode)
		:expansion	(
					(tag t1 (do_calibrate ?x ?c))
					(tag t2 (do_navigate ?x ?w))
					(tag t3 (take_image ?x ?w ?o ?c ?m))
					(tag t4 (send_image_data ?x ?o ?m))
			    	)
		:constraints
					(and 
			  			(before (and 
			  						(equipped_for_imaging ?x) (on_board ?c ?x)
			  						(supports ?c ?m) (visible_from ?o ?w)
			  					) t1)
			     	)
	)

(:method send_image_data
		:parameters	(?x - rover ?o - objective ?m - mode)
		:expansion	(
					(tag t1 (do_navigate ?x ?w1))
					(tag t2 (communicate_image_data ?x ?l ?o ?m ?w1 ?w2))
			    	)
		:constraints
					(and 
			  			(before (and (at_lander ?l ?w2) (visible ?w1 ?w2)) t1)
			     	)
	)

(:method do_calibrate
		:parameters	(?x - rover ?c - camera)
		:expansion	(
					(tag t1 (do_navigate ?x ?w))
					(tag t2 (calibrate ?x ?c ?o ?w))
			    	)
		:constraints
					(and 
			  			(before (and (calibration_target ?c ?o) (visible_from ?o ?w)) t1)
			     	)
	)

)