package org.estf.gradle.classes;

public class AgentPolicyResponse {
    
    public AgentPolicy item;

    public AgentPolicyResponse(AgentPolicy item) {
        this.item = item;
    }

    public AgentPolicy getItem() {
        return this.item;
    }

    public void setItem(AgentPolicy item) {
        this.item = item;
    }
    
}
