(define (problem BLOCKS-13-1)
(:domain BLOCKS)
     (:requirements :strips :typing :negative-preconditions :htn :equality)

(:objects F I J H E D B L A C K M G - block)
(:INIT 
	(CLEAR J) 
    (CLEAR B) 
    (ONTABLE F) 
    (ONTABLE K) 
    (ON J E) 
    (ON E D) 
    (ON D C)
    (ON C A) 
    (ON A L) 
    (ON L H) 
    (ON H G) 
    (ON G M) 
    (ON M I) 
    (ON I F) 
    (ON B K)
    (HANDEMPTY))
(:goal
     :tasks  (
            (tag t1 (do_put_on I B))
            (tag t2 (do_put_on H I))
            (tag t3 (do_put_on G H))
            (tag t4 (do_put_on K G))
            (tag t5 (do_put_on F K))
            (tag t6 (do_put_on J F))
            (tag t7 (do_put_on C J))
            (tag t8 (do_put_on M C))
            (tag t9 (do_put_on L M))
            (tag t10 (do_put_on E L))
            (tag t11 (do_put_on A E))
            (tag t12 (do_put_on D A))

        )

    :constraints(and
                    (after (and
                                (ON D A) 
                                (ON A E) 
                                (ON E L) 
                                (ON L M) 
                                (ON M C) 
                                (ON C J) 
                                (ON J F)
                                (ON F K) 
                                (ON K G) 
                                (ON G H) 
                                (ON H I) 
                                (ON I B)
                            ) t1)
                )    
))
