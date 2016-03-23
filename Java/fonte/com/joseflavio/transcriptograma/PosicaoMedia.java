
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
import java.util.LinkedList;
import java.util.List;

/**
 * Posi��o m�dia das linhas que cont�m pelo menos um dos conte�dos informados.
 * @author Jos� Fl�vio de Souza Dias J�nior
 * @version 2015
 */
public class PosicaoMedia {

	public static void main( String[] args ) {
		
		if( args.length < 2 ){
			System.out.println( "Posi��o m�dia das linhas que cont�m pelo menos um dos conte�dos informados." );
			System.out.println( PosicaoMedia.class.getSimpleName() + " <arquivo> <conteudo1> <conteudo2> <conteudoN>" );
			System.exit( 1 );
		}
		
		try{
			
			File arquivo = new File( args[0] );
			
			List<String> conteudos = new LinkedList<String>();
			for( int i = 0; i < (args.length-1); i++ ) conteudos.add( args[i+1] );
			
			long posicao = 1;
			long media = 0;
			long total = 0;
			
			BufferedReader entrada = new BufferedReader( new FileReader( arquivo ) );
			try{
				String linha;
				while( ( linha = entrada.readLine() ) != null ){
					for( String conteudo : conteudos ){
						if( linha.contains( conteudo ) ){
							media += posicao;
							total++;
							break;
						}
					}
					posicao++;
				}
			}finally{
				entrada.close();
			}
			
			media /= total;
			
			System.out.println( "" + media );
			System.out.flush();
			
		}catch( Exception e ){
			e.printStackTrace();
		}

	}
	
}
