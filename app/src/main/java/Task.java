public class Task {
    private String name;
    private String Description;
    private int CompletionDate;
    private boolean CheckedOut;

    //default constructor; initializes class variables to default values
    public Task() {
        name = "";
        Description = "";
        CompletionDate = 0;
        CheckedOut = false;
    }

    public Task(String name, String description){
        this.name = name;
        Description = description;
    }
}
