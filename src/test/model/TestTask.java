package model;

import model.exceptions.EmptyStringException;
import model.exceptions.InvalidProgressException;
import model.exceptions.NegativeInputException;
import model.exceptions.NullArgumentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import parsers.Parser;
import parsers.TagParser;
import parsers.exceptions.ParsingException;

import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;

public class TestTask {


    String goodString = "Register for the course. ## cpsc210; tomorrow; important; urgent; in progress";
    String badString = "Register for the course. cpsc210; tomorrow; important; urgent; in progress";
    private Task task;
    private Task task2;
    private Calendar rightNow;
    private DueDate dueDate;
    private Tag tagOne;
    private Tag tagTwo;

    @BeforeEach
    void runBefore() {
        task = new Task("Task One");
        rightNow = Calendar.getInstance();
        rightNow.set(2019,2,24,2,39);
        dueDate = new DueDate(rightNow.getTime());
        task.setDueDate(dueDate);
        Priority p1 = new Priority(1);
        task.setPriority(p1);
        task.setStatus(Status.TODO);
        task2 = new Task("Task One");
        task2.setDueDate(dueDate);
        task2.setStatus(Status.TODO);
        task2.setPriority(p1);
        tagOne = new Tag("cpsc 110");
        tagTwo = new Tag("cpsc 210");
    }

    @Test
    void testPriorityIsNull() {
        try {
            task2.setPriority(null);
            fail();
        } catch (NullArgumentException e) {
            System.out.println("Illegal argument: priority is null");
        }
    }

    @Test
    void testSetDescriptionWithGoodDes() {
        try {
            task.setDescription(goodString);
            assertEquals("Register for the course. ",task.getDescription());
        } catch (EmptyStringException e) {
            fail();
        }
    }

    @Test
    void testSetDescriptionWithBadDes() {
        try {
            task.setDescription(badString);
            assertEquals("Register for the course. cpsc210; tomorrow; important; urgent; in progress",task.getDescription());
        } catch (EmptyStringException e) {
            fail();
        }
    }

    @Test
    void testTagNameIsNull() {
        try {
            String tagName = null;
            task.containsTag(tagName);
            fail();
        } catch (EmptyStringException e) {
            System.out.println("Tag name cannot be empty or null");
        }
    }

    @Test
    void testTagNameIsEmpty() {
        try {
            task.containsTag("");
            fail();
        } catch (EmptyStringException e) {
            System.out.println("Tag name cannot be empty or null");
        }
    }


    @Test
    void testDescriptionWithNull() {
        try {
            String des = null;
            task.setDescription(des);
        } catch (EmptyStringException e) {
            System.out.println("Throw EmptyStringException");
        }
    }

    @Test
    void testConstructorWithEmptyString() {
        try {
            task.setDescription("");
        } catch (EmptyStringException e) {
            System.out.println("Throw EmptyStringException");
        }
    }

    @Test
    void testConstructorWithBadString() {
        task = new Task(badString);
        assertEquals(Status.TODO, task.getStatus());
        assertFalse(task.getPriority().isImportant());
        assertFalse(task.getPriority().isUrgent());
        assertNull(task.getDueDate());
        assertEquals(0, task.getTags().size());
    }



    @Test
    void testParsing() {
        String description = "  ## tag1";
        Task t1 = new Task(description);
        Parser tagParser = new TagParser();
        try {
            tagParser.parse(description,t1);
            assertEquals("  ",t1.getDescription());
            assertNull(t1.getDueDate());
            assertEquals(Status.TODO,t1.getStatus());
            assertFalse(t1.getPriority().isImportant());
            assertFalse(t1.getPriority().isUrgent());
        } catch (ParsingException e) {
            fail();
        }
    }

    @Test
    void testConstructor2() {
        Parser tagParser = new TagParser();
        try {
            task = new Task("Register for the course. ## cpsc210; today; important; urgent; to do");
            tagParser.parse(task.getDescription(), task);
            assertEquals("Register for the course. ", task.getDescription());
            assertEquals(Status.TODO, task.getStatus());
            assertTrue(task.getPriority().isImportant());
            assertTrue(task.getPriority().isUrgent());
            assertTrue(task.getDueDate().isDueToday());
        } catch (EmptyStringException e) {
            fail();
        } catch (ParsingException e) {
            assertEquals("Register for the course. ", task.getDescription());
        }
    }

    @Test
    void testAddTag() {
        task.addTag("tmr");
        task.addTag("tmr");
        assertEquals(1, task.getTags().size());
    }

    @Test
    void testSetStatusWithNull() {
        try {
            task.setStatus(null);
            fail();
        } catch (NullArgumentException e) {
            // expected
        }

    }

    @Test
    void testSetStatus() {
        try {
            task.setStatus(Status.TODO);
            assertEquals(Status.TODO, task.getStatus());
        } catch (NullArgumentException e) {
            fail();
        }

    }

    @Test
    void testRemoveTag() {
        task.addTag("tmr");
        task.removeTag("yst");
        assertEquals(1, task.getTags().size());
        task.removeTag("tmr");
        assertEquals(0, task.getTags().size());
        task.removeTag("tmr");
        assertEquals(0, task.getTags().size());
    }

    @Test
    void testContainsTag() {
        task.addTag("tmr");
        assertTrue(task.containsTag("tmr"));
        assertFalse(task.containsTag("yst"));
    }

    @Test
    void testSetDescription() {
        task.setDescription("ss");
        assertEquals("ss", task.getDescription());
    }

