package com.testwithspring.master.testcontainers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.shaded.org.apache.http.HttpResponse;
import org.testcontainers.shaded.org.apache.http.client.methods.HttpGet;
import org.testcontainers.shaded.org.apache.http.impl.client.CloseableHttpClient;
import org.testcontainers.shaded.org.apache.http.impl.client.HttpClients;
import org.testcontainers.shaded.org.apache.http.util.EntityUtils;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This example demonstrates how we can run a generic container
 * with JUnit 5 by using a custom JUnit 5 extension.
 */
@ExtendWith(TestContainersExtension.class)
@DisplayName("Run a generic container")
class GenericContainerExtensionTest {

    private static GenericContainer webServer = new GenericContainer(
            "alpine:3.5"
    )
            .withExposedPorts(80)
            .withCommand("/bin/sh",
                    "-c",
                    "while true; do echo \"HTTP/1.1 200 OK\n\n" +
                            "TestContainers Rules!\" | nc -l -p 80; done"
            );

    @Test
    @DisplayName("Should return the correct message")
    void shouldReturnCorrectMessage() throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("http://" +
                webServer.getContainerIpAddress() +
                ":" + webServer.getMappedPort(80)
        );
        HttpResponse resp = httpclient.execute(httpGet);

        String greeting = EntityUtils.toString(resp.getEntity()).trim();
        assertThat(greeting).isEqualTo("TestContainers Rules!");
    }
}
