package ru.javawebinar.topjava.service;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javawebinar.topjava.repository.JpaUtil;

public abstract class AbstractJpaUserServiceTest extends AbstractUserServiceTest {
    @Autowired
    JpaUtil jpaUtil;

    @Before
    public void setup2Jpa() {
        jpaUtil.clear2ndLevelHibernateCache();
    }
}