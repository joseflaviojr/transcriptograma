
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
import java.util.HashMap;
import java.util.Map;

/**
 * Traduz os nomes contidos numa coluna de tabela, conforme um dicionário.
 * @author José Flávio de Souza Dias Júnior
 * @version 2016
 */
public class TraduzirColuna {
	
	public static void main( String[] args ) {
		
		if( args.length < 6 ){
			System.out.println( "Traduz os nomes contidos numa coluna de tabela, conforme um dicionário." );
			System.out.println( TraduzirColuna.class.getSimpleName() + ".sh <tabela> <coluna> <dicionario> <nome_antigo> <nome_novo> <ausencia>" );
			System.out.println();
			System.out.println( "<tabela>     : Tabela que contém a coluna a ser traduzida." );
			System.out.println( "<coluna>     : Número da coluna a ser traduzida. Valor >= 1" );
			System.out.println( "<dicionario> : Tabela que associa os nomes antigos aos novos." );
			System.out.println( "<nome_antigo>: Coluna do dicionário que contém os antigos nomes. Valor >= 1" );
			System.out.println( "<nome_novo>  : Coluna do dicionário que contém os novos nomes. Valor >= 1" );
			System.out.println( "<ausencia>   : Prefixo a ser utilizado quando <nome_antigo> ausente. Ex.: \"#\"" );
			System.exit( 1 );
		}
		
		try{
			
			final File   tab_arq  = new File( args[0] );
			final int    tab_col  = Integer.parseInt( args[1] ) - 1;
			final File   dic_arq  = new File( args[2] );
			final int    dic_c01  = Integer.parseInt( args[3] ) - 1;
			final int    dic_c02  = Integer.parseInt( args[4] ) - 1;
			final String ausencia = args[5];
			
			Map<String,String> dicionario = new HashMap<String,String>( 5000 );
			
			BufferedReader entrada = new BufferedReader( new FileReader( dic_arq ) );
			try{
				String linha;
				while( ( linha = entrada.readLine() ) != null ){
					String[] colunas = linha.split( "\t" );
					int total = colunas.length;
					if( dic_c01 >= total || dic_c02 >= total ) continue;
					String nome1 = colunas[dic_c01];
					String nome2 = colunas[dic_c02];
					if( nome1.isEmpty() || nome2.isEmpty() ) continue;
					dicionario.put( nome1, nome2 );
				}
			}finally{
				entrada.close();
			}
			
			entrada = new BufferedReader( new FileReader( tab_arq ) );
			try{
				String linha;
				while( ( linha = entrada.readLine() ) != null ){
					String[] colunas = linha.split( "\t" );
					int total = colunas.length;
					if( tab_col < total ){
						String novo = dicionario.get( colunas[tab_col] );
						if( novo == null ) novo = ausencia + colunas[tab_col];
						colunas[tab_col] = novo;
						System.out.print( colunas[0] );
						for( int i = 1; i < total; i++ ){
							System.out.print( "\t" + colunas[i] );
						}
						System.out.println();
					}else{
						System.out.println( linha );
					}
				}
			}finally{
				entrada.close();
			}
			
		}catch( Exception e ){
			e.printStackTrace();
		}
		
	}
	
}
