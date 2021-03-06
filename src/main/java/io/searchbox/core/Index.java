package io.searchbox.core;

import io.searchbox.AbstractAction;
import io.searchbox.Action;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.elasticsearch.action.ActionRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.common.io.stream.BytesStreamOutput;

import java.io.IOException;
import java.util.Map;

/**
 * @author Dogukan Sonmez
 */


public class Index extends AbstractAction implements Action {

    private static Logger log = Logger.getLogger(Index.class.getName());

    public static class Builder {
        private String index = null;
        private String type = null;
        private String id = null;
        private final Object source;

        public Builder(Object source) {
            this.source = source;
        }

        public Builder index(String val) {
            index = val;
            return this;
        }

        public Builder type(String val) {
            type = val;
            return this;
        }

        public Builder id(String val) {
            id = val;
            return this;
        }

        public Index build() {
            return new Index(this);
        }
    }

    private Index(Builder builder) {
        setData(builder.source);
        prepareIndex(builder.index, builder.type, builder.id);
    }

    public Index(ActionRequest request) {
        IndexRequest indexRequest = (IndexRequest) request;
        String indexName = indexRequest.index();
        String type = indexRequest.type();
        setData(indexRequest.source());
        String id = indexRequest.id();
        prepareIndex(indexName, type, id);

    }

    private void prepareIndex(String indexName, String typeName, String id) {
        super.indexName = indexName;
        super.typeName = typeName;
        if (id != null) {
            setRestMethodName("PUT");
        } else {
            setRestMethodName("POST");
        }
        super.id = id;
    }

    // See IndexResponse.readFrom to understand how to create output
    public byte[] createByteResult(Map jsonMap) throws IOException {
        BytesStreamOutput output = new BytesStreamOutput();
        output.writeUTF((String) jsonMap.get("_index"));
        output.writeUTF((String) jsonMap.get("_id"));
        output.writeUTF((String) jsonMap.get("_type"));
        output.writeLong(((Double) jsonMap.get("_version")).longValue());
        output.writeBoolean(false);
        return output.copiedByteArray();
    }


    /* Need to call buildURI method each time to check if new parameter added*/
    @Override
    public String getURI() {
        StringBuilder sb = new StringBuilder();
        sb.append(buildURI(indexName, typeName, id));
        String queryString = buildQueryString();
        if (StringUtils.isNotBlank(queryString)) sb.append(queryString);
        return sb.toString();
    }

    @Override
    public String getName() {
        return "INDEX";
    }

    @Override
    public String getPathToResult() {
        return "ok";
    }
}
