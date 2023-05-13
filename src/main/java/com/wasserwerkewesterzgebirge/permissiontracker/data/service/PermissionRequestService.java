package com.wasserwerkewesterzgebirge.permissiontracker.data.service;

import com.wasserwerkewesterzgebirge.permissiontracker.data.entities.PermissionRequest;
import com.wasserwerkewesterzgebirge.permissiontracker.data.repositories.PermissionRequestRepository;
import org.springframework.stereotype.Service;

@Service
public class PermissionRequestService {
    private final PermissionRequestRepository permissionRequestRepository;

    public PermissionRequestService(PermissionRequestRepository permissionRequestRepository) {
        this.permissionRequestRepository = permissionRequestRepository;
    }

    public PermissionRequest findByYesCode(String yesCode) {
        return permissionRequestRepository.findByYesCode(yesCode);
    }

    public PermissionRequest findByNoCode(String noCode) {
        return permissionRequestRepository.findByNoCode(noCode);
    }

    public void saveONE_PermissionRequest(PermissionRequest permissionRequest_to_be_saved) {
        permissionRequestRepository.save(permissionRequest_to_be_saved);
    }

}