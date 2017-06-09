(define (problem BLOCKS-12-0)
(:domain BLOCKS)
     (:requirements :strips :typing :negative-preconditions :htn :equality)

(:objects F I J H E D B L A C K G - block)
(:INIT 
	(CLEAR H) 
    (CLEAR L) 
    (CLEAR J) 
    (ONTABLE C) 
    (ONTABLE F) 
    (ONTABLE J)
    (ON H A) 
    (ON A G) 
    (ON G K) 
    (ON K E) 
    (ON E B) 
    (ON B D) 
    (ON D I) 
    (ON I C)
    (ON L F)
    (HANDEMPTY))
(:goal
     :tasks  (
            (tag t1 (do_put_on H G))
            (tag t2 (do_put_on A H))
            (tag t3 (do_put_on F A))
            (tag t4 (do_put_on K F))
            (tag t5 (do_put_on E K))
            (tag t6 (do_put_on J E))
            (tag t7 (do_put_on D J))
            (tag t8 (do_put_on L D))
            (tag t9 (do_put_on B L))
            (tag t10 (do_put_on C B))
            (tag t11 (do_put_on I C))

        )

    :constraints(and
                    (after (and
                                (ON I C) 
                                (ON C B) 
                                (ON B L) 
                                (ON L D) 
                                (ON D J) 
                                (ON J E) 
                                (ON E K)
                                (ON K F) 
                                (ON F A) 
                                (ON A H) 
                                (ON H G)
                            ) t1)
                )    
))