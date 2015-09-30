#!/bin/bash

if [ "$1" == "" ]; then
    echo "DEG - Differentially Expressed Gene"
    echo "DEG.sh <entrada> <saida> <controle> <alvo>"
    echo "<entrada> : arquivo de entrada (transcriptogramas)"
    echo "<saida>   : arquivo de saída (ranking dos genes)"
    echo "<controle>: índices do grupo de controle (ex.: \"1-5,8,9-12\")"
    echo "<alvo>    : índices do grupo alvo (ex.: \"13-21,25,29\")"
else
    echo "source('$TRANSCRIPTOGRAMA/Transcriptograma.R');calcularDEG('$1','$2','$3','$4');" | R -q --no-save
fi
