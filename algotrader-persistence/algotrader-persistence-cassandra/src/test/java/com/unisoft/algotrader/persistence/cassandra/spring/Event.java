package com.unisoft.algotrader.persistence.cassandra.spring;

import com.google.common.base.Objects;
import org.springframework.cassandra.core.Ordering;
import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.mapping.Table;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Created by alex on 6/28/15.
 */
@Table
public class Event {

    @PrimaryKeyColumn(name = "id", ordinal = 2, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
    private UUID id;
    @PrimaryKeyColumn(name = "type", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private String type;
    @PrimaryKeyColumn(name = "bucket", ordinal = 1, type = PrimaryKeyType.PARTITIONED)
    private String bucket;
    @Column
    private Set<String> tags = new HashSet();

    public Event(UUID id, String type, String bucket, Set<String> tags) {
        this.id = id;
        this.type = type;
        this.bucket = bucket;
        this.tags.addAll(tags);
    }

    public UUID getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getBucket() {
        return bucket;
    }

    public Set getTags() {
        return tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Event)) return false;
        Event event = (Event) o;
        return Objects.equal(id, event.id) &&
                Objects.equal(type, event.type) &&
                Objects.equal(bucket, event.bucket) &&
                Objects.equal(tags, event.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, type, bucket, tags);
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", bucket='" + bucket + '\'' +
                ", tags=" + tags +
                '}';
    }
}