package matt.housekeep;

import java.util.List;

public class Home {
    private User user;
    private List<Group> Groups;
    private List<Task> Tasks;
    private List<Chore> Chores;
    private List<ToDoList> ToDoLists;

    //default constructor; shouldn't be called
    public Home(){
        user = null;
        Groups = null;
        Tasks = null;
        Chores = null;
        ToDoLists = null;
    }

    //overloaded constructor; takes user as input initializes home's class variables to
    //the user's stored data
    public Home(User user){

        this.user = user;
        this.Groups = this.user.getUsersGroups();
        this.Tasks = this.user.getUsersTasks();
        this.Chores = this.user.getUsersChores();
        this.ToDoLists = this.user.getUsersTDL();
    }

}
