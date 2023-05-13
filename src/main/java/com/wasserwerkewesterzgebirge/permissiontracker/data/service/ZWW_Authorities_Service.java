package com.wasserwerkewesterzgebirge.permissiontracker.data.service;

import com.wasserwerkewesterzgebirge.permissiontracker.data.entities.ZWW_Authority;
import com.wasserwerkewesterzgebirge.permissiontracker.data.repositories.ZWW_Authorities_Repo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ZWW_Authorities_Service {
    private final ZWW_Authorities_Repo zww_authorities_repo;

    public ZWW_Authorities_Service(ZWW_Authorities_Repo zww_authorities_repo) {
        this.zww_authorities_repo = zww_authorities_repo;
    }

    public List<ZWW_Authority> findAllAuthorities() {
        return zww_authorities_repo.findAll();
    }

    public void saveAll(List<ZWW_Authority> zwwAuthoritiesList) {
        zww_authorities_repo.saveAll(zwwAuthoritiesList);
    }
    public List<ZWW_Authority> searchForAuthorities(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return zww_authorities_repo.findAll();
        }
        else {
            return zww_authorities_repo.search(stringFilter);
        }
    }
}
