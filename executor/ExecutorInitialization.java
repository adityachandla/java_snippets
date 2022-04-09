import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class ExecutorInitialization {

    private static class ThreadInfoPrinter implements Runnable {
        private int sleepMillis;

        ThreadInfoPrinter(int sleepMillis) {
            this.sleepMillis = sleepMillis;
        }

        @Override
        public void run() {
            sleep(sleepMillis);
            System.out.printf("ThreadId: %d\n", Thread.currentThread().getId());
        }

    }

    private static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        }catch(InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        //1. Fixed size thread pool 
        System.out.println("Fixed side thread pool of size 5 and 10 threads");
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for(int i = 0; i < 10; i++) {
            executorService.submit(new ThreadInfoPrinter(5));
        }
        customShutdown(executorService);

        //2. Cached Thread pool
        //Use existing threads when load is low
        System.out.println("Cached thread pool where thread is created every 20 ms");
        executorService = Executors.newCachedThreadPool();
        for(int i = 0; i < 10; i++) {
            executorService.submit(new ThreadInfoPrinter(10));
            sleep(20);
        }
        customShutdown(executorService);

        //Create new threads when load is high
        System.out.println("Cached thread pool where a new thread is created every 1 ms");
        executorService = Executors.newCachedThreadPool();
        for(int i = 0; i < 10; i++) {
            executorService.submit(new ThreadInfoPrinter(20));
            sleep(1);
        }
        customShutdown(executorService);


        //3. Work stealing thread group
        //Number of threads created equal to Runtime.getRuntime().availableProcessors()
        System.out.println("Work stealing pool where threads are created every 1ms");
        System.out.println(Runtime.getRuntime().availableProcessors());
        executorService = Executors.newWorkStealingPool();
        for(int i = 0; i < 10; i++) {
            executorService.submit(new ThreadInfoPrinter(20));
        }
        customShutdown(executorService);
        //Work stealing thread group vs Fixed size thread pool
        //Fixed size thread pool creates a single queue from which all threads deque
        //This creates contention therefore in work stealing pool,
        //Every thread has its own queue and it first tries to dequeue from its own queue
        //if its own queue is empty then it tries to dequeue from other threads' queues 
        //This reduces contention.
    }

    private static void customShutdown(ExecutorService service) {
        //This is the way suggested by oracle to shutdown an executorservice properly
        //https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/ExecutorService.html
        service.shutdown(); //This method just inhibits any new submissions to service
        try {
            if(!service.awaitTermination(1, TimeUnit.SECONDS)) {
                service.shutdownNow();
            }
        }catch(InterruptedException e) {
            service.shutdownNow();
        }
    }

}
