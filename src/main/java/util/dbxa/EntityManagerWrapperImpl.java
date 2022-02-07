package util.dbxa;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.function.BiConsumer;

@Repository
public class EntityManagerWrapperImpl implements EntityManagerWrapper {

    @PersistenceContext(unitName = "emfA")
    private EntityManager emA;
    @PersistenceContext(unitName = "emfB")
    private EntityManager emB;

    @Transactional
    @Override
    public void doWithEntityManager(BiConsumer<EntityManager, EntityManager> command) {
        command.accept(emA, emB);
    }
}
