
#
#  Copyright (C) 2015 José Flávio de Souza Dias Júnior
#  
#  This file is part of Transcriptograma - <http://www.joseflavio.com/transcriptograma/>.
#  
#  Transcriptograma is free software: you can redistribute it and/or modify
#  it under the terms of the GNU Lesser General Public License as published by
#  the Free Software Foundation, either version 3 of the License, or
#  (at your option) any later version.
#  
#  Transcriptograma is distributed in the hope that it will be useful,
#  but WITHOUT ANY WARRANTY; without even the implied warranty of
#  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
#  GNU Lesser General Public License for more details.
#  
#  You should have received a copy of the GNU Lesser General Public License
#  along with Transcriptograma. If not, see <http://www.gnu.org/licenses/>.
#

#
#  Direitos Autorais Reservados (C) 2015 José Flávio de Souza Dias Júnior
# 
#  Este arquivo é parte de Transcriptograma - <http://www.joseflavio.com/transcriptograma/>.
# 
#  Transcriptograma é software livre: você pode redistribuí-lo e/ou modificá-lo
#  sob os termos da Licença Pública Menos Geral GNU conforme publicada pela
#  Free Software Foundation, tanto a versão 3 da Licença, como
#  (a seu critério) qualquer versão posterior.
# 
#  Transcriptograma é distribuído na expectativa de que seja útil,
#  porém, SEM NENHUMA GARANTIA; nem mesmo a garantia implícita de
#  COMERCIABILIDADE ou ADEQUAÇÃO A UMA FINALIDADE ESPECÍFICA. Consulte a
#  Licença Pública Menos Geral do GNU para mais detalhes.
# 
#  Você deve ter recebido uma cópia da Licença Pública Menos Geral do GNU
#  junto com Transcriptograma. Se não, veja <http://www.gnu.org/licenses/>.
#

#----------------------------------------

instalados <- rownames(installed.packages())

if( ! "GOstats" %in% instalados ){
	source("http://bioconductor.org/biocLite.R")
	biocLite("GOstats")
}

if( ! "org.Hs.eg.db" %in% instalados ){
	source("http://bioconductor.org/biocLite.R")
	biocLite("org.Hs.eg.db")
}

library("GOstats")
library("org.Hs.eg.db")
library("stats")
library("tcltk")

#----------------------------------------

arquivos <- c()
if( interactive() ){
	arquivos <- tk_choose.files(
		default = getwd(),
		caption = "Selecione os arquivos de genes...",
		multi=TRUE,
		filters=matrix(c("Texto", ".txt", "Todos", "*"), 2, 2, byrow=TRUE)
	)
}

#----------------------------------------

janela <- tktoplevel()
tktitle(janela) <- "Ontologia"
escolha <- tclVar(0)
BP_botao <- tkbutton(janela, text="  BP  ", command=function() tclvalue(escolha) <- 1)
MF_botao <- tkbutton(janela, text="  MF  ", command=function() tclvalue(escolha) <- 2)
CC_botao <- tkbutton(janela, text="  CC  ", command=function() tclvalue(escolha) <- 3)
tkgrid(BP_botao, MF_botao, CC_botao)
tkbind(janela, "<Destroy>", function() tclvalue(escolha) <- 1)
tkfocus(janela)
tkwait.variable(escolha)
ontologia <- c("BP", "MF", "CC")[as.integer(tclvalue(escolha))]
tkdestroy(janela)

#----------------------------------------

m <- matrix(NA,0,2)

for( arquivo in arquivos ){

	if( ! file.exists(arquivo) || file.info(arquivo)$isdir ) next
	print(arquivo)

	genes <- read.table(arquivo, header=FALSE, blank.lines.skip=TRUE)
	genes <- as.matrix(genes)

	resultado <- hyperGTest(
		new("GOHyperGParams",
			geneIds=genes,
			universeGeneIds=NULL,
			annotation="org.Hs.eg.db",
			ontology=ontologia,
			pvalueCutoff=0.001,
			conditional=FALSE,
			testDirection="over"
		)
	)

	resultado <- summary(resultado)
	resultado[,2] <- p.adjust(resultado[,2], method="BH")

	write.table(
		resultado,
		file=paste(arquivo,".GOHyperGTest.txt", sep=""),
		sep="\t", quote=FALSE, col.names=TRUE, row.names=FALSE
	)

	gos <- resultado[,1]
	terms <- resultado[,7]
	pvalues <- resultado[,2]

	m <- cbind(m,NA)
	mcoluna <- ncol(m)

	for( i in 1:length(gos) ){
		go <- gos[i]
		mlinha <- which(m[,1]==go)
		if( length(mlinha) == 0 ){
			m <- rbind(m,NA)
			mlinha <- nrow(m)
			m[mlinha,1] <- go
			m[mlinha,2] <- terms[i]
		}
		m[mlinha,mcoluna] <- pvalues[i]
	}

}

#----------------------------------------

colnames(m) <- c("GO", "Term", 1:(ncol(m)-2))

if( interactive() ){
	arquivo <- tclvalue(tkgetSaveFile(
		title="Salvar matriz geral de p-values",
		filetypes="{Texto {.txt}} {CSV {.csv}} {Todos *}",
		initialdir=getwd(),
		defaultextension=".txt"
	))
	write.table( m, file=arquivo, sep="\t", quote=FALSE, col.names=TRUE, row.names=FALSE )
}

#----------------------------------------