
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

long CustoCFM( int** matrizadj, int* ordem, int total );
inline double Sortear( int limiteNaoIncluso );

/*
 *  Cost Function Method - CFM
 *
 *  Versão comparativa
 *  Comparative version
 *
 *  Original:
 *    Rybarczyk-Filho, J. L. L., Castro, M. A., Dalmolin, R. J., Moreira, J. C.,
 *        Brunnet, L. G., and de Almeida, R. M. (2011).
 *        Towards a genome-wide transcriptogram: the Saccharomyces cerevisiae case.
 *        Nucleic acids research, 39(8):3005–3016.
 */
int main( int argc, char *argv[] ) {

	// Inicialização

	if( argc == 1 ){
		printf( "%s\n", "./CFM <matriz> <semente> <intervalo> <registros>" );
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
	for( int i = 0; i < total; i++ ) matrizadj[i] = new int[total];

	CSV_Arquivo( &entrada, matrizadj, matrizadj, total );
	entrada.close();

	double tempo;
	time_t tempo0, tempo1, tempo2, tempo_melhoria;
	time( &tempo0 );
	tempo_melhoria = tempo1 = tempo0;
	ImprimirResultadoInicial( total, &saida );

	// Estatística

	unsigned long estat_metrica = 0;
	unsigned long estat_reorganizacoes = 0;
	unsigned long estat_reorganizacoes_desfeitas = 0;
	unsigned long estat_melhorias = 0;

	estat << "tempo;metrica;reorganizacoes;reorganizacoes_desfeitas;melhorias" << endl;
	
	// Processo

	int ordem[total];
	int ordem_melhor[total];
	for( int i = 0; i < total; i++ ) ordem[i] = ordem_melhor[i] = i;

	long custo, custo_novo, custo_melhor;
	custo = CustoCFM( matrizadj, ordem, total );
	estat_metrica++;
	custo_melhor = custo;

	double temperatura = 0.0001 * custo;
	double temperatura_reducao = 0.8;
	int temperatura_passos = 100;

	// Monte Carlo

	int passo = 1, sorteio = 0;
	int s1, s2;

	while( custo_melhor > 0 ){

		if( ++sorteio > total ){
			passo++;
			if( ( passo % temperatura_passos ) == 0 ) temperatura *= temperatura_reducao;
			sorteio = 1;
		}

		s1 = Sortear(total);
		s2 = Sortear(total);

		Permutar( ordem, total, s1, s2 );
		estat_reorganizacoes++;

		custo_novo = CustoCFM( matrizadj, ordem, total );
		estat_metrica++;

		if( custo_novo < custo || Sortear(1) <= exp((custo-custo_novo)/temperatura) ){
			
			custo = custo_novo;

			if( custo < custo_melhor ){
				custo_melhor = custo;
				memcpy( ordem_melhor, ordem, total * sizeof(int) );
				time( &tempo_melhoria );
				estat_melhorias++;
			}

		}else{
			Permutar( ordem, total, s1, s2 );
			estat_reorganizacoes_desfeitas++;
		}

		time( &tempo2 );
		tempo = difftime( tempo2, tempo1 );
		if( tempo >= intervalo ){
			ImprimirResultado( &tempo0, &tempo2, &tempo_melhoria, ordem_melhor, total, &saida );
			estat << (long)difftime( tempo2, tempo0 ) << ";" << estat_metrica << ";" << estat_reorganizacoes << ";" << estat_reorganizacoes_desfeitas << ";" << estat_melhorias << endl;
			estat.flush();
			tempo1 = tempo2;
			if( --limite == 0 ) break;
		}

	}

	return 0;

}

/* Função Custo do CFM */
long CustoCFM( int** matrizadj, int* ordem, int total ) {

	long custo = 0;

	const int ultimo = total - 1;
	int i, j, valor;

	for( i = 0; i < ultimo; i++ ){
		for( j = i+1; j < total; j++ ){
			valor = 0;
			if( matrizadj[ordem[i]][ordem[j]] ){
				if( i != ultimo ) valor += 1 - matrizadj[ordem[i+1]][ordem[j  ]];
				if( j != ultimo ) valor += 1 - matrizadj[ordem[i  ]][ordem[j+1]];
				if( i != 0 )      valor += 1 - matrizadj[ordem[i-1]][ordem[j  ]];
				if( j != 0 )      valor += 1 - matrizadj[ordem[i  ]][ordem[j-1]];
			}else{
				if( i != ultimo ) valor += matrizadj[ordem[i+1]][ordem[j  ]];
				if( j != ultimo ) valor += matrizadj[ordem[i  ]][ordem[j+1]];
				if( i != 0 )      valor += matrizadj[ordem[i-1]][ordem[j  ]];
				if( j != 0 )      valor += matrizadj[ordem[i  ]][ordem[j-1]];
			}
			custo += abs(i-j) * valor;
		}
	}

	return custo;

}

/* Sorteio */
inline double Sortear( int limiteNaoIncluso ) {
	return ( ( rand() % 100 ) / 100.0 ) * limiteNaoIncluso;
}
