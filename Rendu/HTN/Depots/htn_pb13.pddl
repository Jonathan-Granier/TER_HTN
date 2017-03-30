(define (problem depotprob5646 )
(:domain Depot )
	(:requirements :strips :typing :negative-preconditions :htn :equality)

(:objects
	depot0 depot1 depot2 - Depot
	distributor0 distributor1 distributor2 - Distributor
	truck0 truck1 - Truck
	pallet0 pallet1 pallet2 pallet3 pallet4 pallet5 pallet6 pallet7 pallet8 pallet9 - Pallet
	crate0 crate1 crate2 crate3 crate4 crate5 - Crate
	hoist0 hoist1 hoist2 hoist3 hoist4 hoist5 - Hoist )
(:init
	(at pallet0 depot0 )
	(clear crate2 )
	(at pallet1 depot1 )
	(clear pallet1 )
	(at pallet2 depot2 )
	(clear crate5 )
	(at pallet3 distributor0 )
	(clear crate4 )
	(at pallet4 distributor1 )
	(clear pallet4 )
	(at pallet5 distributor2 )
	(clear pallet5 )
	(at pallet6 distributor1 )
	(clear pallet6 )
	(at pallet7 depot0 )
	(clear pallet7 )
	(at pallet8 depot0 )
	(clear crate3 )
	(at pallet9 distributor0 )
	(clear pallet9 )
	(at truck0 distributor1 )
	(at truck1 depot0 )
	(at hoist0 depot0 )
	(available hoist0 )
	(at hoist1 depot1 )
	(available hoist1 )
	(at hoist2 depot2 )
	(available hoist2 )
	(at hoist3 distributor0 )
	(available hoist3 )
	(at hoist4 distributor1 )
	(available hoist4 )
	(at hoist5 distributor2 )
	(available hoist5 )
	(at crate0 depot2 )
	(on crate0 pallet2 )
	(at crate1 depot2 )
	(on crate1 crate0 )
	(at crate2 depot0 )
	(on crate2 pallet0 )
	(at crate3 depot0 )
	(on crate3 pallet8 )
	(at crate4 distributor0 )
	(on crate4 pallet3 )
	(at crate5 depot2 )
	(on crate5 crate1 )
 )
(:goal
	:tasks  (
			 ;; A remplir 
			
			
		)
	:constraints(and
			(after (and
								(on crate0 pallet0 )
								(on crate1 pallet5 )
								(on crate2 pallet4 )
								(on crate3 pallet7 )
								(on crate4 pallet9 )
								(on crate5 pallet1 )
	 )
				 t1)
		)
))
