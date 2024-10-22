package com.jas.agent;

import com.sun.tools.attach.VirtualMachine;

import java.io.IOException;

/**
 * @author chengleiwang
 * @date 2024/10/22 20:08
 */
public class AttachMain {
    /**
     * Attach agent
     * eg: java -j
     *
     * @param args args[0] is pid, args[1] is agent jar full path
     * @throws Exception
     */
    public static void main(String[] args) {
        if (args == null || args.length < 2) {
            InnerLogUtils.error("args is null or length < 2");
            return;
        }
        VirtualMachine vm = null;
        try {
            vm = VirtualMachine.attach(args[0]);
            vm.loadAgent(args[1]);
        } catch (Throwable ex) {
            InnerLogUtils.error("loadAgent error", ex);
        } finally {
            if (vm != null) {
                try {
                    vm.detach();
                } catch (IOException e) {
                    InnerLogUtils.error("detach vm error", e);
                }
            }
        }
    }
}
