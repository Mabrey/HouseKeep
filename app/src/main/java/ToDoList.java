import java.util.List;

public class ToDoList {
    private String name;
    private List<Task> ListOfTasks;

    //default constructor; sets class variables to default values
    public ToDoList(){
        name = "";
        ListOfTasks = null;
    }

    //overloaded constructor; takes in String and List<Task> as inputs
    //sets taks name and list
    public ToDoList(String name, List<Task> ListOfTasks){
        this.name = name;
        this.ListOfTasks = ListOfTasks;
    }

    //sets list name; takes string as input
    public void setName(String name) {
        this.name = name;
    }

    //returns list name as string
    public String getName() {
        return name;
    }

    //sets list of tasks; takes in List<Task> as input
    public void setListOfTasks(List<Task> listOfTasks) {
        ListOfTasks = listOfTasks;
    }

    //returns list of current tasks as List<Task>
    public List<Task> getListOfTasks() {
        return ListOfTasks;
    }
}
