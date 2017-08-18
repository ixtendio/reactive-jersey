package com.example.reactive.jersey.infrastructure.config;

import com.example.reactive.jersey.ReactiveJerseyApplication;
import com.google.common.reflect.ClassPath;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletProperties;
import org.springframework.context.annotation.Configuration;

import javax.ws.rs.ApplicationPath;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;

@Configuration
@Slf4j
@ApplicationPath("/api")
public class ApiResourceConfig extends ResourceConfig {

    public ApiResourceConfig() {
        String infrastructurePackageName = ReactiveJerseyApplication.class.getPackage().getName() + ".infrastructure";
        configureJersey(Collections.singleton(infrastructurePackageName));
        property(ServletProperties.FILTER_FORWARD_ON_404, true);
    }

    private void configureJersey(final Set<String> packagesToScan) {
        packagesToScan.forEach(packageName -> registerClassesUnderPackage(getClassLoader(), packageName));
    }

    private void registerClassesUnderPackage(final ClassLoader classLoader, final String packageName) {
        log.info("Auto discovering packages for package [{}]", packageName);

        try {
            ClassPath.from(classLoader).getAllClasses().forEach(classInfo -> {
                        final String classNameWithoutSpringBootMagic =
                                classInfo.getName().replace("BOOT-INF.classes.", "");

                        if (classNameWithoutSpringBootMagic.startsWith(packageName)) {
                            log.info("Registering class {} as resource", classNameWithoutSpringBootMagic);
                            try {
                                register(Class.forName(classNameWithoutSpringBootMagic, true, getClassLoader()));
                            } catch (ClassNotFoundException e) {
                                log.error("This should not happen, we found a class that could not be found.", e);
                            }
                        } else {
                            log.trace("Skipping class {}", classInfo.getName());
                        }
                    }
            );

        } catch (IOException e) {
            log.error("Failed to auto discover", e);
        }
    }


}
