
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
  
    public double HaversineDist(double long1, double long2, double lat1, double lat2){
        int R = 6371; 
 
        double latDistance = (lat2-lat1) * Math.PI / 180;
        double lonDistance = (long2-long1) * Math.PI / 180;
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + 
        Math.cos((lat1*Math.PI)/180) * Math.cos((lat2*Math.PI)/180) * 
        Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c =  Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double distance = R * c;
 
        //System.out.println("The distance between two lat and long is:" + distance);

        return distance;
}    
}
