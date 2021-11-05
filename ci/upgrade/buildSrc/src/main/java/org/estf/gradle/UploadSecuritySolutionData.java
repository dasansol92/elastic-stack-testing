/**
 * Creates an alert for Security Solution
 *
 *
 * @author  Gloria Hornero
 *
 */

package org.estf.gradle;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import org.estf.gradle.classes.*;
import org.estf.gradle.rest.*;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;

import java.io.*;

public class UploadSecuritySolutionData extends DefaultTask {

    @Input
    public String esBaseUrl;

    @Input
    public String kbnBaseUrl;

    @Input
    public String username;

    @Input
    public String password;

    @Input
    public String version;

    @Input
    public String upgradeVersion;

    @TaskAction
    public void run() throws IOException, InterruptedException {
        RestApi api = new RestApi(username, password, version, upgradeVersion);
        Instance instance = new Instance(username, password, esBaseUrl, kbnBaseUrl);

        int majorVersion = api.setMajorVersion();
        if (majorVersion > 6) {

            createsSiemSignalsIndex(instance);
            createsDetectionRule(instance);
            createsAuditbeatIndex(instance);
            increasesNumberOfFieldsLimitForMapping(instance);
            createsAuditbeatMapping(instance);
            createsDocumentToGenerateAlert(instance);
            addEndpointPolicy(instance);
            addTrustedApp(instance);

        }
    }

    public void createsSiemSignalsIndex(Instance instance) throws IOException {
        SiemSignalsIndexCreation endpoint = new SiemSignalsIndexCreation(instance);
        endpoint.sendPostRequest();
    }

    public void createsDetectionRule(Instance instance) throws IOException, InterruptedException {
        DetectionRuleCreation endpoint = new DetectionRuleCreation(instance, "buildSrc/src/main/resources/detectionRule.json");
        endpoint.sendPostRequest();
    }

    public void createsAuditbeatIndex(Instance instance) throws IOException, InterruptedException {
        IndexCreation endpoint = new IndexCreation(instance, "/auditbeat-upgrade-1");
        endpoint.sendPutRequest();
    }

    public void increasesNumberOfFieldsLimitForMapping(Instance instance) throws IOException {
        FieldsLimitMappingIncrease endpoint = new FieldsLimitMappingIncrease(instance, "auditbeat-upgrade-1");
        endpoint.sendPutRequest();
    }

    public void createsAuditbeatMapping(Instance instance) throws IOException, InterruptedException {
        MappingCreation endpoint = new MappingCreation(instance, "auditbeat-upgrade-1", "buildSrc/src/main/resources/auditbeatMapping.json");
        endpoint.sendPutRequest();
    }


    public void createsDocumentToGenerateAlert(Instance instance) throws IOException, InterruptedException {
        DocumentCreation endpoint = new DocumentCreation(instance, "auditbeat-upgrade-1", "buildSrc/src/main/resources/auditbeatDoc.json");
        endpoint.sendPostRequest();
    }

    public void addEndpointPolicy(Instance instance) throws IOException, InterruptedException {
        AgentPolicyCreation agentPolicyCreation = new AgentPolicyCreation(instance, "buildSrc/src/main/resources/agentPolicy.json");
        AgentPolicyResponse agentPolicyResponse = agentPolicyCreation.sendPostRequest();
        
        EndpointPolicyCreation endpointPolicyCreation = new EndpointPolicyCreation(instance, "buildSrc/src/main/resources/endpointIntegration.json");
        endpointPolicyCreation.sendPostRequest(agentPolicyResponse.getItem().getId());
    }
    
    public void addTrustedApp(Instance instance) throws IOException, InterruptedException {
        TrustedAppCreation trustedAppCreation = new TrustedAppCreation(instance, "buildSrc/src/main/resources/trustedApp.json");
        CreateTrustedAppResponse createTrustedAppResponse = trustedAppCreation.sendPostRequest();

        System.out.println("Created Trusted Application:\n\n" + createTrustedAppResponse.toString());
        

    }
}
