**注解记录请求，返回参数日志工具**

- Spring环境（包括spring boot）也可以使用，除以下说明及依赖外无需多余配置

- 使用方法

1.需要加入maven插件：aspectj-maven-plugin

```xml
<plugin>
    <groupId>org.codehaus.mojo</groupId>
    <artifactId>aspectj-maven-plugin</artifactId>
    <version>1.10</version>
    <configuration>
        <encoding>UTF-8</encoding>
        <complianceLevel>${JDK_VERSION}</complianceLevel>
        <proc>none</proc>
        <aspectLibraries>
            <aspectLibrary>
                <groupId>com.hupubao</groupId>
                <artifactId>hupubao-common-utils</artifactId>
            </aspectLibrary>
        </aspectLibraries>
    </configuration>
    <executions>
        <execution>
            <goals>
                <goal>compile</goal>
            </goals>
            <configuration>
                <source>${JDK_VERSION}</source>
                <target>${JDK_VERSION}</target>
            </configuration>
        </execution>
    </executions>
</plugin>

```

2. 配置过滤器

```java
@Configuration
@ConditionalOnClass(SpringfoxWebMvcConfiguration.class)
public class ApplicationWebMvcConfigurer implements WebMvcConfigurer {


	/**
	 * <h1>向下传递可重复读filter</h1>
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Bean
	public FilterRegistrationBean repeatedlyReadFilter() {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		RepeatedlyReadFilter repeatedlyReadFilter = new RepeatedlyReadFilter();
		registration.setFilter(repeatedlyReadFilter);
		registration.addUrlPatterns("/*");
		return registration;
	}
}
```

3.使用

```java
import javax.servlet.http.HttpServletRequest;

public class BookController {
    
    @LogReqResArgs(title = "书架接口")
    public static Object bookShelf(HttpServletRequest request) {
    
        //Your code.
        return null;
    }
}
```

3.注意：参数必须有HttpServletRequest类型参数，
若需要记录返回参数日志，则方法需要返回值。

4.注解详细参数

|参数名|描述|类型|默认值|
| :------: | :------: | :------: | :------|
|title|日志头描述|String|@RequestMapping路径或方法名|
|titleRequest|日志请求参数头描述|String|request parameters:|
|titleResponse|日志返回参数头描述|String|response parameters:|
|titleException|日志异常头描述|String|exception:|
|logException|是否记录异常日志|boolean|true|
|logResponseWithRequest|记录返回结果时带上请求参数|boolean|false|
|logMethod|是否返回记录HTTP请求类型,GET,POST...|boolean|true|
|logUrl|是否记录请求链接|boolean|true|

5.输出日志样例

