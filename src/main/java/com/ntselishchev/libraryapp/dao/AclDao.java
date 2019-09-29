package com.ntselishchev.libraryapp.dao;

import org.springframework.security.acls.dao.AclRepository;
import org.springframework.security.acls.domain.MongoAcl;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Repository
public interface AclDao extends AclRepository {

}