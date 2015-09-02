#!/bin/bash

if [ "$1" == ""  ]; then
    echo "Estatistica.sh <diretório dos experimentos>"
    echo "O nome dos experimentos deve seguir o padrão de GeradorDeExperimentos.R"
    echo "Executar antes o script GerarImagensDeExperimento.sh"
else
    java -cp $TRANSCRIPTOGRAMA/Java/build/libs/transcriptograma-2015.jar com.joseflavio.transcriptograma.Estatistica "$1"
    java -cp $TRANSCRIPTOGRAMA/Java/build/libs/transcriptograma-2015.jar com.joseflavio.transcriptograma.EstatisticaProporcao "$1"
    java -cp $TRANSCRIPTOGRAMA/Java/build/libs/transcriptograma-2015.jar com.joseflavio.transcriptograma.EstatisticaMedia "$1/Estatistica.txt"
    java -cp $TRANSCRIPTOGRAMA/Java/build/libs/transcriptograma-2015.jar com.joseflavio.transcriptograma.EstatisticaMedia "$1/Proporcao.txt"
fi
