(define (problem gripper-x-1 )
(:domain gripper-typed )
	(:requirements :strips :typing :negative-preconditions :htn :equality)

(:objects rooma roomb - room
             ball4 ball3 ball2 ball1 - ball )
(:init (at-robby rooma )
          (free left )
          (free right )
          (at ball4 rooma )
          (at ball3 rooma )
          (at ball2 rooma )
          (at ball1 rooma ) )
(:goal
	:tasks  (
			 ;; A remplir
					(tag t1 (do_problem rooma roomb))
		
			
		)
	:constraints(and
			(after (and 			(at ball4 roomb )
               						(at ball3 roomb )
               						(at ball2 roomb )
               						(at ball1 roomb )
               						 )
				 t1)
		)
))