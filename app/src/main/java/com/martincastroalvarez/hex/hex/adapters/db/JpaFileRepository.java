package com.martincastroalvarez.hex.hex.adapters.db;

import com.martincastroalvarez.hex.hex.adapters.entities.FileEntity;
import com.martincastroalvarez.hex.hex.adapters.mappers.FileMapper;
import com.martincastroalvarez.hex.hex.domain.models.File;
import com.martincastroalvarez.hex.hex.domain.ports.out.FileRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaFileRepository extends JpaRepository<FileEntity, Integer>, FileRepository {
    @Override
    default File save(File file) {
        return FileMapper.toFile(save(FileMapper.toFileEntity(file)));
    }

    @Override
    default Optional<File> get(Integer id) {
        return findById(id).map(FileMapper::toFile);
    }
}
