package pl.rkalaska.resourceloader.entity;

import javax.persistence.*;
import javax.sql.rowset.serial.SerialBlob;
import java.util.Date;
import java.util.Objects;

@Table(name="RESOURCE")
@Entity

public class ResourceDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="location")
    private String url;

    @Column(name="content")
    private SerialBlob content;

    @Column(name="createdOn")
    private Date timestamp;


    @PrePersist
    protected void onCreate() {
        timestamp = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SerialBlob getContent() {
        return content;
    }

    public void setContent(SerialBlob content) {
        this.content = content;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResourceDTO that = (ResourceDTO) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(url, that.url) &&
                Objects.equals(content, that.content) &&
                Objects.equals(timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, url, content, timestamp);
    }

    @Override
    public String toString() {
        return "ResourceDTO{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", content=" + content +
                ", timestamp=" + timestamp +
                '}';
    }
}
