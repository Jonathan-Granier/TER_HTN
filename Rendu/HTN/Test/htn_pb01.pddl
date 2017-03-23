(define (problem Hiking-3-4)
(:domain hiking)
	     (:requirements :strips :typing :negative-preconditions :htn :equality)

(:objects 
	 place0 place1 place2 - place
	 B - bool
)
(:init
	
	(next place0 place1)
	(next place1 place2)
	(down B)
)
(:goal
     :tasks  (
            (tag t1 (do_Test))

        )

    :constraints(and
                    (after (and
                                (up B)
                                ;(walked couple0 place1)
								;(walked couple1 place2)
								;(walked couple2 place2)

                            ) t1)
                )    
))

