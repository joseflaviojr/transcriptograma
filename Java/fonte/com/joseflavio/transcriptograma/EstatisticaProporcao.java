
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
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Proporção dos resultados estatísticos do CLA e DEM em relação aos do CFM.
 * @author José Flávio de Souza Dias Júnior
 * @version 2015
 */
public class EstatisticaProporcao {

	public static void main( String[] args ) {
		
		if( args.length == 0 ){
			System.out.println( "Proporção dos resultados estatísticos do CLA e DEM em relação aos do CFM." );
			System.out.println( "Informe o local dos experimentos." );
		}
		
		try{
		
			File diretorio = new File( args[0] );
			File estatistica = new File( diretorio, "Estatistica.txt" );
			
			if( ! estatistica.exists() ){
				System.out.println( estatistica.getName() + " não encontrado." );
				System.exit( 1 );
			}

			BufferedReader entrada = new BufferedReader( new FileReader( estatistica ) );
			FileWriter     saida   = new FileWriter( new File( diretorio, "Proporcao.txt" ) );
			
			Map<String,String[]> resultadosCFM = new HashMap<String,String[]>( 1000 );
			String[] colunas = new String[Estatistica.COLUNAS.length];
			StringBuilder buffer = new StringBuilder( 500 );
			
			NumberFormat nf = NumberFormat.getNumberInstance();
			nf.setGroupingUsed( false );
			nf.setMinimumFractionDigits( 10 );
			
			Ferramenta.proximaLinha( entrada, colunas, ';', buffer );
			while( Ferramenta.proximaLinha( entrada, colunas, ';', buffer ) > 0 ){
				if( colunas[3] != null && colunas[3].equals( "CFM" ) ){
					resultadosCFM.put( Ferramenta.concatenar( colunas, ";", 0, 4 ), colunas.clone() );
				}
			}

			entrada.close();
			entrada = new BufferedReader( new FileReader( estatistica ) );
			
			saida.write( Ferramenta.concatenar( Estatistica.COLUNAS, ";", 0, Estatistica.COLUNAS.length - 1 ) + "\n" );
			
			Ferramenta.proximaLinha( entrada, colunas, ';', buffer );
			while( Ferramenta.proximaLinha( entrada, colunas, ';', buffer ) > 0 ){
				if( colunas[3] != null ){
					
					String chave_xxx = Ferramenta.concatenar( colunas, ";", 0, 4 );
					System.out.println( chave_xxx );
					
					colunas[3] = "CFM";
					String chave_cfm = Ferramenta.concatenar( colunas, ";", 0, 4 );
					String[] colunas_cfm = resultadosCFM.get( chave_cfm );
					
					if( colunas_cfm == null ) continue;
					
					String[] cfm = Arrays.copyOfRange( colunas_cfm, 5, Estatistica.COLUNAS.length );
					String[] xxx = Arrays.copyOfRange( colunas, 5, Estatistica.COLUNAS.length );
					
					saida.write( chave_xxx );
					saida.write( ";" );
					saida.write( Ferramenta.concatenar( dividir( xxx, cfm, nf ), ";", 0, xxx.length - 1 ) );
					saida.write( "\n" );
					
				}
			}
			
			entrada.close();
			saida.close();
			
		}catch( Exception e ){
			e.printStackTrace();
		}

	}
	
	private static String[] dividir( String[] numerador, String[] divisor, NumberFormat nf ) throws ParseException {
		String[] resultado = new String[ numerador.length ];
		for( int i = 0; i < numerador.length; i++ ){
			String num = numerador[i];
			String div = divisor[i];
			if( Estatistica.inadequado( num ) || Estatistica.inadequado( div ) ) resultado[i] = "--";
			else resultado[i] = nf.format( nf.parse( num ).doubleValue() / nf.parse( div ).doubleValue() );
		}
		return resultado;
	}
	
}
