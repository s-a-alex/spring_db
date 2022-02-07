package dao.db;

import entity.Singer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repo.db.SingerRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Transactional
public class SingerServiceImpl implements SingerService {
    @Autowired
    private SingerRepository singerRepository;

    @Transactional(readOnly=true)
    @Override
    public List<Singer> findAll() {
        return StreamSupport.stream(singerRepository.findAll().spliterator(), false).collect(Collectors.toList());
    }

    @Transactional(readOnly=true)
    @Override
    public List<Singer> findAllWithAlbums() {
        return StreamSupport.stream(singerRepository.findAllWithAlbums().spliterator(), false).collect(Collectors.toList());
    }

    @Transactional(readOnly=true)
    @Override
    public List<Singer> findByFirstName(String firstName) {
        return singerRepository.findByFirstName(firstName);
    }

    @Transactional(readOnly=true)
    @Override
    public List<Singer> findByFirstNameAndLastName(String firstName, String lastName) {
        return singerRepository.findByFirstNameAndLastName(firstName, lastName);
    }

    @Transactional(readOnly=true)
    @Override
    public List<Object[]> findByInstrumentId(String iId){
        return singerRepository.findByInstrumentId(iId);
    }
}
