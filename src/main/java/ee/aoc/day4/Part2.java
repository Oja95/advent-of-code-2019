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

@SuppressWarnings("Duplicates")
public class Part2 {

  private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("[yyyy-MM-dd HH:mm]");
  private static final Pattern GUARD_ID_PATTERN = Pattern.compile("#(\\d+)");

  public static void main(String[] args) {
    System.out.println(getMostSleptIdTimesSleptMinutes(InputFileReaderUtil.getLinesFromClassPathResourceFile("/day4/input.txt")));
  }

  static int getMostSleptIdTimesSleptMinutes(List<String> inputList) {
    CurrentIdHolder idHolder = new CurrentIdHolder();
    List<String> collect = inputList.stream().sorted(Comparator.comparing(Part2::parseDateFromInput)).collect(Collectors.toList());
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


//      System.out.println(getDatesMinuteDiff(fallAsleepEvent.eventDate, wakeUpEvent.eventDate));
      sleepTimes.put(fallAsleepEvent.id,
          sleepTimes.getOrDefault(fallAsleepEvent.id, 0) + getDatesMinuteDiff(fallAsleepEvent.eventDate, wakeUpEvent.eventDate));
    }

    var sleepTimesMap = new HashMap<Integer, int[]>();


    for (int i = 0; i < eventList.size(); i+=2) {
      Event fallSleep = eventList.get(i);
      Event wakeUp = eventList.get(i + 1);

      int[] sleepTimesArray = sleepTimesMap.getOrDefault(fallSleep.id, new int[60]);

      for (int j = fallSleep.eventDate.getMinutes(); j < wakeUp.eventDate.getMinutes(); j++) {
        sleepTimesArray[j]++;
      }

      sleepTimesMap.put(fallSleep.id, sleepTimesArray);
    }

    int mostSleptMinute = 0;
    int mostSleptValue = 0;
    int mostSleptId = 0;

    for (Map.Entry<Integer, int[]> integerEntry : sleepTimesMap.entrySet()) {
//      System.out.println(integerEntry.getKey());
      int[] value = integerEntry.getValue();
      for (int i = 0; i < 60; i++) {
        if (value[i] > mostSleptValue) {
          mostSleptMinute = i;
          mostSleptId = integerEntry.getKey();
          mostSleptValue = value[i];
        }
      }
    }


    System.out.println();
    System.out.println(mostSleptValue);
    System.out.println(mostSleptId);
    System.out.println(mostSleptMinute);
    return mostSleptMinute * mostSleptId;
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
