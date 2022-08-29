package com.example.hibernatetest.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@RequiredArgsConstructor

@Entity
@Table(name = "addresses")
public class Address {

    @Id
    @GeneratedValue(generator = "address_id_gen", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "address_id_gen", sequenceName = "address_id_sequence", initialValue = 10000001)
    @Column(name = "address_id")
    private Long id;

    @NonNull
    @Basic(optional = false)
    private String city;

    @NonNull
    @Basic(optional = false)
    private String street;

    @NonNull
    @Basic(optional = false)
    private String buildingNumber;

    @NonNull
    @Basic(optional = false)
    private String zipCode;

    @ManyToOne(optional = false)
    @JoinColumn(name = "client_id")
    @JsonIgnore
    private Client client;


}
