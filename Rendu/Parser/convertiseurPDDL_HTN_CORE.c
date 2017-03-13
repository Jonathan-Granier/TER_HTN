#include <stdlib.h>
#include <stdio.h>

int recupereEntreParenthese(FILE *src, char *dest);
void sauterUnePartie(FILE *src);

int main(int argc,char *argv[])
{

	if(argc != 3)
	{
		printf("Usage : convertiseurPDDL_HTN_CORE <src> <dest>");
		return -1;

	}

	char retour[1000000];
	FILE *src = fopen(argv[1],"r");
	FILE *dst = fopen(argv[2],"w");
	
	//Definition du probleme
	recupereEntreParenthese(src,retour);
	fprintf(dst,"%s\n",retour);
	
	//Nom du Domain
	recupereEntreParenthese(src,retour);
	fprintf(dst,"%s\n",retour);

	fprintf(dst,"\t(:requirements :strips :typing :negative-preconditions :htn :equality)\n\n");

	//Object
	recupereEntreParenthese(src,retour);
	fprintf(dst,"%s\n",retour);

	//Initialisation
	fprintf(dst,"(:INIT \n");
	sauterUnePartie(src);
	//Récuperation des prédicats d'initialisation
	while(recupereEntreParenthese(src,retour)==1)
	{
		fprintf(dst,"\t%s\n",retour);
	}

	fprintf(dst,")\n");

	//Ecriture du But
	fprintf(dst,"(:goal\n");
	fprintf(dst,"\t:tasks  (\n");
	fprintf(dst,"\t\t\t ;; A remplir \n\t\t\t\n\t\t\t\n");
	fprintf(dst,"\t\t)\n");
	fprintf(dst,"\t:constraints(and\n\t\t\t(after (and\n");

	//Saut du goal
	sauterUnePartie(src);
	//Saut du AND
	sauterUnePartie(src);

	//Récupération des containtes
	while(recupereEntreParenthese(src,retour)==1)
	{
		fprintf(dst,"\t\t\t\t\t%s\n",retour);
	}

	//Fermeture des parenthèses
	fprintf(dst,"\t\t\t\t) t1\n");
	fprintf(dst,"\t\t)\n");
	fprintf(dst,"))\n");
	fclose(src);
	fclose(dst);
	return 0;

}


// Recupere une chaine de caractere situé entre 2 parenthèses
int recupereEntreParenthese(FILE *src, char *dest)
{
	int i= 0;
	char c = fgetc(src); 
	

	while(c!= '(' && c!= ')')
		c = fgetc(src);

	if(c == ')')
		return 0;
	
	while(c!= ')')
	{
		dest[i] = c;
		i++;
		c = fgetc(src);
	}
	dest[i] = c;
	i++;
	dest[i] = '\0';
	return 1;
}	

// Saute une partie du fichier d'une parenthese ouvrant à un espace
void sauterUnePartie(FILE *src)
{
	char c = fgetc(src);
	while(c!='(')
		c = fgetc(src);
	while(c!=' ' && c!= '\n')
		c = fgetc(src);
}