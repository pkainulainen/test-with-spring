package com.testwithspring.intermediate;

import com.github.springtestdbunit.dataset.FlatXmlDataSetLoader;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ReplacementDataSet;
import org.springframework.core.io.Resource;

/**
 * This data set loader has two goals:
 * <ul>
 *     <li>
 *         It extends the {@code FlatXmlDataSetLoader} class because it enables
 *         column sensing.
 *     </li>
 *     <li>
 *         It allows us to use so called replacement data sets. The placeholder strings
 *         and actual values are configured in the private {@code createReplacementDataSet()} method.
 *     </li>
 * </ul>
 */
public class ReplacementDataSetLoader extends FlatXmlDataSetLoader {

    @Override
    protected IDataSet createDataSet(Resource resource) throws Exception {
        IDataSet flatXmlDataSet = super.createDataSet(resource);
        return createReplacementDataSet(flatXmlDataSet);
    }

    private IDataSet createReplacementDataSet(IDataSet flatXmlDataSet) {
        ReplacementDataSet replacementDataSet = new ReplacementDataSet(flatXmlDataSet);

        replacementDataSet.addReplacementObject("[empty_string]", "");
        replacementDataSet.addReplacementObject("[null]", null);

        return replacementDataSet;
    }
}
