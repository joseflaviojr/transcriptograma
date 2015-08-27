#!/bin/bash

if [ "$1" == "" ] || [ "$2" == "" ] || [ "$3" == "" ] || [ "$4" == "" ]; then
    echo "Enriquecer.sh <anotacao:Hs|Sc> <ontologia:BP|MF|CC> <saida> <arquivo1> <arquivoN>"
else
    anotacao="org.Hs.eg.db"
    if [ "$1" == "Sc" ]; then anotacao="org.Sc.sgd.db"; fi
    arquivos="'$4'"
    for (( c=5; c<=$#; c++ ))
    do
        arquivos="$arquivos,'${!c}'"
    done
    echo "source('$TRANSCRIPTOGRAMA/Transcriptograma.R');enriquecer(anotacao='$anotacao',ontologia='$2',saida='$3',arquivos=c($arquivos));" | R -q --no-save
fi
