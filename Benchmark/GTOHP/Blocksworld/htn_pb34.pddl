(define (problem BLOCKS-16-2)
(:domain BLOCKS)
     (:requirements :strips :typing :negative-preconditions :htn :equality)

(:objects F I G O J H N E P D B L A C K M  - block)
(:INIT 
	(CLEAR E) 
    (CLEAR L) 
    (ONTABLE J) 
    (ONTABLE O) 
    (ON E F) 
    (ON F H) 
    (ON H B)
    (ON B C) 
    (ON C M) 
    (ON M D) 
    (ON D A) 
    (ON A P) 
    (ON P N) 
    (ON N G) 
    (ON G I)
    (ON I K) 
    (ON K J) 
    (ON L O)
    (HANDEMPTY))
(:goal
     :tasks  (
            (tag t1 (do_put_on P O))
            (tag t2 (do_put_on A P))
            (tag t3 (do_put_on N A))
            (tag t4 (do_put_on M N))
            (tag t5 (do_put_on L M))
            (tag t6 (do_put_on C L))
            (tag t7 (do_put_on E C))
            (tag t8 (do_put_on G E))
            (tag t9 (do_put_on J G))
            (tag t10 (do_put_on K J))
            (tag t11 (do_put_on B K))
            (tag t12 (do_put_on F B))
            (tag t13 (do_put_on H F))
            (tag t14 (do_put_on D H))
            (tag t15 (do_put_on I D))
        )

    :constraints(and
                    (after (and
                                (ON I D) 
                                (ON D H) 
                                (ON H F) 
                                (ON F B) 
                                (ON B K) 
                                (ON K J) 
                                (ON J G)
                                (ON G E) 
                                (ON E C) 
                                (ON C L) 
                                (ON L M) 
                                (ON M N) 
                                (ON N A) 
                                (ON A P)
                                (ON P O)
                            ) t1)
                )    
))
