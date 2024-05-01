package meowranker;

import java.util.*;
import java.lang.Math;

public class Ranker {
    
    public static double[] getPopularity(double[][] M , int UrlsCount){

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
        
        while(Norm(currRank , UrlsCount) - Norm(prevRank , UrlsCount)> 1e-10){
            prevRank=currRank;
            currRank = calculateCurrRank(prevRank, UrlsCount, d, M_hat);
        }

        return currRank;
    }   

    public static double[] calculateCurrRank(double[] prevRank  , int UrlsCount , double d , double[][] M_hat){
        
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

    public static double Norm(double[] vector , int size){
        double norm = 0;

        for(int i =0 ; i<size ; i++){
            norm +=vector[i]*vector[i];
        }

        return Math.sqrt(norm);
    }
}
