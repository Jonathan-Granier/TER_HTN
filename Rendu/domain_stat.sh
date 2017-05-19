#!/bin/bash

SCRIPT_PDDL="pddl.sh"

###################
#		Nom
###################
HTN="HTN"
PDDL="PDDL"
CORE="CORE"
FILE_TIME_STAT="Stats/Stat_Temps_de_resolution_$1.dat"
FILE_SIZE_PLAN_STAT="Stats/Stat_Taille_plan_$1.dat"
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

		echo -e -n "-1" >> $FILE_SIZE_PLAN_STAT

	else
		EXEC=$(sed -n '3p' $1)
		if [ "$EXEC" != "Plan valid" ] ; then
			echo -e -n "-1" >> $FILE_SIZE_PLAN_STAT

		else
			sed -i 's/Final value: //g' $1
			VALUE=$(sed -n '4p' $1)
			echo -e -n "$VALUE" >> $FILE_SIZE_PLAN_STAT
		fi
	fi

}



function test_un_probleme()
{

	TEMP=temp.pddl
	
	TIME_TOTAL="Temps(sec) :"

	echo -e "${NEUTRE} Probleme numero : $2"
	echo -e -n "$2 ; " >> $FILE_TIME_STAT
	echo -e -n "$2 ; " >> $FILE_SIZE_PLAN_STAT


	timeout $TIMEMAX ./$SCRIPT_PDDL -h $1 $2 > $TEMP
	RETVAL=$?
	if [ $RETVAL -eq 124 ]; then
		echo -e -n "-1" >> $FILE_SIZE_PLAN_STAT
		echo -e -n "-1" >> $FILE_TIME_STAT
	else
		sed -i -n '/seconds total time/p' $TEMP
		sed -i -r "s/                //g" $TEMP
		sed -i -r "s/ seconds total time//g" $TEMP
		TIME=$(sed -r "s/,/./g" $TEMP)
		echo -e -n "$TIME" >> $FILE_TIME_STAT
		
		
		./$SCRIPT_PDDL -vH $1 $2 > $TEMP
		Parse_Validateur $TEMP $HTN
	fi


	echo -e -n ";" >> $FILE_SIZE_PLAN_STAT
	echo -e -n ";" >> $FILE_TIME_STAT



	timeout $TIMEMAX ./$SCRIPT_PDDL -c $1 $2  > $TEMP
	RETVAL=$?
	if [ $RETVAL -eq 124 ]; then
		echo -e -n "-1" >> $FILE_SIZE_PLAN_STAT
		echo -e -n "-1" >> $FILE_TIME_STAT
	else
		sed -i -n '/Conjectures found in/p' $TEMP
		sed -i -r "s/ s://g" $TEMP
		sed -i -r "s/Conjectures found in//g" $TEMP
		TIME=$(sed -r "s/,/./g" $TEMP)
		echo -e -n "$TIME" >> $FILE_TIME_STAT
		

		./$SCRIPT_PDDL -vC $1 $2 > $TEMP
		Parse_Validateur $TEMP $CORE
	fi

	


	if [ $MAKE_PDDL -eq 1 ]; then
		echo -e -n ";" >> $FILE_SIZE_PLAN_STAT
		echo -e -n ";" >> $FILE_TIME_STAT
		timeout $TIMEMAX ./$SCRIPT_PDDL -p $1 $2 > $TEMP
		RETVAL=$?
		if [ $RETVAL -eq 124 ]; then
			echo -e -n "-1" >> $FILE_SIZE_PLAN_STAT
			echo -e -n "-1" >> $FILE_TIME_STAT
		else
			sed -i -n '/seconds total time/p' $TEMP
			sed -i -r "s/                  //g" $TEMP
			sed -i -r "s/seconds total time//g" $TEMP
			TIME=$(sed -r "s/,/./g" $TEMP)
			echo -e -n "$TIME" >> $FILE_TIME_STAT


			./$SCRIPT_PDDL -vP $1 $2 > $TEMP
			Parse_Validateur $TEMP $PDDL
		fi
	fi
	echo  "" >> $FILE_SIZE_PLAN_STAT
	echo  "" >> $FILE_TIME_STAT

	


}

if [ $MAKE_PDDL -eq 1 ]; then
	echo "No_Prob ; $HTN ; $CORE ; $PDDL " > $FILE_TIME_STAT
	echo "No_Prob ; $HTN ; $CORE ; $PDDL" > $FILE_SIZE_PLAN_STAT
else
	echo "No_Prob ; $HTN ; $CORE " > $FILE_TIME_STAT
	echo "No_Prob ; $HTN ; $CORE " > $FILE_SIZE_PLAN_STAT
fi

for fullfile in PDDL/$1/* 
	do
		#fullfile=PDDL/$1/p01.pddl
		file=$(basename $fullfile)
		if [ "${file::1}" = 'p' ]; then
			name=${file:1}
			name=${name%%.*}
			test_un_probleme $1 $name
		fi
	done



