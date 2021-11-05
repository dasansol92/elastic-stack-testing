package org.estf.gradle.classes;

public class TrustedApp {

   public String id;
   public String name;
   public String description;
   public String created_at;
   public String created_by;
   public String os;
   public ExceptionEntry entries[];

   public TrustedApp(String id, String name, String description, String created_at, String created_by, String os, ExceptionEntry entries[]) {
      this.id = id;
      this.name = name;
      this.description = description;
      this.created_by = created_by;
      this.created_at = created_at;
      this.os = os;
      this.entries = entries;
  }

  public String toString() {
      return "id: " + this.id + ",\n" + "name: " + this.name + ",\n" + "description: " + this.description + ",\n" + "created_by: " + this.created_by + ",\n" + "created_at: " + this.created_at + ",\n" + "os:" + this.os;
  }
}
