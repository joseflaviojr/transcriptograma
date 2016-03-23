
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
	public static short[][] carregarMatriz( File arquivo ) throws IOException {
		
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
			
			short[][] matriz = new short[total][total];
			
			char c, numero[] = new char[50];
			int d = 0, i = 0, j = 0;
	
			while( ( c = (char) entrada.read() ) != (char) -1 ){
	
				switch( c ){
					case ',' :
					case ';' :
					case '|' :
					case ' ' :
					case '\t' :
					case '\n' :
						matriz[i][j] = Short.parseShort( new String( numero, 0, d ) );
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
				matriz[i][j] = Short.parseShort( new String( numero, 0, d ) );
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
			List<Short> ordem = new ArrayList<Short>( 5000 );
			String linha;
			
			while( ( linha = entrada.readLine() ) != null ){
				
				Registro registro = new Registro();
				
				registro.setTempoDecorrido( Integer.parseInt( linha ) );
				registro.setTempoMelhoria( Integer.parseInt( entrada.readLine() ) );
				
				ordem.clear();
				for( String n : entrada.readLine().split( "," ) ) ordem.add( Short.parseShort( n ) );
				short[] array = new short[ ordem.size() ];
				for( int i = 0; i < array.length; i++ ) array[i] = ordem.get( i );
				registro.setOrdem( array );
				
				registros.add( registro );
				
			}
			
			return registros.toArray( new Registro[ registros.size() ] );
			
		}finally{
			entrada.close();
		}
		
	}
	
	public static short[] carregarOrdem( File arquivo ) throws IOException {
		
		BufferedReader entrada = new BufferedReader( new FileReader( arquivo ) );
		
		try{
			
			List<Short> ordem = new ArrayList<Short>( 5000 );
			for( String n : entrada.readLine().split( "," ) ) ordem.add( Short.parseShort( n ) );
			short[] array = new short[ ordem.size() ];
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
	
	public static List<String> carregarLinhas( File arquivo ) throws IOException {
	
		BufferedReader entrada = new BufferedReader( new FileReader( arquivo ) );
		int tamanhoEstimado = (int)( arquivo.length() / 30d );
		
		try{
			
			List<String> linhas = new ArrayList<String>( tamanhoEstimado );
			
			String linha;
			while( ( linha = entrada.readLine() ) != null ){
				linhas.add( linha );
			}
			
			return linhas;
			
		}finally{
			entrada.close();
		}
		
	}
	
	/**
	 * Imagem PNG da matriz ordenada.
	 */
	public static void salvarImagem( short[][] matriz, short[] ordem, File destino ) throws IOException {
		
		int total = ordem.length;
		BufferedImage img = new BufferedImage( total, total, BufferedImage.TYPE_INT_RGB );
		
		for( int i = 0; i < total; i++ ){
			for( int j = 0; j < total; j++ ){
				img.setRGB( i, j, matriz[ordem[i]-1][ordem[j]-1] != 0 ? Color.BLACK.getRGB() : Color.WHITE.getRGB() );
			}
		}
		
		ImageIO.write( img, "PNG", destino );
		
	}
	
	public static boolean grafoOrientado( short[][] matriz ) {
		int total = matriz.length;
		for( int i = 0; i < (total-1); i++ ){
			for( int j = i+1; j < total; j++ ){
				if( matriz[i][j] != matriz[j][i] ) return true;
			}
		}
		return false;
	}
	
	public static long calcularDispersao( short[][] matriz, short[] ordem ) {
		return calcularDispersao( matriz, ordem, grafoOrientado( matriz ) );
	}
	
	public static long calcularDispersao( short[][] matriz, short[] ordem, boolean orientado ) {
		
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
	 * Calcula a dispersão virtual fisicamente mínima e a máxima.<br>
	 * <pre>
	 * [0] = Dispersão mínima
	 * [1] = Dispersão máxima
	 * [2] = Total de arestas
	 * </pre>
	 */
	public static long[] calcularDispersaoMinMax( short[][] matriz, boolean orientado ) {
		
		long resultado[] = { 0, 0, 0 };
		long a, arestas = 0;
		int  i, j, total = matriz[0].length;

		for( i = 0; i < total; i++ ){
			for( j = 0; j < total; j++ ){
				if( matriz[i][j] != 0 ) arestas++;
			}
		}
		
		if( ! orientado ) arestas /= 2;
		resultado[2] = arestas;
		
		// Dispersão mínima
		
		for( i = total - 1, a = arestas; a > 0 && i > 0; i-- ){
			for( j = i; a > 0 && j > 0; j-- ){
				resultado[0] += total - i;
				a--;
				if( orientado && a > 0 ){
					resultado[0] += total - i;
					a--;
				}
			}
		}
		
		// Dispersão máxima
		
		for( i = 1, a = arestas; a > 0 && i < total; i++ ){
			for( j = i; a > 0 && j > 0; j-- ){
				resultado[1] += total - i;
				a--;
				if( orientado && a > 0 ){
					resultado[1] += total - i;
					a--;
				}
			}
		}
		
		return resultado;

	}
	
	/**
	 * Custo CFM.
	 * @param matriz Matriz de adjacências de grafo não orientado e sem ponderação.
	 */
	public static long calcularCustoCFM( short[][] matriz, short[] ordem ) {
		
		long custo = 0;

		int total = ordem.length;
		int ultimo = total - 1;
		int i, j, aresta, vizinhanca;

		for( i = 0; i < ultimo; i++ ){
			for( j = i+1; j < total; j++ ){
				aresta = matriz[ordem[i]-1][ordem[j]-1];
				vizinhanca = 0;
				if( i != ultimo ) vizinhanca += aresta - matriz[ordem[i+1]-1][ordem[j  ]-1];
				if( j != ultimo ) vizinhanca += aresta - matriz[ordem[i  ]-1][ordem[j+1]-1];
				if( i != 0 )      vizinhanca += aresta - matriz[ordem[i-1]-1][ordem[j  ]-1];
				if( j != 0 )      vizinhanca += aresta - matriz[ordem[i  ]-1][ordem[j-1]-1];
				custo += Math.abs(i-j) * Math.abs(vizinhanca);
			}
		}

		return custo;
		
	}
	
//	/**
//	 * Atualiza o peso de cada aresta conforme a distância entre os pares de vértices no ordenamento.
//	 */
//	public static void atualizarArestas( short[][] matriz, short[] ordem ) {
//		
//	}
	
	/**
	 * Escada: fator que mede a qualidade da redução do número de arestas entre as diagonais a partir do centro.
	 */
	public static long calcularEscada( short[][] matriz, short[] ordem, boolean orientado ) {

		long escada = 0;
		int total = ordem.length;
		short[] vetor = new short[total-1];
		
		for( int d = 1; d < total; d++ ){
			for( int i = 0; i < (total-d); i++ ){
				vetor[d-1] += matriz[ordem[i]-1][ordem[i+d]-1] != 0 ? 1 : 0;
			}
		}
		
		if( orientado ){
			for( int d = 1; d < total; d++ ){
				for( int i = d; i < total; i++ ){
					vetor[d-1] += matriz[ordem[i]-1][ordem[i-d]-1] != 0 ? 1 : 0;
				}
			}
		}
		
		for( int i = 0; i < (vetor.length-1); i++ ){
			escada += vetor[i] - vetor[i+1];
		}

		return escada;
		
	}
	
	/**
	 * @param matriz Matriz de adjacências.
	 * @param ordem Sequência numérica com valores entre 1 e matriz.length.
	 * @param origem 0 &le; origem &lt; matriz.length.
	 * @param destino 0 &le; destino &lt; matriz.length.
	 * @return peso da aresta.
	 */
	public static short aresta( short[][] matriz, short[] ordem, short origem, short destino ) {
		return matriz[ordem[origem]-1][ordem[destino]-1];
	}
	
	/**
	 * Grau de vértice, com restrição de destinos.
	 * @param matriz Matriz de adjacências.
	 * @param ordem Sequência numérica com valores entre 1 e matriz.length.
	 * @param vertice Índice do vértice em questão. 0 &le; vertice &lt; matriz.length.
	 * @param inicio 0 &le; inicio &lt; matriz.length.
	 * @param fim 0 &le; fim &lt; matriz.length.
	 * @return grau do vértice.
	 */
	public static short grau( short[][] matriz, short[] ordem, short vertice, short inicio, short fim ) {
		if( inicio < 0 ) inicio = 0;
		if( fim >= ordem.length ) fim = (short)( ordem.length - 1 );
		short grau = 0;
		for( short destino = inicio; destino <= fim; destino++ ){
			grau += matriz[ordem[vertice]-1][ordem[destino]-1] != 0 ? 1 : 0;
		}
		return grau;
	}
	
	/**
	 * Grau de um específico vértice de um grafo.
	 * @see #grau(short[][], short[], short, short, short)
	 */
	public static short grau( short[][] matriz, short[] ordem, short vertice ) {
		return grau( matriz, ordem, vertice, (short) 0, (short)( ordem.length - 1 ) );
	}
	
	/**
	 * Algoritmo de Floyd-Warshall.<br>
	 * Floyd, R. W. (1962). Algorithm 97: Shortest path. Communications of the ACM, 5(6):345-.
	 * @return matriz de distâncias, já com as colunas/linhas permutadas conforme a ordem especificada.
	 */
	public static short[][] matrizDistancias( short[][] matriz, short[] ordem ) {

		final short INFINITO = Short.MAX_VALUE;
		final int total = ordem.length;
		short[][] mdist = new short[total][total];
		short i, j, v, a, b;

		for( i = 0; i < total; i++ ){
			for( j = 0; j < total; j++ ){
				mdist[i][j] = i == j ? (short) 0 : matriz[ordem[i]-1][ordem[j]-1];
			}
		}
		
		for( v = 0; v < total; v++ ){
			for( i = 0; i < total; i++ ){
				for( j = 0; j < total; j++ ){
					a = mdist[i][v];
					b = mdist[v][j];
					a = a == 0 || b == 0 ? INFINITO : (short)( a + b );
					if( mdist[i][j] > a ){
						mdist[i][j] = a;
					}
				}
			}
		}
		
		return mdist;
		
	}
	
	/**
	 * Busca pelo maior valor dentro de uma submatriz quadrada.
	 * @param inicio Linha/Coluna inicial. 0 &le; inicio &lt; matriz.length.
	 * @param fim Linha/Coluna final. 0 &le; fim &lt; matriz.length.
	 */
	public static short maiorValorSubmatriz( short[][] matriz, short inicio, short fim ) {
		
		short maior = Short.MIN_VALUE;
		
		for( short i = inicio; i <= fim; i++ ){
			for( short j = inicio; j <= fim; j++ ){
				if( matriz[i][j] > maior ) maior = matriz[i][j];
			}
		}
		
		return maior;
		
	}
	
	/**
	 * Modularidade por Janela (Window Modularity).
	 */
	public static short[] calcularModularidadeJanela( short[][] matriz, short[] ordem, short janela ) {
	
		int total = ordem.length;
		short[] modularidade = new short[total];
		short[] grau = new short[total];
		int i, j, janelai, janelaf;
		double soma1, soma2;
		
		for( i = 0; i < total; i++ ){
			grau[i] = grau( matriz, ordem, (short) i );
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
			
			modularidade[pivo] = (short) Math.ceil( soma1 / soma2 * 100d );
			
		}
		
		return modularidade;
		
	}
	
	/**
	 * Modularidade por Janela Restrita (Restricted Window Modularity).
	 */
	public static short[] calcularModularidadeJanelaRestrita( short[][] matriz, short[] ordem, short janela ) {
	
		int total = ordem.length;
		short[] modularidade = new short[total];
		short[] grau = new short[total];
		int i, j, janelai, janelaf;
		short maior;
		
		for( i = 0; i < total; i++ ){
			grau[i] = grau( matriz, ordem, (short) i );
		}
		
		for( i = 0; i < total; i++ ){
			
			janelai = i - (int) Math.floor( janela / 2d );
			janelaf = janelai + janela - 1;
			if( janelai < 0 ) janelai = 0;
			if( janelaf >= total ) janelaf = total - 1;
			
			maior = 0;
			for( j = janelai; j <= janelaf; j++ ){
				if( grau[j] > maior ) maior = grau[j];
			}
			
			modularidade[i] = maior > 0 ? (short)( grau[i] * 100f / maior ) : 0;
			
		}
		
		return modularidade;
		
	}
	
	/**
	 * Modularidade por Densidade.
	 */
	public static short[] calcularModularidadeDensidade( short[][] matriz, short[] ordem, double densidadeMin ) {
		
		int total = ordem.length;
		short[] modularidade = new short[total];
		short i, j, raio, grau;
		double densidade;
		
		for( j = 0; j < total; j++ ){
			
			raio = (short) Math.max( j, total-j-1 );
			grau = 0;
			
			for( i = 0; i < total; i++ ){
				if( matriz[ordem[i]-1][ordem[j]-1] != 0 ) grau++;
			}
			
			while( raio > 0 ){
				
				densidade = grau / (double)( raio * 2 );
				if( densidade >= densidadeMin ) break;
				
				i = (short)( j - raio );
				if( i >= 0 && matriz[ordem[i]-1][ordem[j]-1] != 0 ) grau--;
				
				i = (short)( j + raio );
				if( i < total && matriz[ordem[i]-1][ordem[j]-1] != 0 ) grau--;
				
				raio--;
				
			}

			modularidade[j] = raio;
			
		}
		
		return modularidade;
		
	}
	
	/**
	 * Calcula as fronteiras modulares automaticamente.
	 * @param modularidade Veja {@link #calcularModularidadeJanela(short[][], short[], short)}.
	 * @param distanciaMinima Distância mínima entre dois picos.
	 * @param alturaMinima Altura mínima dos picos.
	 */
	public static short[] fronteiras( short[] modularidade, short distanciaMinima, short alturaMinima ) {
		
			short total = (short) modularidade.length;
		
		boolean[] pico = new boolean[total];
		Arrays.fill( pico, false );

		short topo = Short.MIN_VALUE;
		short x, y, z, distancia;
		
		for( short m : modularidade ){
				if( m > topo ) topo = m;
		}
		
		//Varredura descendente em busca de picos
		for( y = topo; y > 0; y-- ){
			for( x = 0; x < total; x++ ){
				if( modularidade[x] == y && ! pico[x] ){
					
					//Pico à direita
					distancia = 1;
					for( z = (short)( x + 1 ); z < total; z++ ){
						if( pico[z] ) break;
						distancia++;
					}
					if( distancia < distanciaMinima ) continue;
					
					//Pico à esquerda
					distancia = 1;
					for( z = (short)( x - 1 ); z >= 0; z-- ){
						if( pico[z] ) break;
						distancia++;
					}
					if( distancia < distanciaMinima ) continue;
					
					//Marcando pico
					pico[x] = true;
					
				}
			}
		}

		//Identificando os picos
		List<Short> picos = new ArrayList<Short>();
		for( x = 0; x < total; x++ ){
			if( pico[x] ) picos.add( x );
		}
		
		//Calculando as fronteiras
		List<Short> fronteiras = new ArrayList<Short>();
		boolean concluido = false;
		short vale;
		
		while( ! concluido ){
				
				fronteiras.clear();
				concluido = true;
				
			for( z = 0; z < (picos.size()-1); z++ ){
					
					x = (short)( picos.get( z	 ) + 1 );
					y = (short)( picos.get( z + 1 ) - 1 );
				
				distancia = Short.MAX_VALUE;
				vale = x;
				while( x <= y ){
					if( modularidade[x] < distancia ){
						distancia = modularidade[x];
						vale = x;
					}
					x++;
				}
				
				distancia = (short)( modularidade[picos.get( z )] - modularidade[vale] );
				if( distancia < alturaMinima ){
					picos.remove( z );
					concluido = false;
					break;
				}
				
				distancia = (short)( modularidade[picos.get( z + 1 )] - modularidade[vale] );
				if( distancia < alturaMinima ){
					picos.remove( z + 1 );
					concluido = false;
					break;
				}
				
				fronteiras.add( vale );
					
			}
			
		}
		
		short[] resultado = new short[ fronteiras.size() ];
		for( x = 0; x < resultado.length; x++ ){
			resultado[x] = fronteiras.get( x );
		}
		
		return resultado;
		
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
