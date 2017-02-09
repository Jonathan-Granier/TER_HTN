
(define (problem strips-sat-x-1)
(:domain satellite)
(:requirements :strips :typing :htn :negative-preconditions)

  ;---------------- Facts -----------------------
  (:objects
    satellite0 - satellite
  instrument0 - instrument
  satellite1 - satellite
  instrument1 - instrument
  instrument2 - instrument
  instrument3 - instrument
  satellite2 - satellite
  instrument4 - instrument
  satellite3 - satellite
  instrument5 - instrument
  instrument6 - instrument
  instrument7 - instrument
  satellite4 - satellite
  instrument8 - instrument
  instrument9 - instrument
  instrument10 - instrument
  satellite5 - satellite
  instrument11 - instrument
  instrument12 - instrument
  instrument13 - instrument
  satellite6 - satellite
  instrument14 - instrument
  instrument15 - instrument
  instrument16 - instrument
  satellite7 - satellite
  instrument17 - instrument
  instrument18 - instrument
  instrument19 - instrument
  satellite8 - satellite
  instrument20 - instrument
  instrument21 - instrument
  instrument22 - instrument
  satellite9 - satellite
  instrument23 - instrument
  image0 - mode
  thermograph2 - mode
  thermograph1 - mode
  spectrograph3 - mode
  Star0 - direction
  Star3 - direction
  Star4 - direction
  GroundStation1 - direction
  Star2 - direction
  Phenomenon5 - direction
  Star6 - direction
  Star7 - direction
  Phenomenon8 - direction
  Phenomenon9 - direction
  Star10 - direction
  Planet11 - direction
  Phenomenon12 - direction
  Phenomenon13 - direction
  Phenomenon14 - direction
  Phenomenon15 - direction
  Planet16 - direction
  Phenomenon17 - direction
  Planet18 - direction
  Planet19 - direction
  Planet20 - direction
  Phenomenon21 - direction
  Planet22 - direction
  Planet23 - direction
  Phenomenon24 - direction
  Phenomenon25 - direction
  Phenomenon26 - direction
  Phenomenon27 - direction
  Star28 - direction
  Star29 - direction
  Phenomenon30 - direction
  Phenomenon31 - direction
  Phenomenon32 - direction
  Phenomenon33 - direction
  Phenomenon34 - direction
  Planet35 - direction
  Star36 - direction
  Phenomenon37 - direction
  Phenomenon38 - direction
  Phenomenon39 - direction
  Star40 - direction
  Star41 - direction
  Phenomenon42 - direction
  Star43 - direction
  Planet44 - direction
  Planet45 - direction
  Planet46 - direction
  Star47 - direction
  Star48 - direction
  Star49 - direction
  Phenomenon50 - direction
  Phenomenon51 - direction
  Phenomenon52 - direction
  Planet53 - direction
  Planet54 - direction
  Star55 - direction
  Planet56 - direction
  Phenomenon57 - direction
  Phenomenon58 - direction
  Planet59 - direction
  Phenomenon60 - direction
  Star61 - direction
  Star62 - direction
  Star63 - direction
  Planet64 - direction
  Planet65 - direction
  Star66 - direction
  Planet67 - direction
  Phenomenon68 - direction
  Star69 - direction
  Planet70 - direction
  Star71 - direction
  Phenomenon72 - direction
  Planet73 - direction
  Star74 - direction
  Phenomenon75 - direction
  Planet76 - direction
  Star77 - direction
  Planet78 - direction
  Planet79 - direction
  Phenomenon80 - direction
  Phenomenon81 - direction
  Planet82 - direction
  Star83 - direction
  Phenomenon84 - direction
  Planet85 - direction
  Planet86 - direction
  Phenomenon87 - direction
  Planet88 - direction
  Planet89 - direction
  Star90 - direction
  Phenomenon91 - direction
  Star92 - direction
  Phenomenon93 - direction
  Planet94 - direction
  Star95 - direction
  Planet96 - direction
  Phenomenon97 - direction
  Planet98 - direction
  Phenomenon99 - direction
  Planet100 - direction
  Star101 - direction
  Planet102 - direction
  Phenomenon103 - direction
  Phenomenon104 - direction
  )

  ;--------------- Initial State -----------------
  (:init
      (supports instrument0 thermograph1)
  (supports instrument0 thermograph2)
  (supports instrument0 image0)
  (calibration_target instrument0 Star4)
  (on_board instrument0 satellite0)
  (power_avail satellite0)
  (pointing satellite0 Planet23)
  (supports instrument1 image0)
  (supports instrument1 spectrograph3)
  (supports instrument1 thermograph1)
  (calibration_target instrument1 Star4)
  (supports instrument2 thermograph2)
  (supports instrument2 thermograph1)
  (supports instrument2 spectrograph3)
  (calibration_target instrument2 Star3)
  (supports instrument3 thermograph1)
  (calibration_target instrument3 Star0)
  (on_board instrument1 satellite1)
  (on_board instrument2 satellite1)
  (on_board instrument3 satellite1)
  (power_avail satellite1)
  (pointing satellite1 GroundStation1)
  (supports instrument4 thermograph1)
  (supports instrument4 image0)
  (calibration_target instrument4 Star4)
  (on_board instrument4 satellite2)
  (power_avail satellite2)
  (pointing satellite2 Planet96)
  (supports instrument5 spectrograph3)
  (supports instrument5 thermograph2)
  (supports instrument5 image0)
  (calibration_target instrument5 Star2)
  (supports instrument6 image0)
  (supports instrument6 thermograph2)
  (calibration_target instrument6 GroundStation1)
  (supports instrument7 thermograph2)
  (supports instrument7 thermograph1)
  (supports instrument7 spectrograph3)
  (calibration_target instrument7 Star2)
  (on_board instrument5 satellite3)
  (on_board instrument6 satellite3)
  (on_board instrument7 satellite3)
  (power_avail satellite3)
  (pointing satellite3 Phenomenon84)
  (supports instrument8 thermograph2)
  (supports instrument8 thermograph1)
  (calibration_target instrument8 Star2)
  (supports instrument9 image0)
  (supports instrument9 thermograph2)
  (supports instrument9 spectrograph3)
  (calibration_target instrument9 Star3)
  (supports instrument10 thermograph2)
  (supports instrument10 spectrograph3)
  (supports instrument10 thermograph1)
  (calibration_target instrument10 Star0)
  (on_board instrument8 satellite4)
  (on_board instrument9 satellite4)
  (on_board instrument10 satellite4)
  (power_avail satellite4)
  (pointing satellite4 Phenomenon52)
  (supports instrument11 image0)
  (calibration_target instrument11 Star0)
  (supports instrument12 image0)
  (supports instrument12 thermograph1)
  (supports instrument12 thermograph2)
  (calibration_target instrument12 Star4)
  (supports instrument13 spectrograph3)
  (supports instrument13 thermograph1)
  (supports instrument13 image0)
  (calibration_target instrument13 Star2)
  (on_board instrument11 satellite5)
  (on_board instrument12 satellite5)
  (on_board instrument13 satellite5)
  (power_avail satellite5)
  (pointing satellite5 Phenomenon87)
  (supports instrument14 thermograph1)
  (calibration_target instrument14 Star4)
  (supports instrument15 thermograph1)
  (supports instrument15 image0)
  (calibration_target instrument15 Star2)
  (supports instrument16 thermograph1)
  (calibration_target instrument16 GroundStation1)
  (on_board instrument14 satellite6)
  (on_board instrument15 satellite6)
  (on_board instrument16 satellite6)
  (power_avail satellite6)
  (pointing satellite6 Phenomenon51)
  (supports instrument17 image0)
  (calibration_target instrument17 GroundStation1)
  (supports instrument18 thermograph2)
  (calibration_target instrument18 Star3)
  (supports instrument19 image0)
  (supports instrument19 thermograph2)
  (calibration_target instrument19 Star2)
  (on_board instrument17 satellite7)
  (on_board instrument18 satellite7)
  (on_board instrument19 satellite7)
  (power_avail satellite7)
  (pointing satellite7 Phenomenon93)
  (supports instrument20 image0)
  (calibration_target instrument20 Star4)
  (supports instrument21 spectrograph3)
  (supports instrument21 image0)
  (calibration_target instrument21 Star2)
  (supports instrument22 thermograph1)
  (supports instrument22 thermograph2)
  (calibration_target instrument22 GroundStation1)
  (on_board instrument20 satellite8)
  (on_board instrument21 satellite8)
  (on_board instrument22 satellite8)
  (power_avail satellite8)
  (pointing satellite8 Phenomenon17)
  (supports instrument23 spectrograph3)
  (calibration_target instrument23 Star2)
  (on_board instrument23 satellite9)
  (power_avail satellite9)
  (pointing satellite9 Phenomenon99)
  )

  (:goal
        :tasks  (
                    (tag t1  (do_mission Phenomenon5 thermograph1) )
                    (tag t2  (do_mission Star6 thermograph1) )
                    (tag t3  (do_mission Star7 spectrograph3) )
                    (tag t4  (do_mission Phenomenon8 image0) ) 
                    (tag t5  (do_mission Phenomenon9 image0) )
                    (tag t6  (do_mission Star10 spectrograph3) )
                    (tag t7  (do_mission Planet11 thermograph2) )
                    (tag t8  (do_mission Phenomenon12 image0) )
                    (tag t9  (do_mission Phenomenon13 thermograph1) )
                    (tag t10 (do_mission Phenomenon14 thermograph2) )
                    (tag t11 (do_mission Phenomenon15 thermograph1) )
                    (tag t12 (do_mission Planet16 thermograph2) )
                    (tag t13 (do_mission Phenomenon17 thermograph1) )
                    (tag t14 (do_mission Planet18 thermograph1) )
                    (tag t15 (do_mission Planet19 thermograph1) )
                    (tag t16 (do_mission Phenomenon21 image0) )
                    (tag t17 (do_mission Planet22 spectrograph3) )
                    (tag t18 (do_mission Planet23 thermograph2) )
                    (tag t19 (do_mission Phenomenon24 thermograph2) )
                    (tag t20 (do_mission Phenomenon25 thermograph2) )
                    (tag t21 (do_mission Phenomenon26 thermograph2) )
                    (tag t22 (do_mission Phenomenon27 thermograph2) )
                    (tag t23 (do_mission Star28 thermograph2) )
                    (tag t24 (do_mission Phenomenon30 image0) )
                    (tag t25 (do_mission Phenomenon31 image0) )
                    (tag t26 (do_mission Phenomenon32 image0) )
                    (tag t27 (do_mission Phenomenon33 thermograph2) )
                    (tag t28 (do_mission Planet35 thermograph2) )
                    (tag t29 (do_mission Star36 spectrograph3) )
                    (tag t30 (do_mission Phenomenon37 thermograph1) )
                    (tag t31 (do_mission Phenomenon38 thermograph2) )
                    (tag t32 (do_mission Phenomenon39 thermograph2) )
                    (tag t33 (do_mission Star41 thermograph2) )
                    (tag t34 (do_mission Phenomenon42 thermograph2) )
                    (tag t35 (do_mission Star43 thermograph1) )
                    (tag t36 (do_mission Planet44 thermograph2) )
                    (tag t37 (do_mission Planet45 thermograph2) )
                    (tag t38 (do_mission Planet46 thermograph1) )
                    (tag t39 (do_mission Star47 spectrograph3) )
                    (tag t40 (do_mission Star48 spectrograph3) )
                    (tag t41 (do_mission Phenomenon50 spectrograph3) )
                    (tag t42 (do_mission Phenomenon51 image0) )
                    (tag t43 (do_mission Phenomenon52 spectrograph3) )
                    (tag t44 (do_mission Planet53 spectrograph3) ) 
                    (tag t45 (do_mission Planet54 spectrograph3) )
                    (tag t46 (do_mission Star55 thermograph1) )
                    (tag t47 (do_mission Planet56 thermograph1) )
                    (tag t48 (do_mission Phenomenon57 spectrograph3) )
                    (tag t49 (do_mission Planet59 spectrograph3) )
                    (tag t50 (do_mission Phenomenon60 thermograph2) )
                    (tag t51 (do_mission Star61 thermograph2) )
                    (tag t52 (do_mission Star62 thermograph2) )
                    (tag t53 (do_mission Star63 thermograph2) )
                    (tag t54 (do_mission Planet64 thermograph2) )
                    (tag t55 (do_mission Planet65 spectrograph3) )
                    (tag t56 (do_mission Star66 spectrograph3) )
                    (tag t57 (do_mission Planet67 thermograph2) )
                    (tag t58 (do_mission Phenomenon68 thermograph1) )
                    (tag t59 (do_mission Star69 thermograph2) )
                    (tag t60 (do_mission Planet70 thermograph1) )
                    (tag t61 (do_mission Star71 image0) )
                    (tag t62 (do_mission Phenomenon72 image0) )
                    (tag t63 (do_mission Planet73 thermograph1) )
                    (tag t64 (do_mission Star74 thermograph1) )
                    (tag t65 (do_mission Phenomenon75 thermograph1) )
                    (tag t66 (do_mission Planet76 spectrograph3) )
                    (tag t67 (do_mission Star77 thermograph1) )
                    (tag t68 (do_mission Planet78 image0) )
                    (tag t69 (do_mission Planet79 thermograph2) )
                    (tag t70 (do_mission Phenomenon80 thermograph2) )
                    (tag t71 (do_mission Phenomenon81 thermograph1) )
                    (tag t72 (do_mission Planet82 image0) )
                    (tag t73 (do_mission Star83 spectrograph3) )
                    (tag t74 (do_mission Phenomenon84 thermograph2) )
                    (tag t75 (do_mission Planet85 image0) )
                    (tag t76 (do_mission Planet86 thermograph1) )
                    (tag t77 (do_mission Phenomenon87 spectrograph3) )
                    (tag t78 (do_mission Planet88 image0) )
                    (tag t79 (do_mission Star90 thermograph2) )
                    (tag t80 (do_mission Phenomenon91 spectrograph3) )
                    (tag t81 (do_mission Star92 thermograph1) )
                    (tag t82 (do_mission Phenomenon93 thermograph2) )
                    (tag t83 (do_mission Planet94 thermograph2) )
                    (tag t84 (do_mission Star95 spectrograph3) )
                    (tag t85 (do_mission Planet96 thermograph1) )
                    (tag t86 (do_mission Phenomenon97 spectrograph3) )
                    (tag t87 (do_mission Planet98 spectrograph3) )
                    (tag t88 (do_mission Phenomenon99 thermograph2) )
                    (tag t89 (do_mission Planet100 thermograph1) )
                    (tag t90 (do_mission Star101 image0) )
                    (tag t91 (do_mission Planet102 thermograph2) )
                    (tag t92 (do_mission Phenomenon103 image0) )
                    (tag t93 (do_mission Phenomenon104 thermograph2) )
                    (tag t94 (do_turning satellite8 Phenomenon57) )
                )
        :constraints(and
                        (after (and 
                                    (have_image Phenomenon5 thermograph1)
                                    (have_image Star6 thermograph1)
                                    (have_image Star7 spectrograph3)
                                    (have_image Phenomenon8 image0)
                                    (have_image Phenomenon9 image0)
                                    (have_image Star10 spectrograph3)
                                    (have_image Planet11 thermograph2)
                                    (have_image Phenomenon12 image0)
                                    (have_image Phenomenon13 thermograph1)
                                    (have_image Phenomenon14 thermograph2)
                                    (have_image Phenomenon15 thermograph1)
                                    (have_image Planet16 thermograph2)
                                    (have_image Phenomenon17 thermograph1)
                                    (have_image Planet18 thermograph1)
                                    (have_image Planet19 thermograph1)
                                    (have_image Phenomenon21 image0)
                                    (have_image Planet22 spectrograph3)
                                    (have_image Planet23 thermograph2)
                                    (have_image Phenomenon24 thermograph2)
                                    (have_image Phenomenon25 thermograph2)
                                    (have_image Phenomenon26 thermograph2)
                                    (have_image Phenomenon27 thermograph2)
                                    (have_image Star28 thermograph2)
                                    (have_image Phenomenon30 image0)
                                    (have_image Phenomenon31 image0)
                                    (have_image Phenomenon32 image0)
                                    (have_image Phenomenon33 thermograph2)
                                    (have_image Planet35 thermograph2)
                                    (have_image Star36 spectrograph3)
                                    (have_image Phenomenon37 thermograph1)
                                    (have_image Phenomenon38 thermograph2)
                                    (have_image Phenomenon39 thermograph2)
                                    (have_image Star41 thermograph2)
                                    (have_image Phenomenon42 thermograph2)
                                    (have_image Star43 thermograph1)
                                    (have_image Planet44 thermograph2)
                                    (have_image Planet45 thermograph2)
                                    (have_image Planet46 thermograph1)
                                    (have_image Star47 spectrograph3)
                                    (have_image Star48 spectrograph3)
                                    (have_image Phenomenon50 spectrograph3)
                                    (have_image Phenomenon51 image0)
                                    (have_image Phenomenon52 spectrograph3)
                                    (have_image Planet53 spectrograph3)
                                    (have_image Planet54 spectrograph3)
                                    (have_image Star55 thermograph1)
                                    (have_image Planet56 thermograph1)
                                    (have_image Phenomenon57 spectrograph3)
                                    (have_image Planet59 spectrograph3)
                                    (have_image Phenomenon60 thermograph2)
                                    (have_image Star61 thermograph2)
                                    (have_image Star62 thermograph2)
                                    (have_image Star63 thermograph2)
                                    (have_image Planet64 thermograph2)
                                    (have_image Planet65 spectrograph3)
                                    (have_image Star66 spectrograph3)
                                    (have_image Planet67 thermograph2)
                                    (have_image Phenomenon68 thermograph1)
                                    (have_image Star69 thermograph2)
                                    (have_image Planet70 thermograph1)
                                    (have_image Star71 image0)
                                    (have_image Phenomenon72 image0)
                                    (have_image Planet73 thermograph1)
                                    (have_image Star74 thermograph1)
                                    (have_image Phenomenon75 thermograph1)
                                    (have_image Planet76 spectrograph3)
                                    (have_image Star77 thermograph1)
                                    (have_image Planet78 image0)
                                    (have_image Planet79 thermograph2)
                                    (have_image Phenomenon80 thermograph2)
                                    (have_image Phenomenon81 thermograph1)
                                    (have_image Planet82 image0)
                                    (have_image Star83 spectrograph3)
                                    (have_image Phenomenon84 thermograph2)
                                    (have_image Planet85 image0)
                                    (have_image Planet86 thermograph1)
                                    (have_image Phenomenon87 spectrograph3)
                                    (have_image Planet88 image0)
                                    (have_image Star90 thermograph2)
                                    (have_image Phenomenon91 spectrograph3)
                                    (have_image Star92 thermograph1)
                                    (have_image Phenomenon93 thermograph2)
                                    (have_image Planet94 thermograph2)
                                    (have_image Star95 spectrograph3)
                                    (have_image Planet96 thermograph1)
                                    (have_image Phenomenon97 spectrograph3)
                                    (have_image Planet98 spectrograph3)
                                    (have_image Phenomenon99 thermograph2)
                                    (have_image Planet100 thermograph1)
                                    (have_image Star101 image0)
                                    (have_image Planet102 thermograph2)
                                    (have_image Phenomenon103 image0)
                                    (have_image Phenomenon104 thermograph2)
                                    (pointing satellite8 Phenomenon57)
                                ) t94)
                    )
)
