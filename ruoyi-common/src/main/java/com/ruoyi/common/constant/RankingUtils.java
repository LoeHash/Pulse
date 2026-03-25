package com.ruoyi.common.constant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RankingUtils {

    public static List<String> SKILL_POOL = Arrays.asList(
            "Java",
            "Python",
            "C++",
            "Go",
            "JavaScript",
            "TypeScript",
            "Spring",
            "Spring Boot",
            "Spring Cloud",
            "MyBatis",
            "MyBatis-Plus",
            "JPA",
            "Hibernate",
            "Dubbo",
            "Netty",
            "MySQL",
            "PostgreSQL",
            "MongoDB",
            "Redis",
            "ElasticSearch",
            "SQL优化",
            "索引设计",
            "事务",
            "锁机制",
            "分库分表",
            "读写分离",
            "缓存一致性",
            "前端",
            "HTML",
            "CSS",
            "Vue",
            "React",
            "Node.js",
            "Express",
            "Next.js",
            "Nuxt",
            "Webpack",
            "Vite",
            "小程序",
            "算法",
            "数据结构",
            "数据分析",
            "数据挖掘",
            "机器学习",
            "深度学习",
            "人工智能",
            "自然语言处理",
            "计算机视觉",
            "推荐系统",
            "强化学习",
            "特征工程",
            "模型训练",
            "模型评估",
            "PyTorch",
            "TensorFlow",
            "Sklearn",
            "Keras",
            "Docker",
            "Kubernetes",
            "K8s",
            "Linux",
            "Linux内核",
            "Shell",
            "操作系统",
            "操作系统原理",
            "计算机组成原理",
            "网络",
            "网络协议",
            "TCP/IP",
            "HTTP",
            "HTTPS",
            "微服务",
            "分布式",
            "高并发",
            "并发编程",
            "系统设计",
            "RESTful",
            "API设计",
            "消息队列",
            "Kafka",
            "RabbitMQ",
            "Nacos",
            "Sentinel",
            "Gateway",
            "RPC",
            "日志分析",
            "问题排查",
            "内存泄漏",
            "性能优化",
            "压力测试",
            "JMeter",
            "CI/CD",
            "Jenkins",
            "DevOps",
            "Prometheus",
            "Grafana",
            "项目管理",
            "需求分析",
            "技术选型",
            "文档编写",
            "代码规范",
            "重构",
            "竞赛经验",
            "量子计算",
            "编程",
            "量子",
            "后端",
            "系统",
            "元宇宙",
            "数字创意",
            "VR/AR",
            "虚拟设计",
            "创业",
            "创新",
            "新能源",
            "医疗AI",
            "智慧医疗",
            "医学影像",
            "健康科技",
            "自动驾驶",
            "汽车算法",
            "金融科技",
            "区块链",
            "智能金融",
            "数字支付",
            "开发"
    );


    public static Integer VECTOR_SIZE;

    public static Map<String, Integer> TAGS_INDEX_MAP = new ConcurrentHashMap<>();
    public static List<Integer> ZERO_LIST_TEMPLATE = new ArrayList<>();


    static{
        for (int i = 0; i < SKILL_POOL.size(); i++) {
            TAGS_INDEX_MAP.put(SKILL_POOL.get(i),i);
        }

        ArrayList<Integer> tmp = new ArrayList<>(SKILL_POOL.size());
        for (int i = 0; i < SKILL_POOL.size(); i++) {
            tmp.add(0);
        }
        ZERO_LIST_TEMPLATE = tmp;

        VECTOR_SIZE = SKILL_POOL.size();
    }




    public static byte[] tagsToBitmap(List<String> tags) {
        // 16字节  可存储128个技能
        byte[] bitMap = new byte[16];
        System.out.println(SKILL_POOL.size());
        for (String tag : tags) {
            Integer indexObj = TAGS_INDEX_MAP.get(tag);
            if (indexObj == null) {
                continue; // 标签不在池中，跳过
            }

            int index = indexObj;

            // 计算在哪个long（第0或第1个）
            int longIndex = index / 64;

            // 计算在long中的哪一位
            int bitInLong = index % 64;

            // 计算在byte数组中的位置
            int byteIndex = longIndex * 8 + (bitInLong / 8);

            // 计算在byte中的哪一位
            int bitInByte = bitInLong % 8;

            // 设置位
            int byteValue = bitMap[byteIndex] & 0xFF;
            byteValue |= (1 << (7 - bitInByte));
            bitMap[byteIndex] = (byte) byteValue;
        }

        return bitMap;
    }


    public static List<Integer> getTagsVector(List<String> tags){
        List<Integer> vector = new ArrayList<>(ZERO_LIST_TEMPLATE);

        for (String tag : tags) {
            Integer i = TAGS_INDEX_MAP.get(tag);

            if (i != null){
                vector.set(i, 1);
            }
        }
        return vector;
    }


    /**
     * 获取两个二进制向量的欧式距离
     * @return
     */
    public static int getVectorEupDistanceInBin(byte[] v1, byte[]v2){
        //将两个向量异或
        for (int i = 0; i < v1.length; i++) {
            v1[i] ^= v2[i];
        }
        //直接计算
        return bitCount(v1);
    }

    /**
     * 获取两个向量的欧式距离
     * @return
     */
    public static int getVectorEupDistance(List<Integer> v1, List<Integer> v2){
        //无须开根号
        int distance = 0;

        //逻辑与短路
        if (v1.size() != v2.size() || v1.size() != SKILL_POOL.size()){
            throw new RuntimeException("向量错误！");
        }

        for (int i = 0; i < VECTOR_SIZE; i++) {

            Integer sub = v1.get(i) - v2.get(i);
            distance += sub * sub;
        }

        return distance;
    }

    private static int bitCount(byte[] tags){

        byte[] copied = Arrays.copyOf(tags, tags.length);

        int count = 0;
        for (int i = 0; i < copied.length; i++) {
            byte b = copied[i];

            while (b != 0) {
                b &= (byte) (b - 1);
                count += 1;
            }
        }
        return count;
    }





}
