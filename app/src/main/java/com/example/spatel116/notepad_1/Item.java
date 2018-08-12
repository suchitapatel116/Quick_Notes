package com.example.spatel116.notepad_1;

/**
 * Created by spatel116 on 1/26/2018.
 */

public class Item {

    private String dateTime;
    private String note;

    public String getDateTime()
    {
        return dateTime;
    }
    public String getNote()
    {
        return note;
    }
    public void setDateTime(String dt)
    {
        this.dateTime = dt;
    }
    public void setNote(String desc)
    {
        this.note = desc;
    }
    public String toString()
    {
        return dateTime + ", " + note;
    }
}
