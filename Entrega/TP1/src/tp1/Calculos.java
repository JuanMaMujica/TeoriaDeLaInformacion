package tp1;

import java.io.BufferedReader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class Calculos {

    public static HashMap<String,Integer> generaMapa(BufferedReader lector, int caso) throws IOException{
            char x;
            int auxiliar=0;
            int cant=0;
            int k;
            switch (caso) {
            case 1: k=3;
            break;
            case 2: k=5;
            break;
            case 3: k=7;
            break;
            default: k=0;
            }
            String arrayString[]= new String[3350];
            Arrays.fill(arrayString, "");
            HashMap<String,Integer> mapa1 = new HashMap<String, Integer>(); //hashmap con clave String (el elemento agregado) y value Integer (cantidad de veces q aparece)
            while ((auxiliar!=-1)) { //do while
            	//while i menor a k y lector read distinto de -1 iterar. si i es igual a k, descartar. 
 	
                for (int i = 0; i < k; i++) {
                    if ((auxiliar = lector.read()) != -1) {
                        x = (char) auxiliar;
                        arrayString[cant] += x;
                    }
                }
                
                if (arrayString[cant].length()==k) {
                	if (mapa1.get(arrayString[cant]) == null) {
                        mapa1.put(arrayString[cant], 0);
                    }
                    int h = mapa1.get(arrayString[cant]);
                    mapa1.put(arrayString[cant],h+1);
                   // System.out.println(arrayString[cant]);
                    cant++; 	
                }
                
                
            }               
            return mapa1;
    }
    
    public static double info(HashMap<String,Integer> mapa) {
            Iterator<String> iterador = mapa.keySet().iterator();
            double aux = 0;
            double size= 0;
            
            //fijarse xq estoy recorriendo el mapa 2 veces xd 
            for (Integer val : mapa.values()) {
                    size+=val;
            }
            while (iterador.hasNext()) {
                    String clave = iterador.next();
                    aux+=Math.log(1/(mapa.get(clave)/size))/Math.log(3);
            }
            return aux;             
    }
    
    public static double Entropia(HashMap<String,Integer> mapa) {
            Iterator<String> iterador = mapa.keySet().iterator();
            double aux = 0;
            double size= 0;
            
            //fijarse xq estoy recorriendo el mapa 2 veces xd 
            for (Integer val : mapa.values()) {
                    size+=val;
            }
            while (iterador.hasNext()) {
                    String clave = iterador.next();
                    aux+=(mapa.get(clave)/size)* Math.log(1/(mapa.get(clave)/size))/Math.log(3);
            }
            return aux;             
    }
    
    public static double longitudMedia(HashMap<String,Integer> mapa) {
            Iterator<String> iterador = mapa.keySet().iterator();
            double aux = 0;
            double size = 0;
            for (Integer val : mapa.values()) {
                    size+=val;
            }
            while (iterador.hasNext()) {
                    String clave = iterador.next();
                    aux+=(mapa.get(clave)/size)*clave.length();
            }
            return aux;
    }
    
    public static double Kraft(HashMap<String,Integer> mapa, int caso) {
        Iterator<String> iterador = mapa.keySet().iterator();
        double aux = 0;
        double size = 0;
        int k;
        
        switch (caso) {
        case 1: k=3;
        break;
        case 2: k=5;
        break;
        case 3: k=7;
        break;
        default: k=0;
        }
        
        for (int i=0; i<Math.pow(3, k);i++) {
            aux+=Math.pow(3,-k); 
        }
        
        return aux;
}

    public static double Rendimiento(HashMap<String, Integer> mapa3) {
             return (Calculos.Entropia(mapa3) / Calculos.longitudMedia(mapa3));
            
    }

    public static double Redundancia(HashMap<String, Integer> mapa3) {
            // TODO Auto-generated method stub
            return (1 - Calculos.Rendimiento(mapa3));
    }
    
    public static HashMap<String,Integer> orderMap(HashMap<String,Integer> map){
        LinkedHashMap<String,Integer> descendingMap = new LinkedHashMap<>();
           map.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
           .forEachOrdered(x -> descendingMap.put(x.getKey(), x.getValue()));
        return descendingMap;
    }
    
    public static HashMap<String,String> ShanonFano(HashMap<String,Integer> descendingMap){
        LinkedHashMap<String,String> codificacionShanonFano = new LinkedHashMap<String,String>();
        descendingMap.remove("");
        descendingMap.entrySet().stream().forEachOrdered(x -> codificacionShanonFano.put(x.getKey(),"")); //inicializo con vacio el mapa 
        
        creaTabla(codificacionShanonFano,descendingMap);
        return codificacionShanonFano;
    }
    
    private static void creaTabla(HashMap<String,String> tabla, HashMap<String,Integer> descendingMap){
        if(descendingMap.size()>1){
            int suma=0;
            int mitad=0;
            int sumaAux=0;
            double aux=0;
            String simbolo=null;
            String acumula=null;
            for(Map.Entry<String,Integer> map: descendingMap.entrySet()){
                suma+=map.getValue();
            }
            
            for(Map.Entry<String,Integer> map: descendingMap.entrySet()){
                aux = map.getValue();
                simbolo = map.getKey();
                if(simbolo==null){
                    simbolo="";
                }
                acumula = tabla.get(simbolo);
                if(sumaAux+aux < suma/2){
                    tabla.put(simbolo, acumula+"0");
                    mitad++;
                    sumaAux+=aux;
                } else {
                    if(Math.abs((suma/2)-sumaAux) >= Math.abs((suma/2)-(sumaAux+aux)) && aux!=0){
                        tabla.put(simbolo, acumula+"0");
                        mitad++;
                        sumaAux+=aux;
                    } else {
                        tabla.put(simbolo, acumula+"1");
                    }
                }
            }
            if(mitad!=0){
                LinkedHashMap<String,Integer> superior = new LinkedHashMap<String,Integer>();
                LinkedHashMap<String,Integer> inferior = new LinkedHashMap<String,Integer>();
                int i=0;
                int j=0;
                for(Map.Entry<String,Integer> map : descendingMap.entrySet()){
                    if(i<mitad){
                        superior.put(map.getKey(), map.getValue());
                    }
                    i++;
                }
                creaTabla(tabla,superior);
                
                for(Map.Entry<String,Integer> map : descendingMap.entrySet()){
                    if(j>=mitad && j<descendingMap.size()){
                        inferior.put(map.getKey(), map.getValue());
                    }
                    j++;
                }
                creaTabla(tabla,inferior);
            }
        }
    }

    public static void crearCodificacion(HashMap<String,String> codificacion,BufferedReader lector, int caso) throws IOException {
    	//armar algoritmo de descompresion para verificar
    	//guardar los datos como datos binarios!!! 
    	//citar autores en el informe!!! suma puntos
        File codificacionFile;
        char x;
        int auxiliar=0;
        int cant=0;
        int k;
        switch (caso) {
        case 1: 
            k=3;
            codificacionFile = new File("archivo3.bin");
        break;
        case 2: 
            k=5;
            codificacionFile = new File("archivo5.bin");
        break;
        case 3: 
            k=7;
            codificacionFile = new File("archivo7.bin");
        break;
        default:
            k=0;
            codificacionFile = new File("archivoDefault.bin");
        break;
        }
        
        if(!codificacionFile.exists()){
            codificacionFile.createNewFile();
        }
        
        FileWriter fw = new FileWriter(codificacionFile);
     //   BufferedWriter bw = new BufferedWriter(fw);
        
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
            for(Map.Entry<String,String> cod : codificacion.entrySet()){
                if(simbolo.equals(cod.getKey())){
                    codShanonFano+=cod.getValue();
                }
            }  
        }
        byte b=0;
        int i=0;
        byte c=0;
        int dato=0;
        int p=0;
        
       byte[] byte_array = new byte[(codShanonFano.length()/8)+1];
        
        Arrays.fill(byte_array, (byte) 0);
        
         /*   for (String clave:codificacion.keySet()) {
        String valor = codificacion.get(clave);
        System.out.println("Clave: " + clave + ", valor: " + valor);
    }  //generar tabla */
        
        System.out.println(codShanonFano);
        
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
            //dato = b & 0xFF;   //mascara para considerar que es unsigned byte     
           // System.out.println(Integer.toBinaryString(dato) + " "+ (dato));
            //System.out.println(b);
        }
        
        fout.write(byte_array);

        fout.close();
        fw.close();     
        }
    }