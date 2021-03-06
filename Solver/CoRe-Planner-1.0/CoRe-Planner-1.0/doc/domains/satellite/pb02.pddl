        ; child-snack task with 10 children and 0.4 gluten factor 
; constant factor of 1.3
; random seed: 234324

(define (problem strips-sat-x-1)
(:domain satellite)
(:requirements :strips :typing :htn :negative-preconditions)

  ;---------------- Facts -----------------------
  (:objects
    satellite0 - satellite
    instrument0 - instrument
    instrument1 - instrument
    infrared0 - mode
    infrared1 - mode
    image2 - mode
    GroundStation1 - direction
    Star0 - direction
    GroundStation2 - direction
    Planet3 - direction
    Planet4 - direction
    Phenomenon5 - direction
    Phenomenon6 - direction
    Star7 - direction
  )

  ;--------------- Initial State -----------------
  (:init
      (supports instrument0 infrared1)
      (supports instrument0 infrared0)
      (calibration_target instrument0 Star0)
      (supports instrument1 image2)
      (supports instrument1 infrared1)
      (supports instrument1 infrared0)
      (calibration_target instrument1 GroundStation2)
      (on_board instrument0 satellite0)
      (on_board instrument1 satellite0)
      (power_avail satellite0)
      (pointing satellite0 Planet4)
  )

  (:goal
        :tasks  (
                    (tag t1  (do_mission Planet3 infrared0)     )
                    (tag t2  (do_mission Planet4 infrared0)     )
                    (tag t3  (do_mission Phenomenon5 image2)    )
                    (tag t4  (do_mission Phenomenon6 infrared0) )
                    (tag t5  (do_mission Star7 infrared0)       )
                )
        :constraints(and
                        (after (and 
                                    (have_image Planet3 infrared0)
                                    (have_image Planet4 infrared0)
                                    (have_image Phenomenon5 image2)
                                    (have_image Phenomenon6 infrared0)
                                    (have_image Star7 infrared0)
                                ) t5)
                    )
)
