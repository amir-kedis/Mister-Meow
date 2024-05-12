
package meowranker;

import java.util.*;
import java.lang.Math;
import meowdbmanager.DBManager;
import meowindexer.Tokenizer;

public class Ranker {

    public DBManager db;
    public Tokenizer tokenizer;
    
    public Ranker(){
        db = new DBManager();
        tokenizer = new Tokenizer();
    }

    // The function takes graph of links between documents
    // where edge from doc1 to doc2 is representing by adding
    // 1/(outgoing urls from doc1) in the cell M[doc2][doc1]
    // resulting in matrix with sum of it's columns always = 1
    public double[] getPopularity(double[][] M , int UrlsCount){

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
        
        //normalizing the final array
        double norm = Norm(currRank , UrlsCount);

        for(int i  = 0 ; i<UrlsCount; i++){
            currRank[i]/=norm;
        }
        return currRank;
    }   

    public double[] calculateCurrRank(double[] prevRank  , int UrlsCount , double d , double[][] M_hat){
        
        double[] currRank = new double[UrlsCount];
    
        for(int i = 0 ; i<UrlsCount ; i++){
            double val = 0;
            for(int j=0 ; j<UrlsCount ; j++){
                val+=M_hat[i][j]*prevRank[j];
            }
            currRank[i] = val+(1-d);
        }

        return currRank;
    }

    public double Norm(double[] vector , int size){
        double norm = 0;

        for(int i =0 ; i<size ; i++){
            norm +=vector[i]*vector[i];
        }

        return Math.sqrt(norm);
    }
}
