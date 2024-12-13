/**
 * Utils.java
 * @version: 0.1
 * @autor: J. Alfredo Brambila H. <alfredo.brambila@outlook.com>
 * @autor: Miguel Angel Garcia Morales <talivan12@hotmail.com>
 * @autor: Hector Fraire Huacuja <hector.fraire2014@gmail.com>
 * Proyecto Biblioteca de clases para MO-JFramework 
 * Enero de 2022
 * Puede hacer uso total o parcial de este codigo dandole credito a los autores
 */

package algorithm;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;
import problem.IShop;

/**
 *
 * @author 
 */
public class Utils {
       
    
    public static void printMatrix(double[] mtx) {
       double menor=mtx[0];
        for (int i = 0; i < mtx.length; i++) {
                if (mtx[i]<menor){
                   menor=mtx[i];
                }
        }
        System.out.print(menor);
    }
    
    public static void printMatrixAddCol(double[][] mtx) {
        System.out.println("");
        for (int i = 0; i < mtx.length; i++) {
            for (int j = 0; j < mtx[0].length; j++) {
                System.out.print(mtx[i][j] + "\t");
            }
            System.out.print(mtx[i][mtx[0].length-2] + mtx[i][mtx[0].length-1]);
            System.out.println("");
        }
    }
    
    public static int getRandomIntNumber(int min, int max) {
        Random rand = new Random();
        return (int)(min+(max-min) * rand.nextDouble());
        //return (int) ((Math.random() * (max - min)) + min);
    }
    
    public static int getRandomIntNumberCeroToMax(int max) {
        Random rand = new Random();
        return rand.nextInt(max);
        //return (int) ((Math.random() * (max - min)) + min);
    }
    
    public static double getRandomDoubleNumber(double min, double max) {
        //return Math.random() * (max - min) + min;
        Random rand = new Random();
        return (min+(max-min) * rand.nextDouble());
    }
    
    
    public static int[] getRandomPos(int max, int nVar) {
        Random rand = new Random();
        int[] obj = new int[nVar];
        for(int i=0; i<nVar; i++) {
            
            //obj[i]=(int) (Math.random() * (max-1)) + 1;
            obj[i]= (int)Math.floor(Math.random()*(max-0+1)+0);
        }
        //obj=FP(obj);
               
        return obj;
    }
    
    public static double getRandomNumber0_1() {
        Random rand = new Random();
        return rand.nextDouble();
    }
    

}
