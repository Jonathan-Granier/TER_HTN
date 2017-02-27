(define (problem BLOCKS-8-2)
(:domain BLOCKS)
     (:requirements :strips :typing :negative-preconditions :htn :equality)

(:objects F H E D B A C G - block)
(:INIT 
	(CLEAR D) 
    (CLEAR A) 
    (CLEAR E) 
    (CLEAR H) 
    (CLEAR C) 
    (ONTABLE G)
    (ONTABLE A) 
    (ONTABLE E) 
    (ONTABLE H) 
    (ONTABLE C) 
    (ON D B) 
    (ON B F) 
    (ON F G)
    (HANDEMPTY))
(:goal
     :tasks  (
            (tag t1 (do_put_on D H))
            (tag t2 (do_put_on A D))
            (tag t3 (do_put_on F A))
            (tag t4 (do_put_on G F))
            (tag t5 (do_put_on E G))
            (tag t6 (do_put_on B E))
            (tag t7 (do_put_on C B))

        )

    :constraints(and
                    (after (and
                                (ON C B) 
                                (ON B E) 
                                (ON E G) 
                                (ON G F) 
                                (ON F A) 
                                (ON A D) 
                                (ON D H)
                            ) t1)
                )    
))
