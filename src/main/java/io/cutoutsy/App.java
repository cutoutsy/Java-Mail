package io.cutoutsy;

import io.cutoutsy.utils.ConfigParas;
import io.cutoutsy.utils.RedisUtil;
import redis.clients.jedis.Jedis;

public class App
{
    public static void main( String[] args )
    {
        RedisUtil ru = new RedisUtil();
        Jedis redis = ru.getJedisInstance();
        while (true){
            String notice = redis.lpop(ConfigParas.mail_db);
            if (notice != null && notice.length() > 0) {
                String toAddress = notice.split("\\$\\$")[0];
                String title = notice.split("\\$\\$")[1];
                String content = notice.split("\\$\\$")[2];
                SimpleMailSender.SendMail(toAddress, title, content);
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
