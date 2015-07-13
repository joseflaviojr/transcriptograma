
/*
 *  Copyright (C) 2015 José Flávio de Souza Dias Júnior
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
 *  Direitos Autorais Reservados (C) 2015 José Flávio de Souza Dias Júnior
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

#include <cstdlib>

struct Centralidade {
	int   posicao;
	float valor;
};

bool Inicializar( int** adjacencia, int** distancia, int* grau_entrada, Centralidade* centralidade, int total );
long Dispersao( int** adjacencia, int* ordem, int total, bool orientado );
int  CompararCentralidade( const void* a, const void* b );
inline int Sortear( int maximo );

/* 
 *  Claritate
 *
 *  Versão comparativa
 *  Comparative version
 * 
 *  Original: <http://www.joseflavio.com/claritate/>
 */
int main( int argc, char *argv[] ) {

	// Inicialização

	if( argc == 1 ){
		printf( "%s\n", "./Claritate <matriz> <semente> <intervalo> <registros>" );
		return -1;
	}

	if( argc > 2 ) srand(atoi(argv[2]));

	int intervalo = 60; //1 minuto
	if( argc > 3 ) intervalo = atoi(argv[3]);

	int limite = 60; //60 registros
	if( argc > 4 ) limite = atoi(argv[4]);

	const char *nomeArquivoAdjac = argv[1];
	const char *nomeArquivoOrdem = strcat( strcpy( (char*)malloc(256), nomeArquivoAdjac ), ".txt" );
	const char *nomeArquivoEstat = strcat( strcpy( (char*)malloc(256), nomeArquivoAdjac ), ".estat.txt" );
	
	ifstream entrada ( nomeArquivoAdjac );
	ofstream saida   ( nomeArquivoOrdem, ofstream::trunc );
	ofstream estat   ( nomeArquivoEstat, ofstream::trunc );

	const int total = CSV_Largura( &entrada );

	int**         adjacencia   = new int*[total];
	int**         distancia    = new int*[total];
	int*          grau_entrada = new int[total];
	Centralidade* centralidade = new Centralidade[total];

	for( int i = 0; i < total; i++ ){
		adjacencia[i]           = new int[total];
		distancia[i]            = new int[total];
		grau_entrada[i]         = 0;
		centralidade[i].posicao = i;
		centralidade[i].valor   = 0;
	}

	CSV_Arquivo( &entrada, adjacencia, distancia, total );
	entrada.close();

	double tempo;
	time_t tempo0, tempo1, tempo2, tempo_melhoria;
	time( &tempo0 );
	tempo_melhoria = tempo1 = tempo0;
	ImprimirResultadoInicial( total, &saida );

	bool orientado =
	Inicializar( adjacencia, distancia, grau_entrada, centralidade, total );

	// Estatística

	unsigned long estat_metrica = 0;
	unsigned long estat_reorganizacoes = 0;
	unsigned long estat_reorganizacoes_desfeitas = 0;
	unsigned long estat_melhorias = 0;

	estat << "tempo;metrica;reorganizacoes;reorganizacoes_desfeitas;melhorias" << endl;

	// Dispersão

	qsort( centralidade, total, sizeof(Centralidade), CompararCentralidade );

	int ordem[total];
	int posicao[total], posicao_atual, posicao_nova;

	for( int i = 0, pos; i < total; i++ ){
		pos = centralidade[i].posicao;
		ordem[i] = pos;
		posicao[pos] = i;
	}

	for( int i = 0, j, filhos, media; i < total; i++ ){

		filhos = 0;
		media = 0;

		for( j = 0; j < total; j++ ){
			if( adjacencia[i][j] != DESCONEXO && ( orientado || grau_entrada[j] <= grau_entrada[i] ) ){
				filhos++;
				media += posicao[j];
			}
		}

		if( filhos == 0 ) continue;
		
		posicao_atual = posicao[i];
		posicao_nova = ceil( (float) media / filhos );

		Deslocar( posicao_atual, posicao_nova, ordem, total );
		estat_reorganizacoes++;
		for( j = 0; j < total; j++ ) posicao[ordem[j]] = j;

	}

	// Compressão

	long dispersao, dispersao_nova;

	dispersao = Dispersao( adjacencia, ordem, total, orientado );
	estat_metrica++;

	int s1, s2, s3;
	int v1, v2, v3;
	int dist1, dist2;
	int real1, real2;
	int novo1, novo2;
	int pivo;

	while( dispersao > 0 ){

		s1 = Sortear(total-1);
		s3 = Sortear(total-1);
		if( abs(s3-s1) <= 1 ) continue;
		if( s1 > s3 ){
			s2 = s1;
			s1 = s3;
			s3 = s2;
		}
		s2 = s1 + Sortear(s3-s1-2) + 1;

		v1 = ordem[s1];
		v2 = ordem[s2];
		v3 = ordem[s3];

		dist1 = distancia[v1][v2];
		dist2 = distancia[v2][v3];

		if( dist1 == DESCONEXO || dist2 == DESCONEXO ) continue;

		real1 = s2 - s1;
		real2 = s3 - s2;

		novo1 = ceil( ( ((float)dist1) / ( dist1 + dist2 ) ) * ( real1 + real2 ) );
		novo2 = real1 + real2 - novo1;

		pivo = Sortear(100);

		if( pivo <= 33 ){

			posicao_atual = s1;
			posicao_nova  = s2 - novo1;
			if( posicao_nova < 0 ) posicao_nova = 0;

		}else if( pivo <= 66 ){

			posicao_atual = s2;
			posicao_nova  = s1 + novo1;

		}else{

			posicao_atual = s3;
			posicao_nova  = s2 + novo2;
			if( posicao_nova >= total ) posicao_nova = total - 1;

		}

		Deslocar( posicao_atual, posicao_nova, ordem, total );
		estat_reorganizacoes++;

		dispersao_nova = Dispersao( adjacencia, ordem, total, orientado );
		estat_metrica++;

		if( dispersao_nova < dispersao ){
			dispersao = dispersao_nova;
			time( &tempo_melhoria );
			estat_melhorias++;
		}else{
			Deslocar( posicao_nova, posicao_atual, ordem, total );
			estat_reorganizacoes_desfeitas++;
		}

		time( &tempo2 );
		tempo = difftime( tempo2, tempo1 );
		if( tempo >= intervalo ){
			ImprimirResultado( &tempo0, &tempo2, &tempo_melhoria, ordem, total, &saida );
			estat << (long)difftime( tempo2, tempo0 ) << ";" << estat_metrica << ";" << estat_reorganizacoes << ";" << estat_reorganizacoes_desfeitas << ";" << estat_melhorias << endl;
			estat.flush();
			tempo1 = tempo2;
			if( --limite == 0 ) break;
		}

	}

	return 0;

}

