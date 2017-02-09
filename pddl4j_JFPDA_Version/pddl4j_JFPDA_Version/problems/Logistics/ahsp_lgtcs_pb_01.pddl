
(define (problem pb1) 
  (:domain logistics)
  (:requirements :strips :typing :universal-preconditions :conditional-effects)
  (:objects p1 p2 - obj
            london paris - city
            truck - truck
            plane - airplane
            north south - location 
	    lhr cdg - airport)
  (:init (in-city cdg paris) 
  	 (in-city lhr london) 
	 (in-city north paris) 
	 (in-city south paris) 
	 (at plane lhr) 
	 (at truck cdg)
	 (at p1 lhr) 
	 (at p2 lhr))
(:goal (and (at p1 north) (at p2 south))))