package src.main.java.meowcrawler;
import java.util.*;

public class QueueManagerMain{
    public static void main(String[] args) {
        QueueManager q = new QueueManager();
        String baseURL = "https://en.wikipedia.org";
        q.push(baseURL);
        q.push("String1");
        q.push("String2");

        q.print();
    }

}