package client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Prueba {
	
	public static void main(String[] args) throws IOException {
		comprimirArchivo_2(1);
		System.out.println("-----------------------------------------------------------------------------------");

	
	}
	
	public static void comprimirArchivo_2(int arch) throws IOException{
		ArrayList<String> caracteres = new ArrayList();
		ArrayList<Frecuencia> frecuencia = new ArrayList();
		List<SimboloShannon> simbolos = new ArrayList<SimboloShannon>();
		Escritura escribir;
		OpShannon o = new OpShannon(); // aca se invocan los metodos para obtener shannon
		int pos,auxFile;
		char auxChar;
		double cont = 0, entropia, longMedia;
		double contRetorno=0,contEnters=0;
		String aux;
		FileReader fr = null;
		BufferedReader br = null;
			fr = new FileReader("tp1_grupo8.txt");
			br = new BufferedReader(fr);
			escribir = new Escritura(arch);
		while ((auxFile = br.read()) != -1) {
			if(auxFile!=13) { 
				if(arch!=3) {
			cont++;
			auxChar = (char) auxFile;
			aux = String.valueOf(auxChar);
			
			if((int)auxFile < 255 &&  (int)auxFile != 771 && (int)auxFile != 769 && (int)auxFile != 776) //lo puedo reemp por que no sea < a 255 pero dfesp veo
			{
				if (caracteres.contains(aux)) {
					pos = busqueda(caracteres, aux);
					frecuencia.get(pos).aumentaFrec();
				}
	
				else {
					caracteres.add(aux);
					frecuencia.add(new Frecuencia());
				}
			}
		 }
			else {
				if(auxFile!=10) { 
					cont++;
					auxChar = (char) auxFile;
					aux = String.valueOf(auxChar);
					if (caracteres.contains(aux)) {
						pos = busqueda(caracteres, aux);
						frecuencia.get(pos).aumentaFrec();
					}

					else {
						caracteres.add(aux);
						frecuencia.add(new Frecuencia());
					}
					
				}
				else 
					contEnters++; 
			}
		}
			else
				contRetorno++; 
		}

		for (int i = 0; i < caracteres.size(); i++) {
			if (caracteres.get(i).equals(String.valueOf((char) 10)) && arch!=3) {
				simbolos.add(new SimboloShannon("enter", cont, frecuencia.get(i).getFrecuencia()));
			}
			else
				simbolos.add(new SimboloShannon(caracteres.get(i), cont, frecuencia.get(i).getFrecuencia()));
		}
		Collections.sort(simbolos);
		o.iniciaShannon(0, simbolos.size()-1, simbolos);
		o.muestraShannon(simbolos, escribir);
		o.creaTablaCodificaciones(simbolos,escribir);
		o.auxCompresionShannonFano(simbolos, arch, escribir);
		o.compresionShannonFano(arch);
		entropia = o.getEntropia(simbolos, escribir);
		longMedia = o.getLongMedia(simbolos, escribir);
		System.out.println("Entropia del cÃ³digo: " + entropia);
		System.out.println("Longitud media del cÃ³digo: " + longMedia);
		o.getRendimientoRedundancia(entropia, longMedia, escribir);

			o.getTasaCompresion(o.getTamArchivo(arch),o.getTamComprimido(arch),escribir);

			
		
		System.out.println("Caracteres: "+ Arrays.asList(caracteres));
		System.out.println("Frecuencias: "+ Arrays.asList(frecuencia));
		System.out.println("Simbolos: "+ Arrays.asList(simbolos));
		Decodificacion.decodifica();
	}

	
	private static int busqueda(ArrayList caracteres, String carac) {
		int i = 0;
		while (i < caracteres.size() && !caracteres.get(i).equals(carac))
			i++;
		return i;
	}
}
