package modelo;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Decodificacion {
	public static void decodifica(List<SimboloShannon> simbolos) throws IOException {
		int auxiliar=0;
		String binaryString="";
		boolean flag = false;
		BufferedReader lectorCod;
        FileReader arch;
        byte bytes;
        arch = new FileReader("arch.Fan");
        BufferedReader lector = new BufferedReader(arch);
        FileInputStream fout = new FileInputStream("arch.Fan");
        
        
        String cadenaABuscar = "";
		int recorre = fout.read() & 0xFF;

		bytes=(byte) (fout.read() & 0xFF);
		while(bytes!=-1) // Recorremos el resto del arch desde donde se quedo parado
		{
			String bMasc =  Integer.toBinaryString(bytes & 0xFF);
			System.out.println("BMasc: "+ bMasc);
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
				//System.out.println("Cadena a buskar: "+ cadenaABuscar);
				resultado= buscaElemento(simbolos,cadenaABuscar);
				if(resultado != null)
				{
					System.out.println("Resultado: "+ resultado);
					System.out.println("Cadena encontrada: "+ cadenaABuscar);
					cadenaABuscar = "";
				} //else System.out.println("null");
			}	
			
		bytes=(byte) (fout.read() & 0xFF);
		
		if(bytes == -1)
			bytes =(byte) 255;
		}
}
		public static String buscaElemento(List<SimboloShannon> simbolos, String valorABuscar) {
			Iterator<SimboloShannon> iterador = simbolos.iterator();
					
			
			SimboloShannon simboloAux=new SimboloShannon("",0,0);
			while(iterador.hasNext() && !simboloAux.getCodigoShannon().equals(valorABuscar)) {
				simboloAux = iterador.next();
				//System.out.println("valor a buscar: "+ valorABuscar + "codigo actual: "+ simboloAux.getCodigoShannon());
			}
	
			return iterador.hasNext()?simboloAux.getSimbolo():null;
		}


}
