package repo.db;

import entity.Singer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SingerRepository extends CrudRepository<Singer, Long> {
    @Query("select distinct s from Singer s left join fetch s.albums a left join fetch s.instruments i")
    List<Singer> findAllWithAlbums();
    List<Singer> findByFirstName(String firstName);
    List<Singer> findByFirstNameAndLastName(String firstName, String lastName);
    @Query("select distinct i.instrumentId, s.firstName, s.lastName, s.birthDate from Singer s join s.albums a join s.instruments i where i.instrumentId like %:instrumentId%")
    List<Object[]> findByInstrumentId(@Param("instrumentId") String iId);
}
