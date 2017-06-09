(define (problem BLOCKS-5-1)
(:domain BLOCKS)
     (:requirements :strips :typing :negative-preconditions :htn :equality)

(:objects E D B A C - block)
(:INIT 
	(CLEAR B) 
    (CLEAR E) 
    (CLEAR C) 
    (ONTABLE D) 
    (ONTABLE E) 
    (ONTABLE C)
    (ON B A) 
    (ON A D) 
    (HANDEMPTY))
(:goal
     :tasks  (
            (tag t1 (do_put_on A E))
            (tag t2 (do_put_on B A))
            (tag t3 (do_put_on C B))
            (tag t4 (do_put_on D C))

        )

    :constraints(and
                    (after (and
                                (ON D C) 
                                (ON C B) 
                                (ON B A) 
                                (ON A E)
                            ) t1)
                )    
))
