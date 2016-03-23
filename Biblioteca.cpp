
/*
 *  Copyright (C) 2015-2016 José Flávio de Souza Dias Júnior
 *  
 *  This file is part of Transcriptograma - <http://www.joseflavio.com/transcriptograma/>.
 *  
 *  Transcriptograma is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  Transcriptograma is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU Lesser General Public License for more details.
 *  
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with Transcriptograma. If not, see <http://www.gnu.org/licenses/>.
 */

/*
 *  Direitos Autorais Reservados (C) 2015-2016 José Flávio de Souza Dias Júnior
 * 
 *  Este arquivo é parte de Transcriptograma - <http://www.joseflavio.com/transcriptograma/>.
 * 
 *  Transcriptograma é software livre: você pode redistribuí-lo e/ou modificá-lo
 *  sob os termos da Licença Pública Menos Geral GNU conforme publicada pela
 *  Free Software Foundation, tanto a versão 3 da Licença, como
 *  (a seu critério) qualquer versão posterior.
 * 
 *  Transcriptograma é distribuído na expectativa de que seja útil,
 *  porém, SEM NENHUMA GARANTIA; nem mesmo a garantia implícita de
 *  COMERCIABILIDADE ou ADEQUAÇÃO A UMA FINALIDADE ESPECÍFICA. Consulte a
 *  Licença Pública Menos Geral do GNU para mais detalhes.
 * 
 *  Você deve ter recebido uma cópia da Licença Pública Menos Geral do GNU
 *  junto com Transcriptograma. Se não, veja <http://www.gnu.org/licenses/>.
 */

#include "Biblioteca.h"

/* Permutação */
void Permutar( int* ordem, int total, int a, int b ) {
	int x = ordem[a];
	ordem[a] = ordem[b];
	ordem[b] = x;
}

/* Permutação */
void Permutar( int** matriz, int* ordem, int total, int a, int b ) {

	int i, x;

	for( i = 0; i < total; i++ ){
		x = matriz[a][i];
		matriz[a][i] = matriz[b][i];
		matriz[b][i] = x;
	}

	for( i = 0; i < total; i++ ){
		x = matriz[i][a];
		matriz[i][a] = matriz[i][b];
		matriz[i][b] = x;
	}

	x = ordem[a];
	ordem[a] = ordem[b];
	ordem[b] = x;

}

/* Permutação */
void Permutar( int** matriz1, int** matriz2, int* ordem, int total, int a, int b ) {

	int i, x;

	for( i = 0; i < total; i++ ){

		x = matriz1[a][i];
		matriz1[a][i] = matriz1[b][i];
		matriz1[b][i] = x;

		x = matriz2[a][i];
		matriz2[a][i] = matriz2[b][i];
		matriz2[b][i] = x;

	}

	for( i = 0; i < total; i++ ){

		x = matriz1[i][a];
		matriz1[i][a] = matriz1[i][b];
		matriz1[i][b] = x;

		x = matriz2[i][a];
		matriz2[i][a] = matriz2[i][b];
		matriz2[i][b] = x;

	}

	x = ordem[a];
	ordem[a] = ordem[b];
	ordem[b] = x;

}

/* Deslocamento intraordem */
void Deslocar( int posicao_atual, int posicao_nova, int* ordem, int total ) {

	int valor = ordem[posicao_atual];

	if( posicao_atual <= posicao_nova ){
		for( int i = posicao_atual; i < posicao_nova; i++ ) ordem[i] = ordem[i+1];
	}else{
		for( int i = posicao_atual; i > posicao_nova; i-- ) ordem[i] = ordem[i-1];
	}

	ordem[posicao_nova] = valor;

}

/* Arquivo de entrada no formato CSV */
void CSV_Arquivo( ifstream* origem, int** destino1, int** destino2, int largura ) {
	
	char numero[50], c;
	int  d = 0, i = 0, j = 0, v;
	bool destinosDiferentes = destino2 != destino1;

	while( ! (*origem).eof() ){

		c = (*origem).get();

		switch( c ){
			case ',' :
			case ';' :
			case '|' :
			case ' ' :
			case '\t' :
			case '\n' :
				numero[d++] = '\0';
				v = strtol( numero, NULL, 10 );
				destino1[i][j] = v;
				if( destinosDiferentes ) destino2[i][j] = v;
				j++;
				d = 0;
				break;
			case '"' :
			case '\'' :
			case '\r' :
				break;
			default :
				numero[d++] = c;
				break;
		}

		if( j == largura ){
			i++;
			j = 0;
		}

	}

	if( d > 0 && j > 0 && j < largura ){
		numero[d++] = '\0';
		v = strtol( numero, NULL, 10 );
		destino1[i][j] = v;
		if( destinosDiferentes ) destino2[i][j] = v;
	}

}

/* Ordem da Matriz de Adjacência */
int CSV_Largura( ifstream* origem ){
	int total = 1;
	char c;
	while( ! (*origem).eof() ){
		c = (*origem).get();
		if( c == '\n' ) break;
		if( c == ',' || c == ';' || c == '|' || c == ' ' || c == '\t' ) total++;
	}
	(*origem).seekg( 0, (*origem).beg );
	return total;
}

/* Impressão de Resultado */
void ImprimirResultado( time_t* inicio, time_t* agora, time_t* melhoria, int* ordem, int total, ostream* saida ) {

	// Tempo decorrido
	*saida << (long)difftime( *agora, *inicio ) << endl << (long)difftime( *melhoria, *inicio ) << endl;

	// Ordem
	*saida << ordem[0] + 1;
	for( int i = 1; i < total; i++ ) *saida << ',' << ordem[i] + 1;
	*saida << endl;

	saida->flush();

}

/* Impressão do Estágio Inicial */
void ImprimirResultadoInicial( int total, ostream* saida ) {

	// Tempo decorrido
	*saida << 0 << endl << 0 << endl;

	// Ordem
	*saida << 1;
	for( int i = 2; i <= total; i++ ) *saida << ',' << i;
	*saida << endl;

	saida->flush();

}
