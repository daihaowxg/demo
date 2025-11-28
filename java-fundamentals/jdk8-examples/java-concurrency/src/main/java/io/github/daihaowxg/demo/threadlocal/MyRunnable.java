package io.github.daihaowxg.demo.threadlocal;

/**
 * @author wxg
 * @since 2025/3/14
 */
public class MyRunnable implements Runnable{

    private final String user;

    public MyRunnable(String user) {
        this.user = user;
    }

    /**
     * When an object implementing interface {@code Runnable} is used
     * to create a thread, starting the thread causes the object's
     * {@code run} method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method {@code run} is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        ThreadLocalContext.setUser(user);

        System.out.println("当前线程：" + Thread.currentThread().getName() + "，用户：" + ThreadLocalContext.getUser());

        ThreadLocalContext.setUser(user + "10086");

        System.out.println("当前线程：" + Thread.currentThread().getName() + "，更新后的用户：" + ThreadLocalContext.getUser());
    }
}