    @Test
    void testToString() {
        task.addTag("CPSC210");
        task.addTag("CPSC213");
        System.out.println(task.toString());
    }

    @Test
    void testExactlySameTaskCaseOne() {
        assertTrue(task.equals(task2));
    }

    @Test
    void testExactlySameTaskCaseTwo() {
        assertTrue(task2.equals(task));
    }

    @Test
    void testDifferentDescription() {
        task2.setDescription("Task Two");
        assertFalse(task.equals(task2));
    }

    @Test
    void testDifferentPriority() {
        Priority p2 = new Priority(2);
        task2.setPriority(p2);
        assertFalse(task.equals(task2));
    }

    @Test
    void testDifferentStatus() {
        task2.setStatus(Status.IN_PROGRESS);
        assertFalse(task.equals(task2));
    }

    @Test
    void testDifferentDueDate() {
        Calendar nextDay = Calendar.getInstance();
        DueDate dueDate2 = new DueDate(nextDay.getTime());
        dueDate2.postponeOneDay();
        task2.setDueDate(dueDate2);
        assertFalse(task.equals(task2));
    }

    @Test
    void testDifferentStatusAndDecr() {
        task2.setStatus(Status.IN_PROGRESS);
        task2.setDescription("Task Two");
        assertFalse(task.equals(task2));
    }

    @Test
    void testDifferentPriorityAndDecAndStatus() {
        Priority p2 = new Priority(2);
        task2.setPriority(p2);
        task2.setStatus(Status.IN_PROGRESS);
        task2.setDescription("Task Two");
        assertFalse(task.equals(task2));
    }

    @Test
    void testAllDifferent() {
        Priority p2 = new Priority(2);
        Calendar nextDay = Calendar.getInstance();
        DueDate dueDate2 = new DueDate(nextDay.getTime());
        dueDate2.postponeOneDay();
        task2.setDueDate(dueDate2);
        task2.setPriority(p2);
        task2.setStatus(Status.IN_PROGRESS);
        task2.setDescription("Task Two");
        assertFalse(task.equals(task2));
    }

    @Test
    void testAddTagThrowExc(){
        try {
            Tag tagNull = null;
            task.addTag(tagNull);
            fail("NullArgumentException should have been thrown");
        } catch (NullArgumentException e) {
            System.out.println("NullArgumentException are expected to be threw");
        }
    }

    @Test
    void testRemoveTagThrowExc(){
        try {
            Tag tagNull = null;
            task.removeTag(tagNull);
            fail("NullArgumentException should have been thrown");
        } catch (NullArgumentException e) {
            System.out.println("NullArgumentException are expected to be threw");
        }
    }

    @Test
    void testAddSingleTag() {
        task.addTag(tagOne);
        assertEquals(1,task.getTags().size());
    }

    @Test
    void testAddSingleTagDuplicate() {
        task.addTag(tagOne);
        task.addTag(tagOne);
        assertEquals(1,task.getTags().size());
    }

    @Test
    void testAddDifferentTags() {
        task.addTag(tagOne);
        task.addTag(tagTwo);
        assertEquals(2,task.getTags().size());
    }

    @Test
    void testAddDifferentTagsDuplicate() {
        task.addTag(tagTwo);
        task.addTag(tagOne);
        task.addTag(tagTwo);
        task.addTag(tagTwo);
        task.addTag(tagOne);
        assertEquals(2,task.getTags().size());
    }

    @Test
    void testRemoveSingleTag() {
        task.addTag(tagOne);
        assertEquals(1,task.getTags().size());
        task.removeTag(tagOne);
        assertEquals(0,task.getTags().size());
    }

    @Test
    void testRemoveTagOutSideTheList() {
        task.addTag(tagOne);
        assertEquals(1,task.getTags().size());
        task.removeTag(tagTwo);
        assertEquals(1,task.getTags().size());
    }

    @Test
    void testRemoveMultipleTags() {
        task.addTag(tagTwo);
        task.addTag(tagOne);
        assertEquals(2,task.getTags().size());
        task.removeTag(tagOne);
        task.removeTag(tagTwo);
        assertEquals(0,task.getTags().size());
    }

    @Test
    void testGetProgress() {
        task.setProgress(24);
        assertEquals(24,task.getProgress());
    }

    @Test
    void testGetEstimatedTimeToComplete() {
        task.setEstimatedTimeToComplete(4);
        assertEquals(4,task.getEstimatedTimeToComplete());
    }

    @Test
    void testNegativeTime() {
        try {
            task.setEstimatedTimeToComplete(-8);
            fail();
        } catch (NegativeInputException e) {
            System.out.println("Throw NegativeInputException");
        }
    }

    @Test
    void testNegativeProgress() {
        try {
            task.setProgress(-49);
            fail();
        } catch (InvalidProgressException e) {
            System.out.println("Throw InvalidProgressException");
        }
    }

    @Test
    void testProgressBiggerThan100() {
        try {
            task.setProgress(102);
            fail();
        } catch (InvalidProgressException e) {
            System.out.println("Throw InvalidProgressException");
        }
    }

    @Test
    void testEqual() {
        assertTrue(task.equals(task));
    }

    @Test
    void testEqualWithDifferentType() {
        Project project = new Project("description");
        assertFalse(task.equals(project));
    }

    @Test
    void testToStingWithNullDueDate() {
        task.setDueDate(null);
        System.out.println(task.toString());
    }

    @Test
    void testToStringWithMoreThanOneTag() {
        System.out.println(task2.toString());
    }


}

