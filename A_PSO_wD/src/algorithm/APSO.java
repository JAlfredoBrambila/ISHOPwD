/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package algorithm;

import adaptative.AdaptativeModule1;
import adaptative.SlidingWindow;
import adaptative.Window;
import java.util.LinkedList;
import problem.Problem;
import problem.IShop;

/**
 *
 * @author
 */
public class APSO {

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
    
    //Adaptative
    int curSize; // Current window size
    int windowSize; // Length of the sliding window
    int numStrategies; // Number of operators
    
    int[] rank; // Rank values of operators
    int[] strategyUsage; // Number of usages of operators
    int[] strategySelected; // Indexes of the selected operators
    
    double scale; // Scale factor to control the Exploration vs. Exploitation in FRRMAB model
    double decayFactor; // Decay factor
    
    SlidingWindow slideWin;

    double[] reward; // Rewards of the applied operators
    double[] quality; // Quality of the applied operators
    double[] decayReward; // Decayed reward values
    double[] probability; // Selection probability
    double[] improvement; // Fitness improvement of each offspring
    double[][] slidingWindow; // Sliding window
    
    public APSO() {
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
        
        //Adaptative 0.5
        windowSize = (int) (0.8 * this.nPop);
        numStrategies = 6;
        scale = 5.0;
        decayFactor = 1.0;
        curSize = 0;
                
        strategyUsage = new int[numStrategies];
        decayReward = new double[numStrategies] ;
        reward = new double[numStrategies];
        rank = new int[numStrategies];
        strategyUsage = new int[numStrategies];
        strategySelected = new int[this.nPop];
        quality = new double[numStrategies];
        probability = new double[numStrategies];
        improvement = new double[this.nPop];
        slidingWindow = new double[2][windowSize];
        
        slideWin = new SlidingWindow(windowSize);
        
        for(int i=0; i<this.numStrategies; i++) {
            this.rank[i] = 0;
            this.decayReward[i] = 0.0;
        }
        
        for(int i=0; i<this.windowSize; i++) {
            this.slidingWindow[0][i] = -1;
            this.slidingWindow[1][i] = -1;
        }
        
        initParametersAOS();
        
    }
    
