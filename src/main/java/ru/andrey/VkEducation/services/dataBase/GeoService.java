package ru.andrey.VkEducation.services.dataBase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.andrey.VkEducation.models.user.dependencies.Geo;
import ru.andrey.VkEducation.repositories.GeoRepository;


@Service
@Transactional(readOnly = true)
public class GeoService {
    private final GeoRepository geoRepository;

    @Autowired
    public GeoService(GeoRepository geoRepository) {
        this.geoRepository = geoRepository;
    }

    @Transactional
    public void save(Geo geo) {
        geoRepository.save(geo);
    }
}
