#!/bin/bash

if [ "$1" == "" ]; then
    echo "HeatmapDEG.sh <tgrams> <nomes> <deg> <saida>"
    echo ""
    echo "<tgrams> : Arquivo com os transcriptogramas."
    echo "<nomes>  : Arquivo com o nome de cada transcriptograma."
    echo "<deg>    : Arquivo com o resultado de DEG."
    echo "<saida>  : Arquivo PNG de saída."
else
    R -q --no-save -e "source('$TRANSCRIPTOGRAMA/Transcriptograma.R');gerarHeatmapDEG(transcriptograma='$1',nomenclatura='$2',deg='$3',saida='$4');"
fi
