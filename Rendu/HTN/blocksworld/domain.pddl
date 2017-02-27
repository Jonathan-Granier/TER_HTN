;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; BlocksWorld , version HTN
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(define (domain BLOCKS)
  (:requirements :strips :typing :htn :negative-preconditions :equality)
  (:types block)
  (:predicates (on ?x - block ?y - block)
	       (ontable ?x - block)
	       (clear ?x - block)
	       (handempty)
	       (holding ?x - block)
	       )

  (:action pick-up
	     :parameters (?x - block)
	     :precondition (and (clear ?x) (ontable ?x) (handempty))
	     :effect
	     (and (not (ontable ?x))
		   (not (clear ?x))
		   (not (handempty))
		   (holding ?x)))

  (:action put-down
	     :parameters (?x - block)
	     :precondition (holding ?x)
	     :effect
	     (and (not (holding ?x))
		   (clear ?x)
		   (handempty)
		   (ontable ?x)))
 
  (:action stack
	     :parameters (?x - block ?y - block)
	     :precondition (and (holding ?x) (clear ?y))
	     :effect
	     (and (not (holding ?x))
		   (not (clear ?y))
		   (clear ?x)
		   (handempty)
		   (on ?x ?y)))
 
  (:action unstack
	     :parameters (?x - block ?y - block)
	     :precondition (and (on ?x ?y) (clear ?x) (handempty))
	     :effect
	     (and (holding ?x)
		   (clear ?y)
		   (not (clear ?x))
		   (not (handempty))
		   (not (on ?x ?y))))
  (:action nop
	     :parameters ()
	     :precondition (and )
	     :effect (and )
	)      


;;; Methods
;;;;;;;;;;;;;;;;;

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; 	Mettre un block sur un autre
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


	;; Mettre X sur Y 
	;; Cas ou X est Y 
	

	;; TODO a revoir peut-etre , si il faut mettre un pile de 2 block sur le 
	(:method do_put_on
		:parameters (?x - block ?y - block)
		:expansion (
						(tag t1 (nop))
					)
		:constraints
					( and
						(before 
							(on ?x ?y)
						t1
						)
					)


	)

;; Cas ou X est sur Y mais X doit aller sur la table
	(:method do_put_on
		:parameters (?x - block ?y - block)
		:expansion (
						(tag t1 (do_clear ?x))
				    	(tag t2 (do_clear ?y))
				    	(tag t3 (do_on_table ?y))
				    	(tag t4 (do_move ?x ?y))
					)
		:constraints
					( and
						(before 
							( and 
								(on ?x ?y)
								( not (ontable ?y))
							)
						t1
						)
					)


	)



	;; Cas ou X n'est pas sur Y
	(:method do_put_on
		:parameters	(?x - block ?y - block)
		:expansion	(
						(tag t1 (do_clear ?x))
				    	(tag t2 (do_clear ?y))
				    	(tag t3 (do_on_table ?y))
				    	(tag t4 (do_move ?x ?y))
			    	)
		:constraints
					( and 
			  			( before 	( and 
			  						( handempty ) 
			  						( not(on ?x ?y) )
			  						)
			  						t1
			  			)
			     	  	( between ( and 
			     	  				(handempty) 
			     	  				(clear ?x)
			     	  				)
			     	  				t1 t2
			     	  	)
			     	  	( between ( and 
			     	  				(handempty) 
			     	  				(clear ?x) 
			     	  				(clear ?y)
			     	  				) 
			     	  				t2 t3
			     	  	)
			     	)

	)
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;		 Mettre sur la table 
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; 2 Choix en fonction du resultat attendu
	;; Met sur la table le block x qui n'est pas déjà sur la table
	(:method do_on_table
		:parameters (?x - block)
		:expansion	(
						
						(tag t1 (unstack ?x ?y))
						(tag t2 (put-down ?x))
					)
		:constraints
					( and 
						( before
							( and 
								(clear ?x)
								(handempty)
								( not (ontable ?x))
							)
							t1
						)
					)
	)

	;; Ne fait rien 
	(:method do_on_table
		:parameters (?x - block)
		:expansion	(
						(tag t1 (nop))
					)
		:constraints
					( and 
						( before
							( and 
								(clear ?x)
							)
							t1
						)
					)
	)


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;	Bouger un cube sur un autre
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Bouge un cube X sur Y
	;; Si X est sur la table

	(:method do_move 
		:parameters	(?x - block ?y - block)
		:expansion	(
						(tag t1 (pick-up ?x))
						(tag t2 (stack ?x ?y))
			    	)
		:constraints
					( and 
			  			( before
			  				( and  

				  			(clear ?x)
				  			(clear ?y)
				  			(handempty)
				  			(ontable ?x)
				  			)
				  			t1
				  		)
			     	)

	)

	;; Si X est sur un Block
	(:method do_move 
		:parameters	(?x - block ?y - block)
		:expansion	(
						(tag t1 (unstack ?x ?z))
						(tag t2 (stack ?x ?y))
			    	)
		:constraints
					( and 
			  			( before 
			  				( and
			  				(clear ?x)
			  				(clear ?y)
			  				(handempty)
			  				( not (ontable ?x))
			  				)
			  			t1
			  			)
			     	)
	)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;		 Nettoie un cube
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


;; Nettoie un Cube X
;; cas ou X est nettoyé
	(:method do_clear
		:parameters (?x - block)
		:expansion 	(
						(tag t1 (nop))
					)
		:constraints
					(
						and 
						( before
							(clear ?x) 
						t1
						)


					)


	)

;; Cas ou X n'est pas nettoyé
	(:method do_clear
		:parameters (?x - block)
		:expansion 	(
						
						(tag t1 (do_clear ?y))
						(tag t2 (unstack ?y ?x))
						(tag t3 (put-down ?y))
					)
		:constraints
					(
						and 
						( before
							( and 
							(not (clear ?x))
							(on ?y ?x)
							(handempty) 
							)
						t1
						)


					)


	)
)