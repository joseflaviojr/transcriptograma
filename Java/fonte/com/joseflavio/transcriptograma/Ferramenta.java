
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

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

/**
 * @author José Flávio de Souza Dias Júnior
 * @version 2015
 */
public class Ferramenta {
	
	/**
	 * @param arquivo Arquivo no formato CSV.
	 */
	public static int[][] carregarMatriz( File arquivo ) throws IOException {
		
		BufferedReader entrada = new BufferedReader( new FileReader( arquivo ) );
		
		try{
		
			// Tamanho
			
			int total = 1;
			
			for( char c : entrada.readLine().toCharArray() ){
				if( c == ',' || c == ';' || c == '|' || c == ' ' || c == '\t' ) total++;
			}
			
			entrada.close();
			entrada = new BufferedReader( new FileReader( arquivo ) );
			
			// Matriz
			
			int[][] matriz = new int[total][total];
			
			char c, numero[] = new char[50];
			int  d = 0, i = 0, j = 0;
	
			while( ( c = (char) entrada.read() ) != (char) -1 ){
	
				switch( c ){
					case ',' :
					case ';' :
					case '|' :
					case ' ' :
					case '\t' :
					case '\n' :
						matriz[i][j] = Integer.parseInt( new String( numero, 0, d ) );
						j++;
						d = 0;
						break;
					case '"' :
					case '\'' :
					case '\r' :
						break;
					default :
						numero[d++] = c;
						break;
				}
	
				if( j == total ){
					i++;
					j = 0;
				}
	
			}
	
			if( d > 0 && j > 0 && j < total ){
				matriz[i][j] = Integer.parseInt( new String( numero, 0, d ) );
			}
			
			return matriz;
			
		}finally{
			entrada.close();
		}
		
	}
	
	public static Registro[] carregarRegistros( File arquivo ) throws IOException {
		
		BufferedReader entrada = new BufferedReader( new FileReader( arquivo ) );
		
		try{
			
			List<Registro> registros = new ArrayList<Registro>( 100 );
			List<Integer> ordem = new ArrayList<Integer>( 5000 );
			String linha;
			
			while( ( linha = entrada.readLine() ) != null ){
				
				Registro registro = new Registro();
				
				registro.setTempoDecorrido( Integer.parseInt( linha ) );
				registro.setTempoMelhoria( Integer.parseInt( entrada.readLine() ) );
				
				ordem.clear();
				for( String n : entrada.readLine().split( "," ) ) ordem.add( Integer.parseInt( n ) );
				int[] array = new int[ ordem.size() ];
				for( int i = 0; i < array.length; i++ ) array[i] = ordem.get( i );
				registro.setOrdem( array );
				
				registros.add( registro );
				
			}
			
			return registros.toArray( new Registro[ registros.size() ] );
			
		}finally{
			entrada.close();
		}
		
	}
	
	public static int[] carregarOrdem( File arquivo ) throws IOException {
		
		BufferedReader entrada = new BufferedReader( new FileReader( arquivo ) );
		
		try{
			
			List<Integer> ordem = new ArrayList<Integer>( 5000 );
			for( String n : entrada.readLine().split( "," ) ) ordem.add( Integer.parseInt( n ) );
			int[] array = new int[ ordem.size() ];
			for( int i = 0; i < array.length; i++ ) array[i] = ordem.get( i );
			return array;
			
		}finally{
			entrada.close();
		}
		
	}
	
	public static List<String> carregarNomes( File arquivo ) throws IOException {
		
		BufferedReader entrada = new BufferedReader( new FileReader( arquivo ) );
		
		try{
			
			List<String> nomes = new ArrayList<String>( 5000 );
			
			String linha;
			while( ( linha = entrada.readLine() ) != null ) nomes.add( linha );
			
			return nomes;
			
		}finally{
			entrada.close();
		}
		
	}
	
	public static Map<String,String> carregarMapa( File arquivo, String separador ) throws IOException {
		
		BufferedReader entrada = new BufferedReader( new FileReader( arquivo ) );
		int tamanhoEstimado = (int)( arquivo.length() / 30d );
		
		try{
			
			Map<String,String> mapa = new HashMap<String,String>( tamanhoEstimado );
			
			String linha;
			String[] valores;
			while( ( linha = entrada.readLine() ) != null ){
				
				valores = linha.split( separador );
				
				mapa.put( valores[0], valores[1] );
				
			}
			
			return mapa;
			
		}finally{
			entrada.close();
		}
		
	}
	
	/**
	 * Imagem PNG da matriz ordenada.
	 */
	public static void salvarImagem( int[][] matriz, int[] ordem, File destino ) throws IOException {
		
		int total = ordem.length;
		BufferedImage img = new BufferedImage( total, total, BufferedImage.TYPE_INT_RGB );
		
		for( int i = 0; i < total; i++ ){
			for( int j = 0; j < total; j++ ){
				img.setRGB( i, j, matriz[ordem[i]-1][ordem[j]-1] != 0 ? Color.BLACK.getRGB() : Color.WHITE.getRGB() );
			}
		}
		
		ImageIO.write( img, "PNG", destino );
		
	}
	
	public static boolean grafoOrientado( int[][] matriz ) {
		int total = matriz.length;
		for( int i = 0; i < (total-1); i++ ){
			for( int j = i+1; j < total; j++ ){
				if( matriz[i][j] != matriz[j][i] ) return true;
			}
		}
		return false;
	}
	
