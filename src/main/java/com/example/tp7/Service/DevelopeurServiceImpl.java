package com.example.tp7.Service;

import com.example.tp7.DAO.DevelopeurRepository;
import com.example.tp7.entity.Developeur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DevelopeurServiceImpl implements DevelopeurService {
    @Autowired
    private DevelopeurRepository developeurRepository;

    public DevelopeurServiceImpl() {
        super();
    }
    @Autowired
    public DevelopeurServiceImpl(DevelopeurRepository developeurRepository) {
        this.developeurRepository = developeurRepository;
    }
    @Override
    public List<Developeur> findAll() {
        return developeurRepository.findAll();
    }

    @Override
    public Developeur findById(int id) {
        return developeurRepository.findById(id).orElse(null);
    }

    @Override
    public Developeur save(Developeur developeur) {
        return developeurRepository.save(developeur);
    }

    @Override
    public void deleteById(int id) {
         developeurRepository.deleteById(id);
    }

    

}
