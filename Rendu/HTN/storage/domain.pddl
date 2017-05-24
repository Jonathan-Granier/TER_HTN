; IPC5 Domain: Storage Propositional
; Authors: Alfonso Gerevini and Alessandro Saetti 

(define (domain Storage-Propositional)
(:requirements :strips :typing :negative-preconditions :htn :equality)
(:types hoist surface place - object
	;area - (either object surface)
	crate area - surface
	container depot - place
	storearea transitarea - area
	)
(:predicates (clear ?s - storearea)
	     (in ?x - (either storearea crate) ?p - place)
	     (available ?h - hoist) 
	     (lifting ?h - hoist ?c - crate) 
	     (at ?h - hoist ?a - area)
	     (on ?c - crate ?s - storearea) 
	     (connected ?a1 ?a2 - area)

	     ; Ajouté :
	     (visited ?a - area)
         ; Pas utilisé ?
         (compatible ?c1 ?c2 - crate)

         ) 

(:action lift
 :parameters (?h - hoist ?c - crate ?a1 - storearea ?a2 - area ?p - place)
 :precondition (and 
 				(connected ?a1 ?a2) 
 				(at ?h ?a2) 
 				(available ?h) 
		   		(on ?c ?a1) 
		   		(in ?a1 ?p))
 :effect (and 
 			(not (on ?c ?a1)) 
 			(clear ?a1)
	      	(not (available ?h)) 
	      	(lifting ?h ?c) 
	      	(not (in ?c ?p))))
				
(:action drop
 :parameters (?h - hoist ?c - crate ?a1 - storearea ?a2 - area ?p - place)
 :precondition (and 
	 			(connected ?a1 ?a2) 
	 			(at ?h ?a2) 
	 			(lifting ?h ?c) 
			    (clear ?a1) 
			    (in ?a1 ?p))
 :effect (and 
 			(not (lifting ?h ?c)) 
 			(available ?h)
	      	(not (clear ?a1)) 
	      	(on ?c ?a1) 
	      	(in ?c ?p)))

(:action move
 :parameters (?h - hoist ?from ?to - storearea)
 :precondition (and 
 				(at ?h ?from) 
 				(clear ?to) 
 				(connected ?from ?to))
 :effect (and 
 			(not (at ?h ?from)) 
 			(at ?h ?to) 
 			(not (clear ?to)) 
 			(clear ?from)))

(:action go-out
 :parameters (?h - hoist ?from - storearea ?to - transitarea)
 :precondition (and 
 				(at ?h ?from) 
 				(connected ?from ?to))
 :effect (and 	
 			(not (at ?h ?from)) 
 			(at ?h ?to) 
 			(clear ?from)))

(:action go-in
 :parameters (?h - hoist ?from - transitarea ?to - storearea)
 :precondition (and 
 				(at ?h ?from) 
 				(connected ?from ?to) 
 				(clear ?to))
 :effect (and 
 			(not (at ?h ?from)) 
 			(at ?h ?to) 
 			(not (clear ?to))))

  
;------------------------------------------------------------------
;                          Action rajouté
;------------------------------------------------------------------

(:action visit
     :parameters (?a - area)
     :precondition (and )
     :effect (and (visited ?a))
)  

(:action unvisit
     :parameters (?a - area)
     :precondition (and (visited ?a))
     :effect (and (not(visited ?a)))
)



(:action nop
  :parameters   ()
  :precondition (and)
  :effect       (and)
  )


;----------------------------------------------------------------
; 					Résoud le problem
;----------------------------------------------------------------

; Algo
; Tant qu'il y a des caisses dans les containers, le transporter au dépot



(:method do_problem 

        :parameters ()
        :expansion  (
                        (tag t1 (do_have_container ?crate ?container))  
                    )
        :constraints( 
                    and 
                        (before ( and 
                                     ( not (in ?crate ?container))
                                     ( on ?crate ?s)
                                ) 
                        t1
                        )

                    )
    )

(:method do_problem 

        :parameters ()
        :expansion  (
                        (tag t1 (do_put_on ?c ?depot))
                        ;(tag t2 (do_problem))  
                    )
        :constraints( 
                    and 
                        (before ( and 
                                    ( not ( in ?c ?depot))
                                    ( on ?c ?s ) 
                                ) 
                        t1
                        )
                    )
    )



;----------------------------------------------------------------
;             Place une caisse sur un dépot     
;----------------------------------------------------------------

; Cas où la caisse est déjà bien placé
(:method do_put_on 

        :parameters (?c - crate ?to - depot)
        :expansion  (
                        (tag t1 (nop))  
                    )
        :constraints( 
                    and 
                        (before ( and 
                               		( in ?c ?to)
                                ) 
                        t1
                        )
                    )
    )

; Cas ou la caisse est ailleurs
(:method do_put_on 

    :parameters (?c - crate ?to - depot)
    :expansion  (
    				
    				;(tag t1 (nop))
    				(tag t1 (do_move_hoist_to_container ?c ?from ?h))
    				(tag t2 (do_move_hoist_to_depot ?c ?to ?h)) 
                )
    :constraints( 
                and 
                    (before ( and 
                           	( not ( in ?c ?to))
                           	( in ?c ?from)
                            ( available ?h) 
                            ) 
                    t1
                    )
                )
    )




;----------------------------------------------------------------
;             		Bouge une grue vers un container    
;----------------------------------------------------------------


; Cas où la grue n'est pas adjacente à la zone de la caisse et ne peux pas allez sur une zone de transition
(:method do_move_hoist_to_container 

    :parameters (?c - crate ?container - container ?h - hoist)
    :expansion  (
                    
                  ;(tag t1 (nop))
                  (tag t1 (move ?h ?from ?to))
                  (tag t2 (visit ?to))
                  (tag t3 (do_move_hoist_to_container ?c ?container ?h))
                  (tag t4 (unvisit ?to))
                   
                )
    :constraints( 
                and 
                    (before ( and 
                            ( on ?c ?dest) 
                            ( not ( connected ?from ?dest)) 
                            ( available ?h) 
                            ( at ?h ?from) 
                            ( connected ?from ?to) 
                            ( clear ?to)
                            ) 
                    t1
                    )
                )
    )




; Cas où la grue est adjacente à la zone de la caisse : attrape la caisse
(:method do_move_hoist_to_container

    :parameters (?c - crate ?container - container ?h - hoist)
    :expansion  (
    				;(tag t1 (nop))
    				(tag t1(lift ?h ?c ?to ?from ?container))
                    
                   
                )
    :constraints( 
                and 
                    (before ( and 
                           	( connected ?from ?to) 
							( at ?h ?from) 
							( available ?h) 
	   						( on ?c ?to) 
	   						( in ?to ?container)
                            )  
                    t1
                    )
                )
    )



; Cas où la grue n'est pas adjacente à la zone de la caisse mais adjacente à une zone de transition
(:method do_move_hoist_to_container 

    :parameters (?c - crate ?container - container ?h - hoist)
    :expansion  (
    				
                   ;(tag t1 (nop))
                   (tag t1 (go-out ?h ?from ?to))
                   (tag t2 (visit ?to))
                   (tag t3 (do_move_hoist_to_container ?c ?container ?h))
                   (tag t4 (unvisit ?to))
                   
                )
    :constraints( 
                and 
                    (before ( and 
                    		( on ?c ?dest) 
                    		( not ( connected ?from ?dest)) 
                    		( available ?h) 
                           	( at ?h ?from) 
					        ( connected ?from ?to)
                            ) 
                    t1
                    )
                )
    )

; Cas où la grue n'est pas adjacente à la zone de la caisse mais est sur une zone de transition
(:method do_move_hoist_to_container 

    :parameters (?c - crate ?container - container ?h - hoist)
    :expansion  (
                   ;(tag t1 (nop))
                   (tag t1 (go-in ?h ?from ?to))
                   (tag t2 (visit ?to))
                   (tag t3 (do_move_hoist_to_container ?c ?container ?h))
                   (tag t4 (unvisit ?to))
                )
    :constraints( 
                and 
                    (before ( and 
                            ( on ?c ?dest) 
                    		( not ( connected ?from ?dest)) 
                    		( available ?h) 
                           	( at ?h ?from) 
								( connected ?from ?to) 
								( clear ?to)
                            ) 
                    t1
                    )
                )
    )













;----------------------------------------------------------------
;             		Bouge une grue vers une dépot   
;----------------------------------------------------------------







; Cas où la grue n'est pas adjacente à une zone de dépot et ne peux pas allez sur une zone de transition





; Cas où la grue est adjacente à une zone de dépot acceptable
(:method do_move_hoist_to_depot 

    :parameters (?c - crate ?depot - depot ?h - hoist)
    :expansion  (
    				
                  ;(tag t1(nop))
                  (tag t1 ( drop ?h ?c ?to ?from ?depot ))
                   
                )
    :constraints( 
                and 
                    (before ( and 
                         		( connected ?to ?from) 
                                ( not ( connected ?to ?away))
								( at ?h ?from) 
								( lifting ?h ?c) 
						    	( clear ?to) 
						    	( in ?to ?depot)
                            ) 
                    t1
                    )
                )
    )

(:method do_move_hoist_to_depot 

    :parameters (?c - crate ?depot - depot ?h - hoist)
    :expansion  (
                    
                  ;(tag t1(nop))
                  (tag t1 ( drop ?h ?c ?to ?from ?depot ))
                   
                )
    :constraints( 
                and 
                    (before ( and 
                                ( connected ?to ?from) 
                                ( connected ?to ?away)
                                ( not ( clear ?away))
                                ( at ?h ?from) 
                                ( lifting ?h ?c) 
                                ( clear ?to) 
                                ( in ?to ?depot)
                            ) 
                    t1
                    )
                )
    )

; Cas où la grue n'est pas adjacente à une zone de dépot mais adjacente à une zone de transition
(:method do_move_hoist_to_depot 

    :parameters (?c - crate ?depot - depot ?h - hoist)
    :expansion  (
    				
                   (tag t1 (go-out ?h ?from ?to))
                   (tag t2 (visit ?to))
                   (tag t3 (do_move_hoist_to_depot ?c ?depot ?h))
                   (tag t4 (unvisit ?to))
                )
    :constraints( 
                and 
                    (before ( and 
                    		( in ?dest ?depot) 
                    		( not ( connected ?from ?dest)) 
                    		( lifting ?h ?c) 
                           	( at ?h ?from) 
								( connected ?from ?to)
                            ) 
                    t1
                    )
                )
    )

; Cas où la grue n'est pas adjacente à une zone de dépot mais est sur une zone de transition
(:method do_move_hoist_to_depot 

    :parameters (?c - crate ?container - container ?h - hoist)
    :expansion  (
    				
                   (tag t1 (go-in ?h ?from ?to))
                   (tag t2 (visit ?to))
                   (tag t3 (do_move_hoist_to_depot ?c ?container ?h))
                   (tag t4 (unvisit ?to))
                )
    :constraints( 
                and 
                    (before ( and 
                            ( in ?dest ?depot) 
                    		( not ( connected ?from ?dest)) 
                    		( lifting ?h ?c) 
                           	( at ?h ?from) 
								( connected ?from ?to) 
								( clear ?to)
                            ) 
                    t1
                    )
                )
    )



(:method do_move_hoist_to_depot 

    :parameters (?c - crate ?container - container ?h - hoist)
    :expansion  (
                    
                  (tag t1 (move ?h ?from ?to))
                  (tag t2 (visit ?to))
                  (tag t3 (do_move_hoist_to_depot ?c ?container ?h))
                  (tag t4 (unvisit ?to))
                )
    :constraints( 
                and 
                    (before ( and 
                            ( in ?dest ?depot) 
                            ;( not ( connected ?from ?dest)) 
                            ( available ?h) 
                            ( at ?h ?from) 
                                ( connected ?from ?to) 
                                ( clear ?to)
                            ) 
                    t1
                    )
                )
)   







;----------------------------------------------------------------
;                   Method pour forcer le typage   
;----------------------------------------------------------------



(:method do_have_container

    :parameters (?crate - crate ?container - container)
    :expansion  (
                    
                   (tag t1 (nop))
                )
    :constraints( 
                and 
                    
                   
                )
    )




)
