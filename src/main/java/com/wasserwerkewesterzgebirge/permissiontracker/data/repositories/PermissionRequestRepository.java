package com.wasserwerkewesterzgebirge.permissiontracker.data.repositories;

import com.wasserwerkewesterzgebirge.permissiontracker.data.entities.PermissionRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRequestRepository extends JpaRepository<PermissionRequest, Long> {
    PermissionRequest findByYesCode(String yesCode);

    PermissionRequest findByNoCode(String noCode);
}


