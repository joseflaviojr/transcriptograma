
#
#  Copyright (C) 2015-2016 José Flávio de Souza Dias Júnior
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
#  Direitos Autorais Reservados (C) 2015-2016 José Flávio de Souza Dias Júnior
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

# Enriquecimento funcional através de ontologia.
# Gene Ontology - Term Enrichment (http://amigo.geneontology.org/rte)
#
# anotacao: "org.Hs.eg.db" | "org.Sc.sgd.db" | ...
# ontologia: BP=Biological Process, MF=Molecular Function, CC=Cellular Component
enriquecer <- function( anotacao="org.Hs.eg.db", ontologia="MF",
						corteValorP=0.001, ajuste="BH",
						saida=NULL, arquivos=c() ){

	instalados <- rownames(installed.packages())

	if( ! "GOstats" %in% instalados ){
		source("http://bioconductor.org/biocLite.R")
		biocLite("GOstats")
	}

	if( ! anotacao %in% instalados ){
		source("http://bioconductor.org/biocLite.R")
		biocLite(anotacao)
	}

	library("GOstats")
	library(anotacao, character.only=TRUE)
	library("stats")

	if( length(ontologia) == 0 || length(saida) == 0 || length(arquivos) == 0 ){
		library("tcltk")
	}

	#---------------

	if( length(arquivos) == 0 && interactive() ){
		arquivos <- tk_choose.files(
			default = getwd(),
			caption = "Selecione os arquivos que contém lista de genes...",
			multi=TRUE,
			filters=matrix(c("Texto", ".txt", "Todos", "*"), 2, 2, byrow=TRUE)
		)
	}

	if( length(arquivos) == 0 ) return(FALSE)

	#---------------

	if( length(ontologia) == 0 ){
		janela <- tktoplevel()
		tktitle(janela) <- "Ontologia GO (BP=Biological Process, MF=Molecular Function, CC=Cellular Component)"
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
	}

	if( length(ontologia) == 0 ) return(FALSE)

	#---------------

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
				annotation=anotacao,
				ontology=ontologia,
				pvalueCutoff=corteValorP,
				conditional=FALSE,
				testDirection="over"
			)
		)

		resultado <- summary(resultado)
		resultado[,2] <- p.adjust(resultado[,2], method=ajuste)

		write.table(
			resultado,
			file=paste(arquivo,".GO-",ontologia,".txt", sep=""),
			sep="\t", quote=FALSE, col.names=TRUE, row.names=FALSE
		)

		gos <- resultado[,1]
		terms <- resultado[,7]
		pvalues <- resultado[,2]

		m <- cbind(m,NA)
		mcoluna <- ncol(m)

		gos_total <- length(gos)
		if( gos_total > 0 ){
			for( i in 1:gos_total ){
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

	}

	#---------------

	colnames(m) <- c("GO", "Term", 1:(ncol(m)-2))

	if( length(saida) == 0 ){
		if( interactive() ){
			saida <- tclvalue(tkgetSaveFile(
				title="Salvar resultado geral",
				filetypes="{Texto {.txt}} {CSV {.csv}} {Todos *}",
				initialdir=getwd(),
				defaultextension=".txt"
			))
		}else{
			saida <- "resultado.txt"
		}
	}

	if( length(saida) > 0 ){
		write.table( m, file=saida, sep="\t", quote=FALSE, col.names=TRUE, row.names=FALSE )
	}

	TRUE

}

#----------------------------------------

