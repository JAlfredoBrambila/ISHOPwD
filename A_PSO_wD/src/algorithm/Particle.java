/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package algorithm;

/**
 *
 * @author J. Alfredo Brambila H. <alfredo.brambila@outlook.com>
 */
public class Particle extends Individual{
    private double[] velocity;
    private int[] bestPosition;
    private double bestCost;
    public Particle() {
        
    }
    
    public void clone(Particle a) {
        super.clone(a);
        velocity = new double[a.getVelocity().length];
        for(int i=0; i<a.getVelocity().length; i++) {
            velocity[i] = a.getVelocity()[i];
        }
        //
        bestPosition = new int[a.getBestPosition().length];
        for(int i=0; i<a.getBestPosition().length; i++) {
            bestPosition[i] = a.getPosition()[i];
        }
        bestCost = a.getBestCost();
    }

    /**
     * @return the velocity
     */
    public double[] getVelocity() {
        return velocity;
    }

    /**
     * @param velocity the velocity to set
     */
    public void setVelocity(double[] velocity) {
        this.velocity = velocity;
    }

    /**
     * @return the bestPosition
     */
    public int[] getBestPosition() {
        return bestPosition;
    }

    /**
     * @param bestPosition the bestPosition to set
     */
    public void setBestPosition(int[] bestPosition) {
        this.bestPosition = new int[bestPosition.length];
        for(int i=0; i<bestPosition.length; i++) {
            this.bestPosition[i] = bestPosition[i];
        }
    }

    /**
     * @return the bestCost
     */
    public double getBestCost() {
        return bestCost;
    }

    /**
     * @param bestCost the bestCost to set
     */
    public void setBestCost(double bestCost) {
            this.bestCost = bestCost;
        
    }
}
