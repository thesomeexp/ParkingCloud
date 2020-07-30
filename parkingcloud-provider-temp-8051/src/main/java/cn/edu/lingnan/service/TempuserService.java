package cn.edu.lingnan.service;

public interface TempuserService {
    Integer searchUserOptDESC(Integer tid, int uid);

    Integer addSelect(int uid, Integer tid, Integer opt);

    Integer updateSelect(int uid, Integer tid, Integer opt);
}
