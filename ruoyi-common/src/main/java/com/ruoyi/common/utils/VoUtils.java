package com.ruoyi.common.utils;

import com.ruoyi.common.core.domain.User;
import com.ruoyi.common.core.domain.Course;
import com.ruoyi.common.core.domain.vo.CourseVO;
import com.ruoyi.common.core.domain.vo.UserVO;
import org.springframework.beans.BeanUtils;

public class VoUtils {
    public static UserVO transUserToVo(User user)
    {
        UserVO userVo = new UserVO();
        BeanUtils.copyProperties(user,userVo);
        return userVo;
    }

    public static CourseVO transCourseToVo(Course course)
    {
        CourseVO courseVO = new CourseVO();
        BeanUtils.copyProperties(course, courseVO);
        return courseVO;
    }
}
