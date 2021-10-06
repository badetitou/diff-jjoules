package fr.davidson.diff.jjoules;

import fr.davidson.diff.jjoules.util.Utils;
import fr.davidson.diff.jjoules.util.maven.MavenRunner;
import org.junit.jupiter.api.BeforeEach;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * @author Benjamin DANGLOT
 * benjamin.danglot@davidson.fr
 * on 06/10/2021
 */
public abstract class AbstractDiffJJoulesStepTest {

    public static final String ROOT_PATH_V1 = "src/test/resources/diff-jjoules-demo/";
    public static final String TARGET_FOLDER_PATH_V1 = ROOT_PATH_V1 + "/target/";
    public static final String JJOULES_REPORT_PATH_V1 = TARGET_FOLDER_PATH_V1 + "/jjoules-reports/";
    public static final String CLASSPATH_PATH_V1 = ROOT_PATH_V1 + "/classpath";

    public static final String ROOT_PATH_V2 = "src/test/resources/diff-jjoules-demo-v2/";
    public static final String CLASSPATH_PATH_V2 = ROOT_PATH_V2 + "/classpath";
    public static final String TARGET_FOLDER_PATH_V2 = ROOT_PATH_V2 + "/target/";
    public static final String JJOULES_REPORT_PATH_V2 = TARGET_FOLDER_PATH_V2 + "/jjoules-reports/";

    public static final String DIFF_JJOULES_FOLDER_PATH = ROOT_PATH_V1 + "/diff-jjoules";
    public static final String SIMPLE_NAME_TEST_CLASS = "InternalListTest";
    public static final String PACKAGE_TEST_CLASS = "fr.davidson.diff_jjoules_demo";
    public static final String FULL_QUALIFIED_NAME_TEST_CLASS = PACKAGE_TEST_CLASS + "." + SIMPLE_NAME_TEST_CLASS;
    public static final String TEST_COUNT = "testCount";
    public static final String JAVA_EXTENSION = ".java";
    public static final String PACKAGE_PATH = "/fr/davidson/diff_jjoules_demo/";
    public static final String TEST_CLASS_PATH = PACKAGE_PATH + SIMPLE_NAME_TEST_CLASS + JAVA_EXTENSION;

    protected Configuration getConfiguration() {
        final Configuration configuration = new Configuration(
                new File(ROOT_PATH_V1).getAbsolutePath(),
                new File(ROOT_PATH_V2).getAbsolutePath(),
                "",
                CLASSPATH_PATH_V1,
                CLASSPATH_PATH_V2,
                Utils.readClasspathFile(CLASSPATH_PATH_V1).split(":"),
                Utils.readClasspathFile(CLASSPATH_PATH_V1).split(":"),
                false,
                5
        );
        configuration.setTestsList(
                new HashMap<String, List<String>>() {
                    {
                        put(FULL_QUALIFIED_NAME_TEST_CLASS, new ArrayList<>());
                        get(FULL_QUALIFIED_NAME_TEST_CLASS).addAll(Collections.singletonList(TEST_COUNT));
                    }
                }
        );
        return configuration;
    }

    @BeforeEach
    protected void setUp() throws IOException {
        // compile
        MavenRunner.runCleanAndCompile(ROOT_PATH_V1);
        MavenRunner.runCleanAndCompile(ROOT_PATH_V2);
        new File(DIFF_JJOULES_FOLDER_PATH).delete();
        new File(JJOULES_REPORT_PATH_V1).mkdirs();
        new File(JJOULES_REPORT_PATH_V2).mkdirs();
        Files.copy(
                Paths.get(ROOT_PATH_V1 + "fr.davidson.diff_jjoules_demo.InternalListTest#testCount_v1.json"),
                Paths.get(JJOULES_REPORT_PATH_V1 + "fr.davidson.diff_jjoules_demo.InternalListTest#testCount.json"),
                StandardCopyOption.REPLACE_EXISTING
        );
        Files.copy(
                Paths.get(ROOT_PATH_V1 + "fr.davidson.diff_jjoules_demo.InternalListTest#testCount_v2.json"),
                Paths.get(JJOULES_REPORT_PATH_V2 + "fr.davidson.diff_jjoules_demo.InternalListTest#testCount.json"),
                StandardCopyOption.REPLACE_EXISTING
        );
    }

}
