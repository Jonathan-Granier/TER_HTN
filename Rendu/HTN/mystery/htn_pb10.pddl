(define (problem mysty-x-10 )
(:domain mystery-typed )
	(:requirements :strips :typing :negative-preconditions :htn :equality)

(:objects tofu snickers popover lamb potato melon beef pork bacon
             orange ham kale pepper hamburger cantelope flounder
             grapefruit wurst arugula - food
             triumph stimulation expectation rest love satisfaction
             learning aesthetics intoxication - pleasure
             laceration depression loneliness hangover prostatitis angina
             anger grief anxiety sciatica jealousy boils dread
             loneliness-1 grief-2 abrasion dread-8 angina-3 sciatica-4 anger-7
             abrasion-5 hangover-6 jealousy-16 anxiety-12 prostatitis-13
             depression-14 boils-15 loneliness-9 anger-10 laceration-11
             angina-30 dread-31 boils-32 laceration-27 jealousy-28
             hangover-29 prostatitis-24 anxiety-25 grief-26 - pain
             surrey moravia quebec oregon alsace kentucky - province
             mercury mars neptune pluto - planet )
(:init (eats snickers potato )
          (eats wurst grapefruit )
          (attacks quebec oregon )
          (craves prostatitis-13 pepper )
          (eats flounder kale )
          (eats ham pork )
          (harmony triumph pluto )
          (eats arugula hamburger )
          (eats beef snickers )
          (craves expectation beef )
          (craves loneliness snickers )
          (craves satisfaction ham )
          (locale kale quebec )
          (locale ham alsace )
          (attacks moravia quebec )
          (eats beef ham )
          (craves laceration-11 hamburger )
          (eats arugula cantelope )
          (craves aesthetics flounder )
          (craves anger popover )
          (craves anxiety-25 arugula )
          (eats beef arugula )
          (craves boils-15 pepper )
          (eats pepper cantelope )
          (eats orange beef )
          (eats hamburger cantelope )
          (harmony satisfaction neptune )
          (craves jealousy melon )
          (eats hamburger wurst )
          (eats pork bacon )
          (eats kale flounder )
          (eats tofu lamb )
          (locale potato quebec )
          (eats popover melon )
          (craves stimulation melon )
          (locale grapefruit oregon )
          (harmony expectation pluto )
          (craves dread-31 cantelope )
          (craves anger-7 orange )
          (attacks surrey moravia )
          (eats kale arugula )
          (craves dread beef )
          (craves loneliness-9 hamburger )
          (craves hangover-6 ham )
          (craves grief lamb )
          (eats ham beef )
          (craves boils-32 cantelope )
          (eats cantelope hamburger )
          (eats arugula beef )
          (craves love orange )
          (eats pork ham )
          (locale tofu surrey )
          (craves depression snickers )
          (craves angina-30 cantelope )
          (craves jealousy-16 kale )
          (craves anxiety potato )
          (craves learning pepper )
          (locale orange quebec )
          (harmony aesthetics neptune )
          (harmony rest mars )
          (eats wurst hamburger )
          (eats cantelope arugula )
          (craves prostatitis popover )
          (eats lamb tofu )
          (craves intoxication grapefruit )
          (craves abrasion pork )
          (eats grapefruit pepper )
          (locale hamburger quebec )
          (locale snickers alsace )
          (locale pork kentucky )
          (eats cantelope flounder )
          (orbits neptune pluto )
          (craves boils melon )
          (eats beef orange )
          (eats orange pork )
          (harmony love mars )
          (craves hangover popover )
          (eats cantelope pepper )
          (eats bacon orange )
          (eats orange bacon )
          (craves grief-26 arugula )
          (craves angina-3 bacon )
          (eats potato melon )
          (harmony stimulation pluto )
          (craves laceration snickers )
          (craves sciatica melon )
          (orbits mercury mars )
          (craves jealousy-28 grapefruit )
          (harmony learning mars )
          (craves loneliness-1 pork )
          (craves laceration-27 grapefruit )
          (eats flounder cantelope )
          (craves depression-14 pepper )
          (eats popover tofu )
          (craves hangover-29 grapefruit )
          (eats bacon pork )
          (locale beef moravia )
          (craves rest pork )
          (eats melon potato )
          (locale bacon alsace )
          (eats snickers beef )
          (eats arugula kale )
          (locale cantelope quebec )
          (eats hamburger arugula )
          (locale flounder moravia )
          (eats tofu popover )
          (craves dread-8 bacon )
          (craves prostatitis-24 arugula )
          (craves abrasion-5 ham )
          (locale arugula moravia )
          (eats melon popover )
          (orbits mars neptune )
          (eats lamb snickers )
          (craves angina popover )
          (locale wurst alsace )
          (attacks alsace kentucky )
          (craves triumph tofu )
          (eats potato snickers )
          (attacks oregon alsace )
          (locale pepper oregon )
          (craves anxiety-12 pepper )
          (eats pork orange )
          (eats snickers lamb )
          (eats grapefruit wurst )
          (locale melon alsace )
          (craves grief-2 pork )
          (craves anger-10 hamburger )
          (craves sciatica-4 bacon )
          (locale popover oregon )
          (locale lamb moravia )
          (harmony intoxication neptune )
          (eats pepper grapefruit ) )
(:goal
	:tasks  (
			 ;; A remplir 
			(tag t1 (do_catapulte  jealousy-16 tofu))
			
		)
	:constraints(and
			(after (and 						(craves jealousy-16 tofu ) )
				 t1)
		)
))
