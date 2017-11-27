package Util;

import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.security.MessageDigest;
import java.util.stream.IntStream;

public class RandomGenerator {
    private static final int capacity = 512;
    private static final int dangerous_line = 100;
    private Queue<Byte> entropyPool = new LinkedList<>();
    private MessageDigest md;

    public RandomGenerator() throws NoSuchAlgorithmException {
        this.md = MessageDigest.getInstance("SHA-1");
    }
    /**
     * Generate Entropy
     * @throws InterruptedException
     */
    public void produceEntropy() throws InterruptedException {
        while (true)
        {
            synchronized (this)
            {
                while (entropyPool.size()==capacity)
                    wait();
                entropyPool.add((byte)getRandomNumberInRange(0,256));
                if (entropyPool.size()>capacity/2){
                notify();
                }
            }
        }
    }

    /**
     * consume Entropy
     * @throws InterruptedException
     */
    public void generateRandom ()throws InterruptedException {
        while (true)
        {
            synchronized (this)
            {
                while (entropyPool.size()<=dangerous_line)
                    wait();
                byte byteData[] = md.digest(getPoolByteData());
                StringBuilder sb = new StringBuilder();
                for (int i=0;i<byteData.length;i++)
                sb.append((char)byteData[i]);
                IntStream.rangeClosed(1, 20)
                        .forEach(p->entropyPool.poll());
                System.out.print(sb.toString());
                notify();
            }
        }
    }

    private byte[] getPoolByteData() {
        int index=0;
        byte[] pool = new byte[entropyPool.size()];
        Iterator<Byte> iterator = entropyPool.iterator();
        while(iterator.hasNext()){
            pool[index] = iterator.next().byteValue();
            index++;
        }
        return pool;
    }


    private int getRandomNumberInRange(int min, int max) {
        Random r = new Random();
        return r.ints(min, (max + 1)).findFirst().getAsInt();
    }

}
