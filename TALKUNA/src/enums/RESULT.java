package enums;

public class RESULT {

    private int id;
    private String desc;

    public RESULT(int id, String desc) {
        this.id = id;
        this.desc = desc;
    }

    public RESULT setId(int id) {
        this.id = id;
        return this;
    }

    public RESULT setDesc(String desc) {
        this.desc = desc;
        return this;
    }

    public int getId() {
        return id;
    }

    public String getDesc() {
        return desc;
    }

    public String toString() {
        return "RESULT [" + "id=" + id + " desc='" + desc + "']";
    }

}
