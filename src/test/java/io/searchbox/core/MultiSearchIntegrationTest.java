package io.searchbox.core;

import fr.tlrx.elasticsearch.test.annotations.ElasticsearchNode;
import fr.tlrx.elasticsearch.test.support.junit.runners.ElasticsearchRunner;
import io.searchbox.Action;
import io.searchbox.client.ElasticSearchResult;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static junit.framework.Assert.*;

/**
 * @author Dogukan Sonmez
 */
@RunWith(ElasticsearchRunner.class)
@ElasticsearchNode
public class MultiSearchIntegrationTest extends AbstractIntegrationTest{

    @Test
    public void multiSearch() {
        try {
            MultiSearch multiSearch = new MultiSearch();
            Search search = new Search("{\"match_all\" : {}}");
            multiSearch.addSearch(search);
            executeTestCase(multiSearch);
        } catch (Exception e) {
            fail("Failed during the multi search valid parameters. Exception:" + e.getMessage());
        }
    }

    @Test
    public void singleMultiSearchWitIndex() {
        try {
            MultiSearch multiSearch = new MultiSearch();
            Search search = new Search("{\"match_all\" : {}}");
            search.addIndex("twitter");
            multiSearch.addSearch(search);
            executeTestCase(multiSearch);
        } catch (Exception e) {
            fail("Failed during the multi search valid parameters. Exception:" + e.getMessage());
        }
    }

    @Test
    public void MultiSearchWitIndex() {
        try {
            MultiSearch multiSearch = new MultiSearch();
            Search search = new Search("{\"match_all\" : {}}");
            search.addIndex("twitter");
            multiSearch.addSearch(search);
            Search search2 = new Search("{\"match_all\" : {}}");
            multiSearch.addSearch(search2);
            executeTestCase(multiSearch);
        } catch (Exception e) {
            fail("Failed during the multi search valid parameters. Exception:" + e.getMessage());
        }
    }

    private void executeTestCase(Action action) throws RuntimeException, IOException {
        ElasticSearchResult result = client.execute(action);
        assertNotNull(result);
        assertTrue((Boolean) result.getValue("ok"));
        assertFalse(result.isSucceeded());
    }
}
