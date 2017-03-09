package tank;

/**
 * @Author: tank
 * @Email: kaixiong.tan@qq.com
 * @Date: 2017/2/23
 * @Version: 1.0
 * @Description:
 */
public class Demo {
    public Demo(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    private Integer id;
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Demo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
