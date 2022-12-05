package client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Decodificacion {
	public static void decodifica() throws IOException {
		List<SimboloShannon> simbolos = new ArrayList<SimboloShannon>(); //para obtener la tabla luego de decodificarla
		int auxiliar=0;
		int i=0;
		String binaryString="";
		boolean flag = false;
		BufferedReader lectorCod;
        FileReader arch;
        byte bytes;
        arch = new FileReader("Arch.Fan");
        int longitudTabla;
        BufferedReader lector = new BufferedReader(arch);
        FileInputStream fout = new FileInputStream("Arch.Fan");
        
        File archivoDescomprimido = new File("Descomprimido.txt");
	       
	       if(!archivoDescomprimido.exists()){
	           archivoDescomprimido.createNewFile();
	       }
	        
	   FileWriter fw = new FileWriter(archivoDescomprimido);
        
		Path ruta = Paths.get("Arch.Fan");
        
        
        longitudTabla = (int)(bytes=(byte) fout.read()); //El primer elemento que lea que sea la long de la tabla
        System.out.println("Longitud tabla: "+ longitudTabla);
		System.out.println("------------------------------------------------------------");
		int rec = 1;
		while(i < longitudTabla)  
		{
			byte LongitudAsciiBin = (byte) (fout.read() & 0xFF);
			//System.out.println("Simbolos ascii bin: "+ LongitudAsciiBin);
			rec++;
		   //char simbolo = (char) (bytes & 0xFF);
			
			int longCodFano = ((byte)fout.read() & 0xFF);
			//rec++;
			int bytesRecorrer = (int) Math.ceil((double)longCodFano/8);
			
			
			System.out.println("Longitud codigo Fano(bits) : "+ longCodFano + " bytes a recorrer: "+ bytesRecorrer);
			String bMasc = "";
			for(int j=0;j<bytesRecorrer;j++)
			{ 
				String ag="";
				byte b = (byte) (fout.read() & 0xFF);
				//System.out.println("Byte leido: "+ Integer.toBinaryString((int) b&0xFF));
				rec++;
				int l = Integer.toBinaryString((int)(b & 0xFF)).length(); //longitud del binario, tiene que ser 8... y cuando lo rearmo con los dos bytes se complica
				if(j==1) //medio harcodeado para la segunda vuelta pq cuando lee me saca los ceros del pcipio
					for(int z=0; z < 8-l; z++)
					{
                                                rec++;
						ag +="0";
					}
						
				bMasc +=  ag + Integer.toBinaryString((int)(b & 0xFF));
			}
			
			
			String agrego = "";
			for(int z=0; z < (longCodFano - bMasc.length()); z++)//Los ceros q le tengo q meter adelante para reconstruirlo bien
				agrego+="0";
				
			System.out.println("Codigo Fano: " + agrego + bMasc);
			char car = (char) LongitudAsciiBin;
			SimboloShannon simboloFano = new SimboloShannon(Character.toString(car),0,0);
			simboloFano.setCodigoShannon(agrego + bMasc);
			simbolos.add(simboloFano); //armamos la tabla nueva para decodificar	
			i++;		
			}
		
        
        String cadenaABuscar = "";

        System.out.println("REC" + rec);
        i=longitudTabla+rec; //para q arranque donde corresponde
        
        
        System.out.println("tamaño ruta: "+ Files.size(ruta));
		while(i < Files.size(ruta)) // Recorremos el resto del arch desde donde se quedo parado
		{
			
			bytes=(byte) (fout.read() & 0xFF);
			i++;
			String bMasc =  Integer.toBinaryString(bytes & 0xFF);
			//System.out.println("BMasc: "+ bMasc);
			String a = "";
			for (int b=0; b < 8 - bMasc.length(); b++)
			{
				a+="0";
			}
			
			bMasc = a + bMasc;
			//System.out.println("Byte: "+ h++ + " "+ bMasc);
			String resultado = null;
			for(int x=0;x<bMasc.length();x++) //Al ser instantaneos, automaticamente si no encontro codigo, tiene que seguir agregando chars. Y si encontro, el proximo bit arranca uno nuevo
			{
				cadenaABuscar += bMasc.charAt(x); 
				//System.out.println("Cadena a buskar: "+ cadenaABuscar);
				resultado= buscaElemento(simbolos,cadenaABuscar);
				
				if(resultado != null)
				{
					
					//System.out.println("Resultado: "+ resultado);
					//System.out.println("Cadena encontrada: "+ cadenaABuscar);
					
					if (cadenaABuscar.equals("111111001")) 
						fw.write("\n");
					else {
						fw.write(resultado);
					}
					fw.flush();	
					cadenaABuscar = "";
					
				} //else System.out.println("null")
				
			}	
			
		
		
		
		}  
} 
		public static String buscaElemento(List<SimboloShannon> simbolos, String valorABuscar) {
			Iterator<SimboloShannon> iterador = simbolos.iterator();
					
			if (valorABuscar.equals("000"))
				return simbolos.get(simbolos.size()-1).getSimbolo();

			SimboloShannon simboloAux=new SimboloShannon("",0,0);
			while(iterador.hasNext() && !simboloAux.getCodigoShannon().equals(valorABuscar)) {
				simboloAux = iterador.next();
				//System.out.println("valor a buscar: "+ valorABuscar + "codigo actual: "+ simboloAux.getCodigoShannon());
			}
	
			return iterador.hasNext()?simboloAux.getSimbolo():null;
			
		} 


}
