
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
 * @author Jos� Fl�vio de Souza Dias J�nior
 * @version 2015
 * @see Ferramenta#fronteiras(short[], short, short)
 */
public class Fronteiras {

	public static void main( String[] args ) {
		
		if( args.length < 3 ){
			System.out.println( "Calcula as fronteiras modulares automaticamente." );
			System.out.println( Fronteiras.class.getSimpleName() + " <modularidade> <distancia_min> <altura_min>" );
			System.out.println( "<modularidade>  : Arquivo com a modularidade. Ver ModularidadeJanela.sh" );
			System.out.println( "<distancia_min> : Dist�ncia m�nima entre dois picos. Ex.: 50" );
			System.out.println( "<altura_min>    : Altura m�nima dos picos. Ex.: 6" );
			System.exit( 1 );
		}
		
		try{
			
			File  arquivo_modularidade = new File( args[0] );
			short distanciaMinima      = Short.parseShort( args[1] );
			short alturaMinima         = Short.parseShort( args[2] );
			
			short[] modularidade = Ferramenta.carregarOrdem( arquivo_modularidade );
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

}
