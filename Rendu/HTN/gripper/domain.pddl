(define (domain gripper-typed)
   (:requirements :typing :htn :negative-preconditions )
   (:types room ball gripper)
   (:constants left right - gripper)
   (:predicates (at-robby ?r - room)
		(at ?b - ball ?r - room)
		(free ?g - gripper)
		(carry ?o - ball ?g - gripper))

   (:action move
       :parameters  (?from ?to - room)
       :precondition (at-robby ?from)
       :effect (and  (at-robby ?to)
		     (not (at-robby ?from))))



   (:action pick
       :parameters (?obj - ball ?room - room ?gripper - gripper)
       :precondition  (and  (at ?obj ?room) (at-robby ?room) (free ?gripper))
       :effect (and (carry ?obj ?gripper)
		    (not (at ?obj ?room)) 
		    (not (free ?gripper))))


   (:action drop
       :parameters  (?obj - ball ?room - room ?gripper - gripper)
       :precondition  (and  (carry ?obj ?gripper) (at-robby ?room))
       :effect (and (at ?obj ?room)
		    (free ?gripper)
		    (not (carry ?obj ?gripper))))


(:action nop
            :parameters ()
            :precondition (and )
            :effect (and )
    )



(:method do_move_on
    :parameters (?r - room)
    :expansion  (
                    (tag t1 (nop))
                )
    :constraints
                ( and
                    (before 
                        ( at-robby ?r)
                    t1
                    )
                )
)


(:method do_move_on
    :parameters (?r - room)
    :expansion  (
                    (tag t1 (move ?r2 ?r))
                )
    :constraints
                ( and
                    (before 
                        ( and
                        ( not( at-robby ?r))
                        ( at-robby ?r2)
                        )
                    t1
                    )
                )
)



;; Si j'ai plus de ball dans A j'ai fini



;; Sinon je prends 2 balls et je 
(:method do_problem
    :parameters (?ra - room ?rb - room )
    :expansion  (
                    (tag t1 (do_move_on ?ra))
                    (tag t2 (pick ?b1 ?ra ?gl))
                    (tag t3 (pick ?b2 ?ra ?gr))
                    (tag t4 (move ?ra ?rb))
                    (tag t5 (drop ?b1 ?rb ?gl))
                    (tag t6 (drop ?b2 ?rb ?gr))
                    (tag t7 (do_problem ?ra ?rb))
                )
    :constraints
                ( and
                    (before 
                        ( and
                        ( at ?b1 ?ra)
                        ( at ?b2 ?ra)
                        ( free ?gl)
                        ( free ?gr)
                        )
                    t1
                    )
                )
)



(:method do_problem
    :parameters (?ra - room ?rb - room )
    :expansion  (
                    (tag t1 (nop))
                )
    :constraints
                ( and
                    (before 
                        ( not(at ?b ?ra)
                        )
                    t1
                    )
                )
)
)