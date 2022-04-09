public class RecursiveCalling {

    private static class RecursiveRunnable implements Runnable {

        @Override
        public void run() {
            try {
                Thread t = new Thread(new RecursiveRunnable());
                t.start();
                t.join();
            }catch(InterruptedException e) {
                e.printStackTrace();
            }
        }

    }


    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(new RecursiveRunnable());
        t.start();
        t.join();
        //Warning: DO NOT RUN. Causes the following exception:
        //[13.276s][warning][os,thread] Failed to start thread - pthread_create failed (EAGAIN) for attributes: stacksize: 1024k, guardsize: 0k, detached.
        //Exception in thread "Thread-19109" java.lang.OutOfMemoryError: unable to create native thread: possibly out of memory or process/resource limits reached
        //at java.base/java.lang.Thread.start0(Native Method)
        //at java.base/java.lang.Thread.start(Thread.java:800)
        //at RecursiveCalling$RecursiveRunnable.run(RecursiveCalling.java:9)
        //at java.base/java.lang.Thread.run(Thread.java:831)
    }

}
