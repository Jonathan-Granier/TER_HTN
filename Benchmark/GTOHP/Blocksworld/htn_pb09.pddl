(define (problem BLOCKS-6-2)
(:domain BLOCKS)
     (:requirements :strips :typing :negative-preconditions :htn :equality)

(:objects F E D B A C - block)
(:INIT 
	(CLEAR A) 
    (ONTABLE C) 
    (ON A D) 
    (ON D B) 
    (ON B F) 
    (ON F E) 
    (ON E C)
    (HANDEMPTY))
(:goal
     :tasks  (
            (tag t1 (do_put_on C D))
            (tag t2 (do_put_on B C))
            (tag t3 (do_put_on A B))
            (tag t4 (do_put_on F A))
            (tag t5 (do_put_on E F))

        )

    :constraints(and
                    (after (and
                                (ON E F) 
                                (ON F A) 
                                (ON A B) 
                                (ON B C) 
                                (ON C D)
                            ) t1)
                )    
))
