(define (problem BLOCKS-5-0)
(:domain BLOCKS)
     (:requirements :strips :typing :negative-preconditions :htn :equality)

(:objects E D B A C - block)
(:INIT 
	(CLEAR D) 
    (CLEAR C) 
    (ONTABLE D) 
    (ONTABLE A) 
    (ON C E) 
    (ON E B) 
    (ON B A)
    (HANDEMPTY))
(:goal
     :tasks  (
            (tag t1 (do_put_on D C))
            (tag t2 (do_put_on B D))
            (tag t3 (do_put_on E B))
            (tag t4 (do_put_on A E))

        )

    :constraints(and
                    (after (and
                                (ON A E) 
                                (ON E B) 
                                (ON B D) 
                                (ON D C)
                            ) t1)
                )    
))
