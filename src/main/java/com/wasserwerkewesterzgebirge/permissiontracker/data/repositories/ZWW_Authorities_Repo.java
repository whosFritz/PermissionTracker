package com.wasserwerkewesterzgebirge.permissiontracker.data.repositories;

import com.wasserwerkewesterzgebirge.permissiontracker.data.entities.ZWW_Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ZWW_Authorities_Repo extends JpaRepository<ZWW_Authority, Long> {
    @Query("select c from ZWW_Authority c " +
            "where lower(c.name) like lower(concat('%', :searchTerm, '%')) " +
            "or lower(c.description) like lower(concat('%', :searchTerm, '%'))")
    List<ZWW_Authority> search(@Param("searchTerm") String searchTerm);
}
