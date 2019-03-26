package ru.silirdco.nir.core.util;

@SuppressWarnings("unused")
public class VarUtils {
    public static boolean getBoolean(Boolean bool) {
        if (bool == null) {
            return false;
        }
        return bool;
    }

    public static int getInteger(Integer integer) {
        if (integer == null) {
            return 0;
        }
        return integer;
    }

    public static long getLong(Long l) {
        if (l == null) {
            return 0L;
        }
        return l;
    }

    public static float getFloat(Float f) {
        if (f == null) {
            return 0F;
        }
        return f;
    }

    public static double getDouble(Double d) {
        if (d == null) {
            return 0D;
        }
        return d;
    }

    public static String getString(String str) {
        if (str == null) {
            return "";
        }
        return str;
    }

    public static float round(Float f) {
        if (f == null) {
            return 0F;
        }
        float result = f;
        result = result*100;
        result = (float) Math.round(result);
        result = result/100;
        return result;
    }

    public static float roundMin(Float f) {
        if (f == null) {
            return 0F;
        }
        float result = f;
        result = result*100;
        result = (float) Math.floor(result);
        result = result/100;
        return result;
    }

    public static double round(Double d) {
        if (d == null) {
            return 0F;
        }
        double result = d;
        result = result*100;
        result = (float) Math.round(result);
        result = result/100;
        return result;
    }

    public static double roundMin(Double d) {
        if (d == null) {
            return 0F;
        }
        double result = d;
        result = result*100;
        result = (float) Math.floor(result);
        result = result/100;
        return result;
    }
}
