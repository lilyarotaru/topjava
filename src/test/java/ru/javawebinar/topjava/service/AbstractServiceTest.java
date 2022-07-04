package ru.javawebinar.topjava.service;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.ActiveDbProfileResolver;

import java.util.concurrent.TimeUnit;

import static org.slf4j.LoggerFactory.getLogger;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
@ActiveProfiles(resolver = ActiveDbProfileResolver.class)
public abstract class AbstractServiceTest {
    protected static final Logger log = getLogger("result");

    @Rule
    @ClassRule
    public static Stopwatch stopwatch = new Stopwatch() {
        private final StringBuilder results = new StringBuilder();

        @Override
        protected void finished(long nanos, Description description) {
            if (description.isTest()) {
                String result = String.format("\n%-25s %7d", description.getMethodName(), TimeUnit.NANOSECONDS.toMillis(nanos));
                results.append(result);
                log.info(result + " ms\n");
            } else if (description.isSuite()) {
                log.info("\n---------------------------------" +
                        "\nTest                 Duration, ms" +
                        "\n---------------------------------" +
                        results +
                        "\n---------------------------------");
                results.setLength(0);
            }
        }
    };

    abstract void delete();

    abstract void deleteNotFound();

    abstract void create();

    abstract void update();

    abstract void get();

    abstract void getNotFound();

    abstract void getAll();
}
