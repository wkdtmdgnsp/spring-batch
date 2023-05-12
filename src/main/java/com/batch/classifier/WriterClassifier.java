package com.batch.classifier;

import com.batch.domain.ApiRequestV0;
import org.springframework.batch.item.ItemWriter;
import org.springframework.classify.Classifier;

import java.util.HashMap;
import java.util.Map;

public class WriterClassifier<C, T> implements Classifier<C, T> {

    private Map<String, ItemWriter<ApiRequestV0>> writerMap = new HashMap<>();


    @Override
    public T classify(C classifiable) {

        return (T) writerMap.get(((ApiRequestV0)classifiable).getProductV0().getType());
    }

    public void setWriterMap(Map<String, ItemWriter<ApiRequestV0>> writerMap) {
        this.writerMap = writerMap;
    }
}
