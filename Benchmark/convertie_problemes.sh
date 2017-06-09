#!/bin/bash

#Convertie tous les problemes d'un domaine. Soit de PDDL à GTOHP , soit de GTOHP à CORE

function aide()
{
	echo "Script de convertion de probleme de planification"
	echo "Convertie tous les problemes d'un domaine, soit de PDDL à GTOHP , soit de GTOHP à CORE"
	echo
	echo "Usage : ./convertie_problemes.sh <Option> <Domaine>"
	echo " -g 		:		PDDL à GTOHP"
	echo " -c 		:		GTOHP à CORE"
	echo " -u 		:		tache unique : (tag t1 (do_problem ))"
	echo " --help 	: 		affiche l'aide"
}



if [ $# -eq 0 ] || [ $1 = "--help" ]; then
	aide

elif [ $1 = "-g" ] || [ $2 = "-g" ]; then
	if [ $# -eq 2 ]; then
		FOLDER=$2
	else
		FOLDER=$3
	fi
	if [ ! -d GTOHP/$FOLDER ]; then
		mkdir GTOHP/$FOLDER
		echo "Création du dossier GTOHP/$FOLDER"
	fi
	for fullfile in PDDL/$FOLDER/* 
	do
		file=$(basename $fullfile)
		if [ "${file::1}" = 'p' ]; then
			name=${file:1}
			echo $file
			if [ $1 = "-u" ] || [ $2 = "-u" ];then 
				./Parser/convertiseurPDDL_GTOHP_CORE -g -u $fullfile GTOHP/$FOLDER/htn_pb$name
			else
				./Parser/convertiseurPDDL_GTOHP_CORE -g $fullfile GTOHP/TAMER/htn_pb$name
			fi
		fi
	done

elif [ $1 = "-c" ]; then
	if [ ! -d Core/$2 ]; then
		mkdir Core/$2
		echo "Création du dossier Core/$2"
	fi
	for fullfile in GTOHP/$2/* 
	do
		file=$(basename $fullfile)
		if [ "${file::1}" = 'h' ]; then
			name=${file:6}
			name=${name%.*}
			echo $name
			./Parser/convertiseurPDDL_GTOHP_CORE -c $fullfile Core/$2/pb$name.jap
		fi
	done	
else
	aide
fi