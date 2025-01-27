package fr.davidson.diff.jjoules.delta;

import fr.davidson.diff.jjoules.Configuration;
import fr.davidson.diff.jjoules.DiffJJoulesStep;
import fr.davidson.diff.jjoules.delta.data.Data;
import fr.davidson.diff.jjoules.delta.data.Datas;
import fr.davidson.diff.jjoules.delta.data.Deltas;
import fr.davidson.diff.jjoules.util.Constants;
import fr.davidson.diff.jjoules.util.JSONUtils;
import fr.davidson.diff.jjoules.util.MethodNamesPerClassNames;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author Benjamin DANGLOT
 * benjamin.danglot@davidson.fr
 * on 06/10/2021
 */
public class DeltaStep extends DiffJJoulesStep {

    public static final String PATH_TO_JSON_DATA_V1 = "data_v1.json";

    public static final String PATH_TO_JSON_DATA_V2 = "data_v2.json";

    public static final String PATH_TO_JSON_DELTA = "deltas.json";

    private static final Logger LOGGER = LoggerFactory.getLogger(DeltaStep.class);

    protected String getReportPathname() {
        return "delta";
    }

    @Override
    protected void _run(Configuration configuration) {
        this.configuration = configuration;
        LOGGER.info("Run Delta");
        final MethodNamesPerClassNames testsList = configuration.getTestsList();
        final Datas dataV1 = new Datas();
        final Datas dataV2 = new Datas();
        new MeasureEnergyConsumption().measureEnergyConsumptionForBothVersion(
                configuration,
                dataV1,
                dataV2,
                testsList
        );
        final Map<String, Data> mediansV1 = Computation.computeMedian(dataV1);
        final Map<String, Data> mediansV2 = Computation.computeMedian(dataV2);
        final Deltas deltas = Computation.computeDelta(mediansV1, mediansV2);
        JSONUtils.write(configuration.getOutput() + Constants.FILE_SEPARATOR + PATH_TO_JSON_DATA_V1, dataV1);
        configuration.setDataV1(dataV1);
        JSONUtils.write(configuration.getOutput() + Constants.FILE_SEPARATOR + PATH_TO_JSON_DATA_V2, dataV2);
        configuration.setDataV2(dataV2);
        JSONUtils.write(configuration.getOutput() + Constants.FILE_SEPARATOR + PATH_TO_JSON_DELTA, deltas);
        configuration.setDeltas(deltas);
    }


}
