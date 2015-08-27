#!/bin/bash

if [ "$1" == "" ]; then
    echo "Heatmap.sh <arquivo> <rotulo> <maximo>"
else
    echo "source('$TRANSCRIPTOGRAMA/Transcriptograma.R');gerarHeatmap(arquivo='$1',rotulo='$2',maximo=$3);" | R -q --no-save
fi
