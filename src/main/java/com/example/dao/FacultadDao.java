package com.example.dao;

import org.springframework.data.jpa.repository.JpaRepository;

/*jajaja */
import org.springframework.stereotype.Repository;

import com.example.entities.Facultad;


public interface FacultadDao extends JpaRepository<Facultad, Integer> {

}
