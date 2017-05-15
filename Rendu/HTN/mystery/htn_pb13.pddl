(define (problem mysty-x-13 )
(:domain mystery-typed )
	(:requirements :strips :typing :negative-preconditions :htn :equality)

(:objects lettuce chocolate mutton marzipan scallop yogurt rice
             haroset muffin potato turkey ham onion melon wurst flounder
             baguette scallion hamburger papaya lobster lemon - food
             entertainment intoxication satisfaction achievement curiosity love
             aesthetics - pleasure
             anger jealousy hangover grief abrasion loneliness anxiety
             boils sciatica dread angina laceration depression grief-1
             anxiety-2 prostatitis laceration-7 jealousy-8 boils-3 dread-4
             angina-14 hangover-15 sciatica-16 depression-5 anger-6
             loneliness-11 abrasion-12 prostatitis-13 anxiety-9 sciatica-10
             loneliness-32 prostatitis-29 laceration-30 hangover-31 dread-24
             grief-25 abrasion-26 boils-27 depression-28 anger-21
             jealousy-22 angina-23 loneliness-17 prostatitis-18
             depression-19 laceration-20 - pain
             oregon alsace bavaria quebec - province
             mars neptune vulcan venus - planet )
(:init (craves depression rice )
          (craves dread rice )
          (craves prostatitis-18 lemon )
          (locale melon alsace )
          (eats lobster marzipan )
          (craves laceration-7 potato )
          (craves loneliness-32 flounder )
          (eats marzipan lobster )
          (locale ham oregon )
          (craves anger mutton )
          (craves satisfaction rice )
          (eats rice lettuce )
          (locale marzipan oregon )
          (eats lobster baguette )
          (eats turkey potato )
          (eats wurst ham )
          (eats mutton marzipan )
          (craves achievement turkey )
          (eats baguette lobster )
          (eats papaya hamburger )
          (locale chocolate bavaria )
          (craves sciatica-16 turkey )
          (eats baguette lemon )
          (craves abrasion-26 scallion )
          (locale baguette bavaria )
          (eats hamburger papaya )
          (eats scallop turkey )
          (orbits vulcan venus )
          (eats marzipan mutton )
          (locale yogurt alsace )
          (craves prostatitis-29 baguette )
          (craves grief-1 muffin )
          (craves laceration-30 baguette )
          (craves jealousy-8 potato )
          (harmony love venus )
          (eats flounder scallion )
          (craves dread-24 scallion )
          (craves intoxication yogurt )
          (craves boils-27 scallion )
          (locale lemon quebec )
          (orbits neptune vulcan )
          (craves loneliness scallop )
          (eats hamburger scallion )
          (locale muffin bavaria )
          (locale scallop oregon )
          (eats haroset wurst )
          (eats turkey onion )
          (harmony curiosity vulcan )
          (craves grief-25 scallion )
          (eats melon haroset )
          (eats flounder lemon )
          (harmony satisfaction neptune )
          (craves entertainment mutton )
          (craves abrasion scallop )
          (eats potato muffin )
          (craves hangover-31 baguette )
          (eats lettuce rice )
          (eats turkey scallop )
          (eats lettuce chocolate )
          (craves grief scallop )
          (craves aesthetics hamburger )
          (eats lemon onion )
          (eats yogurt scallop )
          (eats lemon flounder )
          (eats chocolate lettuce )
          (locale onion alsace )
          (eats onion lemon )
          (eats haroset melon )
          (craves love baguette )
          (harmony intoxication vulcan )
          (locale flounder quebec )
          (craves curiosity ham )
          (locale wurst bavaria )
          (craves abrasion-12 ham )
          (craves dread-4 potato )
          (eats mutton scallop )
          (craves hangover scallop )
          (locale turkey bavaria )
          (eats rice marzipan )
          (craves anxiety-9 melon )
          (craves laceration rice )
          (craves anger-6 turkey )
          (eats scallion flounder )
          (locale potato alsace )
          (harmony achievement venus )
          (craves jealousy marzipan )
          (craves prostatitis-13 ham )
          (locale lobster alsace )
          (eats muffin ham )
          (craves depression-28 scallion )
          (craves angina-23 hamburger )
          (craves sciatica rice )
          (craves boils yogurt )
          (eats scallop yogurt )
          (locale rice quebec )
          (eats papaya lobster )
          (attacks bavaria quebec )
          (attacks alsace bavaria )
          (attacks oregon alsace )
          (craves laceration-20 lemon )
          (craves boils-3 potato )
          (craves loneliness-11 ham )
          (craves jealousy-22 hamburger )
          (craves anxiety yogurt )
          (locale mutton quebec )
          (craves hangover-15 turkey )
          (eats yogurt chocolate )
          (craves angina rice )
          (eats ham muffin )
          (eats scallop mutton )
          (locale lettuce alsace )
          (eats onion melon )
          (eats ham wurst )
          (eats chocolate yogurt )
          (eats wurst haroset )
          (eats potato turkey )
          (locale scallion alsace )
          (craves depression-5 turkey )
          (craves prostatitis muffin )
          (harmony entertainment venus )
          (craves depression-19 lemon )
          (locale papaya bavaria )
          (eats melon onion )
          (craves anger-21 hamburger )
          (eats onion turkey )
          (eats muffin potato )
          (locale hamburger bavaria )
          (craves angina-14 turkey )
          (eats lobster papaya )
          (eats marzipan rice )
          (locale haroset oregon )
          (craves loneliness-17 lemon )
          (harmony aesthetics neptune )
          (orbits mars neptune )
          (craves anxiety-2 muffin )
          (eats scallion hamburger )
          (craves sciatica-10 melon )
          (eats lemon baguette ) )
(:goal
	:tasks  (
			 ;; A remplir 
	              (tag t1 (do_catapulte  prostatitis-18 lobster))
                   (tag t2 (do_catapulte  laceration-20 ham))	
			
		)
	:constraints(and
			(after (and 						(craves prostatitis-18 lobster )
               						(craves laceration-20 ham ) )
				 t1)
		)
))
