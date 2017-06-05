package tank.event.entity;

/**
 * @Author: tank
 * @Email: kaixiong.tan@qq.com
 * @Date: 2017/6/5
 * @Version: 1.0
 * @Description:
 */
public class LoginEvent {

    private Long id;
    private String name;

    public LoginEvent(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
