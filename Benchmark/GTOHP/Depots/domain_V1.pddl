;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; Depots Version 1
; Ce domaine , a cause de la 1er method (do_put_on) prend trop de place en mémoire, 
; la V2 corrige parciellement ce probleme
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;



(define (domain Depot)
(:requirements :strips :typing :negative-preconditions :htn :equality)
(:types place locatable - object
	depot distributor - place
        truck hoist surface - locatable
        pallet crate - surface)

(:predicates (at ?x - locatable ?y - place) 
             (on ?x - crate ?y - surface)
             (in ?x - crate ?y - truck)
             (lifting ?x - hoist ?y - crate)
             (available ?x - hoist)
             (clear ?x - surface))
	
;Un camion se déplace d'un point Y à un point Z
(:action Drive
:parameters (?x - truck ?y - place ?z - place) 
:precondition (and (at ?x ?y))
:effect (and (not (at ?x ?y)) (at ?x ?z)))



(:action Lift
:parameters (?x - hoist ?y - crate ?z - surface ?p - place)
:precondition (and (at ?x ?p) (available ?x) (at ?y ?p) (on ?y ?z) (clear ?y))
:effect (and (not (at ?y ?p)) (lifting ?x ?y) (not (clear ?y)) (not (available ?x)) 
             (clear ?z) (not (on ?y ?z))))

(:action Drop 
:parameters (?x - hoist ?y - crate ?z - surface ?p - place)
:precondition (and (at ?x ?p) (at ?z ?p) (clear ?z) (lifting ?x ?y))
:effect (and (available ?x) (not (lifting ?x ?y)) (at ?y ?p) (not (clear ?z)) (clear ?y)
		(on ?y ?z)))

(:action Load
:parameters (?x - hoist ?y - crate ?z - truck ?p - place)
:precondition (and (at ?x ?p) (at ?z ?p) (lifting ?x ?y))
:effect (and (not (lifting ?x ?y)) (in ?y ?z) (available ?x)))

(:action Unload 
:parameters (?x - hoist ?y - crate ?z - truck ?p - place)
:precondition (and (at ?x ?p) (at ?z ?p) (available ?x) (in ?y ?z))
:effect (and (not (in ?y ?z)) (not (available ?x)) (lifting ?x ?y)))


(:action nop
        :parameters ()
        :precondition (and )
        :effect (and )
)



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;  Met une caisse c sur une surface s2    ;;;    
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Cas où la caisse est dans un autre dépot ou distributeur
(:method do_put_on 

       :parameters (?c - crate ?s2 - surface)
        :expansion  (
                        (tag t1 (do_clear ?c ?p1))
                        (tag t2 (Lift ?h1 ?c ?s1 ?p1))
                        (tag t3 (do_get_truck ?t ?p1))
                        (tag t4 (Load ?h1 ?c ?t ?p1))
                        (tag t5 (Drive ?t ?p1 ?p2))
                        (tag t6 (do_clear ?s2 ?p2))
                        (tag t7 (Unload ?h2 ?c ?t ?p2))
                        (tag t8 (Drop ?h2 ?c ?s2 ?p2))
                    )
        :constraints( 
                    and 
                        (before ( and 
                                ( at ?c ?p1)
                                ( at ?s1 ?p1)
                                ( on ?c ?s1)
                                ( available ?h1)
                                ( available ?h2)
                                ( at ?h1 ?p1)
                                ( at ?h2 ?p2)
                                ( at ?s2 ?p2)
                                ) 
                        t1
                        )
                    )
    )


;; Cas où la caisse est déjà dans le bon dépot ou distributeur
(:method do_put_on 

       :parameters (?c - crate ?s2 - surface)
        :expansion  (
                        (tag t1 (do_clear ?c ?p))
                        (tag t2 (do_clear ?s2 ?p))
                        (tag t3 (do_lift_crate ?c ?p ?h))
                        (tag t4 (Drop ?h ?c ?s2 ?p))
                    )
        :constraints( 
                    and 
                        (before ( and 
                                ( at ?c ?p)
                                ( at ?s2 ?p)
                                ( available ?h)
                                ( at ?h ?p)
                                ) 
                        t1
                        )
                    )
    )


;;Cas où la caisse est dans un camion t

(:method do_put_on 

       :parameters (?c - crate ?s2 - surface)
        :expansion  (
                        
                        (tag t1 (do_get_truck ?t ?p))
                        (tag t2 (do_clear ?s2 ?p))
                        (tag t3 (Unload ?h ?c ?t ?p))
                        (tag t4 (Drop ?h ?c ?s2 ?p))
                    )
        :constraints( 
                    and 
                        (before ( and 
                                ( in ?c ?t)
                                ( available ?h)
                                ( at ?h ?p)
                                ( at ?s2 ?p)
                                ) 
                        t1
                        )
                    )
    )

;; Cas où la caisse est bien placé
(:method do_put_on 

       :parameters (?c - crate ?s2 - surface)
        :expansion  (
                        
                        (tag t1 (nop))
                    )
        :constraints( 
                    and 
                        (before ( and 
                                ( on ?c ?s2)
                                ) 
                        t1
                        )
                    )
    )


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;         Nettoie une surface             ;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Nettoie la surface s1 à la place p1 et met ce qui a dessus dans un camion

;;Cas où s1 est clear
(:method do_clear
        :parameters (?s1 - surface ?p1 - place)
        :expansion  (
                        (tag t1 (nop))
                    )
        :constraints( 
                    and 
                        (before ( and 
                                ( clear ?s1)
                                (at ?s1 ?p1)
                                ) 
                        t1
                        )
                    )
    )

;;Cas où s1 n'est pas clear
(:method do_clear
        :parameters (?s1 - surface ?p1 - place)
        :expansion  (
                        (tag t1 (do_clear ?c ?p1))
                        (tag t2 (Lift ?h1 ?c ?s1 ?p1))
                        (tag t3 (do_get_truck ?t ?p1))
                        (tag t4 (Load ?h1 ?c ?t ?p1))
                    )
        :constraints( 
                    and 
                        (before ( and 
                                ( not ( clear ?s1)) 
                                ( on ?c ?s1 )
                                ( at ?s1 ?p1)
                                ( at ?h1 ?p1)
                                ) 
                        t1
                        )
                    )
    )









;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;         Récupere un camion              ;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Raméne un camion à la place p si il n'y est pas déjà

(:method do_get_truck
        :parameters (?t - truck ?p1 - place)
        :expansion  (
                        (tag t1 (nop))
                    )
        :constraints( 
                    and 
                        (before ( and 
                                ( at ?t ?p1)
                                ) 
                        t1
                        )
                    )
    )



(:method do_get_truck
        :parameters (?t - truck ?p1 - place)
        :expansion  (
                        (tag t1 (Drive ?t ?p2 ?p1))
                    )
        :constraints( 
                    and 
                        (before ( and 
                                ( not ( at ?t ?p1))
                                ) 
                        t1
                        )
                    )
)







;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;         Souleve une caisse              ;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;;Souleve une caisse avec une grue en fonction de là ou elle est (camion ou surface)

;; Cas où la caisse est dans un camion
(:method do_lift_crate
    :parameters (?c - crate ?p - place ?h - hoist)
    :expansion (
                    (tag t1 (do_get_truck ?t ?p))
                    (tag t2 (Unload ?h ?c ?t ?p))
                )
    :constraints(
                and
                    (before ( and
                            ( in ?c ?t)
                            ( at ?h ?p)
                            )
                    t1
                    )

                )



)

;; Cas où la caisse est sur une surface
(:method do_lift_crate
    :parameters (?c - crate ?p - place ?h - hoist)
    :expansion (
                    (tag t1 (Lift ?h ?c ?s ?p))
                )
    :constraints(
                and
                    (before ( and
                            ( on ?c ?s)
                            ( at ?c ?p)
                            ( at ?s ?p)
                            ( at ?h ?p)
                            )
                    t1
                    )

                )



)

)
