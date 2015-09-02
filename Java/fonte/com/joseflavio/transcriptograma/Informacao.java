
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

import java.io.File;

/**
 * Informa��es sobre uma matriz de adjac�ncias.
 * @author Jos� Fl�vio de Souza Dias J�nior
 * @version 2015
 */
public class Informacao {

	public static void main( String[] args ) {
		
		if( args.length < 1 || args[0].length() == 0 ){
			System.out.println( "Informa��es sobre uma matriz de adjac�ncias." );
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
			System.out.println( "Grafo orientado: " + ( orientado ? "Sim" : "N�o" ) );
			System.out.println( "V�rtices: " + vertices );
			System.out.println( "Arestas:  " + arestas  );
			System.out.println( "Arestas/V�rtices:  " + ( arestas / (float) vertices ) );
			System.out.println();
			System.out.println( "Dispers�o inicial (Di)  : " + Di );
			System.out.println( "Dispers�o final   (Df)  : " + Df );
			System.out.println( "Dispers�o m�nima  (Dmin): " + Dmin );
			System.out.println( "Dispers�o m�xima  (Dmax): " + Dmax );
			System.out.println( "Redu��o local           : " + ( ( 1f - Df / (float) Di ) * 100f ) + " %   1-Df/Di" );
			System.out.println( "Redu��o global          : " + ( ( 1f - ( Df - Dmin ) / (float)( Dmax - Dmin ) ) * 100f ) + " %   1-(Df-Dmin)/(Dmax-Dmin)" );
			System.out.println();
			
		}catch( Exception e ){
			e.printStackTrace();
		}

	}

}
