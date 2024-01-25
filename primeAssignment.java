import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.PriorityQueue;
import java.util.concurrent.atomic.AtomicLong;

// Tells the next thread in the queue which value to take
class Counter
{
    private long value;

    public Counter(long initialValue)
    {
        this.value = initialValue;
    }

    // We increment by 2 in order to skip even numbers (2 is the only even prime)
    public synchronized long getAndIncrement()
    {
        long temp = value;
        value += 2;
        return temp;
    }
}

public class primeAssignment
{
    // Runnable allows use of the run() method for when the thread starts
    static class findPrimes implements Runnable
    {
        private Counter counter;
        private final AtomicLong primeCount;
        private final AtomicLong sum;
        private final PriorityQueue<Long> topNumbers;

        public findPrimes(Counter counter, AtomicLong primeCount, AtomicLong sum, PriorityQueue<Long> topNumbers)
        {
            this.counter = counter;
            this.primeCount = primeCount;
            this.sum = sum;
            this.topNumbers = topNumbers;
        }

        public void run()
        {
            long number;
            while ((number = counter.getAndIncrement()) < 100_000_000L)
            {
                if (isPrime(number))
                {
                    synchronized (primeCount)
                    {
                        primeCount.incrementAndGet();
                        sum.addAndGet(number);
                        synchronized (topNumbers)
                        {
                            if (topNumbers.size() < 10)
                            {
                                topNumbers.offer(number);
                            }
                            else if (number > topNumbers.peek())
                            {
                                topNumbers.poll();
                                topNumbers.offer(number);
                            }
                        }
                    }
                }
            }
        }

        // Using trial division method for checking primality
        private boolean isPrime(long number)
        {
            //if (number <= 1) return false;
            for (long i = 2; i * i <= number; i++)
            {
                if (number % i == 0) return false;
            }
            return true;
        }
    }

    public static void main(String[] args) throws IOException
    {

        // Start counter from 3 (first odd prime)
        Counter counter = new Counter(3);

        // Use atomic numbers to ensure integrity of the value (only one thread accesses it at a time)
        AtomicLong primeCount = new AtomicLong(0);
        AtomicLong sum = new AtomicLong(0);

        // Priority queue is most efficient for this type of work
        PriorityQueue<Long> topNumbers = new PriorityQueue<>();

        // 2 is the only even prime, handle separately for simplicity
        primeCount.incrementAndGet();
        sum.addAndGet(2);
        topNumbers.offer(2L);

        int numberOfThreads = 8;
        Thread[] threads = new Thread[numberOfThreads];

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < numberOfThreads; i++)
        {
            threads[i] = new Thread(new findPrimes(counter, primeCount, sum, topNumbers));
            threads[i].start();
        }

        for (Thread thread : threads)
        {
            try
            {
                thread.join();
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }

        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;

        // Write desired information to the text file
        try (BufferedWriter w = new BufferedWriter(new FileWriter("primes.txt")))
        {
            w.write(totalTime + " " + primeCount.get() + " " + sum.get() + "\n");

            Long[] topTen = new Long[10];

            for (int i = 9; i >= 0; i--)
            {
                topTen[i] = topNumbers.poll();
            }

            for (long prime : topTen)
            {
                if (prime != 0) {
                    w.write(prime + " ");
                }
            }
        }
    }
}
