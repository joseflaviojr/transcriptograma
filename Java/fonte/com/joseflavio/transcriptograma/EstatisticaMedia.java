
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
import java.io.FileWriter;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

/**
 * Média dos resultados estatísticos.
 * @author José Flávio de Souza Dias Júnior
 * @version 2015
 */
public class EstatisticaMedia {
	
	public static void main( String[] args ) {
		
		if( args.length == 0 ){
			System.out.println( "Média dos resultados estatísticos." );
			System.out.println( "Informe o arquivo dos resultados estatísticos. Ex.: Estatistica.txt" );
		}
		
		try{
		
			File estatistica = new File( args[0] );
			
			if( ! estatistica.exists() ){
				System.out.println( estatistica.getName() + " não encontrado." );
				System.exit( 1 );
			}

			List<String[]> linhas = new LinkedList<String[]>();
			String[] colunas = new String[15];
			StringBuilder buffer = new StringBuilder( 500 );
			
			NumberFormat nf = NumberFormat.getNumberInstance();
			nf.setGroupingUsed( false );
			nf.setMinimumFractionDigits( 10 );
			
			BufferedReader entrada = new BufferedReader( new FileReader( estatistica ) );
			Ferramenta.proximaLinha( entrada, colunas, ';', buffer );
			while( Ferramenta.proximaLinha( entrada, colunas, ';', buffer ) > 0 ){
				linhas.add( colunas.clone() );
			}
			entrada.close();
			
			List<Media> medias = new LinkedList<Media>();
			Separador separador = new Separador();
			for( int coluna = 8; coluna <= 9; coluna++ ){
				
				medias.add( new MediaAlgoritmo( coluna, "CFM" ) );
				medias.add( new MediaAlgoritmo( coluna, "CLA" ) );
				medias.add( new MediaAlgoritmo( coluna, "DEM" ) );
				
				medias.add( separador );
				
				medias.add( new MediaAlgoritmoOrientacao( coluna, "CFM", 'N', -1 ) );
				medias.add( new MediaAlgoritmoOrientacao( coluna, "CLA", 'N', -1 ) );
				medias.add( new MediaAlgoritmoOrientacao( coluna, "DEM", 'N', -1 ) );
				
				medias.add( separador );
				
				medias.add( new MediaAlgoritmoOrientacao( coluna, "CFM", 'N', 100 ) );
				medias.add( new MediaAlgoritmoOrientacao( coluna, "CLA", 'N', 100 ) );
				medias.add( new MediaAlgoritmoOrientacao( coluna, "DEM", 'N', 100 ) );
				
				medias.add( new MediaAlgoritmoOrientacao( coluna, "CFM", 'N', 200 ) );
				medias.add( new MediaAlgoritmoOrientacao( coluna, "CLA", 'N', 200 ) );
				medias.add( new MediaAlgoritmoOrientacao( coluna, "DEM", 'N', 200 ) );
				
				medias.add( new MediaAlgoritmoOrientacao( coluna, "CFM", 'N', 300 ) );
				medias.add( new MediaAlgoritmoOrientacao( coluna, "CLA", 'N', 300 ) );
				medias.add( new MediaAlgoritmoOrientacao( coluna, "DEM", 'N', 300 ) );
				
				medias.add( separador );
				
				medias.add( new MediaAlgoritmoOrientacao( coluna, "CFM", 'O', -1 ) );
				medias.add( new MediaAlgoritmoOrientacao( coluna, "CLA", 'O', -1 ) );
				medias.add( new MediaAlgoritmoOrientacao( coluna, "DEM", 'O', -1 ) );
				
				medias.add( separador );
				
				medias.add( new MediaRedeAlgoritmo( coluna, "RAL", "CFM", -1 ) );
				medias.add( new MediaRedeAlgoritmo( coluna, "RAL", "CLA", -1 ) );
				medias.add( new MediaRedeAlgoritmo( coluna, "RAL", "DEM", -1 ) );
				medias.add( new MediaRedeAlgoritmo( coluna, "RLE", "CFM", -1 ) );
				medias.add( new MediaRedeAlgoritmo( coluna, "RLE", "CLA", -1 ) );
				medias.add( new MediaRedeAlgoritmo( coluna, "RLE", "DEM", -1 ) );
				medias.add( new MediaRedeAlgoritmo( coluna, "RPM", "CFM", -1 ) );
				medias.add( new MediaRedeAlgoritmo( coluna, "RPM", "CLA", -1 ) );
				medias.add( new MediaRedeAlgoritmo( coluna, "RPM", "DEM", -1 ) );
				
				medias.add( separador );
				
				medias.add( new MediaRedeAlgoritmo( coluna, "RAL-N", "CFM", -1 ) );
				medias.add( new MediaRedeAlgoritmo( coluna, "RAL-N", "CLA", -1 ) );
				medias.add( new MediaRedeAlgoritmo( coluna, "RAL-N", "DEM", -1 ) );
				medias.add( new MediaRedeAlgoritmo( coluna, "RAL-O", "CFM", -1 ) );
				medias.add( new MediaRedeAlgoritmo( coluna, "RAL-O", "CLA", -1 ) );
				medias.add( new MediaRedeAlgoritmo( coluna, "RAL-O", "DEM", -1 ) );
				medias.add( new MediaRedeAlgoritmo( coluna, "RLE-N", "CFM", -1 ) );
				medias.add( new MediaRedeAlgoritmo( coluna, "RLE-N", "CLA", -1 ) );
				medias.add( new MediaRedeAlgoritmo( coluna, "RLE-N", "DEM", -1 ) );
				medias.add( new MediaRedeAlgoritmo( coluna, "RLE-O", "CFM", -1 ) );
				medias.add( new MediaRedeAlgoritmo( coluna, "RLE-O", "CLA", -1 ) );
				medias.add( new MediaRedeAlgoritmo( coluna, "RLE-O", "DEM", -1 ) );
				medias.add( new MediaRedeAlgoritmo( coluna, "RPM-N", "CFM", -1 ) );
				medias.add( new MediaRedeAlgoritmo( coluna, "RPM-N", "CLA", -1 ) );
				medias.add( new MediaRedeAlgoritmo( coluna, "RPM-N", "DEM", -1 ) );
				medias.add( new MediaRedeAlgoritmo( coluna, "RPM-O", "CFM", -1 ) );
				medias.add( new MediaRedeAlgoritmo( coluna, "RPM-O", "CLA", -1 ) );
				medias.add( new MediaRedeAlgoritmo( coluna, "RPM-O", "DEM", -1 ) );
				
				medias.add( separador );
				
				for( int tamanho = 0; tamanho <= 300; tamanho += 100 ){
				
					medias.add( new MediaRedeAlgoritmo( coluna, "RAL-N", "CFM", tamanho ) );
					medias.add( new MediaRedeAlgoritmo( coluna, "RAL-O", "CFM", tamanho ) );
					medias.add( new MediaRedeAlgoritmo( coluna, "RAL-N", "CLA", tamanho ) );
					medias.add( new MediaRedeAlgoritmo( coluna, "RAL-O", "CLA", tamanho ) );
					medias.add( new MediaRedeAlgoritmo( coluna, "RAL-N", "DEM", tamanho ) );
					medias.add( new MediaRedeAlgoritmo( coluna, "RAL-O", "DEM", tamanho ) );
					medias.add( new MediaRedeAlgoritmo( coluna, "RLE-N", "CFM", tamanho ) );
					medias.add( new MediaRedeAlgoritmo( coluna, "RLE-O", "CFM", tamanho ) );
					medias.add( new MediaRedeAlgoritmo( coluna, "RLE-N", "CLA", tamanho ) );
					medias.add( new MediaRedeAlgoritmo( coluna, "RLE-O", "CLA", tamanho ) );
					medias.add( new MediaRedeAlgoritmo( coluna, "RLE-N", "DEM", tamanho ) );
					medias.add( new MediaRedeAlgoritmo( coluna, "RLE-O", "DEM", tamanho ) );
					medias.add( new MediaRedeAlgoritmo( coluna, "RPM-N", "CFM", tamanho ) );
					medias.add( new MediaRedeAlgoritmo( coluna, "RPM-O", "CFM", tamanho ) );
					medias.add( new MediaRedeAlgoritmo( coluna, "RPM-N", "CLA", tamanho ) );
					medias.add( new MediaRedeAlgoritmo( coluna, "RPM-O", "CLA", tamanho ) );
					medias.add( new MediaRedeAlgoritmo( coluna, "RPM-N", "DEM", tamanho ) );
					medias.add( new MediaRedeAlgoritmo( coluna, "RPM-O", "DEM", tamanho ) );
					
					medias.add( separador );
					
				}
				
			}
			
			for( String[] linha : linhas ){
				for( Media media : medias ){
					media.verificar( linha, nf );
				}
			}
			
			FileWriter saida = new FileWriter( new File( estatistica.getParentFile(), "Media-" + estatistica.getName() ) );
			for( Media media : medias ){
				saida.write( media.formatarResultado( nf ) + "\n" );
			}
			saida.close();
			
		}catch( Exception e ){
			e.printStackTrace();
		}

	}
	
