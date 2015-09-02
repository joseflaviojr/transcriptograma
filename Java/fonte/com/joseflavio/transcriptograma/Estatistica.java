
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

package com.joseflavio.transcriptograma;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.NumberFormat;

/**
 * Estatística dos resultados.
 * @author José Flávio de Souza Dias Júnior
 * @version 2015
 */
public class Estatistica {
	
	public static final String[] COLUNAS = {
		"Experimento", "Rede", "Tamanho", "Algoritmo",
		"Execucao", "Dispersao", "TempoUltimaMelhoria", "Dispersao/TempoUltimaMelhoria",
		"%ReducaoLocal", "%ReducaoLocal/TempoUltimaMelhoria", "TempoReducaoLocal10%", "Mensuracoes",
		"Mudancas", "MudancasDesfeitas", "Melhorias", "DispersaoMin", "DispersaoMax", "Arestas", "%ReducaoGlobal" };

	public static void main( String[] args ) {
		
		if( args.length == 0 ){
			System.out.println( "Estatística dos resultados." );
			System.out.println( "Informe o local dos experimentos." );
		}
		
		try{
		
			File diretorio = new File( args[0] );

			FileWriter saida = new FileWriter( new File( diretorio, "Estatistica.txt" ) );
			saida.write( Ferramenta.concatenar( COLUNAS, ";", 0, COLUNAS.length - 1 ) + "\n" );
			
			File[] matrizes = diretorio.listFiles( new FilenameFilter() {
				public boolean accept( File dir, String name ) {
					return name.endsWith( ".csv" );
				}
			} );
			
			for( File matrizArquivo : matrizes ){
				
				System.out.println( matrizArquivo.getName() );
				
				short[][] matriz = Ferramenta.carregarMatriz( matrizArquivo );
				long[] dispersaoMinMax = Ferramenta.calcularDispersaoMinMax( matriz, Ferramenta.grafoOrientado( matriz ) );
				
				for( int execucao = 1; execucao <= 50; execucao++ ){
					calcular( matrizArquivo, matriz, dispersaoMinMax, "CFM", execucao, saida );
					calcular( matrizArquivo, matriz, dispersaoMinMax, "CLA", execucao, saida );
					calcular( matrizArquivo, matriz, dispersaoMinMax, "DEM", execucao, saida );
				}
				
				System.gc();
				
			}
			
			saida.close();
			
		}catch( IOException e ){
			e.printStackTrace();
		}

	}
	
