package br.com.edu.zup;

import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import jakarta.inject.Inject;

@MicronautTest
public class CarrosGrpc2Test {

    @Inject
    EmbeddedApplication<?> application;

    @Test
    public void testItWorks() {
        Assertions.assertTrue(application.isRunning());
    }

}
