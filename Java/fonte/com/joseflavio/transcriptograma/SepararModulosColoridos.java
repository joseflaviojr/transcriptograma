
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

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.NumberFormat;

import javax.imageio.ImageIO;

/**
 * Constroi listas de genes conforme transcriptograma com modulos coloridos.
 * @author José Flávio de Souza Dias Júnior
 * @version 2015
 */
public class SepararModulosColoridos {

	public static void main( String[] args ) {
		
		if( args.length < 2 ){
			System.out.println( "Constroi listas de genes conforme transcriptograma com modulos coloridos." );
			System.out.println( SepararModulosColoridos.class.getSimpleName() + " <genes.txt> <cores.png> [<prefixo_saida>]" );
			System.exit( 1 );
		}
		
		try{
			
			File arq_genes = new File( args[0] );
			File arq_cores = new File( args[1] );
			String prefixo = args.length > 2 ? args[2] : null;
			
			if( prefixo == null ){
				prefixo = arq_cores.getName();
				prefixo = prefixo.substring( 0, prefixo.lastIndexOf( '.' ) ) + "_";	
			}
			
			BufferedImage cores = ImageIO.read( arq_cores );
			int x = 0, y = cores.getHeight() - 1;

			int modulo = 1;
			NumberFormat nf = NumberFormat.getIntegerInstance();
			nf.setMinimumIntegerDigits( 3 );
			
			FileWriter saida = new FileWriter( new File( arq_cores.getParentFile(), prefixo + nf.format( modulo++ ) + ".txt" ) );
			
			BufferedReader genes = new BufferedReader( new FileReader( arq_genes ) );
			try{
				
				String gene = genes.readLine();
				int cor = cores.getRGB( x++, y );
				
				saida.write( gene + "\n" );
				
				int cor_nova;
				while( ( gene = genes.readLine() ) != null && gene.length() > 0 ){
					cor_nova = cores.getRGB( x++, y );
					if( cor_nova != cor ){
						cor = cor_nova;
						saida.close();
						saida = new FileWriter( new File( arq_cores.getParentFile(), prefixo + nf.format( modulo++ ) + ".txt" ) );
					}
					saida.write( gene + "\n" );
				}
				
			}finally{
				genes.close();
				saida.close();
			}
			
		}catch( Exception e ){
			e.printStackTrace();
		}

	}
	
}
