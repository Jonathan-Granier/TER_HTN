;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Logistics
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(define (domain DIAGNOSIS)
	(:requirements :strips :typing :htn :negative-preconditions :universal-preconditions :conditional-effects)
	(:types template vCenter vMachine software network disk user - object
			vSphere - software		
	)
	(:predicates	(attached ?m - vMachine ?n - network)
	       			(connected-to ?c - vCenter ?s - vSphere)
	       			(disconnected-from ?c - vCenter ?s - vSphere)
	       			(running ?s - vSphere)
	       			(used ?s - vSphere)		
	       			(stopped ?s - vSphere)
	       			(up-to-date ?s - vSphere)
	       			(out-of-date ?s - vSphere)
	       			(empty-logins ?s - vSphere) 
	       			(filled-logins ?s - vSphere)     
	       			(empty-ip-address ?s - vSphere) 
	       			(filled-ip-address ?s - vSphere) 
	       			(available ?t - template ?s - vSphere)    	
	       			(selected ?t - template ?s - vSphere)   
	       			(deployed ?t - template ?m - vMachine ?c - vCenter ?s - vSphere) 		
	)

;;; Operators
;;;;;;;;;;;;;;;;;

	(:action launch
	    :parameters (?s - vSphere) 
	    :precondition (and (stopped ?s)) 
	    :effect (and (running ?s))
	)
	
	(:action update
	    :parameters (?s - vSphere) 
	    :precondition (and (running ?s) (out-of-date ?s)) 
	    :effect (and (up-to-date ?s))
	)
	
	(:action enter-logins
	    :parameters (?s - vSphere) 
	    :precondition (and (running ?s) (up-to-date ?s) (empty-logins ?s)) 
	    :effect (and (not(empty-logins ?s)) (filled-logins ?s))
	)
	
	(:action enter-ip-address
	    :parameters (?s - vSphere) 
	    :precondition (and (running ?s) (up-to-date ?s) (empty-ip-address ?s)) 
	    :effect (and (not(empty-ip-address ?s)) (filled-ip-address ?s))
	)
	
	(:action connect
	    :parameters (?c - vCenter ?s - vSphere) 
	    :precondition (and (running ?s) (up-to-date ?s) (filled-logins ?s) (filled-ip-address ?s)) 
	    :effect (and (connected-to ?c ?s))
	)
	
	(:action import
	    :parameters (?t - template ?s - vSphere) 
	    :precondition (and (connected-to ?c ?s) (available ?t ?s)) 
	    :effect (and (available ?t ?s))
	)
	
	(:action select
	    :parameters (?t - template ?s - vSphere) 
	    :precondition (and (available ?t ?s)) 
	    :effect (and (selected ?t ?s))
	)
	
	(:action deploy
	    :parameters (?t - template ?m - vMachine ?c - vCenter ?s - vSphere) 
	    :precondition (and (connected-to ?c ?s) (selected ?t ?s)) 
	    :effect (and (deployed ?t ?m ?c ?s))
	)
	
	(:action nop
	     :parameters ()
	     :precondition (and )
	     :effect (and )
	)                  


;;; Methods
;;;;;;;;;;;;;;;;;

	(:method vm-creation
		:parameters (?m - vMachine ?c -vCenter ?t - template)
		:expansion (
					(tag t1 (connect-to ?c))
					(tag t2 (choose ?t ?s))
					(tag t3 (deploy ?t ?m ?c ?s))
					)
		:constraints
					(and
						(before (and (connected-to ?c ?s)) t2)
						(after (and (deployed ?t ?m ?c ?s)) last(t1 t2 t3))
					)
	)
	
	(:method connect-to
		:parameters (?c - vCenter)
		:expansion (
					(tag t1 (do-start ?s))
					(tag t2 (enter-logins ?s))
					(tag t3 (enter-ip-address ?s))
					(tag t4 (connect ?c ?s))
					)
		:constraints
					(and
						(series t1 t2)
						(before (and (disconnected-from ?c ?s)) t1)
						(before (and (running ?s)) first(t2 t3))
						(after (and (connected-to ?c ?s)) t4)
					)
	)
	
	(:method connect-to
		:parameters (?c - vCenter)
		:expansion (
					(tag t1 (nop))
					)
		:constraints
					(and
						(before (and (connected-to ?c ?s)) t1)
					)
	)
	
	(:method do-start
		:parameters (?s - vSphere)
		:expansion (
					(tag t1 (launch ?s))
					)
		:constraints
					(and
						(before (and (up-to-date ?s)) t1)
						(after (and (running ?s)) t1)
					)
	)
	
	(:method do-start
		:parameters (?s - vSphere)
		:expansion (
					(tag t1 (launch ?s))
					(tag t2 (update ?s))
					)
		:constraints
					(and
						(before (and (out-of-date ?s)) t1)
						(after (and (running ?s) (up-to-date ?s)) last(t1 t2))
					)
	)
	
	(:method choose
		:parameters (?t - template ?s - vSphere)
		:expansion(
					(tag t1 (do-import ?t ?s))
					(tag t2 (select ?t ?s))
				   )
		:constraints
					(and
						(before (and (running ?s) (connected-to ?c ?s)) t1)
					)
	)
	
	(:method do-import
		:parameters (?t - template ?s - vSphere)
		:expansion(
					(tag t1 (import ?t ?s))
				   )
		:constraints
					(and
						(before (and (not(available ?t ?s))) t1)
					)
	)
	
	(:method do-import
		:parameters (?t - template ?s - vSphere)
		:expansion(
					(tag t1 (nop))
				   )
		:constraints
					(and
						(before (and (available ?t ?s)) t1)
					)
	)	
)