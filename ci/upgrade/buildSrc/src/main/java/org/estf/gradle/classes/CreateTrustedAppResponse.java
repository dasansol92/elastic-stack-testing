package org.estf.gradle.classes;

public class CreateTrustedAppResponse {

    public TrustedApp data;
 
    public CreateTrustedAppResponse(TrustedApp data) {
       this.data = data;
   }

   public String toString () {
     return this.data.toString();
   }
 }
 