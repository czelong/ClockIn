package cn.iotclub.clockin.domain;

public class Rank {

    String name;
    String portrait;
    long time;

    @Override
    public String toString() {
        return "Rank{" +
                "name='" + name + '\'' +
                ", portrait='" + portrait + '\'' +
                ", time=" + time +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
