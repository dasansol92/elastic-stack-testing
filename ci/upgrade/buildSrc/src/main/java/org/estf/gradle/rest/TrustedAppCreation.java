package org.estf.gradle.rest;

import org.estf.gradle.classes.*;
import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.commons.io.IOUtils;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TrustedAppCreation extends BaseEndpoint {
    private final String trustedAppPath;
    private static final String endpointPath = "/api/endpoint/trusted_apps";

    public TrustedAppCreation(Instance instance, String trustedAppPath) {
        super(instance, endpointPath);
        this.trustedAppPath = trustedAppPath;
    }

    @Override
    protected String getMessage() {
        return "to create trusted app";
    }

    public CreateTrustedAppResponse sendPostRequest() throws IOException {
        HttpResponse response = sendRequest(utilLib.getPostEntityForFile(getUrl(instance.kbnBaseUrl), getBase64EncodedAuth(instance.username, instance.password), trustedAppPath));
        String json = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
        
        Gson g = new Gson();  
        CreateTrustedAppResponse trustedAppResponse = g.fromJson(json, CreateTrustedAppResponse.class);
        
        return trustedAppResponse;
    }
}
