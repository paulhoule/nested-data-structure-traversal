import java.util.*;
import java.util.function.Function;

public class MaterializeRecords {
    static List<Section> input() {
        return List.of(
                new Section("Getting started", List.of("Welcome", "Installation")),
                new Section("Basic operator", List.of("Addition / Subtraction","Multiplication / Division")),
                new Section("Advanced operator", true, List.of("Mutability","Immutability")));
    }

    public static void main(String argv[]) {
        System.out.println(materialize(MaterializeRecords::number, input()));
    }

    static int sectionCounter = 1;
    static int lessonCounter = 1;

    static Section number(Section section) {
        lessonCounter = section.reset() ? 1 : lessonCounter;
        Function<Lesson,Lesson> number = (lesson) -> new Lesson(lesson.name(), lessonCounter++);
        return new Section(section.title(), section.reset(), sectionCounter++, materialize(number, section.lessons()));
    }

    record Lesson(String name, Integer position) {}

    record Section(String title, boolean reset, Integer position, List<Lesson> lessons) {
        public Section(String title, List<String> lessons) {
            this(title, false, lessons);
        }

        public Section(String title, boolean resetPos, List<String> lessons) {
            this(title, resetPos, null, materialize((name) -> new Lesson(name, null), lessons));
        };
    }

    static <X,Y> List<Y> materialize(Function<X,Y> f, Iterable<X> x) {
        var y= new ArrayList<Y>();
        x.forEach((that) -> y.add(f.apply(that)));
        return y;
    };
}
