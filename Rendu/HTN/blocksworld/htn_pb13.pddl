(define (problem BLOCKS-8-0)
(:domain BLOCKS)
     (:requirements :strips :typing :negative-preconditions :htn :equality)

(:objects F H E D B A C G - block)
(:INIT 
	(CLEAR A) 
    (CLEAR D) 
    (CLEAR B) 
    (CLEAR C) 
    (ONTABLE E) 
    (ONTABLE F)
    (ONTABLE B) 
    (ONTABLE C) 
    (ON A G) 
    (ON G E) 
    (ON D H) 
    (ON H F) 
    (HANDEMPTY))
(:goal
     :tasks  (
            (tag t1 (do_put_on G B))
            (tag t2 (do_put_on A G))
            (tag t3 (do_put_on C A))
            (tag t4 (do_put_on H C))
            (tag t5 (do_put_on E H))
            (tag t6 (do_put_on F E))
            (tag t7 (do_put_on D F))

        )

    :constraints(and
                    (after (and
                                (ON D F) 
                                (ON F E) 
                                (ON E H) 
                                (ON H C) 
                                (ON C A) 
                                (ON A G) 
                                (ON G B)
                            ) t1)
                )    
))
