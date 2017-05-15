#!/bin/bash

#Convertie tous les problemes d'un domaine. Soit de PDDL à HTN , soit de HTN à CORE

function aide()
{
	echo "Script de convertion de probleme de planification"
	echo "Convertie tous les problemes d'un domaine, soit de PDDL à HTN , soit de HTN à CORE"
	echo
	echo "Usage : ./convertie_problemes.sh <Option> <Domaine>"
	echo " -h 		:		PDDL à HTN"
	echo " -c 		:		HTN à CORE"
	echo " -u 		:		tache unique : (tag t1 (do_problem ))"
	echo " --help 	: 		affiche l'aide"
}



if [ $# -eq 0 ] || [ $1 = "--help" ]; then
	aide

elif [ $1 = "-h" ] || [ $2 = "-h" ]; then
	if [ $# -eq 2 ]; then
		FOLDER=$2
	else
		FOLDER=$3
	fi

	for fullfile in PDDL/$FOLDER/* 
	do
		file=$(basename $fullfile)
		if [ "${file::1}" = 'p' ]; then
			name=${file:1}
			echo $file
			if [ $1 = "-u" ] || [ $2 = "-u" ];then 
				./Parser/convertiseurPDDL_HTN_CORE -h -u $fullfile HTN/$FOLDER/htn_pb$name
			else
				./Parser/convertiseurPDDL_HTN_CORE -h $fullfile HTN/$FOLDER/htn_pb$name
			fi
		fi
	done

elif [ $1 = "-c" ]; then
	for fullfile in HTN/$2/* 
	do
		file=$(basename $fullfile)
		if [ "${file::1}" = 'h' ]; then
			name=${file:6}
			name=${name%.*}
			echo $name
			./Parser/convertiseurPDDL_HTN_CORE -c $fullfile Core/$2/pb$name.jap
		fi
	done	
else
	aide
fi