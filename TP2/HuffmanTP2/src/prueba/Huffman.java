package prueba;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**CLASE EN DONDE SE VAN A EJECUTAR LOS METODOS PRINCIPALES PARA HUFFMAN
 * @author usuario
 *
 */
public class Huffman {

	private ArrayList<NodoArbol> auxLongitudes = new ArrayList();
	private File decodeArchHuffman;
	private FileWriter w2Decode;
	private BufferedWriter bw2Decode;
	private PrintWriter pwDecode;

	/** ACA SE CODIFICA A LOS SIMBOLOS
	 * @param arbol
	 * @param binario
	 * @param simbolos
	 */
	public void getHuffman(NodoArbol arbol, String binario, List<Simbolo> simbolos) {
		String auxBinario = "";
		if (arbol != null) {
			if (arbol.getPadre() != null) {
				auxBinario = arbol.getPadre().getCodigoBinario() + binario; //Va concatenando los 1 y 0
				arbol.setCodigoBinario(auxBinario);   //Al nodo donde estoy parado, le asigna el binario y se lo guarda. (Si es que no es raiz.)
			}
			this.getHuffman(arbol.getIzq(), "0", simbolos); //Derecha asigna cero
			if (arbol.getSimbolo().length() == 1) {
				this.asignaCodigo(arbol.getSimbolo(), arbol.getCodigoBinario(), simbolos);
				this.addLongitud(arbol);
			} else if (arbol.getSimbolo().equals("enter")) {
				this.asignaCodigo(arbol.getSimbolo(), arbol.getCodigoBinario(), simbolos);
				this.addLongitud(arbol);
			}
			this.getHuffman(arbol.getDer(), "1", simbolos); //Izq asigna 1
		}

	}

	/**METODO QUE UTILIZA UNA COLECCION AUXILIAR PARA PODER IMPRIMIR EN ARCHIVOS Y/O PANTALLA LAS CODIFICACIONES DE HUFFMAN Y DE SHANNON FANO EN EL MISMO ORDEN INDEPENDIENTEMENTE DE SU CODIFICACION
	 * @param simbolo
	 * @param codHuffman
	 * @param simbolos COLECCION DE SIMBOLOS
	 */
	public void asignaCodigo(String simbolo, String codHuffman, List<Simbolo> simbolos) {
		int i = 0;
		while (i < simbolos.size() && !simbolo.equals(simbolos.get(i).getSimbolo()))
			i++;
		simbolos.get(i).setCodigoHuffman(codHuffman);
	}

	/**METODO EN DONDE SE ALMACENAN LAS LONGITUDES DE LOS CODIGOS EN HUFFMAN PARA FACILITAR LOS CALCULOS DE LONGITUD MEDIA
	 * @param arbol
	 */
	public void addLongitud(NodoArbol arbol) {
		this.auxLongitudes.add(arbol);
	}

	public double getEntropia(List<Simbolo> simbolos, Escritura escribir) {
		double auxEntropia = 0, auxProb;
		for (int i = 0; i < simbolos.size(); i++) {
			auxProb = simbolos.get(i).getProbabilidad();
			auxEntropia += auxProb * Math.log(1 / auxProb) / Math.log(2);
		}
		escribir.agregaResultado("Entropia del código: " + auxEntropia);
		return auxEntropia;

	}

	public void getTasaCompresion(double tamOrig, double tamComprimido, Escritura escribir) throws IOException {
		double n = tamOrig / tamComprimido;
		System.out.println("Tasa de compresión: " + n + ":1");
		escribir.agregaResultado("Tasa de compresión: " + n + ":1");
		escribir.cierraArchivo2();

	}

	public double getLongMedia(Escritura escribir) {
		double auxLM = 0;
		for (int i = 0; i < this.auxLongitudes.size(); i++) {
			auxLM += this.auxLongitudes.get(i).getProb() * this.auxLongitudes.get(i).getCodigoBinario().length();
		}
		escribir.agregaResultado("Longitud media del código: " + auxLM);
		return auxLM;
	}

