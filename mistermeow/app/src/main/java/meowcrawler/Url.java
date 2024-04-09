package src.main.java.meowcrawler;
import java.util.*;
public class Url{
    private String urlString;
    private int priority;
    private static final int minPriority = 3;
    public Url(String s , int p){
        this.urlString = s;
        this.priority = p;
    }

    //Overriding toString fn in Obj class to be able to print correctly
    public String toString(){
        return "{ " + this.urlString + ", " + this.priority +  " }";
    }

    public int getPriority(){
        return priority;
    }

    public boolean decrementPriority(){
        if(priority > minPriority){
            priority--;
            return true;
        }
        return false;

    }

    public String getUrlString(){
        return urlString;
    }

    String getDomainName(){
        return this.urlString.split("/")[2];
        //another sol:
        // return this.urlStirng.replaceAll("http(s)?://|www\\.|/.*", "");
    }

}

