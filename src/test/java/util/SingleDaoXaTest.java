package util;

import config.AppXaConfig;
import entity.Singer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.orm.jpa.JpaSystemException;
import util.dbxa.EntityManagerWrapper;

import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SingleDaoXaTest {
    private static Logger logger = LoggerFactory.getLogger(SingleDaoXaTest.class);

    private GenericApplicationContext ctx;
    private EntityManagerWrapper emw;

    @Before
    public void setUp() {
        ctx = new AnnotationConfigApplicationContext(AppXaConfig.class);
        emw = ctx.getBean(EntityManagerWrapper.class);
        assertNotNull(emw);
    }

    @Test
    public void testSave() {
        try {
            logger.info("testSave");
            emw.doWithEntityManager((emA, emB) -> {
                Singer singerA = new Singer();
                singerA.setFirstName("Alex");
                singerA.setLastName("SaveXa");
                singerA.setBirthDate(new GregorianCalendar().getTime());
                emA.persist(singerA);
//			    if(true) throw new JpaSystemException(new PersistenceException("Simulation of something going wrong."));
                Singer singerB = new Singer();
                singerB.setFirstName("Alex");
                singerB.setLastName("SaveXa");
                singerB.setBirthDate(new GregorianCalendar().getTime());
                emB.persist(singerB);
                {
                    List<Singer> singers = emA.createQuery("from Singer", Singer.class).getResultList();
                    assertEquals(4L, singers.size());
                    listSingersWithAlbum(singers);
                }
                {
                    List<Singer> singers = emB.createQuery("from Singer", Singer.class).getResultList();
                    assertEquals(4L, singers.size());
                    listSingersWithAlbum(singers);
                }
            });
        } catch (JpaSystemException exc) {
            emw.doWithEntityManager((emA, emB) -> {
                {
                    List<Singer> singers = emA.createQuery("from Singer", Singer.class).getResultList();
                    assertEquals(3L, singers.size());
                    listSingersWithAlbum(singers);
                }
                {
                    List<Singer> singers = emB.createQuery("from Singer", Singer.class).getResultList();
                    assertEquals(3L, singers.size());
                    listSingersWithAlbum(singers);
                }
            });
        }
        logger.info("\n\n\n");
    }

    private static void listSingers(List<Singer> singers) {
        logger.info(" ---- Listing singers:");
        for (Singer singer : singers) {
            logger.info(singer.toString());
        }
    }

    private static void listSingersWithAlbum(List<Singer> singers) {
        logger.info(" ---- Listing singers with instruments:");
        singers.forEach(s -> {
            logger.info(s.toString());
            if (s.getAlbums() != null) {
                s.getAlbums().forEach(a -> logger.info("\t" + a.toString()));
            }
            if (s.getInstruments() != null) {
                s.getInstruments().forEach(i -> logger.info("\tInstrument: " + i.getInstrumentId()));
            }
        });
    }

    @After
    public void tearDown() {
        ctx.close();
    }
}
