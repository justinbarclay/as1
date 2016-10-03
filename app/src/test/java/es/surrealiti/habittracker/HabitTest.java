package es.surrealiti.habittracker;

import org.junit.Test;

import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.*;

/**
 * Created by Justin on 2016-10-02.
 */

public class HabitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void makeHabitTest () throws Exception{
        //Test to make sure we can make a habit
        assertTrue(new Habit("Habit") instanceof Habit);
        assertTrue(new Habit("Habit", new Date(0)) instanceof Habit);
    }

    @Test
    public void habitNameIsString(){
        Habit test = new Habit("Test Habits");
        assertTrue(test.getName().equals("Test Habits"));

        test.setName("More tests");
        assertTrue(test.getName().equals("More tests"));
    }

    @Test
    public void TestHabitDate(){
        Habit dating = new Habit("Test Dates", new Date(0));

        assertTrue(dating.createdOn().equals(new Date(0)));
        dating.setCreationDate(new Date(1));
        assertTrue(dating.createdOn().equals(new Date(1)));
    }
    @Test
    public void TestHabitHistory(){
        Habit history = new Habit("Learning history");

        assertEquals(history.getHistory().size(), 0);
        history.addCompletion();
        history.addCompletion();
        assertEquals(history.getHistory().size(), 2);
    }

    @Test
    public void TestHabitToString(){
        Habit stringy = new Habit("Like the cheese");
        assertEquals(stringy.toString(), stringy.getName());
    }

    @Test
    public void TestHabitUUID(){
        Habit ident = new Habit("Please take my picture");

        assertTrue(ident.getID() instanceof UUID);
        assertTrue(ident.getID().equals(ident.getID()));
        assertEquals(ident.getID().toString().length(), 36);
    }

    @Test
    public void TestHabitDays(){
        Habit happy = new Habit("Days");
        assertFalse(happy.getDays().get("Sunday"));
        assertFalse(happy.getDays().get("Monday"));
        assertFalse(happy.getDays().get("Tuesday"));
        assertFalse(happy.getDays().get("Wednesday"));
        assertFalse(happy.getDays().get("Thursday"));
        assertFalse(happy.getDays().get("Friday"));
        assertFalse(happy.getDays().get("Saturday"));

        happy.trackDay("Friday", true);
        assertTrue(happy.getDays().get("Friday"));
    }

}
