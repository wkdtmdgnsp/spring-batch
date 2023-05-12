package com.batch.classifier;

import com.batch.domain.ApiRequestV0;
import com.batch.domain.ProductV0;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.classify.Classifier;

import java.util.HashMap;
import java.util.Map;

public class ProcessorClassifier<C, T> implements Classifier<C, T> {

    private Map<String, ItemProcessor<ProductV0, ApiRequestV0>> processorMap = new HashMap<>();


    @Override
    public T classify(C classifiable) {

        return (T)processorMap.get(((ProductV0)classifiable).getType());
    }

    public void setProcessorMap(Map<String, ItemProcessor<ProductV0, ApiRequestV0>> processorMap) {
        this.processorMap = processorMap;
    }
}
