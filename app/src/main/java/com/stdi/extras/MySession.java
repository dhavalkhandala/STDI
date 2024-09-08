package com.stdi.extras;

import android.content.Context;
import android.content.SharedPreferences;

public class MySession {

    Context context;
    private String IsPurchase = "isPurchase";
    public static final String base64key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAicVy/IiCzoTL6P8wHdbRe4lJpv+kYwBZc8oVHMTCGKP1Oj7cKjE3SQzV4Ks/LtnIDO3vmZfCfR7X9TdI1IQD5tkpVHjpfi5dmRpm8TOaok10DkjJIZ2kJ0tKUsY3YukdDWBe7u9Or63Q+CViPtVcmRaPCixah3LD8TSvhDcFNvxkMxwJe/Pr0UJsFtk/H3aEh+8D6P9q0AZbY4t0DBcHjDX9mMbgwlhsSI0FrqXkgnalHakKVB/k0Wkz2tLHDTj0gGLaPUBHqbr7hn+IAtMS070yoPK+HUjWb7QAHF/qUdYc6uG4uzsLX8BuprJeEEHgndJT/pqR9eziANr1UqZ+QQIDAQAB";

    public MySession(Context context) {
        this.context = context;
    }

    public void setUserPurchased(boolean isPurchase) {
        if (context != null) {
            SharedPreferences sharedPreferences = context.getSharedPreferences("MY_PREFERENCE", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(IsPurchase, isPurchase);
            editor.apply();
        }
    }

    public boolean isUserPurchased() {
        if (context != null) {
            SharedPreferences sharedPreferences = context.getSharedPreferences("MY_PREFERENCE", Context.MODE_PRIVATE);
            return sharedPreferences.getBoolean(IsPurchase, false);
        } else {
            return false;
        }
    }
}
