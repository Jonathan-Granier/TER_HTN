(define (problem BLOCKS-9-2)
(:domain BLOCKS)
     (:requirements :strips :typing :negative-preconditions :htn :equality)

(:objects F I H E D B A C G - block)
(:INIT 
	(CLEAR H) 
    (CLEAR F) 
    (ONTABLE G) 
    (ONTABLE F) 
    (ON H A) 
    (ON A D) 
    (ON D E)
    (ON E C) 
    (ON C I) 
    (ON I B) 
    (ON B G) 
    (HANDEMPTY))
(:goal
     :tasks  (
            (tag t1 (do_put_on C A))
            (tag t2 (do_put_on B C))
            (tag t3 (do_put_on E B))
            (tag t4 (do_put_on I E))
            (tag t5 (do_put_on D I))
            (tag t6 (do_put_on H D))
            (tag t7 (do_put_on G H))
            (tag t8 (do_put_on F G))

        )

    :constraints(and
                    (after (and
                                (ON F G) 
                                (ON G H) 
                                (ON H D) 
                                (ON D I) 
                                (ON I E) 
                                (ON E B) 
                                (ON B C)
                                (ON C A)
                            ) t1)
                )    
))
