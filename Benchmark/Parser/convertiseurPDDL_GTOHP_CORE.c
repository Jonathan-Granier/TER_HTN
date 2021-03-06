#include <stdlib.h>
#include <stdio.h>
#include <string.h>

#define TAILLE_TEMP 100000
#define NB_OBJECT_MAX 200
#define NB_TYPE_MAX 100
#define SIZE_OBJECT_NAME 100

void get_under_parenthesis(FILE *src, FILE *dst,int is_Core_Init,char *separator);
void go_to_next_parenthesis(FILE *src);
void pddl_to_gtohp(FILE *src,FILE *dst,int uniquetask);
void gtohp_to_core(FILE *src, FILE *dst);
void objects_to_predicats(FILE *src,char *data);
int add_predicat(int index , char *data, char *type, char *object);
void delete_init(FILE *src);
void goals_gtohp_to_goals_core(FILE *src,FILE *dst);
void UppertoLower(char *c);

int main(int argc,char *argv[])
{

		
	if(argc < 4)
	{
		
		printf("Usage : convertiseurPDDL_GTOHP_CORE <options> <src> <dest>\n");
		printf("-c :		gtohp to core\n");
		printf("-g :		pddl to gtohp\n");
		printf("-u : 		tache unique : (tag t1 (do_problem ))\n");
		return -1;

	}
	FILE *src = fopen(argv[argc-2],"r");
	FILE *dst = fopen(argv[argc-1],"w");

	if((argc == 4) && (strcmp(argv[1],"-c") == 0))
		gtohp_to_core(src,dst);
	else
		if((argc == 5) && ((strcmp(argv[1],"-u") == 0) || (strcmp(argv[2],"-u") == 0)))
			pddl_to_gtohp(src,dst,1);
		else
		{
			
			pddl_to_gtohp(src,dst,0);
		}
	
	fclose(src);
	fclose(dst);
	return 0;

}


/*
Traduit un fichier probleme pddl (src) en un fichier probleme GTOHP (dst)
Si unique task != 0 alors dans la catégorie task ecrit : (tag t1 (do_problem ))
Attention : Aucune vérification n'est faite sur la structure du fichier pddl 
			il peut donc y avoir des erreurs 
*/
void pddl_to_gtohp(FILE *src,FILE *dst,int uniquetask)
{

	

	
	//Definition du probleme
	go_to_next_parenthesis(src);
	fprintf(dst,"(define ");
	
	//Nom du Domaine
	go_to_next_parenthesis(src);
	get_under_parenthesis(src,dst,0,"\0");
	fprintf(dst,"\n");

	go_to_next_parenthesis(src);
	get_under_parenthesis(src,dst,0,"\0");
	fprintf(dst,"\n");

	fprintf(dst,"\t(:requirements :strips :typing :negative-preconditions :htn :equality)\n\n");

	//Object
	go_to_next_parenthesis(src);
	get_under_parenthesis(src,dst,0,"\0");
	fprintf(dst,"\n");
	
	//Init
	go_to_next_parenthesis(src);	
	get_under_parenthesis(src,dst,0,"\0");
	fprintf(dst,"\n");


	//Goal 

	fprintf(dst,"(:goal\n");
	fprintf(dst,"\t:tasks  (\n");
	if(uniquetask)
		fprintf(dst,"\t\t\t (tag t1 (do_problem )) \n\t\t\t\n\t\t\t\n");
	else
		fprintf(dst,"\t\t\t ;; A remplir \n\t\t\t\n\t\t\t\n");
	
	fprintf(dst,"\t\t)\n");
	fprintf(dst,"\t:constraints(and\n\t\t\t(after ");

	//Constaints
	go_to_next_parenthesis(src);
	go_to_next_parenthesis(src);
	get_under_parenthesis(src,dst,0,"\t\t\t\t\t\t\0");
	fprintf(dst,"\n");

	fprintf(dst,"\t\t\t\t t1)\n");
	fprintf(dst,"\t\t)\n");
	fprintf(dst,"))\n");
	

}






