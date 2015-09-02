
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

import java.io.File;

/**
 * Informações sobre uma matriz de adjacências.
 * @author José Flávio de Souza Dias Júnior
 * @version 2015
 */
public class Informacao {

	public static void main( String[] args ) {
		
		if( args.length < 1 || args[0].length() == 0 ){
			System.out.println( "Informações sobre uma matriz de adjacências." );
			System.out.println( Informacao.class.getSimpleName() + " <arquivo_matriz> [<arquivo_ordem>]" );
			System.exit( 1 );
		}
		
		try{
			
			File arquivo_matriz  = new File( args[0] );
			File arquivo_ordem   = args.length > 1 ? new File( args[1] ) : null;
			
			short[][] matriz = Ferramenta.carregarMatriz( arquivo_matriz );
			short[] ordem    = arquivo_ordem != null ? Ferramenta.carregarOrdem( arquivo_ordem ) : new short[ matriz[0].length ];
			
			int vertices = ordem.length;
			boolean orientado = Ferramenta.grafoOrientado( matriz );
			
			short[] ordemNatural = new short[vertices];
			for( int i = 0; i < vertices; i++ ){
				ordemNatural[i] = (short)( i + 1 );
			}
			
			if( arquivo_ordem == null ){
				System.arraycopy( ordemNatural, 0, ordem, 0, vertices );
			}
			
			long[] dispersaoMinMax = Ferramenta.calcularDispersaoMinMax( matriz, orientado );
			long   arestas         = dispersaoMinMax[2];
			
			long Di   = Ferramenta.calcularDispersao( matriz, ordemNatural, orientado );
			long Df   = Ferramenta.calcularDispersao( matriz, ordem, orientado );
			long Dmin = dispersaoMinMax[0];
			long Dmax = dispersaoMinMax[1];
			
			System.out.println();
			System.out.println( "Matriz: " + arquivo_matriz.getName() );
			System.out.println( "Ordem:  " + ( arquivo_ordem != null ? arquivo_ordem.getName() : "Natural" ) );
			System.out.println();
			System.out.println( "Grafo orientado: " + ( orientado ? "Sim" : "Não" ) );
			System.out.println( "Vértices: " + vertices );
			System.out.println( "Arestas:  " + arestas  );
			System.out.println( "Arestas/Vértices:  " + ( arestas / (float) vertices ) );
			System.out.println();
			System.out.println( "Dispersão inicial (Di)  : " + Di );
			System.out.println( "Dispersão final   (Df)  : " + Df );
			System.out.println( "Dispersão mínima  (Dmin): " + Dmin );
			System.out.println( "Dispersão máxima  (Dmax): " + Dmax );
			System.out.println( "Redução local           : " + ( ( 1f - Df / (float) Di ) * 100f ) + " %   1-Df/Di" );
			System.out.println( "Redução global          : " + ( ( 1f - ( Df - Dmin ) / (float)( Dmax - Dmin ) ) * 100f ) + " %   1-(Df-Dmin)/(Dmax-Dmin)" );
			System.out.println();
			
		}catch( Exception e ){
			e.printStackTrace();
		}

	}

}
