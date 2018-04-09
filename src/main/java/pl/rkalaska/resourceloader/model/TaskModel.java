package pl.rkalaska.resourceloader.model;

import java.io.Serializable;
import java.util.Objects;

public class TaskModel implements Serializable {

    private static final long serialVersionUID = 3943829063384102809L;
    private String url;
    private int retryCnt = 0;

    public TaskModel(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getRetryCnt() {
        return retryCnt;
    }

    public void setRetryCnt(int retryCnt) {
        this.retryCnt = retryCnt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskModel taskModel = (TaskModel) o;
        return retryCnt == taskModel.retryCnt &&
                Objects.equals(url, taskModel.url);
    }

    @Override
    public int hashCode() {

        return Objects.hash(url, retryCnt);
    }

    @Override
    public String toString() {
        return "TaskModel{" +
                "url='" + url + '\'' +
                ", retryCnt=" + retryCnt +
                '}';
    }
}
