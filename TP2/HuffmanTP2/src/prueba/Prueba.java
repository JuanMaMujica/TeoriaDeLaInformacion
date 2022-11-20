package prueba;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Prueba {

	/**
	 * RECOMENDACIÃ“N, SI EL ENCODING POR DEFECTO NO ES UTF-8, APLICARLO PARA PODER
	 * MOSTRAR TANTO LOS CARACTERES ESPECIALES EN ESPAÃ‘OL COMO LOS CARACTERES EN
	 * CHINO WINDOW -> PREFERENCES -> GENERAL -> CONTENT TYPE -> PARARSE SOBRE TEXT
	 * -> DEBAJO EN DEFAULT ENCODING TIPEAR UTF-8 -> APLICAR Y CERRAR OTRA OPCION:
	 * ALT+ENTER Y DE APARECER OTRO TIPO DE ENCODING, PINCHAR EN OTHER Y SELECCIONAR
	 * UTF-8
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
	
		comprimirArchivo_2(1);
		System.out.println("----------------------------------------------------------------------------------------------------------------------------");
		//comprimirArchivo_2(2);
		System.out.println("----------------------------------------------------------------------------------------------------------------------------");
		//comprimirArchivo_2(3);
	}

	public static void comprimirArchivo_2(int arch) throws IOException {
		FileReader fr = null;
		BufferedReader br = null;
		ArrayList<String> caracteres = new ArrayList();
		ArrayList<Frecuencia> frecuencia = new ArrayList();
		List<Simbolo> simbolos = new ArrayList<Simbolo>();
		int pos, auxFile;
		char auxChar;
		String aux;
		double cont = 0,entropia, longMedia, N;//,tamOrig=0;
		double contEnters=0,contRetorno=0;
		ListaSimple l = new ListaSimple();
		NodoLista listaOrig, listaArbol;
		Huffman h = new Huffman();
		Escritura escribir = null;
		if (arch == 1) {
			fr = new FileReader("tp1_grupo8.txt");
			br = new BufferedReader(fr);
			escribir = new Escritura(arch);
		}else if (arch==2) {
			fr = new FileReader("Chino.txt");
			br = new BufferedReader(fr);
			escribir = new Escritura(arch);
		}else {
			fr = new FileReader("imagen.raw");
			br = new BufferedReader(fr);
			escribir = new Escritura(arch);
			br.readLine();
			br.readLine();
		}
		
		
///////////RECORRO EL ARCHIVO--------------------------
		while ((auxFile = br.read()) != -1) {
			if(auxFile!=13) 
			{// SE OMITE EL DENOMINADO RETORNO DE CARRO YA QUE NO ES NECESARIO 
				if(arch!=3) 
				{
					
					auxChar = (char) auxFile;
					aux = String.valueOf(auxChar); //Valor del char que estoy leyendo
					if((int)auxFile != 771 && (int)auxFile != 769 && (int)auxFile != 776 ) //lo puedo reemp por que no sea < a 255 pero dfesp veo
					{
						cont++;
						if (caracteres.contains(aux)) 
						{ //Si el array contiene el caracter, lo busco y con su posicion aumento la frecuencia
							pos = busqueda(caracteres, aux);
							frecuencia.get(pos).aumentaFrec();
						}
						else  //Caracteres tiene los caracteres distintos
						{
							//System.out.println(aux);
							//System.out.println((int)auxFile);
							caracteres.add(aux);    //Si no, lo agrego y le creo una frecuencia
							frecuencia.add(new Frecuencia());
						}	
					}
					
				}
				else 
				{
					if(auxFile!=10) 
					{  // PARA LA IMAGEN SE OMITE LOS ENTER O \N (NUEVA LINEA)
						cont++;
						auxChar = (char) auxFile;
						aux = String.valueOf(auxChar);
						if (caracteres.contains(aux)) 
						{
							pos = busqueda(caracteres, aux);
							frecuencia.get(pos).aumentaFrec();
						}

						else 
						{
							caracteres.add(aux);
							frecuencia.add(new Frecuencia());
						}
					}
					else contEnters++; // MAS ADELANTE SE EXPLICA SU USO
				}
			}
			else
				contRetorno++; // MAS ADELANTE SE EXPLICA SU USO
		}

		for (int i = 0; i < caracteres.size(); i++) {
			if (caracteres.get(i).equals(String.valueOf((char) 10)) && arch!=3) {
				simbolos.add(new Simbolo("enter", cont, frecuencia.get(i).getFrecuencia()));
			}
			else
			{
				//System.out.println("AGREGANDO: " + caracteres.get(i));
				simbolos.add(new Simbolo(caracteres.get(i), cont, frecuencia.get(i).getFrecuencia()));
			}
		}
	
		Collections.sort(simbolos); //oRDENA POR SIMBOLO Y PROB!
		
		//System.out.println("SIMBOLOS");

		//System.out.println(simbolos.get(0));
		
		//System.out.println("SIMBOLOS");

		listaArbol = l.generaLista(simbolos);
		h.getHuffman(listaArbol.getArbol(), "", simbolos);
		for (int i = 0; i < simbolos.size(); i++) {
			//System.out.println(simbolos.get(i).toString());
			escribir.agregaResultado(simbolos.get(i).toString()); // AQUI SE ESCRIBEN EN LOS ARCHIVOS DE RESULTADOS LA CODIFICACION EN HUFFMAN ADEMÃ�S DE OTROS DATOS
		} 
		h.auxCompresionHuffman(listaArbol, arch, escribir, simbolos); //Comprime tabla y archivo en si, en el primer archivo
		h.compresionHuffman(arch);

		entropia = h.getEntropia(simbolos, escribir);
		longMedia = h.getLongMedia(escribir);
		br.close();
		fr.close();

		System.out.println("Entropia del cÃ³digo: " + entropia);
		System.out.println("Longitud media del cÃ³digo: " + longMedia);
		h.getRendimientoRedundancia(entropia, longMedia, escribir);
		if(arch!=3)
			h.getTasaCompresion(h.getTamArchivo(arch),h.getTamComprimido(arch),escribir); 
		else
		h.getTasaCompresion((h.getTamArchivo(arch)-contRetorno-contEnters-6),(h.getTamComprimido(arch)) ,escribir);
		// PARA LAS IMAGENES HAY QUE RESTAR LOS ENTERS O \N QUE VAN ACOMPANADOS DE LOS RETORNO DE CARRO, LOS 6 QUE TAMBIEN SE RESTAN CORRESPONDEN A LOS PRIMERO 6 BYTES QUE NO SE UTILIZAN DE LA IMAGEN, OBTENIENDO ASI EL TAMANO REAL UTILIZADO DE ELLA
	
			
		h.decodeHuffman();
	}


	private static int busqueda(ArrayList caracteres, String carac) {
		int i = 0;
		while (i < caracteres.size() && !caracteres.get(i).equals(carac))
			i++;
		return i;
	}

}
