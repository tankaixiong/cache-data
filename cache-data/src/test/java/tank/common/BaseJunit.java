package tank.common;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author tank
 * @version :1.0
 * @date:24 Sep 2014 10:16:32
 * @description:
 */
@ContextConfiguration(locations = {"classpath:context-hibernate.xml"})
@Transactional("transactionManager")
@Rollback(false)
@RunWith(SpringJUnit4ClassRunner.class)
public class BaseJunit extends AbstractTransactionalJUnit4SpringContextTests {

    public BaseJunit() {

    }

    @Before
    public void setUp() {


    }

    @After
    public void setDown() {

    }
}
