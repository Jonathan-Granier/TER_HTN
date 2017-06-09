(define (problem BLOCKS-8-1)
(:domain BLOCKS)
     (:requirements :strips :typing :negative-preconditions :htn :equality)

(:objects F H E D B A C G - block)
(:INIT 
	(CLEAR E) 
    (CLEAR H) 
    (CLEAR D) 
    (CLEAR F) 
    (ONTABLE C) 
    (ONTABLE G)
    (ONTABLE D) 
    (ONTABLE F) 
    (ON E C) 
    (ON H A) 
    (ON A B) 
    (ON B G) 
    (HANDEMPTY))
(:goal
     :tasks  (
            (tag t1 (do_put_on A E))
            (tag t2 (do_put_on H A))
            (tag t3 (do_put_on F H))
            (tag t4 (do_put_on G F))
            (tag t5 (do_put_on B G))
            (tag t6 (do_put_on D B))
            (tag t7 (do_put_on C D))

        )

    :constraints(and
                    (after (and
                                (ON C D) 
                                (ON D B) 
                                (ON B G) 
                                (ON G F) 
                                (ON F H) 
                                (ON H A) 
                                (ON A E)
                            ) t1)
                )    
))
