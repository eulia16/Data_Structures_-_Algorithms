
package ds2application;

//import java.lang.Math;
import static java.lang.Math.pow;



public class Distance {
    //This class will calculate the euclidean distanmce between two points, and the Hamming distance between two Strings
    
    //takes in two points(a would have 2 values and then compare that to b's two values(must be doubles));
    public double EuclideanDist(double long1, double long2, double lat1, double lat2){//double[] a, double[] b){
        double ans =0;
        
        double difference = Math.abs(long1 - long2);
        ans = difference * difference;
        difference = Math.abs(lat1 - lat2);
        ans += (difference * difference);
        //for(int i=0;i<a.length; ++i){
          
//        double difference = Math.abs(a[i] - b[i]);
//            ans += pow(difference, 2);
        //}
        return ans;
    }
  
    public double HaversineDist(double lat1, double lat2, double long1, double long2){
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(long2 - long1);
        double rad = 6371;
         
        // convert to radians
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
 
        // apply formulae
        double a = Math.pow(Math.sin(dLat / 2), 2) +
                   Math.pow(Math.sin(dLon / 2), 2) *
                   Math.cos(lat1) * Math.cos(lat2);
       
        double c = 2 * Math.asin(Math.sqrt(a));
        return rad * c;
}    
}