	public void getRendimientoRedundancia(double entropia, double longMedia, Escritura escribir) {
		double rendimiento, redundancia;
		rendimiento = entropia / longMedia;
		redundancia = 1 - rendimiento;
		System.out.println("Rendimiento del código: " + rendimiento);
		System.out.println("Redundancia del código: " + redundancia);
		escribir.agregaResultado("Rendimiento del código: " + rendimiento);
		escribir.agregaResultado("Redundancia del código: " + redundancia);
	}

	
	/** ACA SE REALIZA LA CODIFICACION POR HUFFMAN A UN .Huf
	 * @param listaArbol
	 * @param arch
	 * @param escribir
	 * @throws IOException
	 */
	public void auxCompresionHuffman(NodoLista listaArbol, int arch, Escritura escribir, List<Simbolo> simbolos) throws IOException {
		String auxLetra;
		NodoArbol auxNodo = new NodoArbol(); // nodo auxiliar en el cual se ira asignando los codigos huffman para escribir en archivo
		auxNodo.setCodigoBinario("");
		FileReader fr = null;
		BufferedReader br = null;
		int auxFile;
		if (arch == 1) {
			fr = new FileReader("tp1_grupo8.txt");
			br = new BufferedReader(fr);
		} 
		

		//ACA COMPRIME LA TABLA
		//PARA LA LONGITUD DE LA TABLA (CANT DE SIMBOLOS DISITNOS)

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
			
		escribir.agregaLinea(cantidadDeSimbolosBin);
		
		
		
		
		int i=0;
		while(i<simbolos.size()) {
			
			
			//LA DUDA ES: EL CHAR BACKSTIP Y LA ONDA ME TIRAN VALORES DE ASCII ALTISIMOS PERO EN LA TABLA POSTA NO TIOENEN ESOS VALORES. Es un error?
			
			
			//PARA EL CHAR EN ASCII
			
			String cantidadDeSimbolosASCIBin = Integer.toBinaryString((int)simbolos.get(i).getSimbolo().charAt(0));
			
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
			
			escribir.agregaLinea(cantidadDeSimbolosASCIBin); //Valor ascii
			
			// AHORA LA LONGITUD DEL HUFFMAN
			
			int logitudCodigoHuffmanInt = (simbolos.get(i).getCodigoHuffman().length()); //Longitud del binario. Si el binario es 101010 -> 6
			String logitudCodigoHuffmanBin = Integer.toBinaryString(logitudCodigoHuffmanInt); // 6 -> en binario -> 110
			int logitudCodigoHuffmanBinLength = logitudCodigoHuffmanBin.length(); //longitud del 6 en binario -> 110 ->longitud -> 3
			int cantBytesNecesarios = (int) Math.ceil( (double)logitudCodigoHuffmanInt/8 ); //Tengo la cantidad de bytes necesarios para representar el 6 -> en este caso: 1
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
			
			escribir.agregaLinea(logitudCodigoHuffmanBin); //Valor ascii
			
			
			String codHuffman = simbolos.get(i).getCodigoHuffman();
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


			codHuffman = strBits + codHuffman; //Longitud de 1 byte
			escribir.agregaLinea(codHuffman);
			
			
			
			i++;
		}
		
		double tamañoArch = this.getTamArchivo(1);
		int recorrer = 0;
		while(recorrer < tamañoArch)
		 {
			recorrer++;
			System.out.println(recorrer);
			auxFile = (br.read() & 0xFF);
			if (auxFile != 13) { // OMITE RETORNO DE CARRO 
				if (arch != 3) {
					if (auxFile != 10) { 
						
						
						char auxChar = (char) auxFile;
						auxLetra = String.valueOf(auxChar);
					} else 
						auxLetra = "enter";
					
				
					this.buscaSimbolo(listaArbol.getArbol(),auxLetra,auxNodo);
					
					escribir.agregaLinea(auxNodo.getCodigoBinario());
					//System.out.println("AUXLETRA: " + auxLetra + ":" + auxNodo.getCodigoBinario());
					//System.out.println();
					auxNodo.setCodigoBinario("");
				} else { 
					if (auxFile != 10) { 
						char auxChar = (char) auxFile;
						auxLetra = String.valueOf(auxChar);
						this.buscaSimbolo(listaArbol.getArbol(),auxLetra,auxNodo);
						escribir.agregaLinea(auxNodo.getCodigoBinario());
						auxNodo.setCodigoBinario("");
					}
				}
			}
		}
		fr.close();
		br.close();
		escribir.cierraArchivo1();
		escribir.agregaResultado("--------------------------------------------------------------------");

	}
	
