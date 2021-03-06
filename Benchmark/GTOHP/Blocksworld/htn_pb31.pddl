(define (problem BLOCKS-15-0)
(:domain BLOCKS)
     (:requirements :strips :typing :negative-preconditions :htn :equality)

(:objects F I G O J H N E D B L A C K M  - block)
(:INIT 
	(CLEAR E) 
    (CLEAR M) 
    (CLEAR B) 
    (CLEAR F) 
    (CLEAR I) 
    (ONTABLE G)
    (ONTABLE N) 
    (ONTABLE O) 
    (ONTABLE K) 
    (ONTABLE H) 
    (ON E J) 
    (ON J D) 
    (ON D L)
    (ON L C) 
    (ON C G) 
    (ON M N) 
    (ON B A) 
    (ON A O) 
    (ON F K) 
    (ON I H)
    (HANDEMPTY))
(:goal
     :tasks  (
            (tag t1 (do_put_on I C))
            (tag t2 (do_put_on N I))
            (tag t3 (do_put_on D N))
            (tag t4 (do_put_on J D))
            (tag t5 (do_put_on L J))
            (tag t6 (do_put_on B L))
            (tag t7 (do_put_on A B))
            (tag t8 (do_put_on E A))
            (tag t9 (do_put_on F E))
            (tag t10 (do_put_on M F))
            (tag t11 (do_put_on K M))
            (tag t12 (do_put_on H K))
            (tag t13 (do_put_on O H))
            (tag t14 (do_put_on G O))

        )

    :constraints(and
                    (after (and
                                (ON G O) 
                                (ON O H) 
                                (ON H K) 
                                (ON K M) 
                                (ON M F) 
                                (ON F E) 
                                (ON E A)
                                (ON A B) 
                                (ON B L) 
                                (ON L J) 
                                (ON J D) 
                                (ON D N) 
                                (ON N I) 
                                (ON I C)
                            ) t1)
                )    
))
