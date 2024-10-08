package com.lemon.check.Evacheck;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;

import java.util.Locale;

public class LocaleManager {

    public static Context setLocale(Context c) {
        return updateResources(c);
    }

    private static Context updateResources(Context context) {
        Locale locale = getSystemLocale();

        Locale.setDefault(locale);

        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());
        if (Build.VERSION.SDK_INT >= 17) {
            config.setLocale(locale);
            context = context.createConfigurationContext(config);
        } else {
            config.locale = locale;
            res.updateConfiguration(config, res.getDisplayMetrics());
        }
        return context;
    }

    private static Locale getSystemLocale() {
        return Locale.getDefault();
    }
}
