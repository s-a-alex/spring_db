package util.db;

import javax.persistence.EntityManager;
import java.util.function.Consumer;

public interface EntityManagerWrapper {
    public void doWithEntityManager(Consumer<EntityManager> command);
}
