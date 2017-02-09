(define (problem sat-pb-2-3-10-3-7)
(:domain satellite)
(:requirements :strips :typing :htn :negative-preconditions)
(:objects
	satellite0 - satellite
	satellite1 - satellite
	instrument0 - instrument
	instrument1 - instrument
	instrument2 - instrument
	GroundStation1 - direction
	Star0 - direction
	Star2 - direction
	Planet3 - direction
	Planet5 - direction
	Star6 - direction
	Star4 - direction
	Star7 - direction
	infrared0 - mode
	infrared1 - mode
	thermograph2 - mode
)
(:init
	(supports instrument0 thermograph2)
	(supports instrument0 infrared0)
	(calibration_target instrument0 Star0)
	(on_board instrument0 satellite0)
	(power_avail satellite0)
	(pointing satellite0 Star6)
	(supports instrument1 infrared0)
	(supports instrument1 thermograph2)
	(supports instrument1 infrared1)
	(calibration_target instrument1 Star2)
	(supports instrument2 thermograph2)
	(supports instrument2 infrared1)
	(calibration_target instrument2 Star2)
	(on_board instrument1 satellite1)
	(on_board instrument2 satellite1)
	(power_avail satellite1)
	(pointing satellite1 Star0)
)
(:goal 
		:tasks	(
					(tag t1 (do_mission Planet3 infrared1))
					(tag t2 (do_mission Star4 infrared1))
					(tag t3 (do_mission Planet5 thermograph2))
					(tag t4 (do_mission Star6 infrared1))
					(tag t5 (do_mission Star6 thermograph2))
					
					
				)
		:constraints(and
						(after (have_image planet3 infrared1) t1)
						(after (have_image Star4 infrared1) t2)
						(after (have_image Planet5 thermograph2) t3)
						(after (have_image Star6 infrared1) t4)
						(after (have_image Star6 thermograph2) t5)
					)
)
