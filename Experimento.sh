#!/bin/bash

function Algoritmo {
	echo $(date "+%Y-%m-%d %H:%M:%S")
	echo "$1 $2 [$3]"
	nice -n -20 $6 "$2" "$3" "$4" "$5"
	mv -f "$2.txt" "$2.$1[$3].txt"
	mv -f "$2.estat.txt" "$2.$1[$3].estat.txt"
}

function Experimento {
	for semente in $(seq 1 $4); do
		for matriz in $1; do
			if [[ $5 == *"Cla"* ]]; then
				Algoritmo "CLA" "$matriz" $semente $2 $3 "Claritate"
			fi
			if [[ $5 == *"CFM"* ]]; then
				Algoritmo "CFM" "$matriz" $semente $2 $3 "CFM"
			fi
			if [[ $5 == *"DEM"* ]]; then
				Algoritmo "DEM" "$matriz" $semente $2 $3 "CodigoDemocratico"
			fi
		done
	done
}

if [ "$1" == ""  ] || [ "$2" == ""  ] || [ "$3" == ""  ] || [ "$4" == ""  ] || [ "$5" == ""  ]; then
	echo "Experimento.sh <arquivos> <intervalo(seg)> <registros> <execucoes> <algoritmos>"
	echo "Exemplo: Experimento.sh \"*.csv\" 10 60 5 Cla,CFM"
	echo "Algoritmos: Claritate(Cla), Cost Function Method(CFM), Codigo Democratico(DEM)"
else
	Experimento "$1" "$2" "$3" "$4" "$5"
fi
