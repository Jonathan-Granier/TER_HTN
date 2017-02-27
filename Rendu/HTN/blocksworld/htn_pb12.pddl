(define (problem BLOCKS-7-2)
(:domain BLOCKS)
     (:requirements :strips :typing :negative-preconditions :htn :equality)

(:objects F E D B A C G - block)
(:INIT 
	(CLEAR B) 
    (CLEAR A) 
    (ONTABLE F) 
    (ONTABLE D) 
    (ON B C) 
    (ON C G) 
    (ON G E)
    (ON E F) 
    (ON A D) 
    (HANDEMPTY))
(:goal
     :tasks  (
            (tag t1 (do_put_on C G))
            (tag t2 (do_put_on A C))
            (tag t3 (do_put_on D A))
            (tag t4 (do_put_on F D))
            (tag t5 (do_put_on B F))
            (tag t6 (do_put_on E B))

        )

    :constraints(and
                    (after (and
                                (ON E B) 
                                (ON B F) 
                                (ON F D) 
                                (ON D A) 
                                (ON A C) 
                                (ON C G)
                            ) t1)
                )    
))
