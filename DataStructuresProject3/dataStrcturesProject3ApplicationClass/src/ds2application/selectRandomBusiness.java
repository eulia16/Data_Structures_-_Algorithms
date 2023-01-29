
package ds2application;


public class selectRandomBusiness {
    int numBusiness;
    
    //constructor
    selectRandomBusiness(int numBusinesses){
        numBusiness = numBusinesses;
    }
    
    public String[] randomBusiness(int totalBusiness, String[] ids){
        int min = 0, max = totalBusiness;
        String[] businesses = new String[numBusiness];
        for(int i=0; i<numBusiness;++i){
            int b = (int)(Math.random()*(max-min)+min);
            businesses[i] = ids[b]; 
        }
            
        return businesses;
    }
}
