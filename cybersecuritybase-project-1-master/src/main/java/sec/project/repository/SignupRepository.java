package sec.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sec.project.domain.Signup;

import java.util.List;

public interface SignupRepository extends JpaRepository<Signup, Long>, SignupRepositoryCustom {

    List<Signup> findByUser(String user);
}
