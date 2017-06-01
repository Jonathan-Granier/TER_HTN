#!/bin/bash

function aide()
{
	echo "Script de test de probleme de planification"
	echo "Une fois un probleme resolue, un plan est parsé puis stocké dans un fichier de manière à etre accepté dans le validateur VAL-master"
	echo
	echo "Usage : ./pddl.sh <Option> <Nom du Probleme> <Numero du probeme>"
	echo " -h 		:		version HTN"
	echo " -p 		:		version PDDL FastDownward"
	echo " -p4J 	:		version PDDL4J"
	echo " -c 		:		version Core_HTN"
	echo " -vh 		:		test un plan PDDL"
	echo " -vh 		:		test un plan HTN"
	echo " -vc 		:		test un plan Core HTN"
	echo " --help 	: 		affiche l'aide"
}

#############################################################
######### A Modifier selon l'arboressance    ################
#############################################################
JAR_HTN=../pddl4j_JFPDA_Version/pddl4j_JFPDA_Version/lib/iSHOP.jar
JAR_CORE=../CoRe-Planner-1.0/CoRe-Planner-1.0/lib/planner_Vtest.jar
JAR_PDDL4J=../pddl4j-master/build/libs/pddl4j-3.5.0.jar
FASTDOWNWARD=../FastDownward/fast-downward.py
VAL=../VAL-master/validate
#############################################################


CHEMIN_PDDL=PDDL/$2
TEMP=tmp.pddl

if [ $# -eq 0 ] || [ $1 = "--help" ]; then
	aide

# HTN / GTOP
elif [ $1 = "-h" ]; then
	CHEMIN=pddl4j_JFPDA_Version/pddl4j_JFPDA_Version
	
	PLAN=Plan/HTN/$2/plan_$3.pddl

	if [ ! -d Plan/HTN/$2 ]; then
		mkdir Plan/HTN/$2
		echo "Création du dossier Plan/HTN/$2"
	fi



	java -javaagent:$JAR_HTN -server -Xms6048m -Xmx6048m -classpath $JAR_HTN pddl4j.examples.ISHOP.ISHOP -o HTN/$2/domain.pddl -f HTN/$2/htn_pb$3.pddl | tee $TEMP

	#PARSE
	 
	sed -i -n '/Tasks:/,/Number of explored nodes:/p' $TEMP
	sed -i '1d' $TEMP
	sed -i '$d' $TEMP
	sed -i -r "s/\(([0-9]+)\)/[1]/g" $TEMP
	
	#SUPPRIME LES TACHES "NOP" "VISIT" ET "UNVISIT"
	sed -i '/nop/d' $TEMP
	sed -i '/(visit /d' $TEMP
	sed -i '/(unvisit /d' $TEMP
	sed -i -r "s/[0-9]*://g" $TEMP
	awk '{ print NR -1 " : " $0 }' $TEMP > $PLAN
	sed -i '$d' $PLAN
	rm tmp.pddl

# HTN / CORE
elif [ $1 = "-c" ]; then
	CHEMIN=CoRe-Planner-1.0/CoRe-Planner-1.0
	PLAN=Plan/Core/$2/plan_$3.pddl 


	if [ ! -d Plan/Core/$2 ]; then
		mkdir Plan/Core/$2
		echo "Création du dossier Plan/Core/$2"
	fi

	java -jar $JAR_CORE -f -d Core/$2/$2.jap -p Core/$2/pb$3.jap | tee $TEMP
	rm $PLAN > /dev/null 2> /dev/null

	#PARSE
	sed -i '1,8 d' $TEMP
	sed -i -r "s/[0-9]*\. \[ \(\) ,//g" $TEMP
	sed -i -r "s/]//g" $TEMP
	sed -i -r "s/!//g" $TEMP
	sed -i '/(visit /d' $TEMP
	sed -i '/(unvisit /d' $TEMP
	
	#numérote les lignes du fichier à partir de 0
	awk '{ print NR -1 " : " $0 }' $TEMP > $PLAN
	sed -i '$d' $PLAN
	
	rm $TEMP


# PDDL / Fastdownwar
elif [ $1 = "-p" ]; then
	PLAN=Plan/PDDL/$2/plan_$3.pddl
	
	if [ ! -d Plan/PDDL/$2 ]; then
		mkdir Plan/PDDL/$2
		echo "Création du dossier Plan/PDDL/$2"
	fi


	./$FASTDOWNWARD --plan-file plan_$3.pddl $CHEMIN_PDDL/domain.pddl $CHEMIN_PDDL/p$3.pddl --search "astar(ipdb())"
	mv plan_$3.pddl $PLAN
	#sed -i -n '/Actual search time:/,/Plan length:/p' $PLAN
	#sed -i '1d' $PLAN
	sed -i '$d' $PLAN
	rm output.sas
	

# PDDL / pddl4J
elif [ $1 = "-p4J" ]; then
	PLAN=Plan/PDDL/$2/plan_$3.pddl
	java -javaagent:$JAR_PDDL4J -server -Xms6048m -Xmx6048m fr.uga.pddl4j.planners.hsp.HSP -o $CHEMIN_PDDL/domain.pddl -f $CHEMIN_PDDL/p$3.pddl | tee $PLAN

	if [ ! -d Plan/PDDL/$2 ]; then
		mkdir Plan/PDDL/$2
		echo "Création du dossier Plan/PDDL/$2"
	fi



	sed -i -n '/found plan as follows:/,/time spent:/p' $PLAN
	sed -i '1,2 d' $PLAN
	sed -i '$d' $PLAN

# Validateur HTN / GTOP
elif [ $1 = "-vh" ]; then
	./$VAL $CHEMIN_PDDL/domain.pddl $CHEMIN_PDDL/p$3.pddl Plan/HTN/$2/plan_$3.pddl 

# Validateur HTN / CORE
elif [ $1 = "-vc" ]; then
	./$VAL $CHEMIN_PDDL/domain.pddl $CHEMIN_PDDL/p$3.pddl Plan/Core/$2/plan_$3.pddl 

# Validateur PDDL
elif [ $1 = "-vp" ]; then
	./$VAL $CHEMIN_PDDL/domain.pddl $CHEMIN_PDDL/p$3.pddl Plan/PDDL/$2/plan_$3.pddl 



else
	aide
fi

