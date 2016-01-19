#!/bin/bash

if [ "$#" == 5 ]; then
    R -q --no-save -e "source('$TRANSCRIPTOGRAMA/Qualidade.R');qualidadeSeriacaoArquivo(rede='$1',seriacao='$2',fronteiras='$3',indices='$4',saida='$5');"
elif [ "$#" == 6 ]; then
    cabecalho="TRUE"
    if [ "$6" == "N" ]; then cabecalho="FALSE"; fi
    R -q --no-save -e "source('$TRANSCRIPTOGRAMA/Qualidade.R');qualidadeSeriacaoArquivo(rede='$1',seriacao='$2',fronteiras='$3',indices='$4',saida='$5',cabecalho=$cabecalho);"
else
    printf "Calcula índices de qualidade de uma seriação modularizada de um grafo.\n"
    printf "Qualidade.sh <rede> <seriacao> <fronteiras> <indices> <saida> [<cabecalho:S|N>]\n\n"
    printf "<rede>       : arquivo de especificação da rede; CSV sem cabeçalho e com 2 colunas - origem e destino.\n\n"
    printf "<seriacao>   : arquivo com os nomes dos vértices da rede na sequência resultante da seriação.\n\n"
    printf "<fronteiras> : arquivo com uma lista de inteiros; posições de fronteira modular da seriação.\n\n"
    printf "<indices>    : nomes dos índices de qualidade desejados, separados por vírgula. Todos: \"Silhouette,Dunn,Connectivity\".\n\n"
    printf "<saida>      : arquivo no qual o resultado será gravado.\n\n"
    printf "<cabecalho>  : imprimir cabeçalho dos resultados no arquivo de saída? S ou N\n\n"
fi
