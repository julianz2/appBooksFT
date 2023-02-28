package com.distribuida;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

@ApplicationScoped
@Liveness
public class AuthorsCheck implements HealthCheck {

    private static final String SERVICE_URL = System.getenv("AUTHOR_URL") + "/authors";

    @Override
    public HealthCheckResponse call() {
        boolean isRunning = isServiceRunning();
        if (isRunning) {
            return HealthCheckResponse.up("It's found");
        } else {
            return HealthCheckResponse.down("It's not found");
        }
    }

    private boolean isServiceRunning() {
        try {
            URL serviceUrl = new URL(AuthorsCheck.SERVICE_URL);
            HttpURLConnection connection = (HttpURLConnection) serviceUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            int responseCode = connection.getResponseCode();
            return (responseCode == 200);
        } catch (IOException e) {
            return false;
        }
    }
}
