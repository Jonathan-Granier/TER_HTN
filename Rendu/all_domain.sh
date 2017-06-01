#!/bin/bash

for fullfolder in PDDL/* 
do
	#echo $fullfolder
	folder=$(basename $fullfolder)
	echo $folder
	./test_un_domain_complet.sh $folder
done