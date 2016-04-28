
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
 * M�dia por m�dulos especificados por fronteiras.
 * @author Jos� Fl�vio de Souza Dias J�nior
 * @version 2016
 */
public class MediaModulos {
	
	private static final float VALOR_AUSENTE = Float.MAX_VALUE;
	
	public static void main( String[] args ) {
		
		if( args.length < 2 ){
			System.out.println( "M�dia por m�dulos especificados por fronteiras." );
			System.out.println( MediaModulos.class.getSimpleName() + ".sh <expressoes> <fronteiras>" );
			System.out.println();
			System.out.println( "<expressoes> : Arquivo com tabela de perfis de express�o g�nica." );
			System.out.println( "<fronteiras> : Arquivo com as fronteiras modulares. Ver Fronteiras.sh" );
			System.exit( 1 );
		}
		
		try{
			
			final File arq_expressoes = new File( args[0] );
			final File arq_fronteiras = new File( args[1] );
			
			List<Gene> genes = new ArrayList<Gene>( 5000 );
			
			BufferedReader entrada = new BufferedReader( new FileReader( arq_expressoes ) );
			try{
				String linha, valor;
				while( ( linha = entrada.readLine() ) != null ){
					String[] colunas = linha.split( "\t" );
					Gene gene = new Gene( colunas[0], new float[colunas.length-1] );
					for( int i = 0; i < gene.expressoes.length; i++ ){
						valor = colunas[i+1];
						gene.expressoes[i] =
								valor != null && ! valor.isEmpty() ?
								Float.parseFloat( valor ) :
								VALOR_AUSENTE;
					}
					genes.add( gene );
				}
			}finally{
				entrada.close();
			}
			
			int[] fronteiras;
			
			entrada = new BufferedReader( new FileReader( arq_fronteiras ) );
			try{
				String[] n = entrada.readLine().trim().split( " " );
				fronteiras = new int[ n.length + 1 ];
				for( int i = 0; i < n.length; i++ ){
					fronteiras[i] = Integer.parseInt( n[i] ) - 1;
				}
			}finally{
				entrada.close();
			}
			
			fronteiras[fronteiras.length-1] = genes.size() - 1;
			
			int ini = 0;
			int fim;
			int total_exps = genes.get(0).expressoes.length;
			for( int f = 0; f < fronteiras.length; f++ ){
				fim = fronteiras[f];
				System.out.print( "M" + (f+1) );
				for( int exp = 0; exp < total_exps; exp++ ){
					double soma = 0;
					int total = 0;
					for( int gene = ini; gene <= fim; gene++ ){
						float valor = genes.get( gene ).expressoes[ exp ];
						if( valor != VALOR_AUSENTE ){
							soma += valor;
							total++;
						}
					}
					soma = total > 0 ? soma / total : 0;
					System.out.print( "\t" + soma );
				}
				System.out.println();
				ini = fim + 1;
			}
			
		}catch( Exception e ){
			e.printStackTrace();
		}
		
	}
	
	private static class Gene {
		
		private String nome;
		
		private float[] expressoes;

		public Gene( String nome, float[] expressoes ) {
			this.nome = nome;
			this.expressoes = expressoes;
		}
		
		@Override
		public String toString() {
			return nome;
		}
		
	}
	
}
