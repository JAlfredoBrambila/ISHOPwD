/**
 * Problem.java
 * @version: 0.1
 * @autor: J. Alfredo Brambila H. <alfredo.brambila@outlook.com>
 * @autor: Miguel Angel Garcia Morales <talivan12@hotmail.com>
 * @autor: Hector Fraire Huacuja <hector.fraire2014@gmail.com>
 * Proyecto Biblioteca de clases para MO-JFramework 
 * Enero de 2022
 * Puede hacer uso total o parcial de este codigo dandole credito a los autores
 */
package problem;

import algorithm.Individual;

/**
 *
 * @author 
 */
public interface Problem {
    public void costFunction(Individual ind);
    public int getLoweBound();
    public int getUpperBound();
    public int getnVars();
    public String getName();
    public double calcular_envio(Individual ind);
    public void readInstance(String archivo);
    public int[] FB(int[] Xn);
}
