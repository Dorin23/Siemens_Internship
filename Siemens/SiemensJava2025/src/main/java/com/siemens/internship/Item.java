package com.siemens.internship;

import jakarta.persistence.*;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity //specifica faptul ca aceasta clasa este o entitate JPA
@Getter //creeaza automat getter-ele
@Setter //creeaza automat setter-ele
@AllArgsConstructor //creeaza automat un constructor cu toti parametrii()id, name, description, status, email
@NoArgsConstructor //creeaza un constructor fara parametri
public class Item {
    @Id //adnotarea @ID marcheaza ca coloana id din bd sa fie cheie primara
    @GeneratedValue(strategy = GenerationType.AUTO) //se genereaza automat valoare pentru id folosind strategia auto hibernate
    private Long id;
    @NotBlank(message = "The name must not be empty! ") //camp obligatoriu
    private String name;
    private String description; //descrierea este optionala
    private String status; //starea item-ului

    @NotBlank(message = "The email must not be empty!") //camp obligatoriu
    @Email(message = "The email is not valid!") //verificam daca emailul este intr-un format corect
    @Column(unique = true) //emailul este unic
    private String email;
}