package sec.project.repository;

import sec.project.domain.Signup;

import java.util.List;

public interface SignupRepositoryCustom {

    Integer insecureSave(Signup signup);

    Signup insecureFindOne(Long id);

    List<Signup> insecureFind(String user, String search);
}
