package com.ntselishchev.libraryapp.config;

import com.github.mongobee.Mongobee;
import com.ntselishchev.libraryapp.service.PropertiesService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class MongoBeeConfig {

    private final PropertiesService props;

    @Bean
    public Mongobee mongobee(){
        Mongobee runner = new Mongobee("mongodb://" +
                props.getMongoDbHost() + ":" +
                props.getMongoDbPort() + "/" +
                props.getMongoDbDataBase());
        runner.setDbName(props.getMongoDbDataBase());
        runner.setChangeLogsScanPackage(
                "com.ntselishchev.libraryapp.changelogs");
        return runner;
    }
}
