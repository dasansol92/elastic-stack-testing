/**
 * Creates an alert for Security Solution
 *
 *
 * @author  Gloria Hornero
 *
 */

package org.estf.gradle;

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

            //General setup
            createsSiemSignalsIndex(instance);

            //Custom query detection rule setup
            createsCustomQueryDetectionRule(instance);
            createsAuditbeatCustomIndex(instance);
            increasesNumberOfFieldsLimitForAuditbeatCustomMapping(instance);
            createsAuditbeatCustomMapping(instance);
            createsDocumentToGenerateAlertForCustomQueryDetectionRule(instance);


            //Threshold detection rule setup
            createsAuditbeatThresholdIndex(instance);
            increasesNumberOfFieldsLimitForAuditbeatThresholdMapping(instance);
            createsAuditbeatThresholdMapping(instance);
            createsDocumentsToGenerateAlertForThresholdDetectionRule(instance);
            createsThresholdDetectionRule(instance);

        }

        if (majorVersion > 12) {
            addEndpointPolicy(instance);
            addTrustedApp(instance);
        }
    }

    public void createsSiemSignalsIndex(Instance instance) throws IOException {
        SiemSignalsIndexCreation endpoint = new SiemSignalsIndexCreation(instance);
        endpoint.sendPostRequest();
    }

    public void createsCustomQueryDetectionRule(Instance instance) throws IOException {
        DetectionRuleCreation endpoint = new DetectionRuleCreation(instance, "buildSrc/src/main/resources/detectionRule.json");
        endpoint.sendPostRequest();
    }

    public void createsThresholdDetectionRule(Instance instance) throws IOException {
        DetectionRuleCreation endpoint = new DetectionRuleCreation(instance, "buildSrc/src/main/resources/thresholdRule.json");
        endpoint.sendPostRequest();
    }

    public void createsAuditbeatCustomIndex(Instance instance) throws IOException {
        IndexCreation endpoint = new IndexCreation(instance, "/auditbeat-custom-1");
        endpoint.sendPutRequest();
    }

    public void createsAuditbeatThresholdIndex(Instance instance) throws IOException {
        IndexCreation endpoint = new IndexCreation(instance, "/auditbeat-threshold-1");
        endpoint.sendPutRequest();
    }

    public void increasesNumberOfFieldsLimitForAuditbeatCustomMapping(Instance instance) throws IOException {
        FieldsLimitMappingIncrease endpoint = new FieldsLimitMappingIncrease(instance, "auditbeat-custom-1");
        endpoint.sendPutRequest();
    }

    public void increasesNumberOfFieldsLimitForAuditbeatThresholdMapping(Instance instance) throws IOException {
        FieldsLimitMappingIncrease endpoint = new FieldsLimitMappingIncrease(instance, "auditbeat-threshold-1");
        endpoint.sendPutRequest();
    }

    public void createsAuditbeatCustomMapping(Instance instance) throws IOException {
        MappingCreation endpoint = new MappingCreation(instance, "auditbeat-custom-1", "buildSrc/src/main/resources/auditbeatMapping.json");
        endpoint.sendPutRequest();
    }

    public void createsAuditbeatThresholdMapping(Instance instance) throws IOException {
        MappingCreation endpoint = new MappingCreation(instance, "auditbeat-threshold-1", "buildSrc/src/main/resources/auditbeatMapping.json");
        endpoint.sendPutRequest();
    }


    public void createsDocumentToGenerateAlertForCustomQueryDetectionRule(Instance instance) throws IOException {
        DocumentCreation endpoint = new DocumentCreation(instance, "auditbeat-custom-1", "buildSrc/src/main/resources/auditbeatDoc.json");
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
        

    public void createsDocumentsToGenerateAlertForThresholdDetectionRule(Instance instance) throws IOException {
        DocumentCreation endpoint = new DocumentCreation(instance, "auditbeat-threshold-1", "buildSrc/src/main/resources/auditbeatDoc.json");
        for (int i = 0; i < 2; i++) {
            endpoint.sendPostRequest();
        }
    }
}
