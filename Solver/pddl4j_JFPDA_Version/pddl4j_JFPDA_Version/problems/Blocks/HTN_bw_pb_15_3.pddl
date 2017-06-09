
(define (problem SHOP_bw_pb_15_3)
(:domain blocksworld)
(:requirements :strips :typing :htn :negative-preconditions)
(:objects b1 b2 b3 b4 b5 b6 b7 b8 b9 b10 B11 B12 B13 b14 b15 table - block)
(:init
		(on b1 table) (on b2 b1) (on b3 b2) (on b4 b3) (on b9 b4) (on b10 b9) (clear b10) 
		(on b5 table) (on b8 b5) (on b11 b8) (on b13 b11) (on b15 b13) (clear b15)
		(on b6 table) (on b7 b6) (on b12 b7) (on b14 b12) (clear b14)
)

(:goal
       :tasks(
				(tag t1 (put-on b5 b1)) (tag t2 (put-on b7 b5)) (tag t3 (put-on b8 b7))
				(tag t4 (put-on b3 b2)) (tag t5 (put-on b6 b3)) (tag t6 (put-on b12 b6)) (tag t7 (put-on b13 b12)) (tag t8 (put-on b14 b13))
				(tag t9 (put-on b10 b9)) (tag t10 (put-on b11 b10))
       	     )
       :constraints (and 
       					(series t1 t2 t3 t4 t5 t6 t7 t8 t9 t10)
       					(after (and (on b1 table) (on b5 b1) (on b7 b5) (on b8 b7)
       								(on b2 table) (on b3 b2) (on b6 b3) (on b12 b6) (on b13 b12) (on b14 b13) (clear b14)
       								(on b4 table) (clear b4)
       								(on b9 table) (on b10 b9) (on b11 b10) (clear b11)
       								(on b15 table) (clear b15)
       							) last(t1 t2 t3 t4 t5 t6 t7 t8 t9 t10)
       					)
					)
)

