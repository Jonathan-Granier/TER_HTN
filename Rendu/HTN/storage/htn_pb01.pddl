(define (problem storage-1 )
(:domain Storage-Propositional )
	(:requirements :strips :typing :negative-preconditions :htn :equality)

(:objects
	depot0-1-1 container-0-0 - storearea
	hoist0 - hoist
	crate0 - crate
	container0 - container
	depot0 - depot
	loadarea - transitarea )
(:init
	(in depot0-1-1 depot0 )
	(on crate0 container-0-0 )
	(in crate0 container0 )
	(in container-0-0 container0 )
	(connected loadarea container-0-0 ) 
	(connected container-0-0 loadarea )  
	(connected depot0-1-1 loadarea )
	(connected loadarea depot0-1-1 )    
	(at hoist0 depot0-1-1 )
	(available hoist0 ) )
(:goal
	:tasks  (
			 (tag t1 (do_problem )) 
			
			
		)
	:constraints(and
			(after (and
	(in crate0 depot0 ) )
				 t1)
		)
))
