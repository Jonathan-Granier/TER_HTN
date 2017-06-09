(define (problem BLOCKS-6-1)
(:domain BLOCKS)
     (:requirements :strips :typing :negative-preconditions :htn :equality)

(:objects F E D B A C - block)
(:INIT 
	(CLEAR A) 
    (CLEAR B) 
    (CLEAR E) 
    (CLEAR C) 
    (CLEAR D) 
    (ONTABLE F)
    (ONTABLE B) 
    (ONTABLE E) 
    (ONTABLE C) 
    (ONTABLE D) 
    (ON A F) 
    (HANDEMPTY))
(:goal
     :tasks  (
            (tag t1 (do_put_on A D))
            (tag t2 (do_put_on B A))
            (tag t3 (do_put_on C B))
            (tag t4 (do_put_on F C))
            (tag t5 (do_put_on E F))

        )

    :constraints(and
                    (after (and
                                (ON E F) 
                                (ON F C) 
                                (ON C B) 
                                (ON B A) 
                                (ON A D)
                            ) t1)
                )    
))
