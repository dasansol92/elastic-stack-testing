package org.estf.gradle.classes;

public class AgentPolicy {

   public String id;
   public String name;
   public String description;
   public String namespace;

   public AgentPolicy(String id, String name, String description, String namespace) {
      this.id = id;
      this.name = name;
      this.description = description;
      this.namespace = namespace;
  }

  public String getId() {
      return this.id;
  }
}
