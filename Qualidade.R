
#
#  Copyright (C) 2016 José Flávio de Souza Dias Júnior
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
#  Direitos Autorais Reservados (C) 2016 José Flávio de Souza Dias Júnior
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

# Calcula índices de qualidade de uma seriação modularizada de um grafo.
# Índices disponíveis: "Silhouette", "Dunn" e "Connectivity".
# Retorna uma lista de valores numéricos reais, cada valor correspondendo a um índice escolhido.
#
# grafo      : rede no formato de grafo da biblioteca "igraph".
# seriacao   : lista com os nomes dos vértices na sequência resultante da seriação.
# fronteiras : lista de inteiros; posições de fronteira modular da seriação.
# indices    : lista com os nomes dos índices de qualidade desejados.
qualidadeSeriacao <- function( grafo, seriacao, fronteiras, indices=c("Silhouette","Dunn","Connectivity") ){

    if( ! "igraph" %in% rownames(installed.packages()) ){
        install.packages("igraph", dependencies=TRUE)
    }

    if( ! "cluster" %in% rownames(installed.packages()) ){
        install.packages("cluster", dependencies=TRUE)
    }

    if( ! "clValid" %in% rownames(installed.packages()) ){
        install.packages("clValid", dependencies=TRUE)
    }

    library(igraph)
    library(cluster)
    library(clValid)

    mdist <- shortest.paths(grafo)
    total <- nrow(mdist)
    mdist[is.infinite(mdist)] <- total
    
    fronteiras <- c(0,fronteiras,total)
    agrupamento <- c()
    for( i in 2:length(fronteiras) ){
        agrupamento <- c(agrupamento, rep(i-1,fronteiras[i]-fronteiras[i-1]))
    }

    mdist <- mdist[seriacao,seriacao]
    agrupamento <- as.integer(agrupamento)

    lista <- c()

    for( i in indices ){
        if( i == "Silhouette" ){
            lista <- c(lista, mean(summary(silhouette(dmatrix=mdist, x=agrupamento))$clus.avg.widths))
        }else if( i == "Dunn" ){
            lista <- c(lista, dunn(distance=mdist, clusters=agrupamento))
        }else if( i == "Connectivity" ){
            lista <- c(lista, connectivity(distance=mdist, clusters=agrupamento, neighbSize=10))
        }
    }

    lista

}

#----------------------------------------

# Executa cluster.stats {fpc} para uma ou duas seriações sobre a mesma rede, a fim de analisar
#    comparativamente a qualidade das clusterizações.
# Retorna uma lista com 1 ou 2 elementos:
#    [[1]] = resultado de cluster.stats para a seriacao1 em função da seriacao2.
#    [[2]] = resultado de cluster.stats para a seriacao2 em função da seriacao1.
#
# grafo       : rede no formato de grafo da biblioteca "igraph".
# seriacao1   : lista com os nomes dos vértices na sequência resultante da primeira seriação de referência.
# fronteiras1 : lista de inteiros; posições de fronteira modular da primeira seriação.
# seriacao2   : seriação alternativa, caso se pretenda comparar com a primeira.
compararSeriacao <- function( grafo, seriacao1, fronteiras1, seriacao2=NULL, fronteiras2=NULL,
                                silhouette=TRUE, G2=FALSE, G3=FALSE, wgap=FALSE, sepindex=FALSE ){

    if( ! "igraph" %in% rownames(installed.packages()) ){
        install.packages("igraph", dependencies=TRUE)
    }

    if( ! "fpc" %in% rownames(installed.packages()) ){
        install.packages("fpc", dependencies=TRUE)
    }

    library(igraph)
    library(fpc)

    mdist <- shortest.paths(grafo)
    total <- nrow(mdist)
    mdist[is.infinite(mdist)] <- total
    
    fronteiras1 <- c(0,fronteiras1,total)
    agrupamento1 <- c()
    for( i in 2:length(fronteiras1) ){
        agrupamento1 <- c(agrupamento1, rep(i-1,fronteiras1[i]-fronteiras1[i-1]))
    }

    ordem1 <- order(seriacao1)
    agrupamento1 <- agrupamento1[ordem1]

    elementos <- seriacao1[ordem1]
    mdist <- mdist[elementos,elementos]

    if( ! is.null(seriacao2) ){

        fronteiras2 <- c(0,fronteiras2,total)
        agrupamento2 <- c()
        for( i in 2:length(fronteiras2) ){
            agrupamento2 <- c(agrupamento2, rep(i-1,fronteiras2[i]-fronteiras2[i-1]))
        }

        ordem2 <- order(seriacao2)
        agrupamento2 <- agrupamento2[ordem2]

        r1 <- cluster.stats(d=mdist,
                clustering=agrupamento1,
                alt.clustering=agrupamento2,
                noisecluster=FALSE,
                silhouette=silhouette,
                G2=G2, G3=G3,
                wgap=wgap,
                sepindex=sepindex, sepprob=0.1, sepwithnoise=TRUE,
                compareonly=FALSE, aggregateonly=FALSE)

        r2 <- cluster.stats(d=mdist,
                clustering=agrupamento2,
                alt.clustering=agrupamento1,
                noisecluster=FALSE,
                silhouette=silhouette,
                G2=G2, G3=G3,
                wgap=wgap,
                sepindex=sepindex, sepprob=0.1, sepwithnoise=TRUE,
                compareonly=FALSE, aggregateonly=FALSE)

        resultado <- list(r1, r2)

    }else{

        r1 <- cluster.stats(d=mdist,
                clustering=agrupamento1,
                alt.clustering=NULL,
                noisecluster=FALSE,
                silhouette=silhouette,
                G2=G2, G3=G3,
                wgap=wgap,
                sepindex=sepindex, sepprob=0.1, sepwithnoise=TRUE,
                compareonly=FALSE, aggregateonly=FALSE)

        resultado <- list(r1)
        
    }

    resultado

}

