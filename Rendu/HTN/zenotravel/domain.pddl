(define (domain zeno-travel)
    (:requirements :strips :typing :negative-preconditions :htn :equality)
(:types locatable city flevel - object
        person aircraft - locatable
    )
(:predicates (at ?x - locatable ?c - city)
             (in ?p - person ?a - aircraft)
	     (fuel-level ?a - aircraft ?l - flevel)
	     (next ?l1 ?l2 - flevel))


(:action board
 :parameters (?p - person ?a - aircraft ?c - city)
 
 :precondition (and (at ?p ?c)
                 (at ?a ?c))
 :effect (and (not (at ?p ?c))
              (in ?p ?a)))

(:action debark
 :parameters (?p - person ?a - aircraft ?c - city)

 :precondition (and (in ?p ?a)
                 (at ?a ?c))
 :effect (and (not (in ?p ?a))
              (at ?p ?c)))

(:action fly 
 :parameters (?a - aircraft ?c1 ?c2 - city ?l1 ?l2 - flevel)
 
 :precondition (and (at ?a ?c1)
                 (fuel-level ?a ?l1)
		 (next ?l2 ?l1))
 :effect (and (not (at ?a ?c1))
              (at ?a ?c2)
              (not (fuel-level ?a ?l1))
              (fuel-level ?a ?l2)))
                                  
(:action zoom
 :parameters (?a - aircraft ?c1 ?c2 - city ?l1 ?l2 ?l3 - flevel)

 :precondition (and (at ?a ?c1)
                 (fuel-level ?a ?l1)
		 (next ?l2 ?l1)
		 (next ?l3 ?l2)
		)
 :effect (and (not (at ?a ?c1))
              (at ?a ?c2)
              (not (fuel-level ?a ?l1))
              (fuel-level ?a ?l3)
	)
) 

(:action refuel
 :parameters (?a - aircraft ?c - city ?l - flevel ?l1 - flevel)

 :precondition (and (fuel-level ?a ?l)
                 (next ?l ?l1)
                 (at ?a ?c))
 :effect (and (fuel-level ?a ?l1) (not (fuel-level ?a ?l))))

(:action nop
        :parameters ()
        :precondition (and )
        :effect (and )
)



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;  Transfert un passager dans une cité    ;;;    
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;



;; Le passager est déjà dans la cité en question
(:method do_carry_person

       :parameters ( ?p - person  ?to - city)
        :expansion  (

                        
                        (tag t1 (nop))
                    )
        :constraints( 
                    and 
                        (before ( and 
                                    ( at ?p ?to)
                                ) 
                        t1
                        )
                    )
    )


;; Le passager est dans un avion
(:method do_carry_person 

       :parameters (?p - person  ?to - city )
        :expansion  (

                        (tag t1 (do_fly ?a ?to))
                        (tag t2 (do_debark ?a ?p ?to))
                    )
        :constraints( 
                    and 
                        (before ( and 
                                
                                    ( in ?p ?a)
                                ) 
                        t1
                        )
                    )
    )


(:method do_carry_person 

       :parameters (?p - person  ?to - city )
        :expansion  (

                        ;(tag t1 (nop))
                        (tag t1 (do_fly ?a ?from))
                        (tag t2 (do_board ?a ?p ?from))
                        (tag t3 (do_fly ?a ?to))
                        (tag t4 (do_debark ?a ?p ?to))


                    )
        :constraints( 
                    and 
                        (before ( and 
                                ( not ( at ?p ?to))
                                ( at ?p ?from)
                                ( not ( in ?p ?a))
                                ) 
                        t1
                        )
                    )
    )



;; Le passager est ni à destination , ni dans un avion
(:method do_carry_person 

       :parameters (?p - person  ?to - city )
        :expansion  (


                        (tag t1 (do_board ?a ?p ?from))
                        (tag t2 (do_fly ?a ?to))
                        (tag t3 (do_debark ?a ?p ?to))


                    )
        :constraints( 
                    and 
                        (before ( and 
                                ( not ( at ?p ?to))
                                ( at ?p ?from)
                                ( at ?a ?from)
                                ( not ( in ?p ?a))
                                ) 
                        t1
                        )
                    )
    )



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Fait voler un avion d'une cité à l'autre ;;;    
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;







(:method do_fly

       :parameters (?a - aircraft ?to - city )
        :expansion  (

                        ;(tag t1 (nop))
                        (tag t1 (do_check_city ?a ?to))
                    )
        :constraints( 
                    and 
                        (before ( and 
                                    ( at ?a ?to)
                                ) 
                        t1
                        )
                    )
    )




;; Cas ou il n'y a plus d'essence
(:method do_fly

       :parameters (?a - aircraft ?to - city )
        :expansion  (

                        
                        ;(tag t1 (nop))
                        (tag t1 (do_check_city ?a ?from))
                        (tag t2 (refuel ?a ?from ?l1 ?l2))
                        (tag t3 (fly ?a ?from ?to ?l2 ?l1))
                    )
        :constraints( 
                    and 
                        (before ( and 
                                    ( not ( at ?a ?to))
                                    ( at ?a ?from)
                                    ( fuel-level ?a ?l1)
                                    ( next ?l1 ?l2)
                                    ( not ( next ?l3 ?l1)) 
                                    
                                ) 
                        t1
                        )
                    )
    )


;; Cas ou il y a assez d'essence dans l'avion
(:method do_fly

       :parameters (?a - aircraft ?to - city )
        :expansion  (

                        (tag t1 (do_check_city ?a ?from))
                      
                        (tag t2 (fly ?a ?from ?to ?l1 ?l2))
                    )
        :constraints( 
                    and 
                        (before ( and 
                                    ( not ( at ?a ?to))
                                    ( at ?a ?from)
                                    ( fuel-level ?a ?l1)
                                    ;( next ?l3 ?l1)
                                   
                                ) 
                        t1
                        )
                    )
    )


    ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
    ;;;     Prend un passager dans une cité      ;;;    
    ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(:method do_board

       :parameters (?a - aircraft  ?p - person ?c - city)
        :expansion  (

                        
                        (tag t1 (board ?p ?a ?c))
                        (tag t2 (do_check_city ?a ?c))
                    )
        :constraints( 
                    and 
                        (before ( and 
                                    ( at ?p ?c)
                                    ( at ?a ?c)
                                    ( not ( in ?p ?a))
                                ) 
                        t1
                        )
                    )
    )

    ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
    ;;;     Dépose un passager dans une cité     ;;;    
    ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(:method do_debark

       :parameters (?a - aircraft  ?p - person ?c - city)
        :expansion  (

                        
                        (tag t1 (debark ?p ?a ?c))
                        (tag t2 (do_check_city ?a ?c))
                    )
        :constraints( 
                    and 
                        (before ( and 
                                    ( at ?a ?c)
                                    ( in ?p ?a)
                                ) 
                        t1
                        )
                    )
    )

    ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
    ;;;     Verifie s'il y a des gens à poser ou prendre    ;;;    
    ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;





;; Depose une personne
(:method do_check_city

       :parameters (?a - aircraft ?c - city)
        :expansion  (

                        
                        (tag t1 (debark ?p ?a ?c))
                       ;(tag t2 (do_check_city ?a ?c))
                    )
        :constraints( 
                    and 
                        (before ( and 
                                    ( at ?a ?c)
                                    ( in ?p ?a)
                                ) 
                        t1
                        )
                    )
    )


;; Prend une personne
(:method do_check_city

       :parameters (?a - aircraft ?c - city)
        :expansion  (

                        
                        (tag t1 (board ?p ?a ?c))
                        (tag t2 (do_check_city ?a ?c))
                    )
        :constraints( 
                    and 
                        (before ( and 
                                    ( at ?a ?c)
                                    ( not ( in ?p ?a))
                                ) 
                        t1
                        )
                    )
    )


(:method do_check_city

       :parameters (?a - aircraft ?c - city)
        :expansion  (

                        
                        (tag t1 (nop))
                    )
        :constraints( 
                    and 
                        (before ( and 
                                    ( at ?a ?c)
                                    ( not ( at ?p ?c))
                                ) 
                        t1
                        )
                    )
    )

 

)







   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
    ;;;     Verifie s'il faut remettre de l'escence         ;;;    
    ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(:method do_refuel

       :parameters (?a - aircraft ?c - city ?l1 - flevel ?l2 - flevel)
        :expansion  (

                        
                        (tag t1 (nop))
                    )
        :constraints( 
                    and 
                        (before ( and 
                                    ( at ?a ?c)
                                ) 
                        t1
                        )
                    )
    )

(:method do_refuel

       :parameters (?a - aircraft ?c - city ?l1 - flevel ?l2 - flevel)
        :expansion  (

                        
                         (tag t1 (refuel ?a ?from ?l1 ?l2))
                    )
        :constraints( 
                    and 
                        (before ( and 
                                    ( at ?a ?c)
                                ) 
                        t1
                        )
                    )
    )
