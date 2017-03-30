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
	echo " --help 	: 		affiche l'aide"
}



if [ $# -eq 0 ] || [ $1 = "--help" ]; then
	aide

elif [ $1 = "-h" ]; then
	for fullfile in PDDL/$2/* 
	do
		file=$(basename $fullfile)
		if [ "${file::1}" = 'p' ]; then
			name=${file:1}
			echo $file
			./Parser/convertiseurPDDL_HTN_CORE -h $fullfile HTN/$2/htn_pb$name
		fi
	done

#A tester
elif [ $1 = "-c" ]; then
	for fullfile in HTN/$2/* 
	do
		file=$(basename $fullfile)
		if [ "${file::1}" = 'h' ]; then
			name=${file:6}
			name=${name%.*}
			echo $name
			./Parser/convertiseurPDDL_HTN_CORE -h $fullfile Core/$2/p$name.jap
		fi
	done	
else
	aide
fi