# Course APIs (Admin + Client)

## Scope
This document describes the basic course API implementation added for this project.

- Admin APIs: create/update/delete/detail/list/page
- Client APIs: list/detail/participate/cancel-participate/my-courses
- Role constraints:
  - Admin endpoints require `@RequireRole(RequireRoleConstant.ROLE_HIGHEST_CODE)`
  - Participation-related client endpoints require `@RequireRole(RequireRoleConstant.ROLE_NORMAL_CODE)`

## Main Files
- `ruoyi-admin/src/main/java/com/ruoyi/web/controller/base/admin/AdminCourseController.java`
- `ruoyi-admin/src/main/java/com/ruoyi/web/controller/base/client/ClientCourseController.java`
- `ruoyi-system/src/main/java/com/ruoyi/system/service/CourseService.java`
- `ruoyi-system/src/main/java/com/ruoyi/system/service/impl/CourseServiceImpl.java`
- `ruoyi-system/src/main/java/com/ruoyi/system/mapper/CourseMapper.java`
- `ruoyi-system/src/main/java/com/ruoyi/system/mapper/CourseParticipationMapper.java`
- `ruoyi-common/src/main/java/com/ruoyi/common/core/domain/CourseParticipation.java`
- `ruoyi-common/src/main/java/com/ruoyi/common/core/domain/dto/CourseCreateDTO.java`
- `ruoyi-common/src/main/java/com/ruoyi/common/core/domain/dto/CourseUpdateDTO.java`
- `ruoyi-common/src/main/java/com/ruoyi/common/core/domain/dto/CoursePageQueryDTO.java`
- `ruoyi-common/src/main/java/com/ruoyi/common/core/domain/dto/CourseClientQueryDTO.java`
- `ruoyi-common/src/main/java/com/ruoyi/common/core/domain/vo/CourseVO.java`

## Optional SQL
If you want to enable participation relationship persistence, run:
- `sql/tb_course_participation.sql`

## Quick Build Check
```zsh
cd /work/project/DataGather
mvn -pl ruoyi-admin -am -DskipTests compile
mvn -pl ruoyi-admin -am test -DskipITs
```

