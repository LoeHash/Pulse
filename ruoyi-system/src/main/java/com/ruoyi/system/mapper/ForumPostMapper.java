package com.ruoyi.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.common.core.domain.ForumPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ForumPostMapper extends BaseMapper<ForumPost> {


    @Select("SELECT * FROM db_data_gather.tb_post ORDER BY create_time DESC LIMIT #{limit}")
    List<ForumPost> selectLatestForRecall(int limit);


    //doing the same thing in here..
    // but for hot recall, we can consider
    // more factors like like_count, comment_count
    // , favorite_count, view_count etc.
    @Select(" SELECT" +
            "        id," +
            "        like_count," +
            "        comment_count," +
            "        favorite_count," +
            "        view_count," +
            "        create_time" +
            "    FROM tb_post" +
            "    WHERE is_deleted = 0" +
            "      AND status = 1" +
            "    ORDER BY create_time DESC" +
            "    LIMIT #{limit}")
    List<ForumPost> selectRecentPostsForHotRecall(int fetchSize);

    /**
     * 按多个 boardId 一次性召回
     */
    List<ForumPost> selectRecentByBoardIds(@Param("boardIds") List<Long> boardIds,
                                           @Param("limit") int limit);

    /**
     * 按多个 tag 一次性召回（JSON数组字符串 + LIKE）
     */
    List<ForumPost> selectRecentByTags(@Param("tags") List<String> tags,
                                       @Param("limit") int limit);

    List<ForumPost> selectWithOutExposed(@Param("exposedPostIds") List<Object> exposedPostIds,
                                         @Param("limit") int limit);

    List<ForumPost> selectRecentByBoardIdsWithOutExposed(@Param("exposedPostIds") List<Object> exposedPostIds,
                                                         @Param("boardIds") List<Long> boardIds,
                                                         @Param("limit") int limit);

    List<ForumPost> selectRecentByTagsWithOutExposed(@Param("exposedPostIds") List<Object> exposedPostIds,
                                                     @Param("tags") List<String> tags,
                                                     @Param("limit") int limit);

}

