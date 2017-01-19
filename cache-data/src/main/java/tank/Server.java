package tank;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @Author: tank
 * @Email: kaixiong.tan@qq.com
 * @Date: 2017/1/14
 * @Version: 1.0
 * @Description:
 */
public class Server {

    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("context-hibernate.xml");

    }
}
