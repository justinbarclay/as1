package es.surrealiti.habittracker;

import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Created by Justin on 2016-10-03.
 */

public class HabitListTest {
    @Test
    public void TestHabitListCreation(){
        HabitList newList = new HabitList();
        ArrayList<Habit> aList = new ArrayList<Habit>();
        HabitList anotherList = new HabitList(aList);
        assertTrue(newList instanceof HabitList);
        assertTrue(anotherList instanceof HabitList);
    }

    @Test
    public void TestHabitList(){
        ArrayList<Habit> aList = new ArrayList<Habit>();
        aList.add(new Habit("Watching"));
        aList.add(new Habit("Luke"));
        aList.add(new Habit("Cage"));
        HabitList newList = new HabitList(aList);
        assertEquals(newList.size(), 3);

        newList.addHabit(new Habit("Tonight"));
        assertEquals(newList.size(), 4);
    }

    @Test
    public void TestHabitGets(){
        ArrayList<Habit> aList = new ArrayList<Habit>();
        Habit watching = new Habit("Watching");
        aList.add(watching);
        aList.add(new Habit("Luke"));
        aList.add(new Habit("Cage"));
        HabitList newList = new HabitList(aList);
        Habit newHabit = new Habit("Tonight");
        newList.addHabit(newHabit);

        assertTrue(newList.getHabit(0).isEqual(watching));
        assertTrue(newList.getAllHabits() instanceof ArrayList);
        assertTrue(newList.getTodaysHabits() instanceof ArrayList);
    }

    @Test
    public void TestRemoveHabit(){
        HabitList newList = new HabitList();
        Habit watching = new Habit("Watching");
        Habit newHabit = new Habit("Tonight");
        newList.addHabit(watching);
        newList.addHabit(newHabit);
        assertEquals(newList.size(), 2);
        assertTrue(newList.getHabit(0).isEqual(watching));
        newList.removeHabit(watching);
        assertFalse(newList.getHabit(0).isEqual(watching));
    }

    @Test
    public void TestUpdateHabit(){
        HabitList newList = new HabitList();
        Habit watching = new Habit("Watching");
        Habit newHabit = new Habit("Tonight");
        newList.addHabit(watching);
        newList.addHabit(newHabit);
        assertTrue(newList.getHabit(0).isEqual(watching));
        watching.setName("Not Watching");
        newList.update(watching);
        assertEquals(newList.getHabit(0).getName(), "Not Watching");
    }

}
