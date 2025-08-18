/**
 * @author Jesús Recio Rincón (@jesmrec)
 */

package utils.log;

import java.util.logging.Level;

public class StepLogger {
    public static void logCurrentStep(Level level) {
        String stepName = Thread.currentThread().getStackTrace()[2].getMethodName().toUpperCase();
        Log.log(level, "---------STEP---------: " + stepName);
    }
}
