#!/bin/bash

#Execute tous les domaines et stock les données recuperer dans stats/data

for fullfolder in PDDL/* 
do
	folder=$(basename $fullfolder)
	echo $folder
	./test_un_domain_complet.sh $folder
done