/*
Récupere ce qui se trouve entre parenthése dans un fichier src 
de manière récursive et l'ecrit dans le fichier dst
Avant chaque parenthèse ouvrante (et donc avant chaque appele récursif) rajoute
le separateur au fichier dst
Retour l'index

Input :	
	src  : fichier avec la tete de lecture juste après une parenthèse ouvrante
	dst  : fichier destination
	is_Core_Init : boolean pour savoir si on est dans le cas de la catégorie init de Core , 
	dans ce cas on supprimer :init et on ne commence ni ne termine avec une parenthése
	separator : chaine de caractère qui fini par \0

Exemple:
	Input :
		separateur = \n\0
		is_Core_Init = 0
		
		src = Le roi burgonde (La fleur embouquet fâne , et jamais ne renait ) (salsifi) (j'aime les fruits au sirop) )

Output :
	(Le roi burgonde
	(La fleur embouquet fâne , et jamais ne renait )
	(salsifi)
	(j'aime les fruits au sirop) )
*/
void get_under_parenthesis(FILE *src, FILE *dst,int is_Core_Init,char *separator)
{
	char c;
	int i;

	if(is_Core_Init)
		delete_init(src);
	else
		fprintf(dst,"(");
	
	//data[index]='(';
	//index++;
	c = fgetc(src);
	while(c!=')'){

		if(c=='(')
			get_under_parenthesis(src,dst,0,separator);
		else
			fprintf(dst,"%c",c);


		c=fgetc(src);
	}
	
	if(is_Core_Init == 0)
		fprintf(dst," )");
	
}


/*
 Va jusqu'à la prochaine parenthèse ouvrant sur fichier src
Exemple :
Input :
	blua
	blu 
	bli
	(	blum plum plum plam )
	bli 
	bli

Output :

		blum plum plum plam )
	bli 
	bli
*/
void go_to_next_parenthesis(FILE *src)
{
	char c;
	do
	{
		c = fgetc(src);
	}while(c!='(');
}


/*
Va jusqu'à la prochaine parenthèse ouvrant sur fichier src et renvoi 1
mais s'arrete à la première fermante rencontré et renvoi 0.
Exemple :
Input :
	src:

	blua
	blu 
	bli
	(	blum plum plum plam )
	bli 
	bli

Output :
	1
	src:

		blum plum plum plam )
	bli 
	bli
*/
int go_to_next_parenthesis_stop_close(FILE *src)
{
	char c;
	do
	{
		c= fgetc(src);
	}while(c!='(' && c!=')');

	return c=='(';
}






/*
Traduit un fichier probleme GTOHP (src) en un fichier probleme CORE (dst)
Attention : Aucune vérification n'est faite sur la structure du fichier GTOHP 
			il peut donc y avoir des erreurs 
*/
void gtohp_to_core(FILE *src, FILE *dst)
{
	char temp[TAILLE_TEMP];
	//int index;
	//Saut de toute la 1er partie pour atteindre la liste d'objet
	go_to_next_parenthesis(src);
	go_to_next_parenthesis(src);
	go_to_next_parenthesis(src);
	go_to_next_parenthesis(src);
	go_to_next_parenthesis(src);

	//TODO récuperer le nom 
	fprintf(dst,"(defproblem problem byParser\n");  
	fprintf(dst,"(\n");
	fprintf(dst,"\t;;;\n");
	fprintf(dst,"\t;;;   facts\n");
	fprintf(dst,"\t;;;\n\n\n");

	//Objects
	objects_to_predicats(src,temp);
	fprintf(dst,"%s \n",temp);

	//predicats
	go_to_next_parenthesis(src);
	get_under_parenthesis(src,dst,1,"\0");
	//delete_init(index,temp);

	fprintf(dst,"\n)\n");

	//goals
	fprintf(dst,"\t;;;\n");
	fprintf(dst,"\t;;;   goals\n");
	fprintf(dst,"\t;;;\n\n\n");

	go_to_next_parenthesis(src);
	go_to_next_parenthesis(src);
	goals_gtohp_to_goals_core(src,dst);

	fprintf(dst,"\n");
	fprintf(dst,")");
}





