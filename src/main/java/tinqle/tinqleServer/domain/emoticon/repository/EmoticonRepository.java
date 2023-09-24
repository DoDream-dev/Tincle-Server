package tinqle.tinqleServer.domain.emoticon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tinqle.tinqleServer.domain.emoticon.model.Emoticon;

public interface EmoticonRepository extends JpaRepository<Emoticon, Long>, EmoticonRepositoryCustom {

}
