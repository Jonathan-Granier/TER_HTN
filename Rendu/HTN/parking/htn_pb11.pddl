(define (problem parking )
(:domain parking )
	(:requirements :strips :typing :negative-preconditions :htn :equality)

(:objects
     car_00  car_01  car_02  car_03  car_04  car_05  car_06  car_07  car_08  car_09  car_10  car_11  car_12  car_13  car_14  car_15  car_16  car_17  car_18  car_19  car_20  car_21  car_22  car_23  car_24  car_25  car_26  car_27 - car
     curb_00 curb_01 curb_02 curb_03 curb_04 curb_05 curb_06 curb_07 curb_08 curb_09 curb_10 curb_11 curb_12 curb_13 curb_14 - curb
   )
(:init
     
    (at-curb car_02 )
    (at-curb-num car_02 curb_00 )
    (behind-car car_20 car_02 )
    (car-clear car_20 )
    (at-curb car_03 )
    (at-curb-num car_03 curb_01 )
    (behind-car car_17 car_03 )
    (car-clear car_17 )
    (at-curb car_06 )
    (at-curb-num car_06 curb_02 )
    (behind-car car_15 car_06 )
    (car-clear car_15 )
    (at-curb car_05 )
    (at-curb-num car_05 curb_03 )
    (behind-car car_00 car_05 )
    (car-clear car_00 )
    (at-curb car_09 )
    (at-curb-num car_09 curb_04 )
    (behind-car car_18 car_09 )
    (car-clear car_18 )
    (at-curb car_16 )
    (at-curb-num car_16 curb_05 )
    (behind-car car_08 car_16 )
    (car-clear car_08 )
    (at-curb car_25 )
    (at-curb-num car_25 curb_06 )
    (behind-car car_01 car_25 )
    (car-clear car_01 )
    (at-curb car_14 )
    (at-curb-num car_14 curb_07 )
    (behind-car car_13 car_14 )
    (car-clear car_13 )
    (at-curb car_10 )
    (at-curb-num car_10 curb_08 )
    (behind-car car_22 car_10 )
    (car-clear car_22 )
    (at-curb car_11 )
    (at-curb-num car_11 curb_09 )
    (behind-car car_07 car_11 )
    (car-clear car_07 )
    (at-curb car_19 )
    (at-curb-num car_19 curb_10 )
    (behind-car car_27 car_19 )
    (car-clear car_27 )
    (at-curb car_23 )
    (at-curb-num car_23 curb_11 )
    (behind-car car_12 car_23 )
    (car-clear car_12 )
    (at-curb car_26 )
    (at-curb-num car_26 curb_12 )
    (behind-car car_04 car_26 )
    (car-clear car_04 )
    (at-curb car_24 )
    (at-curb-num car_24 curb_13 )
    (car-clear car_24 )
    (at-curb car_21 )
    (at-curb-num car_21 curb_14 )
    (car-clear car_21 )
   )
(:goal
	:tasks  (
			 ;; A remplir 
                (tag t1 (do_correct_a_curb curb_00 car_00 car_15))
                (tag t2 (do_correct_a_curb curb_01 car_01 car_16))
                (tag t3 (do_correct_a_curb curb_02 car_02 car_17))
                (tag t4 (do_correct_a_curb curb_03 car_03 car_18))
                (tag t5 (do_correct_a_curb curb_04 car_04 car_19))
                (tag t6 (do_correct_a_curb curb_05 car_05 car_20))
                (tag t7 (do_correct_a_curb curb_06 car_06 car_21))
                (tag t8 (do_correct_a_curb curb_07 car_07 car_22))
                (tag t9 (do_correct_a_curb curb_08 car_08 car_23))
                (tag t10 (do_correct_a_curb curb_09 car_09 car_24))
                (tag t11 (do_correct_a_curb curb_10 car_10 car_25))
                (tag t12 (do_correct_a_curb curb_11 car_11 car_26))
                (tag t13 (do_correct_a_curb curb_12 car_12 car_27))
                (tag t14 (do_transfert_car_to_curb car_13 car_14)) 
			
		)
	:constraints(and
			(after (and
      						(at-curb-num car_00 curb_00 )
      						(behind-car car_15 car_00 )
      						(at-curb-num car_01 curb_01 )
      						(behind-car car_16 car_01 )
      						(at-curb-num car_02 curb_02 )
      						(behind-car car_17 car_02 )
      						(at-curb-num car_03 curb_03 )
      						(behind-car car_18 car_03 )
      						(at-curb-num car_04 curb_04 )
      						(behind-car car_19 car_04 )
      						(at-curb-num car_05 curb_05 )
      						(behind-car car_20 car_05 )
      						(at-curb-num car_06 curb_06 )
      						(behind-car car_21 car_06 )
      						(at-curb-num car_07 curb_07 )
      						(behind-car car_22 car_07 )
      						(at-curb-num car_08 curb_08 )
      						(behind-car car_23 car_08 )
      						(at-curb-num car_09 curb_09 )
      						(behind-car car_24 car_09 )
      						(at-curb-num car_10 curb_10 )
      						(behind-car car_25 car_10 )
      						(at-curb-num car_11 curb_11 )
      						(behind-car car_26 car_11 )
      						(at-curb-num car_12 curb_12 )
      						(behind-car car_27 car_12 )
      						(at-curb-num car_13 curb_13 )
      						(at-curb-num car_14 curb_14 )
     )
				 t1)
		)
))
