/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package algorithm;

import java.util.LinkedList;
import problem.Problem;
import problem.IShop;

/**
 *
 * @author
 */
public class PSO {

    Problem problem;
    int nVar;
    int varMin;
    int varMax;
    int MaxIt; // Maximum Number of Iterations
    int nPop; // Population Size (Swarm Size)
    // PSO Parameters
    double w; // Inertia Weight
    double wdamp; // Inertia Weight Damping Ratio
    double c1; // Personal Learning Coefficient
    double c2; // Global Learning Coefficient
    double velMin;
    double velMax;
    LinkedList<Particle> particles;
    Particle GlobalBest;
    long InitTime = 0, EndTime = 0;
    double tiempo;
    
    public PSO() {
        problem = new IShop();
        System.out.println("Problem: " + problem.getName());
        nVar = problem.getnVars();
        varMin = problem.getLoweBound();
        varMax = problem.getUpperBound();
        MaxIt = 100;
        nPop = 100;
        w = 1;
        wdamp = 0.99;
        c1 = 1.5;
        c2 = 2.0;
        // Velocity Limits
        velMax = calculateVelMax(0.2,varMax, varMin); //0.1*(varMax-varMin);
        velMin = -velMax;
        particles = new LinkedList<Particle>();
        GlobalBest = new Particle();
        double initialCost=0;
        initialCost = Double.POSITIVE_INFINITY;
        GlobalBest.setBestCost(initialCost);
        GlobalBest.setCost(initialCost);
    }
    
    public void execute() {
        this.generateParticles();
        //LS(GlobalBest);
        // Utils.printList3(particles);
        double[] bestCost = new double[this.MaxIt];
        //IOBL();
        for(int it=0; it<this.MaxIt; it++) {
            InitTime = System.currentTimeMillis();
            for(int i=0; i<this.nPop; i++) {
                // Actualizar Velocidad
                for(int j=0; j<this.nVar; j++) {
                    this.particles.get(i).getVelocity()[j] = Math.round( w * this.particles.get(i).getVelocity()[j] 
                            + c1 * Math.random() * (particles.get(i).getBestPosition()[j] - particles.get(i).getPosition()[j]) 
                            + c2 * Math.random() * (this.GlobalBest.getPosition()[j] - particles.get(i).getPosition()[j]) );
                }
                
                // Ajustar Límites de Velocity
                for(int j=0; j<this.nVar; j++) {
                    particles.get(i).getVelocity()[j] = Math.max(particles.get(i).getVelocity()[j], this.velMin);
                    particles.get(i).getVelocity()[j] = Math.min(particles.get(i).getVelocity()[j], this.velMax);
                }
                
                //Actualizar Posición
                for(int j=0; j<this.nVar; j++) {
                    particles.get(i).getPosition()[j] = (int) (particles.get(i).getPosition()[j] + particles.get(i).getVelocity()[j]);
                }

                //Ajustar Limites de las posiciones
                for(int j=0; j<this.nVar; j++) {
                    particles.get(i).getPosition()[j] = (int) Math.max(particles.get(i).getPosition()[j], varMin);
                    particles.get(i).getPosition()[j] = (int) Math.min(particles.get(i).getPosition()[j], varMax);
                }
                                
                //Evaluation
                problem.costFunction(particles.get(i));
                
                //Búsqueda Local
                //LS(particles);
                
                
                // Update Personal Best
                if(particles.get(i).getCost() < particles.get(i).getBestCost()) {
                    particles.get(i).setBestPosition((particles.get(i).getPosition()));
                    particles.get(i).setBestCost(particles.get(i).getCost());
                    // Update Global Best
                    if(particles.get(i).getBestCost() < this.GlobalBest.getCost() ) {
                         //this.GlobalBestCost = particles.get(i).getBestCost()[0];
                         this.GlobalBest.clone(particles.get(i));
                         EndTime=System.currentTimeMillis();
                         tiempo = (EndTime - InitTime)/1000.;
                    }
                }
            //w = w * wdamp;    
            }
            IOBL();
            LS(GlobalBest);
            bestCost[it] = this.GlobalBest.getCost();
            w = w * wdamp;
        }
        //LS(GlobalBest);
        //Utils.printMatrix(bestCost);
       // Plot.plotMTXAG(bestCost,"PSO");
       // System.out.println("");
       System.out.print(GlobalBest.getBestCost());
       System.out.println(";"+tiempo);
    }
    
    public void generateParticles() {
        for(int i=0; i<this.nPop; i++) {
            Particle particle = new Particle();
            particle.setPosition(Utils.getRandomPos(varMax, nVar));
            particle.setVelocity(zeros(problem.getnVars()));
            
            problem.costFunction(particle);
            //
            particle.setBestPosition(particle.getPosition());
            particle.setBestCost(particle.getCost());
            
            if(particle.getBestCost() < this.GlobalBest.getCost()) {
                this.GlobalBest.clone(particle);
            }
            
            this.particles.add(particle);
        }
    }
    
