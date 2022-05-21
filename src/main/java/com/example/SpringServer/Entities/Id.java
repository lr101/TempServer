package com.example.SpringServer.Entities;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Id {

    @Column(name = "sensor_id", unique = true)
    private String sensorId;

    @Column(name = "sensor_nick", nullable = false)
    private String sensorNick;

    @OneToOne
    @JoinColumn(name = "sensor_type_id")
    private Type sensorType;

    @ManyToMany
    @JoinTable(
            name = "Id_Category",
            joinColumns = { @JoinColumn(name = "id") },
            inverseJoinColumns = { @JoinColumn(name = "category_id") }
    )
    private Set<Category> categories = new HashSet<>();

    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }

    public String getSensorId() {
        return sensorId;
    }

    public String getSensorNick() {
        return sensorNick;
    }

    public void setSensorNick(String sensorNick) {
        this.sensorNick = sensorNick;
    }

    public Type getSensorType() {
        return sensorType;
    }

    public void setSensorType(Type sensorType) {
        this.sensorType = sensorType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> sensorCategory) {
        this.categories = sensorCategory;
    }

    public void addCategory(Category category) {
        this.categories.add(category);
    }
}
