package ioc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Bob on 2018/4/11.
 * View 事件注解的Annotation
 * @Target(ElementType.FIELD):taget 代表annotation的位置，FIELD 代表注解的是属性  还可以注解方法 METHOD;   注解在某类上 TYPE ;   注解在构造函数上 CONSTRUCTOR
 * @Retention(RetentionPolicy.RUNTIME) 代表什么时候生效 ，CLASS 表示编译时生效； RUNTIME 表示运行时生效； SOURCE 表示源码资源时生效
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OnClick {
    //  @ViewById(R.id.xxx)
    int[] value();
}
