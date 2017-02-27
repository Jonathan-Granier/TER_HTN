(define (problem BLOCKS-5-2)
(:domain BLOCKS)
     (:requirements :strips :typing :negative-preconditions :htn :equality)

(:objects E D B A C - block)
(:INIT 
	(CLEAR D) 
    (ONTABLE B) 
    (ON D E) 
    (ON E C) 
    (ON C A) 
    (ON A B) 
    (HANDEMPTY))
(:goal
     :tasks  (
            (tag t1 (do_put_on E A))
            (tag t2 (do_put_on B E))
            (tag t3 (do_put_on C B))
            (tag t4 (do_put_on D C))

        )

    :constraints(and
                    (after (and
                                (ON D C) 
                                (ON C B) 
                                (ON B E) 
                                (ON E A)
                            ) t1)
                )    
))