# Heat map de arquivo com valores numéricos.
# Quanto menor o valor, maior o destaque (temperatura).
# O arquivo deve estar no formato CSV, tabulado e conter cabeçalho.
# Coluna 1 = sigla
# Coluna 2 = descrição curta
# Coluna 3 = primeiro valor da linha
# Coluna N = n-ésimo valor da linha
#
# classificacao: método de classificação das linhas (ordem) = "LOCAL" ou "GLOBAL".
# garantia     : no caso de "LOCAL", garante a posição dos "n" primeiros com menor valor.
# maximo       : quantidade máxima de linhas no heat map
# largura      : largura da figura, em pixels
# altura       : altura da figura, em pixels
# resolucao    : resolução da figura, em ppi
# fonteTamanho : tamanho da fonte na figura, em pontos
gerarHeatmap <- function( arquivo=NULL, rotulo="M", classificacao="LOCAL", garantia=4, maximo=50,
						  largura=4096, altura=2048, resolucao=300, fonteTamanho=14 ){

	library(stats)
	library(grDevices)

	if( length(arquivo) == 0 ){

		library(tcltk)

		arquivo <- tk_choose.files(
			default = getwd(),
			caption = "Selecione a tabela que contém os dados...",
			multi=FALSE,
			filters=matrix(c("Texto", ".txt", "CSV", ".csv", "Todos", "*"), 3, 2, byrow=TRUE)
		)

	}

	if( length(arquivo) == 0 ) return(FALSE)

	m <- read.csv(arquivo, sep="\t")
	m <- cbind(m,0)
	linhas <- nrow(m)
	colunas <- ncol(m)

	if( maximo > linhas ) maximo <- linhas

	if( classificacao == "GLOBAL" ){

		for( i in 1:linhas ){
			for( j in 3:(colunas-1) ){
				if( ! is.na(m[i,j]) ) m[i,colunas] <- m[i,colunas] + m[i,j]
			}
		}

	}else{

		if( garantia > linhas ) garantia <- linhas

		for( j in 3:(colunas-1) ){
			m <- m[order(m[,j]),]
			if( garantia > 0 ){
				for( i in 1:garantia ){
					if( ! is.na(m[i,j]) ) m[i,colunas] <- -Inf
				}
			}
			if( garantia < linhas ){
				for( i in (garantia+1):linhas ){
					if( ! is.na(m[i,j]) ) m[i,colunas] <- ( m[i,colunas] + i ) / 2
				}
			}
		}

	}

	m <- m[order(m[,colunas]),]
	m2 <- data.matrix( m[ 1:maximo, 3:(colunas-1) ] )

	row.names(m2) <- m[1:maximo,2]
	for( i in 1:maximo ){
		texto <- row.names(m2)[i]
		if( nchar(texto) > 70 ){
			row.names(m2)[i] <- paste(substr(texto,1,67),"...",sep="")
		}
	}

	rotulos <- c()
	for( i in 1:ncol(m2) ) rotulos <- c(rotulos, paste(rotulo,formatC(i,width=2,flag="0"),sep=""))
	colnames(m2) <- rotulos

	png(
		file=paste(substr(arquivo,1,nchar(arquivo)-4),".png",sep=""),
		width=largura,
		height=altura,
		res=resolucao,
		pointsize=fonteTamanho
	)

	heatmap(m2, Rowv=NA, Colv=NA, col=gray.colors(256,0), scale="none", main=NA, xlab=NA, ylab=NA)
	if( ! is.null(dev.list()) ) dev.off()

	TRUE

}

#----------------------------------------

# Heat map de matriz com valores numéricos.
# entrada          : matriz ou arquivo CSV
# cabecalhoColunas : contém cabeçalho das colunas na primeira linha?
# cabecalhoLinhas  : contém cabeçalho das linhas na primeira coluna?
# colunas          : colunas desejadas, sem considerar cabeçalho. Ex.: "1-10,15"
# linhas           : linhas desejadas, sem considerar cabeçalho. Ex.: "1-10,15"
# titulo           : título do heat map
# rotulox          : rótulo do eixo x
# rotuloy          : rótulo do eixo y
# crescente        : Maior valor, maior temperatura?
# coresTotal       : Total de cores (subdivisões do gradiente)
gerarHeatmap2 <- function( entrada, cabecalhoColunas=TRUE, cabecalhoLinhas=TRUE, colunas=NULL, linhas=NULL,
	titulo=NULL, rotulox=NULL, rotuloy=NULL, crescente=TRUE, coresTotal=24 ){

    if( ! "lattice" %in% rownames(installed.packages()) ){
        install.packages("lattice", dependencies=TRUE, repos="http://cran-r.c3sl.ufpr.br/")
    }

	library(grDevices)
    library(lattice)

	matriz <- matriz(entrada, cabecalhoColunas, cabecalhoLinhas)

	if( ! is.null(colunas) ) matriz <- matriz[,numeros(colunas)]
	if( ! is.null(linhas)  ) matriz <- matriz[numeros(linhas),]

	if( crescente ) cores <- c("white","yellow","red","black")
	else cores <- c("black","red","yellow","white")

	levelplot(
		matriz,
		col.regions=colorRampPalette(cores,space="rgb")(coresTotal),
		colorkey=TRUE,
		contour=FALSE,
		pretty=TRUE,
		main=titulo,
		xlab=rotulox,
		ylab=rotuloy,
		scales=list(x=list(rot=90))
	)

}

