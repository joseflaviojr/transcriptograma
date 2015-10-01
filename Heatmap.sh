#!/bin/bash

if [ "$1" == "" ]; then
    echo "Heatmap.sh <arquivo> <rotulo> <maximo>"
    echo ""
    echo "Heat map de arquivo com valores numéricos."
    echo "Quanto menor o valor, maior o destaque (temperatura)."
    echo "O arquivo deve estar no formato CSV, tabulado e conter cabeçalho."
    echo "Coluna 1 = sigla"
    echo "Coluna 2 = descrição curta"
    echo "Coluna 3 = primeiro valor da linha"
    echo "Coluna N = n-ésimo valor da linha"
    echo "maximo: quantidade máxima de linhas no heat map"
else
    R -q --no-save -e "source('$TRANSCRIPTOGRAMA/Transcriptograma.R');gerarHeatmap(arquivo='$1',rotulo='$2',maximo=$3);"
fi
