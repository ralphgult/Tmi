package tm.utils;

/***
 ** <p>   Description: TODO(常量标识集合)</p>
 * @author admin
 *
 */
public class ConstantsHandler {
	
	public static final int judSupervisor=0;
	
   public static final int corrPerson=1;

	/**连接超时异常**/
	public static final int ConnectTimeout = 101;
	/**空指针异常**/
	public static final int NullPointer = 102;
	/**json解析异常**/
	public static final int JSONPARSE = 103;
	
	/**通用执行结果标识**/
	public static final int EXECUTE_SUCCESS= 0;
	public static final int EXECUTE_FAIL = 1;
	
	/**登录**/
	public static final int LOGIN_SUCCESS = 0;
	public static final int LOGIN_FALSE = 1;
 
	public final static int REFRESH_BACKING = 10;      //反弹中
	public final static int REFRESH_BACED = 11;        //达到刷新界限，反弹结束后
	public final static int REFRESH_RETURN = 12;       //没有达到刷新界限，返回
	public final static int REFRESH_DONE = 13;         //加载数据结束
	
	
}