#----------------------------------------

# Heat map de resultado de DEG (Differentially Expressed Gene).
# Ver função calcularDEG()
#
# transcriptograma: Endereço do arquivo que contém os transcriptogramas.
# nomenclatura    : Endereço do arquivo com o nome de cada transcriptograma.
# deg             : Endereço do arquivo gerado por calcularDEG().
# inicio          : Posição inicial do resultado do DEG a ser considerado.
# fim             : Posição final do resultado do DEG a ser considerado.
# titulo          : Título da figura.
# rotulox         : Rótulo do eixo horizontal da figura.
# rotuloy         : Rótulo do eixo vertical da figura.
# saida           : Endereço do arquivo PNG de saída (figura).
# largura         : Largura da figura, em pixels.
# altura          : Altura da figura, em pixels.
# resolucao       : Resolução da figura, em ppi.
# fonteTamanho    : Tamanho da fonte na figura, em pontos.
gerarHeatmapDEG <- function( transcriptograma, nomenclatura, deg, inicio=1, fim=50,
			titulo=NULL, rotulox=NULL, rotuloy=NULL,
			saida="HeatMapDEG.png", largura=2000, altura=2800, resolucao=100, fonteTamanho=30 ){

	library(grDevices)

	t <- as.matrix(read.table(deg, header=TRUE)[inicio:fim,]$gene)

	m <- as.matrix(read.table(transcriptograma, header=FALSE))
	rownames(m) <- m[,1]
	m <- m[,-1]
	colnames(m) <- as.matrix(read.table(nomenclatura, header=FALSE))

	m <- m[t,]
	m <- apply(m, c(1,2), as.numeric)

	paleta <- colorRampPalette(c("white","yellow","red","black"),space="rgb")(24)

	png(file=saida, width=largura, height=altura, res=resolucao, pointsize=fonteTamanho)
	heatmap(m, Rowv=NA, col=paleta, main=titulo, xlab=rotulox, ylab=rotuloy)
	dev.off()

}

#----------------------------------------

# Dendrograma de elementos especificados através de matriz de valores numéricos.
# Os elementos do conjunto são dispostos em colunas.
# entrada          : matriz ou arquivo CSV
# cabecalhoColunas : contém cabeçalho das colunas na primeira linha?
# cabecalhoLinhas  : contém cabeçalho das linhas na primeira coluna?
# colunas          : colunas desejadas, sem considerar cabeçalho. Ex.: "1-10,15"
# linhas           : linhas desejadas, sem considerar cabeçalho. Ex.: "1-10,15"
# transpor         : considerar a matriz na forma transposta?
# titulo           : título do dendrograma
# rotulox          : rótulo do eixo x
# rotuloy          : rótulo do eixo y
# nomes            : lista ou arquivo CSV com os nomes de todas as colunas
# vertical         : disposição do dendrograma na vertical?
# distMetodo       : método para calcular a distância entre os elementos
# hclustMetodo     : método de clusterização hierárquica
gerarDendrograma <- function( entrada, cabecalhoColunas=TRUE, cabecalhoLinhas=TRUE,
	colunas=NULL, linhas=NULL, transpor=FALSE,
	titulo="", rotulox="", rotuloy="", nomes=NULL,
	vertical=TRUE,	distMetodo="euclidean", hclustMetodo="average" ){

	matriz <- matriz(entrada, cabecalhoColunas, cabecalhoLinhas)

	if( ! is.null(nomes) ){
		nomes <- matriz(nomes, FALSE, FALSE)
		if( ncol(nomes) < nrow(nomes) ) nomes <- t(nomes)
		if( ncol(nomes) != ncol(matriz) ) stop(paste("Quantidade de nomes desejada:",ncol(matriz)))
		colnames(matriz) <- nomes
	}

	if( ! is.null(colunas) ) matriz <- matriz[,numeros(colunas)]
	if( ! is.null(linhas)  ) matriz <- matriz[numeros(linhas),]

	if( transpor ) matriz <- t(matriz)

	total <- ncol(matriz)
	
	distancias <- matrix(0,total,total)
	colnames(distancias) <- colnames(matriz)
	rownames(distancias) <- colnames(matriz)

	for( i in 1:total ){
	    for( j in 1:total ){
	        distancias[i,j] <- dist(t(cbind(matriz[,i],matriz[,j])), method=distMetodo)
	    }
	}

	dendrograma <- as.dendrogram(hclust(as.dist(distancias), method=hclustMetodo))

	plot(
		dendrograma,
		main=titulo,
		xlab=rotulox,
		ylab=rotuloy,
		horiz=!vertical
	)

}

