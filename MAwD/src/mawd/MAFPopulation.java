/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mawd;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.UUID;


/**
 *
 * @author MAGM
 */
public class MAFPopulation {
    //Declara variables globales.
    public int[][] instancia=new int[2][1000];
    public int numero_variables,peso_maximo;
    public int[][] poblacion=new int[100][1000];
    public double cij[][];
    public double di[];
    public String archivo;
    public float[][] porcencruzamuta=new float[2][36];
    public int N;//Cantidad de productos
    public int M;//Cantidad de tiendas 
    public int[] sol_i;
    public int [][] pini;
    public double[] cxind,cint;
    public int [][] NewPop;
    public int [][] InterMediatePop;
    public int [][] ChildPop;
    public int [][] nova;
    public double[] costxind;
    public int[] Xc;
    public long InitTime = 0, EndTime = 0;
    public double tiempo,oldTotalCost;
    public int[] sl,si,solution;
    public int popini=100; //Número de individuos en la población inicial
    public double pc=0.60,pm=0.01,er=0.05,nc=0.10,bestcost;
    public double percent,pcruza;
    public double [][] orden;
    public double [] ccruza;
    public int[] mejorsol;
    
    public MAFPopulation(String arc) {
        this.archivo = arc;
    }

    /**
     * @param args the command line arguments
     */
    
