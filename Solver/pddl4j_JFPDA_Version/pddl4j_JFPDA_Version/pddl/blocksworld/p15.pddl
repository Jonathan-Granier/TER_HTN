
(define (problem BLOCKS-8-2)
(:domain BLOCKS)
(:requirements :strips :typing :htn :negative-preconditions)
(:objects F B G C A D - block Y Z - table)
(:INIT (CLEAR D) (CLEAR A) (CLEAR C) 
	(ONTABLE G) (ONTABLE A) (ONTABLE C) 
	(ON D B) (ON B F) (ON F G)
	(HANDEMPTY))

(:goal
	:tasks(
			(tag firstGoal	(stack C A))
			(tag thirdGoal	(transfer A Y Z))
			(tag secondGoal	(pick-up C))
			)
	
	:constraints(and
					(series secondGoal thirdGoal firstGoal)
					(before (and (posed A Z) (ontable C)) secondGoal)
					(between (posed A Z) thirdGoal firstGoal)
				)
)