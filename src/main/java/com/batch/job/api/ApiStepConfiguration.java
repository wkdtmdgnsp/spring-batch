package com.batch.job.api;

import com.batch.chunk.processor.ApiItemProcessor1;
import com.batch.chunk.processor.ApiItemProcessor2;
import com.batch.chunk.processor.ApiItemProcessor3;
import com.batch.chunk.writer.ApiItemWriter1;
import com.batch.chunk.writer.ApiItemWriter2;
import com.batch.chunk.writer.ApiItemWriter3;
import com.batch.classifier.ProcessorClassifier;
import com.batch.classifier.WriterClassifier;
import com.batch.domain.ApiRequestV0;
import com.batch.domain.ProductV0;
import com.batch.partiition.ProductPartitioner;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.OraclePagingQueryProvider;
import org.springframework.batch.item.support.ClassifierCompositeItemProcessor;
import org.springframework.batch.item.support.ClassifierCompositeItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class ApiStepConfiguration {

    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;

    private int chunkSize = 10;

    @Bean
    public Step apiMasterStep() throws Exception {
        return stepBuilderFactory.get("apiMasterStep")
                .partitioner(apiSlaveStep().getName(), partitioner())
                .step(apiSlaveStep())
                .gridSize(3)
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(3);
        taskExecutor.setMaxPoolSize(6);
        taskExecutor.setThreadNamePrefix("api-thread-");

        return taskExecutor;
    }

    @Bean
    public Step apiSlaveStep() throws Exception {
        return stepBuilderFactory.get("apiSlaveStep")
                .<ProductV0, ProductV0>chunk(chunkSize)
                .reader(itemReader(null))
                .processor(itemProcessor())
                .writer(itemWriter())
                .build();
    }

    @Bean
    public ProductPartitioner partitioner() {
        ProductPartitioner productPartitioner = new ProductPartitioner();
        productPartitioner.setDataSource(dataSource);
        return productPartitioner;
    }

    @StepScope
    public ItemReader<ProductV0> itemReader(@Value("#{stepExecutionContext['product']}") ProductV0 productV0) throws Exception {

        JdbcPagingItemReader<ProductV0> reader = new JdbcPagingItemReader<>();

        reader.setDataSource(dataSource);
        reader.setPageSize(chunkSize);
        reader.setRowMapper(new BeanPropertyRowMapper<>(ProductV0.class));

        OraclePagingQueryProvider queryProvider = new OraclePagingQueryProvider();
        queryProvider.setSelectClause("id, name, price, type");
        queryProvider.setFromClause("from product");
        queryProvider.setWhereClause("where type = :type");

        Map<String, Order> sortKeys = new HashMap<>(1);
        sortKeys.put("id", Order.DESCENDING);
        queryProvider.setSortKeys(sortKeys);

        reader.setParameterValues(QueryGenerator.getParameterForQuery("type", productV0.getType()));
        reader.setQueryProvider(queryProvider);
        reader.afterPropertiesSet();

        return reader;
    }

    @Bean
    public ItemProcessor itemProcessor() {
        ClassifierCompositeItemProcessor<ProductV0, ApiRequestV0> processor = new ClassifierCompositeItemProcessor<ProductV0, ApiRequestV0>();
        ProcessorClassifier<ProductV0, ItemProcessor<?, ? extends ApiRequestV0>> classifier = new ProcessorClassifier();
        Map<String, ItemProcessor<ProductV0, ApiRequestV0>> processorMap = new HashMap<>();
        processorMap.put("1", new ApiItemProcessor1());
        processorMap.put("2", new ApiItemProcessor2());
        processorMap.put("3", new ApiItemProcessor3());

        classifier.setProcessorMap(processorMap);

        processor.setClassifier(classifier);

        return processor;
    }

    @Bean
    public ItemWriter itemWriter() {
        ClassifierCompositeItemWriter<ApiRequestV0> writer = new ClassifierCompositeItemWriter<>();
        WriterClassifier<ApiRequestV0, ItemWriter<? super ApiRequestV0>> classifier = new WriterClassifier();
        Map<String, ItemWriter<ApiRequestV0>> writerMap = new HashMap<>();
        writerMap.put("1", new ApiItemWriter1());
        writerMap.put("2", new ApiItemWriter2());
        writerMap.put("3", new ApiItemWriter3());

        classifier.setWriterMap(writerMap);

        writer.setClassifier(classifier);

        return writer;
    }
}
