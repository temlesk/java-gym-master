package ru.yandex.practicum.gym;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TimetableTest {

    @Test
    void testGetTrainingSessionsForDaySingleSession() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        //Проверить, что за понедельник вернулось одно занятие
        Map<TimeOfDay, List<TrainingSession>> trainingsMonday = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        assertEquals(1, trainingsMonday.size());

        List<TrainingSession> timeTrainings = trainingsMonday.get(new TimeOfDay(13, 0));
        assertEquals(1, timeTrainings.size());
        //Проверить, что за вторник не вернулось занятий
        Map<TimeOfDay, List<TrainingSession>> trainingsThursday = timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);
        assertEquals(0, trainingsThursday.size());
    }

    @Test
    void testGetTrainingSessionsForDayMultipleSessions() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        TrainingSession sessionMonday = new TrainingSession(group, coach, DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        timetable.addNewTrainingSession(sessionMonday);

        TrainingSession sessionThursday1 = new TrainingSession(group, coach, DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        timetable.addNewTrainingSession(sessionThursday1);

        TrainingSession sessionThursday2 = new TrainingSession(group, coach, DayOfWeek.THURSDAY, new TimeOfDay(20, 0));
        timetable.addNewTrainingSession(sessionThursday2);

        // Проверить, что за понедельник вернулось одно занятие
        Map<TimeOfDay, List<TrainingSession>> trainingsMonday = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        assertEquals(1, trainingsMonday.size());

        List<TrainingSession> timeTrainings = trainingsMonday.get(new TimeOfDay(13, 0));
        assertEquals(1, timeTrainings.size());

        // Проверить, что за четверг вернулось два занятия в правильном порядке: сначала в 13:00, потом в 20:00
        Map<TimeOfDay, List<TrainingSession>> trainingsThursday = timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);
        assertEquals(2, trainingsThursday.size());

        List<TrainingSession> timeTrainingsThursday13 = trainingsThursday.get(new TimeOfDay(13, 0));
        assertEquals(1, timeTrainingsThursday13.size());
        List<TrainingSession> timeTrainingsThursday20 = trainingsThursday.get(new TimeOfDay(20, 0));
        assertEquals(1, timeTrainingsThursday20.size());

        // Проверить, что за вторник не вернулось занятий
        Map<TimeOfDay, List<TrainingSession>> trainingsTuesday = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        assertEquals(0, trainingsTuesday.size());
    }


    @Test
    void testGetTrainingSessionsForDayAndTime() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        //Проверить, что за понедельник в 13:00 вернулось одно занятие
        Map<TimeOfDay, List<TrainingSession>> trainingMonday13 = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        List<TrainingSession> sessionsAt13 = trainingMonday13.get(new TimeOfDay(13, 0));
        assertEquals(1, sessionsAt13.size());

        //Проверить, что за понедельник в 14:00 не вернулось занятий
        Map<TimeOfDay, List<TrainingSession>> trainingMonday14 = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        List<TrainingSession> sessionsAt14 = trainingMonday14.getOrDefault(new TimeOfDay(14, 0), Collections.emptyList());
        assertEquals(0, sessionsAt14.size());
    }

    //Мои тесты
    //Проверяем, что лишь одно занятие в указанное время и день
    @Test
    void testGetTrainingSessionsForDayAndTime_WhenSessionExists_ReturnsCorrectList(){
        Timetable timetable = new Timetable();
        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        List<TrainingSession> result = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(13,0));
        assertEquals(1, result.size());
        assertEquals(singleTrainingSession, result.get(0));
    }

    @Test
    void testGetTrainingSessionsForDayAndTime_WhenMultipleSessionExists_ReturnsCorrectList(){
        Timetable timetable = new Timetable();
        Coach coachA = new Coach("Петров", "Петр", "Петрович");
        Group groupA = new Group("Борьба", Age.ADULT, 120);
        Coach coachB = new Coach("Александров", "Александр", "Александрович");
        Group groupB = new Group("Бокс", Age.CHILD, 90);
        TrainingSession singleTrainingSession1 = new TrainingSession(groupA, coachA,
                DayOfWeek.MONDAY, new TimeOfDay(12, 0));
        TrainingSession singleTrainingSession2 = new TrainingSession(groupB, coachB,
                DayOfWeek.TUESDAY, new TimeOfDay(12, 0));

        timetable.addNewTrainingSession(singleTrainingSession1);
        timetable.addNewTrainingSession(singleTrainingSession2);

        List<TrainingSession> result = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(12, 0));

        assertEquals(1, result.size());
//        assertEquals(result.contains(singleTrainingSession1));
//        assertEquals(result.contains(singleTrainingSession2));
    }



    //Тесты для методе getCountByCoaches
    @Test
    void testGetCountMultipleSessionByCoaches(){
        Timetable timetable = new Timetable();
        Coach coachА = new Coach("Пупкин", "Василий","Васиьевич");
        Group groupА = new Group("Акробатика", Age.CHILD, 60);

        timetable.addNewTrainingSession(new TrainingSession(groupА, coachА, DayOfWeek.MONDAY, new TimeOfDay(14, 0)));
        timetable.addNewTrainingSession(new TrainingSession(groupА, coachА, DayOfWeek.THURSDAY, new TimeOfDay(12, 0)));
        timetable.addNewTrainingSession(new TrainingSession(groupА, coachА, DayOfWeek.SATURDAY, new TimeOfDay(18, 0)));

        List<CounterOfTrainings> result = timetable.getCountByCoaches();
        assertEquals(1, result.size());
    }

    @Test
    void testGetCountMultipleSessionByCoachesSortedDescending(){
        Timetable timetable = new Timetable();
        Coach coachA = new Coach("Петров", "Петр", "Петрович");
        Group groupA = new Group("Борьба", Age.ADULT, 120);
        Coach coachB = new Coach("Александров", "Александр", "Александрович");
        Group groupB = new Group("Бокс", Age.CHILD, 90);

        timetable.addNewTrainingSession(new TrainingSession(groupA, coachA, DayOfWeek.TUESDAY, new TimeOfDay(12, 0)));
        timetable.addNewTrainingSession(new TrainingSession(groupB, coachB, DayOfWeek.MONDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(groupA, coachA, DayOfWeek.SATURDAY, new TimeOfDay(9, 0)));
        timetable.addNewTrainingSession(new TrainingSession(groupB, coachB, DayOfWeek.FRIDAY, new TimeOfDay(20, 0)));

        List<CounterOfTrainings> result = timetable.getCountByCoaches();
        assertEquals(2, result.size());
    }

    @Test
    public void testEmptyTimetableReturnsEmptyList() {
        Timetable timetable = new Timetable();
        List<CounterOfTrainings> result = timetable.getCountByCoaches();
        assertTrue(result.isEmpty());
    }

}
