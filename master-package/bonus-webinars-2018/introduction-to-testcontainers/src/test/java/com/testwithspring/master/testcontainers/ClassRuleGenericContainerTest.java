package com.testwithspring.master.testcontainers;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.ClassRule;
import org.junit.Test;
import org.testcontainers.containers.GenericContainer;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This test class demonstrates how you can create a new generic
 * container that is started before any test method is run and destroyed
 * after all test methods have been run.
 */
public class ClassRuleGenericContainerTest {

    @ClassRule
    public static GenericContainer webServer = new GenericContainer("alpine:3.2")
            .withExposedPorts(80)
            .withCommand("/bin/sh",
                    "-c",
                    "while true; do echo \"HTTP/1.1 200 OK\n\nTestContainers Rules!\" | nc -l -p 80; done"
            );

    @Test
    public void shouldReturnCorrectMessage() throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("http://" + webServer.getContainerIpAddress() + ":" + webServer.getMappedPort(80));
        HttpResponse resp = httpclient.execute(httpGet);

        String greeting = EntityUtils.toString(resp.getEntity()).trim();
        assertThat(greeting).isEqualTo("TestContainers Rules!");
    }
}
