;-----------------------------------------------------------
; Version 1
; le problem est résolu par un sucetion de presonne à servir
;-----------------------------------------------------------



(define (domain miconic)
    (:requirements :strips :typing :negative-preconditions :htn :equality)
  (:types passenger - object
          floor - object
         )

(:predicates 
(origin ?person - passenger ?floor - floor)
;; entry of ?person is ?floor
;; inertia

(destin ?person - passenger ?floor - floor)
;; exit of ?person is ?floor
;; inertia

(above ?floor1 - floor  ?floor2 - floor)
;; ?floor2 is located above of ?floor1

(boarded ?person - passenger)
;; true if ?person has boarded the lift

(not-boarded ?person - passenger)
;; true if ?person has not boarded the lift

(served ?person - passenger)
;; true if ?person has alighted as her destination

(not-served ?person - passenger)
;; true if ?person is not at their destination

(lift-at ?floor - floor)
;; current position of the lift is at ?floor
)


;;stop and allow boarding

(:action board
  :parameters (?f - floor ?p - passenger)
  :precondition (and (lift-at ?f) (origin ?p ?f))
  :effect (boarded ?p))

(:action depart
  :parameters (?f - floor ?p - passenger)
  :precondition (and (lift-at ?f) (destin ?p ?f)
		     (boarded ?p))
  :effect (and (not (boarded ?p))
	       (served ?p)))
;;drive up

(:action up
  :parameters (?f1 - floor ?f2 - floor)
  :precondition (and (lift-at ?f1) (above ?f1 ?f2))
  :effect (and (lift-at ?f2) (not (lift-at ?f1))))


;;drive down

(:action down
  :parameters (?f1 - floor ?f2 - floor)
  :precondition (and (lift-at ?f1) (above ?f2 ?f1))
  :effect (and (lift-at ?f2) (not (lift-at ?f1))))

(:action nop
    :parameters ()
    :precondition (and )
    :effect         (and )
)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;                   Resoudre le probleme
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


;Tant qu'il ya des personnes non servie , les servir

;;Cas où tous le monde est servie
(:method do_problem

        :parameters ()
        :expansion  (
                        (tag t1 (nop))  
                    )
        :constraints( 
                    and 
                        (before ( and 
                                    ( lift-at ?f)

                                    ) 
                        t1
                        )
                         
                    )
    )

;;Cas où la personne à servir est dans l'ascenseur
(:method do_problem

        :parameters ()
        :expansion  (
                        (tag t1 (do_throw_passenger ?p))
                        (tag t2 (do_problem))
                    )
        :constraints( 
                    and 
                        (before ( and 
                                    ( not ( served ?p)
                                    ( boarded ?p)
                                    ) 
                        t1
                        )
                         
                    )
    )
;; Cas où la persone à servir est à son point de départ
(:method do_problem

        :parameters ()
        :expansion  (
                        
                        (tag t1 (do_take_passenger ?p))
                        (tag t2 (do_throw_passenger ?p))
                        (tag t3 (do_problem))
                    )
        :constraints( 
                    and 
                        (before ( and 
                                    ( not ( served ?p))
                                    ( not ( boarded ?p))
                                    ) 
                        t1
                        )
                         
                    )
    )


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;                   Check un étage
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


(:method do_check_floor

    :parameters (?f - floor)
    :expansion (
                    (tag t1 (nop))
                    
                )
    :constraints( 
                    and 
                        (before ( and 
                                    ( lift-at ?f)
                                ) 
                        t1
                        )
                    )


)
;; Fait descendre les personnes qui doivent descendre
(:method do_check_floor

    :parameters (?f - floor)
    :expansion (
                    (tag t1 (depart ?f ?p))
                    (tag t2 (do_check_floor ?f))
                )
    :constraints( 
                    and 
                        (before ( and 
                                    ( lift-at ?f)
                                    ( boarded ?p)
                                    ( destin ?p ?f)

                                ) 
                        t1
                        )
                    )


)

; Fait monter les personnes qui doivent monter
(:method do_check_floor

    :parameters (?f - floor)
    :expansion (
                    (tag t1 (board ?f ?p))
                    (tag t2 (do_check_floor ?f))
                )
    :constraints( 
                    and 
                        (before ( and 
                                    ( lift-at ?f)
                                    ( origin ?p ?f)
                                    ( not ( boarded ?p))
                                    ( not ( served ?p))
                                ) 
                        t1
                        )
                    )


)



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;                   Recupere une personne
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;;Cas où l'ascenseur est au bonne étage
(:method do_take_passenger
    :parameters (?p - passenger)
    :expansion (
                    (tag t1 (board ?f ?p))
                    (tag t2 (do_check_floor ?f))

                )
    :constraints( 
                    and 
                        (before ( and 
                                    ( lift-at ?f)
                                    ( origin ?p ?f)
                                ) 
                       t1
                      )
                   )

)



;; Cas où l'ascenseur est en dessous
(:method do_take_passenger
    :parameters (?p - passenger )
    :expansion (
                    
                    (tag t1 (up ?f ?to))
                    (tag t2 (board ?to ?p))
                    (tag t3 (do_check_floor ?to))

                )
    :constraints( 
                    and 
                        (before ( and 
                                    ( lift-at ?f)
                                    ( above ?f ?to)
                                    ( not( boarded ?p))
                                    ( origin ?p ?to)
                                    ( not ( served ?p))
                                ) 
                        t1
                        )
                    )


)

;; Cas où l'ascenseur est en dessus

(:method do_take_passenger
    :parameters (?p - passenger)
    :expansion (
                    
                    (tag t1 (down ?f ?to))
                    (tag t2 (board ?to ?p))
                    (tag t3 (do_check_floor ?to))
                )
    :constraints( 
                    and 
                        (before ( and 
                                    ( lift-at ?f)
                                    ( above ?to ?f)
                                    ( not( boarded ?p))
                                    ( origin ?p ?to)
                                    ( not ( served ?p))
                                ) 
                        t1
                        )
                    )


)


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;                   Pose une personne
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


;;Cas où l'ascenseur est au bonne étage
(:method do_throw_passenger
    :parameters (?p - passenger)
    :expansion (
                    
                    (tag t1 (depart ?f ?p))
                    (tag t2 (do_check_floor ?f))
                )
    :constraints( 
                    and 
                        (before ( and 
                                    ( lift-at ?f)
                                ) 
                        t1
                        )
                    )

    )






;; Cas où l'ascenseur est en dessous
(:method do_throw_passenger
 :parameters (?p - passenger)
    :expansion (
                    (tag t1 (up ?f ?to))
                    (tag t2 (depart ?to ?p))
                    (tag t3 (do_check_floor ?to))
                )
    :constraints( 
                    and 
                        (before ( and 
                                    ( lift-at ?f)
                                    ( above ?f ?to)
                                    ( boarded ?p)
                                    ( destin ?p ?to)
                                ) 
                        t1
                        )
                    )


)

;; Cas où l'ascenseur est en dessus
(:method do_throw_passenger
 :parameters (?p - passenger)
    :expansion (
                    ;(tag t1 (nop))
                    (tag t1 (down ?f ?to))
                    (tag t2 (depart ?to ?f))
                    (tag t3 (do_check_floor ?to))
                )
    :constraints( 
                    and 
                        (before ( and 
                                    ( lift-at ?f)
                                    ( above ?to ?f)
                                    ( boarded ?p)
                                    ( destin ?p ?to)
                                ) 
                        t1
                        )
                    )


)




)



