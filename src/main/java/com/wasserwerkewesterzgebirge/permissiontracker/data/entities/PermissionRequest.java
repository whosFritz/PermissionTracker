package com.wasserwerkewesterzgebirge.permissiontracker.data.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity class for the permission request
 * <p>
 * This class is used to store the information about a permission request.
 * <p>
 * The information stored are:
 * <p>
 * - requestingPerson: the displayname of the person requesting the permission
 * <p>
 * - requestersMail: the mail of the person requesting the permission
 * <p>
 * - requestedPermissions: the permissions that are requested
 * <p>
 * - sonstiges: additional information about the permission request
 * <p>
 * - datumRequest: the date the permission request was created
 * <p>
 * - datumBoss1: the date the first boss approved the permission request
 * <p>
 * - datumBoss2: the date the second boss approved the permission request
 * <p>
 * - datumBoss3: the date the third boss approved the permission request
 * <p>
 * - datumBoss4: the date the fourth boss approved the permission request
 * <p>
 * - status: the status of the permission request
 * <p>
 * - approvedLevels: the number of levels that are approved
 * <p>
 * - toBeApprovedLevels: the number of levels that are still to be approved
 * <p>
 * - boss1DisplayName: the displayname of the first boss
 * <p>
 * - boss1Mail: the mail of the first boss
 * <p>
 * - boss2DisplayName: the displayname of the second boss
 * <p>
 * - boss2Mail: the mail of the second boss
 * <p>
 * - boss3DisplayName: the displayname of the third boss
 * <p>
 * - boss3Mail: the mail of the third boss
 * <p>
 * - boss4DisplayName: the displayname of the fourth boss
 * <p>
 * - boss4Mail: the mail of the fourth boss
 * <p>
 * - yesCode: the code for approving the permission request
 * <p>
 * - noCode: the code for declining the permission request
 */
@Entity
@Table(name = "permission_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermissionRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "Antragsteller")
    private String requestingPerson;
    @Column(name = "antragsteller_mail")
    private String requestersMail;
    @Column(name = "angefragte Gruppen", columnDefinition = "LONGTEXT")
    private String requestedPermissions;
    // "Name:Gruppenname1,Beschreibung:Gruppenbeschreibung1#Name:Gruppenname2,Beschreibung:Gruppenbeschreibung2";
    @Column(name = "Sonstiges", columnDefinition = "TEXT")
    private String sonstiges;
    @Column(name = "anfrage_datum")
    private String datumRequest;
    @Column(name = "Datum1")
    private String datumBoss1;
    @Column(name = "Datum2")
    private String datumBoss2;
    @Column(name = "Datum3")
    private String datumBoss3;
    @Column(name = "Datum4")
    private String datumBoss4;

    @Column(name = "status")
    private String status;
    @Column(name = "bestätigte Stufen")
    private int approvedLevels;
    @Column(name = "zu bestätigte Stufen")
    private int toBeApprovedLevels;

    @Column(name = "Boss1Name")
    private String boss1DisplayName;
    @Column(name = "Boss1Mail")
    private String boss1Mail;

    @Column(name = "Boss2Name")
    private String boss2DisplayName;
    @Column(name = "Boss2Mail")
    private String boss2Mail;

    @Column(name = "Boss3Name")
    private String boss3DisplayName;
    @Column(name = "Boss3Mail")
    private String boss3Mail;

    @Column(name = "Boss4Name")
    private String boss4DisplayName;
    @Column(name = "Boss4Mail")
    private String boss4Mail;

    @Column(name = "yescode")
    private String yesCode;
    @Column(name = "nocode")
    private String noCode;

    public PermissionRequest(String requestingPerson, String requestersMail, String requestedPermissions, String sonstiges, String datumRequest, String status, int approvedLevels, int toBeApprovedLevels, String boss1DisplayName, String boss1Mail, String boss2DisplayName, String boss2Mail, String boss3DisplayName, String boss3Mail, String boss4DisplayName, String boss4Mail, String yesCode, String noCode) {
        this.requestingPerson = requestingPerson;
        this.requestersMail = requestersMail;
        this.requestedPermissions = requestedPermissions;
        this.sonstiges = sonstiges;
        this.datumRequest = datumRequest;
        this.status = status;
        this.approvedLevels = approvedLevels;
        this.toBeApprovedLevels = toBeApprovedLevels;
        this.boss1DisplayName = boss1DisplayName;
        this.boss1Mail = boss1Mail;
        this.boss2DisplayName = boss2DisplayName;
        this.boss2Mail = boss2Mail;
        this.boss3DisplayName = boss3DisplayName;
        this.boss3Mail = boss3Mail;
        this.boss4DisplayName = boss4DisplayName;
        this.boss4Mail = boss4Mail;
        this.yesCode = yesCode;
        this.noCode = noCode;
    }
}
