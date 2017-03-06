#!/bin/bash

SCRIPT_PDDL="pddl.sh"

###################
#		Nom
###################
HTN="HTN"
PDDL="PDDL"
CORE="CORE"
###################


###################
#	Couleur 
###################
RED='\e[0;31m'
GREEN='\e[1;32m'
ORANGE='\e[0;33m'
NEUTRE='\e[0;m'
###################        Plan executed successfully - checking goal



############################################################################
# Fonction Parse_Validateur
#	Input : 	
#		$1  : Fichier contenant le retour d'un validateur
#		$2  : Nom de la methode qui est a été utilisé 
#	
#	Effet :
#		Affiche de maniere plus lisible le retour du validateur
#				<Nom_Methode> : <Taille_du_Plan> 		
#		Code couleur
#			Rouge : Erreur dans la description du plan 
#			Orange : L'execution du plan ne permet d'atteindre l'état attendu
#			Vert : L'état attendu a été obtenu , on est content
############################################################################

function Parse_Validateur()
{
	EXEC=$(sed -n '2p' $1) 
	if [ "$EXEC" != "Plan executed successfully - checking goal" ]; then
		VALUE=0
		echo -e -n "${RED} $2 : $VALUE  " 
	else
		EXEC=$(sed -n '3p' $1)
		if [ "$EXEC" != "Plan valid" ] ; then
			VALUE=0
			echo -e -n "${ORANGE} $2 : $VALUE  "
		else
			sed -i 's/Final value: //g' $1
			VALUE=$(sed -n '4p' $1)

			echo -e -n " ${GREEN} $2 : $VALUE  "
		fi
	fi

}


function test_un_probleme()
{

	TEMP=tmp.pddl

	echo -e "${NEUTRE} Probleme numero : $2"
	echo -e -n "\t"
	./$SCRIPT_PDDL -h $1 $2 > /dev/null 2> /dev/null
	./$SCRIPT_PDDL -vH $1 $2 > $TEMP
	Parse_Validateur $TEMP $HTN


	./$SCRIPT_PDDL -c $1 $2  > /dev/null 2> /dev/null
	./$SCRIPT_PDDL -vC $1 $2 > $TEMP
	Parse_Validateur $TEMP $CORE

	./$SCRIPT_PDDL -p $1 $2 > /dev/null 2> /dev/null
	./$SCRIPT_PDDL -vP $1 $2 > $TEMP
	Parse_Validateur $TEMP $PDDL

	#rm $TEMP
	echo ""


}

for i in `seq 01 20` ;
do
	if [ $i -lt 10 ]; then

		i="0$i"
	fi
	test_un_probleme $1 $i
done
