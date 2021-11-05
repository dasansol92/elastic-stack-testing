package org.estf.gradle.rest;

import org.estf.gradle.classes.*;
import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.commons.io.IOUtils;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class EndpointPolicyCreation extends BaseEndpoint {
    private final String endpointPolicyPath;
    private static final String endpointPath = "/api/fleet/package_policies";

    public EndpointPolicyCreation(Instance instance, String endpointPolicyPath) {
        super(instance, endpointPath);
        this.endpointPolicyPath = endpointPolicyPath;
    }

    @Override
    protected String getMessage() {
        return "to create endpoint policy";
    }

    public void sendPostRequest(String policy_id) throws IOException {
        String json = new String(Files.readAllBytes(Paths.get(endpointPolicyPath)));

        Gson g = new Gson();  
        EndpointIntegrationRequest endpointIntegrationRequest = g.fromJson(json, EndpointIntegrationRequest.class);
        endpointIntegrationRequest.setPolicyId(policy_id);

        String endpointIntegrationRequestString = g.toJson(endpointIntegrationRequest);  
        System.out.println(endpointIntegrationRequestString);
        HttpResponse response = sendRequest(utilLib.getPostEntityForString(getUrl(instance.kbnBaseUrl), getBase64EncodedAuth(instance.username, instance.password), endpointIntegrationRequestString));
        String outputJson = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
        System.out.println(outputJson);
    }
}
