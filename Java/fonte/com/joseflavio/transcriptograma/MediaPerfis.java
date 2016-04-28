
/*
 *  Copyright (C) 2015-2016 Jos� Fl�vio de Souza Dias J�nior
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
 *  Direitos Autorais Reservados (C) 2015-2016 Jos� Fl�vio de Souza Dias J�nior
 * 
 *  Este arquivo � parte de Transcriptograma - <http://www.joseflavio.com/transcriptograma/>.
 * 
 *  Transcriptograma � software livre: voc� pode redistribu�-lo e/ou modific�-lo
 *  sob os termos da Licen�a P�blica Menos Geral GNU conforme publicada pela
 *  Free Software Foundation, tanto a vers�o 3 da Licen�a, como
 *  (a seu crit�rio) qualquer vers�o posterior.
 * 
 *  Transcriptograma � distribu�do na expectativa de que seja �til,
 *  por�m, SEM NENHUMA GARANTIA; nem mesmo a garantia impl�cita de
 *  COMERCIABILIDADE ou ADEQUA��O A UMA FINALIDADE ESPEC�FICA. Consulte a
 *  Licen�a P�blica Menos Geral do GNU para mais detalhes.
 * 
 *  Voc� deve ter recebido uma c�pia da Licen�a P�blica Menos Geral do GNU
 *  junto com Transcriptograma. Se n�o, veja <http://www.gnu.org/licenses/>.
 */

package com.joseflavio.transcriptograma;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * M�dia de perfis de express�o g�nica.
 * @author Jos� Fl�vio de Souza Dias J�nior
 * @version 2016
 */
public class MediaPerfis {
	
	private static final double VALOR_AUSENTE = Double.MAX_VALUE;
	
	public static void main( String[] args ) {
		
		if( args.length < 2 ){
			System.out.println( "M�dia de perfis de express�o g�nica." );
			System.out.println( MediaPerfis.class.getSimpleName() + ".sh <expressoes> <grupo1> [<grupo2> [<grupoN>]]" );
			System.out.println();
			System.out.println( "<expressoes> : Arquivo com tabela de perfis de express�o g�nica." );
			System.out.println( "<grupoN>     : Colunas que formam um grupo para m�dia: \"1,2,3-7,...\" ou \"0\" para todas." );
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
