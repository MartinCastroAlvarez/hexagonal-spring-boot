package com.martincastroalvarez.hex.hex.domain.ports.out;

import com.martincastroalvarez.hex.hex.domain.models.File;
import java.util.Optional;

public interface FileRepository {
    Optional<File> get(Integer id);
    File save(File file);
}