	private static abstract class Media {
		
		protected double soma = 0;
		
		protected int total = 0;
		
		protected int coluna;
		
		public Media( int coluna ) {
			this.coluna = coluna;
		}
		
		public double getMedia() {
			return soma / total;
		}

		public abstract boolean verificar( String[] colunas, NumberFormat nf ) throws ParseException;
		
		public abstract String formatarResultado( NumberFormat nf ) throws ParseException;
		
		protected void somar( String valor, NumberFormat nf ) throws ParseException {
			soma += nf.parse( valor ).doubleValue();
			total++;
		}
		
	}
	
	private static class MediaRedeAlgoritmo extends Media {
		
		private String rede;
		
		private String algoritmo;
		
		private int tamanho;
		
		public MediaRedeAlgoritmo( int coluna, String rede, String algoritmo, int tamanho ) {
			super( coluna );
			this.rede = rede;
			this.algoritmo = algoritmo;
			this.tamanho = tamanho;
		}

		@Override
		public boolean verificar( String[] colunas, NumberFormat nf ) throws ParseException {
			if(	colunas[1].startsWith( rede ) &&
				colunas[3].equals( algoritmo ) &&
				( tamanho == -1 || Integer.parseInt( colunas[2] ) == tamanho ) ){
				if( ! Estatistica.inadequado( colunas[coluna] ) ) somar( colunas[coluna], nf );
				return true;
			}
			return false;
		}
		
