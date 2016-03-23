
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

package com.joseflavio.transcriptograma;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Gera uma matriz de adjacências conforme especificação de uma rede.<br>
 * Arquivo de entrada: CSV de 2 colunas sem cabeçalho; as linhas correspondem a arestas entre dois vértices (colunas).<br>
 * Uma terceira coluna, opcional, pode especificar o peso da aresta (valor inteiro).
 * @author José Flávio de Souza Dias Júnior
 * @version 2015
 */
public class MatrizAdjacencias {
	
	public static void main( String[] args ) {
		
		boolean argumentosOK = true;
		for( int i = 0; i < args.length; i++ ){
			if( args[i].isEmpty() ){
				argumentosOK = false;
				break;
			}
		}
		
		if( args.length < 3 || ! argumentosOK ){
			System.out.println( "Gera uma matriz de adjacências conforme especificação de uma rede." );
			System.out.println( "Arquivo de entrada (rede): CSV de 2 colunas sem cabeçalho; as linhas correspondem a arestas entre dois vértices (colunas)." );
			System.out.println( "Uma terceira coluna, opcional, pode especificar o peso da aresta (valor inteiro)." );
			System.out.println( MatrizAdjacencias.class.getSimpleName() + " <arquivo_rede> <orientada:sim|nao> <separador_csv:tab|esp|virg|pvirg|vert>" );
			System.out.println( "Saída: <arquivo_rede.matriz.csv> e <arquivo_rede.nomes.txt>" );
			System.exit( 1 );
		}
		
		try{
			
			File    arquivo       = new File( args[0] );
			boolean orientada     = args[1].equals( "sim" );
			String  separador_csv = args[2];
			
			if( separador_csv.equals( "tab" ) ) separador_csv = "\t";
			else if( separador_csv.equals( "esp" ) ) separador_csv = " ";
			else if( separador_csv.equals( "virg" ) ) separador_csv = ",";
			else if( separador_csv.equals( "pvirg" ) ) separador_csv = ";";
			else if( separador_csv.equals( "vert" ) ) separador_csv = "|";
			
			List<String[]> linhas = new ArrayList<String[]>( 5000 );
			List<String> nomes = new ArrayList<String>( 5000 );
			Map<String,Integer> posicoes = new HashMap<String,Integer>( 5000 );
			int i, j;
			
			BufferedReader entrada = new BufferedReader( new FileReader( arquivo ) );
			try{
				String linha, nome;
				String[] colunas;
				while( ( linha = entrada.readLine() ) != null ){
					colunas = linha.split( separador_csv );
					linhas.add( colunas );
					for( i = 0; i < 2; i++ ){
						nome = colunas[i];
						if( posicoes.get( nome ) == null ){
							nomes.add( nome );
							posicoes.put( nome, nomes.size() - 1 );
						}
					}
				}
			}finally{
				entrada.close();
			}
			
			Collections.sort( nomes );
			int total = 0;
			for( String nome : nomes ){
				posicoes.put( nome, total++ );
			}

			System.out.println( "Total: " + total );
			
			short[][] matriz = new short[total][total];
			
			for( String[] coluna : linhas ){
				i = posicoes.get( coluna[0] );
				j = posicoes.get( coluna[1] );
				matriz[i][j] = coluna.length < 3 ? 1 : Short.parseShort( coluna[2] );
				if( ! orientada ) matriz[j][i] = matriz[i][j];
			}
			
			FileWriter arquivoMatriz = new FileWriter( new File( arquivo.getAbsolutePath() + ".matriz.csv" ) );
			FileWriter arquivoNomes  = new FileWriter( new File( arquivo.getAbsolutePath() + ".nomes.txt" ) );
			
			for( i = 0; i < total; i++ ){
				
				for( j = 0; j < total; j++ ){
					if( j > 0 ) arquivoMatriz.write( separador_csv );
					arquivoMatriz.write( "" + matriz[i][j] );
				}
				
				arquivoNomes.write( nomes.get( i ) );
				
				if( i < (total-1) ){
					arquivoMatriz.write( "\n" );
					arquivoNomes.write( "\n" );					
				}
				
			}
			
			arquivoMatriz.close();
			arquivoNomes.close();
			
		}catch( Exception e ){
			e.printStackTrace();
		}

	}
	
}
