package ru.andrey.VkEducation.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.andrey.VkEducation.models.user.dependencies.Geo;
import ru.andrey.VkEducation.repositories.GeoRepository;

import java.util.Optional;


@Service
@Transactional(readOnly = true)
public class GeoService {
    private final GeoRepository geoRepository;

    @Autowired
    public GeoService(GeoRepository geoRepository) {
        this.geoRepository = geoRepository;
    }

    @Transactional
    public Optional<Geo> save(Geo geo) {
        Optional<Geo> geoOptional = geoRepository.findByLatAndLng(geo.getLat(), geo.getLng());
        if (geoOptional.isEmpty()) {
            geoRepository.save(geo);
            geoOptional = Optional.of(geo);
        }


        return geoOptional;
    }
}
