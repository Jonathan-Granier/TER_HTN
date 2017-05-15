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


;ALGO :
;Recupere tout les gens
;Poser les gens qui ont besoin d'etre Poser
;Une fois tout les gens Recupere
;Aller tous les poser


(:method do_problem

        :parameters ()
        :expansion  (
                        (tag t1 (do_check_floor ?f1))
                        (tag t2 (do_take_all ?f1))
                        ;(tag t3 (do_start_throw_all))
                    )
        :constraints( 
                    and 
                        (before ( and 
                                    ( lift-at ?f1)
                                    ) 
                        t1
                        )
                         
                    )
    )

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;                   Check un étage
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Fait descendre les personnes qui doivent descendre
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
                                ) 
                        t1
                        )
                    )


)


; Fin du check de l'etage

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;                   Recupere tout le monde
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(:method do_take_all
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



;; Tant qu'il reste une personne qui n'est pas monté dans l'ascenseur et qui est toujours à son point de départ
(:method do_take_all
    :parameters (?f - floor )
    :expansion (
                    
                    (tag t1 (up ?f ?to))
                    
                    (tag t3 (do_check_floor ?to))
                    (tag t4 (do_take_all ?to))
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

(:method do_take_all
    :parameters (?f - floor)
    :expansion (
                    
                    (tag t1 (down ?f ?to))
                    (tag t2 (do_check_floor ?to))
                    (tag t3 (do_take_all ?to))
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
;                   Pose tout le monde
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Lancement des methodes pour déposer les personnes.
(:method do_start_throw_all
 :parameters ()
    :expansion (
                    (tag t1 (do_throw_all ?f))
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


(:method do_throw_all
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







;; DANS LES TENEBRES
(:method do_throw_all
 :parameters (?f - floor)
    :expansion (
                    (tag t1 (up ?f ?to))
                    (tag t2 (do_check_floor ?to))
                    (tag t3 (do_throw_all ?to))
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

(:method do_throw_all
 :parameters (?f - floor)
    :expansion (
                    (tag t1 (down ?f ?to))
                    (tag t2 (do_check_floor ?to))
                    (tag t3 (do_throw_all ?to))
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



