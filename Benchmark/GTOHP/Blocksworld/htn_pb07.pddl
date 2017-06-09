(define (problem BLOCKS-6-0)
(:domain BLOCKS)
     (:requirements :strips :typing :negative-preconditions :htn :equality)

(:objects F E D B A C - block)
(:INIT 
	(CLEAR D) 
    (CLEAR F) 
    (ONTABLE C) 
    (ONTABLE B) 
    (ON D A) 
    (ON A C) 
    (ON F E)
    (ON E B) 
    (HANDEMPTY))
(:goal
     :tasks  (
            (tag t1 (do_put_on F D))
            (tag t2 (do_put_on E F))
            (tag t3 (do_put_on A E))
            (tag t4 (do_put_on B A))
            (tag t5 (do_put_on C B))

        )

    :constraints(and
                    (after (and
                                (ON C B) 
                                (ON B A) 
                                (ON A E) 
                                (ON E F) 
                                (ON F D)
                            ) t1)
                )    
))
