package com.gp.gatling;

import io.gatling.javaapi.core.CoreDsl;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpDsl;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.time.Duration;

import static io.gatling.javaapi.http.HttpDsl.*;
import static io.gatling.javaapi.core.CoreDsl.constantUsersPerSec;


public class UserContextSimulation extends Simulation {


    private String securityContextBaseUrl = "https://rbs-dev.gpdev.us";
    private String path = "/api/v2/security-user-context/security-context";
    private String token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJnb2dsb2JhbC1kZXYiLCJpYXQiOjE2NDU1MzE2NDUsImV4cCI6MjY0NTUzODg0NSwicGVyc29uX2lkIjoiRzoxOTciLCJ1c2VyX2lkIjozNTczMCwidXNlcl90eXBlIjoiRyIsImxvZ2luX29yaWdpbiI6ImdvZ2xvYmFsLWNsYXNzaWMiLCJ3aG9hbWkiOnsidXNlclR5cGUiOiJHIiwicGVyc29uSWQiOjE5NywiZW5hYmxlZCI6dHJ1ZSwidXNlciI6eyJpZCI6MzU3MzAsImVtYWlsIjoibGJvZHppb255QGdsb2JhbGl6YXRpb24tcGFydG5lcnMuY29tIiwidXVpZCI6IjAwY2U2YmU5LWFjMzUtMTFlYy04NzYwLTEyYjFhMWE2MjBiMyJ9LCJmaXJzdE5hbWUiOiJLZWl0aCIsImxhc3ROYW1lIjoiUm91bmRzIiwicGVyc29uUm9sZXMiOlt7InJvbGVDb2RlIjoiMiIsInVzZXJUeXBlIjoiRyIsInJvbGVOYW1lIjoiR1BfQURNSU4ifV0sIm90aGVyUGVyc29uUmVjb3Jkc0ZvclVzZXIiOltdfX0.1CAy2DBCpvM9ogvmvUXAzZOsOn5bU2bzPZWzf5QjNE8";

    HttpProtocolBuilder httpProtocolBuilder = HttpDsl.http
            .baseUrl(securityContextBaseUrl)
            .acceptHeader("application/json")
            .digestAuth("Bearer", token)
            .userAgentHeader("Gatling/Performance Test");

    ScenarioBuilder scenarioBuilder = CoreDsl.scenario("Get user Context")
            .exec(
                    http("get user context")
                            .get(path)
                            .header("Content-Type", "application/json")
                            .check(status().is(200))
            );


    public UserContextSimulation(){
        this.setUp(scenarioBuilder.injectOpen(constantUsersPerSec(10).during(Duration.ofMinutes(5)))).protocols(httpProtocolBuilder);
    }


}