    public void execute() {
        
        // Adaptative
        int strategy_flag;
        int flag1, flag2, flag3, flag4;
        int uniform_flag, pre_flag, latter_flag;

        flag1 = flag2 = flag3 = flag4 = -1;
        uniform_flag = pre_flag = latter_flag = -1;
        // Adaptative
        
        this.generateParticles();
        //LS(GlobalBest);
        // Utils.printList3(particles);
        double[] bestCost = new double[this.MaxIt];
        //IOBL();
        for(int it=0; it<this.MaxIt; it++) {
            InitTime = System.currentTimeMillis();
            for(int i=0; i<this.nPop; i++) {
                
                ////////
                // Adaptative
                if (uniform_flag == -1) {
                    strategy_flag = (int) Math.ceil(Utils.getRandomNumber0_1() * this.numStrategies);
                    switch (strategy_flag) {
                        case 1:
                            flag1 = 1;
                            break;
                        case 2:
                            flag2 = 1;
                            break;
                        case 3:
                            flag3 = 1;
                            break;
                        case 4:
                            flag4 = 1;
                            break;
                    }

                    if (flag1 == 1 && flag2 == 1) {
                        pre_flag = 1;
                    }
                    if (flag3 == 1 && flag4 == 1) {
                        latter_flag = 1;
                    }
                    if (pre_flag == 1 && latter_flag == 1) {
                        uniform_flag = 1;
                    }
                } else {
                    strategy_flag = AdaptativeModule1.FRRMAB(quality, decayReward, strategyUsage, numStrategies, scale);
                }
                
                this.strategySelected[i] = strategy_flag;
                this.parameterPool(strategy_flag, i); //////////
                ////////
                
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
                
                    double f1 = particles.get(i).getBestCost();
                    double f2 = particles.get(i).getCost();
                    double fitnessimprovement = (f1-f2) / f1;
                
                
                // Update Personal Best
                if(particles.get(i).getCost() < particles.get(i).getBestCost()) {
                    particles.get(i).setBestPosition((particles.get(i).getPosition()));
                    particles.get(i).setBestCost(particles.get(i).getCost());
                    
                    this.improvement[i] = fitnessimprovement; // Adaptative
                    
                    // Update Global Best
                    if(particles.get(i).getBestCost() < this.GlobalBest.getCost() ) {
                         //this.GlobalBestCost = particles.get(i).getBestCost()[0];
                         this.GlobalBest.clone(particles.get(i));
                         EndTime=System.currentTimeMillis();
                         tiempo = (EndTime - InitTime)/1000.;
                    }
                }
                
                // Adaptative
                this.refreshment();
                
                Window current_element = new Window();
                current_element.setIndex(strategy_flag);
                current_element.setFitnessImprovement(this.improvement[i]);
                
                if (curSize < windowSize) {
                    slideWin.add(current_element);
                    curSize++;
                } else {
                    slideWin.removeElement(0);
                    slideWin.add(current_element);
                }
                slidingWindow = slideWin.slidingWindowToMtx();
                
                AdaptativeModule1.updateRewards(slidingWindow, reward, strategyUsage, curSize);
                AdaptativeModule1.rankRewards(reward, numStrategies, rank);
                AdaptativeModule1.CreaditAssignmentDecay(reward, decayReward, numStrategies, rank, decayFactor);
                
                // Adaptative
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
                if(current.getCost() < current.getBestCost()) 
                {
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

    public void IOBL() {
        for(int i=0; i<this.nPop; i++) 
        {
            int aux,alea;
            alea=(int)Math.floor(Math.random()*(varMax-0+1)+0);
            for(int j=0; j<this.nVar; j++) 
            {
               if (i%2==0) 
                  aux=varMin+varMax-(particles.get(i).getBestPosition()[j]+alea);
               else           
                  aux=varMin+varMax-(particles.get(i).getBestPosition()[j]-alea);
            
               particles.get(i).getPosition()[j]=aux;
            }
            
            //Ajustar Limites de las posiciones
                for(int j=0; j<this.nVar; j++) 
                {
                    particles.get(i).getPosition()[j] = (int) Math.max(particles.get(i).getPosition()[j], varMin);
                    particles.get(i).getPosition()[j] = (int) Math.min(particles.get(i).getPosition()[j], varMax);
                }

            //Evaluation
                problem.costFunction(particles.get(i));
                if(particles.get(i).getCost() < particles.get(i).getBestCost()) 
                {
                    particles.get(i).setBestPosition((particles.get(i).getPosition()));
                    particles.get(i).setBestCost(particles.get(i).getCost());
                    // Update Global Best
                    if(particles.get(i).getBestCost() < this.GlobalBest.getCost() ) 
                    {
                         this.GlobalBest.clone(particles.get(i));
                         EndTime=System.currentTimeMillis();
                         tiempo = (EndTime - InitTime)/1000.;
                    }
                }
        }
    }
    
    public void parameterPool(int strategySelected, int i) {
        //System.out.println("Selected: " + strategySelected);
        switch(strategySelected) {
            case 1:
                this.action1();
                break;
            case 2:
                this.action2();
                break;
            case 3:
                this.action3();
                break;
            case 4:
                this.action4();
                break;
            case 5:
                this.action5();
                break;
            case 6: 
                this.action6();
                break;
        }
    }
    
    public void action1() {
        c1 = c1 + 0.0001;
        c2 = c2 + 0.0001;
    }
    
    public void action2() {
        c1 = c1 - 0.0001;
        c2 = c2 - 0.0001;
    }
    
    public void action3() {
        c1 = c1 + 0.0001;
        //c2 = c1 + 0.0001;
    }
    
    public void action4() {
        c2 = c2 + 0.0001;
    }
    
    public void action5() {
        c1 = c1 - 0.0001;
    }
    
    public void action6() {
        c2 = c2 - 0.0001;
    }
    
    public void initParametersAOS() {
        for(int i=0; i<this.numStrategies; i++) {
            this.probability[i] = 1.0 / (double) (this.numStrategies);
            this.quality[i] = 0.0;
            this.reward[i] = 0.0;
            this.strategyUsage[i] = 0;
        }
    }
    
    public void refreshment() {
        for(int i=0; i<this.numStrategies; i++) {
            this.strategyUsage[i] = 0;
            this.decayReward[i] = 0.0;
            this.reward[i] = 0.0;
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
