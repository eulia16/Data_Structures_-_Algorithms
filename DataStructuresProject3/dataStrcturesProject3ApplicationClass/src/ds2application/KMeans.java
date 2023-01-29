
package ds2application;

//This will take in the candidates(their points), and compute the distances of each 
public class KMeans {
    
     public double returnMeans(double[] centroids, int length){
        double mean=0, value=0, counter=0;
         for(int i=0; i<length;++i){
             if(centroids[i] != 0){
             value +=centroids[i];
             counter++;
             }
             else
                 continue;
         }
         mean = value/counter;
         return mean;
     }
    
}
