;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Gripper
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(define (domain GRIPPER)
	(:requirements :strips :typing :htn :negative-preconditions :equality)
	(:types room ball gripper - object)
	(:predicates	(at-robby ?r - room)
					(at ?b - ball ?r - room)
					(free ?g - gripper)
					(carry ?o - ball ?g - gripper)
	)


;;; Operators
;;;;;;;;;;;;;;;;;

	(:action move
       :parameters  (?from - room ?to - room)
       :precondition (and (at-robby ?from))
       :effect (and  (at-robby ?to) (not (at-robby ?from)))
	)

   (:action pick
       :parameters (?obj - ball ?room - room ?gripper - gripper)
       :precondition  (and (at ?obj ?room) (at-robby ?room) (free ?gripper))
       :effect (and (carry ?obj ?gripper)
		    		(not (at ?obj ?room)) 
		    		(not (free ?gripper))
		   		)
	)


   (:action drop
       :parameters  (?obj - ball ?room - room ?gripper - gripper)
       :precondition  (and (carry ?obj ?gripper) (at-robby ?room))
       :effect (and (at ?obj ?room)
		    		(free ?gripper)
		    		(not (carry ?obj ?gripper))
		    	)
	)  
	
	(:action nop
   		:parameters ()
   		:precondition (and )
   		:effect (and )
   )             


;;; Methods
;;;;;;;;;;;;;;;;;

	(:method transport
		:parameters	(?b - ball ?r1 - room ?r2 -room)
		:expansion	(
						(tag t0 (go-to ?r1))
						(tag t1 (pick ?b ?r1 ?g))
						(tag t2 (move ?r1 ?r2))
						(tag t3 (drop ?b ?r2 ?g))
					)
		:constraints(and
						(series t0 t1 t2 t3)
						(before (and (at ?b ?r1) (free ?g)) t0)
						(after (and (at ?b ?r2) (free ?g)) t3)
					)
	)
	
	(:method transport
		:parameters	(?b1 - ball ?b2 - ball ?r1 - room ?r2 -room)
		:expansion	(
						(tag t0 (go-to ?r1))
						(tag t1 (pick ?b1 ?r1 ?g1))
						(tag t2 (pick ?b2 ?r1 ?g2))
						(tag t3 (move ?r1 ?r2))
						(tag t4 (drop ?b1 ?r2 ?g1))
						(tag t5 (drop ?b2 ?r2 ?g2))
					)
		:constraints(and
						(series t0 t1)
						(series t0 t2)
						(series t1 t3)
						(series t2 t3)
						(series t3 t4)
						(series t3 t5)
						(before (and (at ?b1 ?r1) (at ?b2 ?r1) (free ?g1) (free ?g2)) t0)
						(after (and (at ?b1 ?r2) (at ?b2 ?r2) (free ?g1) (free ?g2)) last(t4 t5))
					)
	)
	
	(:method go-to
		:parameters	(?r1 - room)
		:expansion	(
						(tag t1 (move ?r ?r1))
					)
		:constraints(and
						(before (and (not(at-robby ?r1)) (free ?g1) (free ?g2)) t1)
					)
	)
	
	(:method go-to
		:parameters	(?r1 - room)
		:expansion	(
						(tag t1 (nop))
					)
		:constraints(and
						(before (and (at-robby ?r1)(free ?g1) (free ?g2)) t1)
					)
	)
)