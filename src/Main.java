import Util.RandomGenerator;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Main {
    public static void main(String args[]) throws Exception {
       final RandomGenerator randomGenerator = new RandomGenerator();
       new Thread(()->{
           try {
               randomGenerator.produceEntropy();
           } catch (InterruptedException e) {
               e.printStackTrace();
           }
       }).start();
       new Thread(()->{
           try {
               randomGenerator.generateRandom();
           } catch (InterruptedException e) {
               e.printStackTrace();
           }
       }).start();
    }
}
