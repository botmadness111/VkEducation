package ru.andrey.VkEducation.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.andrey.VkEducation.models.user.dependencies.Geo;

@Repository
public interface GeoRepository extends JpaRepository<Geo, Integer> {
}
