import java.util.LinkedList;
import java.util.concurrent.CountDownLatch;

public class MyThreadPool {
    private final int nThreads;
    private final LinkedList<Runnable> tasks = new LinkedList<>();
    private final CountDownLatch downLatch;
    private final Object awaitMonitor = new Object();
    private boolean shutdown = false;

    public MyThreadPool(int nThreads) {
        this.nThreads = nThreads;
        downLatch = new CountDownLatch(nThreads);
        initialize();
    }

    public void execute(Runnable task) {
        if (shutdown) {
            throw new IllegalStateException("ThreadPool is already shutdown");
        }
        tasks.add(task);
        synchronized (awaitMonitor) {
            awaitMonitor.notifyAll();
        }
    }

    public void shutdown() {
        shutdown = true;
        synchronized (awaitMonitor) {
            awaitMonitor.notifyAll();
            }
    }

    public void initialize() {
        CountDownLatch initLatch = new CountDownLatch(nThreads);
        for (int i = 0; i < nThreads; i++) {
            new Thread(()->{
                while (!shutdown){
                    Runnable task = null;
                    synchronized (tasks) {
                        if (!tasks.isEmpty()) task = tasks.removeFirst();
                    }
                    if (task != null) {
                        System.out.println(Thread.currentThread().getName() + ": process : " + task);
                        task.run();
                    } else {
                        try {
                            System.out.println(Thread.currentThread().getName() + ": waiting for new tasks");
                            synchronized (awaitMonitor) {
                            initLatch.countDown();
                            awaitMonitor.wait();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                System.out.println(Thread.currentThread().getName() + ": shutdown");
                downLatch.countDown();
            }
            ).start();
        }

        System.out.println("Waiting for threads initialization");
        try {
            initLatch.await();
            System.out.println("Threads initialized");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    void awaitTermination() {
        try {
            downLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
