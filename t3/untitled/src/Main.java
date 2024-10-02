import java.util.LinkedList;

public class Main {
    public static void main(String[] args) {
        MyThreadPool myThreadPool = new MyThreadPool(10);

        myThreadPool.execute(null);
        myThreadPool.execute(null);
        myThreadPool.execute(null);
        myThreadPool.execute(null);
        for (int i = 0; i < 15; i++) {

            final int index = i;
            myThreadPool.execute(() -> {
                System.out.println("    Start task" + index);
                try {
                    Thread.sleep(index%5 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("    End task" + index);
            });
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        myThreadPool.shutdown();
        System.out.println(Thread.currentThread().getName() + ": awaiting pool threads termination!");
        myThreadPool.awaitTermination();
        System.out.println(Thread.currentThread().getName() + ": pool threads terminated!");

        myThreadPool.execute(null);
    }
}