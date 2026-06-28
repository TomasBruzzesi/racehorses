package daos;

import java.util.List;

import config.JPAUtil;
import horses.BalancedHorse;
import horses.EnduranceHorse;
import horses.FastHorse;
import jakarta.persistence.EntityManager;
import schemas.Horse;

//Acceso a datos de caballos con JPA/Hibernate.
public class HorseDAO {

    private final JPAUtil jpaUtil;

    public HorseDAO() {
        this.jpaUtil = JPAUtil.getInstance();
    }

    //Método para insertar un caballo y asigna el id generado.
    public int insert(Horse horse) {
        EntityManager entityManager = jpaUtil.crearEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(horse);
            entityManager.getTransaction().commit();
            return horse.getId();
        } catch (RuntimeException e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new RuntimeException("Error al insertar caballo.", e);
        } finally {
            entityManager.close();
        }
    }

    //Método para borrar el catalogo actual y cargar los caballos iniciales.
    public void seedDefaults() {
        EntityManager entityManager = jpaUtil.crearEntityManager();
        try {
            entityManager.getTransaction().begin();

            entityManager.createQuery("UPDATE Player p SET p.selectedHorse = null").executeUpdate();
            entityManager.createQuery("DELETE FROM Horse").executeUpdate();

            persistDefaultHorses(entityManager);

            entityManager.getTransaction().commit();
        } catch (RuntimeException e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new RuntimeException("Error al cargar caballos iniciales.", e);
        } finally {
            entityManager.close();
        }
    }

    private void persistDefaultHorses(EntityManager entityManager) {
        entityManager.persist(new FastHorse("Thunder", 6.54, 96.0, 0.36));
        entityManager.persist(new BalancedHorse("Storm", 5.52, 108.0, 1.0));
        entityManager.persist(new EnduranceHorse("Blaze", 4.74, 120.0, 0.5));
        entityManager.persist(new FastHorse("Bolt", 6.54, 90.0, 0.35));
        entityManager.persist(new BalancedHorse("Mistral", 5.16, 114.0, 1.1));
        entityManager.persist(new EnduranceHorse("Shadow", 4.62, 126.0, 0.55));
        entityManager.persist(new FastHorse("Flash", 6.66, 86.0, 0.38));
        entityManager.persist(new BalancedHorse("Comet", 5.40, 106.0, 1.05));
        entityManager.persist(new EnduranceHorse("Atlas", 4.80, 118.0, 0.48));
        entityManager.persist(new FastHorse("Rocket", 6.42, 94.0, 0.34));
    }

    //Método para obtener todos los caballos del catalogo.
    public List<Horse> findAll() {
        EntityManager entityManager = jpaUtil.crearEntityManager();
        try {
            return entityManager
                    .createQuery("SELECT h FROM Horse h ORDER BY h.id", Horse.class)
                    .getResultList();
        } catch (RuntimeException e) {
            throw new RuntimeException("Error al listar caballos.", e);
        } finally {
            entityManager.close();
        }
    }
}
