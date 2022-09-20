package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Scanner;
import Jama.Matrix;
//fuente de memoria no nula!!!
//el informe que este bien prolijo como se pide en el pdf. vale mucho de la nota. justificar xD
//si no es ergodica, el sistema de ecuaciones no tendra solucion o similar
public class Main {
	public static void main(String[] args) {
		double[] arreglo= {0,0,0,0,0,0,0,0,0};   //el criterio que us� aca fue que los primeros tres elementos del array van a simbolizar la cantidad de veces que fueron a determinada letra si la anterior era la A.
		//es decir, para el elemento 0, la cantidad de veces que hay una A despues de una A, para el elemento 1, la cantidad de veces que hay una B despues de una A, y as� sucesivamente con los dem�s simbolos de la fuente.
		int[] cantSimbolos= {0,0,0};  // lo hice de auxiliar para verificar que est�n bien las cantidades
		double[][] matriz = new double[3][3];
		double[][] matrizSistema = new double[3][3];
		try {
			FileReader arch = new FileReader("archivo.txt");
			if (arch.ready()) {
				System.out.println("Archivo encontrado");
				BufferedReader lector = new BufferedReader(arch);
				char x;
				char ant='X';
				int cantidad=0;
				int auxiliar=0;
				while ((auxiliar = lector.read()) !=-1) {
					x= (char) auxiliar;
					cantidad++;
					// System.out.println(x);  //lee bien
					switch (ant) {
						case ('A'): arreglo[x-65]++;
						break;
						case ('B'): arreglo[x-62]++;
						break;
						case ('C'): arreglo[x-59]++;
						break;
					}
					
					switch (x) {
						case ('A'): cantSimbolos[0]++;
						break;
						case ('B'): cantSimbolos[1]++;
						break;
						case ('C'): cantSimbolos[2]++;
						break;
					}
					ant=x;
				}
				
				
				for (int i=0;i<9;i++) {
					System.out.println("Elemento ["+i+"]: "+ arreglo[i]);
				}
				
				for (int i=0;i<9;i++) {
					char a,b;
					if (i / 3 < 1) 
						a='A';
					else if (i/3 >=1 && i/3<2)
						a='B';
					else
						a='C';
					
					switch (i % 3) {
						case (0): b='A';
						break;
						case (1): b='B';
						break;
						case (2): b='C';
						break;
						default: b='X';
					}
					
					System.out.println("P("+ a+ "->"+ b  +")"+"=" + (float) arreglo[i]/cantidad);
					
					
				}
			/*	System.out.println("------------------");                              //usado para verificar 
				for (int i=0;i<3;i++) {
					System.out.println("Elemento ["+i+"]: "+ cantSimbolos[i]);
				}
				System.out.println("------------------");
				System.out.println("Cantidad de simbolos totales:" + cantidad); */ 
				matriz[0][0]=arreglo[0]/(arreglo[0]+arreglo[1]+arreglo[2]);
				matriz[1][0]=arreglo[1]/(arreglo[0]+arreglo[1]+arreglo[2]);
				matriz[2][0]=arreglo[2]/(arreglo[0]+arreglo[1]+arreglo[2]);
				matriz[0][1]=arreglo[3]/(arreglo[3]+arreglo[4]+arreglo[5]);
				matriz[1][1]=arreglo[4]/(arreglo[3]+arreglo[4]+arreglo[5]);
				matriz[2][1]=arreglo[5]/(arreglo[3]+arreglo[4]+arreglo[5]);
				matriz[0][2]=arreglo[6]/(arreglo[6]+arreglo[7]+arreglo[8]);
				matriz[1][2]=arreglo[7]/(arreglo[6]+arreglo[7]+arreglo[8]);
				matriz[2][2]=arreglo[8]/(arreglo[6]+arreglo[7]+arreglo[8]);
				
				
				
				
				
				System.out.println("Matriz...");
				for (int i=0;i<3;i++) { //no nula!!!
					for (int j=0;j<3;j++) {
						System.out.print("["+matriz[i][j]+"]"); //"Matriz["+ i+"]["+j+"] ="
					}
					System.out.println("");
				}
				
				matrizSistema=matriz;
				
				
				matrizSistema[0][0]--;
				matrizSistema[1][1]--;
				matrizSistema[2][2]--;
				
				double[] resultadosSistema= {0,0,0};
			
				Matrix matrixSystem = new Matrix(matrizSistema);  //libreria jama para resolver sistema de ecuaciones. ver documentacion
				Matrix resultados = new Matrix(resultadosSistema,3); // documentacion: https://math.nist.gov/javanumerics/jama/doc/
				
				Matrix solucion = matrixSystem.solve(resultados);
				
				 System.out.println("Matriz Sistema...");  //muestro matriz - matriz identidad
				for (int i=0;i<3;i++) { //no nula!!!
					for (int j=0;j<3;j++) {
						System.out.print("["+matrizSistema[i][j]+"]"); //"Matriz["+ i+"]["+j+"] ="
					}
					System.out.println("");
				} 
							
				System.out.println("x = " + solucion.get(0, 0));
		        System.out.println("y = " + solucion.get(1, 0));
		        System.out.println("z = " + solucion.get(2, 0));
			}
		} catch (Exception e) {
			System.out.println("Error");
		}
	}
}
