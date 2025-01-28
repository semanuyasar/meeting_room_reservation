package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class MeetingRoomTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static MeetingRoom getMeetingRoomSample1() {
        return new MeetingRoom().id(1L).name("name1").location("location1").capacity(1);
    }

    public static MeetingRoom getMeetingRoomSample2() {
        return new MeetingRoom().id(2L).name("name2").location("location2").capacity(2);
    }

    public static MeetingRoom getMeetingRoomRandomSampleGenerator() {
        return new MeetingRoom()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .location(UUID.randomUUID().toString())
            .capacity(intCount.incrementAndGet());
    }
}
