package fr.davidson.diff.jjoules.delta;

import fr.davidson.diff.jjoules.delta.data.Data;
import fr.davidson.diff.jjoules.delta.data.Datas;
import fr.davidson.diff.jjoules.delta.data.Deltas;
import fr.davidson.diff.jjoules.util.JSONUtils;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Benjamin DANGLOT
 * benjamin.danglot@davidson.fr
 * on 24/09/2021
 */
public class ComputationTest {

    public static final String DATA_V_1_JSON = "src/test/resources/json/data_v1.json";

    public static final String DATA_V_2_JSON = "src/test/resources/json/data_v2.json";

    @Test
    void test() {
        Datas d1 = new Datas();
        d1.put("key", new ArrayList<>());
        d1.get("key").add(new Data(0, 0, 0, 0, 0, 0, 0, 0));
        d1.get("key").add(new Data(50, 50, 50, 50, 50, 50, 50, 50));

        Datas d2 = new Datas();
        d2.put("key", new ArrayList<>());

        d2.get("key").add(new Data(10, 10, 10, 10, 10, 10, 10, 10));
        assertFalse(d1.isEmptyIntersectionPerTestMethodName(d2).get("key"));

        d2.get("key").clear();
        d2.get("key").add(new Data(-1, -1, -1, -1, -1, -1, -1, -1));
        assertTrue(d1.isEmptyIntersectionPerTestMethodName(d2).get("key"));

        d2.get("key").clear();
        d2.get("key").add(new Data(10, 10, 10, 10, 10, 10, 10, 10));
        d2.get("key").add(new Data(-1, -1, -1, -1, -1, -1, -1, -1));
        assertFalse(d1.isEmptyIntersectionPerTestMethodName(d2).get("key"));
    }

    @Test
    void testIsEmptyIntersectionPerTestMethodName() {
        final Datas datas1 = JSONUtils.read("src/test/resources/json/gson_data_v1.json", Datas.class);
        final Datas datas2 = JSONUtils.read("src/test/resources/json/gson_data_v1.json", Datas.class);
        final Map<String, Boolean> emptyIntersectionPerTestMethodName = datas1.isEmptyIntersectionPerTestMethodName(datas2);
        assertEquals(0, emptyIntersectionPerTestMethodName.keySet().stream().filter(emptyIntersectionPerTestMethodName::get).count());
    }

    @Test
    void testGetMedian() {
        assertEquals(30.0D, Computation.getMedian(Arrays.asList(10.0D, 50.0D)));
        assertEquals(30.0D, Computation.getMedian(Arrays.asList(10.0D, 30.0D, 50.0D)));
        assertEquals(50.0D, Computation.getMedian(Arrays.asList(10.0D, 25.0D, 75.0D, 100.0D)));
    }

    @Test
    void testComputeMedian() {
        final Datas datas = JSONUtils.read(DATA_V_1_JSON, Datas.class);
        final Map<String, Data> medians = Computation.computeMedian(datas);
        final String testName = "fr.davidson.diff_jjoules_demo.InternalListTest#testCount";
        final Data data = medians.get(testName);
        assertEquals(1.31596288E8, data.energy);
    }

    @Test
    void testComputeDelta() {
        final Map<String, Data> medians1 = new HashMap<String, Data>() {
            {
                put("test", new Data(
                        0.0D, 10.0D, 10.0D, 10.0D,
                        0.0D, -10.0D, -10.0D, -10.0D)
                );
            }
        };
        final Map<String, Data> medians2 = new HashMap<String, Data>() {
            {
                put("test", new Data(
                        0.0D, -10.0D, -10.0D, -10.0D,
                        0.0D, 10.0D, 10.0D, 10.0D)
                );
            }
        };
        final Deltas deltas = Computation.computeDelta(medians1, medians2);
        assertEquals(0.0D, deltas.get("test").energy);
        assertEquals(-20.0D, deltas.get("test").instructions);
        assertEquals(-20.0D, deltas.get("test").durations);
        assertEquals(-20.0D, deltas.get("test").cycles);
        assertEquals(0.0D, deltas.get("test").caches);
        assertEquals(20.0D, deltas.get("test").cacheMisses);
        assertEquals(20.0D, deltas.get("test").branches);
        assertEquals(20.0D, deltas.get("test").branchMisses);
    }
}