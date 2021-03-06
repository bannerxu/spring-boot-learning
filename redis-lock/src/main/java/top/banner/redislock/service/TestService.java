package top.banner.redislock.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import top.banner.lib.lock.RedisLock;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class TestService {
    private static final Logger log = LoggerFactory.getLogger(TestService.class);

    int a = 0;
    int b = 0;
    final Random random = new Random(System.currentTimeMillis());


    public void add() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(random.nextInt(100));
        a++;
        log.info("a => {}", a);
    }

    @RedisLock(name = "lockAdd", key = "'lock'")
    public void lockAdd() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(random.nextInt(100));
        log.info("b => {}", b++);
    }


    public void awaitAfterShutdown(ExecutorService threadPool) {

        threadPool.shutdown();
        try {
            if (!threadPool.awaitTermination(60, TimeUnit.SECONDS)) {
                threadPool.shutdownNow();
            }
        } catch (InterruptedException ex) {
            threadPool.shutdownNow();
            Thread.currentThread().interrupt();
        }

    }
}
