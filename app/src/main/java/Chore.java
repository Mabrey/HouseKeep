import java.util.List;

public class Chore {
    private String name;
    private int FreqDays;
    private int FreqWeeks;
    private List<User> Rotation;
    private User ResponsibleForChore;

    //default constructor; initializes all class variables to null values
    public Chore() {
        name = "";
        FreqDays = 0;
        FreqWeeks = 0;
        Rotation = null;
        ResponsibleForChore = null;
    }

    //Constructor, sets name as string, frequency in days and frequency in weeks
    public Chore(String name, int freqDays, int freqWeeks) {
        this.name = name;
        freqDays = FreqDays;
        freqWeeks = FreqWeeks;
    }

    //sets name of chore, takes in a string
    public void setName(String name){
        this.name = name;
    }

    //returns name of chore
    public String getName(){
        return name;
    }

    //sets frequency in terms of days, takes in an integer
    public void setFreqDays(int freqDays) {
        FreqDays = freqDays;
    }

    //sets freqency in terms of weeks, takes in an integer
    public void setFreqWeeks(int freqWeeks){
        FreqWeeks = freqWeeks;
    }

    //returns total frequency in days
    public int getFrequency(){
        return (FreqWeeks * 7) + FreqDays;
    }

    //returns users in chore rotation as List<User>
    public List<User> getRotation() {
        return Rotation;
    }

    public void setRotation(List<User> rotation) {
        Rotation = rotation;
    }

    //returns user responsible for chore as User object
    public User getResponsibleForChore() {
        return ResponsibleForChore;
    }

    public void setResponsibleForChore(User responsibleForChore) {
        ResponsibleForChore = responsibleForChore;
    }
}
