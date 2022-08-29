package com.example.hibernatetest.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
@NoArgsConstructor
@RequiredArgsConstructor

@Entity
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue(generator = "client_id_gen", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "client_id_gen", sequenceName = "client_id_sequence", initialValue = 70000001)
    @Column(name = "client_id")
    private Long id;

    @NonNull
    @Basic(optional = false)
    private String firstName;

    @NonNull
    @Basic(optional = false)
    private String lastName;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({"client"})
    private Set<Address> addresses = new HashSet<>();

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({"client"})
    private Set<Order> orders = new HashSet<>();

    public void addAddress(@NonNull Address address){
        address.setClient(this);
        addresses.add(address);
    }

    public void removeAddress(@NonNull Address address){
        addresses.remove(address);
    }

    public void addOrder(@NonNull Order order){
        order.setClient(this);
        orders.add(order);
    }

    public void removeOrder(@NonNull Order order){
        orders.remove(order);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(id, client.id) && firstName.equals(client.firstName) && lastName.equals(client.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName);
    }

    @Data
    public static class ClientDTO {
        private Long id;
        private String firstName;
        private String lastName;
        private Set<Address> addresses;

        public ClientDTO build (Client client) {
            this.id = client.id;
            this.firstName = client.firstName;
            this.lastName = client.lastName;
            this.addresses = client.addresses;
            return this;
        }
    }

    @Data
    public static class ClientPureDTO {
        private Long id;
        private String firstName;
        private String lastName;

        public ClientPureDTO build(Client client) {
            this.id = client.id;
            this.firstName = client.firstName;
            this.lastName = client.lastName;
            return this;
        }
    }
}
