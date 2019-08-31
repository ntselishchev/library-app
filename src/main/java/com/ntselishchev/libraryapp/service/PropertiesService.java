package com.ntselishchev.libraryapp.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Getter
@Setter
public class PropertiesService {

    @Value("${spring.data.mongodb.host}")
    private String mongoDbHost;
    @Value("${spring.data.mongodb.port}")
    private String mongoDbPort;
    @Value("${spring.data.mongodb.database}")
    private String mongoDbDataBase;

}