#----------------------------------------

# Calcula a silhouette {cluster} de uma seriação modularizada de um grafo.
# Retorna um objeto do tipo "sil".
#
# grafo      : rede no formato de grafo da biblioteca "igraph".
# seriacao   : lista com os nomes dos vértices na sequência resultante da seriação.
# fronteiras : lista de inteiros; posições de fronteira modular da seriação.
silhouetteSeriacao <- function( grafo, seriacao, fronteiras ){

    if( ! "igraph" %in% rownames(installed.packages()) ){
        install.packages("igraph", dependencies=TRUE)
    }

    if( ! "cluster" %in% rownames(installed.packages()) ){
        install.packages("cluster", dependencies=TRUE)
    }

    library(igraph)
    library(cluster)

    mdist <- shortest.paths(grafo)
    total <- nrow(mdist)
    mdist[is.infinite(mdist)] <- total
    
    fronteiras <- c(0,fronteiras,total)
    agrupamento <- c()
    for( i in 2:length(fronteiras) ){
        agrupamento <- c(agrupamento, rep(i-1,fronteiras[i]-fronteiras[i-1]))
    }

    mdist <- mdist[seriacao,seriacao]
    agrupamento <- as.integer(agrupamento)

    silhouette(dmatrix=mdist, x=agrupamento)

}

#----------------------------------------

# Calcula índices de qualidade de uma seriação modularizada de um grafo, especificado em arquivo externo.
# Ver função qualidadeSeriacao() para mais informações.
# 
# rede       : arquivo de especificação da rede; CSV sem cabeçalho e com 2 colunas - origem e destino.
# orientada  : rede orientada? (grafo)
# seriacao   : arquivo com os nomes dos vértices da rede na sequência resultante da seriação.
# fronteiras : arquivo com uma lista de inteiros; posições de fronteira modular da seriação.
# indices    : nomes dos índices de qualidade desejados, separados por vírgula. Padrão: "Silhouette,Dunn,Connectivity".
# saida      : arquivo no qual o resultado será acrescentado.
# cabecalho  : imprimir cabeçalho dos resultados no arquivo de saída?
qualidadeSeriacaoArquivo <- function( rede, orientada=F, seriacao, fronteiras, indices="Silhouette,Dunn,Connectivity", saida, cabecalho=TRUE ){

    if( ! "igraph" %in% rownames(installed.packages()) ){
        install.packages("igraph", dependencies=TRUE)
    }

    library(igraph)

    grafo_ <- graph.data.frame(read.table(rede,       header=F), directed=orientada)
    seria_ <- as.character(    read.table(seriacao,   header=F)[,1])
    front_ <- as.integer  (    read.table(fronteiras, header=F)[1,])
    indic_ <- c(unlist(strsplit(indices,",")))

    resultado <- qualidadeSeriacao(grafo=grafo_, seriacao=seria_, fronteiras=front_, indices=indic_)

    sink(saida, append=T)

    if( cabecalho ){
        cat(indic_, sep="\t")
        cat("\n")
    }

    cat(resultado, sep="\t")
    cat("\n")

    sink()

}

