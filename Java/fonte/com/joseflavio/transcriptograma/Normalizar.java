
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
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Normaliza linearmente todos os valores num�ricos de uma tabela CSV em rela��o ao maior valor dela. 
 * @author Jos� Fl�vio de Souza Dias J�nior
 * @version 2015
 */
public class Normalizar {

	public static void main( String[] args ) {
		
		boolean argumentosOK = true;
		for( int i = 0; i < args.length; i++ ){
			if( args[i].length() == 0 ){
				argumentosOK = false;
				break;
			}
		}
		
		if( args.length < 3 || ! argumentosOK ){
			System.out.println( "Normaliza linearmente todos os valores num�ricos de uma tabela CSV em rela��o ao maior valor dela." );
			System.out.println( Normalizar.class.getSimpleName() + " <arquivo_csv> <separador_csv:tab|esp|virg|pvirg|vert> <limite:100|1000|...>" );
			System.exit( 1 );
		}
		
		try{
			
			File   arquivo       = new File( args[0] );
			String separador_csv = args[1];
			int    limite        = Integer.parseInt( args[2] );
			
			if( separador_csv.equals( "tab" ) ) separador_csv = "\t";
			else if( separador_csv.equals( "esp" ) ) separador_csv = " ";
			else if( separador_csv.equals( "virg" ) ) separador_csv = ",";
			else if( separador_csv.equals( "pvirg" ) ) separador_csv = ";";
			else if( separador_csv.equals( "vert" ) ) separador_csv = "|";
			
			List<String[]> linhas = new ArrayList<String[]>( 5000 );
			double valor;
			double maiorValor = Double.MIN_VALUE;
			
			NumberFormat nf = NumberFormat.getNumberInstance( Locale.ENGLISH );
			nf.setGroupingUsed( false );
			nf.setMinimumFractionDigits( 9 );
			
			BufferedReader entrada = new BufferedReader( new FileReader( arquivo ) );
			try{
				String linha;
				String[] colunas;
				while( ( linha = entrada.readLine() ) != null ){
					colunas = linha.split( separador_csv );
					for( String coluna : colunas ){
						try{
							valor = nf.parse( coluna ).doubleValue();
							if( valor > maiorValor ) maiorValor = valor;
						}catch( Exception e ){
						}
					}
					linhas.add( colunas );
				}
			}finally{
				entrada.close();
			}
			
			nf.setMaximumFractionDigits( 0 );
			
			for( String[] colunas : linhas ){
				boolean primeiro = true;
				for( String coluna : colunas ){
					if( ! primeiro ) System.out.print( separador_csv );
					primeiro = false;
					try{
						valor = nf.parse( coluna ).doubleValue() / maiorValor * limite;
						System.out.print( nf.format( (long) valor ) );
					}catch( Exception e ){
						System.out.print( coluna );
					}
				}
				System.out.println();
			}
			
			System.out.flush();
			
		}catch( Exception e ){
			e.printStackTrace();
		}

	}
	
}
