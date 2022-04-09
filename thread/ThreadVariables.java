public class ThreadVariables {

    private static class ThreadWithVars implements Runnable {
        private static int i = 0;
        private final int j;

        ThreadWithVars(int j) {
            this.j = j;
        }

        @Override
        public void run() {
            System.out.printf("'i' is %d and 'j' is %d\n", i, j);
            i++;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        //Threads can have both static and non static instace variables
        Thread t1 = new Thread(new ThreadWithVars(100));
        Thread t2 = new Thread(new ThreadWithVars(200));

        t1.start(); //Prints 0 100
        t1.join();

        t2.start(); //Prints 1 200
        t2.join();
    }

}
