package src.main.java.meowcrawler;

import java.util.*;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class QueueManager {

    private static final int domainQCapacity = 100;
    private Queue<Url> priorityQ;                  //priority queue to priotorize urls on pushing
    //    private LinkedList<Url>[] domainQList;        //list of domain queues
    private PriorityQueue<Queue<Url>> domainQs;
    private BiMap<String, Queue<Url>> domainIndexMp;  //Bidirectional-map between url and domain queue in the list
//    private Set<Integer> EmptyQueuesSet;           //Set of indicies of empty queues inoreder to manage pop
//    private Set<Integer> EmptyQueuesSet;           //Set of indicies of empty queues inoreder to manage pop

    private Integer domainQListSize;               //Denotes the total number of domain queues inside the list
//    private Integer nextPopIndex;                  //Selects which domain queue to pop urls from
//    private Integer urlsInDomainQ;                 //Count of total urls in all domainqueues

    public QueueManager() {
        priorityQ = new PriorityQueue(1, new UrlComparator());
        domainIndexMp = HashBiMap.create();
//        domainQList = new LinkedList[domainQCapacity];
        domainQs = new PriorityQueue(domainQCapacity, new llComparator());
        domainQListSize = 0;
//        nextPopIndex = 0;
//        urlsInDomainQ = 0;
    }

    public boolean push(String newUrl) {
        int priority = (int) (Math.random() * 10);    //TODO: assign getProirity fn
        Url u = new Url(newUrl, priority);
        if (priorityQ.add(u)) {
            System.out.println("Inserted Sucessfully");
            return true;
        }
        System.out.println("Error in insertion");
        return false;
    }

    public String pop() throws Exception {
//        if (domainQList[nextPopIndex] == null)
//            throw new Exception("Trying to pop from empty queue");
//        urlsInDomainQ--;
//        String s = domainQList[nextPopIndex].poll().getUrlString();
//        incrementNextPopIndex();
//        return s;

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
//        return urlsInDomainQ == 0 ? true : false;
        return domainQs.isEmpty();
    }

//    private void incrementNextPopIndex() {
//        if (urlsInDomainQ == 0){
//            nextPopIndex = 0;
//            return;
//        }
//        int ind = EmptyQueuesSet.
//
//    }

    private int getPriority(String Url) {
        return 1;
        //TODO: implement
    }

    public boolean moveToDomainQ() {
        Url u = priorityQ.poll();

        //checking for empty priority queue
        if (u == null)
            return false;

        String domain = u.getDomainName();

//        Integer index = domainIndexMp.get(domain);
//
//        if (index == null) {
//            index = createDomainQueue(domain);
//            if (index == -1) {
//                System.out.println("Cannot move url to domain queue");
//                u.decrementPriority();              //decreasing priority so that it doesn't get again into the most prior one
//                priorityQ.add(u);                   //return to priorityQ
//            }
//        }
//        domainQList[index].add(u);
//        urlsInDomainQ++;
//        return true;

        Queue<Url> q = domainIndexMp.get(domain);
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
    // 1-the index of the created queue if available
    // 2- (-1) incase of no available queues in the list
    private Queue<Url> createDomainQueue(String domain) {
//        Integer ind = EmptyQueuesSet.Ceiling((Integer) 0);
//        if (ind == null) {
//            if (domainQListSize == domainQCapacity)
//                return -1;
//            ind = domainQListSize++;
//            domainQList[ind] = new LinkedList<>();
//        } else {
//            EmptyQueuesSet.remove(ind);
//        }
//        domainIndexMp.put(domain, ind);
//        return ind;

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

    public void print(Queue<Url> ll) {
        Iterator iterator = ll.iterator();
        while (iterator.hasNext()) {
            System.out.print(iterator.next());
        }
        System.out.println();
    }


    public void printPriorityQ() {
        print(priorityQ);
    }
}

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
