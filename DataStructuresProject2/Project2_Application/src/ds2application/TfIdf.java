
package ds2application;

import java.lang.Math.*;


public class TfIdf {
    
   



double tf(FrequencyTable fq, String categories, String searchWord){//add anither parameter String searchWord that will return the tf value of the string passed in 
    double tfValue =0;
    //the tf value is how often the word occurs in the 'sentence' divided by the size of the sentence
    tfValue = (float)fq.frequency(searchWord)/ (float)fq.size;
    
    return tfValue;
}

double idf(double numDocuments, double wordFrequency){
    double idfValue=0;
    //idf value is the log(base 10) of the number of 'sentences' divided by the amount of times that wprd occurs
    idfValue = Math.log10(numDocuments/wordFrequency);
    
    
    return idfValue;
}
    
double tfIdf(double tf, double idf){
    double tfIdf=0;
    
    tfIdf = tf * idf;
    
    return tfIdf;
}
    
}
