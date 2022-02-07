package util.dbhibernateapi;

import entity.Album;
import entity.Instrument;
import entity.Singer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.GregorianCalendar;

@Service
public class DBHibernateApiInitializer {
    private Logger logger = LoggerFactory.getLogger(DBHibernateApiInitializer.class);

    @Autowired
    SessionFactory sessionFactory;

    @PostConstruct
    public void initDB(){
        logger.info("\n\n\nStarting database initialization...");

        try(Session session=sessionFactory.openSession()){
            Transaction transaction=session.beginTransaction();

            Instrument guitar = new Instrument();
            guitar.setInstrumentId("Guitar");
            session.saveOrUpdate(guitar);
            Instrument piano = new Instrument();
            piano.setInstrumentId("Piano");
            session.saveOrUpdate(piano);
            Instrument voice = new Instrument();
            voice.setInstrumentId("Voice");
            session.saveOrUpdate(voice);
            Singer singer = new Singer();

            singer.setFirstName("John");
            singer.setLastName("Mayer");
            singer.setBirthDate(new GregorianCalendar(1977, 9, 16).getTime());
            singer.getInstruments().add(guitar);
            singer.getInstruments().add(piano);
            Album album1 = new Album();
            album1.setTitle("The Search For Everything");
            album1.setReleaseDate(new GregorianCalendar(2017, 0, 20).getTime());
            singer.addAlbum(album1);
            Album album2 = new Album();
            album2.setTitle("Battle Studies");
            album2.setReleaseDate(new GregorianCalendar(2009, 10, 17).getTime());
            singer.addAlbum(album2);
            session.save(singer);

            singer = new Singer();
            singer.setFirstName("Eric");
            singer.setLastName("Clapton");
            singer.setBirthDate(new GregorianCalendar(1945, 2, 30).getTime());
            singer.getInstruments().add(guitar);
            Album album = new Album();
            album.setTitle("From The Cradle");
            album.setReleaseDate(new GregorianCalendar(1994, 8, 13).getTime());
            singer.addAlbum(album);
            session.save(singer);

            singer = new Singer();
            singer.setFirstName("John");
            singer.setLastName("Butler");
            singer.setBirthDate(new GregorianCalendar(1975, 3, 1).getTime());
            singer.getInstruments().add(guitar);
            session.save(singer);

            transaction.commit();

            logger.info("Database initialization finished.\n\n\n");
        }
    }
}
