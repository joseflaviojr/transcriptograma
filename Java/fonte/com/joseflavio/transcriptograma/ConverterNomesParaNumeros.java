
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Converte nomes de genes/prote�nas para n�meros ordinais.
 * @author Jos� Fl�vio de Souza Dias J�nior
 * @version 2015
 */
public class ConverterNomesParaNumeros {

	public static void main( String[] args ) {
		
		if( args.length < 2 ){
			System.out.println( "Converte nomes de genes/prote�nas para n�meros ordinais." );
			System.out.println( ConverterNomesParaNumeros.class.getSimpleName() + " <arquivo_nomes> <arquivo_referencia>" );
			System.exit( 1 );
		}
		
		try{
			
			File arquivo_nomes = new File( args[0] );
			File arquivo_referencia   = new File( args[1] );
			
			List<String> nomes = Ferramenta.carregarNomes( arquivo_nomes );
			List<String> referencia = Ferramenta.carregarNomes( arquivo_referencia );
			
			int total = referencia.size();
			Map<String,Integer> mapa = new HashMap<String,Integer>( total );
			for( int i = 0; i < total; i++ ) mapa.put( referencia.get( i ), i+1 );
			
			boolean primeiro = true;
			for( String n : nomes ){
				if( ! primeiro ) System.out.print( ',' );
				System.out.print( mapa.get( n ) );
				primeiro = false;
			}
			System.out.flush();
			
		}catch( Exception e ){
			e.printStackTrace();
		}

	}

}
