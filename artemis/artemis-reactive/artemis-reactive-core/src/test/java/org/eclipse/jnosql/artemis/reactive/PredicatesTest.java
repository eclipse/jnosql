package org.eclipse.jnosql.artemis.reactive;

import jakarta.nosql.NonUniqueResultException;
import org.eclipse.microprofile.reactive.streams.operators.CompletionSubscriber;
import org.eclipse.microprofile.reactive.streams.operators.ReactiveStreams;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

public class PredicatesTest {


    @Test
    public void shouldReturnMono() throws ExecutionException, InterruptedException {
        Publisher<String> predicate = ReactiveStreams.fromIterable(Collections.singleton("Banana")).buildRs();
        final CompletionSubscriber<String, List<String>> subscriber = ReactiveStreams.<String>builder().limit(2).toList().build();
        predicate.subscribe(subscriber);
        final CompletionStage<List<String>> completion = subscriber.getCompletion();
        final CompletionStage<Optional<String>> stage = completion.thenApply(singleResult());
        final CompletableFuture<String> future = stage.thenApply(Optional::get).toCompletableFuture();
        final String text = future.get();
        System.out.println("text " + text);

    }

    @Test
    public void shouldReturnEmpty() throws ExecutionException, InterruptedException {
        Publisher<String> predicate = ReactiveStreams.fromIterable(Collections.<String>emptyList()).buildRs();
        final CompletionSubscriber<String, List<String>> subscriber = ReactiveStreams.<String>builder().limit(2).toList().build();
        predicate.subscribe(subscriber);
        final CompletionStage<List<String>> completion = subscriber.getCompletion();
        final CompletionStage<Optional<String>> stage = completion.thenApply(singleResult());
        final CompletableFuture<Optional<String>> future = stage.toCompletableFuture();
        final Optional<String> value = future.get();
        Assertions.assertFalse(value.isPresent());
    }

    @Test
    public void shouldReturnErrorWhenThereIsTwoElements() throws ExecutionException, InterruptedException {
        Publisher<String> predicate = ReactiveStreams.fromIterable(Arrays.asList("Banana", "Apple")).buildRs();
        final CompletionSubscriber<String, List<String>> subscriber = ReactiveStreams.<String>builder().limit(2).toList().build();
        predicate.subscribe(subscriber);
        final CompletionStage<List<String>> completion = subscriber.getCompletion();
        final CompletionStage<Optional<String>> stage = completion.thenApply(singleResult());
        final CompletableFuture<Optional<String>> future = stage.toCompletableFuture();
        final Optional<String> value = future.get();
        Assertions.assertFalse(value.isPresent());
    }

    private <U> Function<List<U>, Optional<U>> singleResult() {
        return entities -> {
            if (entities.size() == 0) {
                return Optional.empty();
            } else if (entities.size() == 1) {
                return Optional.of(entities.get(0));
            }
            throw new NonUniqueResultException("There is more than one element found in this query");
        };
    }
}
