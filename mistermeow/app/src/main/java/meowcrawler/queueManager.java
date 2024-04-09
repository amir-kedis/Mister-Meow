package src.main.java.meowcrawler;

import java.util.*;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class QueueManager {

    private static final int domainQCapacity = 100;
    private Queue<Url> priorityQ;                       //priority queue to priotorize urls on pushing
    private PriorityQueue<Queue<Url>> domainQs;         //list of domain queues
    private BiMap<String, Queue<Url>> domainIndexMp;    //Bidirectional-map between url and domain queue in the list

    private Integer domainQListSize;                    //Denotes the total number of domain queues inside the list

    public QueueManager() {
        priorityQ = new PriorityQueue(1, new UrlComparator());
        domainIndexMp = HashBiMap.create();
        domainQs = new PriorityQueue(domainQCapacity, new llComparator());
        domainQListSize = 0;
    }

    public boolean push(String newUrl) {
        int priority = getPriority();
        Url u = new Url(newUrl, priority);
        if (priorityQ.add(u)) {
            System.out.println("Inserted Sucessfully");
            return true;
        }
        System.out.println("Error in insertion");
        return false;
    }

    public String pop() throws Exception {
        Queue<Url> q = domainQs.poll();
        if (q.isEmpty())
            throw new Exception("Trying to pop from empty queue");
        String s = q.poll().getUrlString();

        if (q.isEmpty()) {
            domainQListSize--;
            String url = domainIndexMp.inverse().get(q);
            domainIndexMp.remove(url);
        } else {
            domainQs.add(q);
        }

        return s;
    }

    public boolean isEmpty() {
        return domainQs.isEmpty();
    }

    private int getPriority(String Url) {
        return (int) (Math.random() * 10);
        //TODO: find better implementation
    }

    public boolean moveToDomainQ() {
        Url u = priorityQ.poll();

        if (u == null)
            return false;

        String domain = u.getDomainName();
        Queue<Url> q = domainIndexMp.get(domain);

        //handling case of no available space in the "domainQs" list
        if (q == null) {
            q = createDomainQueue(domain);
            if (q == null) {
                u.decrementPriority();              //decreasing priority so that it doesn't get again into the most prior one
                priorityQ.add(u);                   //return to priorityQ
                return false;
            }
        }
        q.add(u);
        return true;
    }

    //This fn returns:
    // 1- reference to the domain queue if there exist available space
    // 2- null incase of no available queues in the list
    private Queue<Url> createDomainQueue(String domain) {

        if (domainQListSize == domainQCapacity){
            System.out.println("Cannot create more domain queues");
            return null;
        }
        Queue<Url> l = new LinkedList<Url>();
        domainQs.add(l);
        System.out.println("New domain queue has been formed");
        domainQListSize++;
        domainIndexMp.put(domain, l);
        return l;
    }

    //functions for testing only
    public void printDomainQ() {
        Iterator iterator = domainQs.iterator();
        while (iterator.hasNext()) {
            print((Queue<Url>)iterator.next());
        }
    }
    public void printPriorityQ() {
        print(priorityQ);
    }

    public void print(Queue<Url> ll) {
        Iterator iterator = ll.iterator();
        while (iterator.hasNext()) {
            System.out.print(iterator.next());
        }
        System.out.println();
    }
}

//implements fn compare to be able to sort Linkedlists inside the priority queue
class llComparator implements Comparator<LinkedList<Url>> {
    public int compare(LinkedList<Url> l1, LinkedList<Url> l2) {
        if (l1.size() < l2.size())
            return 1;
        else if (l1.size() > l2.size())
            return -1;
        else
            return 0;
    }
}