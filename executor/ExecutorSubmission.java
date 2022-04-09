import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutionException;

public class ExecutorSubmission {

    private static class CalculateFibonnaci implements Callable<Long> {
        private int num;

        CalculateFibonnaci(int num) {
            this.num = num;
        }

        @Override
        public Long call() {
            long a = 0, b = 1;
            for(int i = 0; i < num; i++) {
                long temp = b;
                b = a+b;
                a = b;
            }
            return a;
        }
    }

    private static class PrintNum implements Runnable {
        private int num;

        PrintNum(int num) {
            this.num = num;
        }

        @Override
        public void run() {
            System.out.println(num);
        }
    }

    public static void main(String[] args) throws InterruptedException,ExecutionException {
        ExecutorService service = Executors.newFixedThreadPool(3);
        //We can submit a runnable to an executor and it will simply run it
        //A normal submit also returns a Future<?> which we can wait on for synchronization
        Future<?> lastFuture = null;
        for (int i = 0; i < 5; i++) {
            lastFuture = service.submit(new PrintNum(i));
        }
        lastFuture.get();

        //We can also return a result by using Callable instead of Runnable
        //Unlike runnable, which is part of java.lang, Callable is a part of java.util.concurrent package
        List<Future<Long>> fibonacciResults = new ArrayList<>();
        for(int j = 11; j < 15; j++) {
            fibonacciResults.add(service.submit(new CalculateFibonnaci(j)));
        }
        for(Future<Long> res : fibonacciResults) {
            System.out.println(res.get());
        }
        customShutdown(service);
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
