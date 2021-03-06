(define (problem BLOCKS-11-0)
(:domain BLOCKS)
     (:requirements :strips :typing :negative-preconditions :htn :equality)

(:objects F I J H E D B A C K G - block)
(:INIT 
	(CLEAR B) 
    (CLEAR J) 
    (CLEAR C) 
    (ONTABLE I) 
    (ONTABLE D) 
    (ONTABLE E)
    (ON B G) 
    (ON G H) 
    (ON H K) 
    (ON K A) 
    (ON A F) 
    (ON F I) 
    (ON J D) 
    (ON C E)
    (HANDEMPTY))
(:goal
     :tasks  (
            (tag t1 (do_put_on G C))
            (tag t2 (do_put_on E G))
            (tag t3 (do_put_on F E))
            (tag t4 (do_put_on I F))
            (tag t5 (do_put_on K I))
            (tag t6 (do_put_on H K))
            (tag t7 (do_put_on B H))
            (tag t8 (do_put_on D B))
            (tag t9 (do_put_on J D))
            (tag t10 (do_put_on A J))

        )

    :constraints(and
                    (after (and
                                (ON A J) 
                                (ON J D) 
                                (ON D B) 
                                (ON B H) 
                                (ON H K) 
                                (ON K I) 
                                (ON I F)
                                (ON F E) 
                                (ON E G) 
                                (ON G C)
                            ) t1)
                )    
))
