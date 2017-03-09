(define (problem BLOCKS-13-0)
(:domain BLOCKS)
     (:requirements :strips :typing :negative-preconditions :htn :equality)

(:objects F I J H E D B L A C K M G - block)
(:INIT 
	(CLEAR B) 
    (CLEAR I) 
    (CLEAR M) 
    (ONTABLE K) 
    (ONTABLE G) 
    (ONTABLE M)
    (ON B F) 
    (ON F D) 
    (ON D C) 
    (ON C J) 
    (ON J A) 
    (ON A E) 
    (ON E H) 
    (ON H L)
    (ON L K) 
    (ON I G)
    (HANDEMPTY))
(:goal
     :tasks  (
            (tag t1 (do_put_on B K))
            (tag t2 (do_put_on J B))
            (tag t3 (do_put_on L J))
            (tag t4 (do_put_on E L))
            (tag t5 (do_put_on H E))
            (tag t6 (do_put_on M H))
            (tag t7 (do_put_on A M))
            (tag t8 (do_put_on F A))
            (tag t9 (do_put_on D F))
            (tag t10 (do_put_on C D))
            (tag t11 (do_put_on I C))
            (tag t12 (do_put_on G I))

        )

    :constraints(and
                    (after (and
                                (ON G I) 
                                (ON I C) 
                                (ON C D) 
                                (ON D F) 
                                (ON F A) 
                                (ON A M) 
                                (ON M H)
                                (ON H E) 
                                (ON E L) 
                                (ON L J) 
                                (ON J B) 
                                (ON B K)
                            ) t1)
                )    
))