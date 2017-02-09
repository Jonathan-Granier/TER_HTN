(define (problem prob)
 (:domain barman)
   (:requirements :strips :typing :negative-preconditions :htn :equality)

 (:objects 
     shaker1 - shaker
     left right - hand
     shot1 shot2 shot3 shot4 shot5 shot6 shot7 shot8 shot9 shot10 shot11 shot12 shot13 shot14 shot15 shot16 - shot
     ingredient1 ingredient2 ingredient3 ingredient4 ingredient5 - ingredient
     cocktail1 cocktail2 cocktail3 cocktail4 cocktail5 cocktail6 cocktail7 cocktail8 cocktail9 cocktail10 cocktail11 - cocktail
     dispenser1 dispenser2 dispenser3 dispenser4 dispenser5 - dispenser
     l0 l1 l2 - level
)
 (:init 
  (ontable shaker1)
  (ontable shot1)
  (ontable shot2)
  (ontable shot3)
  (ontable shot4)
  (ontable shot5)
  (ontable shot6)
  (ontable shot7)
  (ontable shot8)
  (ontable shot9)
  (ontable shot10)
  (ontable shot11)
  (ontable shot12)
  (ontable shot13)
  (ontable shot14)
  (ontable shot15)
  (ontable shot16)
  (dispenses dispenser1 ingredient1)
  (dispenses dispenser2 ingredient2)
  (dispenses dispenser3 ingredient3)
  (dispenses dispenser4 ingredient4)
  (dispenses dispenser5 ingredient5)
  (clean shaker1)
  (clean shot1)
  (clean shot2)
  (clean shot3)
  (clean shot4)
  (clean shot5)
  (clean shot6)
  (clean shot7)
  (clean shot8)
  (clean shot9)
  (clean shot10)
  (clean shot11)
  (clean shot12)
  (clean shot13)
  (clean shot14)
  (clean shot15)
  (clean shot16)
  (empty shaker1)
  (empty shot1)
  (empty shot2)
  (empty shot3)
  (empty shot4)
  (empty shot5)
  (empty shot6)
  (empty shot7)
  (empty shot8)
  (empty shot9)
  (empty shot10)
  (empty shot11)
  (empty shot12)
  (empty shot13)
  (empty shot14)
  (empty shot15)
  (empty shot16)
  (handempty left)
  (handempty right)
  (shaker-empty-level shaker1 l0)
  (shaker-level shaker1 l0)
  (next l0 l1)
  (next l1 l2)
  (cocktail-part1 cocktail1 ingredient3)
  (cocktail-part2 cocktail1 ingredient1)
  (cocktail-part1 cocktail2 ingredient2)
  (cocktail-part2 cocktail2 ingredient3)
  (cocktail-part1 cocktail3 ingredient1)
  (cocktail-part2 cocktail3 ingredient5)
  (cocktail-part1 cocktail4 ingredient2)
  (cocktail-part2 cocktail4 ingredient5)
  (cocktail-part1 cocktail5 ingredient2)
  (cocktail-part2 cocktail5 ingredient5)
  (cocktail-part1 cocktail6 ingredient1)
  (cocktail-part2 cocktail6 ingredient5)
  (cocktail-part1 cocktail7 ingredient5)
  (cocktail-part2 cocktail7 ingredient2)
  (cocktail-part1 cocktail8 ingredient4)
  (cocktail-part2 cocktail8 ingredient3)
  (cocktail-part1 cocktail9 ingredient1)
  (cocktail-part2 cocktail9 ingredient4)
  (cocktail-part1 cocktail10 ingredient1)
  (cocktail-part2 cocktail10 ingredient5)
  (cocktail-part1 cocktail11 ingredient1)
  (cocktail-part2 cocktail11 ingredient3)
)
 (:goal
    :tasks  (
                (tag t1 (do_cocktail_in_shot shot1 cocktail3))
                (tag t2 (do_cocktail_in_shot shot2 cocktail7))
                (tag t3 (do_cocktail_in_shot shot3 cocktail1))
                (tag t4 (do_cocktail_in_shot shot4 cocktail6))
                (tag t5 (do_cocktail_in_shot shot5 cocktail4))
                (tag t6 (do_cocktail_in_shot shot6 cocktail10))
                (tag t7 (do_cocktail_in_shot shot7 cocktail9))
                (tag t8 (do_cocktail_in_shot shot8 cocktail8))
                (tag t9 (do_cocktail_in_shot shot9 cocktail5))
                (tag t10 (do_cocktail_in_shot shot10 cocktail1))
                (tag t11 (do_cocktail_in_shot shot11 cocktail2))
                (tag t12 (do_cocktail_in_shot shot12 ingredient1))
                (tag t13 (do_cocktail_in_shot shot13 ingredient4))
                (tag t14 (do_cocktail_in_shot shot14 ingredient2))
                (tag t15 (do_cocktail_in_shot shot15 ingredient2))
            )

    :constraints(and
                    (after (and
                                (contains shot1 cocktail3)
                                (contains shot2 cocktail7)
                                (contains shot3 cocktail1)
                                (contains shot4 cocktail6)
                                (contains shot5 cocktail4)
                                (contains shot6 cocktail10)
                                (contains shot7 cocktail9)
                                (contains shot8 cocktail8)
                                (contains shot9 cocktail5)
                                (contains shot10 cocktail1)
                                (contains shot11 cocktail2)
                                (contains shot12 ingredient1)
                                (contains shot13 ingredient4)
                                (contains shot14 ingredient2)
                                (contains shot15 ingredient2)
                            ) t15)
)))
