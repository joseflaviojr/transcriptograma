
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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Fronteiras modulares baseadas em DEG (Differentially Expressed Genes).
 * @author José Flávio de Souza Dias Júnior
 * @version 2016
 */
public class FronteirasDEG {
	
	private static final double VALOR_AUSENTE = Double.MAX_VALUE;

	public static void main( String[] args ) {
		
		if( args.length < 11 ){
			System.out.println( "Fronteiras modulares baseadas em DEG (Differentially Expressed Genes)." );
			System.out.println( FronteirasDEG.class.getSimpleName() + ".sh <seriacao> <tabela_deg> <cabecalho> <coluna_gene> <coluna_valor> <absoluto> <classificacao> <tamanho_min> <limiar ou quantidade> <s_fronteiras> <s_modulos>" );
			System.out.println();
			System.out.println( "<seriacao>      : Arquivo com genes ordenados sequencialmente." );
			System.out.println( "<tabela_deg>    : Arquivo com resultado de ranking de genes. Ver DEG.sh" );
			System.out.println( "<cabecalho>     : A <tabela_deg> contém cabeçalho? S ou N" );
			System.out.println( "<coluna_gene>   : Coluna da <tabela_deg> que contém os nomes dos genes. Ex.: 1 (a partir de 1)" );
			System.out.println( "<coluna_valor>  : Coluna da <tabela_deg> que contém os valores de ranking dos genes. Ex.: 2" );
			System.out.println( "<absoluto>      : Considerar todos os valores como absolutos (desconsiderar o sinal)? S ou N" );
			System.out.println( "<classificacao> : Classificação dos valores: \"ASC\" (o menor é melhor) ou \"DESC\" (o maior é melhor)." );
			System.out.println( "<tamanho_min>   : Tamanho mínimo para os módulos resultantes." );
			System.out.println( "<limiar>        : Genes com valores de ranking além do limiar (número real) serão desconsiderados." );
			System.out.println( "<quantidade>    : Inteiro que determina a quantidade de genes a considerar - últimos do ranking." );
			System.out.println( "<s_fronteiras>  : Arquivo de saída que armazenará as posições de fronteira modular." );
			System.out.println( "<s_modulos>     : Arquivo de saída que armazenará informações sobre os módulos." );
			System.exit( 1 );
		}
		
		try{
			
			boolean limiteLimiar;
			try{
				Integer.parseInt( args[8] );
				limiteLimiar = false;
			}catch( NumberFormatException e ){
				limiteLimiar = true;
			}
			
			final File    arq_seriacao = new File( args[0] );
			final File    arq_tabela   = new File( args[1] );
			final boolean cabecalho    = args[2].toUpperCase().equals( "S" );
			final int     col_gene     = Integer.parseInt( args[3] );
			final int     col_valor    = Integer.parseInt( args[4] );
			final boolean absoluto     = args[5].toUpperCase().equals( "S" );
			final boolean ascendente   = args[6].toUpperCase().equals( "ASC" );
			final int     tamanho_min  = Integer.parseInt( args[7] );
			final double  limiar       = limiteLimiar ? Double.parseDouble( args[8] ) : 0;
			final int     quantidade   = ! limiteLimiar ? Integer.parseInt( args[8] ) : 0;
			final File    s_fronteiras = new File( args[9] );
			final File    s_modulos    = new File( args[10] );
			
			Map<String,Gene> mapa = new HashMap<String,Gene>( 1000 );
			
			BufferedReader entrada = new BufferedReader( new FileReader( arq_seriacao ) );
			try{
				String nome;
				int posicao = 1;
				while( ( nome = entrada.readLine() ) != null ){
					mapa.put( nome, new Gene( nome, posicao++ ) );
				}
			}finally{
				entrada.close();
			}
			
			entrada = new BufferedReader( new FileReader( arq_tabela ) );
			try{
				if( cabecalho ) entrada.readLine();
				String linha;
				String[] colunas;
				while( ( linha = entrada.readLine() ) != null ){
					colunas = linha.split( "\t" );
					Gene gene = mapa.get( colunas[col_gene-1] );
					if( gene == null ) continue;
					try{
						gene.valor = Double.parseDouble( colunas[col_valor-1] );
						if( absoluto ) gene.valor = Math.abs( gene.valor );
					}catch( NumberFormatException e ){
						gene.valor = VALOR_AUSENTE;
					}
				}
			}finally{
				entrada.close();
			}
			
			List<Gene> genes = new ArrayList<Gene>( mapa.values() );
			
			genes.sort( new Comparator<Gene>(){
				@Override
				public int compare( Gene a, Gene b ) {
					if( a.valor == b.valor ) return 0;
					int ordem = a.valor < b.valor ? -1 : 1;
					return ascendente ? - ordem : ordem;
				}
			} );
			
			List<Gene> fronteiras = new ArrayList<Gene>();
			int contagem = 0;
			
			for( Gene gene : genes ){
				
				if( gene.valor == VALOR_AUSENTE ) continue;
				
				if( limiteLimiar ){
					if( ascendente && gene.valor < limiar ) break;
					if( ! ascendente && gene.valor > limiar ) break;					
				}else{
					if( ++contagem >= quantidade ) break;
				}
				
				if( gene.posicao < tamanho_min ) continue;
				if( ( genes.size() - gene.posicao ) < tamanho_min ) continue;
				
				boolean incompativel = false;
				for( Gene fronteira : fronteiras ){
					if( Math.abs( gene.posicao - fronteira.posicao ) < tamanho_min ){
						incompativel = true;
						break;
					}
				}
				
				if( ! incompativel ) fronteiras.add( gene );
				
			}
			
			fronteiras.sort( new Comparator<Gene>(){
				@Override
				public int compare( Gene a, Gene b ) {
					return a.posicao - b.posicao;
				}
			} );
			
			FileWriter saida = new FileWriter( s_fronteiras );
			try{
				boolean primeiro = true;
				for( Gene fronteira : fronteiras ){
					if( primeiro ) primeiro = false;
					else saida.write( ' ' );
					saida.write( "" + fronteira.posicao );
				}
				saida.write( '\n' );
			}finally{
				saida.close();
			}
			
			genes.sort( new Comparator<Gene>(){
				@Override
				public int compare( Gene a, Gene b ) {
					return a.posicao - b.posicao;
				}
			} );
			
			if( fronteiras.get( fronteiras.size() - 1 ).posicao != genes.size() ){
				fronteiras.add( genes.get( genes.size() - 1 ) );
			}
			
			saida = new FileWriter( s_modulos );
			try{
				if( Locale.getDefault().getLanguage().equals( new Locale("pt").getLanguage() ) ){
					saida.write( "Modulo\tTamanho\tDEG-Media\tInicio\tFim\n" );
				}else{
					saida.write( "Module\tSize\tDEG-Average\tBegin\tEnd\n" );
				}
				int modulo = 1;
				int ini = 1;
				int fim;
				for( Gene fronteira : fronteiras ){
					fim = fronteira.posicao;
					int tamanho = fim - ini + 1;
					double media = media( genes, ini - 1, fim - 1 );
					saida.write( modulo + "\t" + tamanho + "\t" + media + "\t" + ini + "\t" + fim + "\n" );
					modulo++;
					ini = fim + 1;
				}
			}finally{
				saida.close();
			}
			
		}catch( Exception e ){
			e.printStackTrace();
		}
		
	}
	
	private static double media( List<Gene> genes, int inicio, int fim ) {
		double soma = 0;
		int total = 0;
		for( int i = inicio; i <= fim; i++ ){
			double valor = genes.get( i ).valor;
			if( valor != VALOR_AUSENTE ){
				soma += valor;
				total++;
			}
		}
		return total > 0 ? soma / total : 0;
	}
	
	private static class Gene {
		
		private String nome;
		
		private int posicao;
		
		private double valor = VALOR_AUSENTE;

		public Gene( String nome, int posicao ) {
			this.nome = nome;
			this.posicao = posicao;
		}
		
		@Override
		public String toString() {
			return nome;
		}
		
	}
	
}
