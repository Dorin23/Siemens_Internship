package com.siemens.internship;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

//Interfata care defineste repository-ul pentru entitatea Item, cu cheia primara de tip Long
public interface ItemRepository extends JpaRepository<Item, Long> {
    //Interogare personalizata scrisa in JPQl
    //Selecteaza doar campul id din toate obiectele item
    @Query("SELECT id FROM Item")
    List<Long> findAllIds(); //returneaza o lista cu toate valorile id din tabelul Item
}
