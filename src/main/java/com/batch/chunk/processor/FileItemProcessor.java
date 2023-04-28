package com.batch.chunk.processor;

import com.batch.domain.Product;
import com.batch.domain.ProductV0;
import org.modelmapper.ModelMapper;
import org.springframework.batch.item.ItemProcessor;

public class FileItemProcessor implements ItemProcessor<ProductV0, Product> {


    @Override
    public Product process(ProductV0 item) throws Exception {

        ModelMapper modelMapper = new ModelMapper();
        Product product = modelMapper.map(item, Product.class);

        return product;
    }
}