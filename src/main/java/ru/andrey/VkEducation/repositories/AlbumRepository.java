package ru.andrey.VkEducation.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.andrey.VkEducation.models.album.Album;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Integer> {
}
