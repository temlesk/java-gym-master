package ru.yandex.practicum.gym;

import java.util.*;

public class Timetable {

    private Map<DayOfWeek, TreeMap<TimeOfDay, List<TrainingSession>>> timetable = new HashMap<>();

    public void addNewTrainingSession(TrainingSession trainingSession) {
        //сохраняем занятие в расписании
        DayOfWeek dayOfWeek = trainingSession.getDayOfWeek();
        TimeOfDay timeOfDay = trainingSession.getTimeOfDay();

        TreeMap<TimeOfDay, List<TrainingSession>> dayTrainings = timetable.get(dayOfWeek);
        if (Objects.isNull(dayTrainings)) {
            dayTrainings = new TreeMap<>();
            timetable.put(dayOfWeek, dayTrainings);
        }

        List<TrainingSession> trainingSessions = dayTrainings.get(timeOfDay);
        if (Objects.isNull(trainingSessions)) {
            trainingSessions = new ArrayList<>();
            dayTrainings.put(timeOfDay, trainingSessions);
        }
        trainingSessions.add(trainingSession);
    }

    public Map<TimeOfDay, List<TrainingSession>> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        //как реализовать, тоже непонятно, но сложность должна быть О(1)
        TreeMap<TimeOfDay, List<TrainingSession>> trainings = timetable.get(dayOfWeek);
        return Objects.nonNull(trainings) ? trainings : new TreeMap<>();
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        //как реализовать, тоже непонятно, но сложность должна быть О(1)
        TreeMap<TimeOfDay, List<TrainingSession>> dayTrainings = timetable.get(dayOfWeek);
        if (Objects.isNull(dayTrainings)) {
            return new ArrayList<>();
        }
        return dayTrainings.getOrDefault(timeOfDay, new ArrayList<>());

    }

    public List<CounterOfTrainings> getCountByCoaches() {
        Map<Coach, Integer> countByCoaches = new HashMap<>();
        for (Map<TimeOfDay, List<TrainingSession>> daySchedule : timetable.values()) {
            for (List<TrainingSession> sessions : daySchedule.values()) {
                for (TrainingSession session : sessions) {
                    Coach coach = session.getCoach();
                    countByCoaches.put(coach, countByCoaches.getOrDefault(coach, 0) + 1);
                }
            }
        }
        List<CounterOfTrainings> result = new ArrayList<>();
        for (Map.Entry<Coach, Integer> entry : countByCoaches.entrySet()) {
            result.add(new CounterOfTrainings(entry.getKey(), entry.getValue()));
        }

        // Сортируем по убыванию количества тренировок
        result.sort((c1, c2) -> Integer.compare(c2.getCount(), c1.getCount()));

        return result;
    }
}