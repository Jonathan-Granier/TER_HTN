(define (problem BLOCKS-7-1)
(:domain BLOCKS)
     (:requirements :strips :typing :negative-preconditions :htn :equality)

(:objects F E D B A C G - block)
(:INIT 
	(CLEAR A) 
    (CLEAR C) 
    (ONTABLE G) 
    (ONTABLE F) 
    (ON A G) 
    (ON C D) 
    (ON D B)
    (ON B E) 
    (ON E F) 
    (HANDEMPTY))
(:goal
     :tasks  (
            (tag t1 (do_put_on C D))
            (tag t2 (do_put_on G C))
            (tag t3 (do_put_on F G))
            (tag t4 (do_put_on B F))
            (tag t5 (do_put_on E B))
            (tag t6 (do_put_on A E))

        )

    :constraints(and
                    (after (and
                                (ON A E) 
                                (ON E B) 
                                (ON B F) 
                                (ON F G) 
                                (ON G C) 
                                (ON C D)
                            ) t1)
                )    
))
