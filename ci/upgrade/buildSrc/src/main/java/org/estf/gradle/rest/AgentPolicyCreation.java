package org.estf.gradle.rest;

import org.estf.gradle.classes.*;
import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.commons.io.IOUtils;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class AgentPolicyCreation extends BaseEndpoint {
    private final String agentPolicyPath;
    private static final String endpointPath = "/api/fleet/agent_policies?sys_monitoring=true";

    public AgentPolicyCreation(Instance instance, String agentPolicyPath) {
        super(instance, endpointPath);
        this.agentPolicyPath = agentPolicyPath;
    }

    @Override
    protected String getMessage() {
        return "to create agent policy";
    }

    public AgentPolicyResponse sendPostRequest() throws IOException {
        HttpResponse response = sendRequest(utilLib.getPostEntityForFile(getUrl(instance.kbnBaseUrl), getBase64EncodedAuth(instance.username, instance.password), agentPolicyPath));
        String json = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
        
        Gson g = new Gson();  
        AgentPolicyResponse policyResponse = g.fromJson(json, AgentPolicyResponse.class);
        return policyResponse;
    }
}