/*
 *  Algoritmos conjugados para inicialização do Claritate.
 *  Floyd, R. W. (1962). Algorithm 97: Shortest path. Communications of the ACM, 5(6):345–.
 *  Hage, P. and Harary, F. (1995). Eccentricity and centrality in networks. Social Networks, 17:57–63.
 */
bool Inicializar( int** adjacencia, int** distancia, int* grau_entrada, Centralidade* centralidade, int total ) {

	bool orientado = false;
	int i, j, k, a, b, distanciaij;

	for( i = 0; i < total; i++ ){

		for( j = 0; j < total; j++ ){

			// Floyd-Warshall
			distanciaij = distancia[i][j];
			for( k = 0; k < total; k++ ){
				a = distancia[i][k];
				b = distancia[k][j];
				b = a == DESCONEXO || b == DESCONEXO ? DESCONEXO : a + b;
				if( b != DESCONEXO && ( distanciaij == DESCONEXO || b < distanciaij ) ) distanciaij = b;
			}
			distancia[i][j] = distanciaij;

			// Centralidade por excentricidade
			if( distanciaij > centralidade[i].valor ) centralidade[i].valor = distanciaij;

			// Grafo orientado?
			if( ! orientado && adjacencia[i][j] != adjacencia[j][i] ) orientado = true;

			// Grau de entrada
			if( adjacencia[j][i] != DESCONEXO ) grau_entrada[i]++;

		}

		if( centralidade[i].valor != 0 ) centralidade[i].valor = 1 / centralidade[i].valor;

	}

	return orientado;

}

/* Volume do sistema multiestelar */
long Dispersao( int** adjacencia, int* ordem, int total, bool orientado ) {

	long dispersao = 0;
	int i, j;

	if( ! orientado ){
		for( i = 0; i < (total-1); i++ ){
			for( j = i+1; j < total; j++ ){
				if( adjacencia[ordem[i]][ordem[j]] != DESCONEXO ){
					dispersao += (j-i);
				}
			}
		}
	}else{
		for( i = 0; i < total; i++ ){
			for( j = 0; j < total; j++ ){
				if( adjacencia[ordem[i]][ordem[j]] != DESCONEXO ){
					dispersao += (i>=j) ? (i-j) : (j-i);
				}
			}
		}
	}

	return dispersao;

}

int CompararCentralidade( const void* a, const void* b ) {
	return ( ((Centralidade*)a)->valor - ((Centralidade*)b)->valor );
}

/* Sorteio */
inline int Sortear( int maximo ) {
	return rand() % ( maximo + 1 );
}
