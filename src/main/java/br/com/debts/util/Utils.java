package br.com.debts.util;

import java.util.function.Supplier;

public class Utils {

    public static <T> boolean nonNullSafeEval(Supplier<T> supplier) {
        try {
            return supplier.get() != null;
        } catch (NullPointerException npe) {
            return false;
        }
    }
    
}
