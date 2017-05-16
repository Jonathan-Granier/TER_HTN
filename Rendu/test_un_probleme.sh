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
################### 

###################
#	Debug
###################
MAKE_PDDL=0
TIMEMAX=300s
###################


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
		echo -e -n "\t${RED}$2 : $VALUE  " 
	else
		EXEC=$(sed -n '3p' $1)
		if [ "$EXEC" != "Plan valid" ] ; then
			VALUE=0
			echo -e -n "\t${ORANGE}$2 : $VALUE  "
		else
			sed -i 's/Final value: //g' $1
			VALUE=$(sed -n '4p' $1)

			echo -e -n "\t${GREEN}$2 : $VALUE  "
		fi
	fi

}



function test_un_probleme()
{

	TEMP=temp.pddl
	
	TIME_TOTAL="Temps(sec) :"

	echo -e "${NEUTRE} Probleme numero : $2"
	echo -e -n "\t"
	timeout $TIMEMAX ./$SCRIPT_PDDL -h $1 $2 > $TEMP
	RETVAL=$?
	if [ $RETVAL -eq 124 ]; then
		echo "timeout"
	else
		sed -i -n '/seconds total time/p' $TEMP
		sed -i -r "s/                //g" $TEMP
		TIME=$(sed -r "s/ seconds total time//g" $TEMP)
		TIME_TOTAL="$TIME_TOTAL \t$TIME"
	
		./$SCRIPT_PDDL -vH $1 $2 > $TEMP
		Parse_Validateur $TEMP $HTN
	fi

	timeout $TIMEMAX ./$SCRIPT_PDDL -c $1 $2  > $TEMP
	RETVAL=$?
	if [ $RETVAL -eq 124 ]; then
		echo "timeout"
	else
		sed -i -n '/Conjectures found in/p' $TEMP
		sed -i -r "s/ s://g" $TEMP
		TIME=$(sed -r "s/Conjectures found in//g" $TEMP)
		TIME_TOTAL="$TIME_TOTAL \t\t$TIME"

		./$SCRIPT_PDDL -vC $1 $2 > $TEMP
		Parse_Validateur $TEMP $CORE
	fi

	if [ $MAKE_PDDL -eq 1 ]; then
		timeout $TIMEMAX ./$SCRIPT_PDDL -p $1 $2 > $TEMP
		RETVAL=$?
		if [ $RETVAL -eq 124 ]; then
		echo "timeout"
		else
			sed -i -n '/seconds total time/p' $TEMP
			sed -i -r "s/                  //g" $TEMP
			TIME=$(sed -r "s/seconds total time//g" $TEMP)
			TIME_TOTAL="$TIME_TOTAL \t\t$TIME"


			./$SCRIPT_PDDL -vP $1 $2 > $TEMP
			Parse_Validateur $TEMP $PDDL
		fi
	fi

	#rm $TEMP
	echo ""
	echo -e "${NEUTRE} $TIME_TOTAL"
	echo "------------------------"


}

for fullfile in PDDL/$1/* 
	do
		file=$(basename $fullfile)
		if [ "${file::1}" = 'p' ]; then
			name=${file:1}
			name=${name%%.*}
			test_un_probleme $1 $name
		fi
	done
