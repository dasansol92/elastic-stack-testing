package org.estf.gradle.classes;

import com.google.gson.annotations.SerializedName;

public class EndpointIntegrationRequest {
   public String name;
   public String description;
   public String namespace;
   public String policy_id;
   public Boolean enabled;
   public String output_id;
   public String inputs[];
   
   @SerializedName("package")
   public FleetPackage fleetPackage;

   public EndpointIntegrationRequest(String name,
    String description,
    String namespace,
    String policy_id,
    Boolean enabled,
    String output_id,
    String inputs[],
    FleetPackage fleetPackage
    ) {
      this.name = name;
      this.description = description;
      this.namespace = namespace;
      this.policy_id = policy_id;
      this.enabled = enabled;
      this.output_id = output_id;
      this.inputs = inputs;
      this.fleetPackage = fleetPackage;
  }

  public void setPolicyId (String policy_id) {
      this.policy_id = policy_id;
  }
}
