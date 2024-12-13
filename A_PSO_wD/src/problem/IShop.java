/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package problem;

import algorithm.Individual;
import algorithm.Utils;
import problem.Problem;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 *
 * @author 
 */
public class IShop implements Problem {
    
    public int m,n;
    int DeliveryMin;
    double pij[][];
    double fj[];
    String archivo;
    int[] sol_i;
    double min;    
    int[] X;
    int[] Xc;
    double[] costos;
    double[] costoxp;
    double[] ccostoxp;
    double oldTotalCost; 
    int lower;
    int upper;
    String problemName;
    static int contEval=0;
    int nVars;
    int nObjectives;
    
    public IShop() {

        problemName = "IShOP";
        readInstance("50n240m_30.txt");
        nVars = n; // 10
        lower = 0;
        upper = m-1;
        
    }

    @Override
    public void readInstance(String archivo) {
        try {
                Scanner leer = new Scanner(new File(archivo));
                n = leer.nextInt();//Numero de productos
                m = leer.nextInt();//Numero de tiendas
                pij=new double[m][n];//matriz de costos tiendaXproducto
	        fj=new double[m]; //vector de costos de envío            
                //llenar matriz de costos de productos por cada tienda
                for(int i=0;i<m;i++)
                {
                   for(int j=0;j<n;j++)
                   {
                     pij[i][j]=leer.nextDouble(); 
                   }
       	        }
                
	        //llenar vector de costos de envío por tienda
	        for(int k=0;k<m;k++)
	        {
		       fj[k]=leer.nextDouble();		
	        }
            } 
            catch (FileNotFoundException ex) 
            {
               System.out.println("No se pudo leer el archivo...");
            }
    }
    
    @Override
    public void costFunction(Individual ind) {
        int n = ind.getPosition().length;
        int pos;
        double sum=0;
        double costo=0.0,envio,d;
        int dt,adt;
        for(int i=0; i<n; i++) 
        {
            costo+=pij[ind.getPosition()[i]][i];
        }
        envio=calcular_envio(ind);
        d=descuento(costo+envio);
        //sum = costo+envio;
        
        ind.setCost(d);
    }
    
    //Calcular el costo de lista de productos
public double descuento(double psd)
{
	double tdesc=0.0;
	if (psd<25)
          tdesc=psd;
        if (psd>25 && psd<=50)
          tdesc=psd*0.95;
        if (psd>50 && psd<=100)
          tdesc=psd*0.90;
        if (psd>100 && psd<=200)
          tdesc=psd*0.85;
        if (psd>200)
          tdesc=psd*0.80;
	
    return tdesc;        
}
    
public double calcular_envio(Individual ind)
{
	double envio=0.0;
        int[] visitada=new int[m];
        for (int i=0;i<n;i++)
        {
                for (int j=0;j<m;j++)
                {
                    if (j==ind.getPosition()[i])
                    visitada[j]=1;
                }
        }
        for (int i=0;i<m;i++)
        {
            if (visitada[i]!=0){
                envio+=fj[i];
            }
        }
	return envio;
}

public int[] FB(int[] Xn)
{
    int[] Xc=new int[Xn.length];
    double oldTotalCost;
    Xc = Xn;
    oldTotalCost=FX(Xc);
    int iprima=0;
    for(int i=0;i<n;i++)
    {
        for(int j=0;j<m;j++)
        {
            Xc[i]=j;
            if(FX(Xc)<=oldTotalCost)
            {
                oldTotalCost=FX(Xc);
                iprima=j;
            }
        }
        Xc[i]= iprima;
    }
return Xc;
}

//public int[] FP(int[] Xn)
//{
//    int[] Xc=new int[Xn.length];
//    double oldTotalCost;
//    Xc = Xn;
//    oldTotalCost=FX(Xc);
//    int iprima=0;
//    for(int i=0;i<Utils.n;i++)
//    {
//        for(int j=0;j<m;j++)
//        {
//            Xc[i]=j;
//            if(FX(Xc)<=oldTotalCost)
//            {
//                oldTotalCost=FX(Xc);
//                iprima=j;
//                break;        
//            }
//            
//        }
//        Xc[i]= iprima;
//    }
//return Xc;
//}

public double FX(int[] lista)
{
	double costo=0.0,envio,d;
	for (int i=0;i<n;i++)
        {
            costo+=pij[lista[i]][i];
        }
        envio=enviado(lista);
        d=descuento(costo+envio);
	return d;        
}
//
public  double enviado(int[] lenvio)
{
	double envio=0.0;
        int[] visitada=new int[m];
	
        for (int i=0;i<n;i++)
        {
                for (int j=0;j<m;j++)
                {
                    if (j==lenvio[i])
                    visitada[j]=1;
                }
        }
        for (int i=0;i<m;i++)
        {
            if (visitada[i]!=0){
                envio+=fj[i];
            }
        }
	return envio;
        
}

    @Override
    public int getLoweBound() {
        return this.lower;
    }

    /**
     *
     * @return
     */
    @Override
    public int getUpperBound() {
        return this.upper;
    }

    @Override
    public int getnVars() {
        return nVars;
    }

    @Override
    public String getName() {
        return this.problemName;
    }

}
