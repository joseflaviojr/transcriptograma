
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
import java.io.FileWriter;
import java.text.NumberFormat;

/**
 * Divide uma seria��o em v�rios arquivos, cada qual representando um m�dulo.
 * @author Jos� Fl�vio de Souza Dias J�nior
 * @version 2016
 */
public class SepararModulos {
	
	public static void main( String[] args ) {
		
		if( args.length < 3 ){
			System.out.println( "Divide uma seria��o em v�rios arquivos, cada qual representando um m�dulo." );
			System.out.println( SepararModulos.class.getSimpleName() + ".sh <seriacao> <fronteiras> <descarte> [<prefixo>]" );
			System.out.println();
			System.out.println( "<seriacao>   : Arquivo com genes seriados verticalmente." );
			System.out.println( "<fronteiras> : Arquivo com as fronteiras modulares. Ver Fronteiras.sh" );
			System.out.println( "<descarte>   : Prefixo dos genes que ser�o descartados. Ex.: \"#\"" );
			System.out.println( "<prefixo>    : Prefixo dos arquivos de sa�da. Ex.: \"Modulo_\"" );
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
