package com.whdev.tokendemo.filter;


import com.whdev.tokendemo.Utils.JwtUtil;
import com.whdev.tokendemo.Utils.RedisCache;
import domain.LoginUser;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;


@Component
//OncePerRequestFilter是Spring提供的一个过滤器，可以确保在一次请求中只通过一次filter，而不需要重复执行
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private RedisCache redisCache;

    /*
        1. 定义wt认证过滤器
            1.1 获取token
            1.2. 解析token获取其中的userid
            1.3. 从redis中获取用户信息
            1.4. 存入SecurityContextHolder
    */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //获取token，指定你要获取的请求头叫什么
        String token = request.getHeader("token");

        //判空，不一定所有的请求都有请求头，第一次请求的话，是没有token的
        if (!StringUtils.hasText(token)) {  //!StringUtils.hasText()方法用于检查给定的字符串是否为空或仅包含空格字符
            //如果请求没有携带token，那么就不需要解析token，不需要获取用户信息，把请求和响应传给下一个过滤器
            filterChain.doFilter(request, response);
            //return之后，就不会走下面那些代码
            return;
        }
        //解析token
        String userid;
        try {
            Claims claims = JwtUtil.parseJWT(token);  //解析token，当token过期或者token不合法的时候，会抛出异常
            userid = claims.getSubject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Token Expired or Invalid - JwtAuthenticationTokenFilter.doFilterInternal", e);
        }
        //从redis中获取用户信息
        String redisKey = "login:" + userid;
        //LoginUser是我们在domain目录写的实体类
        LoginUser loginUser = redisCache.getCacheObject(redisKey);
        //判断获取到的用户信息是否为空，因为redis里面可能并不存在这个用户信息，例如缓存过期了
        if(Objects.isNull(loginUser)){
            //redis的有效期可以设置得比 token 短，确保用户信息只在一定时间内保持有效。
            // 这样即使 token 长时间有效，Redis 只缓存短期需要的用户数据，减少不必要的内存占用。
            // 对于频繁请求的用户，Redis 的信息会自动续期，而不频繁访问数据库；对于长时间不活动的用户，缓存会自动释放，以保持系统资源的合理使用
            throw new RuntimeException("Login Expired - JwtAuthenticationTokenFilter.doFilterInternal");
            // 同时，redis的主要作用，就是可以“主动将用户踢下线”，以 redis 的数据为准
        }

        //把最终的LoginUser用户信息，通过setAuthentication方法，存入SecurityContextHolder
        //TODO 获取权限信息封装到Authentication中，这里为什么还要再次封装权限？比如说原有的loginuser是有效的，他自己本身应该就有权限信息
        UsernamePasswordAuthenticationToken authenticationToken =
                //第一个参数是LoginUser用户信息，第二个参数是凭证(null)，第三个参数是权限信息(null)
                new UsernamePasswordAuthenticationToken(loginUser,null,loginUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        //全部做完之后，就放行，把请求和响应传给下一个过滤器
        filterChain.doFilter(request, response);
    }
}