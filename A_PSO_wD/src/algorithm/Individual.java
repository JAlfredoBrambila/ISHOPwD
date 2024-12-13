/**
 * Individual.java
 * @version: 0.1
 * @autor: J. Alfredo Brambila H. <alfredo.brambila@outlook.com>
 * @autor: Miguel Angel Garcia Morales <talivan12@hotmail.com>
 * @autor: Hector Fraire Huacuja <hector.fraire2014@gmail.com>
 * Proyecto Biblioteca de clases para MO-JFramework 
 * Enero de 2022
 * Puede hacer uso total o parcial de este codigo dandole credito a los autores
 */

package algorithm;

import java.util.LinkedList;

/**
 *
 * @author 
 */
public class Individual {
    
    private int[] position;
    private double cost;
    
    public Individual() {
        
    }
   
    public void clone(Individual a) {
        this.position = new int[a.getPosition().length];
        for(int i=0; i<a.getPosition().length; i++) {
            this.position[i] = a.getPosition()[i];
        }
        this.cost = a.getCost();
    }
    
    
    public Individual(int nPos, int nCos) {
        this.position = new int[nPos];
        this.cost = nCos;
    }
    
    /**
     * @return the position
     */
    public int[] getPosition() {
        return position;
    }
        
    public void setCost(double value) {
        this.cost = value;
    }
    
    public void setPosition(int i, int value) {
        this.position[i] = value;
    }
    
    

    /**
     * @param position the position to set
     */
    public void setPosition(int[] position) {
        this.position = position;
    }

    /**
     * @return the cost
     */
    public double getCost() {
        return cost;
    }
        
}
