(define (problem depotprob6178 )
(:domain Depot )
	(:requirements :strips :typing :negative-preconditions :htn :equality)

(:objects
	depot0 depot1 depot2 depot3 - Depot
	distributor0 distributor1 distributor2 distributor3 - Distributor
	truck0 truck1 truck2 truck3 - Truck
	pallet0 pallet1 pallet2 pallet3 pallet4 pallet5 pallet6 pallet7 pallet8 pallet9 - Pallet
	crate0 crate1 crate2 crate3 crate4 crate5 crate6 crate7 - Crate
	hoist0 hoist1 hoist2 hoist3 hoist4 hoist5 hoist6 hoist7 - Hoist )
(:init
	(at pallet0 depot0 )
	(clear crate6 )
	(at pallet1 depot1 )
	(clear crate1 )
	(at pallet2 depot2 )
	(clear pallet2 )
	(at pallet3 depot3 )
	(clear crate7 )
	(at pallet4 distributor0 )
	(clear crate2 )
	(at pallet5 distributor1 )
	(clear crate5 )
	(at pallet6 distributor2 )
	(clear pallet6 )
	(at pallet7 distributor3 )
	(clear pallet7 )
	(at pallet8 distributor2 )
	(clear crate4 )
	(at pallet9 depot3 )
	(clear crate0 )
	(at truck0 depot0 )
	(at truck1 distributor0 )
	(at truck2 depot2 )
	(at truck3 distributor3 )
	(at hoist0 depot0 )
	(available hoist0 )
	(at hoist1 depot1 )
	(available hoist1 )
	(at hoist2 depot2 )
	(available hoist2 )
	(at hoist3 depot3 )
	(available hoist3 )
	(at hoist4 distributor0 )
	(available hoist4 )
	(at hoist5 distributor1 )
	(available hoist5 )
	(at hoist6 distributor2 )
	(available hoist6 )
	(at hoist7 distributor3 )
	(available hoist7 )
	(at crate0 depot3 )
	(on crate0 pallet9 )
	(at crate1 depot1 )
	(on crate1 pallet1 )
	(at crate2 distributor0 )
	(on crate2 pallet4 )
	(at crate3 distributor1 )
	(on crate3 pallet5 )
	(at crate4 distributor2 )
	(on crate4 pallet8 )
	(at crate5 distributor1 )
	(on crate5 crate3 )
	(at crate6 depot0 )
	(on crate6 pallet0 )
	(at crate7 depot3 )
	(on crate7 pallet3 )
 )
(:goal
	:tasks  (
			 ;; A remplir 
			
			
		)
	:constraints(and
			(after (and
								(on crate0 pallet6 )
								(on crate1 pallet8 )
								(on crate3 crate1 )
								(on crate4 pallet5 )
								(on crate5 crate7 )
								(on crate6 pallet4 )
								(on crate7 crate4 )
	 )
				 t1)
		)
))