	private static void calcular( File matrizArquivo, short[][] matriz, long[] dispersaoMinMax, String algoritmo, int execucao, FileWriter saida ) throws IOException {

		/* --------------- */
		
		String prefixo = matrizArquivo.getName() + "." + algoritmo + "[" + execucao + "]";
		File arquivoDispersao = new File( matrizArquivo.getParent(), prefixo + ".disp.txt" );
		File arquivoEstatisca = new File( matrizArquivo.getParent(), prefixo + ".estat.txt" );
		File arquivoOrdem     = new File( matrizArquivo.getParent(), prefixo + ".ordem.txt" );
		if( ! arquivoDispersao.exists() || ! arquivoEstatisca.exists() || ! arquivoOrdem.exists() ) return;
		
		/* --------------- */
		
		saida.write( "" + Integer.parseInt( prefixo.substring( 1, 4 ) ) );
		saida.write( ";" );
		saida.write( prefixo.substring( 6, 11 ) );
		saida.write( ";" );
		saida.write( "" + Integer.parseInt( prefixo.substring( 12, 16 ) ) );
		saida.write( ";" );
		saida.write( algoritmo );
		saida.write( ";" );
		saida.write( "" + execucao );
		saida.write( ";" );
		
		/* --------------- */
		
		String[] colunas = new String[5];
		StringBuilder buffer = new StringBuilder(50);
		int i, j;
		
		NumberFormat nf = NumberFormat.getNumberInstance();
		nf.setGroupingUsed( false );
		nf.setMinimumFractionDigits( 10 );
		
		/* --------------- */
		
		BufferedReader dispersao = new BufferedReader( new FileReader( arquivoDispersao ) );
		long[][] dispersaoMatriz = new long[500][2];
		int dispersaoTotal = 0;
		while( Ferramenta.proximaLinha( dispersao, colunas, ',', buffer ) > 0 ){
			dispersaoMatriz[dispersaoTotal][0] = Long.parseLong( colunas[0] );
			dispersaoMatriz[dispersaoTotal][1] = Long.parseLong( colunas[1] );
			dispersaoTotal++;
		}
		dispersao.close();
		
		/* --------------- */
		
		BufferedReader estatistica = new BufferedReader( new FileReader( arquivoEstatisca ) );
		long[][] estatisticaMatriz = new long[500][5];
		int estatiscaTotal = 0;
		Ferramenta.proximaLinha( estatistica, colunas, ';', buffer );
		while( Ferramenta.proximaLinha( estatistica, colunas, ';', buffer ) > 0 ){
			for( j = 0; j < 5; j++ ){
				estatisticaMatriz[estatiscaTotal][j] = Long.parseLong( colunas[j] );	
			}
			estatiscaTotal++;
		}
		estatistica.close();
		
		/* --------------- */
		
		long dispersaoInicial = dispersaoMatriz[0][1];
		
		i = dispersaoTotal-1;
		long dispersaoFinal = dispersaoMatriz[i][1];
		
		saida.write( "" + dispersaoFinal );
		saida.write( ";" );
		
		/* --------------- */
		
		long tempo = dispersaoMatriz[i][0];
		saida.write( "" + tempo );
		saida.write( ";" );
		
		/* --------------- */
		
		if( tempo == 0 ) tempo = 1;
		saida.write( "" + (int)( dispersaoFinal / (float) tempo ) );
		saida.write( ";" );
		
		/* --------------- */
		
		float reducaoLocal = ( 1f - dispersaoFinal / (float) dispersaoInicial ) * 100f;
		saida.write( nf.format( reducaoLocal ) );
		saida.write( ";" );
		
		/* --------------- */
		
		saida.write( nf.format( reducaoLocal / tempo ) );
		saida.write( ";" );
		
		/* --------------- */
		
		long dispersaoParcial = (int)( dispersaoInicial * 0.9d );
		for( i = 0; i < dispersaoTotal; i++ ){
			if( dispersaoMatriz[i][1] <= dispersaoParcial ){
				saida.write( "" + dispersaoMatriz[i][0] );
				saida.write( ";" );
				break;
			}
		}
		if( i == dispersaoTotal ) saida.write( "--;" );
		
		/* --------------- */
		
		i = estatiscaTotal - 1;
		
		saida.write( "" + estatisticaMatriz[i][1] );
		saida.write( ";" );
		saida.write( "" + estatisticaMatriz[i][2] );
		saida.write( ";" );
		saida.write( "" + estatisticaMatriz[i][3] );
		saida.write( ";" );
		saida.write( "" + estatisticaMatriz[i][4] );
		
		/* --------------- */
		
		long dispersaoMin = dispersaoMinMax[0];
		long dispersaoMax = dispersaoMinMax[1];
		
		saida.write( ";" );
		saida.write( "" + dispersaoMin );
		saida.write( ";" );
		saida.write( "" + dispersaoMax );
		
		/* --------------- */
		
		saida.write( ";" );
		saida.write( "" + dispersaoMinMax[2] );
		
		/* --------------- */
		
		saida.write( ";" );
		float reducaoGlobal = ( 1f - ( dispersaoFinal - dispersaoMin ) / (float)( dispersaoMax - dispersaoMin ) ) * 100f;
		saida.write( nf.format( reducaoGlobal ) );
		
		/* --------------- */
		
		saida.write( "\n" );
		
		/* --------------- */
		
	}
	
	public static boolean inadequado( String valor ) {
		return valor == null || valor.length() == 0 || valor.equals( "--" );
	}
	
}
