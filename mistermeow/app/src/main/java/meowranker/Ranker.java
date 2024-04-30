package meowranker;

import java.util.*;
import com.google.common.HashMap;
import com.google.common.Map;

public class Ranker {
    
    public double[] getPopularity(double[][] M , int UrlsCount){

        //TODO: implement logic 
        
        double d =0.85;
        double[][] M_hat = new double[UrlsCount][UrlsCount];
        for(int i =0 ; i<UrlsCount ; i++){
            for(int j=0;j<UrlsCount ; j++){
                M_hat[i][j] = d*M[i][j];
            }
        }

        double[] prevRank = new double[UrlsCount];
        double[] currRank ;

        for(int i=0 ; i<UrlsCount ; i++)
            prevRank[i] = 1.0/(double)UrlsCount;
        
        currRank = calculateCurrRank(prevRank, UrlsCount, d, M_hat);
        
        while(Norm(currRank) - Norm(prevRank)> 1e-10){
            prevRank=currRank;
            currRank = calculateCurrRank(prevRank, UrlsCount, d, M_hat);
        }

        return currRank;
    }   

    public double[] calculateCurrRank(double[] prevRank  , int UrlsCount , double d , double[][] M_hat){
        
        double[] currRank = new double[UrlsCount];
    
        for(int i = 0 ; i<UrlsCount ; i++){
            double val = 0;
            for(int j=0 ; j<UrlsCount ; j++){
                val+=M_hat[i][j]*prevRank[j]+(1-d);
            }
            currRank[i] = val;
        }

        return currRank;
    }

    public double Norm(double[] vector){
        double norm = 0;
        //TODO: implement
        return norm;
    }
}
