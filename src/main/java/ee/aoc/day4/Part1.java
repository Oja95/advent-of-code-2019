package ee.aoc.day4;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import ee.aoc.InputFileReaderUtil;

public class Part1 {

  private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("[yyyy-MM-dd HH:mm]");
  private static final Pattern GUARD_ID_PATTERN = Pattern.compile("#(\\d+)");

  public static void main(String[] args) {
    System.out.println(getMostSleptIdTimesSleptMinutes(InputFileReaderUtil.getLinesFromClassPathResourceFile("/day4/input.txt")));
  }

  static int getMostSleptIdTimesSleptMinutes(List<String> inputList) {
    CurrentIdHolder idHolder = new CurrentIdHolder();
    List<String> collect = inputList.stream().sorted(Comparator.comparing(Part1::parseDateFromInput)).collect(Collectors.toList());
    List<Event> eventList = new ArrayList<>();

    for (String s : collect) {
//      System.out.println(s);
      Matcher matcher = GUARD_ID_PATTERN.matcher(s);
      if (matcher.find()) {
        idHolder.setId(Integer.valueOf(matcher.group(1)));
        continue;
      }

      EventType type = s.endsWith("wakes up") ? EventType.WAKE_UP : EventType.FALL_ASLEEP;
      eventList.add(new Event(idHolder.getId(), type, parseDateFromInput(s)));
//      System.out.println(type + " " + idHolder.getId() + " " + parseDateFromInput(s));
    }

    var sleepTimes = new HashMap<Integer, Integer>();

    for (int i = 0; i < eventList.size(); i += 2) {
      Event fallAsleepEvent = eventList.get(i);
      Event wakeUpEvent = eventList.get(i + 1);
      if (fallAsleepEvent.id != wakeUpEvent.id || fallAsleepEvent.eventType != EventType.FALL_ASLEEP || wakeUpEvent.eventType != EventType.WAKE_UP)
        throw new RuntimeException("wtf?");


      System.out.println(getDatesMinuteDiff(fallAsleepEvent.eventDate, wakeUpEvent.eventDate));
      sleepTimes.put(fallAsleepEvent.id,
          sleepTimes.getOrDefault(fallAsleepEvent.id, 0) + getDatesMinuteDiff(fallAsleepEvent.eventDate, wakeUpEvent.eventDate));
    }

    int maxTime = 0;
    int maxId = 0;
    for (Map.Entry<Integer, Integer> integerIntegerEntry : sleepTimes.entrySet()) {
      Integer value = integerIntegerEntry.getValue();
      if (value > maxTime) {
        maxTime =value;
        maxId = integerIntegerEntry.getKey();
      }
    }

//    List<Map.Entry<Integer, Integer>> collect1 = sleepTimes.entrySet().stream().sorted(Comparator.comparing(Map.Entry::getValue)).collect(Collectors.toList());
//    for (Map.Entry<Integer, Integer> integerIntegerEntry : collect1) {
//      System.out.println(integerIntegerEntry.getKey() + " " + integerIntegerEntry.getValue());
//    }

//    int sleptMostId = collect1.get(collect1.size() - 1).getKey();
    int sleptMostId = maxId;
    System.out.println(maxId);

    List<Event> mostIdSleptEvents = eventList.stream().filter(x -> x.id == sleptMostId).collect(Collectors.toList());
    int[] sleepMinutes = new int[60];
    for (int i = 0; i < mostIdSleptEvents.size(); i += 2) {
      Event fallAsleepEvent = mostIdSleptEvents.get(i);
      Event wakeUpEvent = mostIdSleptEvents.get(i + 1);

      int fallAsleepMinute = fallAsleepEvent.eventDate.getMinutes();
      int wakeUpMinute = wakeUpEvent.eventDate.getMinutes();
      System.out.print(fallAsleepMinute + " " + wakeUpMinute);
      System.out.println();

      for (int j = fallAsleepMinute; j < wakeUpMinute; j++) {
        sleepMinutes[j]++;
      }
    }

    int maxSleptMinute = 0;
    int maxMinuteValue = 0;
    for (int i = 0; i < 60; i++) {
//      System.out.print(i + ":" + sleepMinutes[i] + " ");
      System.out.print(sleepMinutes[i] + " ");

      if (sleepMinutes[i] > maxMinuteValue) {
        maxSleptMinute = i;
        maxMinuteValue = sleepMinutes[i];
      }
    }

    System.out.println();
    System.out.println(maxSleptMinute + " " + maxMinuteValue);
    System.out.println(sleptMostId);

    return maxSleptMinute * sleptMostId;
  }

  static int getDatesMinuteDiff(Date date1, Date date2) {
    return Math.toIntExact(Duration.between(date1.toInstant(), date2.toInstant()).toMinutes());
  }


  private static Date parseDateFromInput(String input) {
    try {
      return DATE_FORMAT.parse(input);
    }
    catch (ParseException e) {
      throw new RuntimeException(e);
    }
  }

  private static class Event {
    private final int id;
    private final EventType eventType;
    private final Date eventDate;

    Event(int id, EventType eventType, Date eventDate) {
      this.id = id;
      this.eventType = eventType;
      this.eventDate = eventDate;
    }
  }

   private enum EventType {
    WAKE_UP, FALL_ASLEEP
  }

}
