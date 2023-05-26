package com.wasserwerkewesterzgebirge.permissiontracker.data.service;

import com.wasserwerkewesterzgebirge.permissiontracker.data.entities.ZWW_Authority;
import com.wasserwerkewesterzgebirge.permissiontracker.data.repositories.ZWW_Authorities_Repo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This is the service for the authorities.
 * It contains the methods to save and find authorities.
 */
@Service
public class ZWW_Authorities_Service {

    /**
     * The repository for authorities.
     */
    private final ZWW_Authorities_Repo zww_authorities_repo;

    /**
     * Constructor for the service.
     *
     * @param zww_authorities_repo The repository for authorities.
     */
    public ZWW_Authorities_Service(ZWW_Authorities_Repo zww_authorities_repo) {
        this.zww_authorities_repo = zww_authorities_repo;
    }

    /**
     * This method finds all ZWW_Authorities in the database / repository.
     *
     * @return A List of ZWW_Authority.
     */
    public List<ZWW_Authority> findAllAuthorities() {
        return zww_authorities_repo.findAll();
    }

    /**
     * This method saves a ZWW_Authority by calling the save method of the repository.
     *
     * @param zwwAuthoritiesList The ZWW_Authority to be saved.
     */
    public void saveAll(List<ZWW_Authority> zwwAuthoritiesList) {
        zww_authorities_repo.saveAll(zwwAuthoritiesList);
    }

    /**
     * This method searches for ZWW_Authorities by calling the search method of the repository.
     *
     * @param stringFilter The search string.
     * @return A List of ZWW_Authority.
     */
    public List<ZWW_Authority> searchForAuthorities(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return zww_authorities_repo.findAll();
        } else {
            return zww_authorities_repo.search(stringFilter);
        }
    }
}
