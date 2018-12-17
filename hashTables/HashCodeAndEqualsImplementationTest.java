import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Hashing with wrong hashCode() or equals(). Suppose that you implement a data type OlympicAthlete
 * for use in a java.util.HashMap.
 */
public class HashCodeAndEqualsImplementationTest {

    /**
     * Describe what happens if you override hashCode() but not equals().
     */
    private static class OlympicAthlete1 {
        String name;

        public OlympicAthlete1(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }
    }

    /**
     * Describe what happens if you override equals() but not hashCode().
     */
    private static class OlympicAthlete2 {
        String name;

        public OlympicAthlete2(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof OlympicAthlete2)) return false;
            OlympicAthlete2 that
                    = (OlympicAthlete2) o;
            return Objects.equals(name, that.name);
        }

    }

    /**
     * Describe what happens if you override hashCode() but implement <code>public boolean
     * equals(OlympicAthlete that)</code> instead of <code>public boolean equals(Object
     * that)</code>
     */
    private static class OlympicAthlete3 {
        String name;

        public OlympicAthlete3(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }

        public boolean equals(OlympicAthlete3 o) {
            if (this == o) return true;
            if (!(o instanceof OlympicAthlete3))
                return false;
            OlympicAthlete3 that
                    = (OlympicAthlete3) o;
            return Objects.equals(name, that.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }
    }


    public static void main(String[] args) {
        {
            // Override hashcode but no equals -> can't find (identity equals
            // will always be false)
            Map<OlympicAthlete1, Integer> map = new HashMap<>();
            for (int i = 1; i < 10; i++) {
                map.put(new OlympicAthlete1("AthleteHash" + i), i);
            }
            System.out.println("The map:" + map);
            OlympicAthlete1
                    athlete = new OlympicAthlete1("AthleteHash1");
            System.out.println("Can't find:" + map.get(athlete));
        }

        {
            // Override equals but no hashcode -> can't find (new object's identity
            // hashcode will be different than the objects in the map)
            Map<OlympicAthlete2, Integer> map = new HashMap<>();
            for (int i = 1; i < 10; i++) {
                map.put(new OlympicAthlete2("AthleteEq" + i), i);
            }
            System.out.println("The map:" + map);
            OlympicAthlete2 athlete = new OlympicAthlete2("AthleteEq1");
            System.out.println("We also can't find it:" + map.get(athlete));
        }

        {
            // Override wrong equals which won't be called and no hashcode
            // -> can't find (new object's identity hashcode will be different
            // than the objects in the map)
            Map<OlympicAthlete3, Integer> map = new HashMap<>();
            for (int i = 1; i < 10; i++) {
                map.put(new OlympicAthlete3("AthleteWrongEq" + i), i);
            }
            System.out.println("The map:" + map);
            OlympicAthlete3 athlete = new OlympicAthlete3("AthleteWrongEq1");
            System.out.println("We also can't find it:" + map.get(athlete));
        }

    }
}
