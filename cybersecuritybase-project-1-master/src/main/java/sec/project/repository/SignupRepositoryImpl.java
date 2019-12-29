package sec.project.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sec.project.domain.Signup;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

public class SignupRepositoryImpl implements SignupRepositoryCustom {

    private static Logger log = LoggerFactory.getLogger(SignupRepositoryImpl.class);

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public Integer insecureSave(Signup signup) {

        // 2013-A1-Injection
        // the query used by this method is vulnerable to SQL injection

        StringBuilder sql = new StringBuilder("INSERT INTO Signup (name, address, user) VALUES ('");
        sql.append(signup.getName());
        sql.append("', '");
        sql.append(signup.getAddress());
        sql.append("', '");
        sql.append(signup.getUser());
        sql.append("')");

        log.info("Executing native query: {}", sql.toString());

        Query nativeQuery = em.createNativeQuery(sql.toString());

        return nativeQuery.executeUpdate();
    }

    @Override
    public Signup insecureFindOne(Long id) {

        StringBuilder sql = new StringBuilder("SELECT * FROM Signup WHERE id = ");
        sql.append(id);

        return (Signup) em.createNativeQuery(sql.toString(), Signup.class).getSingleResult();
    }

    @Override
    public List<Signup> insecureFind(String user, String search) {

        StringBuilder sql = new StringBuilder("SELECT * FROM Signup WHERE user = ");
        sql.append("'").append(user).append("'")
                .append(" AND name = '").append(search).append("'");

        log.info("Executing native query: {}", sql.toString());

        return em.createNativeQuery(sql.toString(), Signup.class).getResultList();
    }
}
