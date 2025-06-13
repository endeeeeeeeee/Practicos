// Clase base para Logger

package com.simkernel.io;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class KernelLogger {
    public static final Logger LOG = LogManager.getLogger("SimKernel");


    public static void error(String msg) {
        LOG.error(msg);
    }

    public static void warn(String msg) {
        LOG.warn(msg);
    }
}
