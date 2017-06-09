;  (c) 2001 Copyright (c) University of Huddersfield
;  Automatically produced from GIPO from the domain hiking
;  All rights reserved. Use of this software is permitted for non-commercial
;  research purposes, and it may be copied only for that use.  All copies must
;  include this copyright message.  This software is made available AS IS, and
;  neither the GIPO team nor the University of Huddersfield make any warranty about
;  the software or its performance.

(define (domain hiking)
  (:requirements :strips :typing :negative-preconditions :htn :equality)
  (:types car tent person couple place )
  (:predicates 
              (at_tent ?x1 - tent ?x2 - place)
              (at_person ?x1 - person ?x2 - place)
              (at_car ?x1 - car ?x2 - place)
              (partners ?x1 - couple ?x2 - person ?x3 - person)
              (up ?x1 - tent)
              (down ?x1 - tent)
              (walked ?x1 - couple ?x2 - place)
              (next ?x1 - place ?x2 - place)
)


  ;  Démonte une tente
  (:action put_down
         :parameters (?x1 - person ?x2 - place ?x3 - tent)
         :precondition  (and 
                        (at_person ?x1 ?x2)
                        (at_tent ?x3 ?x2)
                        (up ?x3))
         :effect    (and 
                    (down ?x3)
                    (not (up ?x3))
)
)
  ;; Monte une tente
  (:action put_up
         :parameters (?x1 - person ?x2 - place ?x3 - tent)
         :precondition  (and 
                        (at_person ?x1 ?x2)
                        (at_tent ?x3 ?x2)
                        (down ?x3))
         :effect    (and 
                    (up ?x3)
                    (not (down ?x3))
)
)

  ;;Transporte 2 personnes d'une place à l'autre
  (:action drive_passenger
         :parameters (?x1 - person ?x2 - place ?x3 - place ?x4 - car ?x5 - person)
         :precondition  (and 
                        (at_person ?x1 ?x2)
                        (at_car ?x4 ?x2)
                        (at_person ?x5 ?x2)
                        (not (= ?x1 ?x5)))
         :effect    (and 
                    (at_person ?x1 ?x3)
                    (not (at_person ?x1 ?x2))
                    (at_car ?x4 ?x3)
                    (not (at_car ?x4 ?x2))
                    (at_person ?x5 ?x3)
                    (not (at_person ?x5 ?x2))
)
)

  ;; Transporte 1 personne d'une place à l'autre
  (:action drive
         :parameters (?x1 - person ?x2 - place ?x3 - place ?x4 - car)
         :precondition  (and 
                        (at_person ?x1 ?x2)
                        (at_car ?x4 ?x2))
         :effect    (and 
                    (at_person ?x1 ?x3)
                    (not (at_person ?x1 ?x2))
                    (at_car ?x4 ?x3)
                    (not (at_car ?x4 ?x2))
)
)
  ;; Transporte 1 personnes et 1 tente d'une place à l'autre
  (:action drive_tent
         :parameters (?x1 - person ?x2 - place ?x3 - place ?x4 - car ?x5 - tent)
         :precondition  (and 
                        (at_person ?x1 ?x2)
                        (at_car ?x4 ?x2)
                        (at_tent ?x5 ?x2)
                        (down ?x5))
         :effect    (and 
                    (at_person ?x1 ?x3)
                    (not (at_person ?x1 ?x2))
                    (at_car ?x4 ?x3)    
                    (not (at_car ?x4 ?x2))
                    (at_tent ?x5 ?x3)
                    (not (at_tent ?x5 ?x2))
)
)
  ;; Transporte 2 personnes et 1 tente d'une place à l'autre
  (:action drive_tent_passenger
         :parameters (?x1 - person ?x2 - place ?x3 - place ?x4 - car ?x5 - tent ?x6 - person)
         :precondition  (and 
                        (at_person ?x1 ?x2)
                        (at_car ?x4 ?x2)
                        (at_tent ?x5 ?x2)
                        (down ?x5)
                        (at_person ?x6 ?x2)
                        (not (= ?x1 ?x6)))
         :effect    (and 
                    (at_person ?x1 ?x3)
                    (not (at_person ?x1 ?x2))
                    (at_car ?x4 ?x3)
                    (not (at_car ?x4 ?x2))
                    (at_tent ?x5 ?x3)
                    (not (at_tent ?x5 ?x2))
                    (at_person ?x6 ?x3)
                    (not (at_person ?x6 ?x2))
)
)

  ;; Un couple marche d'une place à l'autre , il faut que la tente soit monté au départ
  (:action walk_together
         :parameters (?x1 - tent ?x2 - place ?x3 - person ?x4 - place ?x5 - person ?x6 - couple)
         :precondition  (and 
                        (at_tent ?x1 ?x2)
                        (up ?x1)
                        (at_person ?x3 ?x4)
                        (next ?x4 ?x2)
                        (at_person ?x5 ?x4)
                        (not (= ?x3 ?x5))
                        (walked ?x6 ?x4)
                        (partners ?x6 ?x3 ?x5)
                        )
         :effect    (and 
                    (at_person ?x3 ?x2)
                    (not (at_person ?x3 ?x4))
                    (at_person ?x5 ?x2)
                    (not (at_person ?x5 ?x4))
                    (walked ?x6 ?x2)
                    (not (walked ?x6 ?x4))
                    
)
)

(:action nop
         :parameters ()
         :precondition (and )
         :effect (and )
) 
  

; Algo
;

; Pour chaque étape / place
;   Démonter la tente
;   La transporter à la prochaine étape avec 2 voitures
;       Si il n'y a pas 2 voitures sur place , allez en chercher une à l'étape précédante
;   Monter la tente
;   Revenir avec 1 voiture
;   Faire marcher tous les couples à la prochaine étape







;----------------------------------------------------
;                   Do_problem
;----------------------------------------------------

; Fait avancer tous les couples qui doivent avancer d'une place à l'autre
; Recommencer tant qu'il reste des couples à faire avancer

(:method do_problem
    :parameters ()
    :expansion (
                    (tag t1 (nop))
                    
                )
    :constraints
                ( and
                   
                    
                )


)


(:method do_problem
    :parameters ()
    :expansion (
                    (tag t1 (do_go_to_next_place ?from ?to))
                    (tag t2 (do_problem))
                    
                )
    :constraints
                ( and
                    ( before 
                        (next ?from ?to)

                    t1
                    )
                )


)


;----------------------------------------------------
;               Allez à la prochaine place
;----------------------------------------------------

; Monte une tente
; Transfert une tente à la prochaine place
; Fait marcher tous ceux qui doivent marcher
(:method do_go_to_next_place
    :parameters (?from ?to - place)
    :expansion (
                    (tag t1 (do_down_tent ?from))
                    (tag t2 (do_transfert ?from ?to))
                    (tag t3 (do_walk_all ?from ?to))
                    
                )
    :constraints
                ( and
                    ( before 
                        ( next ?from ?to)

                    t1
                    )
                )


)



;------------------------------------------------------------
;                        Demonte une tente 
;------------------------------------------------------------

; Cas où il y a une tente monté
(:method do_down_tent
    :parameters (?p - place)
    :expansion (
                    (tag t1 (nop))
                    
                )
    :constraints
                ( and
                    ( before ( and 
                             ( at_tent ?t ?p)
                             ( down ?t)
                             )
                    t1
                    )
                )


)

; Cas où il n'y a pas de tente monté
(:method do_down_tent
    :parameters (?p - place)
    :expansion (
                    (tag t1 (put_down ?person ?p ?t))
                    
                )
    :constraints
                ( and
                    ( before 
                            ( and 
                            ( at_tent ?t ?p)
                            ( up ?t)
                            ( at_person ?person ?p)
                            )
                    t1
                    )
                )


)


;------------------------------------------------------------
;                        Monte une tente 
;------------------------------------------------------------

(:method do_put_tent
    :parameters (?p - place)
    :expansion (
                    (tag t1 (put_up ?person ?p ?t))
                    
                )
    :constraints
                ( and
                    ( before 
                            ( and 
                            ( at_tent ?t ?p)
                            ( down ?t)
                            ( at_person ?person ?p)
                            )
                    t1
                    )
                )


)

;------------------------------------------------------------
;                        Transfert une tente
;------------------------------------------------------------

; Cas où il y a 2 voitures présentes
(:method do_transfert
    :parameters (?from ?to - place)
    :expansion (
                    (tag t1 (do_drive_tent ?from ?to ?c1))
                    (tag t2 (do_drive ?from ?to ?c2))
                    (tag t3 (do_put_tent ?to))
                    (tag t4 (do_drive_passenger ?to ?from ?c1))
                )
    :constraints
                ( and
                    ( before 
                        ( and 
                       
                        ( at_car ?c1 ?from)
                        ( at_car ?c2 ?from)
                        ( next ?from ?to)
                        )
                    t1
                    )
                )


)


; Cas où il y a 1 voiture présente
(:method do_transfert
    :parameters (?from ?to - place)
    :expansion (
                    (tag t1 (do_drive_tent_passenger ?from ?previous ?c1))
                    (tag t2 (do_drive_tent ?previous ?to ?c1))
                    (tag t3 (do_drive ?previous ?to ?c2))
                    (tag t4 (do_put_tent ?to))
                    (tag t5 (do_drive_passenger ?to ?from ?c1))
                )
    :constraints
                ( and
                    ( before 
                        ( and 
                        ( at_car ?c1 ?from)
                        ( at_car ?c2 ?previous)
                        ( next ?from ?to)
                        )
                    t1
                    )
                )


)



;------------------------------------------------------------
;                        Fait marcher tous le monde
;------------------------------------------------------------



(:method do_walk_all
    :parameters (?from ?to - place)
    :expansion (
                    (tag t1 (nop))
                )
    :constraints
                ( and
                    ( before 
                        ( next ?from ?to)
                    t1
                    )
                )


)



(:method do_walk_all
    :parameters (?from ?to - place)
    :expansion (
                    (tag t1 (walk_together ?t ?to ?person_1 ?from ?person_2 ?couple))
                    (tag t2 (do_walk_all ?from ?to))
                )
    :constraints
                ( and
                    ( before 
                        ( and 
                        ( at_tent ?t ?to)
                        ( up ?t)
                        ( at_person ?person_1 ?from)
                        ( next ?from ?to)
                        ( at_person ?person_2 ?from)
                        ( walked ?couple ?from)
                        ( partners ?couple ?person_1 ?person_2)
                        )
                    t1
                    )
                )


)


;------------------------------------------------------------
;                        Methode de Transfert
;------------------------------------------------------------

; 1 personne


(:method do_drive
    :parameters (?from ?to - place ?c - car)
    :expansion (
                    (tag t1 (drive ?person ?from ?to ?c ))
                )
    :constraints
                ( and
                    ( before 
                        ( and 
                        ( at_person ?person ?from)
                        ( at_car ?c ?from)
                        
                        )
                    t1
                    )
                )


)



; 1 personne + 1 tente

(:method do_drive_tent
    :parameters (?from ?to - place ?c - car)
    :expansion (
                    (tag t1 (drive_tent ?person ?from ?to ?c ?t))
                )
    :constraints
                ( and
                    ( before 
                        ( and 
                        ( at_person ?person ?from)
                        ( at_car ?c ?from)
                        ( at_tent ?t ?from)
                        ( down ?t)
                        
                        )
                    t1
                    )
                )
)
; 2 personnes

(:method do_drive_passenger
    :parameters (?from ?to - place ?c - car)
    :expansion (
                    (tag t1 (drive_passenger ?person_1 ?from ?to ?c ?person_2))
                )
    :constraints
                ( and
                    ( before 
                        ( and 
                        ( at_person ?person_1 ?from)
                        ( at_person ?person_2 ?from)
                        ( at_car ?c ?from)
                        
                        )
                    t1
                    )
                )


)
; 2 personnes + 1 tente

(:method do_drive_tent_passenger
    :parameters (?from ?to - place ?c - car)
    :expansion (
                     (tag t1 (drive_tent_passenger ?person_1 ?from ?to ?c ?t ?person_2))
                )
    :constraints
                ( and
                    ( before 
                        ( and 
                        ( at_person ?person_1 ?from)
                        ( at_person ?person_2 ?from)
                        ( at_car ?c ?from)
                        ( at_tent ?t ?from)
                        ( down ?t)
                        
                        )
                    t1
                    )
                )


)





)

