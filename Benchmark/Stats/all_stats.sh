#!/bin/bash

# Lance le script R pour chaque domaine.

for fullfile in Data/Stat_Taille_plan_*
do
	file=$(basename $fullfile)
	file=${file:17}
	file=${file%%.*}
	echo $file
	./Script_R.R $file >> /dev/null
done