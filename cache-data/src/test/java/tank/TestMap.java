package tank;


import org.apache.commons.lang.time.StopWatch;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author: tank
 * @Email: kaixiong.tan@qq.com
 * @Date: 2017/2/23
 * @Version: 1.0
 * @Description:
 */
public class TestMap {

    public static void main(String a[]) {
        StopWatch stopwatch=new StopWatch();

        HashMap hashMap = new HashMap();

        for(int i=0;i<10;i++){
            hashMap.put(i, new Demo(i,"name"+i));
        }


        System.out.println("Original HashMap : " + hashMap);
        stopwatch.start();

        //HashMap hashMap1 = (HashMap) hashMap.clone();
        HashMap hashMap1 = new HashMap(hashMap);

        //Set set=new HashSet(hashMap.keySet());
        //getSet(hashMap);

        stopwatch.stop();

        System.out.println("消耗:"+stopwatch.getTime());

        ((Demo)hashMap1.get(1)).setName("tank");

        System.out.println(hashMap.remove(hashMap1.get(1)));

        System.out.println("Original HashMap : " + hashMap);
        System.out.println("Copied HashMap : " + hashMap1);
    }

    public static Set getSet(HashMap data){

        Set set=new HashSet();
        for(Object obj:data.values()){
            set.add(obj);
        }

        return set;
    }
}
