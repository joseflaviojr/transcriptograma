
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
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.nio.file.Files;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.lyncode.jtwig.JtwigModelMap;
import com.lyncode.jtwig.JtwigTemplate;
import com.lyncode.jtwig.configuration.JtwigConfiguration;
import com.lyncode.jtwig.exception.ResourceException;
import com.lyncode.jtwig.resource.JtwigResource;

/**
 * Gera gráfico com transcriptogramas e modularidades.
 * @author José Flávio de Souza Dias Júnior
 * @version 2015
 */
public class GraficoTranscriptograma {
	
	public static final String[] CORES = {
		"000000", "FF0000", "0000FF", "00FF00", "800080", "FFFF00", "008080", "800000",
		"808000", "FF00FF", "000080", "008000", "808080", "C0C0C0", "B8860B", "CD5C5C",
		"4682B4", "FF4500", "FF1493", "20B2AA", "BDB76B", "2F4F4F", "CD6090", "8B8B00",
		"551A8B", "90EE90", "FA8072", "FF6347", "20B2AA", "FF7F50", "483D8B", "696969",
		"4B0082", "00CED1", "191970", "FF8C00", "8FBC8F", "BC8F8F", "1E90FF", "DAA520",
		"48D1CC", "E9967A", "9ACD32", "DA70D6", "7CFC00", "C71585", "6B8E23", "DC143C",
		"B22222", "D2691E", "F0E68C", "00FFFF"
	};

