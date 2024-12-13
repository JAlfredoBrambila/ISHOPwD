/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package gui;

import algorithm.APSO;

/**
 *
 * @author 
 */
public class MainA_PSO {
    public static void main(String[] args) {
        APSO algoritm = new APSO();
        for (int i=0;i<30;i++)
        {
            System.out.print((i+1)+";");
            algoritm.execute();
        }
    }
}
