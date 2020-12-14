package com.example.cddlemptyproject.logic.data.model;


public class Group {

    private String resourceUuid;
    private String groupName;
    private String groupLeader;

    public Group(String uuid, String name, String leader){
        this.resourceUuid = uuid;
        this.groupName = name;
        this.groupLeader = leader;
    }

    public String getGroupLeader() {
        return groupLeader;
    }

    public String getResourceUuid() {
        return resourceUuid;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupLeader(String groupLeader) {
        this.groupLeader = groupLeader;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setResourceUuid(String resourceUuid) {
        this.resourceUuid = resourceUuid;
    }
}
