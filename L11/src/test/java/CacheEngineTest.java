import com.jbtits.otus.lecture11.cache.CacheEngine;
import com.jbtits.otus.lecture11.cache.CacheEngineImpl;
import com.jbtits.otus.lecture11.dbservice.entity.UserDataSet;
import com.jbtits.otus.lecture11.dbservice.service.DBService;
import com.jbtits.otus.lecture11.dbservice.service.DBServiceJDBC;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CacheEngineTest {
    private DBService dbService;
    private CacheEngine cacheEngine;
    private static final int CACHE_SIZE = 10;
    private static final long CACHE_IDLE_TIMEOUT_MS = 2000L;

    private UserDataSet createUser() {
        UserDataSet user = new UserDataSet();
        user.setName("John Doe");
        user.setAge(99);
        return user;
    }

    @Before
    public void init() {
        cacheEngine = new CacheEngineImpl(CACHE_SIZE, CACHE_IDLE_TIMEOUT_MS);
        dbService = new DBServiceJDBC(cacheEngine);
    }

    @After
    public void tearDown() {
        dbService.shutdown();
        cacheEngine.shutdown();
    }

    @Test
    public void idleTimeoutTest() throws InterruptedException {
        for (int i = 0; i < CACHE_SIZE; i++) {
            dbService.saveUser(createUser());
        }
        Thread.sleep(CACHE_IDLE_TIMEOUT_MS + 10);
        for (int i = 0; i < CACHE_SIZE; i++) {
            dbService.getUserById(i + 1);
        }
        assertEquals(0, cacheEngine.getHitCount());
        assertEquals(CACHE_SIZE, cacheEngine.getMissCount());
    }

    @Test
    public void missTest() {
        for (int i = 0; i < CACHE_SIZE * 2; i++) {
            dbService.saveUser(createUser());
        }
        for (int i = 0; i < CACHE_SIZE; i++) {
            dbService.getUserById(i + 1);
        }
        assertEquals(0, cacheEngine.getHitCount());
        assertEquals(CACHE_SIZE, cacheEngine.getMissCount());
    }

    @Test
    public void hitAndMissTest() {
        for (int i = 0; i < CACHE_SIZE + CACHE_SIZE / 2; i++) {
            dbService.saveUser(createUser());
        }
        for (int i = CACHE_SIZE / 2; i < CACHE_SIZE; i++) {
            dbService.getUserById(i + 1);
        }
        for (int i = 0; i < CACHE_SIZE / 2; i++) {
            dbService.getUserById(i + 1);
        }
        assertEquals(CACHE_SIZE / 2, cacheEngine.getHitCount());
        assertEquals(CACHE_SIZE / 2, cacheEngine.getMissCount());
    }

    @Test
    public void hitTest() {
        for (int i = 0; i < CACHE_SIZE; i++) {
            dbService.saveUser(createUser());
        }
        for (int i = 0; i < CACHE_SIZE; i++) {
            dbService.getUserById(i + 1);
        }
        assertEquals(CACHE_SIZE, cacheEngine.getHitCount());
        assertEquals(0, cacheEngine.getMissCount());
    }
}
