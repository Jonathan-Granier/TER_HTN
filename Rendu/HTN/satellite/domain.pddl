
(define (domain satellite)
    (:requirements :strips :typing :negative-preconditions :htn :equality)
  (:types satellite direction instrument mode)
 (:predicates 
               (on_board ?i - instrument ?s - satellite)
	       (supports ?i - instrument ?m - mode)
	       (pointing ?s - satellite ?d - direction)
	       (power_avail ?s - satellite)
	       (power_on ?i - instrument)
	       (calibrated ?i - instrument)
	       (have_image ?d - direction ?m - mode)
	       (calibration_target ?i - instrument ?d - direction))
 
 

  (:action turn_to
   :parameters (?s - satellite ?d_new - direction ?d_prev - direction)
   :precondition (and (pointing ?s ?d_prev)
                   (not (= ?d_new ?d_prev))
              )
   :effect (and  (pointing ?s ?d_new)
                 (not (pointing ?s ?d_prev))
           )
  )

 
  (:action switch_on
   :parameters (?i - instrument ?s - satellite)
 
   :precondition (and (on_board ?i ?s) 
                      (power_avail ?s)
                 )
   :effect (and (power_on ?i)
                (not (calibrated ?i))
                (not (power_avail ?s))
           )
          
  )

 
  (:action switch_off
   :parameters (?i - instrument ?s - satellite)
 
   :precondition (and (on_board ?i ?s)
                      (power_on ?i) 
                  )
   :effect (and (not (power_on ?i))
                (power_avail ?s)
           )
  )

  (:action calibrate
   :parameters (?s - satellite ?i - instrument ?d - direction)
   :precondition (and (on_board ?i ?s)
		      (calibration_target ?i ?d)
                      (pointing ?s ?d)
                      (power_on ?i)
                  )
   :effect (calibrated ?i)
  )


  (:action take_image
   :parameters (?s - satellite ?d - direction ?i - instrument ?m - mode)
   :precondition (and (calibrated ?i)
                      (on_board ?i ?s)
                      (supports ?i ?m)
                      (power_on ?i)
                      (pointing ?s ?d)
                      (power_on ?i)
               )
   :effect (have_image ?d ?m)
  )


(:action nop
    :parameters ()
    :precondition (and )
    :effect         (and )
)




;; Prendre une image :
;;     Allumer l'instrument adequat 
;;     Le calibrer 
;;     Orienter le satellite dans la bonne direction
;;     Prendre la photo.

;; Cas où il y a déjà un instrument correspondant au mode allumé
(:method do_take_image

    :parameters (?d - direction ?m - mode  )
    :expansion  (

                    
                    (tag t1 (do_turn_to ?s ?d))
                    (tag t2 (take_image ?s ?d ?i ?m))


                )
    :constraints( 
                and 
                    (before ( and 
                                ( supports ?i ?m)
                                ( on_board ?i ?s) 
                                ( power_on ?i )
                                ( calibrated ?i) 
                            ) 
                    t1
                    )
                )
 
)

;; Cas où il n'y a pas d'instrument correspondant au mode allumé
(:method do_take_image

    :parameters (?d - direction ?m - mode  )
    :expansion  (

                    
                    (tag t1 (do_power_on ?i ?s))
                    (tag t2 (do_calibrate ?i ?s))
                    (tag t3 (do_turn_to ?s ?d))
                    (tag t4 (take_image ?s ?d ?i ?m))


                )
    :constraints( 
                and 
                    (before ( and 
                                ( supports ?i ?m)
                                ( on_board ?i ?s)  
                            ) 
                    t1
                    )
                )

)



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;                   Allume un instrument
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


;; Cas où il est déjà allumé
(:method do_power_on

    :parameters (?i - instrument ?s - satellite  )
    :expansion  (

                    
                    (tag t1 (nop))


                )
    :constraints( 
                and 
                    (before ( and 
                                 ( power_on ?i)
                                 ( on_board ?i ?s)
                            ) 
                    t1
                    )
                )
)

;; Cas où il n'est pas allumé
(:method do_power_on

    :parameters (?i - instrument ?s - satellite  )
    :expansion  (

                    
                    (tag t1 (switch_on ?i ?s))


                )
    :constraints( 
                and 
                    (before ( and 
                                ( not ( power_on ?i))
                                ( on_board ?i ?s)
                            ) 
                    t1
                    )
                )
)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;               Calibre un instrument               ;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Cas où il est déjà calibré
(:method do_calibrate

    :parameters (?i - instrument ?s - satellite )
    :expansion  (

                    
                    (tag t1 (nop))


                )
    :constraints( 
                and 
                    (before ( and 
                                 ( calibrated ?i)
                                 ( on_board ?i ?s)
                            ) 
                    t1
                    )
                )
)

;; Cas où il n'est pas calibré
(:method do_calibrate

    :parameters (?i - instrument ?s - satellite )
    :expansion  (
                    ;(tag t1 (nop))
                    (tag t1 (do_turn_to ?s ?d))
                    (tag t2 (calibrate ?s ?i ?d))


                )
    :constraints( 
                and 
                    (before ( and 
                                ( not ( calibrated ?i))
                                ( on_board ?i ?s)
                                ( calibration_target ?i ?d)
                            ) 
                    t1
                    )
                )
)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;               Oriente un statellite               ;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Cas où il est déjà bien orienté
(:method do_turn_to

    :parameters (?s - satellite ?d - direction)
    :expansion  (

                    
                    (tag t1 (nop))


                )
    :constraints( 
                and 
                    (before ( and 
                                ( pointing ?s ?d)
                            ) 
                    t1
                    )
                )
)

;; Cas où il n'est pas bien orienté
(:method do_turn_to

    :parameters (?s - satellite ?d - direction)
    :expansion  (

                    
                    (tag t1 (turn_to ?s ?d ?from))


                )
    :constraints( 
                and 
                    (before ( and 
                                ( pointing ?s ?from)
                            ) 
                    t1
                    )
                )
)
)

