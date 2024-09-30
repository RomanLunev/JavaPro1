import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        List<Integer> integers = new ArrayList<>(Arrays.asList(5, 2, 10, 9, 4, 3, 10, 1, 13));
        List<Integer> resultIntegers;

        System.out.println("// Реализуйте удаление из листа всех дубликатов: ");
        resultIntegers = integers.stream()
                .distinct()
                .toList();
        System.out.println(resultIntegers);

        System.out.println("// Найдите в списке целых чисел 3-е наибольшее число (пример: 5 2 10 9 4 3 10 1 13 => 10): ");
        Integer resultInteger = integers.stream()
                .sorted(Comparator.reverseOrder())
                .skip(2)
                .findAny()
                .orElse(null);

        System.out.println(resultInteger);

        System.out.println("// Найдите в списке целых чисел 3-е наибольшее «уникальное» число (пример: 5 2 10 9 4 3 10 1 13 => 9, в отличие от прошлой задачи здесь разные 10 считает за одно число): ");

        Integer resultInteger2 = integers.stream()
                .sorted(Comparator.reverseOrder())
                .distinct()
                .skip(2)
                .findAny()
                .orElse(null);

        System.out.println(resultInteger2);

        List<Person> people = new ArrayList<>(Arrays.asList(
                new Person("Bob", 20, "Инженер"),
                new Person("Rob", 30, "Инженер"),
                new Person("Jack", 40, "Инженер"),
                new Person("John", 50, "Инженер"),
                new Person("Sam", 60, "Менеджер")
        ));

        System.out.println("// Имеется список объектов типа Сотрудник (имя, возраст, должность), необходимо получить список имен 3 самых старших сотрудников с должностью «Инженер», в порядке убывания возраста: ");
        List<String> resultPeople = people.stream()
                .filter(x->"Инженер".equals(x.getPosition()))
                .sorted(Comparator.reverseOrder())
                .limit(3)
                .map(Person::getName)
                .toList();
        System.out.println(resultPeople);

        System.out.println("//Имеется список объектов типа Сотрудник (имя, возраст, должность), посчитайте средний возраст сотрудников с должностью «Инженер»: ");
        Double averageAge = people.stream()
                .filter(x->"Инженер".equals(x.getPosition()))
                .collect(Collectors.averagingDouble(Person::getAge));
        System.out.println(averageAge);

        System.out.println("// Найдите в списке слов самое длинное: ");
        List<String> words = new ArrayList<>(Arrays.asList("one", "two", "three", "four","five"));
        String longestWorld = words.stream()
                .reduce((x,y)->(x.length()>=y.length())?x:y).orElse(null);
        System.out.println(longestWorld);

        System.out.println("//Имеется строка с набором слов в нижнем регистре, разделенных пробелом. Постройте хеш-мапы, в которой будут хранится пары: слово - сколько раз оно встречается во входной строке: ");
        String sentence = "one two two three three three";
        Map<String,Long> wordsCount = Arrays.stream(sentence.split(" "))
                .collect(Collectors.groupingBy(String::valueOf, Collectors.counting()));
        System.out.println(wordsCount);

        System.out.println("//Отпечатайте в консоль строки из списка в порядке увеличения длины слова, если слова имеют одинаковую длины, то должен быть сохранен алфавитный порядок: ");
        words.stream()
                .sorted((x, y) -> {
                    if (x.length()>y.length()) return 1;
                    else if (x.length()<y.length()) return -1;
                    else return x.compareTo(y);
                    })
                .forEach(System.out::println);

        System.out.println("//Имеется массив строк, в каждой из которых лежит набор из 5 строк, разделенных пробелом, найдите среди всех слов самое длинное, если таких слов несколько, получите любое из них: ");
        String[] strings = new String[] {"one two three four five", "six seven eight nine ten", "eleven twelve thirteen fourteen fifteen"};
        longestWorld = Arrays.stream(strings)
                .flatMap(x->Arrays.stream(x.split(" ")))
                .reduce((x,y)->(x.length()>=y.length())?x:y).orElse(null);
        System.out.println(longestWorld);
    }
}

class Person implements Comparable<Person> {
    String name;
    int age;
    String position;

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getPosition() {
        return position;
    }

    @Override
    public String
    toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", position='" + position + '\'' +
                '}';
    }

    @Override
    public int compareTo(Person o) {
        if (this.age > o.age) {
            return 1;
        } else if (this.age < o.age) {
            return -1;
        }
        return 0;
    }

    public Person(String name, int age, String position) {
        this.name = name;
        this.age = age;
        this.position = position;
    }
}
