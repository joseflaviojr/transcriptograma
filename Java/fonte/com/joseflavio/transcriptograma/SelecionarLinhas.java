
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
import java.io.IOException;
import java.util.Arrays;

/**
 * Seleciona espec�ficas linhas de um arquivo de texto.
 * @author Jos� Fl�vio de Souza Dias J�nior
 * @version 2015
 */
public class SelecionarLinhas {

	public static void main( String[] args ) {
		
		if( args.length < 2 ){
			System.out.println( "Seleciona espec�ficas linhas de um arquivo de texto." );
			System.out.println( SelecionarLinhas.class.getSimpleName() + " <arquivo> <linha1> <linha2> <linhaN:1..N>" );
			System.exit( 1 );
		}
		
		try{
			
			File arquivo = new File( args[0] );
			int  total   = args.length - 1;
			
			int[] numero = new int[total];
			for( int i = 0; i < total; i++ ) numero[i] = Integer.parseInt( args[i+1] );
			Arrays.sort( numero );
			
			BufferedReader entrada = new BufferedReader( new FileReader( arquivo ) );
			try{
				for( int i = 0; i < total; i++ ){
					saltarLinhas( entrada, i > 0 ? numero[i]-numero[i-1]-1 : numero[i]-1 );
					System.out.println( entrada.readLine() );
				}
			}finally{
				entrada.close();
			}
			
			System.out.flush();
			
		}catch( Exception e ){
			e.printStackTrace();
		}

	}
	
	private static void saltarLinhas( BufferedReader entrada, int quantidade ) throws IOException {
		for( int i = 0; i < quantidade; i++ ){
			entrada.readLine();
		}
	}
	
}
