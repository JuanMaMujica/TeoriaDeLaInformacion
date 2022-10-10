package tp1;

import java.io.BufferedReader;
import java.io.FileReader;

import java.util.Arrays;
import java.util.HashMap;

import javax.swing.JOptionPane;

public class Main {
    public static void main(String[] args) {
        double[] arreglo= {0,0,0,0,0,0,0,0,0};   //el criterio que us? aca fue que los primeros tres elementos del array van a simbolizar la cantidad de veces que fueron a determinada letra si la anterior era la A.
        //es decir, para el elemento 0, la cantidad de veces que hay una A despues de una A, para el elemento 1, la cantidad de veces que hay una B despues de una A, y as? sucesivamente con los dem?s simbolos de la fuente.
        int[] cantSimbolos= {0,0,0};  // lo hice de auxiliar para verificar que est?n bien las cantidades
        double[][] matriz = new double[3][3];
        double[][] matrizSistema = new double[3][3];
        try {
            String[] options = new String[] {
                "Parte 1", "Parte 2- 3 caracteres", "Parte 2- 5 caracteres", "Parte 2- 7 Caracteres", "Terminar"
            };
            int option =
                JOptionPane.showOptionDialog(null, "Que hacemo?", "Elegir", JOptionPane.DEFAULT_OPTION,
                                             JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
            HashMap<String, String> codificacionSF;
            HashMap<String,Integer> descendingMap;
            BufferedReader lectorCod;
            FileReader arch = new FileReader("archivo.txt");
                if (arch.ready()) {
                System.out.println("Archivo encontrado");
                BufferedReader lector = new BufferedReader(arch);
                switch(option)
                {
                case 0:
                    char x;
                    char ant = 'X';
                    int cantidad = 0;
                    int auxiliar = 0;
                    while ((auxiliar = lector.read()) != -1) {
                        x = (char) auxiliar;
                        cantidad++;
                        // System.out.println(x);  //lee bien
                        switch (ant)
                        {
                        case ('A'):
                            arreglo[x - 65]++;
                            break;
                        case ('B'):
                            arreglo[x - 62]++;
                            break;
                        case ('C'):
                            arreglo[x - 59]++;
                            break;
                        }

                        switch (x) {
                        case ('A'):
                            cantSimbolos[0]++;
                            break;
                        case ('B'):
                            cantSimbolos[1]++;
                            break;
                        case ('C'):
                            cantSimbolos[2]++;
                            break;
                        }
                        ant = x;
                    }


                    for (int i = 0; i < 9; i++) {
                        System.out.println("Elemento [" + i + "]: " + arreglo[i]);
                    }

                    for (int i = 0; i < 9; i++) {
                        char a, b;
                        if (i / 3 < 1)
                            a = 'A';
                        else if (i / 3 >= 1 && i / 3 < 2)
                            a = 'B';
                        else
                            a = 'C';

                        switch (i % 3) {
                        case (0):
                            b = 'A';
                            break;
                        case (1):
                            b = 'B';
                            break;
                        case (2):
                            b = 'C';
                            break;
                        default:
                            b = 'X';
                        }
                        System.out.println("P(" + a + "->" + b + ")" + "=" + (float) arreglo[i] / cantidad);
                    }
                    /*      System.out.println("------------------");                              //usado para verificar
                                for (int i=0;i<3;i++) {
                                        System.out.println("Elemento ["+i+"]: "+ cantSimbolos[i]);
                                }
                                System.out.println("------------------");
                                System.out.println("Cantidad de simbolos totales:" + cantidad); */

                    matriz[0][0] = arreglo[0] / (arreglo[0] + arreglo[1] + arreglo[2]);
                    matriz[1][0] = arreglo[1] / (arreglo[0] + arreglo[1] + arreglo[2]);
                    matriz[2][0] = arreglo[2] / (arreglo[0] + arreglo[1] + arreglo[2]);
                    matriz[0][1] = arreglo[3] / (arreglo[3] + arreglo[4] + arreglo[5]);
                    matriz[1][1] = arreglo[4] / (arreglo[3] + arreglo[4] + arreglo[5]);
                    matriz[2][1] = arreglo[5] / (arreglo[3] + arreglo[4] + arreglo[5]);
                    matriz[0][2] = arreglo[6] / (arreglo[6] + arreglo[7] + arreglo[8]);
                    matriz[1][2] = arreglo[7] / (arreglo[6] + arreglo[7] + arreglo[8]);
                    matriz[2][2] = arreglo[8] / (arreglo[6] + arreglo[7] + arreglo[8]);

                    System.out.println("Matriz...");
                    for (int i = 0; i < 3; i++) { //no nula!!!
                        for (int j = 0; j < 3; j++) {
                            System.out.print("[" + matriz[i][j] + "]"); //"Matriz["+ i+"]["+j+"] ="
                        }
                        System.out.println("");
                    }

                    double[] vectorEstacionario = { (double) 1 / 3, (double) 1 / 3, (double) 1 / 3 };
                    double[] vectorAux = { 0, 0, 0 };

                    double aux = 0;
                    for (int k = 0; k < 250; k++) {
                        for (int i = 0; i < 3; i++) {
                            for (int j = 0; j < 3; j++) {
                                aux += vectorEstacionario[j] * matriz[i][j];
                            }
                            vectorAux[i] = aux;
                            aux = 0;
                        }
                        for (int p = 0; p < 3; p++)
                            vectorEstacionario[p] = vectorAux[p];
                    }

                    for (int i = 0; i < 3; i++)
                        System.out.println("V[" + i + "]: " +
                                           vectorEstacionario[i]); //existe el vector estacionario y es independiente de las condiciones iniciales. -> sí es ergódica.


                    double entropia = 0;

                    for (int j = 0; j < 3; j++) {
                        for (int i = 0; i < 3; i++) {
                            entropia +=
                                vectorEstacionario[j] * matriz[i][j] *
                                (Math.log((1 / matriz[i][j]) /
                                          Math.log(2))); //multiplicar por logaritmo en base 2 de 1/matriz[i][j]
                        }
                    }
                    //0.47 0.57 0.393 =1.43
                    System.out.println("Entropia: " + entropia);
                    break;
                case 1:
                    System.out.println("case 1");

                    HashMap<String, Integer> mapa1 = Calculos.generaMapa(lector, 1);
                    lector.close();
                    arch.close();
                    
                    arch = new FileReader("archivo.txt");
                    lectorCod = new BufferedReader(arch);

                    descendingMap = Calculos.orderMap(mapa1);
                    codificacionSF = Calculos.ShanonFano(descendingMap);
                    Calculos.crearCodificacion(codificacionSF, lectorCod, 1);
                    System.out.println(Arrays.asList(mapa1)); //printea el hashmap con el elemento agregado y la cantidad de veces que aparece
                    System.out.println("Cantidad de informacion: " + Calculos.info(mapa1));
                    System.out.println("Entropia: " + Calculos.Entropia(mapa1));
                    System.out.println("Longitud Media: " + Calculos.longitudMedia(mapa1));
                    System.out.println("Kraft: " + Calculos.Kraft(mapa1));
                    System.out.println(Arrays.asList(Calculos.orderMap(mapa1)));
                    System.out.println("Codificacion Shannon-Fano"+Arrays.asList(codificacionSF));
                    arch.close();
                    lectorCod.close();
                    if (Calculos.Kraft(mapa1) <= 1)
                        System.out.println("Cumple con Kraft");
                    else
                        System.out.println("No cumple Kraft");
                    break;
                case 2:

                    HashMap<String, Integer> mapa2 = Calculos.generaMapa(lector, 2);

                    lector.close();
                    arch.close();
                    
                    arch = new FileReader("archivo.txt");
                    lectorCod = new BufferedReader(arch);

                    descendingMap = Calculos.orderMap(mapa2);
                    codificacionSF = Calculos.ShanonFano(descendingMap);
                    Calculos.crearCodificacion(codificacionSF, lectorCod, 2);
                    System.out.println(Arrays.asList(mapa2));
                    System.out.println("Cantidad de informacion: " + Calculos.info(mapa2));
                    System.out.println("Entropia: " + Calculos.Entropia(mapa2));
                    System.out.println("Longitud Media: " + Calculos.longitudMedia(mapa2));
                    System.out.println(Arrays.asList(descendingMap));
                    System.out.println("Codificacion Shannon-Fano"+Arrays.asList(codificacionSF));
                    System.out.println("Kraft: " + Calculos.Kraft(mapa2));
                    arch.close();
                    lectorCod.close();
                    if (Calculos.Kraft(mapa2) <= 1)
                        System.out.println("Cumple con Kraft");
                    else
                        System.out.println("No cumple Kraft");
                    break;
                case 3:
                    System.out.println("case 3");

                    HashMap<String, Integer> mapa3 = Calculos.generaMapa(lector, 3);
                    
                    lector.close();
                    arch.close();
                    
                    arch = new FileReader("archivo.txt");
                    lectorCod = new BufferedReader(arch);

                    descendingMap = Calculos.orderMap(mapa3);
                    codificacionSF = Calculos.ShanonFano(descendingMap);
                    Calculos.crearCodificacion(codificacionSF, lectorCod, 3);
                    System.out.println(Arrays.asList(mapa3));
                    System.out.println("Cantidad de informacion: " + Calculos.info(mapa3));
                    System.out.println("Entropia: " + Calculos.Entropia(mapa3));
                    System.out.println("Longitud Media: " + Calculos.longitudMedia(mapa3));
                    System.out.println(Arrays.asList(descendingMap));
                    System.out.println("Codificacion Shannon-Fano"+Arrays.asList(codificacionSF));
                    System.out.println("Kraft: " + Calculos.Kraft(mapa3));
                    arch.close();
                    lectorCod.close();
                    if (Calculos.Kraft(mapa3) <=
                        1) //este cumple con kraft.... pero ver porque quiza los simbolos que no aparecen deberian contar tambien.
                        System.out.println("Cumple con Kraft");
                    else
                        System.out.println("No cumple Kraft");

                    if (Calculos.longitudMedia(mapa3) > Calculos.Entropia(mapa3)) {
                        System.out.println("RENDIMIENTO" + Calculos.Rendimiento(mapa3));
                        System.out.println("REDUNDANCIA" + Calculos.Redundancia(mapa3));
                    }
                    break;
                }

            }
        } catch (Exception e) {
                System.out.println("Error");
        }
    }
}
