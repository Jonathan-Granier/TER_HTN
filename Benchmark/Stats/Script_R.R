#!/usr/bin/Rscript --slave
argv <- commandArgs(TRUE)
#Score de chaque donné , on fait (min de la ligne)/Valeur et ignore le -1 , on a un score entre 0 et 1 
ComputeScore <- function(data,num)
{
    No_prob <- data_plan_size[,1]
    Score <- data[,num]
    for(i in 1:length(No_prob))
    {
        
        if(is.na(Score[i]))
        {
            print("FLAG 1")
            Score[i] <- 0
        }
        else if(Score[i]<=1)
        {
            print("FLAG 2")
            Score[i] <- 1
        }
        else
        {
            print("FLAG 3")
            minimum <- min(data[i,2],data[i,3],data[i,4],na.rm = TRUE)
            Score[i] <-1/(1+log10(Score[i]/minimum)) 
        }
    }
    
    return (Score)
}


#setwd("/home/jonathan/M1/TER/Rendu/Stats")


Col_GTOHP <- "blue"
Col_CORE <- "red"
Col_PDDL <- "purple"

File_plan <- paste("Data/Stat_Taille_plan_",argv[1],".dat",sep="")
File_time <- paste("Data/Stat_Temps_de_resolution_",argv[1],".dat",sep="")
Name_graph_plan <- paste("Graph/Graphique_taille_plan_",argv[1],".png",sep="")
Name_graph_plan_Score <- paste("Graph/Graphique_taille_plan_Score_",argv[1],".png",sep="")
Name_graph_time <- paste("Graph/Graphique_temps_resolution_",argv[1],".png",sep="")
Name_graph_time_Score <- paste("Graph/Graphique_temps_resolution_Score_",argv[1],".png",sep="")

data_plan_size <- read.table(File_plan,header = TRUE,sep=";",na.strings="-1 ")
data_time <-  read.table(File_time,header = TRUE,sep=";",dec=".",na.strings="-1 ")

data_plan_size <-  sapply(data_plan_size,as.character)
data_plan_size <- ifelse(data_plan_size=="-1",NA,data_plan_size)
data_plan_size <- apply(data_plan_size,2,as.numeric)

data_time <-  sapply(data_time,as.character)
data_time <- ifelse(data_time=="-1",NA,data_time)
data_time <- apply(data_time,2,as.numeric)


GTOHP_plan <- data_plan_size[,2]
CORE_plan <- data_plan_size[,3]
PDDL_plan <- data_plan_size[,4]
GTOHP_time <- data_time[,2]
CORE_time <- data_time[,3]
PDDL_time <- data_time[,4]

Score_GTOHP_plan <- ComputeScore(data_plan_size,2)
Score_GTOHP_time <- ComputeScore(data_time,2)
Score_CORE_plan <- ComputeScore(data_plan_size,3)
Score_CORE_time <- ComputeScore(data_time,3)
Score_PDDL_plan <- ComputeScore(data_plan_size,4)
Score_PDDL_time <- ComputeScore(data_time,4)



## TAILLE DES PLANS
png(file = Name_graph_plan, width = 800, height = 700)
plot(GTOHP_plan,type="b",lwd=2,col=Col_GTOHP, xlab="Plan number",ylab="Size of plan", main="Plans Size Curves",   ylim=range( PDDL_plan,GTOHP_plan, CORE_plan,na.rm=TRUE))
lines(CORE_plan,type="b",lwd=2,col=Col_CORE )
lines(PDDL_plan,type="b",lwd=2,col=Col_PDDL )
legend(x="bottomright", legend=c("GTOHP","CORE","PDDL" ), col=c(Col_GTOHP,Col_CORE,Col_PDDL ),  pch=c(1,1), lwd=c(2,2))
dev.off()

# png(file = Name_graph_plan_Score, width = 800, height = 700)
# plot(Score_GTOHP_plan  ,type="b",lwd=2,col=Col_GTOHP, xlab="Numero du plan",ylab="Score(/1) sur la taille du plan" , main="Graphique sur le score de la taille des plans", ylim=range( max(Score_GTOHP_plan, Score_CORE_plan,na.rm=TRUE)))
# lines(Score_CORE_plan,type="b",lwd=2,col=Col_CORE )
# lines(Score_PDDL_plan,type="b",lwd=2,col=Col_PDDL )
# legend(x="bottomright", legend=c("GTOHP","CORE","PDDL" ), col=c(Col_GTOHP,Col_CORE,Col_PDDL ),  pch=c(1,1), lwd=c(2,2))
# dev.off()
# 



## TEMPS D'EXECUTION
png(file = Name_graph_time, width = 800, height = 700)
plot(GTOHP_time  ,type="b",lwd=2,col=Col_GTOHP,xlab="Plan number",ylab="Execution time", main="Curves on execution time", ylim=range( PDDL_time,GTOHP_time, CORE_time,na.rm=TRUE))
lines(CORE_time,type="b",lwd=2,col=Col_CORE )
lines(PDDL_time,type="b",lwd=2,col=Col_PDDL )
legend(x="bottomright", legend=c("GTOHP","CORE","PDDL" ), col=c(Col_GTOHP,Col_CORE,Col_PDDL ),  pch=c(1,1), lwd=c(2,2))
dev.off()

# png(file = Name_graph_time_Score, width = 800, height = 700)
# plot(Score_GTOHP_time ,type="b",lwd=2,col=Col_GTOHP, xlab="Numero du plan",ylab="Score(/1) sur le temps d'execution", main="Graphique sur le score du temps d'execution" ,ylim=range( max(Score_GTOHP_time, Score_CORE_time,na.rm=TRUE)))
# lines(Score_CORE_time,type="b",lwd=2,col=Col_CORE )
# lines(Score_PDDL_time,type="b",lwd=2,col=Col_PDDL )
# legend(x="bottomright", legend=c("GTOHP","CORE","PDDL" ), col=c(Col_GTOHP,Col_CORE,Col_PDDL ),  pch=c(1,1), lwd=c(2,2))
# dev.off()

print(Score_PDDL_time)
Score_total_GTOHP_plan = sum(Score_GTOHP_plan)
Score_total_CORE_plan = sum(Score_CORE_plan)
Score_total_PDDL_plan = sum(Score_PDDL_plan)
Score_total_GTOHP_time = sum(Score_GTOHP_time)
Score_total_CORE_time = sum(Score_CORE_time)
Score_total_PDDL_time = sum(Score_PDDL_time)

Score_all_plan = paste(argv[1]," & ",Score_total_PDDL_plan, " & ",Score_total_GTOHP_plan," & ", Score_total_CORE_plan, " & ", length(data_time[,1]), "\\")
Score_all_time = paste(argv[1]," & ",Score_total_PDDL_time, " & ",Score_total_GTOHP_time," & ", Score_total_CORE_time, " & ", length(data_time[,1]), "\\")
score_file = paste("Score/score_",argv[1],".txt")
sink(score_file)
print("TIME")
print(Score_all_time)
print("PLAN")
print(Score_all_plan)
sink()
