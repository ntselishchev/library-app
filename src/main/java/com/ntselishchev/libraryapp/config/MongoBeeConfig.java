package com.ntselishchev.libraryapp.config;

import com.github.mongobee.Mongobee;
import com.ntselishchev.libraryapp.service.DataBaseProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class MongoBeeConfig {

    private final DataBaseProperties props;

    @Bean
    public Mongobee mongobee(){
        Mongobee runner = new Mongobee(
                "mongodb://" +
                props.getUsername() + ":" +
                props.getPassword() + "@" +
                props.getHost() + ":" +
                props.getPort() + "/" +
                props.getAuthenticationDatabase()
        );
        runner.setDbName(props.getDatabase());
        runner.setChangeLogsScanPackage(
                "com.ntselishchev.libraryapp.changelogs");
        return runner;
    }
}
