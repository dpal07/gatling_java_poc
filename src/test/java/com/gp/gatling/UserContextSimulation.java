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
    private String token = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnb2dsb2JhbC1kZXYiLCJ1c2VyX2lkIjo3MjAzLCJsb2dpbl9vcmlnaW4iOiJnb2dsb2JhbC1jbGFzc2ljIiwid2hvYW1pIjp7InVzZXJUeXBlIjoiQyIsInBlcnNvbklkIjoiNDgxIiwidXNlciI6eyJpZCI6NzIwMywiZW1haWwiOiJnZ2RlbW9fY29udHJhY3RyZXZpZXdlci10ZXN0QGdsb2JhbGl6YXRpb24tcGFydG5lcnMuY29tIiwidXVpZCI6ImJmZWNhMDljLTdjNDYtMTFlYy04ODU2LTEyMTEwZDkyZGFhNSJ9LCJmaXJzdE5hbWUiOiJEdW1teSBOYW1lIiwibGFzdE5hbWUiOiJEdW1teSBMYXN0IE5hbWUiLCJlbmFibGVkIjp0cnVlLCJwZXJzb25Sb2xlcyI6W10sIm90aGVyUGVyc29uUmVjb3Jkc0ZvclVzZXIiOltdfSwicGVyc29uX2lkIjoiQzo0ODEiLCJpYXQiOjE2NTEyMzk4MzksImV4cCI6MTY2MTI2OTU5OX0.Jov7hx2zkUQglRshN6LSJZxpyQj7Wq7hxeGagsny0u8";

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
