package com.wasserwerkewesterzgebirge.permissiontracker.data.repositories;

import com.wasserwerkewesterzgebirge.permissiontracker.data.entities.PermissionRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Repository for the permission requests
 * <p>
 * This interface is used to store the permission requests in the database.
 * <p>
 * There are methods to find a permission request
 * <p>
 * - by yesCode
 * <p>
 * or
 * <p>
 * - by noCode.
 */
@Repository
public interface PermissionRequestRepository extends JpaRepository<PermissionRequest, Long> {
    PermissionRequest findByYesCode(String yesCode);

    PermissionRequest findByNoCode(String noCode);
}


