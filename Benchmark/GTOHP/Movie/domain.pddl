(define (domain movie-dom) (:requirements :negative-preconditions :htn :typing)
  (:types chips dip pop cheese crackers - object)
  (:predicates (movie-rewound)
               (counter-at-two-hours)
               (counter-at-zero)
               (have-chips)
               (have-dip)
               (have-pop)
               (have-cheese)
               (have-crackers))
  


  
  (:action rewind-movie
           :parameters ()
	   :precondition ()
           :effect (and (movie-rewound)
                        ;; Let's assume that the movie is 2 hours long
                        ))
  
  (:action reset-counter
           :parameters ()
	   :precondition ()
           :effect (counter-at-zero))


  ;;; Get the food and snacks for the movie
  (:action get-chips
           :parameters (?x - chips)
           :precondition ()   
	   :effect (have-chips))
  
  (:action get-dip
           :parameters (?x - dip)
           :precondition ()
	   :effect (have-dip))

  (:action get-pop
           :parameters (?x - pop)
           :precondition ()
           :effect (have-pop))
  
  (:action get-cheese
           :parameters (?x - cheese)
	   :precondition ()
           :effect (have-cheese))
  
  (:action get-crackers
           :parameters (?x - crackers)
           :precondition ()
	   :effect (have-crackers)) 

(:method do_problem
    :parameters ()
    :expansion (
            ;(tag t1 (nop))
            (tag t1 (rewind-movie))
            (tag t2 (do_take_cheese))
            (tag t3 (do_take_crackers))
            (tag t4 (do_take_pop))
            (tag t5 (do_take_dip))
            (tag t6 (do_take_chips))
            (tag t7 (reset-counter))
          )
    :constraints
          ( and
            (before
                ( and 
                                ( not (movie-rewound ))
                )
            t1
            )
          )

  )



(:method do_take_cheese
    :parameters ()
    :expansion (
            (tag t1 (get-cheese ?x))
          )
    :constraints
          ( and
            (before
                ( and 
                        ( not (have-cheese))
                )
            t1
            )
          )

  )
(:method do_take_crackers
    :parameters ()
    :expansion (
            (tag t1 (get-crackers ?x))
          )
    :constraints
          ( and
            (before
                ( and 
                        ( not (have-crackers))
                )
            t1
            )
          )

  )
(:method do_take_pop
    :parameters ()
    :expansion (
            (tag t1 (get-pop ?x))
          )
    :constraints
          ( and
            (before
                ( and 
                        ( not (have-pop))
                )
            t1
            )
          )

  )
(:method do_take_dip
    :parameters ()
    :expansion (
            (tag t1 (get-dip ?x))
          )
    :constraints
          ( and
            (before
                ( and 
                        ( not (have-dip))
                )
            t1
            )
          )

  )


(:method do_take_chips
    :parameters ()
    :expansion (
            (tag t1 (get-chips ?x))
          )
    :constraints
          ( and
            (before
                ( and 
                        ( not (have-chips))
                )
            t1
            )
          )

  )

)
