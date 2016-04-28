
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
import java.util.ArrayList;
import java.util.List;

/**
 * Média de perfis de expressão gênica.
 * @author José Flávio de Souza Dias Júnior
 * @version 2016
 */
public class MediaPerfis {
	
	private static final double VALOR_AUSENTE = Double.MAX_VALUE;
	
	public static void main( String[] args ) {
		
		if( args.length < 2 ){
			System.out.println( "Média de perfis de expressão gênica." );
			System.out.println( MediaPerfis.class.getSimpleName() + ".sh <expressoes> <grupo1> [<grupo2> [<grupoN>]]" );
			System.out.println();
			System.out.println( "<expressoes> : Arquivo com tabela de perfis de expressão gênica." );
			System.out.println( "<grupoN>     : Colunas que formam um grupo para média: \"1,2,3-7,...\" ou \"0\" para todas." );
			System.exit( 1 );
		}
		
		try{
			
			final File arq_tabela = new File( args[0] );
			final Grupo[] grupos  = new Grupo[ args.length - 1 ];
			
			for( int i = 0; i < grupos.length; i++ ){
				grupos[i] = new Grupo( args[i+1] );
			}
			
			BufferedReader entrada = new BufferedReader( new FileReader( arq_tabela ) );
			try{
				String linha;
				while( ( linha = entrada.readLine() ) != null ){
					String[] colunas = linha.split( "\t" );
					double[] expressoes = new double[ colunas.length - 1 ];
					for( int i = 0; i < expressoes.length; i++ ){
						try{
							expressoes[i] = Double.parseDouble( colunas[i+1] );
						}catch( NumberFormatException e ){
							expressoes[i] = VALOR_AUSENTE;
						}
					}
					System.out.print( colunas[0] );
					for( Grupo grupo : grupos ){
						System.out.print( "\t" + grupo.media( expressoes ) );
					}
					System.out.println();
				}
			}finally{
				entrada.close();
			}
			
		}catch( Exception e ){
			e.printStackTrace();
		}
		
	}
	
	private static class Grupo {
		
		private List<Integer> colunas = new ArrayList<Integer>();
		
		private boolean todas = false;
		
		public Grupo( String arg ) {
			if( arg.equals( "0" ) ){
				todas = true;
			}else{
				for( String s : arg.split( "," ) ){
					if( s.contains( "-" ) ){
						String[] parte = s.split( "-" );
						int inicio = Integer.parseInt( parte[0].trim() ) - 1;
						int fim    = Integer.parseInt( parte[1].trim() ) - 1;
						for( int i = inicio; i <= fim; i++ ){
							colunas.add( i );
						}
					}else{
						colunas.add( Integer.parseInt( s.trim() ) - 1 );
					}
				}
			}
		}
		
		public double media( double[] expressoes ) {
			double soma = 0;
			int total = 0;
			if( todas ){
				for( double v : expressoes ){
					if( v != VALOR_AUSENTE ){
						soma += v;
						total++;
					}
				}
			}else{
				for( int i : colunas ){
					double v = expressoes[i];
					if( v != VALOR_AUSENTE ){
						soma += v;
						total++;
					}
				}
			}
			return total > 0 ? soma / total : 0;
		}
		
	}
	
}
