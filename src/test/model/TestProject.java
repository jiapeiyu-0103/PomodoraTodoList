package model;

import model.exceptions.InvalidProgressException;
import model.exceptions.NegativeInputException;
import model.exceptions.NullArgumentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestProject {

    Task task1, task2, task3, task4;
    Project project;
    Project project1;

    @BeforeEach
    void runBefore () {
        task1 = new Task("task1");
        task2 = new Task("task2");
        task3 = new Task("task3");
        task4 = new Task("task4");
        task2.setStatus(Status.DONE);
        task3.setStatus(Status.DONE);
        project = new Project("project");
        project1 = new Project("project One");
    }

    @Test
    void testAddTwoSameTask() {
        project.add(task1);
        assertEquals(1, project.getNumberOfTasks());
        project.add(task1);
        assertEquals(1, project.getNumberOfTasks());
    }

    @Test
    void testAddTwoDifferentTask() {
        project.add(task1);
        assertEquals(1, project.getNumberOfTasks());
        project.add(task2);
        assertEquals(2, project.getNumberOfTasks());
    }

    @Test
    void testAddTwoDifferentTaskWithRemoveInside() {
        project.add(task1);
        assertEquals(1, project.getNumberOfTasks());
        project.add(task2);
        assertEquals(2, project.getNumberOfTasks());
        project.remove(task1);
        assertEquals(1, project.getNumberOfTasks());
    }

    @Test
    void testAddTwoDifferentTaskWithRemoveOutside() {
        project.add(task1);
        assertEquals(1, project.getNumberOfTasks());
        project.add(task2);
        assertEquals(2, project.getNumberOfTasks());
        project.remove(task4);
        assertEquals(2, project.getNumberOfTasks());
    }

    @Test
    void testGetDescription() {
        assertEquals("project",project.getDescription());
    }

    @Test
    void testProgress() {
        project1.add(task1);
        project1.add(task2);
        project1.add(task3);
        assertEquals(0, project1.getProgress());
        task1.setProgress(100);
        assertEquals(33,project1.getProgress());
        task2.setProgress(50);
        task3.setProgress(25);
        assertEquals(58,project1.getProgress());
        Project project2 = new Project("project Two");
        project2.add(task4);
        project2.add(project1);
        assertEquals(29,project2.getProgress());
    }

    @Test
    void testExceptionGetNegProgress() {
        try {
            task1.setProgress(-10);
        } catch (InvalidProgressException e) {
            System.out.println("The progress is invalid");
        }
    }

    @Test
    void testExceptionGetPosProgress() {
        try {
            task1.setProgress(120);
        } catch (InvalidProgressException e) {
            System.out.println("The progress is invalid");
        }
    }

    @Test
    void testExpSetEsmHours() {
        try {
            task1.setEstimatedTimeToComplete(-9);
        } catch (NegativeInputException e) {
            System.out.println("The Estimated hours ia invalid");
        }
    }

    @Test
    void testGetEstimatedTimeToComplete() {
        project1.add(task1);
        project1.add(task2);
        project1.add(task3);
        assertEquals(0,project1.getEstimatedTimeToComplete());
        task1.setEstimatedTimeToComplete(8);
        assertEquals(8,project1.getEstimatedTimeToComplete());
        task2.setEstimatedTimeToComplete(2);
        task3.setEstimatedTimeToComplete(10);
        assertEquals(20,project1.getEstimatedTimeToComplete());
        Project project2 = new Project("Project Two");
        project2.add(task4);
        task4.setEstimatedTimeToComplete(4);
        project2.add(project1);
        assertEquals(24,project2.getEstimatedTimeToComplete());
    }


    @Test
    void testIsCompleteWithNoTasks() {
        assertFalse(project1.isCompleted());
    }

    @Test
    void testIsCompleteWithTasksButNot100() {
        task1.setProgress(45);
        project1.add(task1);
        assertFalse(project1.isCompleted());
    }

    @Test
    void testIsCompleteWithTasksBut100() {
        task1.setProgress(100);
        task2.setProgress(100);
        project1.add(task1);
        project1.add(task2);
        assertTrue(project1.isCompleted());
    }

    @Test
    void testIsCompleteWithTasksSomeAre100() {
        task1.setProgress(45);
        task2.setProgress(100);
        project1.add(task1);
        project1.add(task2);
        assertFalse(project1.isCompleted());
    }

    @Test
    void testNullTask() {
        try {
            project1.add(null);
            fail();
        } catch (NullArgumentException e) {
            System.out.println("Illegal argument: task is null");
        }
    }

    @Test
    void testEquals() {
        project1.equals(new Project("project One"));
        project1.equals(project1);
    }

    @Test
    void testHashCode() {
        Project project2 = new Project("project One");
        assertEquals(project1.hashCode(),project2.hashCode());
    }

    @Test
    void testAddProjectItself() {
        project1.add(project1);
        assertEquals(0,project1.getNumberOfTasks());
    }

    @Test
    void testGetTasksThrowExp() {
        try {
            project.getTasks();
            fail();
        } catch (UnsupportedOperationException e) {
            System.out.println("This operation is not allowed");
        }
    }

    @Test
    void testZeroTasksInProject() {
        Project projectNoTasks = new Project("project with no task");
        assertEquals(0,projectNoTasks.getProgress());
    }
}