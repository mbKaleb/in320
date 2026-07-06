package Deadlock;

public class DeadLockExample {

    private static final Object lock1 = new Object();
    private static final Object lock2 = new Object();

    public static void main(String[] args) {

        Thread thread1 = new Thread(() -> {
            System.out.println("Thread 1: Trying to acquire lock1");
            synchronized (lock1) {
                System.out.println("Thread 1: Acquired lock1");
                sleep(100);
                System.out.println("Thread 1: Waiting to acquire lock2");
                synchronized (lock2) {
                    System.out.println("Thread 1: Acquired lock2");
                }
            }
        });

        Thread thread2 = new Thread(() -> {
            System.out.println("Thread 2: Trying to acquire lock2");
            synchronized (lock2) {
                System.out.println("Thread 2: Acquired lock2");
                sleep(100);
                System.out.println("Thread 2: Waiting to acquire lock1");
                synchronized (lock1) {
                    System.out.println("Thread 2: Acquired lock1");
                }
            }
        });

        thread1.start();
        thread2.start();
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}