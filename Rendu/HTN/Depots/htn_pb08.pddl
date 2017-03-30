(define (problem depotprob4321 )
(:domain Depot )
	(:requirements :strips :typing :negative-preconditions :htn :equality)

(:objects
	depot0 - Depot
	distributor0 distributor1 - Distributor
	truck0 truck1 - Truck
	pallet0 pallet1 pallet2 pallet3 pallet4 pallet5 - Pallet
	crate0 crate1 crate2 crate3 crate4 crate5 crate6 crate7 crate8 crate9 - Crate
	hoist0 hoist1 hoist2 - Hoist )
(:init
	(at pallet0 depot0 )
	(clear crate2 )
	(at pallet1 distributor0 )
	(clear crate6 )
	(at pallet2 distributor1 )
	(clear crate9 )
	(at pallet3 distributor1 )
	(clear crate7 )
	(at pallet4 distributor0 )
	(clear crate0 )
	(at pallet5 distributor0 )
	(clear crate8 )
	(at truck0 distributor0 )
	(at truck1 distributor0 )
	(at hoist0 depot0 )
	(available hoist0 )
	(at hoist1 distributor0 )
	(available hoist1 )
	(at hoist2 distributor1 )
	(available hoist2 )
	(at crate0 distributor0 )
	(on crate0 pallet4 )
	(at crate1 distributor0 )
	(on crate1 pallet1 )
	(at crate2 depot0 )
	(on crate2 pallet0 )
	(at crate3 distributor0 )
	(on crate3 pallet5 )
	(at crate4 distributor1 )
	(on crate4 pallet3 )
	(at crate5 distributor0 )
	(on crate5 crate1 )
	(at crate6 distributor0 )
	(on crate6 crate5 )
	(at crate7 distributor1 )
	(on crate7 crate4 )
	(at crate8 distributor0 )
	(on crate8 crate3 )
	(at crate9 distributor1 )
	(on crate9 pallet2 )
 )
(:goal
	:tasks  (
			 ;; A remplir 
			
			
		)
	:constraints(and
			(after (and
								(on crate0 pallet3 )
								(on crate1 crate0 )
								(on crate3 crate8 )
								(on crate6 pallet2 )
								(on crate7 pallet1 )
								(on crate8 pallet4 )
								(on crate9 pallet0 )
	 )
				 t1)
		)
))
