
(define (problem SHOP_bw_pb_10_4)
(:domain blocksworld)
(:requirements :strips :typing :htn :negative-preconditions)
(:objects b1 b2 b3 b4 b5 b6 b7 b8 b9 b10 table - block)
(:init
		(on b1 table) (on b9 b1) (clear b9) 
		(on b2 table) (on b4 b2) (on b7 b4) (clear b7)
		(on b3 table) (on b5 b3) (on b6 b5) (on b8 b6) (clear b8)
		(on b10 table) (clear b10)
)

(:goal
       :tasks(
				(tag t1 (put-on b2 b1)) (tag t2 (put-on b3 b2)) (tag t3 (put-on b7 b3)) (tag t4 (put-on b9 b7))
				(tag t5 (put-on b6 b5))
				(tag t6 (put-on b10 b8))
       	     )
       :constraints (and 
       					(series t1 t2 t3 t4 t5 t6)
       					(after (and (on b1 table) (on b2 b1) (on b3 b2) (on b7 b3) (on b9 b7) (clear b9)
       								(on b4 table) (clear b4)
       								(on b5 table) (on b6 b5) (clear b6)
       								(on b8 table) (on b10 b8) (clear b10)
       								) last(t1 t2 t3 t4 t5 t6)
       					)
					)
)

