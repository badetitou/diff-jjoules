package fr.davidson.diff.jjoules.delta;

import fr.davidson.diff.jjoules.Configuration;
import fr.davidson.diff.jjoules.delta.data.Datas;
import fr.davidson.diff.jjoules.report.ReportEnum;
import fr.davidson.diff.jjoules.util.Utils;
import fr.davidson.diff.jjoules.util.maven.MavenRunner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Benjamin DANGLOT
 * benjamin.danglot@davidson.fr
 * on 25/09/2021
 */
public class MeasureEnergyConsumptionTest {

    private class MockConfiguration extends Configuration {
        public MockConfiguration() {
            super(
                    new File("src/test/resources/diff-jjoules-demo/").getAbsolutePath(),
                    new File("src/test/resources/diff-jjoules-demo/").getAbsolutePath(),
                    "",
                    Utils.readClasspathFile("src/test/resources/diff-jjoules-demo/classpath").split(":"),
                    Utils.readClasspathFile("src/test/resources/diff-jjoules-demo/classpath").split(":"),
                    false,
                    5,
                    "", "", "", "",
                    "src/test/java/fr/davidson/diff/jjoules/delta/MeasureEnergyConsumptionTest.java",
                    "", "", "",
                    "", "", "",
                    "", "", "", ReportEnum.NONE
            );
        }
    }

    private String[] classpath;

    @BeforeEach
    void setUp() throws IOException {
        // compile
        MavenRunner.runCleanAndCompile("src/test/resources/diff-jjoules-demo");
        new File("src/test/resources/diff-jjoules-demo/target/jjoules-reports/").mkdirs();
        Path dst = Paths.get("src/test/resources/diff-jjoules-demo/target/jjoules-reports/com.google.gson.CommentsTest-testParseComments.json");
        Path src = Paths.get("src/test/resources/json/v1/com.google.gson.CommentsTest-testParseComments.json");
        Files.copy(src, dst, StandardCopyOption.REPLACE_EXISTING);
        this.classpath = Utils.readClasspathFile("src/test/resources/diff-jjoules-demo/classpath").split(":");
    }

    @Test
    void test() {
        final Datas dataV1 = new Datas();
        final Datas dataV2 = new Datas();
        assertTrue(dataV1.isEmpty());
        assertTrue(dataV2.isEmpty());
        new MeasureEnergyConsumption().measureEnergyConsumptionForBothVersion(
                new MockConfiguration(),
                dataV1,
                dataV2,
                new HashMap<String, List<String>>() {
                    {
                        put("fr.davidson.diff_jjoules_demo.InternalListTest", new ArrayList<>());
                        get("fr.davidson.diff_jjoules_demo.InternalListTest").addAll(Arrays.asList("testMapEmptyList"));
                    }
                }
        );
        assertFalse(dataV1.isEmpty());
        assertFalse(dataV2.isEmpty());
    }
}