package com.batch.chunk.writer;

import com.batch.domain.ApiRequestV0;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class ApiItemWriter2 implements ItemWriter<ApiRequestV0> {

    @Override
    public void write(List<? extends ApiRequestV0> items) throws Exception {
        
    }
}
