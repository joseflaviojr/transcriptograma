
source(paste(Sys.getenv("TRANSCRIPTOGRAMA"),"Transcriptograma.R",sep=.Platform$file.sep))

png(file="Dendrogram_Transcriptograms.png", width=3096, height=2048, res=72, pointsize=20)
gerarDendrograma("Transcriptograms.txt", cabecalhoColunas=FALSE, colunas="1-72", titulo="Dendrogram of the Transcriptograms", nomes="Expressions_Labels.txt", vertical=TRUE)
dev.off()

png(file="Dendrogram_Expressions.png", width=3096, height=2048, res=72, pointsize=20)
gerarDendrograma("Expressions_Seriated.txt", cabecalhoColunas=FALSE, colunas="1-72", titulo="Dendrogram of the Expressions", nomes="Expressions_Labels.txt", vertical=TRUE)
dev.off()
