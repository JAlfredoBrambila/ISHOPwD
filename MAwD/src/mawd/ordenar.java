/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mawd;

import java.util.Arrays;
import java.util.Comparator;

/**
 *
 * @author PC
 */
public class ordenar {
    // si el metodo va a estar en una clase que instancies quitale el static
    public static void ordenarMatriz(double[][] array, int columnaOrden) 
    {
        java.util.Arrays.sort(array, new java.util.Comparator<double[]>() 
        {
            public int compare(double[] a, double[] b) 
            {
                return Double.compare(a[columnaOrden], b[columnaOrden]);
            }
        });
    }
    
   public static void main(String[] args) {
        double[][] array = {{1,12.8},{2,25.8},{3,15.0},{4,29.8},{5,10.1}};
       int[] val=new int[60];
       int gen;
       
       for(int i=0;i<60;i++)
       {
           boolean ban=false;
           gen=(int) Math.floor(Math.random()*(99-5+1)+5);
           for(int j=0;j<60;j++)
           {
              if(gen==val[j])
              {
                  ban=true;
                  i--;
                  break;
              }
           }
           if(ban==false){
           val[i]=gen;
           }
           
       }
        //int valorEntero = (int) Math.floor(Math.random()*(99-5+1)+5);
        //mandar matriz y columna a ordenar
        ordenarMatriz(array,1);
        
        for(int i=0; i<array.length; i++) {
            for(int j=0; j<array[0].length; j++) {
                System.out.print("[ " + array[i][j] + " ]");
            }
            System.out.println("");
        }
    }    
}
