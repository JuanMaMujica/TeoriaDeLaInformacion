package huffmantp2;

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


	public static void main(String[] args) throws IOException {
	
		comprimirArchivo_2(1);
		System.out.println("----------------------------------------------------------------------------------------------------------------------------");
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
		}
		
		
///////////RECORRO EL ARCHIVO--------------------------
                int a = (int) h.getTamArchivo(1);
		while ((auxFile = br.read()) != -1) {
			if(auxFile!=13) 
			{// SE OMITE EL DENOMINADO RETORNO DE CARRO YA QUE NO ES NECESARIO 
				if(arch!=3) 
				{
					a++;
					auxChar = (char) auxFile;

					aux = String.valueOf(auxChar); //Valor del char que estoy leyendo
					if((int)auxFile < 255 &&  (int)auxFile != 771 && (int)auxFile != 769 && (int)auxFile != 776 ) //lo puedo reemp por que no sea < a 255 pero dfesp veo
					{
						cont++;
						if (caracteres.contains(aux)) 
						{ //Si el array contiene el caracter, lo busco y con su posicion aumento la frecuencia
							pos = busqueda(caracteres, aux);
							frecuencia.get(pos).aumentaFrec();
						}
						else  //Caracteres tiene los caracteres distintos
						{

							caracteres.add(aux);    //Si no, lo agrego y le creo una frecuencia
							frecuencia.add(new Frecuencia());
						}	
					}
					
			 	}
			 	else 
				{
					if(auxFile!=10) 
					{  
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
					else contEnters++; 
				}
			}
			else
				contRetorno++; 
		}

		for (int i = 0; i < caracteres.size(); i++) {
			if (caracteres.get(i).equals(String.valueOf((char) 10)) && arch!=3) {
				simbolos.add(new Simbolo("enter", cont, frecuencia.get(i).getFrecuencia()));
			}
			else
			{
				simbolos.add(new Simbolo(caracteres.get(i), cont, frecuencia.get(i).getFrecuencia()));
			}
		}
	
		Collections.sort(simbolos); //oRDENA POR SIMBOLO Y PROB!
		

		listaArbol = l.generaLista(simbolos);
		h.getHuffman(listaArbol.getArbol(), "", simbolos);
		for (int i = 0; i < simbolos.size(); i++) {
			escribir.agregaResultado(simbolos.get(i).toString()); // AQUI SE ESCRIBEN EN LOS ARCHIVOS DE RESULTADOS LA CODIFICACION EN HUFFMAN ADEM??S DE OTROS DATOS
		} 
		h.auxCompresionHuffman(listaArbol, arch, escribir, simbolos); //Comprime tabla y archivo en si, en el primer archivo
		h.compresionHuffman(arch);

		entropia = h.getEntropia(simbolos, escribir);
		longMedia = h.getLongMedia(escribir);
		br.close();
		fr.close();

		h.getRendimientoRedundancia(entropia, longMedia, escribir);
		if(arch!=3)
			h.getTasaCompresion(h.getTamArchivo(arch),h.getTamComprimido(arch),escribir); 
		else
			h.getTasaCompresion((h.getTamArchivo(arch)-contRetorno-contEnters-6),(h.getTamComprimido(arch)) ,escribir);
	
		List<Simbolo> simbolosDecode = new ArrayList<Simbolo>();	
		h.decodeHuffman(simbolosDecode); //DECODIFICACION!
	}


	private static int busqueda(ArrayList caracteres, String carac) {
		int i = 0;
		while (i < caracteres.size() && !caracteres.get(i).equals(carac))
			i++;
		return i;
	}

}
