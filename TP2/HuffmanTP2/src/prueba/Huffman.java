package prueba;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
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
		} else if (arch == 2) {
			fr = new FileReader("Chino.txt");
			br = new BufferedReader(fr);
		} else {
			fr = new FileReader("imagen.raw");
			br = new BufferedReader(fr);
			br.readLine();
			br.readLine();
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
			
			
			//System.out.println(i);
			//LA DUDA ES: EL CHAR BACKSTIP Y LA ONDA ME TIRAN VALORES DE ASCII ALTISIMOS PERO EN LA TABLA POSTA NO TIOENEN ESOS VALORES. Es un error?
			
			
			//PARA EL CHAR EN ASCII
			
			String cantidadDeSimbolosASCIBin = Integer.toBinaryString((int)simbolos.get(i).getSimbolo().charAt(0));
			/*System.out.println("SIMBOLO " + simbolos.get(i).getSimbolo());
			System.out.println((int)simbolos.get(i).getSimbolo().charAt(0));
			*/
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
			//System.out.println(codHuffman);
			escribir.agregaLinea(codHuffman);
			//escribir.agregaLinea(auxLetra);
			
			
			//System.out.println("SIMBOLO: " + simbolos.get(i).getSimbolo().charAt(0) + "  " + (int)simbolos.get(i).getSimbolo().charAt(0) + " : " + cantidadDeSimbolosASCIBin + " " + logitudCodigoHuffmanBin + " " + codHuffman);
			
			i++;
		}
		
		while ((auxFile = br.read()) != -1) {
			if (auxFile != 13) { // SE OMITE EL DENOMINADO RETORNO DE CARRO YA QUE NO ES NECESARIO
				if (arch != 3) {
					if (auxFile != 10) { // hay que asignarle la cadena "enter" ya que en la colección el simbolo esta seteado de esa forma
						
						
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
					if (auxFile != 10) {  // PARA LA IMAGEN SE OMITE LOS ENTER O \N
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
		} else if (arch == 2) {
			FileInputStream in = new FileInputStream("AuxiliarChinoHuffman.Huf");
			FileOutputStream out = new FileOutputStream("ChinoHuffman.Huf");
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
		} else {
			FileInputStream in = new FileInputStream("AuxiliarImagenHuffman.Huf");
			FileOutputStream out = new FileOutputStream("ImagenHuffman.Huf");
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
			ruta = Paths.get("Chino.txt");
		} else {
			ruta = Paths.get("imagen.raw");
		}
		return Files.size(ruta);
	}

	public double getTamComprimido(int arch) throws IOException {
		Path ruta;
		if (arch == 1) {
			ruta = Paths.get("tp1_grupo8Huffman.Huf");

		} else if (arch == 2) {
			ruta = Paths.get("ChinoHuffman.Huf");
		} else {
			ruta = Paths.get("ImagenHuffman.Huf");
		}
		return Files.size(ruta);

	}
	
	
	/**
	 * CREA LA TABLA LEYENDO DEL ARCHIVO
	 * @param arch
	 * @return retorna el tamano del archivo original
	 * @throws IOException
	 */
	public void createTabla() {
		
		
		
	}

	
	public void comprimeTableNoBin(int arch, FileReader fr, List<Simbolo> simbolos) {
		
		for (int i = 0; i < simbolos.size(); i++) {
			//System.out.println(simbolos.get(i).getSimbolo());
			//escribir.agregaResultado(simbolos.get(i).toString()); // AQUI SE ESCRIBEN EN LOS ARCHIVOS DE RESULTADOS LA CODIFICACION EN HUFFMAN ADEMÃ�S DE OTROS DATOS
		} 
		
		
		/*while ((auxFile = br.read()) != -1) {
			if (auxFile != 13) { // SE OMITE EL DENOMINADO RETORNO DE CARRO YA QUE NO ES NECESARIO
				if (arch != 3) {
					if (auxFile != 10) { // hay que asignarle la cadena "enter" ya que en la colección el simbolo esta seteado de esa forma
						char auxChar = (char) auxFile;
						auxLetra = String.valueOf(auxChar);
					} else 
						auxLetra = "enter";
					this.buscaSimbolo(listaArbol.getArbol(),auxLetra,auxNodo);
					escribir.agregaLinea(auxNodo.getCodigoBinario());
					auxNodo.setCodigoBinario("");
				} else { 
					if (auxFile != 10) {  // PARA LA IMAGEN SE OMITE LOS ENTER O \N
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
	*/		
	}
	
	public void decodeHuffman() {
		
		FileReader fr = null;
		BufferedReader br = null;
		byte bytes;
		int longitudTabla;
		int i = 0;
		try {
			//fr = new FileReader("tp1_grupo8Huffman.huf");
			FileInputStream fout = new FileInputStream("tp1_grupo8Huffman.huf");
			//br = new BufferedReader(fr);
			longitudTabla = (int)(bytes=(byte) fout.read()); //El primer elemento que lea que sea la long de la tabla
			while((bytes=(byte) fout.read())!=-1 && i < longitudTabla) 
			{
						
				System.out.println((bytes & 0xFF));
				char simbolo = (char) (bytes & 0xFF);
				System.out.println("SIMBOLO: " + simbolo);
				int longCodHuffman = ((byte)fout.read() & 0xFF);
				System.out.println("LONGITUD HUFFMAN: " + longCodHuffman);
				int bytesRecorrer = (int) Math.ceil((double)(longCodHuffman/8));
				System.out.println("BYTES A RECORRER: " + bytesRecorrer);
				for(int j=0;j<bytesRecorrer;j++)
				{
					byte b = (byte) fout.read();
				}
				System.out.println(i);
				i++;
						
				/*binario+=String.valueOf(bytes-48); // ya que el valor que voy a recibir es o un 1 o un 0, entonces resto por 48 por valor de tabla ASCII
				if(binario.length()==8) {
					wrByte=this.pasajeByte(binario); // paso el byte en string a byte real
					binario=""; // reinicio la cadena 
					out.write(wrByte); //escribo en el archivo
				}*/
			}
			
			}catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
		
		
		//this.createTabla();
		
	}

	
}
