package cn.edu.lingnan.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TempUserDao {

    @Select("select useful from tempUser where tid = #{tid} and uid = #{uid} ORDER BY id DESC limit 1")
    Integer searchUserOptDESC(@Param("tid") Integer tid, @Param("uid") int uid);

    @Select("insert into tempUser(uid, tid, useful) values (#{uid}, #{tid}, #{opt})")
    Integer addSelect(@Param("uid")int uid, @Param("tid")Integer tid, @Param("opt")Integer opt);

    @Select("update tempUser set useful = #{opt} where uid = #{uid} and tid = #{tid}")
    Integer updateSelect(@Param("uid")int uid, @Param("tid")Integer tid, @Param("opt")Integer opt);
}