		@Override
		public String formatarResultado( NumberFormat nf ) throws ParseException {
			return algoritmo + " " + rede + ( tamanho == -1 ? "" : " " + tamanho ) + " (" + Estatistica.COLUNAS[coluna] + ") = " + ( total > 0 ? nf.format( getMedia() ) : "--" );				
		}
		
	}
	
	private static class MediaAlgoritmo extends Media {
		
		private String algoritmo;
		
		public MediaAlgoritmo( int coluna, String algoritmo ) {
			super( coluna );
			this.algoritmo = algoritmo;
		}

		@Override
		public boolean verificar( String[] colunas, NumberFormat nf ) throws ParseException {
			if(	colunas[3].equals( algoritmo ) ){
				if( ! Estatistica.inadequado( colunas[coluna] ) ) somar( colunas[coluna], nf );
				return true;
			}
			return false;
		}
		
		@Override
		public String formatarResultado( NumberFormat nf ) throws ParseException {
			return algoritmo + " (" + Estatistica.COLUNAS[coluna] + ") = " + ( total > 0 ? nf.format( getMedia() ) : "--" );
		}
		
	}
	
	private static class MediaAlgoritmoOrientacao extends Media {
		
		private String algoritmo;
		
		private char orientacao;
		
		private int tamanho;
		
		public MediaAlgoritmoOrientacao( int coluna, String algoritmo, char orientacao, int tamanho ) {
			super( coluna );
			this.algoritmo = algoritmo;
			this.orientacao = orientacao;
			this.tamanho = tamanho;
		}

		@Override
		public boolean verificar( String[] colunas, NumberFormat nf ) throws ParseException {
			if(	colunas[1].endsWith( "" + orientacao ) &&
				colunas[3].equals( algoritmo ) &&
				( tamanho == -1 || Integer.parseInt( colunas[2] ) == tamanho ) ){
				if( ! Estatistica.inadequado( colunas[coluna] ) ) somar( colunas[coluna], nf );
				return true;
			}
			return false;
		}
		
		@Override
		public String formatarResultado( NumberFormat nf ) throws ParseException {
			return algoritmo + "-" + orientacao + ( tamanho == -1 ? "" : " " + tamanho ) + " (" + Estatistica.COLUNAS[coluna] + ") = " + ( total > 0 ? nf.format( getMedia() ) : "--" );				
		}
		
	}
	
	private static class Separador extends Media {
		
		public Separador() {
			super( 0 );
		}

		@Override
		public boolean verificar( String[] colunas, NumberFormat nf ) throws ParseException {
			return false;
		}
		
		@Override
		public String formatarResultado( NumberFormat nf ) throws ParseException {
			return "------------------------------";
		}
		
	}
	
}
