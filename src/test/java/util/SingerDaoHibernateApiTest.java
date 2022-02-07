package util;

import config.AppHibernateApiConfig;
import entity.Album;
import entity.Singer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@SuppressWarnings("unchecked")
public class SingerDaoHibernateApiTest {
    private static Logger logger = LoggerFactory.getLogger(SingerDaoHibernateApiTest.class);

    private GenericApplicationContext ctx;
    private SessionFactory sessionFactory;

    @Before
    public void setUp(){
        ctx = new AnnotationConfigApplicationContext(AppHibernateApiConfig.class);
        sessionFactory = ctx.getBean(SessionFactory.class);
        assertNotNull(sessionFactory);
    }

    @Test
    public void testFindAll(){
        logger.info("testFindAll");
        try(Session session=sessionFactory.openSession()) {
            Transaction tr = session.beginTransaction();
            List<Singer> singers = session.createQuery("from Singer", Singer.class).list();
            assertEquals(3, singers.size());

            listSingersWithAlbum(singers);
            tr.commit();
            logger.info("\n\n\n");
        }
    }

    @Test
    public void testFindByID(){
        logger.info("testFindByID");
        try(Session session=sessionFactory.openSession()) {
            Transaction tr = session.beginTransaction();
            Singer singer = session.load(Singer.class, 1L);
            assertNotNull(singer);

            listSingersWithAlbum(Arrays.asList(singer));
            tr.commit();
            logger.info("\n\n\n");
        }
    }

    @Test
    public void testFindByID_Named(){
        logger.info("testFindByID_Named");
        try(Session session=sessionFactory.openSession()) {
            Transaction tr = session.beginTransaction();
            assertEquals(1, session.getNamedQuery("singer.findById").setParameter("id", 1L).list().size());

            listSingersWithAlbum(session.getNamedQuery("singer.findById").setParameter("id", 1L).list());
            tr.commit();
            logger.info("\n\n\n");
        }
    }

    @Test
    public void testInsert(){
        logger.info("testInsert");
        try(Session session=sessionFactory.openSession()) {
            Transaction tr = session.beginTransaction();
            listSingersWithAlbum(session.createQuery("from Singer", Singer.class).list());
            Singer singer = new Singer();
            singer.setFirstName("BB");
            singer.setLastName("King");
            singer.setBirthDate(new GregorianCalendar(1940, 8, 16).getTime());
            Album album = new Album();
            album.setTitle("My Kind of Blues");
            album.setReleaseDate(new GregorianCalendar(1961, 7, 18).getTime());
            singer.addAlbum(album);
            album = new Album();
            album.setTitle("A Heart Full of Blues");
            album.setReleaseDate(new GregorianCalendar(1962, 3, 20).getTime());
            singer.addAlbum(album);
            session.save(singer);
            assertNotNull(singer.getId());
            List<Singer> singers = session.createQuery("from Singer", Singer.class).list();
            assertEquals(4, singers.size());

            listSingersWithAlbum(singers);
            tr.commit();
            logger.info("\n\n\n");
        }
    }

    @Test
    public void testUpdate() {
        logger.info("testUpdate");
        try (Session session = sessionFactory.openSession()) {
            Transaction tr = session.beginTransaction();
            listSingersWithAlbum(session.createQuery("from Singer", Singer.class).list());
            Singer singer = session.get(Singer.class, 1L);
            assertNotNull(singer);
            assertEquals("Mayer", singer.getLastName());
            Album album = singer.getAlbums().stream().filter(a -> a.getTitle().equals("Battle Studies")).findFirst().get();
            singer.setFirstName("John Clayton");
            singer.getAlbums().remove(album);
            session.save(singer);

            listSingersWithAlbum(session.createQuery("from Singer", Singer.class).list());
            tr.commit();
            logger.info("\n\n\n");
        }
    }

    @Test
    public void testDelete() {
        logger.info("testDelete");
        try (Session session = sessionFactory.openSession()) {
            Transaction tr = session.beginTransaction();
            listSingersWithAlbum(session.createQuery("from Singer", Singer.class).list());
            Singer singer = session.get(Singer.class, 3L);
            assertNotNull(singer);
            session.delete(singer);

            listSingersWithAlbum(session.createQuery("from Singer", Singer.class).list());
            tr.commit();
            logger.info("\n\n\n");
        }
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
    public void tearDown(){
        ctx.close();
    }
}
