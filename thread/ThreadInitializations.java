public class ThreadInitializations {

    private static class SomeRunnable implements Runnable {

        @Override
        public void run() {
            System.out.println("I ran");
        }

    }

    private static class ThreadExtension extends Thread {
        @Override
        public void run() {
            System.out.println("I ran by extending thread");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        //Option 1: Implement the runnable interface
        Thread t = new Thread(new SomeRunnable());
        t.start();
        t.join();

        //Option 2: Implement the runnable interface with lambdas
        Thread t1 = new Thread(() -> System.out.println("I ran from lambda"));
        t1.start();
        t1.join();

        //Option 3: Extend Thread class and override start method
        Thread t2 = new ThreadExtension();
        t2.start();
        t2.join();

        //------------------------------------------------------------------------------------

        //Thread.start() vs Thread.run()
        //1. Start creates new thread while run just executes in the same thread
        Thread t3 = new Thread(() -> System.out.println(Thread.currentThread().getId()));
        System.out.printf("Main thread is %s\n", Thread.currentThread().getId());
        System.out.println("Invoking run gives: ");
        t3.run();
        System.out.println("Invoking start gives: ");
        t3.start();
        t3.join();

        //2. Start can be called only once whereas run can be called any number of times
        Thread t4 = new Thread(() -> System.out.println("Thread is running. Runnable Function invoked"));
        System.out.println("Invoking run method twice");
        t4.run();
        t4.run();
        System.out.println("Invoking start method twice");
        t4.start();
        try {
            t4.start();
        } catch(IllegalThreadStateException threadStateException) {
            System.out.println("Got Exception: ");
            threadStateException.printStackTrace();
        }
    }

}
