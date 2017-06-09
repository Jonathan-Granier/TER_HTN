(define (problem BLOCKS-10-0)
(:domain BLOCKS)
     (:requirements :strips :typing :negative-preconditions :htn :equality)

(:objects F I J H E D B A C G - block)
(:INIT 
	(CLEAR C) 
    (CLEAR F) 
    (ONTABLE B) 
    (ONTABLE H) 
    (ON C G) 
    (ON G E) 
    (ON E I)
    (ON I J) 
    (ON J A) 
    (ON A B) 
    (ON F D) 
    (ON D H) 
    (HANDEMPTY))
(:goal
     :tasks  (
            (tag t1 (do_put_on G J))
            (tag t2 (do_put_on H G))
            (tag t3 (do_put_on E H))
            (tag t4 (do_put_on A E))
            (tag t5 (do_put_on I A))
            (tag t6 (do_put_on F I))
            (tag t7 (do_put_on D F))
            (tag t8 (do_put_on B D))
            (tag t9 (do_put_on C B))

        )

    :constraints(and
                    (after (and
                                (ON C B) 
                                (ON B D) 
                                (ON D F) 
                                (ON F I) 
                                (ON I A) 
                                (ON A E) 
                                (ON E H)
                                (ON H G) 
                                (ON G J)
                            ) t1)
                )    
))