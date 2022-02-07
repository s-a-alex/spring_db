package util;

import config.AppConfig;
import dao.db.SingerDao;
import dao.db.SingerService;
import entity.Album;
import entity.Instrument;
import entity.Singer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import util.db.EntityManagerWrapper;

import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@SuppressWarnings("unchecked")
public class SingerDaoTest {
	private static Logger logger = LoggerFactory.getLogger(SingerDaoTest.class);

	private GenericApplicationContext ctx;
	private SingerDao singerDao;
	private EntityManagerWrapper emw;
	private SingerService singerService;

	@Before
	public void setUp() {
		ctx = new AnnotationConfigApplicationContext(AppConfig.class);
		singerDao=ctx.getBean(SingerDao.class);
		assertNotNull(singerDao);
		emw=ctx.getBean(EntityManagerWrapper.class);
		assertNotNull(emw);
		singerService=ctx.getBean(SingerService.class);
		assertNotNull(singerService);
	}

	@Test
	public void testFindAll() {
		logger.info("testFindAll");
		emw.doWithEntityManager(em -> {
			List<Singer> singers = em.createQuery("from Singer", Singer.class).getResultList();
			assertEquals(3, singers.size());
			listSingersWithAlbum(singers);
		});
		emw.doWithEntityManager(em -> {
			List<Singer> singers = em.createNativeQuery("select id, firstName, lastName, birthDate, version from singer", "singerResult").getResultList();
			assertEquals(3, singers.size());
			listSingersWithAlbum(singers);
		});
		List<Singer> singers = singerDao.findAll();
		assertEquals(3, singers.size());
		listSingersWithAlbum(singers);
		singers = singerService.findAllWithAlbums();
		assertEquals(3, singers.size());
		listSingersWithAlbum(singers);
		singers = singerService.findAll();
		assertEquals(3, singers.size());
		listSingers(singers);
		logger.info("\n\n\n");
	}

	@Test
	public void testFindByID() {
		logger.info("testFindByID");
		emw.doWithEntityManager(em -> {
			Singer singer = em.find(Singer.class, 1L);
			assertNotNull(singer);
			listSingersWithAlbum(Arrays.asList(singer));
		});
		Singer singer = singerDao.findById(1L);
		assertNotNull(singer);
		listSingersWithAlbum(Arrays.asList(singer));
		logger.info("\n\n\n");
	}

	@Test
	public void testFindByID_Named() {
		logger.info("testFindByID_Named");
		emw.doWithEntityManager(em -> {
			assertEquals(1, em.createNamedQuery("singer.findById").setParameter("id", 1L).getResultList().size());
			listSingersWithAlbum(em.createNamedQuery("singer.findById").setParameter("id", 1L).getResultList());
		});
		logger.info("\n\n\n");
	}

	@Test
	public void testFindByDataJpa() {
		{
			logger.info("findByFirstName");
			List<Singer> singers = singerService.findByFirstName("John");
			assertEquals(2, singers.size());
			listSingers(singers);
		}
		{
			logger.info("findByFirstNameAndLastName");
			List<Singer> singers = singerService.findByFirstNameAndLastName("John", "Mayer");
			assertEquals(1, singers.size());
			listSingers(singers);
		}
		{
			logger.info("findByInstrumentId");
			List<Object[]> singers = singerService.findByInstrumentId("Voice");
			assertEquals(0, singers.size());
			for (Object[] s : singers) {
				logger.info(s[0] + " " + s[1] + " " + s[2] + " " + s[3]);
			}
		}
		logger.info("\n\n\n");
	}

	@Test
	public void testInsert() {
		logger.info("testInsert");
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

		Instrument instrument = new Instrument();
		instrument.setInstrumentId("Bayan");
		emw.doWithEntityManager(em -> em.persist(instrument));
		singer.getInstruments().add(instrument);

		singerDao.save(singer);
		assertNotNull(singer.getId());

		List<Singer> singers = singerDao.findAll();
		assertEquals(4, singers.size());
		listSingersWithAlbum(singers);
		logger.info("\n\n\n");
	}

	@Test
	public void testUpdate() {
		logger.info("testUpdate");
		emw.doWithEntityManager(em -> {
			listSingersWithAlbum(em.createQuery("from Singer", Singer.class).getResultList());

			Singer singer = em.find(Singer.class, 1L);
			assertNotNull(singer);
			assertEquals("Mayer", singer.getLastName());
			Album album = singer.getAlbums().stream().filter(a -> a.getTitle().equals("Battle Studies")).findFirst().get();

			singer.setFirstName("John Clayton");
			singer.getAlbums().remove(album);

			listSingersWithAlbum(em.createQuery("from Singer", Singer.class).getResultList());
		});

		Singer singer = singerDao.findById(1L);
		assertNotNull(singer);
		assertEquals("Mayer", singer.getLastName());
		Album album = singer.getAlbums().stream().filter(a -> a.getTitle().equals("The Search For Everything")).findFirst().get();

		singer.setFirstName("Tom");
		singer.getAlbums().remove(album);
		singerDao.save(singer);

		listSingersWithAlbum(singerDao.findAll());
		logger.info("\n\n\n");
	}

	@Test
	public void testDelete() {
		logger.info("testDelete");
		emw.doWithEntityManager(em -> {
			listSingersWithAlbum(em.createQuery("from Singer", Singer.class).getResultList());
			Singer singer = em.find(Singer.class, 3L);
			assertNotNull(singer);
			em.remove(singer);

			listSingersWithAlbum(em.createQuery("from Singer", Singer.class).getResultList());
		});
		singerDao.delete(1L);
		listSingersWithAlbum(singerDao.findAll());
		singerDao.delete(singerDao.findById(2L));
		listSingersWithAlbum(singerDao.findAll());
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
