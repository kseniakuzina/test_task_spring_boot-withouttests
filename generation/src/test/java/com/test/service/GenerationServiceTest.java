package com.test.service;

import com.test.entity.GeneratedNumber;
import com.test.repository.NumberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class GenerationServiceTest {
    @Mock
    private NumberRepository numberRepository;

    @InjectMocks
    private GenerationService generationService = new GenerationService();

    @Test
    void ShouldGenerateNumber()
    {

        Mockito.when(numberRepository.save(any(GeneratedNumber.class))).thenReturn(new GeneratedNumber("1234520240101"));

        GeneratedNumber num = generationService.generateNumber();

        Assertions.assertThat(num).isNotNull();
        Assertions.assertThat(num.getNumber().length()).isEqualTo(13);
    }


}