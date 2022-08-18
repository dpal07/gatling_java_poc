package com.gp.gatling;

import io.gatling.app.Gatling;
import io.gatling.core.config.GatlingPropertiesBuilder;

public class RunTests {

    public static void main(String[] args){

        GatlingPropertiesBuilder propertiesBuilder = new GatlingPropertiesBuilder()
                .resultsDirectory("/target")
                .simulationClass(String.valueOf(UserContextSimulation.class));

        Gatling.fromMap(propertiesBuilder.build());
    }
}
