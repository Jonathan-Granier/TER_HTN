(define (problem mysty-x-30 )
(:domain mystery-typed )
	(:requirements :strips :typing :negative-preconditions :htn :equality)

(:objects turkey cucumber baguette beef muffin hotdog mutton
             chicken hamburger yogurt scallop rice guava - food
             triumph intoxication expectation excitement achievement
             - pleasure
             prostatitis dread laceration anxiety boils sciatica abrasion
             - pain
             quebec goias manitoba surrey pennsylvania alsace guanabara
             arizona oregon bosnia bavaria moravia kentucky - province
             mars vulcan neptune venus - planet )
(:init (eats hotdog cucumber )
          (locale baguette guanabara )
          (locale hamburger bavaria )
          (eats muffin baguette )
          (eats baguette muffin )
          (eats rice scallop )
          (craves prostatitis cucumber )
          (craves intoxication beef )
          (attacks surrey pennsylvania )
          (locale muffin oregon )
          (eats guava yogurt )
          (eats turkey chicken )
          (craves sciatica yogurt )
          (attacks quebec goias )
          (craves excitement yogurt )
          (craves anxiety chicken )
          (eats turkey muffin )
          (eats beef hamburger )
          (eats baguette rice )
          (eats hamburger hotdog )
          (eats mutton chicken )
          (harmony excitement venus )
          (harmony triumph vulcan )
          (locale guava oregon )
          (locale hotdog kentucky )
          (eats mutton hotdog )
          (eats chicken hotdog )
          (eats guava rice )
          (attacks arizona oregon )
          (eats hotdog hamburger )
          (eats muffin turkey )
          (eats cucumber hotdog )
          (eats rice baguette )
          (eats beef baguette )
          (craves triumph turkey )
          (attacks guanabara arizona )
          (eats turkey cucumber )
          (eats baguette beef )
          (eats scallop yogurt )
          (eats hamburger beef )
          (eats beef hotdog )
          (locale chicken surrey )
          (attacks alsace guanabara )
          (attacks manitoba surrey )
          (craves achievement guava )
          (locale turkey pennsylvania )
          (eats cucumber baguette )
          (eats chicken mutton )
          (eats baguette cucumber )
          (attacks moravia kentucky )
          (orbits neptune venus )
          (harmony expectation venus )
          (eats scallop rice )
          (locale yogurt bosnia )
          (attacks oregon bosnia )
          (eats chicken turkey )
          (attacks bosnia bavaria )
          (craves dread baguette )
          (attacks bavaria moravia )
          (eats hotdog beef )
          (harmony achievement vulcan )
          (craves abrasion guava )
          (attacks goias manitoba )
          (eats rice guava )
          (craves expectation hamburger )
          (eats cucumber turkey )
          (orbits vulcan neptune )
          (harmony intoxication venus )
          (eats yogurt guava )
          (eats hotdog chicken )
          (locale cucumber bavaria )
          (eats yogurt scallop )
          (orbits mars vulcan )
          (locale beef bavaria )
          (locale mutton goias )
          (locale scallop oregon )
          (locale rice guanabara )
          (craves laceration muffin )
          (attacks pennsylvania alsace )
          (eats hotdog mutton )
          (craves boils hamburger ) )
(:goal
	:tasks  (
			 (tag t1 (do_delivery dread mutton)) 
                (tag t2 (do_delivery boils mutton)) 
                (tag t3 (do_delivery anxiety chicken)) 
			
			
		)
	:constraints(and
			(after (and (craves dread mutton )
               (craves boils mutton )
               (craves anxiety chicken ) )
				 t1)
		)
))