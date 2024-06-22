package io.github.opendme.server.entity;

import java.util.Map;

public class Vehicle {
    Long id;
    String name;
    Integer seats;
    Map<String, Integer> requiredSkills;
}
