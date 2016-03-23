
/*
 *  Copyright (C) 2015-2016 José Flávio de Souza Dias Júnior
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
 *  Direitos Autorais Reservados (C) 2015-2016 José Flávio de Souza Dias Júnior
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;

/**
 * Posição média das linhas que contém pelo menos um dos conteúdos informados.
 * @author José Flávio de Souza Dias Júnior
 * @version 2015
 */
public class PosicaoMedia {

	public static void main( String[] args ) {
		
		if( args.length < 2 ){
			System.out.println( "Posição média das linhas que contém pelo menos um dos conteúdos informados." );
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
