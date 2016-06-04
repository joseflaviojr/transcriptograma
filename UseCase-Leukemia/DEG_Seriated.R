
seriation <- read.table(file="Seriation_Final_Genes.txt", header=F)

seriate <- function( deg, seriation ){
    deg[is.na(deg)] <- 0
    deg             <- cbind(deg["gene"],abs(deg["statistic"]))
    rownames(deg)   <- deg$gene
    deg             <- deg[as.vector(seriation[,1]),]
    deg
}

deg_all   <- seriate(read.table(file="DEG_ALL.txt", header=T), seriation)
deg_aml   <- seriate(read.table(file="DEG_AML.txt", header=T), seriation)
deg       <- cbind(as.character(deg_all$gene), deg_all$statistic, deg_aml$statistic)

write.table(t(deg_all$statistic), file="DEG_Seriated_ALL_Values.txt", row.names=F, col.names=F, sep=",", quote=F)
write.table(t(deg_aml$statistic), file="DEG_Seriated_AML_Values.txt", row.names=F, col.names=F, sep=",", quote=F)
write.table(deg, file="DEG_Seriated.txt", row.names=F, col.names=F, sep="\t", quote=F)