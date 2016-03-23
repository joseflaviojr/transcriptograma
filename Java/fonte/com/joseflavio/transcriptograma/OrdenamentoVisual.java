
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
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

/**
 * Demonstra visualmente um ordenamento de genes.
 * @author Jos� Fl�vio de Souza Dias J�nior
 * @version 2015
 */
public class OrdenamentoVisual {

	public static void main( String[] args ) {
		
		if( args.length < 4 ){
			System.out.println( "Demonstra visualmente um ordenamento de genes." );
			System.out.println( OrdenamentoVisual.class.getSimpleName() + " <genes.txt> <genes_cores.png> <genes_ordenados.txt> <resultado.png>" );
			System.exit( 1 );
		}
		
		try{
			
			File arq_genes = new File( args[0] );
			File arq_cores = new File( args[1] );
			File arq_ordem = new File( args[2] );
			File arq_resul = new File( args[3] );
			
			Map<String,Color> mapa_cor = new HashMap<String,Color>( 5000 );
			BufferedImage cores = ImageIO.read( arq_cores );
			int cores_y = cores.getHeight() - 1;
			
			BufferedReader entrada = new BufferedReader( new FileReader( arq_genes ) );
			try{
				String gene;
				int x = 0;
				while( ( gene = entrada.readLine() ) != null && gene.length() > 0 ){
					mapa_cor.put( gene, new Color( cores.getRGB( x, cores_y ) ) );
					x++;
				}
			}finally{
				entrada.close();
			}
			
			BufferedImage resultado = new BufferedImage( mapa_cor.size(), cores.getHeight(), BufferedImage.TYPE_INT_RGB );
			Graphics2D g = resultado.createGraphics();
			g.setColor( Color.WHITE );
			g.fillRect( 0, 0, resultado.getWidth(), resultado.getHeight() );
			
			entrada = new BufferedReader( new FileReader( arq_ordem ) );
			try{
				String gene;
				int x = 0;
				while( ( gene = entrada.readLine() ) != null && gene.length() > 0 ){
					g.setColor( mapa_cor.get( gene ) );
					g.drawLine( x, 0, x, cores_y );
					x++;
				}
			}finally{
				entrada.close();
			}
			
			ImageIO.write( resultado, "PNG", arq_resul );
			
		}catch( Exception e ){
			e.printStackTrace();
		}

	}
	
}
