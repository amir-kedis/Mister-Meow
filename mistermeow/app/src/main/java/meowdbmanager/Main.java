package meowdbmanager;

import org.bson.types.ObjectId;

public class Main {
  public static void main(String[] args) {
    DBManager dbManager = new DBManager();
    ObjectId id = new ObjectId("6618244771bde05543ad75d8");
    double tf = dbManager.getDocumentFromInverted("big", id);
    System.out.println(tf);
  }
}