#----------------------------------------

# Converte nomes de genes.
# Genes desconhecidos terão o marcador "#" no ínicio.
#
# entrada: arquivo de entrada
# saida: arquivo de saída
# anotacao: "org.Hs.eg.db" | "org.Sc.sgd.db" | ...
# mapa: "org.Hs.egALIAS2EG" | "org.Hs.egENSEMBL2EG" | "org.Hs.egENSEMBLPROT2EG" | ...
converterNomes <- function( entrada, saida, anotacao="org.Hs.eg.db", mapa="org.Hs.egALIAS2EG" ){

	if( ! anotacao %in% rownames(installed.packages()) ){
        source("http://bioconductor.org/biocLite.R")
        biocLite(anotacao)
    }

	library(anotacao, character.only=TRUE)
	mapa <- get(mapa)

	genes <- as.matrix( read.table(entrada, header=FALSE, colClasses=c("character"), comment.char="", blank.lines.skip=TRUE) )
	total <- length(genes)

	for( i in 1:total ){
	    if( exists(genes[i],mapa) ){
	        genes[i] <- get(genes[i],mapa)
	    }else{
	        genes[i] <- paste("#", genes[i], sep="")
	    }
	}

	write.table( genes, file=saida, sep="\t", quote=FALSE, col.names=FALSE, row.names=FALSE )

}

#----------------------------------------

# Para o exemplo "1-3,8,9-11", retorna-se c(1,2,3,8,9,10,11)
numeros <- function( valor ){
    retorno <- c()
    for( f in unlist(strsplit(valor, ",")) ){
        g <- unlist(strsplit(f, "-"))
        if( length(g) == 1 ){
            retorno <- c(retorno,as.numeric(g))
        }else{
            inicio <- as.numeric(g[1])
            fim    <- as.numeric(g[2])
            for( x in inicio:fim ) retorno <- c(retorno,x)
        }
    }
    retorno
}

#----------------------------------------

# matriz           : matriz ou arquivo CSV
# cabecalhoColunas : contém cabeçalho das colunas na primeira linha?
# cabecalhoLinhas  : contém cabeçalho das linhas na primeira coluna?
matriz <- function( matriz, cabecalhoColunas=TRUE, cabecalhoLinhas=TRUE ){

	if( is.null(nrow(matriz)) ){
		matriz <- read.table(matriz, header=FALSE, blank.lines.skip=TRUE)
	}

	if( cabecalhoLinhas ){
		rownames(matriz) <- matriz[,1]
		matriz <- matriz[,-1]
	}

	if( cabecalhoColunas ){
		colnames(matriz) <- matriz[1,]
		matriz <- matriz[-1,]
	}

	as.matrix(matriz)

}

#----------------------------------------

# DEG - Differentially Expressed Gene
#
# entrada: arquivo de entrada (transcriptogramas)
# saida: arquivo de saída (ranking dos genes)
# controle: índices dos transcriptogramas do grupo de controle (ex.: "1-5,8,9-12")
# alvo: índices dos transcriptogramas do grupo alvo (ex.: "13-21,25,29")
calcularDEG <- function( entrada, saida, controle, alvo ){

    if( ! "GeneSelector" %in% rownames(installed.packages()) ){
        source("http://bioconductor.org/biocLite.R")
        biocLite("GeneSelector")
    }

    library("GeneSelector")

    tabela <- read.table(entrada)
    genes  <- tabela[,1]
    tabela <- tabela[,-1]
    grupo1 <- numeros(controle)
    grupo2 <- numeros(alvo)

    tabcomp <- as.matrix(cbind(tabela[,grupo1],tabela[,grupo2]))
    rownames(tabcomp)<- genes

    classes   <- c(rep(1,length(grupo1)), rep(2,length(grupo2)))
    welchT    <- RankingWelchT(tabcomp, classes, type="unpaired", gene.names=genes)
    resultado <- toplist(welchT, top=length(genes))

    resultado <- cbind(resultado,genes[resultado$index])
    colnames(resultado) <- c("index","statistic","pvalue","gene")
    rownames(resultado) <- resultado$gene

    write.table( resultado, file=saida, sep="\t", quote=FALSE, col.names=TRUE, row.names=FALSE )

}

#----------------------------------------