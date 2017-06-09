(define (problem BLOCKS-4-0)
(:domain BLOCKS)
     (:requirements :strips :typing :negative-preconditions :htn :equality)

(:objects D B A C - block)
(:INIT 
	(CLEAR C) 
	(CLEAR A) 
	(CLEAR B) 
	(CLEAR D) 
	(ONTABLE C) 
	(ONTABLE A)
 	(ONTABLE B) 
 	(ONTABLE D) 
 	(HANDEMPTY))
(:goal
     :tasks  (
            (tag t1 (do_put_on B A))
            (tag t2 (do_put_on C B))
            (tag t3 (do_put_on D C))

        )

    :constraints(and
                    (after (and
                                (ON D C) 
								(ON C B) 
								(ON B A)
                            ) t1)
                )    
))
