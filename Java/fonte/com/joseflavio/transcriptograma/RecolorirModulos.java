
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
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

/**
 * Recolore módulos de acordo com uma paleta de cores.
 * @author José Flávio de Souza Dias Júnior
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
			System.out.println( "Recolore módulos de acordo com uma paleta de cores." );
			System.out.println( RecolorirModulos.class.getSimpleName() + ".sh <modularidade.png> <paleta.txt>" );
			System.out.println( "<modularidade.png> : Gráfico de modularidade previamente colorido." );
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
