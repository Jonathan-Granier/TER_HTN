(define (problem BLOCKS-9-1)
(:domain BLOCKS)
     (:requirements :strips :typing :negative-preconditions :htn :equality)

(:objects F I H E D B A C G - block)
(:INIT 
	(CLEAR F) 
    (ONTABLE A) 
    (ON F E) 
    (ON E B) 
    (ON B D) 
    (ON D C) 
    (ON C I)
    (ON I G) 
    (ON G H) 
    (ON H A) 
    (HANDEMPTY))
(:goal
     :tasks  (
            (tag t1 (do_put_on E C))
            (tag t2 (do_put_on F E))
            (tag t3 (do_put_on G F))
            (tag t4 (do_put_on H G))
            (tag t5 (do_put_on B H))
            (tag t6 (do_put_on A B))
            (tag t7 (do_put_on I A))
            (tag t8 (do_put_on D I))

        )

    :constraints(and
                    (after (and
                                (ON D I) 
                                (ON I A) 
                                (ON A B) 
                                (ON B H) 
                                (ON H G) 
                                (ON G F) 
                                (ON F E)
                                (ON E C)
                            ) t1)
                )    
))
