(define (problem mysty-x-28 )
(:domain mystery-typed )
	(:requirements :strips :typing :negative-preconditions :htn :equality)

(:objects wurst shrimp muffin broccoli lamb - food
             intoxication - pleasure
             loneliness anxiety hangover anger angina boils grief - pain
             goias bosnia pennsylvania moravia quebec oregon alsace
             kentucky - province
             neptune vulcan earth - planet )
(:init (craves hangover shrimp )
          (attacks moravia quebec )
          (craves anger muffin )
          (eats lamb shrimp )
          (eats shrimp wurst )
          (eats wurst muffin )
          (craves intoxication wurst )
          (attacks goias bosnia )
          (eats broccoli lamb )
          (eats wurst lamb )
          (eats muffin wurst )
          (craves boils broccoli )
          (locale shrimp alsace )
          (attacks quebec oregon )
          (eats broccoli muffin )
          (locale lamb kentucky )
          (attacks alsace kentucky )
          (eats shrimp lamb )
          (orbits vulcan earth )
          (craves anxiety shrimp )
          (locale broccoli kentucky )
          (harmony intoxication earth )
          (craves grief lamb )
          (locale wurst bosnia )
          (orbits neptune vulcan )
          (eats wurst shrimp )
          (locale muffin pennsylvania )
          (attacks pennsylvania moravia )
          (eats lamb wurst )
          (attacks oregon alsace )
          (eats lamb broccoli )
          (craves angina muffin )
          (craves loneliness wurst )
          (eats muffin broccoli )
          (attacks bosnia pennsylvania ) )
(:goal
	:tasks  (
			 ;; A remplir 
			     (tag t1 (do_catapulte  anger lamb  ))
           (tag t2 (do_catapulte  boils lamb  ))
			
		)
	:constraints(and
			(after (and 						(craves anger lamb )
               						(craves boils lamb ) )
				 t1)
		)
))
