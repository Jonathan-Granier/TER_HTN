(define (problem mysty-x-7 )
(:domain mystery-typed )
	(:requirements :strips :typing :negative-preconditions :htn :equality)

(:objects mutton haroset popover shrimp snickers pepper pea
             muffin lobster pear - food
             stimulation learning - pleasure
             grief depression anger loneliness angina jealousy hangover
             boils dread sciatica abrasion prostatitis laceration
             anxiety dread-3 depression-4 abrasion-1 loneliness-2
             angina-8 sciatica-16 grief-5 anger-6 prostatitis-7 boils-15
             - pain
             arizona manitoba moravia surrey - province
             mercury saturn - planet )
(:init (craves angina-8 muffin )

        (eats mutton muffin )
        (eats mutton pea )

        (eats muffin popover )
        (eats muffin lobster )
        (eats muffin mutton )

        (eats pea mutton )
        (eats pea pepper )
        (eats pea snickers )
          
        (eats pepper pea )
        (eats pepper pear )
        (eats pepper shrimp )

        (eats snickers shrimp )
        (eats snickers popover )
        (eats snickers pea )
        
        (eats haroset lobster )
        (eats haroset shrimp )

        (eats popover lobster )
        (eats popover muffin )
        (eats popover snickers )

        (eats shrimp snickers )
        (eats shrimp pepper )
        (eats shrimp haroset )
        
        (eats pear lobster )
        (eats pear pepper )

        (eats lobster muffin )
        (eats lobster pear )
        (eats lobster popover )

        (locale muffin arizona )
        (attacks manitoba moravia )
        (craves boils-15 pear )
        (craves anger mutton )
        (craves abrasion snickers )

        (locale pea moravia )

        (harmony stimulation saturn )
        (craves depression-4 pea )
        (locale popover manitoba )
        (craves abrasion-1 pea )
        
        (locale shrimp moravia )

        (craves hangover shrimp )
        (craves learning pea )
        (craves angina haroset )
        (eats lobster haroset )
       
        (craves stimulation snickers )
        
        (craves laceration pepper )
        
        (craves sciatica-16 lobster )
        (craves sciatica snickers )
        (craves grief-5 lobster )
        (craves anger-6 lobster )
        (locale snickers arizona )
        
        (craves jealousy popover )
        
        (craves anxiety pepper )
        (locale haroset arizona )
        (orbits mercury saturn )
        (craves loneliness-2 pea )
        (locale pepper manitoba )
       
        (craves grief mutton )
       
        (locale mutton arizona )
        
        (craves loneliness haroset )
        
        (craves dread-3 pea )
        
        (craves dread snickers )
        
        (craves prostatitis snickers )
        (craves prostatitis-7 lobster )
        (locale lobster surrey )
        (harmony learning saturn )
       
        (attacks moravia surrey )
        (attacks arizona manitoba )
        
        (craves depression mutton )
        (locale pear arizona )
       
        (craves boils shrimp ) )
(:goal
	:tasks  (
			 ;; A remplir 
               (tag t1 (do_catapulte  jealousy muffin))

			
		)
	:constraints(and
			(after (and 						;(craves jealousy muffin )
             )
				 t1)
		)
))
