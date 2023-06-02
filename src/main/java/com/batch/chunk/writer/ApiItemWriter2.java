package com.batch.chunk.writer;

import com.batch.domain.ApiRequestV0;
import com.batch.domain.ApiResponseV0;
import com.batch.service.AbstractApiService;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class ApiItemWriter2 implements ItemWriter<ApiRequestV0> {

    private final AbstractApiService apiService;

    public ApiItemWriter2(AbstractApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public void write(List<? extends ApiRequestV0> items) throws Exception {
        ApiResponseV0 responseV0 = apiService.service(items);
        System.out.println("responseV0 = " + responseV0);
    }
}
