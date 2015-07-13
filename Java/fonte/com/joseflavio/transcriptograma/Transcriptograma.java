
/*
 *  Copyright (C) 2015 Jos� Fl�vio de Souza Dias J�nior
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
 *  Direitos Autorais Reservados (C) 2015 Jos� Fl�vio de Souza Dias J�nior
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
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Transcriptograma: Proje��o de express�o g�nica pela m�dia de janela.
 * @author Jos� Fl�vio de Souza Dias J�nior
 * @version 2015
 */
public class Transcriptograma {

	public static void main( String[] args ) {
		
		boolean argumentosOK = true;
		for( int i = 0; i < args.length; i++ ){
			if( args[i].length() == 0 ){
				argumentosOK = false;
				break;
			}
		}
		
		if( args.length < 2 || ! argumentosOK ){
			System.out.println( "Transcriptograma: Proje��o de express�o g�nica pela m�dia de janela." );
			System.out.println( Transcriptograma.class.getSimpleName() + " <arquivo_transcriptomas> <janela:251|300|...>" );
			System.exit( 1 );
		}
		
		try{
			
			File arquivo_transcriptomas = new File( args[0] );
			int  janela                 = Integer.parseInt( args[1] );
			
			List<Expressao> expressoes = new ArrayList<Expressao>( 5000 );
			
			NumberFormat nf = NumberFormat.getNumberInstance( Locale.ENGLISH );
			nf.setGroupingUsed( false );
			nf.setMinimumFractionDigits( 9 );
			
			BufferedReader entrada = new BufferedReader( new FileReader( arquivo_transcriptomas ) );
			try{
				String linha, valor;
				String[] colunas;
				while( ( linha = entrada.readLine() ) != null ){
					colunas = linha.split( "\t" );
					Expressao expressao = new Expressao( colunas[0], new float[colunas.length-1] );
					for( int i = 0; i < expressao.valores.length; i++ ){
						valor = colunas[i+1];
						expressao.valores[i] =
								valor != null && valor.length() > 0 ?
								nf.parse( valor ).floatValue() :
								0f;
					}
					expressoes.add( expressao );
				}
			}finally{
				entrada.close();
			}
			
			int total = expressoes.size();
			int i, j, janelai, janelaf;
			
			for( int pivo = 0; pivo < total; pivo++ ){
				
				Expressao expressao = expressoes.get( pivo );
				int total_valores = expressao.valores.length;
				double[] media = new double[total_valores];
				
				janelai = pivo - (int) Math.floor( janela / 2d );
				janelaf = janelai + janela - 1;
				if( janelai < 0 ) janelai = 0;
				if( janelaf >= total ) janelaf = total - 1;
				
				Arrays.fill( media, 0d );
				for( j = janelai; j <= janelaf; j++ ){
					for( i = 0; i < total_valores; i++ ){
						media[i] += expressoes.get( j ).getValor( i );
					}
				}
				
				System.out.print( expressao.gene );
				
				for( i = 0; i < total_valores; i++ ){
					media[i] /= ( janelaf - janelai + 1 );
					System.out.print( "\t" + nf.format( media[i] ) );
				}
				
				System.out.println();
				System.gc();
				
			}
			
			System.out.flush();
			
		}catch( Exception e ){
			e.printStackTrace();
		}

	}
	
	private static class Expressao {
		
		private String gene;
		
		private float[] valores;

		public Expressao( String gene, float[] valores ) {
			this.gene = gene;
			this.valores = valores;
		}
		
		public float getValor( int i ) {
			return i < valores.length ? valores[i] : 0f;
		}
		
	}

}
