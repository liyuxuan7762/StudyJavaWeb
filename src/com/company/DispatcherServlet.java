package com.company;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@WebServlet("*.do")
public class DispatcherServlet extends ViewBaseServlet {

    private BeanFactory beanFactory;

    public DispatcherServlet() {


    }

    @Override
    public void init() throws ServletException {
        super.init();
        // 从applicaitonContext.xml中读取出请求.do和对应类的关系
        BeanFactory beanFactoryObj = (BeanFactory) getServletContext().getAttribute("beanFactory");
        if(beanFactoryObj != null) {
            this.beanFactory = beanFactoryObj;
        } else {
            throw new RuntimeException("IOC容器获取错误");
        }
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 这个类的作用是拦截所有请求，根据请求的不同，调用fruit.do里面的具体方法
        // 设置编码
        req.setCharacterEncoding("utf-8");
        // 获取请求的具体.do名称
        String servletPath = req.getServletPath();
        // 得到/hello.do 然后去掉/
        servletPath = servletPath.substring(1);
        // 去掉后面的.do
        int lastDotIndex = servletPath.lastIndexOf(".do");
        servletPath = servletPath.substring(0, lastDotIndex);
        System.out.println(servletPath);

        // 根据.do的名称找到从map中找到要调用的controller的对象
        Object controller = beanFactory.getBean(servletPath);

        String operTpye = req.getParameter("operType");
        if (operTpye == null || "".equals(operTpye)) {
            operTpye = "index";
        }

        // 使用反射
        try {
            // Method declaredMethod = controller.getClass().getDeclaredMethod(operTpye, HttpServletRequest.class);
            // 获取参数
            //1.由于每个方法的参数不一致，因此通过getDeclaredMethod(operTpye, HttpServletRequest.class);不太好获取方法
            //所以采用获取所有的方法，然后循环遍历，比较方法名称和operTpye是否一致来找到具体要调用的方法
            Method[] declaredMethods = controller.getClass().getDeclaredMethods();
            for (Method method : declaredMethods) {
                String methodName = method.getName();
                if (operTpye.equals(methodName)) {
                    // 找到要执行的方法
                    // 获取该方法的对应的形参名称
                    Parameter[] parameters = method.getParameters();
                    // 创建一个数组用来存放形参对应的值
                    Object[] parameterValues = new Object[parameters.length];
                    // 循环遍历所有参数，通过参数名从request中取出相应的值
                    for (int i = 0; i < parameters.length; i++) {
                        String name = parameters[i].getName(); // 形参名
                        if ("req".equals(name)) {
                            parameterValues[i] = req;
                        } else if ("resp".equals(name)) {
                            parameterValues[i] = resp;
                        } else if ("session".equals(name)) {
                            parameterValues[i] = req.getSession();
                        } else {
                            String parameterValue = req.getParameter(name); // 获取参数值
                            String type = parameters[i].getType().getName(); // 获取参数类型
                            // 判断如果参数是Integer 则进行一下转换，
                            if (parameterValue != null) {
                                if ("java.lang.Integer".equals(type)) {
                                    parameterValues[i] = Integer.parseInt(parameterValue);
                                } else {
                                    parameterValues[i] = parameterValue;
                                }
                            }
                        }
                    }
                    method.setAccessible(true);
                    Object methodReturnObj = method.invoke(controller, parameterValues);
                    // 获取方法返回值 根据不同的返回值来决定视图渲染
                    String returnStr = (String) methodReturnObj;
                    if (returnStr.startsWith("redirect:")) {
                        // 获取字符串redirect:后面的内容，调用redirect
                        returnStr = returnStr.substring("redirect:".length());
                        resp.sendRedirect(returnStr);
                    } else {
                        super.processTemplate(returnStr, req, resp);
                    }
                }
            }
            //2.找到该方法后，然后获取到该方法的形参名称，通过形参名称，获取到对应的值 将所有的值放入一个参数数组中
            // 3. 调用方法
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 1. 首先将依赖模块里面的属性值都赋值给null
    // 2. 配置ApplicationContext.xml文件，其中添加bean
    // 3. 创建一个接口BeanFactory提供一个方法，根据id获取class对象 <bean id = "" class = ""> </bean>
    // 4. 创建该接口的实现类对象ClassPathXmlApplicationContext 创建构造方法
    // 5. 修改ApplicationContext.xml文件，在其中添加和依赖的Properties
}
