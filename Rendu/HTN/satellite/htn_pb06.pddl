(define (problem strips-sat-x-1 )
(:domain satellite )
	(:requirements :strips :typing :negative-preconditions :htn :equality)

(:objects
	satellite0 - satellite
	instrument0 - instrument
	satellite1 - satellite
	instrument1 - instrument
	instrument2 - instrument
	instrument3 - instrument
	satellite2 - satellite
	instrument4 - instrument
	thermograph2 - mode
	spectrograph0 - mode
	infrared1 - mode
	infrared3 - mode
	GroundStation3 - direction
	Star1 - direction
	Star2 - direction
	Star0 - direction
	Planet4 - direction
	Planet5 - direction
	Star6 - direction
	Star7 - direction
	Phenomenon8 - direction
	Star9 - direction
	Star10 - direction
 )
(:init
	(supports instrument0 infrared1 )
	(supports instrument0 spectrograph0 )
	(calibration_target instrument0 Star1 )
	(on_board instrument0 satellite0 )
	(power_avail satellite0 )
	(pointing satellite0 Phenomenon8 )
	(supports instrument1 infrared3 )
	(calibration_target instrument1 Star2 )
	(supports instrument2 infrared1 )
	(supports instrument2 infrared3 )
	(supports instrument2 thermograph2 )
	(calibration_target instrument2 Star2 )
	(supports instrument3 infrared1 )
	(supports instrument3 infrared3 )
	(supports instrument3 spectrograph0 )
	(calibration_target instrument3 Star2 )
	(on_board instrument1 satellite1 )
	(on_board instrument2 satellite1 )
	(on_board instrument3 satellite1 )
	(power_avail satellite1 )
	(pointing satellite1 Star6 )
	(supports instrument4 infrared3 )
	(calibration_target instrument4 Star0 )
	(on_board instrument4 satellite2 )
	(power_avail satellite2 )
	(pointing satellite2 Star6 )
 )
(:goal
	:tasks  (
			 ;; A remplir 
			(tag t1 (do_take_image Planet4 thermograph2)) 
			(tag t2 (do_take_image Planet5 spectrograph0)) 
			(tag t3 (do_take_image Star6 thermograph2))
			(tag t4 (do_take_image Star7 infrared3)) 
			(tag t5 (do_take_image Phenomenon8 spectrograph0)) 
			(tag t6 (do_take_image Star9 infrared1))
			(tag t7 (do_take_image Star10 infrared3))
			
		)
	:constraints(and
			(after (and
							(have_image Planet4 thermograph2 )
							(have_image Planet5 spectrograph0 )
							(have_image Star6 thermograph2 )
							(have_image Star7 infrared3 )
							(have_image Phenomenon8 spectrograph0 )
							(have_image Star9 infrared1 )
							(have_image Star10 infrared3 )
 )
				 t1)
		)
))
