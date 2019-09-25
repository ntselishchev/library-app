package com.ntselishchev.libraryapp.config;

import com.ntselishchev.libraryapp.dao.AclDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.acls.AclPermissionEvaluator;
import org.springframework.security.acls.domain.*;
import org.springframework.security.acls.jdbc.LookupStrategy;
import org.springframework.security.acls.model.AclCache;
import org.springframework.security.acls.model.AclService;
import org.springframework.security.acls.model.PermissionGrantingStrategy;
import org.springframework.security.acls.mongodb.BasicLookupStrategy;
import org.springframework.security.acls.mongodb.MongoDBMutableAclService;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Configuration
@Slf4j
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class MongoAclConfig extends GlobalMethodSecurityConfiguration {

    private final AclDao aclRepository;
    private final MongoTemplate mongoTemplate;

    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setPermissionEvaluator(new AclPermissionEvaluator(aclService()));
        return expressionHandler;
    }

    @Bean
    public AclAuthorizationStrategy aclAuthorizationStrategy() {
        return new AclAuthorizationStrategyImpl(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    @Bean
    public PermissionGrantingStrategy permissionGrantingStrategy() {
        ConsoleAuditLogger consoleAuditLogger = new ConsoleAuditLogger();
        return new DefaultPermissionGrantingStrategy(consoleAuditLogger);
    }

    @Bean
    public LookupStrategy lookupStrategy() {
        return new BasicLookupStrategy(mongoTemplate, aclCache(), aclAuthorizationStrategy(), permissionGrantingStrategy());
    }

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("acl_cache");
    }

    @Bean
    public AclCache aclCache() {
        Cache springCache = cacheManager().getCache("acl_cache");
        return new SpringCacheBasedAclCache(springCache, permissionGrantingStrategy(), aclAuthorizationStrategy());
    }

    @Bean
    public AclService aclService() {
        return new MongoDBMutableAclService(aclRepository, lookupStrategy(), aclCache());
    }
}
