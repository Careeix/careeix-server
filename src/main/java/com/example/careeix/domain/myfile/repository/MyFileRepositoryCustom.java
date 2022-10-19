package com.example.careeix.domain.myfile.repository;

import com.example.careeix.domain.myfile.entity.MyFile;

import java.util.Optional;

public interface MyFileRepositoryCustom {
    Optional<MyFile> findOneNotDeletedByFileId(Long fileId);
}
