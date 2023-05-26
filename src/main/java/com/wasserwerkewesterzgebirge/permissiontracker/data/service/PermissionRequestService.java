package com.wasserwerkewesterzgebirge.permissiontracker.data.service;

import com.wasserwerkewesterzgebirge.permissiontracker.data.entities.PermissionRequest;
import com.wasserwerkewesterzgebirge.permissiontracker.data.repositories.PermissionRequestRepository;
import org.springframework.stereotype.Service;


/**
 * This is the service for the permission requests.
 * It contains the methods to save and find permission requests.
 */
@Service
public class PermissionRequestService {
    /**
     * The repository for permission requests.
     */
    private final PermissionRequestRepository permissionRequestRepository;

    /**
     * Constructor for the service.
     *
     * @param permissionRequestRepository The repository for permission requests.
     */
    public PermissionRequestService(PermissionRequestRepository permissionRequestRepository) {
        this.permissionRequestRepository = permissionRequestRepository;
    }

    /**
     * This method finds a permission request by its yesCode by calling the findByYesCode method of the repository.
     *
     * @param yesCode The id of the permission request.
     * @return The permission request with the given yesCode.
     */
    public PermissionRequest findByYesCode(String yesCode) {
        return permissionRequestRepository.findByYesCode(yesCode);
    }

    /**
     * This method finds a permission request by its noCode by calling the findByNoCode method of the repository.
     *
     * @param noCode The id of the permission request.
     * @return The permission request with the given noCode.
     */
    public PermissionRequest findByNoCode(String noCode) {
        return permissionRequestRepository.findByNoCode(noCode);
    }

    /**
     * This method saves a permission request by calling the save method of the repository.
     *
     * @param permissionRequest_to_be_saved The permission request to be saved.
     */
    public void saveONE_PermissionRequest(PermissionRequest permissionRequest_to_be_saved) {
        permissionRequestRepository.save(permissionRequest_to_be_saved);
    }

}