	/**METODO DONDE SE REALIZA LA COMPRESION REAL DE HUFFMAN
	 * @param arch
	 * @throws IOException
	 */
	public void compresionHuffman(int arch) throws IOException {
		String binario="";
		byte bytes,wrByte;
		//Como la tabla se agrupa siempre de 1 byte | 1 byte | n bytes -> no va a haber problema con la compresino
		if (arch == 1) {
			FileInputStream in = new FileInputStream("Auxtp1_grupo8Huffman.Huf"); //Archivo huf codificado sin comprimir
			FileOutputStream out = new FileOutputStream("tp1_grupo8Huffman.Huf"); //huf comprimido
			int r = 0;
			double t = this.getTamArchivo(3);
			while(r < t) {
				bytes=(byte) (in.read() & 0xFF);
				r++;
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
			  wrByte=this.pasajeByte(binario); 
			out.write(wrByte);
			}
			in.close();
			out.close();
		} 
	}
	
	/**TRATAR LA CODIFICACION COMO BYTE EN EL ARCHIVO .FAN
	 * @param binario
	 * @return
	 */
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
	
	/**METODO PARA BUSCAR UN SIMBOLO Y ASI RETORNAR SU CODIGO HUFFMAN PARA ESCRIBIR EN EL ARCHIVO A COMPRIMIR
	 * @param arbol
	 * @param caracter
	 * @param auxNodo
	 */
	public void buscaSimbolo(NodoArbol arbol, String caracter, NodoArbol auxNodo) {
		if (arbol != null) {
			this.buscaSimbolo(arbol.getIzq(), caracter, auxNodo);
				if (arbol.getSimbolo().equals(caracter))
					auxNodo.setCodigoBinario(arbol.getCodigoBinario());
			this.buscaSimbolo(arbol.getDer(), caracter, auxNodo);
		}
	}

	/**
	 * @param arch
	 * @return retorna el tamano del archivo original
	 * @throws IOException
	 */
	public double getTamArchivo(int arch) throws IOException {
		Path ruta;
		if (arch == 1) {
			ruta = Paths.get("tp1_grupo8.txt");

		} else if (arch == 2) {
			ruta = Paths.get("tp1_grupo8Huffman.Huf");
		} else {
			ruta = Paths.get("Auxtp1_grupo8Huffman.Huf");
		}
		return Files.size(ruta);
	}

