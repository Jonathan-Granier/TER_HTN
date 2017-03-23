;  (c) 2001 Copyright (c) University of Huddersfield
;  Automatically produced from GIPO from the domain hiking
;  All rights reserved. Use of this software is permitted for non-commercial
;  research purposes, and it may be copied only for that use.  All copies must
;  include this copyright message.  This software is made available AS IS, and
;  neither the GIPO team nor the University of Huddersfield make any warranty about
;  the software or its performance.

(define (domain hiking)
  (:requirements :strips :typing :negative-preconditions :htn :equality)
  (:types place bool)
  (:predicates 
              (next ?x1 - place ?x2 - place)
              (up ?b - bool)
              (down ?b - bool)
)


  ;  Démonter la tente
  (:action do_next
         :parameters (?p1 - place ?b - bool  ?p2 - place)
         :precondition  (and 
                        (next ?p1 ?p2)
                        )
         :effect    (and 
         			(up ?b )
                	(not (down ?b)))
)


(:action nop
         :parameters ()
         :precondition (and )
         :effect (and )
) 
  

  (:method do_Test
        :parameters ()
        :expansion (
                        ;(tag t1 (nop))
                        ;(tag t1 (do_walk_couple ?p1 ?p2))
                        ;(tag t1 (walk_together ?pl1 ))
                        (tag t1 (do_next ?p2 ?b ?p1))
                    )
        :constraints
                    ( and
                        (before 
                           
                            (down ?b)

                        t1
                        )
                    )


)
)

