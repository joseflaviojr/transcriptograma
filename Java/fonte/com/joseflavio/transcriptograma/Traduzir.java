
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Map;

/**
 * Traduz nomes contidos em planilha CSV.
 * @author José Flávio de Souza Dias Júnior
 * @version 2015
 */
public class Traduzir {

	public static void main( String[] args ) {
		
		boolean argumentosOK = true;
		for( int i = 0; i < args.length; i++ ){
			if( args[i].length() == 0 ){
				argumentosOK = false;
				break;
			}
		}
		
		if( args.length < 3 || ! argumentosOK ){
			System.out.println( "Traduz nomes contidos em planilha CSV." );
			System.out.println( Traduzir.class.getSimpleName() + " <arquivo_traducao_nomes> <arquivo_csv> <separador_csv:tab|esp|virg|pvirg|vert> [<apenas_primeira_coluna:v*|f>]" );
			System.exit( 1 );
		}
		
		try{
			
			File    arquivo_nomes = new File( args[0] );
			File    arquivo_csv   = new File( args[1] );
			String  separador_csv = args[2];
			boolean apenasColuna1 = args.length <= 3 || args[3].equals( "v" );
			
			if( separador_csv.equals( "tab" ) ) separador_csv = "\t";
			else if( separador_csv.equals( "esp" ) ) separador_csv = " ";
			else if( separador_csv.equals( "virg" ) ) separador_csv = ",";
			else if( separador_csv.equals( "pvirg" ) ) separador_csv = ";";
			else if( separador_csv.equals( "vert" ) ) separador_csv = "|";
			
			Map<String,String> mapa = Ferramenta.carregarMapa( arquivo_nomes, "\t" );
			
			BufferedReader entrada = new BufferedReader( new FileReader( arquivo_csv ) );
			try{
				String linha;
				String[] colunas;
				String traducao;
				boolean primeira;
				while( ( linha = entrada.readLine() ) != null ){
					colunas = linha.split( separador_csv );
					primeira = true;
					for( String coluna : colunas ){
						if( ! primeira ) System.out.print( separador_csv );
						traducao = primeira || ! apenasColuna1 ? mapa.get( coluna ) : null;
						System.out.print( traducao != null ? traducao : coluna );
						primeira = false;
					}
					System.out.println();
				}
			}finally{
				entrada.close();
			}
			
			System.out.flush();
			
		}catch( Exception e ){
			e.printStackTrace();
		}

	}

}
