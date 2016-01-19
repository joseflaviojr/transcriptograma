#!/bin/bash

java -Xmx2g -cp $TRANSCRIPTOGRAMA/Java/build/libs/transcriptograma-2015.jar com.joseflavio.transcriptograma.GerarImagem "$1" "$2" "$3"
