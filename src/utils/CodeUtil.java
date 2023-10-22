package utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class CodeUtil{
    public static String getCode(){
        /*该函数的功能：
         * 获得长度为5的验证码，其中数字为3，字母为2
         * 数字在0-9中随机抽取，字母在a-zA-Z间随机抽取
         */
        Random r = new Random();
        ArrayList<Character> code = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            //先抽取数字
            if(i < 3){
                int digit = r.nextInt(10);//左闭右开 [0,10}
                code.add((char)('0' + digit));
            }
            else{
                //再抽字母
                int letter = r.nextInt(52);
                if(letter < 26){
                    //小写字母
                    code.add((char)('a' + letter));
                }
                else{
                    //大写字母
                    code.add((char)('A' + letter - 26));
                }
            }
        }
        //打乱
        Collections.shuffle(code);
        //拼接并输出
        StringBuilder sb = new StringBuilder();
        code.stream().forEach(c->sb.append(c));
        //String reasult = "";
        //for (Character c : code) {
        //    reasult += c;
        //}
        
        return sb.toString();
    }

    public static boolean CodeEquals(String code1, String code2){
        //忽略大小写，直接返回
        return code1.toLowerCase().equals(code2.toLowerCase());
    }

}