package src.main.java.meowcrawler;
import java.util.*;

public class QueueManager{
    private Queue<Url> priorityQ;
    private Queue<Url>[] domainQList;
    private Map<Url , Queue<Url>> domainQMp;
    public QueueManager(){
        priorityQ = new PriorityQueue(5 , new UrlComparator());
        domainQMp = new HashMap<Url , Queue<Url>>();
    }

    public boolean push(String newUrl){
        int priority = (int)(Math.random() * 10);    //TODO: assign getProirityfn
        Url u = new Url(newUrl , priority);
        if(priorityQ.add(u)){
            System.out.println("Inserted Sucessfully");
            return true;
        }
        System.out.println("Error in insertion");
        return false;
    }

//    public String pop(){
//
//    }

//    private int getPriority(String Url){
//
//    }

        public void print(){
            Iterator iterator = priorityQ.iterator();
            while (iterator.hasNext()) {
                System.out.print(iterator.next());
            }
        }
}

class UrlComparator implements Comparator<Url> {
    public int compare(Url u1 , Url u2){
        if(u1.priority > u2.priority)
            return 1;
        else if(u1.priority < u2.priority)
            return -1;
        else
            return 0;
    }
}

class Url{
    public String urlString;
    public int priority;

    public Url(String s , int p){
        this.urlString = s;
        this.priority = p;
    }

    public String toString(){
        return "{ " + this.urlString + ", " + this.priority +  " }";
    }

}