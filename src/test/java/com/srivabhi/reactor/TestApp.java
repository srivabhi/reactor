package com.srivabhi.reactor;

import org.junit.jupiter.api.Test;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class TestApp {
    private AtomicInteger cnt = new AtomicInteger(0);
    private List<String> dataSrc =
            Arrays.asList("Abhinav"," is"," a"," developer ","at ","Morgan"," Stanley","He"," likes"," to"," code"," using"," Java"," He"," is"," new"," to ","Reactive ","Java ","and"," trying ","to"," learn"," it");

    private List<String> dataValues =
            Arrays.asList("Abhinav"," is"," a"," developer ","at ","Morgan"," Stanley","He"," likes"," to"," code"," using"," Java"," He"," is"," new"," to ","Reactive ","Java ","and"," trying ","to"," learn"," it");

    @Test
    public void shouldAnswerWithTrue() {
        Flux<Integer> publisher = Flux.range(1, 100);
        publisher.subscribe(System.out::println);
    }

    @Test
    public void one() {
        Flux<UUID> pub = Flux.fromStream(Stream.generate(UUID::randomUUID).limit(10));
        pub.subscribe(id -> {
            System.out.println(cnt.incrementAndGet() + " " + id);
        });
    }

    @Test
    public void two() {
        /*StepVerifier.create(this.dataSrc)
                .expectNextSequence(this.dataValues)
                .expectNextCount()
                .verifyComplete();*/
    }

    @Test
    public void three() {
        Flux<UUID> pub = Flux.fromStream(Stream.generate(UUID::randomUUID).limit(20));
        pub.subscribe(
                id -> {
                    System.out.println(cnt.incrementAndGet() + " " + id);
                    if (cnt.get() == 18) {
                        throw new RuntimeException("EEEE");
                    }
                    ;
                },
                error -> System.out.println("error:" + error.getMessage()),
                () -> {
                    System.out.println("runnable");
                    try {
                        TimeUnit.SECONDS.sleep(10000);
                        System.out.println("DONE");
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    @Test
    public void four(){

        Flux<Integer> numberSeq = Flux.range(1,20)
                .map( e -> {
                   if(e==8) { throw new RuntimeException("error on eights");}
                   return e;
                });

        numberSeq.subscribe(
                e -> System.out.printf("Value received %s%n",e),
                error -> System.err.println("Error Published:: " + error),
                () -> System.out.println("Complete event published")
        );
    }

    @Test
    public void five(){
        Flux<Integer> numberSeq = Flux.range(1,20).delayElements(Duration.ofSeconds(3));
        Disposable cancelRef =
                numberSeq.subscribe( e -> System.out.printf("Value received %s%n",e),
                        error -> System.err.println("Error Published:: " +    error),
                        ()-> System.out.println("Complete event published"));
        Runnable runnableTask = ()->{
        try {
            TimeUnit.SECONDS.sleep(12);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Canceling subscription");
        cancelRef.dispose();
    };
        runnableTask.run();
    }
}
