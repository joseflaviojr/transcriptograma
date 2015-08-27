#!/bin/bash

if [ "$1" == "" ] || [ "$2" == "" ] || [ "$3" == "" ] || [ "$4" == "" ]; then
    echo "ConverterGenes.sh <anotacao:Hs|Sc> <mapa:ALIAS2EG|ENSEMBL2EG|ENSEMBLPROT2EG|etc> <entrada> <saida>"
else
    anotacao="org.Hs.eg.db"
    mapa="org.Hs.eg"
    if [ "$1" == "Sc" ]; then
        anotacao="org.Sc.sgd.db"
        mapa="org.Sc.sgd"
    fi
    echo "source('$TRANSCRIPTOGRAMA/Transcriptograma.R');converterNomes('$3','$4','$anotacao','$mapa$2');" | R -q --no-save
fi
