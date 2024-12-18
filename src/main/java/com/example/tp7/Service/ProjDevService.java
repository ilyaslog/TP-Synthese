package com.example.tp7.Service;

import com.example.tp7.DAO.ProjDevRepository;
import com.example.tp7.entity.ProjDev;

public class ProjDevService {

    private final ProjDevRepository projDevRepository;

    public ProjDevService(ProjDevRepository projDevRepository) {
        this.projDevRepository = projDevRepository;
    }

    public void saveReview(ProjDev projDev) {
        projDevRepository.save(projDev);
    }



}