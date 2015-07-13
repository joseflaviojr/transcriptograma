
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

library(stats)
library(grDevices)
library(tcltk)

DESTAQUE <- "LOCAL"
GARANTIA <- 4
FUNCOES <- 50

arquivo <- tk_choose.files(
	default = getwd(),
	caption = "Selecione a matriz geral de p-values...",
	multi=FALSE,
	filters=matrix(c("Texto", ".txt", "CSV", ".csv", "Todos", "*"), 3, 2, byrow=TRUE)
)

m <- read.csv(arquivo, sep="\t")
m <- cbind(m,0)
linhas <- nrow(m)
colunas <- ncol(m)

if( DESTAQUE == "GLOBAL" ){
	for( i in 1:linhas ){
		for( j in 3:(colunas-1) ){
			if( ! is.na(m[i,j]) ) m[i,colunas] <- m[i,colunas] + m[i,j]
		}
	}
}else{
	for( j in 3:(colunas-1) ){
		m <- m[order(m[,j]),]
		for( i in 1:GARANTIA ){
			if( ! is.na(m[i,j]) ) m[i,colunas] <- -Inf
		}
		for( i in (GARANTIA+1):linhas ){
			if( ! is.na(m[i,j]) ) m[i,colunas] <- ( m[i,colunas] + i ) / 2
		}
	}
}

m <- m[order(m[,colunas]),]
m2 <- data.matrix( m[ 1:FUNCOES, 3:(colunas-1) ] )

row.names(m2) <- m[1:FUNCOES,2]
for( i in 1:FUNCOES ){
	texto <- row.names(m2)[i]
	if( nchar(texto) > 70 ){
		row.names(m2)[i] <- paste(substr(texto,1,67),"...",sep="")
	}
}

rotulos <- c()
for( i in 1:ncol(m2) ) rotulos <- c(rotulos, paste("M",formatC(i,width=2,flag="0"),sep=""))
colnames(m2) <- rotulos

png(
	file=paste(substr(arquivo,1,nchar(arquivo)-4),".png",sep=""),
	width=4096,
	height=2048,
	res=300,
	pointsize=14
)

heatmap(m2, Rowv=NA, Colv=NA, col=gray.colors(256,0), scale="none", main=NA, xlab=NA, ylab=NA)
dev.off()
