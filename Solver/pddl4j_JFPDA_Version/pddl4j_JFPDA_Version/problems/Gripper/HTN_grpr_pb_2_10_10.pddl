
(define (problem HTN_grpr_pb_2_10_10)
	(:domain gripper)
	(:requirements :strips :typing :htn :negative-preconditions)
	(:objects	A B - room
				ball1 ball2 ball3 ball4 ball5 ball6 ball7 ball8 ball9 ball10 - ball
				left right - gripper
	)
	(:init
          (at-robby A)
          (free left)
          (free right)
          (at ball10 A)
          (at ball9 A)
          (at ball8 A)
          (at ball7 A)
          (at ball6 A)
          (at ball5 A)
          (at ball4 A)
          (at ball3 A)
          (at ball2 A)
          (at ball1 A)
	)

(:goal
       :tasks(
       			(tag t1 (transport ball1 A B))
				(tag t2 (transport ball2 A B))
				(tag t3 (transport ball3 A B))
				(tag t4 (transport ball4 A B))
				(tag t5 (transport ball5 A B))
				(tag t6 (transport ball6 A B))
				(tag t7 (transport ball7 A B))
				(tag t8 (transport ball8 A B))
				(tag t9 (transport ball9 A B))
				(tag t10 (transport ball10 A B))
       	     )
       :constraints (and 
       					(after (and (at ball1 B)(at ball2 B)(at ball3 B)(at ball4 B)
       								(at ball5 B)(at ball6 B)(at ball7 B)(at ball8 B)
       								(at ball9 B)(at ball10 B)) last(t1 t2 t3 t4 t5 t6 t7 t8 t9 t10))
					)
)