/**
Traduit les objects qui sont sous forme GTOHP dans le fichier src en prédicats CORE 
et les stockent dans la chaine de caractère data.  

Le fichier src doit avec sa tete de lecture sur :objects

Input :
	Fichier de la forme: 
		:objects 
			D B A C - block
			I K H - pile		
		)
Ouput : 
	Chaine de caractére de la forme :
	(block D)
	(block B)
	(block A)
	(block C)
	(pile I)
	(pile J)
	(pile K)

....(suite du fichier)



**/

void objects_to_predicats(FILE *src,char *data)
{
	char objects[NB_TYPE_MAX][NB_OBJECT_MAX][SIZE_OBJECT_NAME];
	int index_type=0;
	int index_object=0;
	int i;
	int index_data=0;
	fscanf(src,"%s",data);
	do{
			index_object++;
			fscanf(src,"%s",objects[index_type][index_object]);
		}while(	(strcmp(objects[index_type][index_object],"-")!=0) &&
				(strcmp(objects[index_type][index_object],")")!=0));


	do{
		//On a trouvé le type
		fscanf(src,"%s",objects[index_type][0]);
		UppertoLower(objects[index_type][0]);
		//On remplie data
		for(i=1;i<index_object;i++)
			index_data = add_predicat(index_data,data,objects[index_type][0],objects[index_type][i]);

		data[index_data]='\n';
		index_data++;
		index_object=0;
		index_type++;
		//On remplie la liste d'objet
		do{
			index_object++;
			fscanf(src,"%s",objects[index_type][index_object]);
		}while(	(strcmp(objects[index_type][index_object],"-")!=0) &&
				(strcmp(objects[index_type][index_object],")")!=0));
		
	}while((strcmp(objects[index_type][index_object],")")!=0));

}

/**
Ajoute un predicat au tableau data à l'indice index
Input : 
	type et object : 2 chaines de caractéres contenant respectivement le type et l'object du predicat 
	index : entier naturel non nul

Output :
	data : Data avec le nouveau predicat à la fin , à la ligne. 	
	retour : Le nouvelle index de data.

**/
int add_predicat(int index , char *data, char *type, char *object)
{
	sprintf(&data[index],"\t(%s %s)\n",type,object);
	index = index + strlen(type) + strlen(object) + 5;

	return index;
}

/**
	
Supprimer le terme :init et les parenthèses correspondante dans la chaine de caractère data 

Exemple :
Input : 
(:init
	Blu
	bli
	bla
	blum 
	pum
	pum
)

Output: 
	Blu
	bli
	bla
	blum 
	pum
	pum

**/
void delete_init(FILE *src)
{
	fgetc(src);
	fgetc(src);
	fgetc(src);
	fgetc(src);
	fgetc(src);
}

/**
Recupere les goals d'un fichier GTOHP et les renvois dans le format Core
Exemple :
Input : 
:task 
	(tag t1 (do_put_on B A))
    (tag t2 (do_put_on B A))
    (tag t3 (do_put_on B A))
)


OutPut :
(
	(do_put_on B A)
	(do_put_on B A)
	(do_put_on B A)
)
*/
void goals_gtohp_to_goals_core(FILE *src,FILE *dst)
{
	//int index = 2;
	fprintf(dst,"(\n");

	while(go_to_next_parenthesis_stop_close(src) == 1)
	{
		go_to_next_parenthesis(src);
		fprintf(dst,"\t");
		get_under_parenthesis(src,dst,0,"\0");
		fprintf(dst,"\n");
		go_to_next_parenthesis_stop_close(src);

	}
	fprintf(dst,")");
	
}



/*
Prend une chaine caractère et transforme les lettre majuscule en lettre minuscule.
*/
void UppertoLower(char *c)
{
	int i=0;
	while(c[i]!='\0')
	{
		if(c[i]>='A'&&c[i]<='Z')
			c[i]=c[i]+'a'-'A';
		i++;
	}
}
