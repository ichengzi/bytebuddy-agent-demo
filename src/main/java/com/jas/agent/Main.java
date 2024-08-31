package com.jas.agent;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.instrument.Instrumentation;
import java.util.Calendar;
import java.util.Date;

/**
 * @author ReaJason
 * @since 2024/3/8
 */
public class Main {
    public static void premain(String args, Instrumentation inst) throws Exception {
        launch(args, inst);
    }

    private static void launch(String args, Instrumentation inst) throws Exception {
        System.out.println("rainbow-brackets-2024.2.1 cracked plugin");

        AgentBuilder agentBuilder = new AgentBuilder.Default()
                .ignore(ElementMatchers.none())
                .with(AgentBuilder.RedefinitionStrategy.REDEFINITION);
        byPassJavaAgent(agentBuilder, inst);
        byPassLicense(agentBuilder, inst);
    }

    private static void byPassJavaAgent(AgentBuilder agentBuilder, Instrumentation inst) {
        agentBuilder.type(ElementMatchers.named("com.janetfilter.core.utils.StringUtils"))
                .transform(((builder, typeDescription, classLoader, javaModule, protectionDomain) ->
                        builder.visit(Advice.to(StringUtilsInterceptor.class).on(ElementMatchers.named("isEmpty")))))
                .installOn(inst);
        agentBuilder.type(ElementMatchers.named("java.lang.Class"))
                .transform(((builder, typeDescription, classLoader, module, protectionDomain) ->
                        builder.visit(Advice.to(ClassForNameInterceptor.class).on(ElementMatchers.named("forName")))))
                .installOn(inst);
    }

    /**
     * 设置证书过期时间为 50 天，绕过大于 60 天的检测
     */
    private static void byPassLicense(AgentBuilder agentBuilder, Instrumentation inst) {
        agentBuilder.type(ElementMatchers.named("com.intellij.ui.LicensingFacade"))
                .transform((builder, typeDescription, classLoader, javaModule, protectionDomain) ->
                        builder.visit(Advice.to(LicenseExpirationInterceptor.class).on(ElementMatchers.named("getLicenseExpirationDate"))))
                .installOn(inst);
    }

    public static class StringUtilsInterceptor {
        @Advice.OnMethodEnter
        public static void interceptorBefore(@Advice.AllArguments Object[] args,
                                             @Advice.Origin("#m") String methodName) {
            if ("isEmpty".equals(methodName)) {
                Object arg = args[0];
                if (arg != null && arg.toString().isEmpty()) {
                    if (!new Throwable().getStackTrace()[2].getClassName().startsWith("com.janetfilter.")) {
                        throw new RuntimeException("fuck you");
                    }
                }
            }
        }
    }

    public static class ClassForNameInterceptor {
        @Advice.OnMethodEnter
        public static void interceptorBefore(@Advice.AllArguments Object[] args) {
            String[] black = new String[]{
                    "jdk.internal.org.objectweb.asm.",
                    "com.intellij.diagnostic.VMOptions"
            };
            if (args[0] != null) {
                for (String str : black) {
                    if (args[0].toString().startsWith(str)) {
                        throw new RuntimeException("fuck off " + args[0].toString());
                    }
                }
            }
        }
    }

    public static class LicenseExpirationInterceptor {
        @Advice.OnMethodExit
        public static void exit(@Advice.Return(readOnly = false) Date ret) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, 50);
            ret = calendar.getTime();
        }
    }
}