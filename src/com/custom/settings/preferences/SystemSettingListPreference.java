package com.custom.settings.preferences;

import android.content.Context;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.AttributeSet;

import androidx.preference.ListPreference;

public class SystemSettingListPreference extends ListPreference {

    private boolean mAutoSummary = false;

    public SystemSettingListPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public SystemSettingListPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SystemSettingListPreference(Context context) {
        super(context);
    }

    @Override
    public void setValue(String value) {
        super.setValue(value);

        if (mAutoSummary || TextUtils.isEmpty(getSummary())) {
            setSummary(getEntry(), true);
        }

        // Save selected value to Settings.System
        Settings.System.putString(
                getContext().getContentResolver(),
                getKey(),
                value
        );
    }

    @Override
    public void setSummary(CharSequence summary) {
        setSummary(summary, false);
    }

    private void setSummary(CharSequence summary, boolean autoSummary) {
        mAutoSummary = autoSummary;
        super.setSummary(summary);
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        String value;

        if (restoreValue) {
            value = Settings.System.getString(
                    getContext().getContentResolver(),
                    getKey()
            );

            if (value == null) {
                value = (String) defaultValue;
            }
        } else {
            value = (String) defaultValue;
        }

        setValue(value);
    }

    public int getIntValue(int defValue) {
        String value = getValue();

        if (value == null) {
            return defValue;
        }

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defValue;
        }
    }
}