	public static void main( String[] args ) {
		
		boolean argumentosOK = true;
		for( int i = 0; i < args.length; i++ ){
			if( args[i].length() == 0 ){
				argumentosOK = false;
				break;
			}
		}
		
		if( args.length < 5 || ! argumentosOK ){
			System.out.println( "Gera gráfico com transcriptogramas e modularidades." );
			System.out.println( GraficoTranscriptograma.class.getSimpleName() + " <mod_janela> <mod_densidade> <transcriptograma> <series> [<legenda>] <saida.svg>" );
			System.out.println( "<mod_janela>       : Arquivo gerado por ModularidadeJanela.sh" );
			System.out.println( "<mod_densidade>    : Arquivo gerado por ModularidadeDensidade.sh" );
			System.out.println( "<transcriptograma> : Arquivo gerado por Transcriptograma.sh" );
			System.out.println( "<series>           : \"1,2,3-7,...\" ou \"0\" para todas" );
			System.out.println( "<legenda>          : Arquivo do qual as linhas definem a legenda" );
			System.out.println( "<saida.svg>        : Saída no formato Scalable Vector Graphics" );
			System.exit( 1 );
		}
		
		try{
			
			//---------------------------------------
			
			File   arquivo_mod_janela       = new File( args[0] );
			File   arquivo_mod_densidade    = new File( args[1] );
			File   arquivo_transcriptograma = new File( args[2] );
			String series_param             =           args[3]  ;
			File   arquivo_legenda          = null;
			File   arquivo_svg              = null;
			
			if( args.length > 5 ){
				arquivo_legenda = new File( args[4] );
				arquivo_svg     = new File( args[5] );
			}else{
				arquivo_svg     = new File( args[4] );
			}
			
			short[] mod_jan = Ferramenta.carregarOrdem( arquivo_mod_janela );
			short[] mod_den = Ferramenta.carregarOrdem( arquivo_mod_densidade );
			
			List<String> legendas = arquivo_legenda != null ? Files.readAllLines( arquivo_legenda.toPath() ) : null;
			
			List<Serie> series = new ArrayList<Serie>();
			
			float maiorValor = Float.MIN_VALUE;
			float menorValor = Float.MAX_VALUE;
			
			NumberFormat nfingles = NumberFormat.getNumberInstance( Locale.ENGLISH );
			nfingles.setGroupingUsed( false );
			
			NumberFormat nfrotulo = NumberFormat.getNumberInstance( Locale.ENGLISH );
			nfrotulo.setGroupingUsed( false );
			nfrotulo.setMaximumFractionDigits( 2 );
			
			//---------------------------------------
			
			JtwigModelMap jmm = new JtwigModelMap();
			
			Locale local = Locale.getDefault();
			
			if( local.getLanguage().equals( new Locale( "pt" ) ) ){
				jmm.add( "titulo", "Transcriptogramas" );
				jmm.add( "rotulov", "Nível de expressão" );
				jmm.add( "rotuloh", "Genes ordenados" );
				jmm.add( "transcriptograma", "Transcriptograma" );
				jmm.add( "modjanela", "Modularidade por Janela" );
				jmm.add( "moddensidade", "Modularidade por Densidade" );
			}else if( local.getLanguage().equals( new Locale( "fr" ) ) ){
				jmm.add( "titulo", "Transcriptograms" );
				jmm.add( "rotulov", "Niveau d'expression" );
				jmm.add( "rotuloh", "Gènes triés" );
				jmm.add( "transcriptograma", "Transcriptogram" );
				jmm.add( "modjanela", "Modularité Fenêtre" );
				jmm.add( "moddensidade", "Modularité Densité" );
			}else{
				jmm.add( "titulo", "Transcriptograms" );
				jmm.add( "rotulov", "Expression level" );
				jmm.add( "rotuloh", "Ordered genes" );
				jmm.add( "transcriptograma", "Transcriptogram" );
				jmm.add( "modjanela", "Window Modularity" );
				jmm.add( "moddensidade", "Density Modularity" );
			}
			
			//---------------------------------------
			
			BufferedReader entrada = new BufferedReader( new FileReader( arquivo_transcriptograma ) );
			
			try{
				
				String linha;
				String[] colunas = entrada.readLine().split( "\t" );
				
				for( int i = 1; i < colunas.length; i++ ){
					String legenda =
							legendas != null && i <= legendas.size() ?
							legendas.get(i-1) :
							jmm.get( "transcriptograma" ).toString() + " " + i;
					Serie s = new Serie( i, legenda );
					float v = nfingles.parse( colunas[i] ).floatValue();
					if( v > maiorValor ) maiorValor = v;
					if( v < menorValor ) menorValor = v;
					s.valores.add( v );
					series.add( s );
				}
				
				while( ( linha = entrada.readLine() ) != null ){
					colunas = linha.split( "\t" );
					for( int i = 1; i < colunas.length; i++ ){
						float v = nfingles.parse( colunas[i] ).floatValue();
						if( v > maiorValor ) maiorValor = v;
						if( v < menorValor ) menorValor = v;
						series.get( i-1 ).valores.add( v );
					}
				}
				
			}finally{
				entrada.close();
			}
			
			//---------------------------------------
			
			if( ! series_param.equals( "0" ) ){
				List<Serie> series_sel = new ArrayList<Serie>();
				for( String s : series_param.split( "," ) ){
					if( s.contains( "-" ) ){
						String[] parte = s.split( "-" );
						int inicio = Integer.parseInt( parte[0].trim() ) - 1;
						int fim    = Integer.parseInt( parte[1].trim() ) - 1;
						for( int i = inicio; i <= fim; i++ ){
							series_sel.add( series.get( i ) );
						}
					}else{
						Serie serie = series.get( Integer.parseInt( s.trim() ) - 1 );
						series_sel.add( serie );
					}
				}
				series = series_sel;
			}
			
			int cor = 0;
			for( Serie serie : series ){
				serie.cor = cor < CORES.length ? CORES[cor++] : "000000";
			}

			//---------------------------------------
			
			int total = mod_den.length;

			int maiorModJan = Integer.MIN_VALUE;
			int menorModJan = Integer.MAX_VALUE;
			int maiorModDen = Integer.MIN_VALUE;
			int menorModDen = Integer.MAX_VALUE;
			
			for( int i = 0; i < total; i++ ){
				if( mod_jan[i] > maiorModJan ) maiorModJan = mod_jan[i];
				if( mod_jan[i] < menorModJan ) menorModJan = mod_jan[i];
				if( mod_den[i] > maiorModDen ) maiorModDen = mod_den[i];
				if( mod_den[i] < menorModDen ) menorModDen = mod_den[i];
			}
			
			float origx =  70f;
			float origy = 823f;
			float horix = 970f;
			float verty = 323f;
			float horic = horix - origx;
			float vertc = origy - verty;
			
			//---------------------------------------
			
			JtwigConfiguration configuracao = new JtwigConfiguration();
			configuracao.render().strictMode( false );
			
			JtwigTemplate modelo = new JtwigTemplate( new JtwigResource() {
				public InputStream retrieve() throws ResourceException {
					try{
						return GraficoTranscriptograma.class.getResourceAsStream( "/transcriptograma.twig.svg" );
					}catch( Exception e ){
						throw new ResourceException( e );
					}
				}
				public JtwigResource resolve( String relativePath ) throws ResourceException {
					return null;
				}
				public boolean exists() {
					return true;
				}
			}, configuracao );

			//---------------------------------------
			
			jmm.add( "nivel1", nfrotulo.format( menorValor ) );
			jmm.add( "nivel2", nfrotulo.format( ( menorValor + maiorValor ) / 2f ) );
			jmm.add( "nivel3", nfrotulo.format( maiorValor ) );
			
			jmm.add( "totalgenes", total );
			
			//---------------------------------------
			
			StringBuffer sb = new StringBuffer( 1024 );
			
			sb.append( " " + nfingles.format( origx ) );
			sb.append( "," + nfingles.format( origy ) );
			
			for( int i = 0; i < total; i++ ){
				int mod = mod_jan[i];
				sb.append( " " + nfingles.format( origx + i * horic / total ) );
				sb.append( "," + nfingles.format( origy - mod / (float) maiorModJan * vertc ) );
			}
			
			sb.append( " " + nfingles.format( horix ) );
			sb.append( "," + nfingles.format( origy ) );
			
			jmm.add( "janela", sb.toString() );
			
			//---------------------------------------
			
			sb.delete( 0, sb.length() );
			
			sb.append( " " + nfingles.format( origx ) );
			sb.append( "," + nfingles.format( origy ) );
			
			for( int i = 0; i < total; i++ ){
				int mod = mod_den[i];
				sb.append( " " + nfingles.format( origx + i * horic / total ) );
				sb.append( "," + nfingles.format( origy - mod / (float) maiorModDen * vertc ) );
			}
			
			sb.append( " " + nfingles.format( horix ) );
			sb.append( "," + nfingles.format( origy ) );
			
			jmm.add( "densidade", sb.toString() );
			
			//---------------------------------------
			
			for( Serie serie : series ){

				sb.delete( 0, sb.length() );
				
				for( int i = 0; i < total; i++ ){
					float v = serie.valores.get( i );
					sb.append( " " + nfingles.format( origx + i * horic / total ) );
					sb.append( "," + nfingles.format( origy - v / maiorValor * vertc ) );
				}
				
				serie.pontos = sb.toString();
				
			}
			
			jmm.add( "series", series );
			
			//---------------------------------------
			
			FileOutputStream saida = new FileOutputStream( arquivo_svg );
			modelo.output( saida, jmm );
			saida.close();
			
			//---------------------------------------
			
		}catch( Exception e ){
			e.printStackTrace();
		}

	}
	
	public static class Serie {
		
		private List<Float> valores = new ArrayList<Float>( 1000 );
		
		private int numero;
		
		private String legenda;
		
		private String cor;

		private String pontos;
		
		private Serie( int numero, String legenda ) {
			this.numero = numero;
			this.legenda = legenda;
		}
		
		public int getNumero() {
			return numero;
		}
		
		public String getLegenda() {
			return legenda;
		}
		
		public String getCor() {
			return cor;
		}

		public String getPontos() {
			return pontos;
		}
		
	}

}