	public double getTamComprimido(int arch) throws IOException {
		Path ruta = null;
		if (arch == 1) {
			ruta = Paths.get("tp1_grupo8Huffman.Huf");
		} 
		return Files.size(ruta);

	}
	
	
	/** HACE LA DECODIFICACION
	 * @param arch
	 * @return retorna el tamano del archivo original
	 * @throws IOException
	 */
	public void decodeHuffman(List<Simbolo> simbolosDecode) {
		
		FileReader fr = null;
		BufferedReader br = null;
		byte bytes;
		int longitudTabla;
		int i = 0;
		try {
			
			//PRIMERO ARMAMOS LA TABLA Y LA DECODIFICAMOS
			FileInputStream fout = new FileInputStream("tp1_grupo8Huffman.huf");
			longitudTabla = (int)(bytes=(byte) fout.read()); //El primer elemento que lea que sea la long de la tabla
			System.out.println("------------------------------------------------------------");
			int rec = 1;
			while(i < longitudTabla && (bytes=(byte) fout.read())!=-1)  //ojo q aca tendria q hacer la mascara tambien eh
			{
				rec++;
				char simbolo = (char) (bytes & 0xFF);
				
				simbolosDecode.add(new Simbolo(String.valueOf(simbolo),longitudTabla,0)); //La frecuencia ahora para decodificar no me sirve pq es para armar la tabla, entonces la ignoro
				int longCodHuffman = ((byte)fout.read() & 0xFF);
				rec++;
				int bytesRecorrer = (int) Math.ceil((double)longCodHuffman/8);
				String bMasc = "";
				for(int j=0;j<bytesRecorrer;j++)
				{
					String ag="";
					byte b = (byte) (fout.read() & 0xFF);
					rec++;
					int l = Integer.toBinaryString((int)(b & 0xFF)).length(); //longitud del binario, tiene que ser 8... y cuando lo rearmo con los dos bytes se complica
					if(j==1) //medio harcodeado para la segunda vuelta pq cuando lee me saca los ceros del pcipio
						for(int z=0; z < 8-l; z++)
						{
							ag +="0";
						}
							
					bMasc +=  ag + Integer.toBinaryString((int)(b & 0xFF));
				}
				
				String agrego = "";
				for(int z=0; z < (longCodHuffman - bMasc.length()); z++)//Los ceros q le tengo q meter adelante para reconstruirlo bien
					agrego+="0";
					
				simbolosDecode.get(i).setCodigoHuffman(agrego + bMasc);
			
				i++;		
			}
			//CON LA TABLA YA ARMADA, PASAMOS A DECODIFICAR EL ARCHIVO
			String cadenaABuscar = "";

			double tamaño = getTamArchivo(2);
			this.creaDecode();
			while(rec <  tamaño) // Recorremos el resto del arch desde donde se quedo parado
			{
				bytes=(byte) (fout.read() & 0xFF);
				rec++;
				String bMasc =  Integer.toBinaryString(bytes & 0xFF);
				String a = "";
				for (int b=0; b < 8 - bMasc.length(); b++)
				{
					a+="0";
				}
				bMasc = a + bMasc;
				String resultado = null; 

				for(int x=0;x<bMasc.length();x++) //Al ser instantaneos, automaticamente si no encontro codigo, tiene que seguir agregando chars. Y si encontro, el proximo bit arranca uno nuevo
				{
					cadenaABuscar += bMasc.charAt(x); 
					resultado = this.buscaPorHuffman(simbolosDecode, cadenaABuscar);
					if(resultado != null)
					{
						this.escribeDecode(resultado);
						cadenaABuscar = "";
					}
				}	
				
			}

			//this.closeDecode();
			//fout.close();
			
			}catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
		
	}

	
	
	public void creaDecode()
	{
		
		try {
			this.decodeArchHuffman = new File("Resultadostp1g8Decode2.txt");
			this.w2Decode = new FileWriter(this.decodeArchHuffman);
			this.bw2Decode =  new BufferedWriter(this.w2Decode);
			this.pwDecode = new PrintWriter(this.bw2Decode);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void escribeDecode(String res) {
		
		this.pwDecode.write(res);
			
	}
	
	public void closeDecode() {
		try {
			this.w2Decode.close();
			//this.bw2Decode.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public String buscaPorHuffman(List<Simbolo> simbolosDecode, String cadenaABuscar) {
		
		int size = simbolosDecode.size();
		int i=0;
		String resp=null;
		boolean ok = false;
		while(i<size && !ok)
		{
			if(simbolosDecode.get(i).getCodigoHuffman().equals(cadenaABuscar))
			{
				ok = true;
				resp = simbolosDecode.get(i).getSimbolo();
			}
			i++;
		}
		
		return resp;
		
	}
	
}
