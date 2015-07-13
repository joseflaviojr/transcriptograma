
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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

/**
 * Gera gr�fico conforme s�rie de valores armazenado em arquivo.
 * @author Jos� Fl�vio de Souza Dias J�nior
 * @version 2015
 */
public class GerarGrafico {

	public static void main( String[] args ) {
		
		boolean argumentosOK = true;
		for( int i = 0; i < args.length; i++ ){
			if( args[i].length() == 0 ){
				argumentosOK = false;
				break;
			}
		}
		
		if( args.length < 5 || ! argumentosOK ){
			System.out.println( "Gera gr�fico conforme s�rie de valores armazenado em arquivo." );
			System.out.println( GerarGrafico.class.getSimpleName() + " <arquivo_serie_csv> <tipo:linha|area> <altura_pixels:100> <cor_rgb:ff0000> <nome_arquivo_imagem.png>" );
			System.exit( 1 );
		}
		
		try{
			
			File arquivo_serie  = new File( args[0] );
			File arquivo_imagem = new File( arquivo_serie.getParent(), args[4] );
			
			int[]   serie   = Ferramenta.carregarOrdem( arquivo_serie );
			boolean tipo    = args[1].toLowerCase().equals( "linha" );
			int     altura  = Integer.parseInt( args[2] );
			int     cor     = Integer.parseInt( args[3].toUpperCase(), 16 );
			
			int total = serie.length;
			BufferedImage imagem = new BufferedImage( total, altura, BufferedImage.TYPE_INT_RGB );
			Graphics2D g = imagem.createGraphics();
			
			int maior = 1;
			for( int i = 0; i < total; i++ ){
				if( serie[i] > maior ) maior = serie[i];
			}
			
			g.setColor( Color.WHITE );
			g.fillRect( 0, 0, total, altura );
			
			int x, y;
			for( int j = 0; j < total; j++ ){
				y = altura - (int) Math.ceil( (double) serie[j] / maior * altura );
				if( y >= altura ) continue;
				x = j;
				if( tipo ){
					imagem.setRGB( x, y, cor );
				}else{
					while( y < altura ){
						imagem.setRGB( x, y, cor );
						y++;
					}
				}
			}
			
			ImageIO.write( imagem, "PNG", arquivo_imagem );
			
		}catch( Exception e ){
			e.printStackTrace();
		}

	}

}
