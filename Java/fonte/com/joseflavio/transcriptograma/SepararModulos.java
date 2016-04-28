
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
import java.io.FileWriter;
import java.text.NumberFormat;

/**
 * Divide uma seriação em vários arquivos, cada qual representando um módulo.
 * @author José Flávio de Souza Dias Júnior
 * @version 2016
 */
public class SepararModulos {
	
	public static void main( String[] args ) {
		
		if( args.length < 3 ){
			System.out.println( "Divide uma seriação em vários arquivos, cada qual representando um módulo." );
			System.out.println( SepararModulos.class.getSimpleName() + ".sh <seriacao> <fronteiras> <descarte> [<prefixo>]" );
			System.out.println();
			System.out.println( "<seriacao>   : Arquivo com genes seriados verticalmente." );
			System.out.println( "<fronteiras> : Arquivo com as fronteiras modulares. Ver Fronteiras.sh" );
			System.out.println( "<descarte>   : Prefixo dos genes que serão descartados. Ex.: \"#\"" );
			System.out.println( "<prefixo>    : Prefixo dos arquivos de saída. Ex.: \"Modulo_\"" );
			System.exit( 1 );
		}
		
		try{
			
			File   arq_seriacao   = new File( args[0] );
			File   arq_fronteiras = new File( args[1] );
			String descarte       = args[2];
			String prefixo        = args.length > 3 ? args[3] : null;
			
			if( prefixo == null ){
				prefixo = arq_seriacao.getName();
				prefixo = prefixo.substring( 0, prefixo.lastIndexOf( '.' ) ) + "_";
			}
			
			BufferedReader seriacao   = new BufferedReader( new FileReader( arq_seriacao   ) );
			BufferedReader fronteiras = new BufferedReader( new FileReader( arq_fronteiras ) );
			
			int modulo = 1;
			NumberFormat nf = NumberFormat.getIntegerInstance();
			nf.setMinimumIntegerDigits( 4 );
			nf.setGroupingUsed( false );
			
			try{
				
				int totalGeral = 0;
				
				for( String limite : fronteiras.readLine().trim().split( " " ) ){
					
					int total = Integer.parseInt( limite ) - totalGeral;
					totalGeral += total;
					
					FileWriter saida = new FileWriter(
						new File( prefixo + nf.format( modulo++ ) + ".txt" )
					);
					
					try{
						for( int i = 0; i < total; i++ ){
							String linha = seriacao.readLine();
							if( ! linha.startsWith( descarte ) ) saida.write( linha + "\n" );
						}
					}finally{
						saida.close();
					}
					
				}
				
				FileWriter saida = new FileWriter(
					new File( prefixo + nf.format( modulo++ ) + ".txt" )
				);
				
				try{
					String linha;
					while( ( linha = seriacao.readLine() ) != null ){
						if( ! linha.startsWith( descarte ) ) saida.write( linha + "\n" );
					}
				}finally{
					saida.close();
				}
				
			}finally{
				seriacao.close();
				fronteiras.close();
			}
			
		}catch( Exception e ){
			e.printStackTrace();
		}
		
	}
	
}
