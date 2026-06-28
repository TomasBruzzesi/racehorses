package daos;

import config.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import schemas.Horse;
import schemas.Player;

//Acceso a datos de jugadores con JPA/Hibernate.
public class PlayerDAO {

    private final JPAUtil jpaUtil;

    public PlayerDAO() {
        this.jpaUtil = JPAUtil.getInstance();
    }

    //Método para insertar un jugador y asigna el id generado.
    public int insert(Player player) {
        EntityManager entityManager = jpaUtil.crearEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(player);
            entityManager.getTransaction().commit();
            return player.getId();
        } catch (RuntimeException e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new RuntimeException("Error al insertar jugador.", e);
        } finally {
            entityManager.close();
        }
    }

    //Método para actualizar un jugador existente.
    public void update(Player player) {
        EntityManager entityManager = jpaUtil.crearEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(player);
            entityManager.getTransaction().commit();
        } catch (RuntimeException e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new RuntimeException("Error al actualizar jugador.", e);
        } finally {
            entityManager.close();
        }
    }

    //Método para buscar un jugador por su email.
    public Player findByEmail(String email) {
        EntityManager entityManager = jpaUtil.crearEntityManager();
        try {
            return entityManager
                    .createQuery("SELECT p FROM Player p WHERE p.email = :email", Player.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (RuntimeException e) {
            throw new RuntimeException("Error al buscar jugador por email.", e);
        } finally {
            entityManager.close();
        }
    }

    //Método para asignar el caballo seleccionado de un jugador.
    public void updateSelectedHorse(int playerId, int horseId) {
        EntityManager entityManager = jpaUtil.crearEntityManager();
        try {
            entityManager.getTransaction().begin();

            Player player = entityManager.find(Player.class, playerId);
            if (player == null) {
                throw new RuntimeException("Jugador no encontrado: " + playerId);
            }

            Horse horse = entityManager.getReference(Horse.class, horseId);
            player.selectHorse(horse);

            entityManager.getTransaction().commit();
        } catch (RuntimeException e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new RuntimeException("Error al actualizar caballo seleccionado.", e);
        } finally {
            entityManager.close();
        }
    }
}
