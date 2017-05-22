;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;           Ne marche pas
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


(define (domain mystery-typed)
   (:requirements :strips :typing :negative-preconditions :htn :equality)
   (:types province planet food emotion - object
           pleasure pain - emotion)
   (:predicates
       (eats ?n1 ?n2 - food)
       (craves ?v - emotion ?n - food)
       (fears ?c - pain ?v - pleasure)
       (locale ?n - food ?a - province)
       (harmony ?v - emotion ?s - planet)
       (attacks ?i ?j - province)
       (orbits ?i ?j - planet)
       (visited ?n - food)

       )

    ;type:
    ;   province = escence 
    ;   planete = Quantité transportable par un camion
    ;   emotion = localisation
    ;   food    = Pont en bois
    ;   pain    = Caisse
    ;   pleasure = Camion    


    ;Predicates
    ;   (attack =  niveau d'escence)
    ;   (orbite = nb de caisse dans le camion)
    ;   (local = Niveau d'usure)
    ;   (harmony = Quantité de caisse dans le camion)
    ;   (craves = locat_at)
    ;   (fears = est dans un camion)
    ;   (eats,attacks, orbits  fixe )
    

   ;; Met une caisse sur un camion
   (:action overcome
       :parameters (?c - pain ?v - pleasure ?n - food ?s1 ?s2 - planet)
       :precondition (and (craves ?c ?n)
                          (craves ?v ?n)
                          (harmony ?v ?s2)
                          (orbits ?s1 ?s2)
                          )
       :effect (and (not (craves ?c ?n))
                    (fears ?c ?v)
                    (not (harmony ?v ?s2))
                    (harmony ?v ?s1)))
  

    ;; Passe le camion de pont en pont
   (:action feast
       :parameters (?v - pleasure ?n1 ?n2 - food ?l1 ?l2 - province)
       :precondition (and (craves ?v ?n1)
                          (eats ?n1 ?n2)
                          (locale ?n1 ?l2)
                          (attacks ?l1 ?l2))
       :effect (and (not (craves ?v ?n1))
                    (craves ?v ?n2)
                    (not (locale ?n1 ?l2))
                    (locale ?n1 ?l1)))

   ;; Pose la caisse sur un pont.
   (:action succumb
       :parameters (?c - pain ?v - pleasure ?n - food ?s1 ?s2 - planet)
       :precondition (and (fears ?c ?v)
                          (craves ?v ?n)
                          (harmony ?v ?s1)
                          (orbits ?s1 ?s2))
       :effect (and (not (fears ?c ?v))
                    (craves ?c ?n)
                    (not (harmony ?v ?s1))
                    (harmony ?v ?s2)))



;------------------------------------------------------------------
;                          Action rajouté
;------------------------------------------------------------------

(:action visit
     :parameters (?n - food)
     :precondition (and )
     :effect (and (visited ?n))
)  

(:action unvisit
     :parameters (?n - food)
     :precondition (and (visited ?n))
     :effect (and (not(visited ?n)))
)



(:action nop
  :parameters   ()
  :precondition (and)
  :effect       (and)
  )

;------------------------------------------------------------------
;                               Methods
;------------------------------------------------------------------
;                          Faire une livraison
;------------------------------------------------------------------
;Algo :
; Recupere un camion
; Charger la caisse
; Aller à destination
; Decharger la caisse


; Cas où la caisse est déjà a destination
(:method do_delivery
  


       :parameters (?c -  pain ?n - food)
        :expansion  (

                        
                        (tag t1 (nop))
                    )
        :constraints( 
                    and 
                        (before ( and 
                                ( craves ?c ?n)
                                ) 
                        t1
                        )
                    )
    )

; Cas où la caisse n'est pas à destination
(:method do_delivery

       :parameters (?c -  pain ?n - food)
        :expansion  (

                        ;(tag t1 (nop))
                        (tag t1 (do_transfert ?v ?n1))
                        (tag t2 (overcome ?c ?v ?n1 ?s1 ?s2))
                        (tag t3 (do_transfert ?v ?n ))
                        (tag t4 (succumb ?c ?v ?n ?s1 ?s2))
                    )
        :constraints( 
                    and 
                        (before ( and 
                                ( craves ?c ?n1)
                                ( orbits ?s1 ?s2)
                                ) 
                        t1
                        )
                    )
    )



;------------------------------------------------------------------
;                          Bouge un camion
;------------------------------------------------------------------

; Bouge un camion de from vers to , 2 positions adjacentes
(:method do_move

        :parameters (?v - pleasure ?from - food ?to - food)
        :expansion  (

                        
                        (tag t1 (feast ?v ?from ?to ?l1 ?l2))
                    )
        :constraints( 
                    and 
                        (before ( and 
                                ( craves ?v ?from)
                                ( eats ?from ?to )
                                ( attacks ?l1 ?l2)
                                ( locale ?from ?l2)
                                ) 
                        t1
                        )
                    )
    )


;------------------------------------------------------------------
;                         Transfert un camion
;------------------------------------------------------------------






; Cas où le camion est déjà à destination
(:method do_transfert 
    :parameters (?v - pleasure ?to - food)
    :expansion  (    
                    (tag t1 (nop))
                )
    :constraints( 
                    and 
                        (before ( and 
                                ( craves ?v ?to)
                                ) 
                        t1
                        )
                    )
    )

; Lance la recursivité
(:method do_transfert 
    :parameters (?v - pleasure ?to - food)
    :expansion  (    
                    (tag t1 (visit ?from))
                    (tag t2 (do_transfert ?v ?from ?to))
                    (tag t3 (unvisit ?from))
                )
    :constraints( 
                    and 
                        (before ( and 
                                ( craves ?v ?from)
                                ) 
                        t1
                        )
                    )
    )


; Cas où le camion est déjà à destination
(:method do_transfert 
    :parameters (?v - pleasure ?from ?to - food)
    :expansion  (    
                    (tag t1 (nop))
                )
    :constraints( 
                    and 
                        (before ( and 
                                ( craves ?v ?to)
                                ) 
                        t1
                        )
                    )
    )



;Cas où le camion est à un seul pas
(:method do_transfert 
    :parameters (?v - pleasure ?from ?to - food)
    :expansion  (    
                     (tag t1 (do_move ?v ?from ?to))
                )
    :constraints( 
                    and 
                        (before ( and 
                                ( craves ?v ?from)
                                ( eats ?from ?to)
                                ) 
                        t1
                        )
                    )
    )


;Cas où le camion est à 2 pas ou plus
(:method do_transfert 
    :parameters (?v - pleasure ?from ?to - food)
    :expansion  (    
                    ;(tag t1 (nop))
                    
                    (tag t1 (do_move ?v ?from ?mid ))
                    (tag t2 (visit ?mid))
                    (tag t3 (do_transfert ?v ?mid ?to))
                    (tag t4 (unvisit ?mid))
                )
    :constraints( 
                    and 
                        (before ( and 
                                ( craves ?v ?from)
                                ( not ( visited ?mid))
                                ( eats ?from ?mid )
                                ( not ( eats ?from ?to))
                                ) 
                        t1
                        )
                    )
    )

)


  