package util.dbxa;

import javax.persistence.EntityManager;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface EntityManagerWrapper {
    public void doWithEntityManager(BiConsumer<EntityManager, EntityManager> command);
}
