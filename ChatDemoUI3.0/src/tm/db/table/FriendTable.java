package tm.db.table;

/**
 * Created by meixi on 2016/12/20.
 */

public class FriendTable {
    /**
     * 表名
     */
    public static final String TABLE_NAME = "friend_table";
    /**
     * 好友id
     */
    public static final String USERID = "id";
    /**
     * 好友电话
     */
    public static final String USERNAME = "app_name";
    /**
     * 好友头像
     */
    public static final String PHOTO = "app_icon";
    /**
     * 好友昵称
     */
    public static final String NICKNAME = "app_nick";



    public static final String SQL_CREAT_TABLE = new StringBuilder().append("CREATE TABLE IF NOT EXISTS ").append(TABLE_NAME).append("(")
            .append(USERNAME).append(" INTEGER")
            .append(",").append(USERID).append(" INTEGER")
            .append(",").append(PHOTO).append(" TEXT")
            .append(",").append(NICKNAME).append(" TEXT")
            .append(");").toString();
}
