/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.allamiflorencia.Portfolio.service;

import com.allamiflorencia.Portfolio.DTO.InfoDTO;
import com.allamiflorencia.Portfolio.DTO.PseudoInfoDTO;
import com.allamiflorencia.Portfolio.DTO.PseudoSeccionDTO;
import com.allamiflorencia.Portfolio.DTO.SeccionDTO;
import com.allamiflorencia.Portfolio.model.Info;
import com.allamiflorencia.Portfolio.model.Person;
import com.allamiflorencia.Portfolio.model.Seccion;
import com.allamiflorencia.Portfolio.model.Tipo;
import com.allamiflorencia.Portfolio.repository.InfoRepository;
import com.allamiflorencia.Portfolio.repository.PersonRepository;
import com.allamiflorencia.Portfolio.repository.SeccionRepository;
import com.allamiflorencia.Portfolio.repository.TipoRepository;
import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.SqlResultSetMapping;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.lang.String;

/**
 *
 * @author flopy
 */
@Service
public class PortfolioService implements IPortfolioService {
    
    @Autowired
    private TipoRepository tipoRepo;
    
    @Autowired
    private SeccionRepository secRepo;
    
    @Autowired
    private InfoRepository infoRepo;
    
    @Autowired
    private PersonRepository persRepo;
    
    @Autowired
    private EntityManager em;

    @Override
    public void crearTipo(String nombre) {
        Tipo tipo = new Tipo();
        tipo.setTipo(nombre);
        tipoRepo.save(tipo);
    }

    @Override
    public void crearSeccion(PseudoSeccionDTO pseudo_seccion) {
        Seccion seccion = new Seccion();
        seccion.setTitulo(pseudo_seccion.getTitulo());
        
        Tipo tipo = tipoRepo.findTipoByNombre(pseudo_seccion.getTipo());
        seccion.setTipo(tipo);
        
        secRepo.save(seccion);
    }

    @Override
    public void crearInfo(PseudoInfoDTO pseudo_info) {
        Info info = new Info();
        info.setDescripcion(pseudo_info.getDescripcion());
        info.setTitulo(pseudo_info.getTitulo());
        info.setLink(pseudo_info.getLink());
        
        Seccion seccion = secRepo.findSeccionByNombre(pseudo_info.getSeccion());
        info.setSeccion(seccion);
        
        infoRepo.save(info);
    }

    @Override
    public void crearPerson(Person person) {
        persRepo.save(person);
    }

    @Override
    public Person traerPerson() {
        return persRepo.findAll().get(0);
    }

    @Override
    public List<SeccionDTO> traerSeccionesDTO() {
        List<Seccion> secciones = secRepo.findAll();
        List<SeccionDTO> rta = new ArrayList();
        
        for (Seccion s : secciones){
            SeccionDTO sdto = new SeccionDTO();
            sdto.setTitulo(s.getTitulo());
            sdto.setTipo(s.getTipo().getTipo());
            
            List<Object[]> infos = infoRepo.findDTOBySeccion(s.getId());
            List<InfoDTO> data = new ArrayList();
            
            for(Object[] fila : infos){
                InfoDTO info = new InfoDTO();
                info.setTitulo((String) fila[0]);
                info.setLink((String) fila[1]);
                info.setDescripcion((String) fila[2]);
                data.add(info);
            }
            
            sdto.setData(data);
            rta.add(sdto);
        }
        //return rta;
        return rta;
    }

    @Override
    public List<Tipo> traerTipos() {
        return tipoRepo.findAll();
    }

    @Override
    public List<Seccion> traerSecciones() {
        return secRepo.findAll();
    }

    @Override
    public List<Info> traerInfo() {
        return infoRepo.findAll();
    }


    
}
