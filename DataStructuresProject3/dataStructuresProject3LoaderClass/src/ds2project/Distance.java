
package ds2project;




public class Distance {


public double HaversineDist(double lat1, double lat2, double long1, double long2){
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(long2 - long1);
        double rad = 6371;
         
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
        
        double a = Math.pow(Math.sin(dLat / 2), 2) +
                   Math.pow(Math.sin(dLon / 2), 2) *
                   Math.cos(lat1) * Math.cos(lat2);
       
        double c = 2 * Math.asin(Math.sqrt(a));
        return rad * c;
}    
    
}
