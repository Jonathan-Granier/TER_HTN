(define (domain parking)
    (:requirements :strips :typing :negative-preconditions :htn :equality)
 (:types car curb)
 (:predicates 
    (at-curb ?car - car) 
    (at-curb-num ?car - car ?curb - curb)
    (behind-car ?car ?front-car - car)
    (car-clear ?car - car) 
    (curb-clear ?curb - curb)
 )

 

	(:action move-curb-to-curb
		:parameters (?car - car ?curbsrc ?curbdest - curb)
		:precondition (and 
			(car-clear ?car)
			(curb-clear ?curbdest)
			(at-curb-num ?car ?curbsrc)
		)
		:effect (and 
			(not (curb-clear ?curbdest))
			(curb-clear ?curbsrc)
			(at-curb-num ?car ?curbdest)
			(not (at-curb-num ?car ?curbsrc))
                         
		)
	)

	(:action move-curb-to-car
		:parameters (?car - car ?curbsrc - curb ?cardest - car)
		:precondition (and 
			(car-clear ?car)
			(car-clear ?cardest)
			(at-curb-num ?car ?curbsrc)
			(at-curb ?cardest) 
		)
		:effect (and 
			(not (car-clear ?cardest))
			(curb-clear ?curbsrc)
			(behind-car ?car ?cardest)
			(not (at-curb-num ?car ?curbsrc))
			(not (at-curb ?car))
                         
		)
	)

	(:action move-car-to-curb
		:parameters (?car - car ?carsrc - car ?curbdest - curb)
		:precondition (and 
			(car-clear ?car)
			(curb-clear ?curbdest)
			(behind-car ?car ?carsrc)
		)
		:effect (and 
			(not (curb-clear ?curbdest))
			(car-clear ?carsrc)
			(at-curb-num ?car ?curbdest)
			(not (behind-car ?car ?carsrc))
			(at-curb ?car)
                         
		)
	)

	(:action move-car-to-car
		:parameters (?car - car ?carsrc - car ?cardest - car)
		:precondition (and 
			(car-clear ?car)
			(car-clear ?cardest)
			(behind-car ?car ?carsrc)
			(at-curb ?cardest) 
		)
		:effect (and 
			(not (car-clear ?cardest))
			(car-clear ?carsrc)
			(behind-car ?car ?cardest)
			(not (behind-car ?car ?carsrc))
                         
		)
	)


(:action nop
    :parameters ()
    :precondition (and )
    :effect         (and )
)


;------------------------------------------------------------------
;                               Methods
;------------------------------------------------------------------
;                          Corriger une place
;------------------------------------------------------------------

;; Algo :
; On considere qu'il y a toujours au moins 2 place libre
; Pour chaque configuration de place voulu ( curb car_i car_j)
; 	liberer car_i (ne doit pas etre au fond d'une place) 
;	liberer curb-x
;	Mettre car_i dans curb
;	liberer car_j 
;	Mettre car_j dans curb	
;
;


;; Cas où la place est déjà correct ( curb car_i car_j)
(:method do_correct_a_curb 

        :parameters (?curb - curb ?car_i - car ?car_j - car)
        :expansion  (
                        (tag t1 (nop))
                    )
        :constraints( 
                    and 
                        (before ( and 
                                    	( at-curb-num ?car_i ?curb )
      									( behind-car ?car_j ?car_i )
                                    ) 
                        t1
                        )
                         
                    )
    )

;; Cas où car_i est déjà à la bonne place

(:method do_correct_a_curb 

        :parameters (?curb - curb ?car_i - car ?car_j - car)
        :expansion  (
                        ;(tag t1 (nop))
                        (tag t1 (do_clear_j ?car_i))
                        (tag t2 (do_clear_j ?car_j)) 
                        (tag t3 (do_move_car_j ?car_i ?car_j))                   
                    )
        :constraints( 
                    and 
                        (before ( and 
                                   	( at-curb-num ?car_i ?curb )
                                   	( not ( behind-car ?car_j ?car_i ))
                                   	;( behind-car ?car_x ?car_i )
                                    ) 
                        t1
                        )
                         
                    )
    )

;; Cas où car_i n'est pas à la bonne place
(:method do_correct_a_curb 

        :parameters (?curb - curb ?car_i - car ?car_j - car)
        :expansion  (
                        ;(tag t1 (nop))
                        (tag t1 (do_clear_i ?car_i))
                        (tag t2 (do_clear_curb ?curb))
                        (tag t3 (do_move_car_i ?curb ?car_i))
                        (tag t4 (do_clear_j ?car_j))
                        (tag t5 (do_move_car_j ?car_i ?car_j))

                    )
        :constraints( 
                    and 
                        (before ( and 
                                    ( not ( at-curb-num ?car_i ?curb ))
                                ) 
                        t1
                        )
                         
                    )
    )


;------------------------------------------------------------------
;							Transfert
;------------------------------------------------------------------
; Corrige le placement des 2 dernières voitures. 
; Si elle sont l'une sur l'autre , place celle du dessus dans la place libre

; Cas ou elles sont bien placé dans une place chacune
(:method do_transfert_car

        :parameters (?car_a - car ?car_b - car)
        :expansion  (
                        (tag t1 (nop))
                       

                    )
        :constraints( 
                    and 
                        (before ( and 
                                    ( at-curb ?car_a)
                                    ( at-curb ?car_b)
                                ) 
                        t1
                        )
                         
                    )
    )
;; Cas où car_a n'est pas bien placé
(:method do_transfert_car

        :parameters (?car_a - car ?car_b - car)
        :expansion  (
                        (tag t1 (move-car-to-curb ?car_a ?car_b ?curbdest))
                       

                    )
        :constraints( 
                    and 
                        (before ( and 
                                    ( not ( at-curb ?car_a))
                                    ( at-curb ?car_b)
                                    ( car-clear ?car_a)
									( curb-clear ?curbdest)
									( behind-car ?car_a ?car_b)
                                ) 
                        t1
                        )
                         
                    )
    )

;; Cas où car_b n'est pas bien placé
(:method do_transfert_car

        :parameters (?car_a - car ?car_b - car)
        :expansion  (
                        (tag t1 (move-car-to-curb ?car_b ?car_a ?curbdest))
                       

                    )
        :constraints( 
                    and 
                        (before ( and 
                                    ( at-curb ?car_a)
                                    ( not ( at-curb ?car_b))
                                    ( car-clear ?car_b)
									( curb-clear ?curbdest)
									( behind-car ?car_b ?car_a)
                                ) 
                        t1
                        )
                         
                    )
    )




;------------------------------------------------------------------
;                          Liberer car_i
;------------------------------------------------------------------
;; Cas où car_i est clear et que car_i n'est pas au fond d'une place
(:method do_clear_i 

        :parameters (?car_i - car)
        :expansion  (
                        (tag t1 (nop))

                    )
        :constraints( 
                    and 
                        (before ( and 
                                    ( car-clear ?car_i)
                                    ( behind-car ?car_i ?c)
                                ) 
                        t1
                        )
                         
                    )
    )

;; Cas où car_i est clear mais que car_i est au fond d'une place
(:method do_clear_i 

        :parameters (?car_i - car)
        :expansion  (
                        
                        ;(tag t1 (nop))
                        (tag t1 (move-curb-to-car ?car_i ?curbsrc ?cardest))

                    )
        :constraints( 
                    and 
                        (before ( and 
                                    ( car-clear ?car_i)
                                    ( at-curb ?car_i)  
									( car-clear ?cardest)
									( at-curb-num ?car_i ?curbsrc)
									( at-curb ?cardest) 
                                ) 
                        t1
                        )
                         
                    )
    )

;; Cas où car_i n'est pas clear
(:method do_clear_i 

        :parameters (?car_i - car)
        :expansion  (
                        ;(tag t1 (nop))
                       	(tag t1 (do_move_car ?c ?car_i))
                        (tag t2 (do_clear_i ?car_i))

                    )
        :constraints( 
                    and 
                        (before ( and 
                                    ( not ( car-clear ?car_i))
                                    ( behind-car ?c ?car_i)
                                ) 
                        t1
                        )
                         
                    )
    )

;------------------------------------------------------------------
;                          Liberer car_j
;------------------------------------------------------------------
;; Cas où car_j est clear 
(:method do_clear_j 

        :parameters (?car_j - car)
        :expansion  (
                        (tag t1 (nop))

                    )
        :constraints( 
                    and 
                        (before ( and 
                                    ( car-clear ?car_j)
                                    
                                ) 
                        t1
                        )
                         
                    )
    )

;; Cas où car_j n'est pas clear
(:method do_clear_j 

        :parameters (?car_j - car)
        :expansion  (
                        (tag t1 (do_move_car ?c ?car_j))

                    )
        :constraints( 
                    and 
                        (before ( and 
                                    ( not ( car-clear ?car_j))
                                    ( behind-car ?c ?car_j)
                                ) 
                        t1
                        )
                         
                    )
    )




;------------------------------------------------------------------
;              			Liberer une place
;------------------------------------------------------------------

; Cas où la place est déjà clear

(:method do_clear_curb

        :parameters (?curb - curb)
        :expansion  (
                        (tag t1 (nop))

                    )
        :constraints( 
                    and 
                        (before ( and 
									( curb-clear ?curb)

                                ) 
                        t1
                        )
                         
                    )
    )

; Cas où il y a qu'une seul voiture
(:method do_clear_curb

        :parameters (?curb - curb)
        :expansion  (
                       (tag t1 (move-curb-to-car ?car ?curb ?cardest))

                    )
        :constraints( 
                    and 
                        (before ( and 
									( not ( curb-clear ?curb))
									( car-clear ?car)
									( car-clear ?cardest)
									( at-curb-num ?car ?curb)
									( at-curb ?cardest) 

                                ) 
                        t1
                        )
                         
                    )
    )
; Cas ou il y a 2 voiture
(:method do_clear_curb

        :parameters (?curb - curb)
        :expansion  (
                        (tag t1 (do_move_car ?car ?carsrc))
                        (tag t2 (do_clear_curb ?curb))

                    )
        :constraints( 
                    and 
                        (before ( and 
									( not( curb-clear ?curb))
									( behind-car ?car ?carsrc)
									( at-curb-num ?carsrc ?curb)

                                ) 
                        t1
                        )
                         
                    )
    )


;------------------------------------------------------------------
;              			Bouge une voiture i
;------------------------------------------------------------------

;; Cas où la voiture est dans un place 
(:method do_move_car_i

        :parameters (?curbdest - curb ?car_i - car)
        :expansion  (
                        (tag t1 (move-curb-to-curb ?car_i ?curbsrc ?curbdest))
                    )
        :constraints( 
                    and 
                        (before ( and 
                        			( car-clear ?car_i)
									( curb-clear ?curbdest)
									( at-curb-num ?car_i ?curbsrc)

                                ) 
                        t1
                        )
                         
                    )
    )
;; Cas où la voiture est devant une autre voiture
(:method do_move_car_i

        :parameters (?curbdest - curb ?car_i - car)
        :expansion  (
                        ;(tag t1(nop))
                        (tag t1 (move-car-to-curb ?car_i ?carsrc ?curbdest))
                    )
        :constraints( 
                    and 
                        (before ( and 
                        			( car-clear ?car_i)
									( curb-clear ?curbdest)
									( behind-car ?car_i ?carsrc)
									

                                ) 
                        t1
                        )
                         
                    )
    )

;------------------------------------------------------------------
;              			Bouge une voiture j
;------------------------------------------------------------------

;; Cas où la voiture est dans un place 
(:method do_move_car_j

        :parameters (?car_i - car ?car_j - car)
        :expansion  (
                        (tag t1 (move-curb-to-car ?car_j ?curbsrc ?car_i))
                    )
        :constraints( 
                    and 
                        (before ( and 
                        			( car-clear ?car_j)
									( car-clear ?car_i)
									( at-curb-num ?car_j ?curbsrc)
									( at-curb ?car_i) 
                                ) 
                        t1
                        )
                         
                    )
    )

;; Cas où la voiture est devant une autre voiture
(:method do_move_car_j

        :parameters (?car_i - car ?car_j - car)
        :expansion  (
                        ;(tag t1 (nop))
                        (tag t1 (move-car-to-car ?car_j ?carsrc ?car_i))
                    )
        :constraints( 
                    and 
                        (before ( and 
                        			( car-clear ?car_j)
									( car-clear ?car_i)
									( behind-car ?car_j ?carsrc)
									( at-curb ?car_i) 

                                ) 
                        t1
                        )
                         
                    )
    )



;------------------------------------------------------------------
;              	Bouge une voiture qui est ni i , ni j
;------------------------------------------------------------------
; Une voiture qui devant une autre voiture doit bouger

; Cas où la voiture doit bouger sur une autre voiture
(:method do_move_car 

        :parameters (?car - car ?carsrc - car)
        :expansion  (
                        ;(tag t1 (nop))
                        (tag t1 (move-car-to-car ?car ?carsrc ?cardest))

                    )
        :constraints( 
                    and 
                        (before ( and 
                                   	( car-clear ?car)
									( car-clear ?cardest)
									( behind-car ?car ?carsrc)
									( at-curb ?cardest) 

                                ) 
                        t1
                        )
                         
                    )
    )

; Cas où la voiture doit bouger sur une place vide
(:method do_move_car 

        :parameters (?car - car ?carsrc - car)
        :expansion  (
                        ;(tag t1 (nop))
                        (tag t1 (move-car-to-curb ?car ?carsrc ?curbdest))

                    )
        :constraints( 
                    and 
                        (before ( and 
                           			( car-clear ?car)
									( curb-clear ?curbdest)
									( behind-car ?car ?carsrc)

                                ) 
                        t1
                        )
                         
                    )
    )
)
