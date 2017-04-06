-------------------------------------------------
Dossier contenant:
 	-Les 3 versions du probleme de Barman
		-PDDL
		-HTN 
		-HTN_CORE
	-un script pour tester un probleme (faire .pddl.sh -h pour l'aide)
	-un Dossier contenant les differents plans obtenu après l'execution
		Note : les plans sont parsé de manière à passer dans le validateur : VAL-Master
	

Information sur le PDDL :
	On ne peut pas résoudre ce probleme avec le solver classique, en effet la complexitée est trop grande et le solver (pddl4J) pedale dans la semoulle

Information sur le HTN_CORE:
	Les plans sont plus long du fait que le solver va faire :
	 	leave left shot1
		grasp right shot1 
	à la suite.
	Ce probleme est corrigable en rajoutant des cas dans la methode : do_clean_shot et supprimant le fait de lacher le verre à la fin de la methode : get_second_ingredient
	
	Mais faire ceci va creer une difference entre les 2 domaines et ce probleme à pour avantage de montrer une faiblesse du HTN_CORE 

 
