(defproblem baker-problem baker
 	(
		;; static properties
		(agent-name baker)
		(is-built bakery baker-shop baker shop-village)
		(is-settled baker baker-shop shop-village)
		(bread-price baker 1)
		
		;; dynamic properties
		(has-flour baker 4)
		(has-bread baker 1)
		(has-cash baker 2)
		
	)
	((achieve-goals (list 
		//(is-settled baker baker-shop village)
		(has-bread miner 4)
		(is-available bread miner montain-village)
		//(receive-cash miller baker 2 flour)
		//(receive-cash construction-worker baker 2 bakery)
	))
))
