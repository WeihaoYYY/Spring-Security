package com.whdev.tokendemo.Utils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 35238
 * @date 2023/7/11 0011 15:53
 */
public class WebUtils {
    /**
     * 将字符串渲染到客户端
     *
     * @param response 渲染对象
     * @param string 待渲染的字符串
     * @return null
     */

    //为什么用这个WebUtils而不用全局异常：
    // 过滤器属于 Servlet 层，在请求进入 Spring MVC 的控制器之前执行，这意味着全局的 @ControllerAdvice 无法捕获过滤器中抛出的异常。
    // 如果在过滤器中发现请求不符合要求（例如没有身份验证令牌），你需要立刻返回一个错误响应，并且终止请求的继续处理。
    // 在这种情况下，WebUtils.renderString 是一种有效的方法，可以直接在过滤器中操作 HttpServletResponse
    //举例：
    // 假设有一个需求，我们需要对某些请求进行IP 地址检查，如果请求来自于一个被拉黑的 IP 地址，我们需要立即返回错误响应，并且终止请求，而不让这个请求进入全局异常处理器，也不想让它到达具体的控制器
    // 控制器的主要职责是处理应用的业务逻辑，响应请求并返回数据。控制器应该专注于业务层面的操作，如处理用户请求、验证业务数据、返回响应等。
    // 进行 IP 检查是一种基础设施级别的安全检查，并不是具体业务的逻辑。将这种检查放到控制器中会使得控制器代码变得冗余、臃肿，并且会使关注点不清晰。
    // 拦截器的职责是对请求进行预处理和后处理。它通常用于请求的统一认证、授权、安全检查等。将 IP 检查放在拦截器中符合它的职责，因为这属于请求前的安全措施。
    // 通过将 IP 检查放在拦截器中，我们可以将请求是否继续执行的逻辑和业务逻辑分开，这样可以保持代码的清晰性和可维护性。
    public static String renderString(HttpServletResponse response, String string) {
        try
        {
            response.setStatus(200);
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(string);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
