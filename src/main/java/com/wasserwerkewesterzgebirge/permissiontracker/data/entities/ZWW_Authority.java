package com.wasserwerkewesterzgebirge.permissiontracker.data.entities;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ZWW_Authorities")
@Data
@NoArgsConstructor
public class ZWW_Authority {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    protected Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "beschreibung", nullable = false)
    private String description;
    @Column(name = "Kategorie")
    private String category;

    public ZWW_Authority(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
