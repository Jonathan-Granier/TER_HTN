#!/usr/bin/Rscript --slave
argv <- commandArgs(TRUE)
#Score de chaque donné , on fait Valeur/(max de la ligne) , on a un score entre 0 et 1 
ComputeScore <- function(data,num,time)
{
    No_prob <- data_plan_size[,1]
    Score <- data[,num]
    for(i in 1:length(No_prob))
    {
        if(Score[i] == -1)
        {
            Score[i] <- 0
        }
        else
        {
            if(time == 1){  Score[i] <- min(data[i,2],data[i,3])/Score[i] }
            else { Score[i] <- Score[i]/max(data[i,2],data[i,3]) }
        }
    }
    return (Score)
}


#setwd("/home/jonathan/M1/TER/Rendu/Stats")


Col_HTN <- "blue"
Col_CORE <- "red"
Col_PDDL <- "purple"

File_plan <- paste("Stat_Taille_plan_",argv[1],".dat",sep="")
File_time <- paste("Stat_Temps_de_resolution_",argv[1],".dat",sep="")
Name_graph_plan <- paste("Graphique_taille_plan_",argv[1],".png",sep="")
Name_graph_plan_Score <- paste("Graphique_taille_plan_Score_",argv[1],".png",sep="")
Name_graph_time <- paste("Graphique_temps_resolution_",argv[1],".png",sep="")
Name_graph_time_Score <- paste("Graphique_temps_resolution_Score_",argv[1],".png",sep="")

data_plan_size <- read.table(File_plan,header = TRUE,sep=";")
data_time <-  read.table(File_time,header = TRUE,sep=";",dec=".")

HTN_plan <- data_plan_size[,2]
Core_plan <- data_plan_size[,3]
HTN_time <- data_time[,2]
Core_time <- data_time[,3]


Score_HTN_plan <- ComputeScore(data_plan_size,2,0)
Score_HTN_time <- ComputeScore(data_time,2,1)
Score_Core_plan <- ComputeScore(data_plan_size,3,0)
Score_Core_time <- ComputeScore(data_time,3,1)


## TAILLE DES PLANS
png(file = Name_graph_plan, width = 800, height = 700)
plot(HTN_plan ,type="b",lwd=2,col=Col_HTN, xlab="Numero du plan",ylab="Taille d'un plan", main="Graphique sur la taille des plans",   ylim=range( c(HTN_plan, Core_plan)))
lines(Core_plan,type="b",lwd=2,col=Col_CORE )
legend(x="bottomright", legend=c("HTN","Core" ), col=c(Col_HTN,Col_CORE ),  pch=c(1,1), lwd=c(2,2))
dev.off()

png(file = Name_graph_plan_Score, width = 800, height = 700)
plot(Score_HTN_plan  ,type="b",lwd=2,col=Col_HTN, xlab="Numero du plan",ylab="Score(/1) sur la taille du plan" , main="Graphique sur le score de la taille des plans", ylim=range( c(Score_HTN_plan, Score_Core_plan)))
lines(Score_Core_plan,type="b",lwd=2,col=Col_CORE )
legend(x="bottomright", legend=c("HTN","Core" ), col=c(Col_HTN,Col_CORE ), pch=c(1,1), lwd=c(2,2))
dev.off()




## TEMPS D'EXECUTION
png(file = Name_graph_time, width = 800, height = 700)
plot(HTN_time  ,type="b",lwd=2,col=Col_HTN,xlab="Numero du plan",ylab="Temps d'execution", main="Graphique sur le temps d'execution", ylim=range( c(HTN_time, Core_time)))
lines(Core_time,type="b",lwd=2,col=Col_CORE )
legend(x="bottomright", legend=c("HTN","Core" ), col=c(Col_HTN,Col_CORE ),  pch=c(1,1), lwd=c(2,2))
dev.off()

png(file = Name_graph_time_Score, width = 800, height = 700)
plot(Score_HTN_time ,type="b",lwd=2,col=Col_HTN, xlab="Numero du plan",ylab="Score(/1) sur le temps d'execution", main="Graphique sur le score du temps d'execution" ,ylim=range( c(Score_HTN_time, Score_Core_time)))
lines(Score_Core_time,type="b",lwd=2,col=Col_CORE )
legend(x="bottomright", legend=c("HTN","Core" ), col=c(Col_HTN,Col_CORE ),  pch=c(1,1), lwd=c(2,2))
dev.off()