    public void LS(Particle current) {
       int[] auxx=new int[nVar];
       int[] aux=new int[nVar];
            for(int j=0; j<this.nVar; j++) {
               aux[j]=current.getPosition()[j];
            }
            
            auxx=problem.FB(aux);

            //Actualizar posición con LS
            for(int j=0; j<this.nVar; j++) {
                    current.getPosition()[j]=auxx[j];
            }
            
            //Evaluation
                problem.costFunction(current);
                
                // Update Personal Best
                if(current.getCost() < current.getBestCost()) {
                    current.setBestPosition((current.getPosition()));
                    current.setBestCost(current.getCost());
                    // Update Global Best
                    if(current.getBestCost() < this.GlobalBest.getCost() ) {
                         //this.GlobalBestCost = particles.get(i).getBestCost()[0];
                         this.GlobalBest.clone(current);
                         EndTime=System.currentTimeMillis();
                         tiempo = (EndTime - InitTime)/1000.;
                    }
                }       
        
    
}
    
//    public void LS(Particle current) {
//        
//       int[] auxx=new int[nVar];
//        
//       for(int i=0; i<this.nPop; i++) {
//             
//            int[] aux=new int[nVar];
//            for(int j=0; j<this.nVar; j++) {
//               aux[j]=particles.get(i).getPosition()[j];
//            }
//            
//            auxx=problem.FB(aux);
//
//            //Actualizar posición con LS
//            for(int j=0; j<this.nVar; j++) {
//                    particles.get(i).getBestPosition()[j]=auxx[j];
//            }
//            
//            //Evaluation
//                problem.costFunction(particles.get(i));
//                
//                // Update Personal Best
////                if(particles.get(i).getCost()[0] < particles.get(i).getBestCost()[0]) {
////                    particles.get(i).setBestPosition((particles.get(i).getPosition()));
////                    particles.get(i).setBestCost(particles.get(i).getCost());
////                    // Update Global Best
////                    if(particles.get(i).getBestCost()[0] < this.GlobalBest.getCost()[0] ) {
////                         //this.GlobalBestCost = particles.get(i).getBestCost()[0];
////                         this.GlobalBest.clone(particles.get(i));
////                    }
////                }       
//        
//    }
//}    
    


    public void IOBL() {
        
        for(int i=0; i<this.nPop; i++) {
            int aux;
            double novo = 0.0,act;
            int alea;
            alea=(int)Math.floor(Math.random()*(varMax-0+1)+0);
            for(int j=0; j<this.nVar; j++) {
            
                //alea=(int)Math.floor(Math.random()*(varMax-0+1)+0);
                //aux= varMin+varMax-alea;
                //aux= particles.get(i).getPosition()[j]*alea;
                //aux= (int)particles.get(i).getBestPosition()[j]-alea;
               // novo = [aux][j];
               if (i%2==0) 
                  aux=varMin+varMax-(particles.get(i).getBestPosition()[j]+alea);
               else           
                  aux=varMin+varMax-(particles.get(i).getBestPosition()[j]-alea);
                //if (novo<act)
                    particles.get(i).getPosition()[j]=aux;
            }
            
            //Ajustar Limites de las posiciones
                for(int j=0; j<this.nVar; j++) {
                    particles.get(i).getPosition()[j] = (int) Math.max(particles.get(i).getPosition()[j], varMin);
                    particles.get(i).getPosition()[j] = (int) Math.min(particles.get(i).getPosition()[j], varMax);
                }

            //Evaluation
                problem.costFunction(particles.get(i));
                if(particles.get(i).getCost() < particles.get(i).getBestCost()) {
                    particles.get(i).setBestPosition((particles.get(i).getPosition()));
                    particles.get(i).setBestCost(particles.get(i).getCost());
                    // Update Global Best
                    if(particles.get(i).getBestCost() < this.GlobalBest.getCost() ) {
                         //this.GlobalBestCost = particles.get(i).getBestCost()[0];
                         this.GlobalBest.clone(particles.get(i));
                         EndTime=System.currentTimeMillis();
                         tiempo = (EndTime - InitTime)/1000.;
                    }
                }
        }
    }
  
   
    public double[] zeros(int n) {
        double[] a = new double[n];
        for(int i=0; i<n; i++) {
            a[i] = 0.0;
        }
        return a;
    }
    
    public double calculateVelMax(double v, double l, double  u) {
        //0.02 * (varMax - varMin)
        double fw = 0;
        fw=v*(l-u);
        return Math.round(fw);
    }
}
