import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class ScheduledExecutorsExample {

    private static class CountUp implements Runnable {
        private static int i = 0;

        @Override
        public void run() {
            System.out.println(i);
            i++;
        }
    }

    private static class Alphabets implements Runnable {
        private static char c = 'a';

        @Override
        public void run() {
            System.out.println(c);
            c++;
        }

    }

    public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException {
        //ScheduledExecutorService is a subclass of ExecutorService
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);

        //1. We can use it to submit a Runnable/Callable to run after a fixed delay
        System.out.println("Scheduling a  job to run after 5 seconds");
        System.out.printf("Current time is %d\n", System.currentTimeMillis());
        ScheduledFuture<?> future = executorService.schedule(() -> System.out.println(System.currentTimeMillis()), 5, TimeUnit.SECONDS);
        future.get();

        //2. We can use also use it to run jobs periodically after an initial delay
        executorService.scheduleAtFixedRate(new CountUp(), 1, 2, TimeUnit.SECONDS);
        //The above line would run at second 1, 3, 5, 7... REGARDLESS of how long the tasks take to execute
        //Therefore there can be overlapping executions

        //3. We can also use it to run jobs after a fixed delay after execution of a job
        executorService.scheduleWithFixedDelay(new Alphabets(), 1, 2, TimeUnit.SECONDS);
        //The above method would run at second 1, 1+executionTime+2

        executorService.awaitTermination(20, TimeUnit.SECONDS);
        executorService.shutdownNow();
    }

}
