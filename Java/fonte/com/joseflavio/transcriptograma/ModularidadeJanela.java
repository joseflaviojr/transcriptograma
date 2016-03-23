
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

import java.io.File;

/**
 * Calcula a modularidade por janela de uma rede ordenada.
 * @author Jos� Fl�vio de Souza Dias J�nior
 * @version 2015
 */
public class ModularidadeJanela {

	public static void main( String[] args ) {
		
		if( args.length < 3 ){
			System.out.println( "Calcula a modularidade por janela de uma rede ordenada." );
			System.out.println( ModularidadeJanela.class.getSimpleName() + " <arquivo_matriz> <arquivo_ordem> <janela:251|300|...>" );
			System.exit( 1 );
		}
		
		try{
			
			File arquivo_matriz = new File( args[0] );
			File arquivo_ordem  = new File( args[1] );
			
			short[][] matriz  = Ferramenta.carregarMatriz( arquivo_matriz );
			short[]   ordem   = Ferramenta.carregarOrdem( arquivo_ordem );
			short     janela  = Short.parseShort( args[2] );
			
			short[] modularidade = Ferramenta.calcularModularidadeJanela( matriz, ordem, janela );
			System.out.print( modularidade[0] );
			for( int i = 1; i < modularidade.length; i++ ) System.out.print( "," + modularidade[i] );	
			System.out.flush();
			
		}catch( Exception e ){
			e.printStackTrace();
		}

	}

}
