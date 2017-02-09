
(define (problem SHOP_bw_pb_4_2)
(:domain blocksworld)
(:requirements :strips :typing :htn :negative-preconditions)
(:objects b1 b2 b3 b4 table - block)
(:init
	(on b1 table) (on b2 b1) (on b3 table) (on b4 b3) (clear b2) (clear b4)
)

(:goal
	:tasks(
       	(tag t1 (put-on b3 b4))
		(tag t2 (put-on b2 b3))
		(tag t3 (put-on b1 b2))
       	     )
       :constraints	(and 
			(series t1 t2 t3)
       			(after (and (on b4 table) (on b3 b4) (on b2 b3) (on b1 b2)) last(t1 t2 t3))
			)
)

