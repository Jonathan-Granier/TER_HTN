#!/bin/bash

function aide()
{
	echo "Script de test de probleme de planification"
	echo "Une fois un probleme resolue, un plan est parsé puis stocké dans un fichier de manière à etre accepté dans le validateur VAL-master"
	echo
	echo "Usage : ./pddl.sh <Option> <Nom du Probleme> <Numero du probeme>"
	echo " -h 		:		version HTN"
	echo " -p 		:		version PDDL"
	echo " -c 		:		version Core_HTN"
	echo " -vH 		:		test un plan HTN"
	echo " -vC 		:		test un plan Core HTN"
	echo " --help 	: 		affiche l'aide"
}

#############################################################
######### A Modifier selon l'arboressance    ################
#############################################################
JAR_HTN=../pddl4j_JFPDA_Version/pddl4j_JFPDA_Version/lib/iSHOP.jar
JAR_CORE=../CoRe-Planner-1.0/CoRe-Planner-1.0/lib/planner_Vtest.jar
JAR_PDDL=../pddl4j-master/build/libs/pddl4j-3.5.0.jar
VAL=../VAL-master/validate
#############################################################


CHEMIN_PDDL=PDDL/$2


if [ $# -eq 0 ] || [ $1 = "--help" ]; then
	aide

elif [ $1 = "-h" ]; then
	CHEMIN=pddl4j_JFPDA_Version/pddl4j_JFPDA_Version
	TEMP=tmp.pddl
	PLAN=Plan/HTN/$2/plan_$3.pddl

	java -javaagent:$JAR_HTN -server -Xms8048m -Xmx8048m -classpath $JAR_HTN pddl4j.examples.ISHOP.ISHOP -o HTN/$2/domain.pddl -f HTN/$2/htn_pb$3.pddl | tee $TEMP

	#PARSE
	 
	sed -i -n '/Tasks:/,/Number of explored nodes:/p' $TEMP
	sed -i '1d' $TEMP
	sed -i '$d' $TEMP
	sed -i -r "s/\(([0-9]+)\)/[1]/g" $TEMP
	sed -i '/nop/d' $TEMP
	sed -i -r "s/[0-9]*://g" $TEMP
	awk '{ print NR -1 " : " $0 }' $TEMP > $PLAN
	sed -i '$d' $PLAN
	rm tmp.pddl


elif [ $1 = "-p" ]; then
	java -javaagent:$JAR_PDDL -server -Xms2048m -Xmx2048m fr.uga.pddl4j.planners.hsp.HSP -o $CHEMIN_PDDL/domain.pddl -f $CHEMIN_PDDL/p$3.pddl

elif [ $1 = "-vH" ]; then
	./$VAL $CHEMIN_PDDL/domain.pddl $CHEMIN_PDDL/p$3.pddl Plan/HTN/$2/plan_$3.pddl 

elif [ $1 = "-vC" ]; then
	./$VAL $CHEMIN_PDDL/domain.pddl $CHEMIN_PDDL/p$3.pddl Plan/Core/$2/plan_$3.pddl 

elif [ $1 = "-c" ]; then
	CHEMIN=CoRe-Planner-1.0/CoRe-Planner-1.0
	PLAN=Plan/Core/$2/plan_$3.pddl 

	TEMP=tmp.pddl
	java -jar $JAR_CORE -d Core/$2/$2.jap -p Core/$2/pb$3.jap | tee $TEMP
	rm $PLAN > /dev/null 2> /dev/null

	sed -i '1,8 d' $TEMP
	sed -i -r "s/[0-9]*\. \[ \(\) ,//g" $TEMP
	sed -i -r "s/]//g" $TEMP
	sed -i -r "s/!//g" $TEMP
	
	#numérote les lignes du fichier à partir de 0
	awk '{ print NR -1 " : " $0 }' $TEMP > $PLAN
	sed -i '$d' $PLAN
	
	rm $TEMP

else
	aide
fi

#\(([0-9]+)\)