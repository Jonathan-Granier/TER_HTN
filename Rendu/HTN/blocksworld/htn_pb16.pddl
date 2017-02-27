(define (problem BLOCKS-9-0)
(:domain BLOCKS)
     (:requirements :strips :typing :negative-preconditions :htn :equality)

(:objects F I H E D B A C G - block)
(:INIT 
	(CLEAR C) 
    (CLEAR F) 
    (ONTABLE C) 
    (ONTABLE B) 
    (ON F G) 
    (ON G E) 
    (ON E A)
    (ON A I) 
    (ON I D) 
    (ON D H) 
    (ON H B) 
    (HANDEMPTY))
(:goal
     :tasks  (
            (tag t1 (do_put_on E H))
            (tag t2 (do_put_on F E))
            (tag t3 (do_put_on I F))
            (tag t4 (do_put_on A I))
            (tag t5 (do_put_on C A))
            (tag t6 (do_put_on B C))
            (tag t7 (do_put_on D B))
            (tag t8 (do_put_on G D))

        )

    :constraints(and
                    (after (and
                                (ON G D) 
                                (ON D B) 
                                (ON B C) 
                                (ON C A) 
                                (ON A I) 
                                (ON I F) 
                                (ON F E)
                                (ON E H)
                            ) t1)
                )    
))
