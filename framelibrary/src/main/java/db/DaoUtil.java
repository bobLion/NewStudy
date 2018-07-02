package db;

/**
 * @package db
 * @fileName DbUtil
 * @Author Bob on 2018/5/11 18:10.
 * @Describe TODO
 */

public class DaoUtil {
    private DaoUtil(){
        throw new UnsupportedOperationException("can not be instantiated");
    }

    public static String getTableName(Class<?> clazz){
        return clazz.getSimpleName();
    }

    public static String getColumnType(String type){
        String value = null;
        if(type.contains("String")){
            value = " text";
        }else if(type.contains("int")){
            value = " integer";
        }else if(type.contains("boolean")){
            value = " boolean";
        }else if(type.contains("float")){
            value = " float";
        }else if(type.contains("double")){
            value = " double";
        }else if(type.contains("char")){
            value = " varchar";
        }else if(type.contains("long")){
            value = " long";
        }
        return value;
    }

    //首字母大写
    public static String captialize(String name) {
        char[] cs=name.toCharArray();
        cs[0]-=32;
        return String.valueOf(cs);

    }
}
