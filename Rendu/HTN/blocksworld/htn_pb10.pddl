(define (problem BLOCKS-7-0)
(:domain BLOCKS)
     (:requirements :strips :typing :negative-preconditions :htn :equality)

(:objects F E D B A C G - block)
(:INIT 
	(CLEAR E) 
    (ONTABLE D) 
    (ON E G) 
    (ON G B) 
    (ON B A) 
    (ON A F) 
    (ON F C)
    (ON C D) 
    (HANDEMPTY))
(:goal
     :tasks  (
            (tag t1 (do_put_on F E))
            (tag t2 (do_put_on C F))
            (tag t3 (do_put_on B C))
            (tag t4 (do_put_on D B))
            (tag t5 (do_put_on G D))
            (tag t6 (do_put_on A G))

        )

    :constraints(and
                    (after (and
                                (ON A G) 
                                (ON G D) 
                                (ON D B) 
                                (ON B C) 
                                (ON C F) 
                                (ON F E)
                            ) t1)
                )    
))
