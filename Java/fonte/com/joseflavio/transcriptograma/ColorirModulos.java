
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

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

/**
 * Colore m�dulos expressos graficamente.
 * @author Jos� Fl�vio de Souza Dias J�nior
 * @version 2015
 */
public class ColorirModulos {

	public static void main( String[] args ) {
		
		boolean argumentosOK = true;
		for( int i = 0; i < args.length; i++ ){
			if( args[i].isEmpty() ){
				argumentosOK = false;
				break;
			}
		}
		
		if( args.length < 2 || ! argumentosOK ){
			System.out.println( "Colore m�dulos expressos graficamente." );
			System.out.println( ColorirModulos.class.getSimpleName() + ".sh <grafico.png> <saida.png> [<fronteira1> <fronteira2> <fronteiraN>]" );
			System.out.println( "Ex1: ColorirModulos.sh Modularidade.png ModularidadeColorida.png 21 153 302" );
			System.out.println( "Ex2: Fronteiras.sh Modularidade.txt 50 6 | ColorirModulos.sh Modularidade.png ModularidadeColorida.png" );
			System.out.println( "<fronteiraN> : Coordenada X >= 1 que determina uma fronteira modular." );
			System.exit( 1 );
		}
		
		try{
			
			String entrada = null;
			if( args.length < 3 ){
				BufferedReader entradaBR = new BufferedReader( new InputStreamReader( System.in ) );
				while( true ){
					entrada = entradaBR.readLine();
					if( entrada == null || entrada.length() > 0 ) break;
					entrada = null;
				}
			}
			
			File arquivo_grafico = new File( args[0] );
			File arquivo_saida   = new File( args[1] );
			
			BufferedImage grafico = ImageIO.read( arquivo_grafico );
			int           largura = grafico.getWidth();
			int           altura  = grafico.getHeight();
			
			int[] fronteiras;
			if( entrada != null ){
				String[] n = entrada.trim().split( " " );
				fronteiras = new int[ n.length + 1 ];
				for( int i = 0; i < n.length; i++ ){
					fronteiras[i] = Integer.parseInt( n[i] ) - 1;
				}
			}else{
				fronteiras = new int[ args.length - 2 + 1 ];
				for( int i = 0; i < (fronteiras.length-1); i++ ){
					fronteiras[i] = Integer.parseInt( args[i+2] ) - 1;
				}
			}
			fronteiras[fronteiras.length-1] = largura - 1;

			int ultima  = -1;
			int cor_pos = 0;
			int branco  = Color.WHITE.getRGB();
			
			for( int fronteira : fronteiras ){
				
				int cor = Integer.parseInt( GraficoTranscriptograma.CORES[cor_pos], 16 );
				if( ++cor_pos >= GraficoTranscriptograma.CORES.length ) cor_pos = 0;
				
				for( int x = ultima + 1; x <= fronteira && x < largura; x++ ){
					for( int y = 0; y < altura; y++ ){
						if( grafico.getRGB( x, y ) != branco ){
							grafico.setRGB( x, y, cor );	
						}
					}
				}
				
				ultima = fronteira;
				
			}
			
			ImageIO.write( grafico, "PNG", arquivo_saida );
			
		}catch( Exception e ){
			e.printStackTrace();
		}

	}

}
