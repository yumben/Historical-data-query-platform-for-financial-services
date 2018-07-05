package cn.com.infohold.commons;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ForkJoinPool;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.alibaba.fastjson.JSON;

import bdp.commons.dataservice.param.InsertBean;
import cn.com.infohold.basic.util.file.PropUtil;
import cn.com.infohold.service.IDataInsertService;
import cn.com.infohold.util.AnalysisUtil;
import lombok.extern.log4j.Log4j2;

/*@Component
@Aspect*/
@Log4j2
public class AopFilter {

    @Autowired
    IDataInsertService dataInsertService;

    @Pointcut("execution(* cn.com.infohold.controller..*.*(..))")
    public void executeService() {

    }

    /**
     * 前置通知，方法调用前被调用
     *
     * @param joinPoint
     */
    @Before("executeService()")
    public void doBeforeAdvice(JoinPoint joinPoint) {

        // 用的最多 通知的签名
        Signature signature = joinPoint.getSignature();
        String serviceName = signature.getDeclaringTypeName() + "->" + signature.getName();
        
        //异步
        ForkJoinPool FJP = AnalysisUtil.getForkJoinPool();
        Callable<Object> ca = new Callable<Object>() {
            @Override
            public Object call() throws Exception {
               anysDoBeforeAdvice( serviceName );
                return null;
            }
        };
        FJP.submit(ca); 
        
    }

  public void anysDoBeforeAdvice(String serviceName  ) { 
        // 用的最多 通知的签名 
        String operateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        // 获取请求信息和请求url
        Map<String, Object> logMap = new HashMap<String, Object>();
        Map<String, String> requestMap = getRequestParams();
        logMap.put("service_name", serviceName);
        logMap.put("url", requestMap.get("URL"));
        logMap.put("request_params", requestMap.get("requestParams"));
        logMap.put("operate_time", operateTime);
        logMap.put("log_id", UUID.randomUUID().toString());
        logMap.put("client_ip", requestMap.get("ClientIP"));//
        logMap.put("advice_code", "Before");
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        data.add(logMap);
        InsertBean insertBean = new InsertBean();
        insertBean.setData(data);
        String logTableMetadataCode = PropUtil.getProperty("logTableMetadataCode");
        insertBean.setTableCode(logTableMetadataCode);
        try {
            dataInsertService.insertByJson(insertBean);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 后置返回通知 这里需要注意的是: 如果参数中的第一个参数为JoinPoint，则第二个参数为返回值的信息
     * 如果参数中的第一个参数不为JoinPoint，则第一个参数为returning中对应的参数 returning
     * 限定了只有目标方法返回值与通知方法相应参数类型时才能执行后置返回通知，否则不执行，
     * 对于returning对应的通知方法参数为Object类型将匹配任何目标返回值
     *
     * @param joinPoint
     * @param keys
     */
    @AfterReturning(value = "execution(* cn.com.infohold.controller..*.*(..))", returning = "keys")
    public void doAfterReturningAdvice1(JoinPoint joinPoint, Object keys) {

        Signature signature = joinPoint.getSignature();

        String serviceName = signature.getDeclaringTypeName() + "->" + signature.getName();
        //异步
        ForkJoinPool FJP = AnalysisUtil.getForkJoinPool();
        Callable<Object> ca = new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                asynDoAfterReturningAdvice1(serviceName, keys);
                return null;
            }
        };
        FJP.submit(ca);

    }

