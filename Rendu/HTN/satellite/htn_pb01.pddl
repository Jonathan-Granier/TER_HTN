(define (problem strips-sat-x-1 )
(:domain satellite )
	(:requirements :strips :typing :negative-preconditions :htn :equality)

(:objects
	satellite0 - satellite
	instrument0 - instrument
	image1 - mode
	spectrograph2 - mode
	thermograph0 - mode
	Star0 - direction
	GroundStation1 - direction
	GroundStation2 - direction
	Phenomenon3 - direction
	Phenomenon4 - direction
	Star5 - direction
	Phenomenon6 - direction
 )
(:init
	(supports instrument0 thermograph0 )
	(calibration_target instrument0 GroundStation2 )
	(on_board instrument0 satellite0 )
	(power_avail satellite0 )
	(pointing satellite0 Phenomenon6 )
 )
(:goal
	:tasks  (
			 ;; A remplir 
			(tag t1 (do_take_image Phenomenon4 thermograph0)) 
			(tag t2 (do_take_image Star5 thermograph0)) 
			(tag t3 (do_take_image Phenomenon6 thermograph0)) 
			
		)
	:constraints(and
			(after (and
							(have_image Phenomenon4 thermograph0 )
							(have_image Star5 thermograph0 )
							(have_image Phenomenon6 thermograph0 )
 )
				 t1)
		)
))
