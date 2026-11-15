package com.epam.rd.autocode.spring.project.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "blocked_clients")
@Getter
@Setter
@NoArgsConstructor
public class BlockedClient {

    @Id
    private Long clientId;

    @OneToOne(optional = false)
    @MapsId
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    public BlockedClient(Client client) {
        this.client = client;
    }
}
