(define (problem mysty-x-8 )
(:domain mystery-typed )
	(:requirements :strips :typing :negative-preconditions :htn :equality)

(:objects rice okra hotdog tofu muffin tuna pork cantelope turkey
             baguette arugula wonderbread pepper flounder lobster scallop
             tomato pistachio - food
             achievement love triumph stimulation learning lubricity rest
             aesthetics excitement - pleasure
             sciatica hangover anxiety abrasion anger - pain
             bavaria goias pennsylvania bosnia surrey - province
             earth uranus vulcan jupiter - planet )
(:init (harmony learning vulcan )
          (attacks bavaria goias )
          (eats turkey baguette )
          (eats muffin tuna )
          (eats flounder wonderbread )
          (harmony stimulation uranus )
          (eats wonderbread scallop )
          (craves excitement tomato )
          (eats tuna muffin )
          (eats pistachio arugula )
          (harmony achievement vulcan )
          (harmony rest jupiter )
          (locale pork bosnia )
          (craves hangover muffin )
          (eats rice tofu )
          (locale flounder goias )
          (eats cantelope arugula )
          (attacks goias pennsylvania )
          (eats pork lobster )
          (craves lubricity arugula )
          (orbits uranus vulcan )
          (eats okra hotdog )
          (locale tuna surrey )
          (craves sciatica okra )
          (craves anxiety muffin )
          (eats flounder lobster )
          (locale scallop pennsylvania )
          (eats tuna rice )
          (locale turkey goias )
          (craves love okra )
          (eats okra wonderbread )
          (locale tofu goias )
          (eats arugula pistachio )
          (eats rice tuna )
          (orbits vulcan jupiter )
          (craves abrasion baguette )
          (eats baguette turkey )
          (eats tomato scallop )
          (eats tuna okra )
          (attacks bosnia surrey )
          (locale tomato bosnia )
          (eats scallop tomato )
          (locale pepper goias )
          (eats hotdog okra )
          (craves triumph hotdog )
          (eats hotdog tofu )
          (locale cantelope pennsylvania )
          (eats muffin rice )
          (craves learning cantelope )
          (craves anger baguette )
          (eats muffin tofu )
          (craves rest wonderbread )
          (locale hotdog bavaria )
          (eats wonderbread flounder )
          (eats wonderbread okra )
          (eats tofu rice )
          (harmony love jupiter )
          (craves achievement rice )
          (eats baguette tomato )
          (harmony aesthetics vulcan )
          (locale rice bavaria )
          (eats tomato baguette )
          (eats tofu muffin )
          (eats tofu hotdog )
          (locale wonderbread goias )
          (eats arugula cantelope )
          (locale pistachio pennsylvania )
          (eats rice muffin )
          (eats scallop wonderbread )
          (eats pistachio pepper )
          (eats lobster pork )
          (eats lobster flounder )
          (eats pepper turkey )
          (locale lobster goias )
          (attacks pennsylvania bosnia )
          (orbits earth uranus )
          (eats pork cantelope )
          (locale baguette bosnia )
          (harmony excitement jupiter )
          (locale okra bosnia )
          (harmony triumph vulcan )
          (eats okra tuna )
          (eats cantelope pork )
          (eats turkey pepper )
          (harmony lubricity uranus )
          (locale muffin bavaria )
          (craves aesthetics scallop )
          (craves stimulation muffin )
          (eats pepper pistachio )
          (locale arugula bavaria ) )
(:goal
	:tasks  (
			 ;; A remplir 
               (tag t1 (do_catapulte  anxiety wonderbread))

			
		)
	:constraints(and
			(after (and 						(craves anxiety wonderbread ) )
				 t1)
		)
))