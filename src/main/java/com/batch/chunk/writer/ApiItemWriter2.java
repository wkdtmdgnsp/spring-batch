package com.batch.chunk.writer;

import com.batch.domain.ApiRequestV0;
import com.batch.domain.ApiResponseV0;
import com.batch.service.AbstractApiService;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.core.io.FileSystemResource;

import java.util.List;

public class ApiItemWriter2 extends FlatFileItemWriter<ApiRequestV0> {

    private final AbstractApiService apiService;

    public ApiItemWriter2(AbstractApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public void write(List<? extends ApiRequestV0> items) throws Exception {
        ApiResponseV0 responseV0 = apiService.service(items);
        System.out.println("responseV0 = " + responseV0);

        items.forEach(item -> item.setApiResponseV0(responseV0));

        super.setResource(new FileSystemResource("C:\\study\\spring-batch\\src\\main\\resources\\produce2.txt"));
        super.open(new ExecutionContext());
        super.setLineAggregator(new DelimitedLineAggregator<>());
        super.setAppendAllowed(true);
        super.write(items);
    }
}
