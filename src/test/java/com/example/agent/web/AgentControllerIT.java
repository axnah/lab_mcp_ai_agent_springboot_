package com.example.agent.web;

import com.example.agent.agent.BacklogAgent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AgentControllerIT {

  @Autowired
  WebTestClient web;

  @MockitoBean
  BacklogAgent backlogAgent;

  @Test
  void should_call_endpoint() {
    when(backlogAgent.handle(anyString()))
        .thenReturn("Mocked issue creation");

    web.post().uri("/api/run")
        .bodyValue("Create a task to add OpenTelemetry")
        .header("Content-Type", "text/plain")
        .exchange()
        .expectStatus().isOk();
  }
}
