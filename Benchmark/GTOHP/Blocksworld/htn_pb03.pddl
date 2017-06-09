(define (problem BLOCKS-4-2)
(:domain BLOCKS)
     (:requirements :strips :typing :negative-preconditions :htn :equality)

(:objects D B A C - block)
(:INIT 
	(CLEAR A) 
    (CLEAR C) 
    (CLEAR D) 
    (ONTABLE A) 
    (ONTABLE B) 
    (ONTABLE D)
    (ON C B) 
    (HANDEMPTY))
(:goal
     :tasks  (
            (tag t1 (do_put_on C D))
            (tag t2 (do_put_on B C))
            (tag t3 (do_put_on A B))

        )

    :constraints(and
                    (after (and
                                (ON A B) 
                                (ON B C) 
                                (ON C D)
                            ) t1)
                )    
))
