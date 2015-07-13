
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
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.NumberFormat;

/**
 * Gera imagens de todas as etapas dos ordenamentos experimentais realizados.
 * @author José Flávio de Souza Dias Júnior
 * @version 2015
 */
public class GerarImagensDeExperimento {

	public static void main( String[] args ) {
		
		if( args.length == 0 ){
			System.out.println( "Gera imagens de todas as etapas dos ordenamentos experimentais realizados." );
			System.out.println( "Informe o local dos experimentos." );
		}
		
		File diretorio = new File( args[0] );
		
		File[] matrizes = diretorio.listFiles( new FilenameFilter() {
			public boolean accept( File dir, String name ) {
				return name.endsWith( ".csv" );
			}
		} );
		
		try{
			
			for( File matrizArquivo : matrizes ){
				
				System.out.println( matrizArquivo.getName() );
				
				int[][] matriz = Ferramenta.carregarMatriz( matrizArquivo );
				
				File diretorioImagens = new File( matrizArquivo.getParent(), matrizArquivo.getName() + "_IMAGENS" );
				if( ! diretorioImagens.exists() ) diretorioImagens.mkdir();

				gerarImagens( "CLA", matrizArquivo, matriz, diretorioImagens );
				gerarImagens( "DEM", matrizArquivo, matriz, diretorioImagens );
				gerarImagens( "CFM", matrizArquivo, matriz, diretorioImagens );
				
				System.gc();
				
			}
			
		}catch( IOException e ){
			e.printStackTrace();
		}

	}
	
	private static void gerarImagens( String algoritmo, File matrizArquivo, int[][] matriz, File diretorioImagens ) throws IOException {

		File diretorioAlgoritmo = new File( diretorioImagens, algoritmo );
		if( ! diretorioAlgoritmo.exists() ) diretorioAlgoritmo.mkdir();
		
		NumberFormat nf = NumberFormat.getIntegerInstance();
		nf.setMinimumIntegerDigits( 4 );
		nf.setGroupingUsed( false );
		
		for( int execucao = 1; execucao <= 50; execucao++ ){
		
			File resultado = new File( matrizArquivo.getParent(), matrizArquivo.getName() + "." + algoritmo + "[" + execucao + "].txt" );
			if( ! resultado.exists() ) break;
			
			System.out.println( resultado.getName() );
			
			File destino = new File( diretorioAlgoritmo, nf.format( execucao ) );
			if( ! destino.exists() ) destino.mkdir();
			
			String dispersaoArquivoNome = resultado.getName();
			dispersaoArquivoNome = dispersaoArquivoNome.substring( 0, dispersaoArquivoNome.length() - 3 ) + "disp.txt";
			FileWriter dispersaoArquivo = new FileWriter( new File( resultado.getParentFile(), dispersaoArquivoNome ) );
			
			int passo = 1;
			long ultimaDispersao = -1;
			for( Registro registro : Ferramenta.carregarRegistros( resultado ) ){
				
				Ferramenta.salvarImagem(
						matriz,
						registro.getOrdem(),
						new File( destino,
								"IMAGEM[" +
								nf.format( passo ) +
								"]_" +
								registro.getTempoDecorrido() +
								"_" +
								registro.getTempoMelhoria() +
								".png"
						)
				);
				
				long dispersao = Ferramenta.calcularDispersao( matriz, registro.getOrdem() );
				if( dispersao != ultimaDispersao ){
					dispersaoArquivo.write( registro.getTempoMelhoria() + "," + dispersao + "\n" );
					ultimaDispersao = dispersao;
				}
				
				passo++;
				System.gc();
				
			}
		
			dispersaoArquivo.close();
			
		}
		
	}

}
