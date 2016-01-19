
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
 * Calcula as fronteiras da modularidade por janela.
 * @author José Flávio de Souza Dias Júnior
 * @version 2015
 * @see Ferramenta#fronteiras(short[], short, short)
 */
public class Fronteiras {

	public static void main( String[] args ) {
		
		if( args.length < 3 ){
			System.out.println( "Calcula as fronteiras da modularidade por janela." );
			System.out.println( Fronteiras.class.getSimpleName() + ".sh <modularidade> <distancia_min> <altura_min> [<quantidade>]" );
			System.out.println( "<modularidade>  : Arquivo com a modularidade. Ver ModularidadeJanela.sh" );
			System.out.println( "<distancia_min> : Distância mínima entre dois picos. Ex.: 50" );
			System.out.println( "<altura_min>    : Altura mínima dos picos. Ex.: 6" );
			System.out.println( "<quantidade>    : Quantidade preferencial ou maximizada de módulos." );
			System.exit( 1 );
		}
		
		File  arquivo_modularidade = new File( args[0] );
		short distanciaMinima      = Short.parseShort( args[1] );
		short alturaMinima         = Short.parseShort( args[2] );
		
		if( args.length == 3 ){
			aplicacao1( arquivo_modularidade, distanciaMinima, alturaMinima );
		}else{
			aplicacao2( arquivo_modularidade, distanciaMinima, alturaMinima, Short.parseShort( args[3] ) );
		}

	}
	
	private static void aplicacao1( File arquivo, short distanciaMinima, short alturaMinima ) {
		
		try{
			
			short[] modularidade = Ferramenta.carregarOrdem( arquivo );
			short[] fronteiras   = Ferramenta.fronteiras( modularidade, distanciaMinima, alturaMinima );
			
			boolean primeira = true;
			for( short fronteira : fronteiras ){
				System.out.print( ( primeira ? "" : " " ) + ( fronteira + 1 ) );
				primeira = false;
			}
			System.out.println();
			System.out.flush();
			
		}catch( Exception e ){
			e.printStackTrace();
		}
		
	}
	
	private static void aplicacao2( File arquivo, short distanciaMinima, short alturaMinima, short quantidade ) {
		
		try{
			
			short[] modularidade = Ferramenta.carregarOrdem( arquivo );
			int total = modularidade.length;
			
			short alturaMenor = Short.MAX_VALUE;
			short alturaMaior = Short.MIN_VALUE;
			for( short x : modularidade ){
				if( x < alturaMenor ) alturaMenor = x;
				if( x > alturaMaior ) alturaMaior = x;
			}
			
			short alturaMaxima = (short)( alturaMaior - alturaMenor );
			short distanciaMaxima = (short)( total / quantidade );
			
			short[] resultado = new short[0];

			for( short distMin = distanciaMinima; distMin <= distanciaMaxima; distMin++ ){
				for( short altMin = alturaMinima; altMin <= alturaMaxima; altMin++ ){
					
					short[] fronteiras = Ferramenta.fronteiras( modularidade, distMin, altMin );
					int modulos = fronteiras.length + 1;
					
					if( modulos == quantidade ){
						resultado = fronteiras;
						break;
					}else if	( modulos < quantidade ){
						if( fronteiras.length > resultado.length ){
							resultado = fronteiras;							
						}else{
							break;
						}
					}
					
				}
				if( ( resultado.length + 1 ) == quantidade ) break;
			}
			
			boolean primeira = true;
			for( short fronteira : resultado ){
				System.out.print( ( primeira ? "" : " " ) + ( fronteira + 1 ) );
				primeira = false;
			}
			System.out.println();
			System.out.flush();
			
		}catch( Exception e ){
			e.printStackTrace();
		}
		
	}

}
