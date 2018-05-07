package matt.housekeep;

import java.util.Date;

public class Task {
    private String name;
    private String Description;
    private Date CreationDate;
    private Date CompletionDate;
    private boolean CheckedOut;

    //default constructor; initializes class variables to default values
    public Task() {
        name = "";
        Description = "";
        CreationDate = new Date();
        CompletionDate = null;
        CheckedOut = false;
    }

    //overloaded constructor; takes in String for name and description
    public Task(String name, String description){
        this.name = name;
        Description = description;
        CreationDate = new Date();
        CompletionDate = null;
        CheckedOut = false;
    }

    //returns task name as string
    public String getName() {
        return name;
    }

    //sets task name; takes string as input
    public void setName(String name) {
        this.name = name;
    }

    //returns task description
    public String getDescription(){
        return Description;
    }

    //sets task description; takes string as input
    public void setDescription(String description) {
        Description = description;
    }

    //returns creation date of task
    public Date getCreationDate() {
        return CreationDate;
    }

    //returns completion date as Date object
    public Date getCompletionDate() {
        return CompletionDate;
    }

    //sets completion date to current date when called
    public void setCompletionDate() {
        CompletionDate = new Date();
    }

    //returns whether task has been checked out; true = yes, false = no
    public boolean isCheckedOut() {
        return CheckedOut;
    }

    //sets if task is checked out; takes in boolean as input
    public void setCheckedOut(boolean checkedOut) {
        CheckedOut = checkedOut;
    }
}
