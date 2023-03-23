package com.example.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entities.Estudiante;
import com.example.entities.Telefono;


public interface TelefonoDao extends JpaRepository<Telefono, Integer> {

    //@Query(value="delete from telefonos where estudiante_id=:idEstudiante", nativeQuery=true)
    //public void deleteByEstudiante(Integer, idEstudiante); 
    //long deleteByIdEstudiante (@Param("idEstudiante") Integer idEstudiante); 

    long deleteByEstudiante(Estudiante estudiante); 
}
