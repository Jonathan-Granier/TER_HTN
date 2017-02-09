;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Satellite
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


(define (domain satellite)
  	(:requirements :strips :typing :htn :negative-preconditions :equality)
  	(:types satellite direction instrument mode)
 	(:predicates	(on_board ?i - instrument ?s - satellite)
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
   :precondition (and 	(on_board ?i ?s)
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
               )
   :effect (have_image ?d ?m)
  )
  
  (:action nop
   :parameters ()
   :precondition (and )
   :effect (and )
   )
  
  (:method do_mission
  	:parameters (?d - direction ?m - mode)
  	:expansion	(
  					(tag t2 (do_prepare ?s ?i ?d))
  					(tag t3 (take_image ?s ?d ?i ?m))
  				)
  	:constraints(and
  					(series t2 t3)
  					;;(after (have_image ?d ?m) t3)
  				)
  )
  
  (:method do_prepare
  :parameters	(?s - satellite ?i - instrument ?d - direction)
  :expansion	(
  					(tag t1 (do_switching ?s ?i))
  					(tag t2 (do_turning ?s ?d))
  				)
  :constraints	(and
  					(series t1 t2)
  					;;(after (and (pointing ?s ?d) (power_on ?i)) t2)
  				)
  )
  
  (:method do_switching
  :parameters	(?s - satellite ?i - instrument)
  :expansion	(
  					(tag t1 (make_power_available ?s ?other_i))
  					(tag t2	(switch_on ?i ?s))
  					(tag t3 (do_calibration ?s ?i ?d))
  				)
  :constraints	(and
  					(series t1 t2 t3)
  					(before (and (on_board ?i ?s) (on_board ?other_i ?s) (not (power_avail ?s))) t1)
  					;;(after	(power_on ?i) t2)
  					;;(after (calibrated ?i) t3)
  				)
  )
  
  (:method do_switching
  :parameters	(?s - satellite ?i - instrument)
  :expansion	(
  					(tag t1	(switch_on ?i ?s))
  					(tag t2 (do_calibration ?s ?i ?d))
  				)
  :constraints	(and
  					(series t1 t2)
  					(before (and (on_board ?i ?s) (power_avail ?s)) t1)
  					;;(after	(power_on ?i) t1)
  					;;(after (calibrated ?i) t2)
  				)
  )
  
  (:method do_switching
  :parameters	(?s - satellite ?i - instrument)
  :expansion	(
  					(tag t1 (nop))
  				)
  :constraints	(and
  					(before (and (power_on ?i)) t1)
  				)
  )
  
  (:method do_calibration
  :parameters	(?s - satellite ?i - instrument ?d - direction)
  :expansion	(
  					(tag t1 (do_prepare ?s ?i ?d))
  					(tag t2 (calibrate ?s ?i ?d))
  				)
  :constraints	(and
  					(series t1 t2)
  					(before (not(calibrated ?i)) t1)
  					;;(after (calibrated ?i) t2)
  				)
  )
  
  (:method do_calibration
  :parameters	(?s - satellite ?i - instrument ?d - direction)
  :expansion	(
  					(tag n (nop))
  				)
  :constraints	(and
  					(before (calibrated ?i) n)
  				)
  )
  
  (:method make_power_available
  :parameters	(?s - satellite ?other_i - instrument)
  :expansion	(
  					(tag t1 (switch_off ?other_i ?s))
  				)
  :constraints	(and
  					(before (and (power_on ?other_i) (not(power_avail ?s))) t1)
  					;;(after	(and (not(power_on ?other_i)) (power_avail ?s)) t1)
  				)
  )
  
  (:method do_turning
  :parameters	(?s - satellite ?d - direction)
  :expansion	(
  					(tag t1 (turn_to ?s ?d ?other_d))
  				)
  :constraints	(and
  					(before (and (pointing ?s ?other_d) (not(pointing ?s ?d))) t1)
  					;;(after	(and (pointing ?s ?d) (not(pointing ?s ?other_d))) t1)
  				)
  )
  
  (:method do_turning
  :parameters	(?s - satellite ?d - direction)
  :expansion	(
  					(tag n (nop))
  				)
  :constraints	(and
  					(before (pointing ?s ?d) n)
  				)
  )
)

