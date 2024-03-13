package ru.andrey.VkEducation.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.andrey.VkEducation.models.user.dependencies.Geo;

import java.util.Optional;

@Repository
public interface GeoRepository extends JpaRepository<Geo, Integer> {
    Optional<Geo> findByLatAndLng(String lat, String lng);
}
