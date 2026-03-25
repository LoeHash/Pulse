-- 先删表（注意：会清空数据）
DROP TABLE IF EXISTS tb_user;

-- 再建表
CREATE TABLE tb_user (
                         id            BIGINT       NOT NULL AUTO_INCREMENT COMMENT '用户ID',
                         username      VARCHAR(64)  NOT NULL COMMENT '账号',
                         password      VARCHAR(128) NOT NULL COMMENT '登录密码（加密）',
                         nickname      VARCHAR(64)  DEFAULT NULL COMMENT '昵称',
                         gender        TINYINT      DEFAULT 0 COMMENT '性别（0未知 1男 2女）',

                         age           INT          DEFAULT NULL COMMENT '年龄',
                         height_cm     INT          DEFAULT NULL COMMENT '身高（cm）',
                         weight_kg     DECIMAL(5,2) DEFAULT NULL COMMENT '体重（kg）',

                         signature     VARCHAR(255) DEFAULT NULL COMMENT '个性签名',

                         goal_info     VARCHAR(4096) DEFAULT NULL COMMENT '目标信息（JSON数组字符串）',

                         avatar        VARCHAR(255) DEFAULT NULL COMMENT '头像URL',
                         role_id       INT          DEFAULT NULL COMMENT '角色ID',

                         status        TINYINT      DEFAULT 1 COMMENT '状态（1正常 0禁用）',
                         is_deleted    TINYINT      DEFAULT 0 COMMENT '删除标志（0正常 1删除）',

                         create_time   DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                         update_time   DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

                         PRIMARY KEY (id),
                         UNIQUE KEY uk_user_username (username),
                         KEY idx_user_status (status),
                         KEY idx_user_deleted (is_deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';





-- 先删表（注意：会清空数据）
DROP TABLE IF EXISTS tb_course;

-- 创建课程表
CREATE TABLE tb_course (
    -- 主键
                           id BIGINT NOT NULL AUTO_INCREMENT COMMENT '课程ID',

    -- 基本信息
                           title VARCHAR(128) NOT NULL COMMENT '课程标题',
                           cover_url VARCHAR(255) DEFAULT NULL COMMENT '封面URL',
                           video_url VARCHAR(255) DEFAULT NULL COMMENT '视频URL/外链',

    -- 多值字段：varchar 存 JSON 数组字符串
                           category VARCHAR(4096) DEFAULT NULL COMMENT '分类（JSON数组字符串，如["徒手","拉伸"]）',

    -- 与用户 goal_info 同一套体系：用于推荐匹配（如["减脂","塑形"]）
                           goal_tags VARCHAR(4096) DEFAULT NULL COMMENT '目标标签（JSON数组字符串，用于推荐匹配）',

    -- 课程属性
                           difficulty TINYINT DEFAULT 1 COMMENT '难度（1入门 2初级 3中级 4高级）',
                           duration_sec INT DEFAULT 0 COMMENT '课程时长（秒）',
                           calories INT DEFAULT NULL COMMENT '消耗热量估算（kcal，可选）',
                           intro VARCHAR(500) DEFAULT NULL COMMENT '课程简介',

    -- 创建者信息
                           create_user_id BIGINT DEFAULT NULL COMMENT '创建者用户ID，关联tb_user.id，NULL表示系统创建',

    -- 状态字段
                           status TINYINT DEFAULT 1 COMMENT '状态（1上架 0下架）',
                           is_deleted TINYINT DEFAULT 0 COMMENT '删除标志（0正常 1删除）',

    -- 时间字段
                           create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                           update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    -- 主键
                           PRIMARY KEY (id),

    -- 索引
                           INDEX idx_create_user_id (create_user_id) COMMENT '创建者用户ID索引',
                           INDEX idx_status (status) COMMENT '状态索引',
                           INDEX idx_difficulty (difficulty) COMMENT '难度索引',
                           INDEX idx_is_deleted (is_deleted) COMMENT '删除标志索引'

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='课程表';

INSERT INTO tb_course
(title, cover_url, video_url, category, goal_tags, difficulty, duration_sec, calories, intro, status, is_deleted)
VALUES
    ('7分钟燃脂入门', NULL, NULL, '["徒手","HIIT"]', '["减脂"]', 1, 420, 60, '适合新手的短时燃脂训练。', 1, 0),
    ('全身拉伸放松（睡前）', NULL, NULL, '["拉伸","放松"]', '["恢复","改善体态"]', 1, 600, 30, '缓解紧张，改善睡眠质量。', 1, 0),
    ('核心训练入门（无器械）', NULL, NULL, '["徒手","核心"]', '["塑形","核心训练"]', 2, 900, 90, '加强核心稳定，改善体态。', 1, 0),
    ('臀腿塑形进阶', NULL, NULL, '["徒手","力量"]', '["塑形","增肌"]', 3, 1200, 140, '针对臀腿的力量与塑形。', 1, 0),
    ('30分钟有氧燃脂', NULL, NULL, '["有氧"]', '["减脂","提升体能"]', 2, 1800, 220, '中等强度有氧，提升心肺。', 1, 0),
    ('肩颈放松拉伸', NULL, NULL, '["拉伸","肩颈"]', '["恢复","肩颈放松"]', 1, 480, 25, '久坐人群肩颈舒缓。', 1, 0),
    ('跑步前热身（10分钟）', NULL, NULL, '["跑步","热身"]', '["提升体能","防受伤"]', 1, 600, 40, '跑前激活，减少受伤风险。', 1, 0),
    ('跑步后拉伸（12分钟）', NULL, NULL, '["跑步","拉伸"]', '["恢复","防受伤"]', 1, 720, 35, '跑后放松，加速恢复。', 1, 0),
    ('上肢力量（哑铃可选）', NULL, NULL, '["力量","上肢"]', '["增肌","塑形"]', 3, 1500, 180, '提升上肢力量与线条。', 1, 0),
    ('腹肌撕裂者（强化）', NULL, NULL, '["核心","HIIT"]', '["塑形","核心训练","减脂"]', 4, 900, 130, '高强度腹部训练，注意量力而行。', 1, 0),
    ('瑜伽基础流（舒缓）', NULL, NULL, '["瑜伽","柔韧"]', '["恢复","改善体态"]', 1, 1500, 90, '舒缓基础流，提升柔韧。', 1, 0),
    ('全身激活（晨起）', NULL, NULL, '["徒手","热身"]', '["提升体能","改善体态"]', 1, 600, 45, '晨起激活身体状态。', 1, 0);


DROP TABLE IF EXISTS tb_course_participation;

CREATE TABLE tb_course_participation (
                                         id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                         course_id BIGINT NOT NULL COMMENT '课程ID',
                                         user_id BIGINT NOT NULL COMMENT '用户ID',
                                         status TINYINT DEFAULT 1 COMMENT '状态（1有效 0取消）',
                                         create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                         update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                         PRIMARY KEY (id),
                                         UNIQUE KEY uk_course_user (course_id, user_id),
                                         KEY idx_user_id (user_id),
                                         KEY idx_course_id (course_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程参与关系表';


DROP TABLE IF EXISTS tb_course_favorite;

CREATE TABLE tb_course_favorite (
                                    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                    course_id BIGINT NOT NULL COMMENT '课程ID',
                                    user_id BIGINT NOT NULL COMMENT '用户ID',
                                    status TINYINT DEFAULT 1 COMMENT '状态（1有效 0取消）',
                                    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                    PRIMARY KEY (id),
                                    UNIQUE KEY uk_course_favorite_user (course_id, user_id),
                                    KEY idx_course_favorite_user_id (user_id),
                                    KEY idx_course_favorite_course_id (course_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程收藏关系表';


DROP TABLE IF EXISTS tb_course_like;

CREATE TABLE tb_course_like (
                                id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                course_id BIGINT NOT NULL COMMENT '课程ID',
                                user_id BIGINT NOT NULL COMMENT '用户ID',
                                status TINYINT DEFAULT 1 COMMENT '状态（1有效 0取消）',
                                create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                PRIMARY KEY (id),
                                UNIQUE KEY uk_course_like_user (course_id, user_id),
                                KEY idx_course_like_user_id (user_id),
                                KEY idx_course_like_course_id (course_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程点赞关系表';


-- 先删表（注意：会清空数据）
DROP TABLE IF EXISTS tb_workout_record;

-- 再建表（支持开始/结束与自动收口）
CREATE TABLE tb_workout_record (
                                   id            BIGINT      NOT NULL AUTO_INCREMENT COMMENT '记录ID',

                                   user_id       BIGINT      NOT NULL COMMENT '用户ID',
                                   course_id     BIGINT      NOT NULL COMMENT '课程ID',

                                   start_time    DATETIME    DEFAULT NULL COMMENT '开始时间',
                                   end_time      DATETIME    DEFAULT NULL COMMENT '结束时间',

                                   duration_sec  INT         DEFAULT NULL COMMENT '本次训练时长（秒）',
                                   calories      INT         DEFAULT NULL COMMENT '本次消耗热量（kcal，可选）',

                                   completed     TINYINT     DEFAULT 0 COMMENT '是否完成（1完成 0中断）',

                                   auto_closed   TINYINT     DEFAULT 0 COMMENT '是否自动结束（1是 0否）',
                                   close_reason  VARCHAR(32) DEFAULT 'manual' COMMENT '结束原因（user/manual/system_timeout）',

                                   note          VARCHAR(255)  DEFAULT NULL COMMENT '备注（训练笔记，可选）',

    -- 扩展字段：后续你可以塞 AI 分析、步频、左右平衡等
                                   metrics       VARCHAR(4096) DEFAULT NULL COMMENT '训练指标扩展（JSON字符串，可选）',

                                   is_deleted    TINYINT     DEFAULT 0 COMMENT '删除标志（0正常 1删除）',
                                   create_time   DATETIME    DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                   update_time   DATETIME    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

                                   PRIMARY KEY (id),
                                   KEY idx_wr_user_id (user_id),
                                   KEY idx_wr_course_id (course_id),
                                   KEY idx_wr_end_time (end_time),
                                   KEY idx_wr_start_time (start_time),
                                   KEY idx_wr_deleted (is_deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='训练记录表';

DROP TABLE IF EXISTS `tb_notice`;

CREATE TABLE `tb_notice` (
                             `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                             `title` varchar(200) NOT NULL COMMENT '标题',
                             `content` text COMMENT '内容（支持富文本HTML）',
                             `type` varchar(50) NOT NULL COMMENT '类型',
                             `sender_id` bigint DEFAULT NULL COMMENT '发送者ID',
                             `extra` text COMMENT '扩展数据，JSON格式',
                             `status` varchar(20) DEFAULT 'NORMAL' COMMENT '状态',
                             `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                             `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                             `is_deleted` tinyint(1) DEFAULT '0',
                             PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='通知表';

DROP TABLE IF EXISTS `tb_notice_receiver`;

CREATE TABLE `tb_notice_receiver` (
                                      `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                      `notice_id` bigint NOT NULL COMMENT '通知ID',
                                      `user_id` bigint NOT NULL COMMENT '用户ID',
                                      `is_read` tinyint(1) DEFAULT '0',
                                      `read_time` datetime DEFAULT NULL COMMENT '阅读时间',
                                      `is_deleted` tinyint(1) DEFAULT '0',
                                      `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                      `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                      PRIMARY KEY (`id`),
                                      KEY `idx_notice` (`notice_id`),
                                      KEY `idx_user` (`user_id`,`is_read`)
) ENGINE=InnoDB COMMENT='接收记录表';


-- 动作模板表（动作库）
DROP TABLE IF EXISTS tb_action;

CREATE TABLE tb_action (
                           id                  BIGINT        NOT NULL AUTO_INCREMENT COMMENT '动作ID',

                           name                VARCHAR(128)  NOT NULL COMMENT '动作名称（如：深蹲、开合跳、休息）',
                           action_type         VARCHAR(32)   NOT NULL COMMENT '动作类型（EXERCISE/REST/OTHER）',
                           unit_type           VARCHAR(16)   NOT NULL COMMENT '单位类型（REPS/SECONDS）',
                           default_value       INT           DEFAULT NULL COMMENT '默认目标值（次数或秒数）',

                           cover_url           VARCHAR(255)  DEFAULT NULL COMMENT '封面URL',
                           video_url           VARCHAR(255)  DEFAULT NULL COMMENT '教程视频URL（REST可为空）',
                           video_duration_sec  INT           DEFAULT NULL COMMENT '教程视频时长（秒）',

                           tips                TEXT          DEFAULT NULL COMMENT '动作提示/要点（富文本）',
                           intro               VARCHAR(500)  DEFAULT NULL COMMENT '动作简介（列表摘要）',

                           create_user_id      BIGINT        DEFAULT NULL COMMENT '创建者用户ID（NULL=系统动作）',

                           category            VARCHAR(4096) DEFAULT NULL COMMENT '动作分类（JSON数组字符串，如["下肢","力量"]）',
                           goal_tags           VARCHAR(4096) DEFAULT NULL COMMENT '目标标签（JSON数组字符串，如["减脂","增肌"]）',
                           tags                VARCHAR(4096) DEFAULT NULL COMMENT '通用标签（JSON数组字符串，如["新手友好","无器械"]）',

                           status              TINYINT       DEFAULT 1 COMMENT '状态（1启用 0禁用）',
                           is_deleted          TINYINT       DEFAULT 0 COMMENT '删除标志（0正常 1删除）',

                           create_time         DATETIME      DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                           update_time         DATETIME      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

                           PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='动作模板表';

# 必须存在的基础动作：休息
INSERT INTO tb_action
(name, action_type, unit_type, default_value, cover_url, video_url, video_duration_sec,
 tips, intro, create_user_id, category, tags, goal_tags, status, is_deleted)
VALUES
    (
        '休息',
        'REST',
        'SECONDS',
        30,
        NULL,
        NULL,
        NULL,
        '<p>休息用于恢复体力与心率。可根据自身情况延长或缩短。</p>',
        '训练间歇休息',
        NULL,
        '["恢复","间歇"]',
        '["新手友好"]',
        '["恢复"]',
        1,
        0
    );

# 基础的几个动作
-- 插入基础动作数据

INSERT INTO tb_action
    (name,
     action_type,
     unit_type,
     default_value,
     cover_url,
     video_url,
     video_duration_sec,
     tips,
     intro,
     create_user_id,
     category,
     goal_tags,
     tags,
     status,
     is_deleted
    ) VALUES
          ('标准俯卧撑', 'EXERCISE', 'REPS', 15, 'https://example.com/pushup.jpg', 'https://example.com/pushup.mp4', 15, '<p>保持身体呈一条直线，核心收紧，手肘微内收。下落吸气，推起呼气。</p>', '经典的胸部训练动作', NULL, '["上肢","胸部"]', '["增肌","塑形"]', '["徒手","新手友好"]', 1, 0),

          ('自重深蹲', 'EXERCISE', 'REPS', 20, 'https://example.com/squat.jpg', 'https://example.com/squat.mp4', 20, '<p>双脚与肩同宽，背部挺直。下蹲如同坐椅子，膝盖尽量不要超过脚尖，重心在脚后跟。</p>', '下肢力量训练基础', NULL, '["下肢","臀腿"]', '["增肌","塑形"]', '["徒手","基础"]', 1, 0),

          ('平板支撑', 'EXERCISE', 'SECONDS', 45, 'https://example.com/plank.jpg', 'https://example.com/plank.mp4', 30, '<p>肘关节支撑地面，身体保持一条直线，不要塌腰，不要耸肩。</p>', '核心稳定性训练', NULL, '["核心","腹部"]', '["塑形","核心训练"]', '["徒手","静力"]', 1, 0),

          ('开合跳', 'EXERCISE', 'SECONDS', 30, 'https://example.com/jumpingjack.jpg', 'https://example.com/jumpingjack.mp4', 15, '<p>双脚开合跳跃，双手举过头顶，保持节奏。落地轻盈，减少关节冲击。</p>', '全身有氧燃脂', NULL, '["全身","有氧"]', '["减脂","热身"]', '["徒手","HIIT"]', 1, 0),

          ('卷腹', 'EXERCISE', 'REPS', 20, 'https://example.com/crunch.jpg', 'https://example.com/crunch.mp4', 15, '<p>双手护头但不要用力抱头，利用腹部力量卷起上半身，下背部紧贴地面。</p>', '针对腹直肌的训练', NULL, '["核心","腹部"]', '["塑形","核心训练"]', '["徒手","新手友好"]', 1, 0),

          ('交替弓步蹲', 'EXERCISE', 'REPS', 16, 'https://example.com/lunge.jpg', 'https://example.com/lunge.mp4', 20, '<p>向前迈出一步下蹲，前后腿膝盖呈90度，交替进行。保持上半身挺直。</p>', '强化腿部与臀部线条', NULL, '["下肢","臀腿"]', '["塑形","增肌"]', '["徒手","单侧训练"]', 1, 0),

          ('登山跑', 'EXERCISE', 'SECONDS', 30, 'https://example.com/mountainclimber.jpg', 'https://example.com/mountainclimber.mp4', 20, '<p>俯卧撑姿势，双腿交替向胸前折叠，保持背部平直，速度越快强度越大。</p>', '高强度核心与有氧结合', NULL, '["核心","有氧"]', '["减脂","核心训练"]', '["徒手","HIIT"]', 1, 0),

          ('波比跳', 'EXERCISE', 'REPS', 10, 'https://example.com/burpee.jpg', 'https://example.com/burpee.mp4', 25, '<p>结合深蹲、俯卧撑和跳跃的复合动作。动作连贯，燃脂效果极佳。</p>', '全身燃脂杀手', NULL, '["全身","HIIT"]', '["减脂","提升体能"]', '["徒手","进阶"]', 1, 0),

          ('靠墙静蹲', 'EXERCISE', 'SECONDS', 45, 'https://example.com/wallsit.jpg', 'https://example.com/wallsit.mp4', 15, '<p>背部紧贴墙面，大小腿呈90度，坚持住。感受大腿前侧发力。</p>', '增强膝盖稳定性与腿部耐力', NULL, '["下肢","耐力"]', '["恢复","塑形"]', '["徒手","静力"]', 1, 0),

          ('高抬腿', 'EXERCISE', 'SECONDS', 30, 'https://example.com/highknees.jpg', 'https://example.com/highknees.mp4', 15, '<p>原地快速交替抬腿，膝盖尽量抬高至腰部。摆臂配合，保持频率。</p>', '快速提升心率的热身动作', NULL, '["有氧","下肢"]', '["减脂","热身"]', '["徒手","HIIT"]', 1, 0);

-- 注意：休息动作已经在建表脚本中插入，这里不再重复插入。
-- 插入20条新的动作数据
INSERT INTO tb_action
(name,
 action_type,
 unit_type,
 default_value,
 cover_url,
 video_url,
 video_duration_sec,
 tips,
 intro,
 create_user_id,
 category,
 goal_tags,
 tags,
 status,
 is_deleted
) VALUES
      -- 上肢训练系列
      ('钻石俯卧撑', 'EXERCISE', 'REPS', 12, 'https://example.com/diamond_pushup.jpg', 'https://example.com/diamond_pushup.mp4', 20, '<p>双手拇指和食指并拢呈钻石形状，置于胸下方。主要刺激三头肌和胸肌内侧。</p>', '强化三头肌和胸肌中缝', NULL, '["上肢","胸部"]', '["增肌","塑形"]', '["徒手","进阶"]', 1, 0),

      ('宽距俯卧撑', 'EXERCISE', 'REPS', 15, 'https://example.com/wide_pushup.jpg', 'https://example.com/wide_pushup.mp4', 18, '<p>双手间距宽于肩，主要刺激胸肌外侧。注意控制下落深度。</p>', '针对胸肌外侧的训练', NULL, '["上肢","胸部"]', '["增肌","塑形"]', '["徒手","基础"]', 1, 0),

      ('引体向上', 'EXERCISE', 'REPS', 8, 'https://example.com/pullup.jpg', 'https://example.com/pullup.mp4', 25, '<p>双手略宽于肩，利用背阔肌力量将身体拉起，下巴过杠。下落时控制速度。</p>', '背部宽度训练王牌动作', NULL, '["上肢","背部"]', '["增肌","力量"]', '["器械","进阶"]', 1, 0),

      ('澳大利亚引体', 'EXERCISE', 'REPS', 12, 'https://example.com/australian_pullup.jpg', 'https://example.com/australian_pullup.mp4', 20, '<p>低杠斜身划船，身体呈一条直线。适合初学者建立背部力量。</p>', '背部训练入门动作', NULL, '["上肢","背部"]', '["增肌","塑形"]', '["徒手","新手友好"]', 1, 0),

      ('哑铃弯举', 'EXERCISE', 'REPS', 12, 'https://example.com/dumbbell_curl.jpg', 'https://example.com/dumbbell_curl.mp4', 15, '<p>大臂固定，小臂向上弯举至顶峰收缩。全程控制，避免借力。</p>', '经典二头肌训练', NULL, '["上肢","手臂"]', '["增肌","塑形"]', '["器械","基础"]', 1, 0),

      -- 下肢训练系列
      ('保加利亚分腿蹲', 'EXERCISE', 'REPS', 10, 'https://example.com/bulgarian_split_squat.jpg', 'https://example.com/bulgarian_split_squat.mp4', 25, '<p>后脚置于凳上，前腿下蹲至大腿平行地面。专注单侧腿部力量。</p>', '单侧腿部力量训练', NULL, '["下肢","臀腿"]', '["增肌","平衡"]', '["徒手","进阶"]', 1, 0),

      ('侧弓步', 'EXERCISE', 'REPS', 12, 'https://example.com/side_lunge.jpg', 'https://example.com/side_lunge.mp4', 18, '<p>向一侧迈出，下蹲时另一腿伸直。锻炼大腿内外侧和髋关节灵活性。</p>', '提升髋关节灵活性', NULL, '["下肢","臀腿"]', '["塑形","灵活性"]', '["徒手","基础"]', 1, 0),

      ('臀桥', 'EXERCISE', 'REPS', 20, 'https://example.com/glute_bridge.jpg', 'https://example.com/glute_bridge.mp4', 15, '<p>仰卧屈膝，臀部发力向上顶起至身体呈直线。顶峰收缩2秒。</p>', '激活臀部肌肉', NULL, '["下肢","臀部"]', '["塑形","康复"]', '["徒手","新手友好"]', 1, 0),

      ('单腿臀桥', 'EXERCISE', 'REPS', 12, 'https://example.com/single_leg_glute_bridge.jpg', 'https://example.com/single_leg_glute_bridge.mp4', 20, '<p>单腿支撑完成臀桥，增加难度和不稳定性。强化臀中肌。</p>', '进阶臀部训练', NULL, '["下肢","臀部"]', '["塑形","平衡"]', '["徒手","进阶"]', 1, 0),

      ('提踵', 'EXERCISE', 'REPS', 25, 'https://example.com/calf_raise.jpg', 'https://example.com/calf_raise.mp4', 12, '<p>双脚与肩同宽，缓慢踮起脚尖至最高点。强化小腿肌肉。</p>', '小腿塑形训练', NULL, '["下肢","小腿"]', '["塑形","力量"]', '["徒手","基础"]', 1, 0),

      -- 核心训练系列
      ('俄罗斯转体', 'EXERCISE', 'REPS', 20, 'https://example.com/russian_twist.jpg', 'https://example.com/russian_twist.mp4', 18, '<p>坐姿身体后仰，双脚离地，双手交替向两侧旋转。保持核心收紧。</p>', '侧腹肌训练', NULL, '["核心","腹部"]', '["塑形","旋转"]', '["徒手","基础"]', 1, 0),

      ('仰卧举腿', 'EXERCISE', 'REPS', 15, 'https://example.com/leg_raise.jpg', 'https://example.com/leg_raise.mp4', 18, '<p>仰卧，双腿伸直抬起至90度，缓慢下放不触地。锻炼下腹部。</p>', '下腹肌强化训练', NULL, '["核心","腹部"]', '["塑形","核心训练"]', '["徒手","基础"]', 1, 0),

      ('鸟狗式', 'EXERCISE', 'REPS', 12, 'https://example.com/bird_dog.jpg', 'https://example.com/bird_dog.mp4', 15, '<p>四点支撑，同时抬起对侧手脚，保持身体稳定。强化脊柱稳定性。</p>', '核心稳定训练', NULL, '["核心","平衡"]', '["康复","稳定性"]', '["徒手","新手友好"]', 1, 0),

      ('死虫式', 'EXERCISE', 'REPS', 12, 'https://example.com/dead_bug.jpg', 'https://example.com/dead_bug.mp4', 15, '<p>仰卧，手臂和腿抬起，对侧伸展。腰部贴地，保持核心稳定。</p>', '核心激活训练', NULL, '["核心","协调性"]', '["康复","核心激活"]', '["徒手","新手友好"]', 1, 0),

      -- 有氧/HIIT系列
      ('波比跳进阶', 'EXERCISE', 'REPS', 8, 'https://example.com/burpee_advanced.jpg', 'https://example.com/burpee_advanced.mp4', 28, '<p>标准波比跳基础上加入俯卧撑和跳跃摸高，强度更大。</p>', '高强度燃脂动作', NULL, '["全身","HIIT"]', '["减脂","进阶"]', '["徒手","高阶"]', 1, 0),

      ('跳绳', 'EXERCISE', 'SECONDS', 45, 'https://example.com/jump_rope.jpg', 'https://example.com/jump_rope.mp4', 20, '<p>保持节奏，手腕发力摇绳。双脚并拢或交替跳。</p>', '高效有氧燃脂', NULL, '["有氧","全身"]', '["减脂","心肺"]', '["器械","新手友好"]', 1, 0),

      ('滑雪跳', 'EXERCISE', 'REPS', 16, 'https://example.com/skier_swings.jpg', 'https://example.com/skier_swings.mp4', 20, '<p>左右交替跳跃，手臂摆动如滑雪。锻炼侧向移动能力。</p>', '侧向跳跃训练', NULL, '["有氧","下肢"]', '["减脂","敏捷性"]', '["徒手","基础"]', 1, 0),

      ('滑冰步', 'EXERCISE', 'REPS', 20, 'https://example.com/skater_hop.jpg', 'https://example.com/skater_hop.mp4', 18, '<p>单腿向侧跳跃，落地缓冲后立即反向跳跃。提高心肺功能。</p>', '增强心肺功能', NULL, '["有氧","下肢"]', '["减脂","敏捷性"]', '["徒手","进阶"]', 1, 0),

      -- 拉伸/恢复系列
      ('猫牛式', 'OTHER', 'REPS', 10, 'https://example.com/cat_cow.jpg', 'https://example.com/cat_cow.mp4', 12, '<p>四足跪姿，吸气抬头塌腰，呼气低头拱背。灵活脊柱。</p>', '脊柱灵活性训练', NULL, '["恢复","柔韧"]', '["拉伸","康复"]', '["瑜伽","新手友好"]', 1, 0),

      ('下犬式', 'OTHER', 'SECONDS', 30, 'https://example.com/downward_dog.jpg', 'https://example.com/downward_dog.mp4', 15, '<p>双手推地，臀部向上抬高，形成倒V字形。拉伸背部和腿部后侧。</p>', '全身拉伸放松', NULL, '["恢复","柔韧"]', '["拉伸","放松"]', '["瑜伽","新手友好"]', 1, 0);



DROP TABLE IF EXISTS tb_course_action;

CREATE TABLE tb_course_action (
                                  id                BIGINT        NOT NULL AUTO_INCREMENT COMMENT '步骤ID',
                                  course_id         BIGINT        NOT NULL COMMENT '课程ID',
                                  sort_no           INT           NOT NULL COMMENT '步骤序号（从1开始）',
                                  action_id         BIGINT        NOT NULL COMMENT '动作模板ID',
                                  target_value      INT           DEFAULT NULL COMMENT '目标值（次数或秒数；单位由动作模板unit_type决定）',
                                  tips_override     TEXT          DEFAULT NULL COMMENT '步骤提示（富文本，可选，覆盖动作模板tips）',
                                  metrics_override  VARCHAR(4096) DEFAULT NULL COMMENT '步骤指标扩展（JSON字符串，可选）',
                                  status            TINYINT       DEFAULT 1 COMMENT '状态（1启用 0禁用）',
                                  is_deleted        TINYINT       DEFAULT 0 COMMENT '删除标志（0正常 1删除）',
                                  create_time       DATETIME      DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                  update_time       DATETIME      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程动作编排表（线性步骤）';


DROP TABLE IF EXISTS tb_plan;

CREATE TABLE tb_plan (
                         id                 BIGINT        NOT NULL AUTO_INCREMENT COMMENT '计划ID',

                         title              VARCHAR(128)  NOT NULL COMMENT '计划标题',
                         cover_url          VARCHAR(255)  DEFAULT NULL COMMENT '封面URL',
                         intro              VARCHAR(500)  DEFAULT NULL COMMENT '计划简介',

                         days               INT           DEFAULT 0 COMMENT '计划天数（冗余字段，可与明细一致）',

                         fit_people_tags    VARCHAR(4096) DEFAULT NULL COMMENT '适应人群标签（JSON数组字符串，如["宝妈","大体重"]）',
                         goal_tags          VARCHAR(4096) DEFAULT NULL COMMENT '目标标签（JSON数组字符串，如["增肌","舒缓放松"]）',
                         body_part_tags     VARCHAR(4096) DEFAULT NULL COMMENT '部位标签（JSON数组字符串，如["胸","肩","腿"]）',

                         difficulty         TINYINT       DEFAULT 1 COMMENT '难度（1零基础 2初级 3进阶 4强化）',

                         status             TINYINT       DEFAULT 1 COMMENT '状态（1上架 0下架）',
                         is_deleted         TINYINT       DEFAULT 0 COMMENT '删除标志（0正常 1删除）',

                         create_time        DATETIME      DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                         update_time        DATETIME      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

                         PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='训练计划表';

DROP TABLE IF EXISTS tb_plan_day;

CREATE TABLE tb_plan_day (
                             id            BIGINT        NOT NULL AUTO_INCREMENT COMMENT '计划天ID',

                             plan_id       BIGINT        NOT NULL COMMENT '计划ID',
                             day_no        INT           NOT NULL COMMENT '第几天（从1开始）',

                             title         VARCHAR(128)  DEFAULT NULL COMMENT '当天标题（可选）',
                             tips          VARCHAR(1024) DEFAULT NULL COMMENT '当天提示（可选）',

                             status        TINYINT       DEFAULT 1 COMMENT '状态（1启用 0禁���）',
                             is_deleted    TINYINT       DEFAULT 0 COMMENT '删除标志（0正常 1删除）',

                             create_time   DATETIME      DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                             update_time   DATETIME      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

                             PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='训练计划-天表';

DROP TABLE IF EXISTS tb_plan_day_course;

CREATE TABLE tb_plan_day_course (
                                    id            BIGINT        NOT NULL AUTO_INCREMENT COMMENT '记录ID',

                                    plan_id       BIGINT        NOT NULL COMMENT '计划ID（冗余字段，便于查询）',
                                    plan_day_id   BIGINT        NOT NULL COMMENT '计划天ID',

                                    sort_no       INT           NOT NULL COMMENT '当天课程序号（从1开始）',
                                    course_id     BIGINT        NOT NULL COMMENT '课程ID',

                                    remark        VARCHAR(255)  DEFAULT NULL COMMENT '备注（可选）',

                                    status        TINYINT       DEFAULT 1 COMMENT '状态（1启用 0禁用）',
                                    is_deleted    TINYINT       DEFAULT 0 COMMENT '删除标志（0正常 1删除）',

                                    create_time   DATETIME      DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                    update_time   DATETIME      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

                                    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='训练计划-天-课程安排表';


DROP TABLE IF EXISTS tb_sport;

CREATE TABLE tb_sport (
                          id              BIGINT        NOT NULL AUTO_INCREMENT COMMENT '运动ID',

                          name            VARCHAR(128)  NOT NULL COMMENT '运动名称（跑步/尊巴/羽毛球…）',
                          sport_type      VARCHAR(32)   NOT NULL COMMENT '运动类型（RUN/DANCE/BALL/HIIT/OTHER）',

                          calorie_factor  DECIMAL(6,3)  NOT NULL DEFAULT 1.000 COMMENT '消耗系数（���础消耗×系数=最终消耗）',

                          cover_url       VARCHAR(255)  DEFAULT NULL COMMENT '封面URL',
                          intro           VARCHAR(500)  DEFAULT NULL COMMENT '简介',

                          status          TINYINT       DEFAULT 1 COMMENT '状态（1启用 0禁用）',
                          is_deleted      TINYINT       DEFAULT 0 COMMENT '删除标志（0正常 1删除）',
                          create_time     DATETIME      DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                          update_time     DATETIME      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

                          PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='运动种类表';


INSERT INTO tb_sport (name, sport_type, calorie_factor, cover_url, intro, status, is_deleted)
VALUES
    ('跑步',   'RUN',   1.200, NULL, '记录运动时长与平均心率，可选记录里程/配速等。', 1, 0),
    ('快走',   'RUN',   0.850, NULL, '低强度有氧，适合日常通勤与恢复。', 1, 0),
    ('骑行',   'OTHER', 1.050, NULL, '室外/室内骑行均可，建议记录平均心率。', 1, 0),
    ('跳绳',   'HIIT',  1.300, NULL, '高强度间歇类运动，时长较短但强度高。', 1, 0),
    ('HIIT',  'HIIT',  1.250, NULL, '高强度间歇训练（非课程跟练），仅记录时长与平均心率。', 1, 0),
    ('尊巴舞', 'DANCE', 1.050, NULL, '舞蹈有氧，快乐燃脂。', 1, 0),
    ('羽毛球', 'BALL',  1.150, NULL, '球类运动，强度随对抗变化，建议记录平均心率。', 1, 0),
    ('篮球',   'BALL',  1.250, NULL, '对抗强度较高，消耗系数略高。', 1, 0),
    ('瑜伽',   'OTHER', 0.700, NULL, '舒缓拉伸为主，强度较低。', 1, 0);




DROP TABLE IF EXISTS tb_sport_record;

CREATE TABLE tb_sport_record (
                                 id                     BIGINT       NOT NULL AUTO_INCREMENT COMMENT '运动记录ID',

                                 user_id                BIGINT       NOT NULL COMMENT '用户ID',
                                 sport_id               BIGINT       NOT NULL COMMENT '运动ID（tb_sport.id）',

                                 start_time             DATETIME     DEFAULT NULL COMMENT '开始时间',
                                 end_time               DATETIME     DEFAULT NULL COMMENT '结束时间',

                                 duration_sec           INT          DEFAULT NULL COMMENT '运动时长（秒）',
                                 avg_hr                 INT          DEFAULT NULL COMMENT '平均心率（可空）',

                                 calories_base          INT          DEFAULT NULL COMMENT '基础消耗（kcal，未乘系数）',
                                 calorie_factor_snapshot DECIMAL(6,3) DEFAULT NULL COMMENT '消耗系数快照（用于回溯）',
                                 calories               INT          DEFAULT NULL COMMENT '最终消耗（kcal）',

                                 completed              TINYINT      DEFAULT 0 COMMENT '是否完成（1完成 0中断）',
                                 auto_closed            TINYINT      DEFAULT 0 COMMENT '是否自动结束（1是 0否）',
                                 close_reason           VARCHAR(32)  DEFAULT 'manual' COMMENT '结束原因（manual/timeout/app_close等）',

                                 note                   VARCHAR(255) DEFAULT NULL COMMENT '备注',
                                 metrics                TEXT         DEFAULT NULL COMMENT '扩展指标（JSON，可选；跑步里程/配速等放这里）',

                                 is_deleted             TINYINT      DEFAULT 0 COMMENT '删除标志（0正常 1删除）',
                                 create_time            DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                 update_time            DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

                                 PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='运动记录表';

-- =========================
-- 板块表
-- =========================
DROP TABLE IF EXISTS tb_board;
CREATE TABLE tb_board (
                          id              BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '版块ID',
                          name            VARCHAR(64)  NOT NULL COMMENT '版块名称',
                          description     VARCHAR(255) DEFAULT NULL COMMENT '版块描述',
                          icon            VARCHAR(255) DEFAULT NULL COMMENT '版块图标',
                          sort            INT          NOT NULL DEFAULT 99 COMMENT '排序值，越小越靠前',
                          status          TINYINT      NOT NULL DEFAULT 1 COMMENT '状态（0-禁用 1-启用）',
                          post_count      BIGINT       NOT NULL DEFAULT 0 COMMENT '帖子数（冗余）',
                          follower_count  BIGINT       NOT NULL DEFAULT 0 COMMENT '关注数（冗余）',
                          is_deleted      TINYINT      NOT NULL DEFAULT 0 COMMENT '删除标志（0-未删除 1-已删除）',
                          create_by       VARCHAR(64)  DEFAULT '' COMMENT '创建者',
                          create_time     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                          update_by       VARCHAR(64)  DEFAULT '' COMMENT '更新者',
                          update_time     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                          remark          VARCHAR(500) DEFAULT NULL COMMENT '备注',
                          INDEX idx_status_sort (status, sort),
                          INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='社区版块表';


-- =========================
-- 2. 帖子表
-- =========================
DROP TABLE IF EXISTS tb_post;
CREATE TABLE tb_post (
                         id              BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '帖子ID',
                         board_id        BIGINT       NOT NULL COMMENT '所属版块ID',
                         user_id         BIGINT       NOT NULL COMMENT '发帖人ID',
                         title           VARCHAR(200) NOT NULL COMMENT '帖子标题',
                         content         MEDIUMTEXT   NOT NULL COMMENT '帖子内容（富文本）',
                         tags            JSON         DEFAULT NULL COMMENT '帖子标签(JSON数组)',
                         cover_image     JSON         DEFAULT NULL COMMENT '封面图(JSON数组)',
                         view_count      BIGINT       NOT NULL DEFAULT 0 COMMENT '浏览数',
                         like_count      BIGINT       NOT NULL DEFAULT 0 COMMENT '点赞数',
                         comment_count   BIGINT       NOT NULL DEFAULT 0 COMMENT '评论数',
                         favorite_count  BIGINT       NOT NULL DEFAULT 0 COMMENT '收藏数',
                         hot_score       DECIMAL(12,4) NOT NULL DEFAULT 0.0000 COMMENT '热度分',
                         status          TINYINT      NOT NULL DEFAULT 1 COMMENT '状态（0-草稿 1-发布 2-下架）',
                         is_deleted      TINYINT      NOT NULL DEFAULT 0 COMMENT '删除标志（0-未删除 1-已删除）',
                         create_by       VARCHAR(64)  DEFAULT '' COMMENT '创建者',
                         create_time     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                         update_by       VARCHAR(64)  DEFAULT '' COMMENT '更新者',
                         update_time     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                         remark          VARCHAR(500) DEFAULT NULL COMMENT '备注',
                         INDEX idx_board_time (board_id, create_time DESC),
                         INDEX idx_board_hot (board_id, hot_score DESC),
                         INDEX idx_user_time (user_id, create_time DESC),
                         INDEX idx_status_deleted_time (status, is_deleted, create_time DESC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='社区帖子表';


-- =====================================
-- 用户画像表（MVP，可直接用于推荐打分）
-- =====================================
DROP TABLE IF EXISTS tb_user_profile;
CREATE TABLE tb_user_profile (
                                 id                BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
                                 user_id           BIGINT       NOT NULL COMMENT '用户ID',
                                 tag_pref          JSON         DEFAULT NULL COMMENT '标签偏好分数，如 {"Java":12.5,"Spring":8.2}',
                                 board_pref        JSON         DEFAULT NULL COMMENT '版块偏好分数，如 {"1":10.0,"3":4.6}',
                                 last_active_time  DATETIME     DEFAULT NULL COMMENT '最近活跃时间',
                                 profile_version   INT          NOT NULL DEFAULT 1 COMMENT '画像版本号',
                                 is_deleted        TINYINT      NOT NULL DEFAULT 0 COMMENT '删除标志（0-未删除 1-已删除）',
                                 create_by         VARCHAR(64)  DEFAULT '' COMMENT '创建者',
                                 create_time       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                 update_by         VARCHAR(64)  DEFAULT '' COMMENT '更新者',
                                 update_time       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                 remark            VARCHAR(500) DEFAULT NULL COMMENT '备注',
                                 UNIQUE KEY uk_user_id (user_id),
                                 INDEX idx_last_active_time (last_active_time),
                                 INDEX idx_update_time (update_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户画像表';


-- =====================================
-- 用户行为事件表（用于画像更新/训练样本）
-- =====================================
DROP TABLE IF EXISTS tb_user_event;
CREATE TABLE tb_user_event (
                               id             BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '事件ID',
                               user_id        BIGINT       NOT NULL COMMENT '用户ID',
                               post_id        BIGINT       NOT NULL COMMENT '帖子ID',
                               event_type     VARCHAR(32)  NOT NULL COMMENT '事件类型：impression_duration/click/like/comment/favorite',
                               scene          VARCHAR(32)  DEFAULT NULL COMMENT '场景：feed_list/post_detail',
                               page_no        INT          DEFAULT NULL COMMENT '分页页码',
                               start_ts       BIGINT       DEFAULT NULL COMMENT '开始时间戳(ms)',
                               end_ts         BIGINT       DEFAULT NULL COMMENT '结束时间戳(ms)',
                               duration_ms    BIGINT       NOT NULL DEFAULT 0 COMMENT '停留时长(ms)',
                               event_time     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '事件时间',
                               is_deleted     TINYINT      NOT NULL DEFAULT 0 COMMENT '删除标志（0-未删除 1-已删除）',
                               create_by      VARCHAR(64)  DEFAULT '' COMMENT '创建者',
                               create_time    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                               update_by      VARCHAR(64)  DEFAULT '' COMMENT '更新者',
                               update_time    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                               remark         VARCHAR(500) DEFAULT NULL COMMENT '备注',
                               INDEX idx_user_time (user_id, event_time DESC),
                               INDEX idx_post_time (post_id, event_time DESC),
                               INDEX idx_event_type_time (event_type, event_time DESC),
                               INDEX idx_scene_time (scene, event_time DESC),
                               INDEX idx_user_post_time (user_id, post_id, event_time DESC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户行为事件表';


-- =====================================
-- 可选：给已有帖子表补充索引（推荐场景常用）
-- 若已存在可忽略报错
-- =====================================
ALTER TABLE tb_post
    ADD INDEX idx_post_board_time (board_id, create_time DESC),
    ADD INDEX idx_post_hot (hot_score DESC),
    ADD INDEX idx_post_status_deleted_time (status, is_deleted, create_time DESC);

DROP TABLE IF EXISTS tb_recommend_exposure;

CREATE TABLE `tb_recommend_exposure` (
                                         `id` bigint NOT NULL AUTO_INCREMENT,
                                         `user_id` bigint NOT NULL,
                                         `post_id` bigint NOT NULL,
                                         `biz_date` date NOT NULL,
                                         `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                         PRIMARY KEY (`id`),
                                         UNIQUE KEY `uk_user_post_day` (`user_id`,`post_id`,`biz_date`),
                                         KEY `idx_user_time` (`user_id`,`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;