    //Lee los datos necesarios de las instancias.
//**********************************************************************
public void readFile(String archivo) {
        try {
           
            Scanner leer = new Scanner(new File(archivo));
            N = leer.nextInt();//Numero de productos
            M = leer.nextInt();//Numero de tiendas

            
        cij=new double[M][N];//matriz de costos tiendaXproducto
	di=new double[M]; //vector de costos de envío            
            //llenar matriz de costos de productos por cada tienda
            for(int i=0;i<M;i++)
            {
                for(int j=0;j<N;j++)
                {
                    cij[i][j]=leer.nextDouble();
                }
       	    }
	
	//llenar vector de costos de envío por tienda
	for(int k=0;k<M;k++)
	{
		di[k]=leer.nextDouble();		
	}
        } catch (FileNotFoundException ex) {
            System.out.println("No se pudo leer el archivo...");
        }
    }

//Generar solución inicial
public void sol_ini()
{
    sol_i=new int[N];
    for (int i=0;i<N;i++)
    {
        sol_i[i]=(int) (Math.random() * (M-1)) + 1;
    }
}

//imprimir el vector con la solución inicial
public void imprimesolini()
{
    for (int i=0;i<N;i++)
    {
        System.out.println("Producto: "+i+" Tienda: "+sol_i[i]+" ");
    }
}

//Calcular el costo de lista de productos
public double FX(int[] lista)
{
	double costo=0.0,envio;
	for (int i=0;i<N;i++)
        {
            costo+=cij[lista[i]][i];
        }
        envio=calcular_envio(lista);
	return costo+envio;        
}

//Calcular el costo de envío
public double calcular_envio(int[] lenvio)
{
	double envio=0.0;
        int[] visitada=new int[M];
	
        for (int i=0;i<N;i++)
        {
                for (int j=0;j<=M;j++)
                {
                    if (j==lenvio[i])
                    visitada[j]=1;
                }
        }
        for (int i=0;i<M;i++)
        {
            if (visitada[i]!=0){
                envio+=di[i];
            }
        }
	return envio;
        
}

//Genera la poblacion inicial y Cálcula costo por individuo de población inicial
//**********************************************************************
public void generar()
{
    pini=new int[popini][N];
    sl=new int[N];
    cxind=new double[popini];
    double cp=0;
    for(int i=0;i<popini;i++)
    {
        for(int j=0;j<N;j++)
        {
            sl[j]=(int) (Math.random() * (M-1)) + 1;    
        }
      //  if (i<=((popini/2)-1))
      //  {
            cp=FX(sl);
            sl=FP(sl,cp);
            generarpinicial(sl,i);
      //  }
//        else
//        {
//            cp=FX(sl);
//            sl=FB(sl,cp);
//            generarpinicial(sl,i);
//        }
       cxind[i]=FX(sl);
       
    }    
}

//Cálcula costo por individuo de población modificada
//**********************************************************************
public double[] costpopulation(int[][] poblacion)
{
    solution=new int[N];
    costxind=new double[poblacion.length];
    for(int i=0;i<poblacion.length;i++)
    {
        for(int j=0;j<N;j++)
        {
            solution[j]=poblacion[i][j];    
        }
        costxind[i]=FX(solution);
       
    }
return costxind;      
}


public void generarpinicial(int[] aux,int vin)
{
    int j;
        for(j=0;j<N;j++)
        {
            pini[vin][j]= sl[j];
        }
}

   
//Heuristica FirstBest
//**********************************************************************
public int[] FB(int[] Xn,double cst)
{
    Xc = Xn;
    oldTotalCost=cst;
    int iprima=0;
    for(int i=0;i<N;i++)
    {
        for(int j=0;j<M;j++)
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

//Heuristica FirstPlus
//**********************************************************************
public int[] FP(int[] Xn,double cst)
{
    Xc = Xn;
    oldTotalCost=cst;
    int iprima=0;
    for(int i=0;i<N;i++)
    {
        for(int j=0;j<M;j++)
        {
            Xc[i]=j;
            if(FX(Xc)<=oldTotalCost)
            {
                oldTotalCost=FX(Xc);
                iprima=j;
                break;        
            }
            
        }
        Xc[i]= iprima;
    }
return Xc;
}

public double mejorcosto(double[] cost)
{
    double menor=cost[0];
    int fila;
    mejorsol=new int[N];
    for(int i = 0; i < cost.length - 1; i++)
        {
            if(cost[i]<menor)
            {
                menor=cost[i];
                fila=i;
                for(int j=0;j<N;j++)
                {
                    mejorsol[j]=pini[i][j];
                }
            }
        }
    
    
    return menor;
}

public int[] bintour(double[] torneo)
{
    int despues=1;
    int[] indices=new int[popini];
    for(int i=0;i<popini;i++)
    {
        if(i==(popini-1))
        {
          if(torneo[i]<torneo[0])
          {
              indices[i]=i;
          }
          else
          {
              indices[i]=0;
          }
        }
        else if(torneo[i]<torneo[despues])
                {
                    indices[i]=i;
                }
                else
                {
                    indices[i]=despues;
                }
        despues++;
    }
    return indices;
}

public double[] NPop(int[] population)
{
    si=new int[N];
    cint=new double[popini];
    NewPop=new int[population.length][N];
    for (int i=0;i<population.length;i++)
    {
        for(int j=0;j<N;j++)
        {
            NewPop[i][j]=pini[population[i]][j];
            si[j]=NewPop[i][j];
        }
        cint[i]=FX(si);
    }
    return cint;
}

public int[][] IPop(double[] inter)
{
    //Preparar los costos para ser ordenados y despues obtener el 5% de la población
    orden=new double[inter.length][2];
    InterMediatePop=new int[inter.length][N];
    
    for (int i=0;i<inter.length;i++)
    {
        orden[i][0]=i;
        orden[i][1]=inter[i];
    }
    //Ordenar los costos y obtener la población elite
    ordenarMatriz(orden,1);
    //Calcular el porcentaje de la población que será Elite
    percent=inter.length*er;
    int so;
    for(int i=0;i<percent;i++)
    {
        so=(int)orden[i][0];
        for(int j=0;j<N;j++)
        {
            InterMediatePop[i][j]=NewPop[so][j];
        }
    }
    
    return InterMediatePop;
}

public void ordenarMatriz(double[][] array, int columnaOrden) 
    {
        java.util.Arrays.sort(array, new java.util.Comparator<double[]>() 
        {
            public int compare(double[] a, double[] b) 
            {
                return Double.compare(a[columnaOrden], b[columnaOrden]);
            }
        });
    }

public int[][] cruza(double z)
{
    //Calcular el porcentaje de cruza y obtener individuos que se cruzaran
    pcruza=(orden.length-percent)*pc;
    int[] val=new int[(int)pcruza];
    int gen;
    int os;
    int oss;
       for(int i=0;i<pcruza;i++)
       {
           boolean ban=false;
           gen=(int) Math.floor(Math.random()*((orden.length-1)-percent+1)+percent);
           for(int j=0;j<pcruza;j++)
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
       //Llenar IntermediatePop con los individuos que se cruzaran
    int k=(int)percent;   
    for(int i=0;i<val.length;i++)
    {
        os=val[i];
        for(int j=0;j<N;j++)
            {
                InterMediatePop[k][j]=NewPop[os][j];
            }
        k++;
    }
    //Calcular los individuos restantes que se copiaran tal cual
    int actual=(int)percent;
    boolean bnd=false;
    int[] faltantes=new int[orden.length-(val.length+(int)percent)];
    for(int i=0;i<faltantes.length;i++)
    {
        if(bnd==true)
        {i--;}
        for(int j=actual;j<orden.length;j++)
        {
            boolean band=false;
            for(int l=0;l<val.length;l++)
            {
                if(j==val[l])
                {
                    band=true;
                    break;
                }
            }
            if(band==false)
            {
              faltantes[i]=j;
              actual++;
              bnd=false;
              break;
            }
            else
            {
              bnd=true;
              actual++;
            }
        }
    }
    
    //Terminar de llenar InterMediatePop con los individuos restantes de NewPop (Estos no se cruzaran)
    int tl=(int)pcruza+(int)percent;   
    for(int i=0;i<faltantes.length;i++)
    {
        oss=faltantes[i];
        for(int j=0;j<N;j++)
            {
                InterMediatePop[tl][j]=NewPop[oss][j];
            }
        tl++;
    }
    
    //Aplicar el operador de cruza a IntermediatePop
      //Calcular el costo de los padres que se cruzaran
      // ccruza=new double[val.length];
      //  int[] ic=new int[N];
      //  double price;
      //  int y=0;
      //  for (int i=(int)percent;i<(val.length+(int)percent);i++)
      //  {
      //      for(int j=0;j<N;j++)
      //      {
      //          ic[j]=InterMediatePop[i][j];
      //      }
      //      price=FX(ic);
      //      ccruza[y]=price;
      //      y++;
      //  }
        
        //Cruzar los individuos
        int[] child1=new int[N],child2=new int[N];
        int[] parent1=new int[N],parent2=new int[N];
        double dividir=N;
        int h1=(int)percent,h2=h1+1,mitad=(int)Math.round(dividir/2);
        
        double cp1,cp2,pc1,pc2;
        for (int i=(int)percent;i<=((val.length+(int)percent)/2);i++)
        {
            //Obtener los padres
            for(int j=0;j<N;j++)
            {
                parent1[j]=InterMediatePop[h1][j];
                parent2[j]=InterMediatePop[h2][j];
            }
            //Obtener los hijos
            for(int m=0;m<N;m++)
            {
                if(m<mitad){
                   child1[m]=InterMediatePop[h1][m];
                   child2[m]=InterMediatePop[h2][m];
                }else{
                    child1[m]=InterMediatePop[h2][m];
                    child2[m]=InterMediatePop[h1][m];
                }
                
            }
            pc1=FX(parent1);
            pc2=FX(parent2);
            cp1=FX(child1);
            cp2=FX(child1);
            if (cp1<pc1)
            {
               replaceparent(child1,h1); 
            }
            if(cp2<pc2)
            {
               replaceparent(child2,h2); 
            }
            
            h1=h1+2;
            h2=h1+1;
        }
       
    return InterMediatePop;
}

public void replaceparent(int[] hijo,int hi)
{
    for (int i=0;i<hijo.length;i++)
    {
        InterMediatePop[hi][i]=hijo[i];
    }
}

public int[][] mutacion()
{
    int[] mutara=new int[N];
    double alea;
    for(int i=(int)percent;i<InterMediatePop.length;i++)
    {
        for(int j=0;j<N;j++)
        {
            mutara[j]=InterMediatePop[i][j];
        }
      
        alea = Math.random()*(0.05);
        
        if(alea<pm)
        {
            mutara=FB(mutara,FX(mutara));
            for(int k=0;k<N;k++)
            {
              InterMediatePop[i][k]=mutara[k];  
            }
        }
    }
    return InterMediatePop;
}

public int[][] LS()
{
    int[] mejorar=new int[N];
    ChildPop=new int[InterMediatePop.length][N];
    for(int i=0;i<InterMediatePop.length;i++)
    {
        for (int j=0;j<N;j++)
        {
            mejorar[j]=InterMediatePop[i][j];
        }
        mejorar=FB(mejorar,FX(mejorar));
        
        for(int k=0;k<N;k++)
            {
              ChildPop[i][k]=mejorar[k];  
            }
    }
  return ChildPop;  
}

public void resetpopulation(int[] bestilocal)
{
    //nova=new int[pini.length][N];
    sl=new int[N];
    cxind=new double[popini];
    double cp=0;
    pini[0]=bestilocal;
    for(int i=1;i<popini;i++)
    {
        for(int j=0;j<N;j++)
        {
            sl[j]=(int) (Math.random() * (M-1)) + 1;    
        }
            cp=FX(sl);
            sl=FP(sl,cp);
            generarpinicial(sl,i);

       cxind[i]=FX(sl);
       
    }
}

    public static void main(String[] args) throws FileNotFoundException, IOException {
        // TODO code application logic here
        String archivo;
        int[] fnpop;
        int[] bestlocal,bestglobal;
        double[] InterPop;
        double bestglobalcost;
        double besttime=0;
        double timelimit=0.001840843;
        Scanner teclado = new Scanner(System.in);
        MAFPopulation obj=new MAFPopulation("3n20m_2.txt");
        obj.readFile(obj.archivo);
          int contando=1;
while(contando<=30)
{

        //Generar la población inicial
        obj.generar();
        obj.InitTime = System.currentTimeMillis();
        //Obtener el mejor costo de la población inicial y la mejor solución
        obj.bestcost=obj.mejorcosto(obj.cxind);
        bestlocal=obj.mejorsol;
        bestglobal=bestlocal;
        bestglobalcost=obj.bestcost;
        obj.EndTime=System.currentTimeMillis();
        obj.tiempo = (obj.EndTime - obj.InitTime)/1000.;
        besttime=obj.tiempo;
        fnpop=new int[obj.popini];
        obj.InitTime = System.currentTimeMillis();
        
        while (obj.tiempo<timelimit)//0.001840843  
        {
            //Aplicar torneo binario
            fnpop=obj.bintour(obj.cxind);
            //Obtener NewPop
            InterPop=obj.NPop(fnpop);
            //Obtener IntermediatePop
            obj.IPop(InterPop);
            //Aplicar operador de Cruza y rellenar IntermediatePop
            obj.cruza(obj.percent);
            //Aplicar operador de Mutación
            obj.mutacion();
            //Aplicar operador de Mejora Local
            obj.pini=obj.LS();
            //Calcular el Mejor local
            obj.bestcost=obj.mejorcosto(obj.costpopulation(obj.pini));
            bestlocal=obj.mejorsol;
            if(obj.bestcost<bestglobalcost)
            {
                bestglobal=bestlocal;
                bestglobalcost=obj.bestcost;
                obj.EndTime=System.currentTimeMillis();
                obj.tiempo = (obj.EndTime - obj.InitTime)/1000.;
                besttime=obj.tiempo;
            }
            //Solo dejar el mejor de la población y recrear los restantes
            obj.resetpopulation(bestlocal);
            obj.EndTime=System.currentTimeMillis();
            obj.tiempo = (obj.EndTime - obj.InitTime)/1000.;
        }
        //System.out.println("Mejor Global");
//        for(int i=0;i<obj.N;i++)
//        {
//            System.out.println("Tienda: "+bestglobal[i]+" Producto:"+i);
//        }
        //System.out.println("Costo: "+bestglobalcost);
        //System.out.println("Tiempo: "+besttime);
        System.out.println(contando+";"+bestglobalcost+";"+besttime);
contando++;
    
}
}
}
