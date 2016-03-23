
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

void Eleicao( int** populacao, int total, float* insatisfacao, int* eleito, int* candidatonorte, int* candidatosul );
void InteressePopulacional( int** populacao, int total );
inline int Sortear( int maximo );

/* 
 *  Código Democrático
 *
 *  Versão comparativa
 *  Comparative version
 * 
 *  Original: <http://www.joseflavio.com/democratico/>
 */
int main( int argc, char *argv[] ) {

	// Pré-Governo

	if( argc == 1 ){
		printf( "%s\n", "./CodigoDemocratico <matriz> <semente> <intervalo> <registros>" );
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

	int **matrizadj = new int*[total];
	int **populacao = new int*[total];
	for( int i = 0; i < total; i++ ){
		matrizadj[i] = new int[total];
		populacao[i] = new int[total];
	}

	CSV_Arquivo( &entrada, matrizadj, populacao, total );
	entrada.close();

	double tempo;
	time_t tempo0, tempo1, tempo2, tempo_melhoria;
	time( &tempo0 );
	tempo_melhoria = tempo1 = tempo0;
	ImprimirResultadoInicial( total, &saida );

	InteressePopulacional( populacao, total );

	// Estatística

	unsigned long estat_metrica = 0;
	unsigned long estat_reorganizacoes = 0;
	unsigned long estat_reorganizacoes_desfeitas = 0;
	unsigned long estat_melhorias = 0;

	estat << "tempo;metrica;reorganizacoes;reorganizacoes_desfeitas;melhorias" << endl;

	// Governo

	int organizacao[total];
	int organizacao_melhor[total];
	for( int i = 0; i < total; i++ ) organizacao[i] = organizacao_melhor[i] = i;

	float insatisfacao, insatisfacao_nova, insatisfacao_melhor;
	
	int candidatonorte[total], candidatosul[total];
	int eleito, eleito_novo, eleito_melhor;
	int votado, sorteado;

	Eleicao( populacao, total, &insatisfacao, &eleito, candidatonorte, candidatosul );
	estat_metrica++;
	insatisfacao_melhor = insatisfacao;
	eleito_melhor = eleito;

	int mandatos = 0;
	int mandatos_tolerancia = ( eleito<0 ? -eleito : total-eleito ) * 0.8;

	while( insatisfacao_melhor > 0 ){

		votado = abs(eleito);
		sorteado = eleito<0 ? Sortear(votado-1) : eleito+1+Sortear(total-eleito-2);

		Permutar( matrizadj, populacao, organizacao, total, votado, sorteado );
		estat_reorganizacoes++;

		Eleicao( populacao, total, &insatisfacao_nova, &eleito_novo, candidatonorte, candidatosul );
		estat_metrica++;

		if( insatisfacao_nova < insatisfacao || mandatos >= mandatos_tolerancia ){
			
			insatisfacao = insatisfacao_nova;
			eleito = eleito_novo;
			mandatos = 0;
			mandatos_tolerancia = ( eleito<0 ? -eleito : total-eleito ) * 0.8;

			if( insatisfacao < insatisfacao_melhor ){
				insatisfacao_melhor = insatisfacao;
				eleito_melhor = eleito;
				memcpy( organizacao_melhor, organizacao, total * sizeof(int) );
				time( &tempo_melhoria );
				estat_melhorias++;
			}

		}else{
			mandatos++;
			Permutar( matrizadj, populacao, organizacao, total, votado, sorteado );
			estat_reorganizacoes_desfeitas++;
		}

		time( &tempo2 );
		tempo = difftime( tempo2, tempo1 );
		if( tempo >= intervalo ){
			ImprimirResultado( &tempo0, &tempo2, &tempo_melhoria, organizacao_melhor, total, &saida );
			estat << (long)difftime( tempo2, tempo0 ) << ";" << estat_metrica << ";" << estat_reorganizacoes << ";" << estat_reorganizacoes_desfeitas << ";" << estat_melhorias << endl;
			estat.flush();
			tempo1 = tempo2;
			if( --limite == 0 ) break;
		}

	}

	return 0;

}

/* Processo Eleitoral */
void Eleicao( int** populacao, int total, float* insatisfacao, int* eleito, int* candidatonorte, int* candidatosul ) {

	*insatisfacao = 0;

	float apuracao, media;
	int interesse, cand1, cand2, i, j;

	for( i = 0; i < total; i++ ) candidatonorte[i] = candidatosul[i] = 0;
	int candidatonorte_maior = 0;
	int candidatosul_maior = 0;

	const float relevancia = 0.6 * total;

	for( j = 0; j < total; j++ ){
		
		apuracao = 0;

		for( i = 0; i < total; i++ ){
			
			if( i == j ) continue;
			interesse = populacao[i][j];

			if( i < j ){ // Norte
				media = i <= (j-3) ? (populacao[i+1][j]+populacao[i+2][j])/2 : populacao[i+1][j];
				if( interesse < media ){
					apuracao += (media-interesse) / log(relevancia*(j-i));
					cand1 = ++candidatonorte[i];
					cand2 = candidatonorte[candidatonorte_maior];
					if( ( cand1 > cand2 ) || ( cand1 == cand2 && Sortear(100) <= 80 ) ) candidatonorte_maior = i;
				}
			}else{ // Sul
				media = i >= (j+3) ? (populacao[i-1][j]+populacao[i-2][j])/2 : populacao[i-1][j];
				if( interesse < media ){
					apuracao += (media-interesse) / log(relevancia*(i-j));
					cand1 = ++candidatosul[i];
					cand2 = candidatosul[candidatosul_maior];
					if( ( cand1 > cand2 ) || ( cand1 == cand2 && Sortear(100) <= 80 ) ) candidatosul_maior = i;
				}
			}

		}

		*insatisfacao += apuracao;

	}

	*eleito = candidatonorte[candidatonorte_maior] >= candidatosul[candidatosul_maior] ? candidatonorte_maior : -candidatosul_maior;

}

/*
 *  Matriz de Interesse Populacional
 *  Adaptação do algoritmo Floyd-Warshall
 *  Floyd, R. W. (1962). Algorithm 97: Shortest path. Communications of the ACM, 5(6):345–.
 */
void InteressePopulacional( int** populacao, int total ) {

	int i, j, k, a, b;
	int interesse, periferia = 0;

	for( i = 0; i < total; i++ ){
		for( j = 0; j < total; j++ ){
			interesse = populacao[i][j];
			for( k = 0; k < total; k++ ){
				a = populacao[i][k];
				b = populacao[k][j];
				b = a == DESCONEXO || b == DESCONEXO ? DESCONEXO : a + b;
				if( b != DESCONEXO && ( interesse == DESCONEXO || b < interesse ) ) interesse = b;
			}
			populacao[i][j] = interesse;
			if( interesse != DESCONEXO && interesse > periferia ) periferia = interesse;
		}
	}

	periferia *= 2;

	for( i = 0; i < total; i++ ){
		populacao[i][i] = 1;
		for( j = 0; j < total; j++ ){
			if( populacao[i][j] == DESCONEXO ) populacao[i][j] = periferia;
		}
	}

}

/* Sorteio */
inline int Sortear( int maximo ) {
	return rand() % ( maximo + 1 );
}
