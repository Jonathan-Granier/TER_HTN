(define (problem mysty-x-1 )
(:domain mystery-typed )
	(:requirements :strips :typing :negative-preconditions :htn :equality)

(:objects rice pear flounder okra pork lamb - food
             rest - pleasure
             hangover depression abrasion - pain
             kentucky bosnia surrey pennsylvania alsace quebec guanabara
             - province
             mars earth uranus venus - planet )
(:init      
            
            (eats rice rice )
            (eats rice flounder )
            (eats rice pear )

            (eats pork okra )
            (eats pork lamb )

            (eats lamb flounder )
            (eats lamb pork )
            
            (eats okra pear )
            (eats okra pork )
           
            (eats pear okra )
            (eats pear rice )
            
            (eats flounder rice )
            (eats flounder lamb )

           
           
            (locale okra guanabara )
            (locale pork quebec )
            (locale rice bosnia )
            (locale lamb pennsylvania )
            (locale flounder alsace ) 
            (locale pear surrey )

            (harmony rest venus )

            (craves depression flounder )
            (craves abrasion pork )
            (craves rest pork )
            (craves hangover rice )
            
            (attacks kentucky bosnia )
            (attacks bosnia surrey )
            (attacks surrey pennsylvania )
            (attacks pennsylvania alsace )
            (attacks alsace quebec )
            (attacks quebec guanabara )
            
           

            (orbits mars earth )
            (orbits earth uranus )
            (orbits uranus venus )

            
           

            

           
)
           
           
(:goal
	:tasks  (
			  (tag t1 (do_catapulte abrasion rice))
			
			
		)
	:constraints(and
			(after (and 						;(craves abrasion rice )
             )
				 t1)
		)
))
