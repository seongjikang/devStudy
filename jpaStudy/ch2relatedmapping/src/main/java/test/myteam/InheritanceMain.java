package test.myteam;

import test.myteam.domain.Movie;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class InheritanceMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("relatedmapping");
        EntityManager em = emf.createEntityManager();

        EntityTransaction et = em.getTransaction();
        et.begin();

        try {
            Movie movie = new Movie();
            movie.setDirector("봉준호");
            movie.setActor("송강호");
            movie.setName("기생충");
            movie.setPrice(15000);

            em.persist(movie);

            em.flush();
            em.clear();

            Movie findMove = em.find(Movie.class, movie.getId());
            System.out.println(findMove.getName());

            et.commit();
        } catch (Exception e) {

        } finally {
            em.close();
        }

        emf.close();
    }
}
