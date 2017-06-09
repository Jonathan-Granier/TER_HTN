(define (problem BLOCKS-4-1)
(:domain BLOCKS)
     (:requirements :strips :typing :negative-preconditions :htn :equality)

(:objects D B A C - block)
(:INIT 
	(CLEAR B) 
    (ONTABLE D) 
    (ON B C) 
    (ON C A) 
    (ON A D) 
    (HANDEMPTY))
(:goal
     :tasks  (
            (tag t1 (do_put_on A B))
            (tag t2 (do_put_on C A))
            (tag t3 (do_put_on D C))

        )

    :constraints(and
                    (after (and
                                (ON D C) 
                                (ON C A) 
                                (ON A B)
                            ) t1)
                )    
))
