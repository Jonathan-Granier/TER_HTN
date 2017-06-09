
(define (problem SHOP_bw_pb_7_3)
(:domain blocksworld)
(:requirements :strips :typing :htn :negative-preconditions)
(:objects b1 b2 b3 b4 b5 b6 b7 table - block)
(:init
		(on b1 table) (on b7 b1) (clear b7) 
		(on b2 table) (on b4 b2) (on b6 b4) (clear b6)
		(on b3 table) (on b5 b3) (clear b5)
)

(:goal
       :tasks(
				(tag t1 (put-on b3 b1)) (tag t2 (put-on b5 b3)) 
				(tag t3 (put-on b6 b7)) (tag t4 (put-on b2 b6))
       	     )
       :constraints (and 
       					(series t1 t2 t3 t4)
       					(after (and 
       								(on b1 table) (on b3 b1) (on b5 b3) (clear b5)
       								(on b7 table) (on b6 b7) (on b2 b6) (clear b2) 
       								(on b4 table) (clear b4)
       							) last (t1 t2 t3 t4)
       					)
					)
)

