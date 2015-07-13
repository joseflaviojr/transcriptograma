
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

import java.io.File;

/**
 * Gera imagem de matriz de adjac�ncias conforme ordenamento informado.
 * @author Jos� Fl�vio de Souza Dias J�nior
 * @version 2015
 */
public class GerarImagem {

	public static void main( String[] args ) {
		
		if( args.length < 2 || args[0].length() == 0 || args[1].length() == 0 ){
			System.out.println( "Gera imagem de matriz de adjac�ncias conforme ordenamento informado." );
			System.out.println( GerarImagem.class.getSimpleName() + " <arquivo_matriz> <arquivo_ordem> [<nome_arquivo_imagem.png>]" );
			System.exit( 1 );
		}
		
		try{
			
			File arquivo_matriz  = new File( args[0] );
			File arquivo_ordem   = new File( args[1] );
			File arquivo_imagem  = new File( arquivo_ordem.getParent(), args.length > 2 && args[2].length() > 0 ? args[2] : arquivo_ordem.getName() + ".png" );
			
			int[][] matriz = Ferramenta.carregarMatriz( arquivo_matriz );
			int[] ordem = Ferramenta.carregarOrdem( arquivo_ordem );
			
			Ferramenta.salvarImagem( matriz, ordem, arquivo_imagem );
			
		}catch( Exception e ){
			e.printStackTrace();
		}

	}

}
