
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
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

/**
 * Recolore m�dulos de acordo com uma paleta de cores.
 * @author Jos� Fl�vio de Souza Dias J�nior
 * @version 2015
 */
public class RecolorirModulos {

	public static void main( String[] args ) {
		
		boolean argumentosOK = true;
		for( int i = 0; i < args.length; i++ ){
			if( args[i].length() == 0 ){
				argumentosOK = false;
				break;
			}
		}
		
		if( args.length < 2 || ! argumentosOK ){
			System.out.println( "Recolore m�dulos de acordo com uma paleta de cores." );
			System.out.println( RecolorirModulos.class.getSimpleName() + ".sh <modularidade.png> <paleta.txt>" );
			System.out.println( "<modularidade.png> : Gr�fico de modularidade previamente colorido." );
			System.out.println( "<paleta.txt>       : Paleta de cores - cada linha no formato RRGGBB." );
			System.exit( 1 );
		}
		
		try{
			
			File arquivo_modularidade = new File( args[0] );
			File arquivo_paleta       = new File( args[1] );
			
			BufferedImage grafico = ImageIO.read( arquivo_modularidade );
			int           largura = grafico.getWidth();
			int           altura  = grafico.getHeight();
			
			List<Integer> cores = new ArrayList<Integer>();
			BufferedReader paleta = new BufferedReader( new FileReader( arquivo_paleta ) );
			try{
				String linha;
				while( ( linha = paleta.readLine() ) != null ){
					cores.add( Integer.parseInt( linha, 16 ) );
				}
			}finally{
				paleta.close();
			}
			
			final int corBranca = Color.WHITE.getRGB();
			int corIndice = 0;
			int corNova = cores.get( corIndice );
			int corVelha = grafico.getRGB( 0, altura - 1 );
			
			for( int x = 0; x < largura; x++ ){
				for( int y = 0; y < altura; y++ ){
					int cor = grafico.getRGB( x, y );
					if( cor == corBranca ) continue;
					if( cor != corVelha ){
						corIndice++;
						if( corIndice >= cores.size() ) corIndice = 0;
						corNova = cores.get( corIndice );
						corVelha = cor;
					}
					grafico.setRGB( x, y, corNova );
				}
			}
			
			String formato = arquivo_modularidade.getName().toLowerCase().endsWith( "jpg" ) ? "JPG" : "PNG";
			ImageIO.write( grafico, formato, arquivo_modularidade );
			
		}catch( Exception e ){
			e.printStackTrace();
		}

	}

}
