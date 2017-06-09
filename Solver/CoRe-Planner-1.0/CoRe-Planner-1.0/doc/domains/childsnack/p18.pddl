; child-snack task with 21 children and 0.4 gluten factor 
; constant factor of 1.3
; random seed: 234324

(define (problem prob-snack)
  (:domain child-snack)
  (:requirements :strips :typing :htn :negative-preconditions)

  ;---------------- Facts -----------------------
  (:objects
    child1 child2 child3 child4 child5 child6 child7 child8 child9 child10 child11 child12 child13 child14 child15 child16 child17 child18 child19 child20 child21 - child
    bread1 bread2 bread3 bread4 bread5 bread6 bread7 bread8 bread9 bread10 bread11 bread12 bread13 bread14 bread15 bread16 bread17 bread18 bread19 bread20 bread21 - bread-portion
    content1 content2 content3 content4 content5 content6 content7 content8 content9 content10 content11 content12 content13 content14 content15 content16 content17 content18 content19 content20 content21 - content-portion
    tray1 tray2 tray3 tray4 - tray
    table1 table2 table3 - place
    sandw1 sandw2 sandw3 sandw4 sandw5 sandw6 sandw7 sandw8 sandw9 sandw10 sandw11 sandw12 sandw13 sandw14 sandw15 sandw16 sandw17 sandw18 sandw19 sandw20 sandw21 sandw22 sandw23 sandw24 sandw25 sandw26 sandw27 sandw28 - sandwich
  )

  ;--------------- Initial State -----------------
  (:init
     (at tray1 kitchen)
     (at tray2 kitchen)
     (at tray3 kitchen)
     (at tray4 kitchen)
     (at_kitchen_bread bread1)
     (at_kitchen_bread bread2)
     (at_kitchen_bread bread3)
     (at_kitchen_bread bread4)
     (at_kitchen_bread bread5)
     (at_kitchen_bread bread6)
     (at_kitchen_bread bread7)
     (at_kitchen_bread bread8)
     (at_kitchen_bread bread9)
     (at_kitchen_bread bread10)
     (at_kitchen_bread bread11)
     (at_kitchen_bread bread12)
     (at_kitchen_bread bread13)
     (at_kitchen_bread bread14)
     (at_kitchen_bread bread15)
     (at_kitchen_bread bread16)
     (at_kitchen_bread bread17)
     (at_kitchen_bread bread18)
     (at_kitchen_bread bread19)
     (at_kitchen_bread bread20)
     (at_kitchen_bread bread21)
     (at_kitchen_content content1)
     (at_kitchen_content content2)
     (at_kitchen_content content3)
     (at_kitchen_content content4)
     (at_kitchen_content content5)
     (at_kitchen_content content6)
     (at_kitchen_content content7)
     (at_kitchen_content content8)
     (at_kitchen_content content9)
     (at_kitchen_content content10)
     (at_kitchen_content content11)
     (at_kitchen_content content12)
     (at_kitchen_content content13)
     (at_kitchen_content content14)
     (at_kitchen_content content15)
     (at_kitchen_content content16)
     (at_kitchen_content content17)
     (at_kitchen_content content18)
     (at_kitchen_content content19)
     (at_kitchen_content content20)
     (at_kitchen_content content21)
     (no_gluten_bread bread5)
     (no_gluten_bread bread20)
     (no_gluten_bread bread8)
     (no_gluten_bread bread9)
     (no_gluten_bread bread2)
     (no_gluten_bread bread13)
     (no_gluten_bread bread19)
     (no_gluten_bread bread17)
     (no_gluten_content content21)
     (no_gluten_content content9)
     (no_gluten_content content2)
     (no_gluten_content content6)
     (no_gluten_content content12)
     (no_gluten_content content18)
     (no_gluten_content content1)
     (no_gluten_content content8)
     (allergic_gluten child12)
     (allergic_gluten child1)
     (allergic_gluten child2)
     (allergic_gluten child11)
     (allergic_gluten child16)
     (allergic_gluten child17)
     (allergic_gluten child19)
     (allergic_gluten child21)
     (not_allergic_gluten child13)
     (not_allergic_gluten child10)
     (not_allergic_gluten child3)
     (not_allergic_gluten child4)
     (not_allergic_gluten child5)
     (not_allergic_gluten child6)
     (not_allergic_gluten child7)
     (not_allergic_gluten child8)
     (not_allergic_gluten child9)
     (not_allergic_gluten child18)
     (not_allergic_gluten child15)
     (not_allergic_gluten child20)
     (not_allergic_gluten child14)
     (waiting child1 table3)
     (waiting child2 table1)
     (waiting child3 table1)
     (waiting child4 table1)
     (waiting child5 table2)
     (waiting child6 table3)
     (waiting child7 table1)
     (waiting child8 table2)
     (waiting child9 table2)
     (waiting child10 table3)
     (waiting child11 table3)
     (waiting child12 table1)
     (waiting child13 table2)
     (waiting child14 table2)
     (waiting child15 table3)
     (waiting child16 table1)
     (waiting child17 table3)
     (waiting child18 table2)
     (waiting child19 table3)
     (waiting child20 table1)
     (waiting child21 table2)
     (notexist sandw1)
     (notexist sandw2)
     (notexist sandw3)
     (notexist sandw4)
     (notexist sandw5)
     (notexist sandw6)
     (notexist sandw7)
     (notexist sandw8)
     (notexist sandw9)
     (notexist sandw10)
     (notexist sandw11)
     (notexist sandw12)
     (notexist sandw13)
     (notexist sandw14)
     (notexist sandw15)
     (notexist sandw16)
     (notexist sandw17)
     (notexist sandw18)
     (notexist sandw19)
     (notexist sandw20)
     (notexist sandw21)
     (notexist sandw22)
     (notexist sandw23)
     (notexist sandw24)
     (notexist sandw25)
     (notexist sandw26)
     (notexist sandw27)
     (notexist sandw28)
  )
  (:goal
        :tasks  (
                    (tag t1  (serve child1 ))
                    (tag t2  (serve child2 ))
                    (tag t3  (serve child3 ))
                    (tag t4  (serve child4 ))
                    (tag t5  (serve child5 ))
                    (tag t6  (serve child6 ))
                    (tag t7  (serve child7 ))
                    (tag t8  (serve child8 ))
                    (tag t9  (serve child9 ))
                    (tag t10 (serve child10))
                    (tag t11 (serve child11))
                    (tag t12 (serve child12))
                    (tag t13 (serve child13))
                    (tag t14 (serve child14))
                    (tag t15 (serve child15))
                    (tag t16 (serve child16))
                    (tag t17 (serve child17))
                    (tag t18 (serve child18))
                    (tag t19 (serve child19))
                    (tag t20 (serve child20))
                    (tag t21 (serve child21))
                )
        :constraints(and
                        (after (and 
                                    (served child1)
                                    (served child2)
                                    (served child3)
                                    (served child4)
                                    (served child5)
                                    (served child6)
                                    (served child7)
                                    (served child8)
                                    (served child9)
                                    (served child10)
                                    (served child11)
                                    (served child12)
                                    (served child13)
                                    (served child14)
                                    (served child15)
                                    (served child16)
                                    (served child17)
                                    (served child18)
                                    (served child19)
                                    (served child20)
                                    (served child21)
                                ) t21)
                    )
)
)