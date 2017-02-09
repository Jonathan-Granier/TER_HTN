;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Blocks world
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(define (domain BLOCKSWORLD)
	(:requirements :strips :typing :htn :negative-preconditions :equality)
	(:types block table - object)
	(:constants table - block)
	(:predicates	(on ?x - block ?y - block)
  					(clear ?x - block)
  	)


;;; Operators
;;;;;;;;;;;;;;;;;

	(:action unstack
	     :parameters (?x - block ?y - block)
	     :precondition (and (clear ?x) (on ?x ?y))
	     :effect (and
	     			(not(on ?x ?y)) (on ?x table) (clear ?y)
	     		)
	)
          
	(:action dostack
	     :parameters (?x - block ?y - block)
	     :precondition (and
	     					(clear ?x) (on ?x table) (clear ?y)
	     				)
	     :effect (and 
		   			(not(on ?x table))(on ?x ?y)(not(clear ?y))
		   		)
	)
          
  	(:action restack
	     :parameters (?x - block ?y - block ?z - block)
	     :precondition (and 
	     					(clear ?x) (on ?x ?y) (clear ?z)
	     				)
	     :effect (and 
	     			(not(on ?x ?y)) (clear ?y) (on ?x ?z) (not(clear ?z))
				 )
	)
	
	(:action nop
	     :parameters ()
	     :precondition (and )
	     :effect (and )
	)                  


;;; Methods
;;;;;;;;;;;;;;;;;

	(:method make-clear
		:parameters (?x -block)
		:expansion (
					(tag t1 (make-clear ?y))
					(tag t2 (unstack ?y ?x))
					)
		:constraints
					(and
						(before (not (= ?x table)) t1)
						(before (on ?y ?x) t1)
                    )
	)
	
	(:method make-clear
		:parameters (?x -block)
		:expansion (
					(tag t1 (nop))
					)
		:constraints
					(
					)
	)
	
	(:method move
		:parameters	(?x - block ?y - block ?z - block)
		:expansion	(
						(tag t1 (restack ?x ?y ?z))
					)
		:constraints
					(and
						(before (and (not(= ?x table)) (not(= ?y table)) (not(= ?z table))) t1)
						(before (and (clear ?x) (clear ?z) (on ?x ?y)) t1)
					)
	)
	
	(:method move
		:parameters	(?x - block ?y - block ?z - block)
		:expansion	((tag t1 (dostack ?x ?z)))
		:constraints
					(and 
						(before (= ?y table) t1)
						(before (and (clear ?x) (on ?x ?y)) t1)
					)
	)	
	
	(:method put-on
		:parameters	(?x - block ?y - block)
		:expansion	(
					(tag t1 (make-clear ?x))
			    	(tag t2 (make-clear ?y))
			    	(tag t3 (move ?x ?z ?y))
			    	)
		:constraints
					(and 
			     	  	(before (not (on ?x ?y)) t1)
			  			(before (on ?x ?z) t1)
			     	)
	)
)