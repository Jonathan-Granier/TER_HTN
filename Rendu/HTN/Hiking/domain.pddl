;  (c) 2001 Copyright (c) University of Huddersfield
;  Automatically produced from GIPO from the domain hiking
;  All rights reserved. Use of this software is permitted for non-commercial
;  research purposes, and it may be copied only for that use.  All copies must
;  include this copyright message.  This software is made available AS IS, and
;  neither the GIPO team nor the University of Huddersfield make any warranty about
;  the software or its performance.

(define (domain hiking)
  (:requirements :strips :equality :typing)
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


  ;  Démonter la tente
  (:action put_down
         :parameters ( ?x1 - person ?x2 - place ?x3 - tent)
         :precondition  (and 
                        (at_person ?x1 ?x2)
                        (at_tent ?x3 ?x2)
                        (up ?x3))
         :effect    (and 
                    (down ?x3)
                    (not (up ?x3))
)
)
  ;; Monter la tente
  (:action put_up
         :parameters ( ?x1 - person ?x2 - place ?x3 - tent)
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
         :parameters ( ?x1 - person ?x2 - place ?x3 - place ?x4 - car ?x5 - person)
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
         :parameters ( ?x1 - person ?x2 - place ?x3 - place ?x4 - car)
         :precondition  (and 
                        (at_person ?x1 ?x2)(at_car ?x4 ?x2))
         :effect    (and 
                    (at_person ?x1 ?x3)
                    (not (at_person ?x1 ?x2))
                    (at_car ?x4 ?x3)
                    (not (at_car ?x4 ?x2))
)
)
  ;; Transporte 1 personnes et 1 tente d'une place à l'autre
  (:action drive_tent
         :parameters ( ?x1 - person ?x2 - place ?x3 - place ?x4 - car ?x5 - tent)
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
         :parameters ( ?x1 - person ?x2 - place ?x3 - place ?x4 - car ?x5 - tent ?x6 - person)
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
         :parameters ( ?x1 - tent ?x2 - place ?x3 - person ?x4 - place ?x5 - person ?x6 - couple)
         :precondition  (and 
                        (at_tent ?x1 ?x2)
                        (up ?x1)
                        (at_person ?x3 ?x4)
                        (next ?x4 ?x2)
                        (at_person ?x5 ?x4)
                        (not (= ?x3 ?x5))
                        (walked ?x6 ?x4)
                        (partners ?x6 ?x3 ?x5))
         :effect    (and 
                    (at_person ?x3 ?x2)
                    (not (at_person ?x3 ?x4))
                    (at_person ?x5 ?x2)
                    (not (at_person ?x5 ?x4))
                    (walked ?x6 ?x2)
                    (not (walked ?x6 ?x4))
)
)

; Algo :
;   Init :
;   (Une ligne de tente est un ensemble de place , toutes adjacentes qui ont une tente chacune)
;   Pour chaque tente down
;       Les mettres dans une autre place (on fait une ligne de tente)
;   Laisser une voiture dans un endroit sans tente (juste après la ligne de tente)
;   
;   Faire marcher tout les couples jusqu'a qu'il n'y est plus de tente.
;   Construire les tentes à chaque arrivé
;   
;   Milieu :
;   Sur la 1er place sans tente doit se trouver une voiture
;   Monter à 2 dans une voiture
;   Allez là ou il y a une tente + une voiture
;   Prendre voiture + tente
;   Avec une voiture (1 personne):
;       Allez a la dernier place de la nouvelle ligne de tente qu'on veut creer
;   L'autre voiture :
;       tant qu'il y a encore des tentes pas monter 
;           Aller là ou il y a une tente monté
;           La prendre
;           Allez là ou il y a pas de tente 
;           Poser la tente(Ne pas la monter)
;       
;       Rejoindre la 1er voiture
;       Retourner la où sont les autres avec une voiture.
;   
;   
;   Faire marcher tout les couples jusqu'a qu'il n'y est plus de tente.
;   Construire les tentes à chaque arrivé  
;
;    Fin (il reste plus de tente que de place a parcourir) :
;    Monter à 1 dans une voiture     
;    Tant qu'il le faut 
;       Aller là ou il y a une tente monté
;       La prendre
;       Allez là ou il y a pas de tente 
;       Poser la tente(Ne pas la monter) 
;   
;   Revenir au campement
;   
;   Faire marcher tout les couples jusqu'au bout
;   Construire les tentes à chaque arrivé  
;
;
;













  (:method do_
        :parameters ()
        :expansion (
                        (tag t1 ())
                    )
        :constraints
                    ( and
                        (before 
                            ()
                        t1
                        )
                    )


    )














)

