import java.util.Random;

/**
 * 多线程的一些测试.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */


public class threadTest {
    public static void main(String[] args) {
        MyThread t = new MyThread();
        t.start();
        while (true) {
            int v = new Random().nextInt();
            t.setValue(v);
            System.out.println("set value: " + v);
            System.out.println("read value:" + t.getValue());
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class MyThread extends Thread {
    int value = 10;

    public synchronized int getValue() {
        return value;
    }

    public synchronized void setValue(int value) {
        this.value = value;
    }

    @Override
    public void run() {
        while (true) {
            System.out.println("thread value:" + value);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
