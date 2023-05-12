package com.batch.chunk.processor;

import com.batch.domain.ApiRequestV0;
import com.batch.domain.ProductV0;
import org.springframework.batch.item.ItemProcessor;

public class ApiItemProcessor3 implements ItemProcessor<ProductV0, ApiRequestV0> {

    @Override
    public ApiRequestV0 process(ProductV0 item) throws Exception {
        return ApiRequestV0.builder()
                .id(item.getId())
                .productV0(item)
                .build();
    }
}
