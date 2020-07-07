/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/04/14 22:11:44
 *
 * UntilTheEnd/UntilTheEnd/Logging.java
 */

package HamsterYDS.UntilTheEnd;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

public class Logging extends java.util.logging.Logger {
    private static final Logger logger = LogManager.getLogger("UntilTheEnd");
    private static final Logging INSTANCE = new Logging();
    private static final SimpleFormatter formatter = new SimpleFormatter();

    public static @NotNull java.util.logging.Logger getLogger() {
        return INSTANCE;
    }

    private Logging() {
        super("UntilTheEnd", null);
    }

    @Override
    public void log(LogRecord record) {
        if (!isLoggable(getLevel())) return;
        String message = "[UntilTheEnd] " + formatter.formatMessage(record);
        Throwable exception = record.getThrown();
        Level level = record.getLevel();
        if (level == Level.SEVERE) {
            logger.error(message, exception);
        } else if (level == Level.WARNING) {
            logger.warn(message, exception);
        } else if (level == Level.INFO) {
            logger.info(message, exception);
        } else if (level == Level.FINER || level == Level.FINE) {
            logger.fatal(message, exception);
        } else if (level == Level.CONFIG) {
            logger.trace(message, exception);
        }
    }
}