#----------------------------------------

# Calcula a qualidade de uma ou duas seriações modularizadas, comparando-as se possível.
# Ver função compararSeriacao() para mais informações.
# 
# rede        : arquivo de especificação da rede; CSV sem cabeçalho e com 2 colunas - origem e destino.
# orientada   : rede orientada? (grafo)
# seriacao1   : arquivo com os nomes dos vértices da rede na sequência resultante da primeira seriação.
# fronteiras1 : arquivo com uma lista de inteiros; posições de fronteira modular da primeira seriação.
# seriacao2   : arquivo com os nomes dos vértices da rede na sequência resultante da segunda seriação.
# fronteiras2 : arquivo com uma lista de inteiros; posições de fronteira modular da segunda seriação.
# saida       : arquivo no qual o resultado será gravado.
compararSeriacaoArquivo <- function( rede, orientada=F, seriacao1, fronteiras1, seriacao2=NULL, fronteiras2=NULL, saida ){

    if( ! "igraph" %in% rownames(installed.packages()) ){
        install.packages("igraph", dependencies=TRUE)
    }

    library(igraph)

    grafo  <- graph.data.frame(read.table(rede,        header=F), directed=orientada)
    s1     <- as.character(    read.table(seriacao1,   header=F)[,1])
    f1     <- as.integer  (    read.table(fronteiras1, header=F)[1,])
    if( ! is.null(seriacao2) ){
        s2 <- as.character(    read.table(seriacao2,   header=F)[,1])
        f2 <- as.integer  (    read.table(fronteiras2, header=F)[1,])
    }else{
        s2 <- NULL
        f2 <- NULL
    }

    resultado <- compararSeriacao(grafo, seriacao1=s1, fronteiras1=f1, seriacao2=s2, fronteiras2=f2)

    sink(saida)
    print("--------------------------------------------")
    print(seriacao1)
    print(f1)
    print(resultado[[1]])
    print("--------------------------------------------")
    if( ! is.null(seriacao2) ){
        print(seriacao2)
        print(f2)
        print(resultado[[2]])
        print("--------------------------------------------")
    }
    sink()

}

#----------------------------------------

# Calcula a qualidade, conforme o método da silhouette, de uma seriação modularizada.
# Ver função silhouetteSeriacao() para mais informações.
# 
# rede       : arquivo de especificação da rede; CSV sem cabeçalho e com 2 colunas - origem e destino.
# orientada  : rede orientada? (grafo)
# seriacao   : arquivo com os nomes dos vértices da rede na sequência resultante da seriação.
# fronteiras : arquivo com uma lista de inteiros; posições de fronteira modular da seriação.
# saida      : arquivo no qual o resultado será gravado.
silhouetteSeriacaoArquivo <- function( rede, orientada=F, seriacao, fronteiras, saida ){

    if( ! "igraph" %in% rownames(installed.packages()) ){
        install.packages("igraph", dependencies=TRUE)
    }

    library(igraph)

    grafo  <- graph.data.frame(read.table(rede,       header=F), directed=orientada)
    gs     <- as.character(    read.table(seriacao,   header=F)[,1])
    gf     <- as.integer  (    read.table(fronteiras, header=F)[1,])

    resultado <- silhouetteSeriacao(grafo, seriacao=gs, fronteiras=gf)
    resumo    <- summary(resultado)

    indices   <- resumo$clus.avg.widths[order(resumo$clus.avg.widths, decreasing=T)]
    maiores   <- indices[1:5]
    menores   <- indices[(length(indices)-4):length(indices)]

    sink(saida)
    
    cat("Resumo\n")
    print(summary(resumo$clus.avg.widths))

    cat("\nMaiores\n")
    cat(maiores, sep=", ")

    cat("\n\nMaiores Media\n")
    cat(mean(maiores))

    cat("\n\nMenores\n")
    cat(menores, sep=", ")

    cat("\n\nMenores Media\n")
    cat(mean(menores))

    cat("\n\nSilhouette\n")
    cat(resumo$clus.avg.widths, sep=", ")

    cat("\n\nTamanho\n")
    cat(resumo$clus.sizes, sep=", ")

    sink()

}

#----------------------------------------