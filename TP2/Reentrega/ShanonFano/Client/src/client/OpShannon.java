package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class OpShannon {
	
	
	public void iniciaShannon(int limiteInf, int limiteSup, List<SimboloShannon> simbolos) {
		if (limiteInf != limiteSup) {
			int i, j, corte = 0;
			float inf = 0, sup = 0, dif = 0, probDif = 0;
			for (i = limiteInf; i <= limiteSup; i++) {
				sup += simbolos.get(i).getProbabilidad();
				for (j = i + 1; j <= limiteSup; j++) {
					inf += simbolos.get(j).getProbabilidad();
				}
				dif = (sup - inf) * (sup - inf);
				if (inf == sup) {
					break;
				}
				if (i == limiteInf) {
					probDif = dif;
				} else {
					if (dif >= probDif) {
						i = i - 1;
						break;
					}
					probDif = dif;
				}
				inf = 0;
			}
			corte = i;
			this.shannonFano(limiteInf, limiteSup, corte, simbolos);
			iniciaShannon(limiteInf, i, simbolos);
			iniciaShannon(i + 1, limiteSup, simbolos);
		}

	}
	
	public void shannonFano(int limiteInf, int limiteSup, int corte, List<SimboloShannon> simbolos) {
		int i;
		for (i = limiteInf; i <= limiteSup; i++) {
			if (i <= corte)
				simbolos.get(i).setCodigoShannon(simbolos.get(i).getCodigoShannon() + "1");
			else
				simbolos.get(i).setCodigoShannon(simbolos.get(i).getCodigoShannon() + "0");
		}
	}
	

	public double getTamArchivo(int arch) throws IOException{
		Path ruta;
			ruta= Paths.get("tp1_grupo8.txt");

		return Files.size(ruta);
	}
	
	public double getTamComprimido(int arch) throws IOException {
		Path ruta;
			ruta= Paths.get("arch.Fan");
		return Files.size(ruta);
	}

	public void getTasaCompresion(double tamOrig,double tamComprimido,Escritura escribir) throws IOException {
		double n = tamOrig/tamComprimido;
		System.out.println("Tasa de compresión: "+n+":1");
		escribir.agregaResultado("Tasa de compresión: "+n+":1");
		escribir.cierraArchivo2();

	}


	public void muestraShannon(List<SimboloShannon> simbolos,Escritura escribir) {
		for (int i = 0; i < simbolos.size(); i++) {
			System.out.println(simbolos.get(i).toString());
			escribir.agregaResultado(simbolos.get(i).toString());			
		}
		escribir.agregaResultado("---------------------------------------------------------");
	}

	public void auxCompresionShannonFano(List<SimboloShannon> simbolos,int arch, Escritura escribir) throws IOException {
		String aux, auxLetra;
		int j;
		FileReader fr = null;
		BufferedReader br = null;
		int auxFile;
			fr = new FileReader("tp1_grupo8.txt");
			br = new BufferedReader(fr);
		while((auxFile=br.read())!=-1) {
			if(auxFile!=13) {
				if(arch!=3) {
				if(auxFile==10) 
					auxLetra="enter";
				else {
				char auxChar=(char) auxFile;
				auxLetra=String.valueOf(auxChar);
				}
				j=0;
				while (j < simbolos.size()-1 && !auxLetra.equals(simbolos.get(j).getSimbolo())) {
					j++;
				}
				escribir.agregaLinea(simbolos.get(j).getCodigoShannon());
				}
				else {
					if(auxFile!=10) {
						char auxChar=(char) auxFile;
						auxLetra=String.valueOf(auxChar);
						j=0;
						while (j < simbolos.size() && !auxLetra.equals(simbolos.get(j).getSimbolo())) {
							j++;
						}
						escribir.agregaLinea(simbolos.get(j).getCodigoShannon());
					}
				}
			}
		}
		escribir.cierraArchivo1();


	}
	
	
	/*  public static void crearCodificacion(List<SimboloShannon> simbolos, BufferedReader lector) throws IOException {
	        File codificacionFile;
	        char x;
	        int auxiliar=0;
	        int cant=0;
	        int k=1;

	        codificacionFile = new File("Arch.Fan");
	       
	       if(!codificacionFile.exists()){
	           codificacionFile.createNewFile();
	       }
	        
	        FileWriter fw = new FileWriter(codificacionFile);
	        
	        FileOutputStream fout = new FileOutputStream(codificacionFile);
	        
	        
	        String codShanonFano = "";
	        String simboloShanonFano = "";
	        
	        codShanonFano = "";

	        
	        while ((auxiliar!=-1)) {
	            String simbolo = "";
	            for (int i = 0; i < k; i++) {
	                if ((auxiliar = lector.read()) != -1) {
	                    x = (char) auxiliar;
	                    simbolo+=x;
	                }
	            }
	            
	            Iterator<SimboloShannon> iterador = simbolos.iterator();
	            while (iterador.hasNext()) {
	            	SimboloShannon simboloAux = iterador.next();
	            	if (simbolo.equals(simboloAux.getSimbolo())) {
	            		codShanonFano+=simboloAux.getCodigoShannon();
	            	}
	            }
	            
	           // for(Map.Entry<String,String> cod : codificacion.entrySet()){
	        //        if(simbolo.equals(cod.getKey())){
	       //             codShanonFano+=cod.getValue();
	                }
	       //     }   
	       // }
	        byte b=0;
	        int i=0;
	        byte c=0;
	        int dato=0;
	        int p=0;
	        
	       byte[] byte_array = new byte[(codShanonFano.length()/8)+1];
	        
	        Arrays.fill(byte_array, (byte) 0);
	        
	        System.out.println("COD COMPRIMIDO ENTERO: "+ codShanonFano);
	        
	        while (i<codShanonFano.length()){
	            b=0;
	            int j=0;
	            while (j<8 && i<codShanonFano.length()) {
	            	 c = (byte) ((codShanonFano.charAt(i)==49)?1:0);  //char 49 es == 1 int
	                 b = (byte) ((b << 1) | c); 
	                 i++;
	                 j++;	
	            }   
	            byte_array[p++]=b;
	            dato = b & 0xFF;   //mascara para considerar que es unsigned byte     
	         //   System.out.println(Integer.toBinaryString(dato) + " "+ (dato));
	           // System.out.println(b);
	        }
	        
	        fout.write(byte_array);

	        fout.close();
	        fw.close();     
	        } */
	
	
	 public void creaTablaCodificaciones(List<SimboloShannon> simbolos, Escritura escribir) throws IOException{
	    	String cantidadDeSimbolosBin = Integer.toBinaryString(simbolos.size());
			int agrego = (8 - cantidadDeSimbolosBin.length());
			String str = "";
			if (agrego > 0)
			{
				for(int i=0; i<agrego; i++)
				{
					str+="0";
				}
			}	
			else if(agrego < 0)
			{
				//Nunca va a ser mayor que 8, reservi siemppre 1 byte
			}
			cantidadDeSimbolosBin = str + cantidadDeSimbolosBin; //Longitud de 1 byte
				
			System.out.println("Cantidad de simbolos decimal: "+ simbolos.size());
			System.out.println("Cantidad de simbolos bin: "+ cantidadDeSimbolosBin);
			escribir.agregaLinea(cantidadDeSimbolosBin);
			int i=0;
			Iterator<SimboloShannon> iterador = simbolos.iterator();
			while (iterador.hasNext()) {
		//	for (Map.Entry<String, String> set : simbolos.entrySet()) {		
				SimboloShannon simboloAux= iterador.next();
				String cantidadDeSimbolosASCIBin = Integer.toBinaryString((int)simboloAux.getSimbolo().charAt(0));
				//String cantidadDeSimbolosASCIBin = Integer.toBinaryString((int)set.getKey().charAt(0));
				int agregoascii = (8 - cantidadDeSimbolosASCIBin.length());
				String strascii = "";
				if (agregoascii > 0)
				{
					for(int j=0; j<agregoascii; j++)
					{
						strascii+="0";
					}
				}
				else if(agregoascii < 0)
				{
					//nunca va a oasar
				}

				cantidadDeSimbolosASCIBin = strascii + cantidadDeSimbolosASCIBin;
				
				System.out.println("Cantidad de Simbolos ASCIBin: "+ cantidadDeSimbolosASCIBin);
				escribir.agregaLinea(cantidadDeSimbolosASCIBin); //Valor ascii
				
				
				int logitudCodigoHuffmanInt = (simboloAux.getCodigoShannon().length()); //Longitud del binario. Si el binario es 101010 -> 6
				//int logitudCodigoHuffmanInt = (set.getValue().length()); //Longitud del binario. Si el binario es 101010 -> 6
				String logitudCodigoHuffmanBin = Integer.toBinaryString(logitudCodigoHuffmanInt); // 6 -> en binario -> 110
				int logitudCodigoHuffmanBinLength = logitudCodigoHuffmanBin.length(); //longitud del 6 en binario -> 110 ->longitud -> 3
				//System.out.println("Codigo Fano: "+ simboloAux.getCodigoShannon() + " Longitud: "+ logitudCodigoHuffmanBin + " Length binaria: "+ logitudCodigoHuffmanBinLength);
				int cantBytesNecesarios = (int) Math.ceil( (double)logitudCodigoHuffmanInt/8 ); //Tengo la cantidad de bytes necesarios para representar el 6 -> en este caso: 1
				//System.out.println("cantBytesNecesarios: "+ cantBytesNecesarios);
				int agregoHuffman = (8 - logitudCodigoHuffmanBinLength); //Para representar el 6 en binario, necesito 3 bits. ENtonces, el resto lo llenare con 0
				String strHuff = "";
				if (agregoHuffman > 0)
				{
					for(int j=0; j<agregoHuffman; j++)
					{
						strHuff+="0";
					} 
				}
				else if(agregoHuffman < 0)
				{
					//nunca va a pasar
				}
				logitudCodigoHuffmanBin = strHuff + logitudCodigoHuffmanBin; //Longitud de 1 byte
				
				//EL CHAR EN ASCII REPRESENTADO EN 8 BITS
				
				
				System.out.println("Longitud codigo Shannon Fano Bin: "+ logitudCodigoHuffmanBin);
				escribir.agregaLinea(logitudCodigoHuffmanBin); //Valor ascii

				String codShanonFano = simboloAux.getCodigoShannon();
			//	String codHuffman = set.getValue();
				int agregarBits = ((cantBytesNecesarios * 8) - logitudCodigoHuffmanInt);
				String strBits = "";
				if (agregarBits > 0)
				{				
					for(int f = 0; f < agregarBits;f++)
					{
						strBits+="0";
					}				
				}
				else if(agregarBits < 0)
				{
						
				}
				codShanonFano = strBits + codShanonFano; //Longitud de 1 byte
				escribir.agregaLinea(codShanonFano);
				System.out.println("Codigo shannon fano: "+ codShanonFano);	
				i++;
			}
	    } 
	 
	
	
	public void compresionShannonFano(int arch) throws IOException {
		String binario="";
		byte bytes,wrByte;
		if (arch == 1) {
			FileInputStream in = new FileInputStream("Auxiliar.Fan");
			FileOutputStream out = new FileOutputStream("Arch.Fan");
			while((bytes=(byte) in.read())!=-1) {
				binario+=String.valueOf(bytes-48); // ya que el valor que voy a recibir es o un 1 o un 0, entonces resto por 48 por valor de tabla ASCII
				if(binario.length()==8) {
					wrByte=this.pasajeByte(binario); // paso el byte en string a byte real
					binario=""; // reinicio la cadena 
					out.write(wrByte); //escribo en el archivo
				}
			}
			if(!binario.isEmpty()) { //caso particular, cuando se acaba el archivo, si quedo un string no vacio, y menor a un byte, hay que completar con 0
			  while(binario.length()<8) {
				  binario+="0";
			  }
			  wrByte=this.pasajeByte(binario); // paso el byte en string a byte real
			out.write(wrByte);//escribo en el archivo
			}
			in.close();
			out.close();
		} 
	}
	
	public byte pasajeByte(String binario) {
		byte bin=0;
		int contador=(binario.length()-1);
        for(int i=0 ; i<binario.length() ; i++) {
            if(binario.charAt(contador)=='1')
                bin+=Math.pow(2, i);
            contador--;
        }
        return bin;
	}
	
	public double getEntropia(List<SimboloShannon> simbolos,Escritura escribir) {
		double auxEntropia=0,auxProb;
		for (int i=0;i<simbolos.size();i++) {
			auxProb=simbolos.get(i).getProbabilidad();
			auxEntropia+=auxProb*Math.log(1/auxProb)/Math.log(2);
		}
		escribir.agregaResultado("Entropia del código: "+auxEntropia);
		return auxEntropia;
	}
	
	public double getLongMedia(List<SimboloShannon> simbolos,Escritura escribir) {
		double auxLM=0;
		for (int i=0;i<simbolos.size();i++) {
			auxLM+=simbolos.get(i).getProbabilidad()*simbolos.get(i).getCodigoShannon().length();
		}
		escribir.agregaResultado("Longitud media del código: "+auxLM);
		return auxLM;
	}
	public void getRendimientoRedundancia(double entropia,double longMedia,Escritura escribir) {
		double rendimiento,redundancia;
		rendimiento=entropia/longMedia;
		redundancia = 1-rendimiento;
		System.out.println("Rendimiento del código: "+rendimiento);
		System.out.println("Redundancia del código: "+redundancia);
		escribir.agregaResultado("Rendimiento del código: "+rendimiento);
		escribir.agregaResultado("Redundancia del código: "+redundancia);
	}
	

}
