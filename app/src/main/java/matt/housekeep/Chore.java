package matt.housekeep;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Chore {
    private int RotateMonthDay;
    private int ActualRotateDay;
    private String description;
    private String name;
    private String FrequencyType;
    private String Date;
    private int Month;
    private int FreqDays;
    private int FreqWeeks;
    private List<User> Rotation;
    private ArrayList<DaysForRotation> DaysOfWeekRotation;
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

    public Chore(String name, String daily, String description)
    {
        this.name = name;
        this.FrequencyType = daily;
        this.description = description;
    }

    public Chore(String name, String weekly, ArrayList<DaysForRotation> daysOfWeekRotation, String description)
    {
        this.name = name;
        this.FrequencyType = weekly;
        this.description = description;
        this.DaysOfWeekRotation = daysOfWeekRotation;
    }

    public Chore(String name, String monthly, int DayOfMonth, String description)
    {
        this.name = name;
        this.FrequencyType = monthly;
        this.RotateMonthDay = DayOfMonth;
        this.description = description;
    }

    boolean CurrentMonth(int day)
    {
        Calendar calendar = Calendar.getInstance();
        if (day > calendar.DAY_OF_MONTH)   //if day is greater than today
        {
            if(doesDayExist(day, calendar)) //does day past today exist in this month
            {
                ActualRotateDay = day;//
                return true;
            }
            else{
                ActualRotateDay = calendar.getActualMaximum(calendar.DAY_OF_MONTH);
                return true;
            }
        }
        else {
            calendar.add(Calendar.MONTH, 1);
            if(doesDayExist(day, calendar)) //does day past today exist in this month
            {
                ActualRotateDay = day;//
                return false;
            }
            else
            {
                ActualRotateDay = calendar.getActualMaximum(calendar.DAY_OF_MONTH);
                return false;
            }
        }
    }

    boolean doesDayExist(int day, Calendar calendar)
    {
        int lastDay = calendar.getActualMaximum(calendar.DAY_OF_MONTH);
        return (day <= lastDay);
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
    public String getFrequency(){
        return FrequencyType;
    }

    public String getDescription(){
        return description;
    }


    //returns users in chore rotation as List<matt.housekeep.User>
    public List<User> getRotation() {
        return Rotation;
    }

    public void setRotation(List<User> rotation) {
        Rotation = rotation;
    }

    //returns user responsible for chore as matt.housekeep.User object
    public User getResponsibleForChore() {
        return ResponsibleForChore;
    }

    public void setResponsibleForChore(User responsibleForChore) {
        ResponsibleForChore = responsibleForChore;
    }

    public ArrayList<DaysForRotation> getDaysOfWeek() {
        return DaysOfWeekRotation;
    }

    public Object getRotationMonthDay() {
        return RotateMonthDay;
    }

    public Object getRotationCurrentMonth() {
        return ActualRotateDay;
    }

    public void setDate(String date) {
        this.Date = date;
    }

    public String getDate(){
        return this.Date;
    }

    public void setMonth(int month) {
        this.Month = month;
    }
    public int getMonth(){
        return this.Month;
    }

    public static ChoreRotation rotateChore(ChoreRotation oldRotation)
    {
        ChoreRotation newChoreRotation = new ChoreRotation();
        int length = oldRotation.nextOrder.length;
        newChoreRotation.nextOrderName = new String[length];
        newChoreRotation.nextOrder = oldRotation.nextOrder;
        int j = 0;
        for(int i = 0; i < oldRotation.nextOrder.length; i++)
        {
            j++;
            j %= oldRotation.nextOrder.length;

            newChoreRotation.nextOrderName[i] = oldRotation.nextOrderName[j];
        }

        return newChoreRotation;
    }

    public static String updateDueDate(DataSnapshot choreSnap, String choreName)
    {
        String frequency = choreSnap.child("Frequency").child("Type").getValue().toString();
        String dueDate;
        String[] dueDateParsed;
        String newDueDate;

        switch (frequency){
            case "Daily":
                Calendar calendar= Calendar.getInstance();
                calendar.add(Calendar.DATE, 1);
                newDueDate = String.valueOf(calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.DAY_OF_MONTH);
                return newDueDate;


            case "Weekly":
                SimpleDateFormat formatter = new SimpleDateFormat("EEEE");
                dueDate = choreSnap.child("Frequency").child("Due Date").getValue().toString();
                dueDateParsed = dueDate.split("/");

                Calendar calendar2 = Calendar.getInstance();
                String dayOfWeek;
                int day = calendar2.get(Calendar.DAY_OF_WEEK);
                switch (day) {
                    case Calendar.SUNDAY:
                        // Current day is Sunday
                        dayOfWeek = "Sunday";
                        break;

                    case Calendar.MONDAY:
                        // Current day is Monday
                        dayOfWeek = "Monday";
                        break;

                    case Calendar.TUESDAY:
                        // etc.
                        dayOfWeek = "Tuesday";
                        break;

                    case Calendar.WEDNESDAY:
                        dayOfWeek = "Wednesday";
                        break;

                    case Calendar.THURSDAY:
                        dayOfWeek = "Thursday";
                        break;

                    case Calendar.FRIDAY:
                        dayOfWeek = "Friday";
                        break;

                    case Calendar.SATURDAY:
                        dayOfWeek = "Saturday";
                        break;

                    default: dayOfWeek = "Sunday";

                }
                Log.d("Day of week", dayOfWeek);


                int addDays = Integer.parseInt(choreSnap.child("Frequency").child("Days of Week").child(dayOfWeek).getValue().toString());
                calendar2.add(Calendar.DATE, addDays);
                newDueDate = String.valueOf(calendar2.get(Calendar.MONTH) + 1) + "/" + calendar2.get(Calendar.DAY_OF_MONTH);
                return newDueDate;


            case "Monthly":
                dueDate = choreSnap.child("Frequency").child("Due Date").getValue().toString();
                dueDateParsed = dueDate.split("/");
                Calendar calendar3 = Calendar.getInstance();
                calendar3.set(Calendar.YEAR, Integer.parseInt(dueDateParsed[0]) - 1, Integer.parseInt(dueDateParsed[1]));
                calendar3.add(Calendar.MONTH, 1);
                newDueDate = String.valueOf(calendar3.get(Calendar.MONTH) + 1) + "/" + calendar3.get(Calendar.DAY_OF_MONTH);
                return newDueDate;

        }
        return "Invalid";
    }


}

class ChoreRotation{
    String[] nextOrder;
    String[] nextOrderName;

    ChoreRotation()
    {
        this.nextOrder = new String[0];
        this.nextOrderName = new String[0];
    }

    ChoreRotation(String[] nextOrder, String[] nextOrderName)
    {
        this.nextOrder = nextOrder;
        this.nextOrderName = nextOrderName;
    }
}
