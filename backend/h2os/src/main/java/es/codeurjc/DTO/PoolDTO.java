package es.codeurjc.DTO;

import es.codeurjc.model.Pool;

public class PoolDTO {

    private Long id;
    private String name;
    private String direction;
    private int capacity;
    private String scheduleStart;
    private String scheduleEnd;
    private String company;
    private String description;

    public PoolDTO() {
    }

    public PoolDTO(Pool pool) {
        this.id = pool.getId();
        this.name = pool.getName();
        this.direction = pool.getDirection();
        this.capacity = pool.getCapacity();
        this.scheduleStart = pool.getScheduleStart().toString();
        this.scheduleEnd = pool.getScheduleEnd().toString();
        this.company = pool.getCompany();
        this.description = pool.getDescription();
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }


    public String getDirection() {
        return direction;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getScheduleStart() {
        return scheduleStart;
    }

    public String getScheduleEnd() {
        return scheduleEnd;
    }

    public String getCompany() {
        return company;
    }

    public String getDescription() {
        return description;
    }
}
