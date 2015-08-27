#!/bin/bash

if [ "$1" == "" ]; then
    echo "Heatmap.sh <arquivo> <maximo>"
else
    echo "source('$TRANSCRIPTOGRAMA/Transcriptograma.R');gerarHeatmap(arquivo='$1',maximo=$2);" | R -q --no-save
fi
