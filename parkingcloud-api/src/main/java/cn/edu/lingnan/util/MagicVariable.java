package cn.edu.lingnan.util;

public class MagicVariable {
    public static final int INFO_DEFAULT_STATE_VALUES = 5;
    public static final double INFO_SEARCH_LIMIT = 0.02;
    public static final double INFO_ADD_TEMP_LIMIT = 0.002;
    public static final double MINIMUM_X = 73;
    public static final double MAXIMUM_X = 136;
    public static final double MINIMUM_Y = 1;
    public static final double MAXIMUM_Y = 54;
    public static final int MINIMUM_STAR = 0;
    public static final int MAXIMUM_STAR = 5;
    public static final int MINIMUM_STATE = 0;
    public static final int MAXIMUM_STATE = 10;
    public static final long INTERVAL_TIME = 1800000;

    public static final String NO_VERIFIED = "no_verified";
    public static final int HASH_TIMES = 3;
    public static final String ALGORITHM_NAME = "md5";
    public static final String INFO_IMAGE_PATH = "static/img/info";

    public static final String DEFAULT_ROLE = "user";
    public static final String ADMIN_ROLE = "admin";
    public static final String SUBMIT_DATA_ERROR = "提交数据有误";
    public static final String LOGIN_FAIL = "登入失败:手机号/密码错误";
    public static final String USER_PASSWORD_ERROR = "登录密码错误";
    public static final String USER_PHONE_NOT_EXIST = "用户手机号不存在";
    public static final String LOGIN_SUCCESS = "登入成功";
    public static final String LOGOUT_SUCCESS = "登出成功";
    public static final String UN_LOGIN = "未登录";
    public static final String USERNAME_IS_EMPTY = "用户名为空";
    public static final String PASSWORD_IS_EMPTY = "密码为空";
    public static final String PHONE_IS_EMPTY = "手机号为空";
    public static final String USERNAME_IS_EXIST = "用户名已经被使用,不能使用";
    public static final String PHONE_IS_EXIST = "手机号已经被注册,请前往登录";
    public static final String REGISTER_SUCCESS = "注册成功";
    public static final String NO_USER_IN_SESSION = "session中无法获取到user";

    public static final String PARKING_IS_EXIST = "停车场已存在,不能提交";
    public static final String INFO_ID_IS_EMPTY = "停车场id为空";
    public static final String INFO_NAME_IS_EMPTY = "停车场名字为空";
    public static final String INFO_LOCATION_EMPTY = "停车场坐标为空";
    public static final String INFO_STATE_IS_EMPTY = "停车场状态值为空";
    public static final String INFO_IMAGE_IS_EMPTY = "停车场图片为空";
    public static final String INFO_X_ERROR = "停车场坐标X有误";
    public static final String INFO_Y_ERROR = "停车场坐标Y有误";
    public static final String INFO_STATE_IS_ERROR = "停车场状态值有误";
    public static final String INFO_LOCATION_ILLEGAL = "经纬度定位不合法";
    public static final String INFO_ADD_SUCCESS = "停车场添加成功";
    public static final String INFO_NOT_EXIST = "停车场不存在";
    public static final String INFO_DELETE_SUCCESS = "停车场删除成功";

    public static final String PARAM_VALUES_IS_EMPTY = "参数值为空";
    public static final String PARAM_VALUES_ILLEGAL = "参数值不合法";
    public static final String BAD_REQUEST = "参数不合法,Bad Request";

    public static final String RESULT_NO_EXIST = "结果不存在";

    public static final String UNKNOW_ERROR = "未知异常";
    public static final String LOGGER_ERROR_MESSAGE = "未知异常,请勿重复提交该异常以加重服务器负荷,这可能会导致封号.管理员会自行查看日志处理.如多日未处理请联系17450856@qq.com";
    public static final String LOGGER_ERROR_NO_LOGIN = "你触发了异常提交,请描述该异常并发送到邮箱17450856@qq.com谢谢~";
    public static final String LOGGER_NUMBER_FORMAT_EXCEPTION = "数值格式转换异常,请检查提交输入";
    public static final String LOGGER_USER = "触发异常的用户:";
    public static final String LOGGER_MISSING_PARAMETER = "缺少必要参数";
    public static final String LOGGER_DATA_INTEGRITY_VIOLATION_EXCEPTION = "数据完整性违规异常";
    public static final String LOGGER_MYSQL_INTEGRITY_CONSTRAINT_VIOLATION_EXCEPTION = "MySQL完整性约束违反异常";

    public static final String TEMP_ADD_SUCCESS = "状态提交成功";
    public static final String REVIEW_ADD_SUCCESS = "评论添加成功";
    public static final String LOCATION_NOT_NEAR_PARKING = "你不在该停车位附近,无法提交信息";

    public static final String DEFAULT_REVIEW = "用户评价为空";
    public static final String STAR_ILLEGAL = "评分不合法";
    public static final String LOCATION_ILLEGAL = "坐标不合法";
    public static final String LOCATION_IS_EMPTY = "坐标为空";
    public static final String STAR_IS_EMPTY = "评分为空";
    public static final String INTERVAL_TIME_ERROR = "你只能在30分钟内提交一次状态哦";

    public static final int SEARCH_GEOHASH_LIMIT = 6;
    public static final int INFO_GEOHASH_LIMIT = 7;
    public static final int ADD_TEMP_GEOHASH_LIMIT = 7;

    public static final long REPLAY_ATTACK_INTERVAL = 60000;
    public static final String REPLAY_ATTACK_DETECT = "检测到重放攻击";
    public static final String REQUEST_METHOD_NOT_SUPPORTED = "http请求方法不支持";

    // 参数长度类
    public static final int INFO_NAME_MAX_LEN = 64;
    public static final int INFO_CONTENT_MAX_LEN = 4000;
    public static final int USER_NAME_MAX_LEN = 64;
    public static final int USER_PASSWORD_MAX_LEN = 64;
    public static final int USER_PHONE_MAX_LEN = 11;
    public static final int REVIEW_CONTENT_MAX_LEN = 4000;

    public static final String PARAM_VALUES_TOO_MAX = "请求参数长度过长";
    public static final String VERIFIED = "verified";
    public static final String DISABLE = "disable";
    public static final String INFO_IMAGE_NO_EXIST = "停车场照片不存在";

    public static final String NOT_ADMIN = "非管理员不可访问";
    public static final String NO_PERMIT = "未授权";
}
