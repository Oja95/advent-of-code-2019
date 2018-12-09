package ee.aoc.day9;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

class ShiftingArrayList<T> extends ArrayList<T> {

  ShiftingArrayList(Collection<? extends T> c) {
    super(c);
  }

  ShiftingArrayList() {}

  List<T> addAndShift(T element, int index) {
    List<T> after = new ArrayList<>(subList(index, size()));
    List<T> before = new ArrayList<>(subList(0, index));

    before.add(element);
    before.addAll(after);

    return before;
  }
}
