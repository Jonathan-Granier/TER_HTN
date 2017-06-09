
(define (problem SHOP_bw_pb_3_2)
(:domain blocksworld)
(:requirements :strips :typing :htn :negative-preconditions)
(:objects b1 b2 b3 table - block)
(:init
		(on b1 table) (on b2 b1) (on b3 table) (clear b2) (clear b3)
)

(:goal
       :tasks(
				(tag t1 (put-on b2 b3))
				(tag t2 (put-on b1 b2))
       	     )
       :constraints (and 
       					(after (and (on b1 b2) (on b2 b3)) last(t1 t2))
					)
)

