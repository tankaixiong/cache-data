package tank.excel.entity;

import tank.excel.annotation.ExcelEntity;

import java.util.List;
import java.util.Map;

/**
 * @Author: tank
 * @Email: kaixiong.tan@qq.com
 * @Date: 2016/12/30
 * @Version: 1.0
 * @Description:
 */
@ExcelEntity(file = "test.xlsx", sheet = "属性")
public class ExcelEntityDemo {
    protected Integer roleID;
    protected List<Integer[]> interval;

    protected List<Integer[]> attr;

    protected Map<Integer, Integer> test;
    protected List<Integer> test2;
    protected Boolean test3;

    public Integer getRoleID() {
        return roleID;
    }

    public void setRoleID(Integer roleID) {
        this.roleID = roleID;
    }

    public List<Integer[]> getInterval() {
        return interval;
    }

    public void setInterval(List<Integer[]> interval) {
        this.interval = interval;
    }

    public List<Integer[]> getAttr() {
        return attr;
    }

    public void setAttr(List<Integer[]> attr) {
        this.attr = attr;
    }

    public Map<Integer, Integer> getTest() {
        return test;
    }

    public void setTest(Map<Integer, Integer> test) {
        this.test = test;
    }

    public List<Integer> getTest2() {
        return test2;
    }

    public void setTest2(List<Integer> test2) {
        this.test2 = test2;
    }

    public Boolean getTest3() {
        return test3;
    }

    public void setTest3(Boolean test3) {
        this.test3 = test3;
    }
}
