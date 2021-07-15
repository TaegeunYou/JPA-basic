package jpql;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello"); //db당 하나만

        EntityManager em = emf.createEntityManager();   //고객의 요청이 올때마다

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
            em.persist(member);

            Member result1 = em.createQuery("select m from Member m where m.username = :username", Member.class)
                    .setParameter("username", "member1")
                    .getSingleResult();

            System.out.println("result1 = " + result1.getUsername());

            List<Member> result2 = em.createQuery("select m from Member m", Member.class)
                    .getResultList();

            List<Team> result3 = em.createQuery("select t from Member m join m.team t", Team.class)
                    .getResultList();

            em.createQuery("select o.address from Member m join Order o", Address.class)
                    .getResultList();

            em.createQuery("select m.username, m.age from Member m")
                    .getResultList();

            List<Object[]> resultList = em.createQuery("select m.username, m.age from Member m")
                    .getResultList();

            List<MemberDTO> resultDTO = em.createQuery("select new jpql.MemberDTO(m.username, m.age) from Member m", MemberDTO.class)
                    .getResultList();

            MemberDTO memberDTO = resultDTO.get(0);
            System.out.println("memberDTO = " + memberDTO.getUsername());
            System.out.println("memberDTO = " + memberDTO.getAge());

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();

    }
}
