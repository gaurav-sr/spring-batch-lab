package com.codei.hellobatch.configuration;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class JobConfiguration {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Step step1() {
        //tasklet based steps
        return stepBuilderFactory.get("step1").tasklet((stepContribution, chunkContext) -> {
            System.out.println("Executing Step1");
            return RepeatStatus.FINISHED;
        }).build();
    }

    @Bean
    public Step step2() {
        return stepBuilderFactory.get("step2").tasklet((stepContribution, chunkContext) -> {
            System.out.println("Executing Step2");
            return RepeatStatus.FINISHED;
        }).build();
    }

    @Bean
    public Step step3() {
        return stepBuilderFactory.get("step3").tasklet((stepContribution, chunkContext) -> {
            System.out.println("Executing Step3");
            return RepeatStatus.FINISHED;
        }).build();
    }

    @Bean
    public Job helloBatch() {
        return jobBuilderFactory.get("helloBatch")
                .start(step1())
                .next(step3())
                .next(step2())
                .build();
    }

    @Bean
    public Job transitionJobs() {
        return jobBuilderFactory.get("transitionJob")
                .start(step1())
                .on("COMPLETED").to(step2())
                .from(step2()).on("COMPLETED").to(step3())
                .from(step3()).end()
                .build();
    }

    @Bean
    public Job transitionJobsToFail() {
        return jobBuilderFactory.get("transitionJob")
                .start(step1())
                .on("COMPLETED").to(step2())
                .from(step2()).on("COMPLETED").fail()
                .from(step3()).end()
                .build();
    }
}