    public void asynDoAfterReturningAdvice1(String serviceName, Object keys) {
        // 用的最多 通知的签名 
        String operateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        // 获取请求信息和请求url
        Map<String, Object> logMap = new HashMap<String, Object>();
        Map<String, String> requestMap = getRequestParams();
        if (keys != null) {
//            logMap.put("return_msg", keys.toString());
        }
        logMap.put("service_name", serviceName);
        logMap.put("url", requestMap.get("URL"));
        logMap.put("request_params", requestMap.get("requestParams"));
        logMap.put("operate_time", operateTime);
        logMap.put("log_id", UUID.randomUUID().toString());
        logMap.put("client_ip", requestMap.get("ClientIP"));//
        logMap.put("advice_code", "AfterReturning");
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        data.add(logMap);
        InsertBean insertBean = new InsertBean();
        insertBean.setData(data);
        String logTableMetadataCode = PropUtil.getProperty("logTableMetadataCode");
        insertBean.setTableCode(logTableMetadataCode);
        try {
            dataInsertService.insertByJson(insertBean);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 后置异常通知 定义一个名字，该名字用于匹配通知实现方法的一个参数名，当目标方法抛出异常返回后，将把目标方法抛出的异常传给通知方法；
     * throwing 限定了只有目标方法抛出的异常与通知方法相应参数异常类型时才能执行后置异常通知，否则不执行，
     * 对于throwing对应的通知方法参数为Throwable类型将匹配任何异常。
     *
     * @param joinPoint
     * @param exception
     */
    @AfterThrowing(value = "executeService()", throwing = "exception")
    public void doAfterThrowingAdvice(JoinPoint joinPoint, Throwable exception) {
        
        Signature signature = joinPoint.getSignature();
        String serviceName = signature.getDeclaringTypeName() + "->" + signature.getName(); 
        //异步
        ForkJoinPool FJP = AnalysisUtil.getForkJoinPool();
        Callable<Object> ca = new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                asynDoAfterThrowingAdvice(serviceName, exception);
                return null;
            }
        };
        FJP.submit(ca);
    }

   public void asynDoAfterThrowingAdvice(  String serviceName  , Throwable exception) {
       log.error("交易发生异常。。。。。");  
        String operateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        Map<String, String> requestMap = getRequestParams();
        Map<String, Object> logMap = new HashMap<String, Object>();
        logMap.put("log_id", UUID.randomUUID().toString());
        logMap.put("service_name", serviceName);
        logMap.put("url", requestMap.get("URL"));
        logMap.put("request_params", requestMap.get("requestParams"));
        logMap.put("operate_time", operateTime);
//        logMap.put("return_msg", exception.getMessage());
//        logMap.put("return_msg", "");
        logMap.put("client_ip", requestMap.get("ClientIP"));//
        logMap.put("advice_code", "AfterThrowing");
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        data.add(logMap);
        InsertBean insertBean = new InsertBean();
        insertBean.setData(data);
        String logTableMetadataCode = PropUtil.getProperty("logTableMetadataCode");
        insertBean.setTableCode(logTableMetadataCode);
        try {
            dataInsertService.insertByJson(insertBean);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 后置最终通知（目标方法只要执行完了就会执行后置通知方法）
     *
     * @param joinPoint
     */
    @After("executeService()")
    public void doAfterAdvice(JoinPoint joinPoint) {
        // System.out.println("后置通知执行了!!!!");
    }

    /**
     * 环绕通知： 环绕通知非常强大，可以决定目标方法是否执行，什么时候执行，执行时是否需要替换方法参数，执行完毕是否需要替换返回值。
     * 环绕通知第一个参数必须是org.aspectj.lang.ProceedingJoinPoint类型
     */
    @Around("execution(* cn.com.infohold.controller..*.testAround*(..))")
    public Object doAroundAdvice(ProceedingJoinPoint proceedingJoinPoint) {
        System.out.println("环绕通知的目标方法名：" + proceedingJoinPoint.getSignature().getName());
        try {// obj之前可以写目标方法执行前的逻辑
            Object obj = proceedingJoinPoint.proceed();// 调用执行目标方法
            return obj;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    public Map<String, String> getRequestParams() {
        // 获取RequestAttributes
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        // 从获取RequestAttributes中获取HttpServletRequest的信息
        HttpServletRequest request = (HttpServletRequest) requestAttributes
                    .resolveReference(RequestAttributes.REFERENCE_REQUEST);

        String url = request.getRequestURI();
        String URL = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + url;
        // 请求参数
        Enumeration<String> enumeration = request.getParameterNames();
        Map<String, String> parameterMap = new HashMap<String, String>();
        while (enumeration.hasMoreElements()) {
            String parameter = enumeration.nextElement();
            parameterMap.put(parameter, request.getParameter(parameter));
        }
        Map<String, String> map = new HashMap<String, String>();
        map.put("requestParams", JSON.toJSONString(parameterMap));
        map.put("URL", URL);
        map.put("ClientIP", getRemoteHost(request));
        return map;
    }

    public String getRemoteHost(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : ip;
    }

}
