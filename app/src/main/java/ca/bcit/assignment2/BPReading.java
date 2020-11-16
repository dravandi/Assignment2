package ca.bcit.assignment2;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BPReading {
    public String familyMember;
    public String id;
    public String userId;
    public String time;
    public String date;
    public String systolicReading;
    public String diastolicReading;
    public String condition;

    // Add this to get rid on 'no-argument constructor' error. Also make sure the class is
    // static if an inner class (like this one) or that the class is in it's own file.
    public BPReading() {}


    public BPReading(String familyMember, String systolicReading,
                     String diastolicReading) {

        this.id = String.valueOf(System.currentTimeMillis());
        this.familyMember = familyMember;

        //Code block for autogenerating date and time for the reading.
        //Converts system time into a formatted datetime string and splits it.
        Date currentDate = new Date(System.currentTimeMillis());
        SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("mm/dd/yyyy HH:mm:ss");
        String dateTime = dateTimeFormatter.format(currentDate);
        String[] splitDateTime = dateTime.split(" ");
        this.date = splitDateTime[0];
        this.time = splitDateTime[1];

        this.systolicReading = systolicReading;
        this.diastolicReading = diastolicReading;

        //Code block for auto-generating condition for the reading.
        //Converts readings into ints so they can be compared.
        //Decides the condition based on a combination of both readings.
        int systolicReadingInt = Integer.parseInt(this.systolicReading);
        int diastolicReadingInt = Integer.parseInt(this.diastolicReading);
        if(systolicReadingInt > 180 || diastolicReadingInt > 120){
            this.condition = ConditionTypes.HYPERTENSIVE.toString();
        } else if(systolicReadingInt >= 140 || diastolicReadingInt >= 90){
            this.condition = ConditionTypes.STAGE2.toString();
        } else if(systolicReadingInt >= 130 || diastolicReadingInt >= 80){
            this.condition = ConditionTypes.STAGE1.toString();
        } else if(systolicReadingInt >= 120){
            this.condition = ConditionTypes.ELEVATED.toString();
        } else {
            this.condition = ConditionTypes.NORMAL.toString();
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFamilyMember() {
        return familyMember;
    }

    public void setFamilyMember(String familyMember) {
        this.familyMember = familyMember;
    }

    public String getTime() {
        return time;
    }

    public void setTime() {
        Date currentDate = new Date(System.currentTimeMillis());
        SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        String dateTime = dateTimeFormatter.format(currentDate);
        String[] splitDateTime = dateTime.split(" ");
        this.date = splitDateTime[0];
        this.time = splitDateTime[1];
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        Date currentDate = new Date(System.currentTimeMillis());
        SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        String dateTime = dateTimeFormatter.format(currentDate);
        String[] splitDateTime = dateTime.split(" ");
        this.date = splitDateTime[0];
        this.time = splitDateTime[1];
    }

    public String getSystolicReading() {
        return systolicReading;
    }

    public void setSystolicReading(String systolicReading) {
        this.systolicReading = systolicReading;
    }

    public String getDiastolicReading() {
        return diastolicReading;
    }

    public void setDiastolicReading(String diastolicReading) {
        this.diastolicReading = diastolicReading;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition() {
        int systolicReadingInt = Integer.parseInt(this.getSystolicReading());
        int diastolicReadingInt = Integer.parseInt(this.getDiastolicReading());
        if(systolicReadingInt > 180 || diastolicReadingInt > 120){
            this.condition = ConditionTypes.HYPERTENSIVE.toString();
        } else if(systolicReadingInt >= 140 || diastolicReadingInt >= 90){
            this.condition = ConditionTypes.STAGE2.toString();
        } else if(systolicReadingInt >= 130 || diastolicReadingInt >= 80){
            this.condition = ConditionTypes.STAGE1.toString();
        } else if(systolicReadingInt >= 120){
            this.condition = ConditionTypes.ELEVATED.toString();
        } else {
            this.condition = ConditionTypes.NORMAL.toString();
        }
    }
}