	public static long calcularDispersao( int[][] matriz, int[] ordem ) {
		return calcularDispersao( matriz, ordem, grafoOrientado( matriz ) );
	}
	
	public static long calcularDispersao( int[][] matriz, int[] ordem, boolean orientado ) {
		
		long dispersao = 0;
		int total = ordem.length;
		int i, j;

		if( ! orientado ){
			for( i = 0; i < (total-1); i++ ){
				for( j = i+1; j < total; j++ ){
					if( matriz[ordem[i]-1][ordem[j]-1] != 0 ){
						dispersao += (j-i);
					}
				}
			}
		}else{
			for( i = 0; i < total; i++ ){
				for( j = 0; j < total; j++ ){
					if( matriz[ordem[i]-1][ordem[j]-1] != 0 ){
						dispersao += (i>=j) ? (i-j) : (j-i);
					}
				}
			}
		}

		return dispersao;
		
	}
	
	/**
	 * Modularidade por densidade de cada coluna da matriz ordenada.
	 */
	public static int[] calcularModularidadeDensidade( int[][] matriz, int[] ordem, double densidadeMin ) {
		
		int total = ordem.length;
		int[] modularidade = new int[total];
		int i, j, raio, grau;
		double densidade;
		
		for( j = 0; j < total; j++ ){
			
			raio = Math.max( j, total-j-1 );
			grau = 0;
			
			for( i = 0; i < total; i++ ){
				if( matriz[ordem[i]-1][ordem[j]-1] != 0 ) grau++;
			}
			
			while( raio > 0 ){
				
				densidade = grau / (double)( raio * 2 );
				if( densidade >= densidadeMin ) break;
				
				i = j - raio;
				if( i >= 0 && matriz[ordem[i]-1][ordem[j]-1] != 0 ) grau--;
				
				i = j + raio;
				if( i < total && matriz[ordem[i]-1][ordem[j]-1] != 0 ) grau--;
				
				raio--;
				
			}

			modularidade[j] = raio;
			
		}
		
		return modularidade;
		
	}
	
	/**
	 * Modularidade por janela de cada coluna da matriz ordenada.
	 */
	public static int[] calcularModularidadeJanela( int[][] matriz, int[] ordem, int janela ) {
	
		int total = ordem.length;
		int[] modularidade = new int[total];
		int[] grau = new int[total];
		int i, j, janelai, janelaf;
		double soma1, soma2;
		
		for( i = 0; i < total; i++ ){
			grau[i] = 0;
			for( j = 0; j < total; j++ ){
				grau[i] += matriz[ordem[i]-1][ordem[j]-1] != 0 ? 1 : 0;
			}
		}
		
		for( int pivo = 0; pivo < total; pivo++ ){
			
			janelai = pivo - (int) Math.floor( janela / 2d );
			janelaf = janelai + janela - 1;
			if( janelai < 0 ) janelai = 0;
			if( janelaf >= total ) janelaf = total - 1;
			
			soma1 = 0;
			soma2 = 0;
			
			for( i = janelai; i <= janelaf; i++ ){
				for( j = janelai; j <= janelaf; j++ ){
					soma1 += matriz[ordem[i]-1][ordem[j]-1] != 0 ? 1 : 0;
				}
				soma2 += grau[i];
			}
			
			modularidade[pivo] = (int) Math.ceil( soma1 / soma2 * 100d );
			
		}
		
		return modularidade;
		
	}
	
	/**
	 * Reconhece as colunas da próxima linha.<br>
	 * Código fonte proveniente de <a href="https://github.com/joseflaviojr/livre/blob/master/Java/fonte/com/joseflavio/util/CSVUtil.java">https://github.com/joseflaviojr/livre/blob/master/Java/fonte/com/joseflavio/util/CSVUtil.java</a>
	 * @param arquivo Arquivo CSV.
	 * @param colunas Array que receberá o conteúdo de cada coluna.
	 * @param separador Separador de colunas.
	 * @param buffer Buffer a ser utilizado durante o processamento da linha.
	 * @return o número de colunas lidas.
	 */
	public static int proximaLinha( Reader arquivo, String[] colunas, char separador, StringBuilder buffer ) throws IOException {
		
		int len = buffer.length();
		if( len > 0 ) buffer.delete( 0, len );
		
		Arrays.fill( colunas, null );
		
		char ch;
		int c = 0;
		final char FDA = (char) -1;
		
		while( ( ch = (char) arquivo.read() ) != FDA ){
			
			if( ch == separador ){
				colunas[c++] = buffer.toString();
				buffer.delete( 0, buffer.length() );
			}else if( ch == '\n' ){
				colunas[c++] = buffer.toString();
				return c;
			}else if( ch == '\r' ){
				continue;
			}else{
				buffer.append( ch );
			}
			
		}
		
		if( c > 0 || buffer.length() > 0 ) colunas[c++] = buffer.toString();
		return c;
		
	}
	
	public static String concatenar( String[] colunas, String separador, int inicio, int fim ) {
		StringBuilder sb = new StringBuilder( 100 );
		boolean primeiro = true;
		for( int i = inicio; i <= fim; i++ ){
			sb.append( ( primeiro ? "" : separador ) + colunas[i] );
			primeiro = false;
		}
		return sb.toString();
	}
	
}
