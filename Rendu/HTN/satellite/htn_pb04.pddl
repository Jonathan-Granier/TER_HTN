(define (problem strips-sat-x-1 )
(:domain satellite )
	(:requirements :strips :typing :negative-preconditions :htn :equality)

(:objects
	satellite0 - satellite
	instrument0 - instrument
	satellite1 - satellite
	instrument1 - instrument
	instrument2 - instrument
	infrared0 - mode
	infrared1 - mode
	thermograph2 - mode
	GroundStation1 - direction
	Star0 - direction
	Star2 - direction
	Planet3 - direction
	Star4 - direction
	Planet5 - direction
	Star6 - direction
	Star7 - direction
	Phenomenon8 - direction
	Phenomenon9 - direction
 )
(:init
	(supports instrument0 thermograph2 )
	(supports instrument0 infrared0 )
	(calibration_target instrument0 Star0 )
	(on_board instrument0 satellite0 )
	(power_avail satellite0 )
	(pointing satellite0 Star6 )
	(supports instrument1 infrared0 )
	(supports instrument1 thermograph2 )
	(supports instrument1 infrared1 )
	(calibration_target instrument1 Star2 )
	(supports instrument2 thermograph2 )
	(supports instrument2 infrared1 )
	(calibration_target instrument2 Star2 )
	(on_board instrument1 satellite1 )
	(on_board instrument2 satellite1 )
	(power_avail satellite1 )
	(pointing satellite1 Star0 )
 )
(:goal
	:tasks  (
			 ;; A remplir 
			(tag t1 (do_take_image Planet3 infrared1 )) 
			(tag t2 (do_take_image Star4 infrared1)) 
			(tag t3 (do_take_image Planet5 thermograph2))
			(tag t4 (do_take_image Star6 infrared1)) 
			(tag t5 (do_take_image Star7 infrared0)) 
			(tag t6 (do_take_image Phenomenon8 thermograph2))
			(tag t7 (do_take_image Phenomenon9 infrared0)) 
			(tag t8 (do_turn_to satellite1 Planet5)) 
			
		)
	:constraints(and
			(after (and
							(pointing satellite1 Planet5 )
							(have_image Planet3 infrared1 )
							(have_image Star4 infrared1 )
							(have_image Planet5 thermograph2 )
							(have_image Star6 infrared1 )
							(have_image Star7 infrared0 )
							(have_image Phenomenon8 thermograph2 )
							(have_image Phenomenon9 infrared0 )
 )
				 t1)
		)
))
