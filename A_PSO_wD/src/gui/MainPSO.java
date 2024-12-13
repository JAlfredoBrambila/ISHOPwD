/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package gui;

import algorithm.PSO;

/**
 *
 * @author 
 */
public class MainPSO {
    public static void main(String[] args) {
        PSO algoritm = new PSO();
        for (int i=0;i<30;i++)
        {
            System.out.print((i+1)+";");
            algoritm.execute();
        }
    }
}
