(define (problem mysty-x-25 )
(:domain mystery-typed )
	(:requirements :strips :typing :negative-preconditions :htn :equality)

(:objects wurst tuna pistachio chicken - food
             expectation rest - pleasure
             depression angina - pain
             bosnia kentucky bavaria pennsylvania surrey moravia
             - province
             jupiter uranus neptune earth - planet )
(:init (eats wurst chicken )
          (eats tuna pistachio )
          (craves angina chicken )
          (eats chicken pistachio )
          (craves rest pistachio )
          (locale tuna bavaria )
          (eats chicken wurst )
          (harmony expectation uranus )
          (orbits jupiter uranus )
          (craves expectation tuna )
          (attacks kentucky bavaria )
          (craves depression wurst )
          (eats pistachio wurst )
          (attacks bosnia kentucky )
          (orbits neptune earth )
          (eats tuna wurst )
          (locale wurst bavaria )
          (eats pistachio tuna )
          (attacks pennsylvania surrey )
          (eats wurst tuna )
          (harmony rest earth )
          (orbits uranus neptune )
          (eats wurst pistachio )
          (eats pistachio chicken )
          (attacks surrey moravia )
          (attacks bavaria pennsylvania )
          (locale chicken bavaria )
          (locale pistachio moravia ) )
(:goal
	:tasks  (
			 ;; A remplir 
			     (tag t1 (do_catapulte  depression chicken  ))
			
		)
	:constraints(and
			(after (and 						(craves depression chicken ) )
				 t1)
		)
))
