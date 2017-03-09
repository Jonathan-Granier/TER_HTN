(define (problem BLOCKS-16-2)
(:domain BLOCKS)
     (:requirements :strips :typing :negative-preconditions :htn :equality)

(:objects F I G O J Q H N E P D B L A C K M  - block)
(:INIT 
	(CLEAR Q) 
    (CLEAR L) 
    (CLEAR G) 
    (CLEAR H) 
    (CLEAR P) 
    (ONTABLE M)
    (ONTABLE K) 
    (ONTABLE O) 
    (ONTABLE N) 
    (ONTABLE P) 
    (ON Q A) 
    (ON A J) 
    (ON J I)
    (ON I B) 
    (ON B M) 
    (ON L F) 
    (ON F E) 
    (ON E K) 
    (ON G D) 
    (ON D C) 
    (ON C O)
    (ON H N)
    (HANDEMPTY))
(:goal
     :tasks  (
            (tag t1 (do_put_on_table F D))
            (tag t2 (do_put_on K F))
            (tag t3 (do_put_on I K))
            (tag t4 (do_put_on B I))
            (tag t5 (do_put_on G B))
            (tag t6 (do_put_on A G))
            (tag t7 (do_put_on P A))
            (tag t8 (do_put_on M P))
            (tag t9 (do_put_on E M))
            (tag t10 (do_put_on C E))
            (tag t11 (do_put_on H C))
            (tag t12 (do_put_on J H))
            (tag t13 (do_put_on O J))
            (tag t14 (do_put_on L O))
            (tag t15 (do_put_on N L))
            (tag t16 (do_put_on Q N))
        )

    :constraints(and
                    (after (and
                                (ON Q N) 
                                (ON N L) 
                                (ON L O) 
                                (ON O J) 
                                (ON J H) 
                                (ON H C) 
                                (ON C E)
                                (ON E M) 
                                (ON M P) 
                                (ON P A) 
                                (ON A G) 
                                (ON G B) 
                                (ON B I) 
                                (ON I K)
                                (ON K F) 
                                (ON F D)
                            ) t1)
                )    
))
