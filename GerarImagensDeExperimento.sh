#!/bin/bash

if [ "$1" == ""  ]; then
    echo "GerarImagensDeExperimento.sh <diretório dos experimentos>"
else
    java -Xmx2g -cp $TRANSCRIPTOGRAMA/Java/build/libs/transcriptograma-2015.jar com.joseflavio.transcriptograma.GerarImagensDeExperimento "$1"
fi
