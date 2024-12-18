package com.example.tp7.Service;

import com.example.tp7.DAO.ProjDevRepository;
import com.example.tp7.entity.ProjDev;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ProjDevService {

    private final ProjDevRepository projDevRepository;

    public ProjDevService(ProjDevRepository projDevRepository) {
        this.projDevRepository = projDevRepository;
    }

    public void saveReview(ProjDev projDev) {
        projDevRepository.save(projDev);
    }



}
