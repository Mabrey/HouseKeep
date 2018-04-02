import java.util.List;

public class Group {
    String nameOfGroup;
    User ownerOfGroup;
    List<User> groupMembers;
    List<Chore> Chores;
    List<Task> Tasks;
    List<ToDoList> Lists;

    public Group(String nameOfGroup, User ownerOfGroup) {
        super();
        this.nameOfGroup = nameOfGroup;
        this.ownerOfGroup = ownerOfGroup;
    }

    public Group(String nameOfGroup, User ownerOfGroup, List<User> groupMembers) {
        super();
        this.nameOfGroup = nameOfGroup;
        this.ownerOfGroup = ownerOfGroup;
        this.groupMembers = groupMembers;
    }

    //Name of Group
    public void setNameOfGroup(String nameOfGroup) {
        this.nameOfGroup = nameOfGroup;
    }
    public String getNameOfGroup() {
        return nameOfGroup;
    }

    //Owner of Group
    public void setOwnerOfGroup(User ownerOfGroup) {
        this.ownerOfGroup = ownerOfGroup;
    }
    public User getOwnerOfGroup() {
        return ownerOfGroup;
    }

    //Users
    public void addUser(User newUser){
        groupMembers.add(newUser);
    }
    public void removeUser(User removedUser){
        groupMembers.remove(removedUser);
    }

    //Chores
    public void addChore(Chore newChore){
        Chores.add(newChore);
    }
    public void removeChore(Chore removedChore){
        Chores.remove(removedChore);
    }

    //Tasks
    public void addTask(Task newTask){
        Tasks.add(newTask);
    }
    public void removeTask(Task removedTask){
        Tasks.remove(removedTask);
    }

    //ToDoLists
    public void addTDL(ToDoList newTDL){
        Lists.add(newTDL);
    }
    public void removeTDL(ToDoList removedTDL){
        Lists.remove(removedTDL);
    }
}
