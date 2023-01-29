
package ds2application;

import java.util.Random;
import javax.swing.JFrame;


public class Mediod {
  private double numberOfMediods=0, maximum=0;
  
  public Mediod(int n, int x){
      numberOfMediods = n;
      maximum = x;
  }

    public double randomMediods(double[] centroids){
        int a=0, min=1;
        double answer=0;
        //Random random = new Random();
        a = (int)(Math.random()*(maximum-min)+min);
        //a = random.nextInt(maximum);
        answer = centroids[a];
        return answer;
    }  
    
    public double[][] getCentroid(double data[],double centroid[][]){
    double numclusters = numberOfMediods;
    double distance[][]=new double[(int)numclusters][data.length];//[(int) numclusters][data.length];
    double cluster[]=new double[data.length];
    double clusternodecount[]=new double[(int) numclusters];
    boolean done = true;

    centroid[0]=centroid[1];
    centroid[1]=new double[]{0,0,0,0,0};
    System.out.println("************* Starting to get new centroid *************");

    //computing the distances
    for(int i=0;i<numclusters;i++){
            for(int j=0;j<data.length;j++){
                //if(data[j] != 0){
                    distance[i][j]=Math.abs(data[j]-centroid[0][i]);
                    System.out.println(distance[i][j]+",");
                //}
                    //System.out.println("Centroid: "+centroid[0][i]);
            }
            System.out.println();
    }

    for(int j=0;j<data.length;j++){
            int smallerDistance=0;
            if(distance[0][j]<distance[1][j] && distance[0][j]<distance[2][j] 
                    && distance[0][j] < distance[3][j] && distance[0][j] < distance[4][j])
                    smallerDistance=0;
            if(distance[1][j]<distance[0][j] && distance[1][j]<distance[2][j]
                    && distance[1][j] < distance[3][j] && distance[1][j] < distance[4][j])
                    smallerDistance=1;
            if(distance[2][j]<distance[0][j] && distance[2][j]<distance[1][j]
                    && distance[2][j] < distance[3][j] && distance[2][j] < distance[4][j])
                    smallerDistance=2;//
            if(distance[3][j]<distance[0][j] && distance[3][j]<distance[1][j]
                    && distance[3][j] < distance[2][j] && distance[3][j] < distance[4][j])
                    smallerDistance=3;
            if(distance[4][j]<distance[0][j] && distance[4][j]<distance[1][j]
                    && distance[4][j] < distance[2][j] && distance[4][j] < distance[3][j])
                    smallerDistance=4;
            

            centroid[1][smallerDistance]=centroid[1][smallerDistance]+data[j];
            clusternodecount[smallerDistance]=clusternodecount[smallerDistance]+1;
            cluster[j]=smallerDistance;


    }
   

    System.out.println("********************************************************");
    System.out.println("Final Centroids: ");

    //computing the mean
    for(int j=0;j<numclusters;j++){
        //prevent division by 0
        if(clusternodecount[j] == 0){
        clusternodecount[j] = 3;
        }
            centroid[1][j]=centroid[1][j]/clusternodecount[j];
            System.out.println(centroid[1][j]+",");
        
    }
    
    System.out.println("********************************************************");
    
   

    for(int j=0;j<numclusters;j++){
            if(done && centroid[0][j] == centroid[1][j]){
                    done=true;
                    continue;
            }
            done=false;
    }
    double[][] dataToPass = new double[5][data.length];

    if(!done){

            getCentroid(data,centroid);
    }
    else{
            System.out.println("********************************************************");
            System.out.println(" Final Cluster: ");
            for(int i=0;i<numclusters;i++){	
                  System.out.print("Cluster "+(i+1)+":");
                for(int j=0;j<data.length;j++){
                    if(cluster[j]==i){
                        dataToPass[i][j] = data[j];
                    System.out.print(data[j]+" ,");
                    }
                    }
                System.out.println();
            }
    }
    
    
    
    //we will now add values to the plane and show the mediods
//JFrame frame = new JFrame();
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        GUI gui = new GUI();
//        //double[] numbers = {1,2,4,5,2,1,2,4};
//        gui.setXCoordinates(dataToPass);
////        gui.setg1(dataToPass[0]);
////        gui.setg2(dataToPass[1]);
////        gui.setg3(dataToPass[2]);
////        gui.setg4(dataToPass[3]);
////        gui.setg5(dataToPass[4]);
//        frame.add(gui);
//        frame.setSize(1000,1000);
//        frame.setLocation(200,200);
//        frame.setVisible(true);
         
    return centroid;

    }
}

