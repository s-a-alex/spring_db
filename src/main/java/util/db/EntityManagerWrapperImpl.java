package util.db;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.function.Consumer;

@Repository
public class EntityManagerWrapperImpl implements EntityManagerWrapper {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    @Override
    public void doWithEntityManager(Consumer<EntityManager> command) {
        command.accept(em);
    }
}
