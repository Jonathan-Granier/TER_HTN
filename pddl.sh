#!/bin/bash

function aide()
{
	echo "Usage : ./pddl.sh <Option> <Nom du Probleme> <Numero du probeme>"
	echo " -h 	:		version HTN"
	echo " -j 	:		version PDDL"
	echo " -v 	:		test un plan"
	echo " --help : 	affiche l'aide"
}


if [ $# -eq 0 ] || [ $1 = "--help" ]; then
	aide

elif [ $1 = "-h" ]; then
	CHEMIN=pddl4j_JFPDA_Version/pddl4j_JFPDA_Version
	java -javaagent:$CHEMIN/lib/iSHOP.jar -server -Xms8048m -Xmx8048m -classpath $CHEMIN/lib/iSHOP.jar pddl4j.examples.ISHOP.ISHOP -o $CHEMIN/problems/$2/domain.pddl -f $CHEMIN/problems/$2/htn_pb$3.pddl | tee tmp.pddl

	#PARSE
	PLAN=Plan/$2/plan_$3.pddl 
	sed -n '/Tasks:/,/Number of explored nodes:/p' tmp.pddl > $PLAN
	sed -i '1d' $PLAN
	sed -i '$d' $PLAN
	sed -i -r "s/\(([0-9]+)\)/[1]/g" $PLAN
	rm tmp.pddl


elif [ $1 = "-j" ]; then
	CHEMIN=seq-agl/$2
	java -javaagent:pddl4j-master/build/libs/pddl4j-3.5.0.jar -server -Xms2048m -Xmx2048m fr.uga.pddl4j.planners.hsp.HSP -o $CHEMIN/domain.pddl -f $CHEMIN/p$3.pddl

elif [ $1 = "-v" ]; then
	CHEMIN=seq-agl/$2
	./VAL-master/validate $CHEMIN/domain.pddl $CHEMIN/p$3.pddl Plan/$2/plan_$3.pddl 

else
	aide
fi

#\(([0-9]+)\)