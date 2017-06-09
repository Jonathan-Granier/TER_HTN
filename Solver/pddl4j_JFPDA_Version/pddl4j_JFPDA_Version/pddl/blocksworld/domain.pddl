;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; 4 Op-blocks world
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(define (domain BLOCKS)
	(:requirements :strips :typing :htn :negative-preconditions)
	(:types 	movable fixed - object
  				block - movable
  				disk - movable
  				table - fixed
  	)
	(:constants brick - block)
	(:predicates	(on ?x - movable ?y - movable)
  				(posed ?x - movable ?y - fixed)
  				(ontable ?x - movable)
  				(clear ?x - movable)
  				(handempty)
  				(holding ?x - movable)
  	)

	(:action pick-up
	     :parameters (?x - block)
	     :precondition (and (clear ?x) (ontable ?x) (handempty))
	     :effect (and 
	     			(not (ontable ?x))
		   			(not (clear ?x))
		   			(not (handempty))
		   			(holding ?x)
		   		)
	)

	(:action pick-up
	     :parameters (?x - movable ?f - fixed)
	     :precondition (and (clear ?x) (posed ?x ?f) (handempty))
	     :effect (and 
	     			(not (posed ?x ?f))
		   			(not (clear ?x))
		   			(not (handempty))
		   			(holding ?x)
		   		)
	)

	(:action put-down
	     :parameters (?x - block)
	     :precondition (holding ?x)
	     :effect (and 
	     			(not (holding ?x))
		   			(clear ?x)
		   			(handempty)
		   			(ontable ?x)
		   		)
	)
	
	(:action put-down
	     :parameters (?x - movable ?f - fixed)
	     :precondition (holding ?x)
	     :effect (and 
	     			(not (holding ?x))
		   			(clear ?x)
		   			(handempty)
		   			(posed ?x ?f)
		   		)
	)
  (:action stack
	     :parameters (?x - block ?y - block)
	     :precondition (and (holding ?x) (clear ?y))
	     :effect
	     (and (not (holding ?x))
		   (not (clear ?y))
		   (clear ?x)
		   (handempty)
		   (on ?x ?y)))
		   
		   
	(:action unstack
	     :parameters	(?x - block ?y - block)
	     :precondition	(and 
	     					(on ?x ?y) 
	     					(clear ?x) 
	     					(handempty)
	     				)
	     :effect		(and 
	     					(holding ?x)
		   					(clear ?y)
		   					(not (clear ?x))
		   					(not (handempty))
		   					(not (on ?x ?y))
		   				)
	)
		   
	(:method transfer
		:parameters	(?x - block ?y - block ?z - block)
		:expansion	(
					(tag t1 (pick-up ?x)) 
			    	(tag t2 (put-down ?x))
			    	(tag t3 (stack ?x brick))
			    	)
		:constraints(and 
						(series t1 t2 t3) 
						(before (and (clear ?x) (ontable ?x) (handempty)) t1)
						(before (and (holding ?x) (clear ?y)) t2 t3) 
					)
	)
	
	(:method transfer
		:parameters	(?x - block ?f1 - fixed ?f2 - fixed)
		:expansion	(
					(tag t1 (pick-up ?x ?f1))
			    	(tag t2 (put-down ?x ?f2))
			    	)
		:constraints(and 
						(series t1 t2)
			     	  	(before (and (posed ?x ?f1) (handempty)) t1) 
			     	  	(between (holding ?x) t1 t2)
			     	  	(after (posed ?x ?f2) t2)
			     	)
	)
	

)
