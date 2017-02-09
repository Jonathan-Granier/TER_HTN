
(define (problem SHOP_bw_pb_15_3)
(:domain blocksworld)
(:requirements :strips :typing :htn :negative-preconditions)
(:objects b1 b2 b3 b4 b5 b6 b7 b8 b9 b10 B11 B12 B13 b14 b15 b16 b17 b18 b19 b20 table - block)
(:init
		(on b1 table) (on b2 b1) (on b3 b2) (on b4 b3) (on b8 b4) (on b15 b8) (on b19 b15) (clear b19) 
		(on b5 table) (on b6 b5) (on b9 b6) (on b10 b9) (on b13 b10) (clear b13)
		(on b7 table) (on b12 b7) (clear b12)
		(on b11 table) (on b14 b11) (on b16 b14) (on b17 b16) (on b20 b17) (clear b20)
		(on b18 table) (clear b18)
)

(:goal
       :tasks(
				(tag t0 (make-clear b1)) (tag t00 (make-clear b5)) (tag t000 (make-clear b11)) (tag t1 (put-on b3 b1)) (tag t2 (put-on b4 b3)) (tag t3 (put-on b5 b4))
				(tag t4 (put-on b9 b5)) (tag t5 (put-on b11 b9)) (tag t6 (put-on b15 b11))
				(tag t7 (put-on b8 b2)) (tag t8 (put-on b10 b6)) (tag t9 (put-on b18 b10)) (tag t10 (put-on b13 b7)) (tag t11 (put-on b17 b16))
       	     )
       :constraints (and 
       					;;(series t1 t2 t3 t4 t5 t6 t7 t8 t9 t10 t11)
       					(after (and (on b1 table) (on b3 b1) (on b4 b3) (on b5 b4) (on b9 b5) (on b11 b9) (on b15 b11) (clear b15)
       								(on b2 table) (on b8 b2) (clear b8)
       								(on b6 table) (on b10 b6) (on b18 b10) (clear b18)		
       								(on b7 table) (on b13 b7) (clear b13)
       								(on b12 table) (clear b12)
       								(on b14 table) (clear b14)
       								(on b16 table) (on b17 b16) (clear b17)
       								(on b19 table) (clear b19)
       								(on b20 table) (clear b20)
       							) last(t11)
       					)
					)
)

