(define (problem sat-pb-3-1)
(:domain satellite)
(:requirements :strips :typing :htn :negative-preconditions)
(:objects
	satellite0 - satellite
	instrument0 - instrument
	instrument1 - instrument
	thermograph0 - mode
	GroundStation2 - direction
	Phenomenon6 - direction
	Star5 - direction
)
(:init
	(supports instrument0 thermograph0)
	(supports instrument1 thermograph0)
	(calibration_target instrument1 GroundStation2)
	(calibration_target instrument0 GroundStation2)
	(on_board instrument1 satellite0)
	(on_board instrument0 satellite0)
	(power_avail satellite0)
	(pointing satellite0 Phenomenon6)
)
(:goal 
		:tasks	(
					(tag t1 (do_mission Star5 thermograph0))
				)
		:constraints(and
						(after (have_image Star5 thermograph0) t1)
					)
)
