package matt.housekeep;

public class InviteMessage {
    private String groupName;
    private String groupKey;

    public InviteMessage(String groupName, String groupKey){
        this.groupName = groupName;
        this.groupKey = groupKey;
    }

    public String getGroupName(){
        return groupName;
    }

    public String getGroupKey(){
        return groupKey;
    }

}

