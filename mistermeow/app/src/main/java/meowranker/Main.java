package meowranker;

public class Main {
    public static void main(String[] argv){
        
        int UrlCount = 3;
        double[][] M = new double[UrlCount][];

        M[0] = new double[]{0 , 0 , 1};
        M[1] = new double[]{1 , 0 , 0};
        M[2] = new double[]{0 , 1 , 0};

        double[] r = Ranker.getPopularity(M , UrlCount);

        for(int i=0 ; i<UrlCount; i++)
            System.out.print(r[i]+ " ");
    }    
}
