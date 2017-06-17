package io.cutoutsy.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class RedisUtil {
    private JedisPool pool = null;
    int jedis_instance_count = 0;

    public RedisUtil(){
        String redis_host = ConfigParas.redis_host;
        int redis_port = Integer.valueOf(ConfigParas.redis_port);
        String redis_auth = ConfigParas.redis_auth;
        int redis_database = Integer.valueOf(ConfigParas.redis_database);
        pool = this.initJedisPool(redis_host, redis_port, redis_auth, redis_database);
    }

    public RedisUtil(String ip, int port, String password, int database){
        pool = this.initJedisPool(ip, port, password, database);
    }

    private JedisPool initJedisPool(String ip, int port, String password, int database) {
        if (pool == null) {
            int max_active = 1000;
            int max_total = 1000;
            int max_idle = 1000;
            int max_wait = 100;
            JedisPoolConfig config = new JedisPoolConfig();
//			config.setMaxActive(max_active);
            config.setMaxWaitMillis(1000 * max_wait);
            config.setMaxIdle(max_idle);
            config.setMaxTotal(max_total);
//			config.setMaxWait(1000 * max_wait);
            config.setTestOnBorrow(true);
            config.setTestOnReturn(true);
            try {
                pool = new JedisPool(config, ip, port, 1000 * 10, password, database);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return pool;
    }
    public JedisPool getJedisPool() {
        return this.pool;
    }
    /* 从JedisPool中得到Jedis实例，并使引用计数加1 */
    public Jedis getJedisInstance() {
        Jedis jedis = pool.getResource();
        jedis_instance_count++;
        if (jedis != null) {
            return jedis;
        }
        return null;
    }
    public void release_jedis(Jedis jedis) {
        this.pool.returnResource(jedis);
        jedis_instance_count--;
    }
    /* 保存set */
    public void save_set(Jedis jedis, String set_name, Set<String> set) {
        Iterator<String> it = set.iterator();
        while (it.hasNext()) {
            String tmpProxy = it.next().toString();
            jedis.sadd(set_name, tmpProxy);
            jedis.hset("check_proxy_pool", tmpProxy, "0");
        }
    }
    /* 删除set */
    public void clean_set(Jedis jedis, String set_name) {
        jedis.del(set_name);
    }
    /* 从set中随机取元素 */
    public String pick(Jedis jedis,String set_name){
        return jedis.spop(set_name);
    }
    /* 保存单个元素到set中 */
    public void add(Jedis jedis,String set_name,String element){
        jedis.sadd(set_name,element);
    }
    /* 查询set中的元素数量 */
    public Long num(Jedis jedis,String set_name){
        return jedis.scard(set_name);
    }

}
