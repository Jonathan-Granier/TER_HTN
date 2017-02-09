
(define (problem SHOP_bw_pb_4_1)
(:domain blocksworld)
(:requirements :strips :typing :htn :negative-preconditions)
(:objects b1 b2 b3 b4 table - block)
(:init
		(on b1 table) (on b2 table) (on b3 table) (on b4 b3) (clear b1) (clear b2) (clear b4)
)

(:goal
       :tasks(
       			(tag t1 (put-on b3 b4))
				(tag t2 (put-on b2 b3))
				(tag t3 (put-on b1 b2))
       	     )
       :constraints (and 
       					(after (and (on b1 b2) (on b2 b3) (on b3 b4) (clear b1)) t3)
					)
)

