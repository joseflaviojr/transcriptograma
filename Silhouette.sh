#!/bin/bash

if [ "$#" == 4 ]; then
    R -q --no-save -e "source('$TRANSCRIPTOGRAMA/Qualidade.R');silhouetteSeriacaoArquivo(rede='$1',orientada=F,seriacao='$2',fronteiras='$3',saida='$4');"
else
    echo "Calcula a qualidade, conforme o método da silhouette, de uma seriação modularizada."
    echo "Silhouette.sh <rede> <seriacao> <fronteiras> <saida>"
    echo ""
    echo "<rede>       : arquivo de especificação da rede; CSV sem cabeçalho e com 2 colunas - origem e destino."
    echo ""
    echo "<seriacao>   : arquivo com os nomes dos vértices da rede na sequência resultante da seriação."
    echo ""
    echo "<fronteiras> : arquivo com uma lista de inteiros; posições de fronteira modular da seriação."
    echo ""
    echo "<saida>      : arquivo no qual o resultado será gravado."
fi
