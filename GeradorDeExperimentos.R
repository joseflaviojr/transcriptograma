
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

#------------------------------------------------------------------#
# Legenda:
# RAL-O = Rede Aleatória Orientada
# RAL-N = Rede Aleatória Não Orientada
# RLE-O = Rede Livre de Escala (Barabasi) Orientada
# RLE-N = Rede Livre de Escala (Barabasi) Não Orientada
# RPM-O = Rede Pequeno Mundo Orientada
# RPM-N = Rede Pequeno Mundo Não Orientada
# ARQ-O = Rede orientada real, especificada em arquivo externo
# ARQ-N = Rede não orientada real, especificada em arquivo externo
# [III] XXX-X(NNNN)(GGG)(PI-PE) T = III: identificação; NNNN: vértices; XXX-X: tipo da rede;
#                                   GGG: grupos de mesmo tamanho; xGG: grupos de diferentes tamanhos;
#                                   PI: probabilidade de ligação interna de grupo; PE: probabilidade externa
#                                   T: comentário opcional
#------------------------------------------------------------------#
library(igraph)
#------------------------------------------------------------------#
# Cria grafo aleatório simples com vértices distribuídos em grupos de mesmo tamanho.
RAL <- function( vertices, orientado=FALSE, grupos=3, probInterna=0.3, probExterna=0.02, embaralhar=TRUE ){
	
	rede <- matrix(0L,vertices,vertices)
	rede <- apply( rede, c(1,2), function(x){ if( runif(1,0,1) <= probExterna ) 1L else 0L } )

	quantPorGrupo <- trunc(vertices/grupos)
	for( grupo in 1:grupos ){
		inicio <- ( grupo - 1 ) * quantPorGrupo + 1
		fim <- if( grupo == grupos ) vertices else grupo * quantPorGrupo
		for( i in inicio:fim ){
			for( j in inicio:fim ){
				rede[i,j] <- if( runif(1,0,1) <= probInterna ) 1L else 0L
			}
		}
	}

	for( i in 1:vertices ) rede[i,i] <- 0L

	if( embaralhar ){
		desordem <- sample.int(vertices)
		rede <- rede[desordem,desordem]
	}

	graph.adjacency( rede, mode = if(orientado) "directed" else "undirected" )

}
#------------------------------------------------------------------#
# Cria grafo aleatório simples com vértices distribuídos em grupos de tamanho especificado.
RAX <- function( vertices, orientado=FALSE, grupos=c(10,10,10), probInterna=0.3, probExterna=0.02, embaralhar=TRUE ){
	
	rede <- matrix(0L,vertices,vertices)
	rede <- apply( rede, c(1,2), function(x){ if( runif(1,0,1) <= probExterna ) 1L else 0L } )

	i <- 1
	for( grupo in 1:length(grupos) ){
		inicio <- i
		fim <- inicio + grupos[grupo] - 1
		for( i in inicio:fim ){
			for( j in inicio:fim ){
				rede[i,j] <- if( runif(1,0,1) <= probInterna ) 1L else 0L
			}
		}
		i <- i + 1
	}

	for( i in 1:vertices ) rede[i,i] <- 0L

	if( embaralhar ){
		desordem <- sample.int(vertices)
		rede <- rede[desordem,desordem]
	}

	graph.adjacency( rede, mode = if(orientado) "directed" else "undirected" )

}
#------------------------------------------------------------------#
# Cria grafo conforme modelo Barabasi-Albert (Rede Livre de Escala).
RLE <- function( vertices, orientado=FALSE, poder=1, embaralhar=TRUE ){

	rede <- barabasi.game( vertices, directed=orientado, power=poder )

	if( embaralhar ){
		desordem <- sample.int(vertices)
		rede <- as.matrix(rede[])[desordem,desordem]
		rede <- graph.adjacency( rede, mode = if(orientado) "directed" else "undirected" )
	}

	rede

}
#------------------------------------------------------------------#
# Cria grafo conforme modelo de rede denominado Pequeno Mundo.
RPM <- function( vertices, orientado=FALSE, embaralhar=TRUE ){

	rede <- graph.ring( vertices, directed=orientado, mutual=FALSE, circular=TRUE )
	rede <- rewire.edges( rede, prob=0.5, loops=FALSE, multiple=FALSE )

	if( embaralhar ){
		desordem <- sample.int(vertices)
		rede <- as.matrix(rede[])[desordem,desordem]
		rede <- graph.adjacency( rede, mode = if(orientado) "directed" else "undirected" )
	}

	rede

}
#------------------------------------------------------------------#
# Cria grafo simples com arestas especificadas em arquivo externo.
ARQ <- function( arquivo, orientado=FALSE ){
	rede <- read.table(arquivo)
	colnames(rede) <- c("Origem","Destino")
	graph.data.frame( rede, directed=orientado )
}
#------------------------------------------------------------------#
# Salva o grafo em arquivo externo no formato CSV.
salvar <- function( grafo, arquivo ){
	write.table( as.matrix(grafo[]), file=arquivo, sep=",", col.names=F, row.names=F )
}
#------------------------------------------------------------------#
gerarExperimentos01 <- function(){

	# Redes aleatórias

	set.seed(1)
	grafo <- RAL(  100, grupos=2,  probInterna=0.4, probExterna=0.02, orientado=FALSE, embaralhar=TRUE )
	salvar( grafo, "[001] RAL-N(0100)(002)(0,400-0,020).csv" )

	set.seed(1)
	grafo <- RAL(  100, grupos=2,  probInterna=0.4, probExterna=0.04, orientado=FALSE, embaralhar=TRUE )
	salvar( grafo, "[002] RAL-N(0100)(002)(0,400-0,040).csv" )

	set.seed(1)
	grafo <- RAL(  100, grupos=2,  probInterna=0.4, probExterna=0.08, orientado=FALSE, embaralhar=TRUE )
	salvar( grafo, "[003] RAL-N(0100)(002)(0,400-0,080).csv" )

	set.seed(1)
	grafo <- RAL(  100, grupos=2,  probInterna=0.6, probExterna=0.02, orientado=FALSE, embaralhar=TRUE )
	salvar( grafo, "[004] RAL-N(0100)(002)(0,600-0,020).csv" )

	set.seed(1)
	grafo <- RAL(  100, grupos=2,  probInterna=0.4, probExterna=0.02, orientado=TRUE,  embaralhar=TRUE )
	salvar( grafo, "[005] RAL-O(0100)(002)(0,400-0,020).csv" )

	set.seed(1)
	grafo <- RAL(  100, grupos=2,  probInterna=0.4, probExterna=0.08, orientado=TRUE,  embaralhar=TRUE )
	salvar( grafo, "[006] RAL-O(0100)(002)(0,400-0,080).csv" )

	set.seed(1)
	grafo <- RAL(  100, grupos=3,  probInterna=0.4, probExterna=0.02, orientado=FALSE, embaralhar=TRUE )
	salvar( grafo, "[007] RAL-N(0100)(003)(0,400-0,020).csv" )

	set.seed(1)
	grafo <- RAL(  100, grupos=3,  probInterna=0.4, probExterna=0.04, orientado=FALSE, embaralhar=TRUE )
	salvar( grafo, "[008] RAL-N(0100)(003)(0,400-0,040).csv" )

	set.seed(1)
	grafo <- RAL(  100, grupos=3,  probInterna=0.4, probExterna=0.08, orientado=FALSE, embaralhar=TRUE )
	salvar( grafo, "[009] RAL-N(0100)(003)(0,400-0,080).csv" )

	set.seed(1)
	grafo <- RAL(  100, grupos=3,  probInterna=0.6, probExterna=0.02, orientado=FALSE, embaralhar=TRUE )
	salvar( grafo, "[010] RAL-N(0100)(003)(0,600-0,020).csv" )

	set.seed(1)
	grafo <- RAL(  100, grupos=3,  probInterna=0.4, probExterna=0.02, orientado=TRUE,  embaralhar=TRUE )
	salvar( grafo, "[011] RAL-O(0100)(003)(0,400-0,020).csv" )

	set.seed(1)
	grafo <- RAL(  100, grupos=3,  probInterna=0.4, probExterna=0.08, orientado=TRUE,  embaralhar=TRUE )
	salvar( grafo, "[012] RAL-O(0100)(003)(0,400-0,080).csv" )

	set.seed(1)
	grafo <- RAL(  100, grupos=10, probInterna=0.5, probExterna=0.02, orientado=FALSE, embaralhar=TRUE )
	salvar( grafo, "[013] RAL-N(0100)(010)(0,500-0,020).csv" )

	set.seed(1)
	grafo <- RAL(  100, grupos=10, probInterna=0.5, probExterna=0.04, orientado=FALSE, embaralhar=TRUE )
	salvar( grafo, "[014] RAL-N(0100)(010)(0,500-0,040).csv" )

	set.seed(1)
	grafo <- RAL(  100, grupos=10, probInterna=0.5, probExterna=0.08, orientado=FALSE, embaralhar=TRUE )
	salvar( grafo, "[015] RAL-N(0100)(010)(0,500-0,080).csv" )

	set.seed(1)
	grafo <- RAL(  100, grupos=10, probInterna=0.5, probExterna=0.02, orientado=TRUE,  embaralhar=TRUE )
	salvar( grafo, "[016] RAL-O(0100)(010)(0,500-0,020).csv" )

	set.seed(1)
	grafo <- RAL(  100, grupos=10, probInterna=0.5, probExterna=0.08, orientado=TRUE,  embaralhar=TRUE )
	salvar( grafo, "[017] RAL-O(0100)(010)(0,500-0,080).csv" )

	set.seed(1)
	grafo <- RAX(  100, grupos=c(10,20,35,5,10,20), probInterna=0.5, probExterna=0.01, orientado=FALSE, embaralhar=TRUE )
	salvar( grafo, "[018] RAL-N(0100)(x06)(0,500-0,010).csv" )

	set.seed(1)
	grafo <- RAX(  100, grupos=c(10,20,7,10,20,8,5,20), probInterna=0.4, probExterna=0.01, orientado=FALSE, embaralhar=TRUE )
	salvar( grafo, "[019] RAL-N(0100)(x08)(0,400-0,010).csv" )

	set.seed(1)
	grafo <- RAX(  100, grupos=c(10,20,7,10,20,8,5,20), probInterna=0.6, probExterna=0.01, orientado=TRUE, embaralhar=TRUE )
	salvar( grafo, "[020] RAL-O(0100)(x08)(0,600-0,010).csv" )

	set.seed(1)
	grafo <- RAL(  200, grupos=25, probInterna=0.5, probExterna=0.01, orientado=FALSE,  embaralhar=TRUE )
	salvar( grafo, "[021] RAL-N(0200)(025)(0,500-0,010).csv" )

	set.seed(1)
	grafo <- RAL(  300, grupos=25, probInterna=0.5, probExterna=0.01, orientado=FALSE,  embaralhar=TRUE )
	salvar( grafo, "[022] RAL-N(0300)(025)(0,500-0,010).csv" )

	set.seed(1)
	grafo <- RAL(  400, grupos=25, probInterna=0.5, probExterna=0.01, orientado=FALSE,  embaralhar=TRUE )
	salvar( grafo, "[023] RAL-N(0400)(025)(0,500-0,010).csv" )

	set.seed(1)
	grafo <- RAL(  500, grupos=25, probInterna=0.5, probExterna=0.01, orientado=FALSE,  embaralhar=TRUE )
	salvar( grafo, "[024] RAL-N(0500)(025)(0,500-0,010).csv" )

	set.seed(1)
	grafo <- RAL( 1000, grupos=25, probInterna=0.5, probExterna=0.01, orientado=FALSE,  embaralhar=TRUE )
	salvar( grafo, "[025] RAL-N(1000)(025)(0,500-0,010).csv" )

	# Redes livres de escala

	set.seed(1)
	grafo <- RLE(  100, orientado=FALSE, embaralhar=TRUE )
	salvar( grafo, "[026] RLE-N(0100).csv" )

	set.seed(1)
	grafo <- RLE(  100, orientado=FALSE, embaralhar=TRUE )
	salvar( grafo, "[027] RLE-N(0100).csv" )

	set.seed(1)
	grafo <- RLE(  100, orientado=TRUE,  embaralhar=TRUE )
	salvar( grafo, "[028] RLE-O(0100).csv" )

	set.seed(1)
	grafo <- RLE(  200, orientado=FALSE, embaralhar=TRUE )
	salvar( grafo, "[029] RLE-N(0200).csv" )

	set.seed(1)
	grafo <- RLE(  300, orientado=FALSE, embaralhar=TRUE )
	salvar( grafo, "[030] RLE-N(0300).csv" )

	set.seed(1)
	grafo <- RLE(  400, orientado=FALSE, embaralhar=TRUE )
	salvar( grafo, "[031] RLE-N(0400).csv" )

	set.seed(1)
	grafo <- RLE(  400, orientado=TRUE,  embaralhar=TRUE )
	salvar( grafo, "[032] RLE-O(0400).csv" )

	set.seed(1)
	grafo <- RLE(  500, orientado=FALSE, embaralhar=TRUE )
	salvar( grafo, "[033] RLE-N(0500).csv" )

	set.seed(1)
	grafo <- RLE(  500, orientado=TRUE,  embaralhar=TRUE )
	salvar( grafo, "[034] RLE-O(0500).csv" )

	set.seed(1)
	grafo <- RLE( 1000, orientado=FALSE, embaralhar=TRUE )
	salvar( grafo, "[035] RLE-N(1000).csv" )

	# Redes pequeno mundo

	set.seed(1)
	grafo <- RPM(  100, orientado=FALSE, embaralhar=TRUE )
	salvar( grafo, "[036] RPM-N(0100).csv" )

	set.seed(1)
	grafo <- RPM(  100, orientado=FALSE, embaralhar=TRUE )
	salvar( grafo, "[037] RPM-N(0100).csv" )

	set.seed(1)
	grafo <- RPM(  100, orientado=TRUE,  embaralhar=TRUE )
	salvar( grafo, "[038] RPM-O(0100).csv" )

	set.seed(1)
	grafo <- RPM(  200, orientado=FALSE, embaralhar=TRUE )
	salvar( grafo, "[039] RPM-N(0200).csv" )

	set.seed(1)
	grafo <- RPM(  300, orientado=FALSE, embaralhar=TRUE )
	salvar( grafo, "[040] RPM-N(0300).csv" )

	set.seed(1)
	grafo <- RPM(  400, orientado=FALSE, embaralhar=TRUE )
	salvar( grafo, "[041] RPM-N(0400).csv" )

	set.seed(1)
	grafo <- RPM(  400, orientado=TRUE,  embaralhar=TRUE )
	salvar( grafo, "[042] RPM-O(0400).csv" )

	set.seed(1)
	grafo <- RPM(  500, orientado=FALSE, embaralhar=TRUE )
	salvar( grafo, "[043] RPM-N(0500).csv" )

	set.seed(1)
	grafo <- RPM(  500, orientado=TRUE,  embaralhar=TRUE )
	salvar( grafo, "[044] RPM-O(0500).csv" )

	set.seed(1)
	grafo <- RPM( 1000, orientado=FALSE, embaralhar=TRUE )
	salvar( grafo, "[045] RPM-N(1000).csv" )

}
#------------------------------------------------------------------#
gerarExperimentos02 <- function(){

	experimento <- 1
	semente <- 1

	for( tamanho in seq(100, 1000, by=100) ){
		for( poder in seq(0.5, 1, by=0.5) ){
			for( n in 1:2 ){

				set.seed(semente)
				
				grafo <- RLE( tamanho, orientado=FALSE, poder=poder, embaralhar=TRUE )
				salvar( grafo, paste("[", formatC(experimento, width=3, flag="0"), "] RLE-N(", formatC(tamanho, width=4, flag="0"), ").csv", sep="") )

				experimento <- experimento + 1
				semente <- semente + 1

			}
		}
	}

}
#------------------------------------------------------------------#