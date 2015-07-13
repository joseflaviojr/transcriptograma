#!/bin/bash

if [ "$1" == ""  ]; then
    echo "GerarImagensDeExperimento.sh <diretorio do experimento>"
else
    java -cp $TRANSCRIPTOGRAMA/Java/build/libs/transcriptograma-2015.jar com.joseflavio.transcriptograma.GerarImagensDeExperimento "$1"
fi
