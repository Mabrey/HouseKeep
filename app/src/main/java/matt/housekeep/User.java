package matt.housekeep;

import java.util.List;

/*
TO-DO:  may need to add methods for removing a single group/chore/task/matt.housekeep.ToDoList
        to the current list
 */
public class User {
    private String name;
    private String username;
    private int completedChores;
    private List<Group> UsersGroups;
    private List<Chore> UsersChores;
    private List<Task> UsersTasks;
    private List<ToDoList> UsersTDL;

    //default constructor; sets class variables to default values
    public User(){
        completedChores = 0;
        UsersGroups = null;
        UsersChores = null;
        UsersTasks = null;
        UsersTDL = null;
    }

    //overloaded constructor; sets all user class variables to given input values
    public User(int completedChores, List<Group> UsersGroups, List<Chore> UsersChores,
                List<Task> UsersTasks, List<ToDoList> UsersTDL){
        this.completedChores = completedChores;
        this.UsersGroups = UsersGroups;
        this.UsersChores = UsersChores;
        this.UsersTasks = UsersTasks;
        this.UsersTDL = UsersTDL;
    }

    //sets name of matt.housekeep.User; takes string as input
    public void setName(String name) {
        this.name = name;
    }

    //return user's name as string
    public String getName() {
        return name;
    }

    public void setUsername(String username)  {this.username = username;}

    public String getUsername() {return username;}

    //sets number of user's completed chores; takes int as input
    public void setCompletedChores(int completedChores) {
        this.completedChores = completedChores;
    }

    //returns user's number of completed chores as int
    public int getCompletedChores() {
        return completedChores;
    }

    //sets user's groups; takes List<matt.housekeep.Group> as input
    public void setUsersGroups(List<Group> usersGroups) {
        UsersGroups = usersGroups;
    }

    //adds group to user's list of groups; takes matt.housekeep.Group as input
    public void addGroup(Group group){
        UsersGroups.add(group);
    }

    //removes specific group from user's list of groups; takes matt.housekeep.Group as input
    public void removeUsersGroup(Group group){
        UsersGroups.remove(group);
    }

    //returns user's groups as List<matt.housekeep.Group>
    public List<Group> getUsersGroups() {
        return UsersGroups;
    }

    //sets user's chores; takes List<matt.housekeep.Chore> as input
    public void setUsersChores(List<Chore> usersChores) {
        UsersChores = usersChores;
    }

    //adds a single chore to the user's current list of chores; takes matt.housekeep.Chore as input
    public void addUsersChore(Chore chore){
        UsersChores.add(chore);
    }

    //removes specific chore from user's list of chores; takes matt.housekeep.Chore as input
    public void removeUsersChore(Chore chore){
        UsersChores.remove(chore);
    }

    //returns user's chores as List<matt.housekeep.Chore>
    public List<Chore> getUsersChores() {
        return UsersChores;
    }

    //sets user's tasks; takes List<matt.housekeep.Task> as input
    public void setUsersTasks(List<Task> usersTasks) {
        UsersTasks = usersTasks;
    }

    //adds task to user's list of tasks; takes task as input
    public void addUsersTask(Task task){
        UsersTasks.add(task);
    }

    //removes specific task from user's list of tasks; takes matt.housekeep.Task as input
    public void removeUsersTask(Task task){
        UsersTasks.remove(task);
    }

    //returns user's tasks as List<matt.housekeep.Task>
    public List<Task> getUsersTasks() {
        return UsersTasks;
    }

    //sets user's to-do list; takes Lists<matt.housekeep.ToDoList> as input
    public void setUsersTDL(List<ToDoList> usersTDL) {
        UsersTDL = usersTDL;
    }

    //adds a to-do list to the user's list of TDL's; takes ToDolist as input
    public void addUsersTDL(ToDoList tdl){
        UsersTDL.add(tdl);
    }

    public void removeUsersTDL(ToDoList tdl){
        UsersTDL.remove(tdl);
    }

    //returns user's to-do list as List<matt.housekeep.ToDoList>
    public List<ToDoList> getUsersTDL() {
        return UsersTDL;
    }
}
