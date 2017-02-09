
(define (problem HTN_grpr_pb_2_4_2)
	(:domain gripper)
	(:requirements :strips :typing :htn :negative-preconditions)
	(:objects	A B - room
				ball1 ball2 ball3 ball4 - ball
				left right - gripper
	)
	(:init
          (at-robby A)
          (free left)
          (free right)
          (at ball4 A)
          (at ball3 A)
          (at ball2 A)
          (at ball1 A)
	)

(:goal
       :tasks(
       			(tag t1 (transport ball1 ball2 A B))
				(tag t2 (transport ball3 ball4 A B))

       	     )
       :constraints (and 
       					(after (and (at ball1 B)(at ball2 B)(at ball3 B)(at ball4 B)) last(t1 t2))
					)
)

