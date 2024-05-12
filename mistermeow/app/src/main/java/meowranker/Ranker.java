
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
    public double[] getPopularity(double[][] M, int UrlsCount) {

        double d = 0.85;
        double[][] M_hat = new double[UrlsCount][UrlsCount];
        for (int i = 0; i < UrlsCount; i++) {
            for (int j = 0; j < UrlsCount; j++) {
                M_hat[i][j] = d * M[i][j];
            }
        }

        double[] prevRank = new double[UrlsCount];
        double[] currRank;

        for (int i = 0; i < UrlsCount; i++)
            prevRank[i] = 1.0 / (double) UrlsCount;

        currRank = calculateCurrRank(prevRank, UrlsCount, d, M_hat);

        while (Norm(currRank, UrlsCount) - Norm(prevRank, UrlsCount) > 1e-10) {
            prevRank = currRank;
            currRank = calculateCurrRank(prevRank, UrlsCount, d, M_hat);
        }

        // normalizing the final array
        double norm = Norm(currRank, UrlsCount);

        for (int i = 0; i < UrlsCount; i++) {
            currRank[i] /= norm;
        }
        return currRank;
    }

    public double[] calculateCurrRank(double[] prevRank, int UrlsCount, double d, double[][] M_hat) {

        double[] currRank = new double[UrlsCount];

        for (int i = 0; i < UrlsCount; i++) {
            double val = 0;
            for (int j = 0; j < UrlsCount; j++) {
                val += M_hat[i][j] * prevRank[j];
            }
            currRank[i] = val + (1 - d);
        }

        return currRank;
    }

    public double Norm(double[] vector, int size) {
        double norm = 0;

        for (int i = 0; i < size; i++) {
            norm += vector[i] * vector[i];
        }

        return Math.sqrt(norm);
    }

    /**
     * constructUrlsGraph - returns a constructed graph from the Urls data in the
     * database.
     * 
     * @return double[][] - a matrix of doubles, with size N * N,
     *         where N equal to number of urls in the database.
     */
    public double[][] constructUrlsGraph() {
        // Number of nodes in graph is number of urls in database.
        int nodesNum = db.getUrlsCount();
        System.out.println(nodesNum);
        // Create a 2D array filled with 0s initialiy.
        double[][] graph = new double[nodesNum][nodesNum];

        // Construct graph with parents arrays.
        constructMatrix(graph, nodesNum);

        // Scale graph with a certain formula
        scaleMatrix(graph, nodesNum);

        return graph;
    }

    /**
     * constructMatrix - takes a 2D matrix and fill it with edges from parents
     * arrays of each node.
     * 
     * @param graph    - the 2D matrix to fill.
     * @param nodesNum - the number of nodes and the size of the graph.
     */
    public void constructMatrix(double[][] graph, int nodesNum) {

        // Loop over each node (url) and get it's parents array and construct graph.
        for (int i = 0; i < nodesNum; i++) {
            List<Integer> nodeParents = db.getParentsArr(i);

            // Loop over the parents array and add 1 for each edge from parent to node.
            for (int j = 0; j < nodeParents.size(); j++) {
                int parentId = nodeParents.get(j);

                if (parentId > 0 && parentId < nodesNum) {
                    graph[i][parentId]++;
                }
            }
        }

    }

    /**
     * scaleMatrix - takes a 2D Matrix and scales each column by a certain formula.
     * 
     * @param graph    - the 2D matrix to scale.
     * @param nodesNum - the number of nodes and the size of the graph.
     */
    public void scaleMatrix(double[][] graph, int nodesNum) {

        // Loop over each column in the graph.
        for (int j = 0; j < nodesNum; j++) {
            double sum = 0;

            // Calculate sum of values in the j'th column
            for (int i = 0; i < nodesNum; i++)
                sum += graph[i][j];

            if (sum == 0)
                continue;

            // Divide each cell value by the sum of column, to scale it.
            for (int i = 0; i < nodesNum; i++)
                graph[i][j] /= sum;
        }
    }

}
