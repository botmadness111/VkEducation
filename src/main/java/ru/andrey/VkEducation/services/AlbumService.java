package ru.andrey.VkEducation.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.andrey.VkEducation.models.album.Album;
import ru.andrey.VkEducation.repositories.AlbumRepository;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class AlbumService {
    private final AlbumRepository albumRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public AlbumService(ModelMapper modelMapper, AlbumRepository albumRepository) {
        this.albumRepository = albumRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public Optional<Album> save(Album album) {
        Optional<Album> albumOptional = albumRepository.findByIdvk(album.getIdvk());

        if (albumOptional.isEmpty()) {
            albumRepository.save(album);
            albumOptional = Optional.of(album);
        }

        return albumOptional;
    }

    @Transactional
    public Optional<Album> update(Album albumUpd) {
        Optional<Album> albumOptional = albumRepository.findByIdvk(albumUpd.getIdvk());

        Album album = albumOptional.orElseThrow(() -> new RuntimeException("There is no such album in the database"));

        modelMapper.map(albumUpd, album);

        Album updatedAlbum = albumRepository.save(album);
        return Optional.of(updatedAlbum);
    }

    @Transactional
    public void delete(Album albumDeleted) {
        albumRepository.deleteByIdvk(albumDeleted.getIdvk());
    }
}
