package fr.davidson.diff.jjoules.class_instrumentation.configuration;

import java.io.File;
import java.util.Arrays;

/**
 * @author Benjamin DANGLOT
 * benjamin.danglot@davidson.fr
 * 27/11/2020
 */
public class Configuration {

    public final String pathToFirstVersion;

    public final String pathToSecondVersion;

    public final String pathToTestListAsCSV;

    public final String[] classpath;

    public final String[] classpathV1;

    public final String[] classpathV2;

    public final int nbDuplication;

    public final boolean shouldRandomize;

    public Configuration(String pathToFirstVersion,
                         String pathToSecondVersion,
                         String pathToTestListAsCSV,
                         String[] classpath,
                         String[] classpathV2,
                         int nbDuplication,
                         boolean shouldRandomize) {
        this.pathToFirstVersion = pathToFirstVersion;
        this.pathToSecondVersion = pathToSecondVersion;
        this.pathToTestListAsCSV = new File(pathToTestListAsCSV).isAbsolute() ? pathToTestListAsCSV : this.pathToFirstVersion + "/" + pathToTestListAsCSV;
        this.classpath = classpath;
        this.classpathV1 = classpath;
        this.classpathV2 = classpathV2;
        this.nbDuplication = nbDuplication;
        this.shouldRandomize = shouldRandomize;
    }

    @Override
    public String toString() {
        return "Configuration{" +
                "pathToFirstVersion='" + pathToFirstVersion + '\'' +
                ", pathToSecondVersion='" + pathToSecondVersion + '\'' +
                ", pathToTestListAsCSV='" + pathToTestListAsCSV + '\'' +
                '}';
    }
}