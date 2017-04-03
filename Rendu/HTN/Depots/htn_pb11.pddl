(define (problem depotprob8765 )
(:domain Depot )
	(:requirements :strips :typing :negative-preconditions :htn :equality)

(:objects
	depot0 depot1 depot2 - Depot
	distributor0 distributor1 distributor2 - Distributor
	truck0 truck1 - Truck
	pallet0 pallet1 pallet2 pallet3 pallet4 pallet5 - Pallet
	crate0 crate1 crate2 crate3 crate4 crate5 crate6 crate7 crate8 crate9 - Crate
	hoist0 hoist1 hoist2 hoist3 hoist4 hoist5 - Hoist )
(:init
	(at pallet0 depot0 )
	(clear crate1 )
	(at pallet1 depot1 )
	(clear crate3 )
	(at pallet2 depot2 )
	(clear crate9 )
	(at pallet3 distributor0 )
	(clear pallet3 )
	(at pallet4 distributor1 )
	(clear pallet4 )
	(at pallet5 distributor2 )
	(clear crate8 )
	(at truck0 depot2 )
	(at truck1 distributor0 )
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
	(at crate0 depot1 )
	(on crate0 pallet1 )
	(at crate1 depot0 )
	(on crate1 pallet0 )
	(at crate2 depot2 )
	(on crate2 pallet2 )
	(at crate3 depot1 )
	(on crate3 crate0 )
	(at crate4 depot2 )
	(on crate4 crate2 )
	(at crate5 depot2 )
	(on crate5 crate4 )
	(at crate6 distributor2 )
	(on crate6 pallet5 )
	(at crate7 distributor2 )
	(on crate7 crate6 )
	(at crate8 distributor2 )
	(on crate8 crate7 )
	(at crate9 depot2 )
	(on crate9 crate5 )
 )
(:goal
	:tasks  (
				(tag t1 (do_put_on crate4 pallet0))

				;(tag t2 (do_put_on crate5 pallet2))
				;(tag t3 (do_put_on crate6 crate5))

				;(tag t4 (do_put_on crate8 pallet3))

				;(tag t5 (do_put_on crate1 pallet4))
				;(tag t6 (do_put_on crate7 crate1))
				;(tag t7 (do_put_on crate0 crate7))

				;(tag t8 (do_put_on crate2 pallet5))
				;(tag t9 (do_put_on crate9 crate2))
				;(tag t10 (do_put_on crate3 crate9))
			
			
		)
	:constraints(and
			(after (and
								;(on crate0 crate7 )
								;(on crate1 pallet4 )
								;(on crate2 pallet5 )
								;(on crate3 crate9 )
								;(on crate4 pallet0 )
								;(on crate5 pallet2 )
								;(on crate6 crate5 )
								;(on crate7 crate1 )
								;(on crate8 pallet3 )
								;(on crate9 crate2 )
	 )
				 t1)
		)
))
