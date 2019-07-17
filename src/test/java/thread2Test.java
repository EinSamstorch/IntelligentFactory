import java.util.Objects;
import java.util.concurrent.Semaphore;

/**
 * .
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class thread2Test {
    public static void main(String[] args) {
        Object lock = new Object();
        new ThreadA(lock).start();
        new ThreadB(lock).start();
    }
}

class ThreadA extends Thread{
    private final Object lock;

    public ThreadA(Object lock) {
        this.lock = lock;
    }

    @Override
    public void run() {
        synchronized (lock){
            try {
                System.out.println("Wait");
                lock.wait();
                System.out.println("Wait Done");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}

class ThreadB extends Thread{
    private final Object lock;

    public ThreadB(Object lock) {
        this.lock = lock;
    }

    @Override
    public void run() {
        System.out.println("Start sleep");
        try {
            sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        synchronized (lock){
            lock.notify();
            System.out.println("Notify Done");
        }

    }
}


