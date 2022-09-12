package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		int[] arreglo= {0,0,0,0,0,0,0,0,0};   //el criterio que usé aca fue que los primeros tres elementos del array van a simbolizar la cantidad de veces que fueron a determinada letra si la anterior era la A.
		//es decir, para el elemento 0, la cantidad de veces que hay una A despues de una A, para el elemento 1, la cantidad de veces que hay una B despues de una A, y así sucesivamente con los demás simbolos de la fuente.
		int[] cantSimbolos= {0,0,0};  // lo hice de auxiliar para verificar que estén bien las cantidades
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
				
			}
		} catch (Exception e) {
			System.out.println("Error");
		}
	}
}
