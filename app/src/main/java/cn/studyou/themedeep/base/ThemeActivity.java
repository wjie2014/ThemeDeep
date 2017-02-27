package cn.studyou.themedeep.base;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.SeekBar;

import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.context.IconicsContextWrapper;
import com.mikepenz.iconics.typeface.IIcon;

import java.util.ArrayList;

import cn.studyou.themedeep.R;
import cn.studyou.themedeep.utils.ColorPaletteUtils;
import cn.studyou.themedeep.utils.PreferenceUtils;
import cn.studyou.themedeep.utils.ThemeUtils;

/**
 * 基本功能：主题
 * 创建：王杰
 * 创建时间：2017/2/27
 * 邮箱：w489657152@gmail.com
 */

public class ThemeActivity extends AppCompatActivity {
    private ThemeUtils themeUtils;
    private PreferenceUtils SP;
    private boolean coloredNavBar;
    private boolean obscuredStatusBar;
    private boolean applyThemeImgAct;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SP = PreferenceUtils.getInstance(getApplicationContext());
        themeUtils = new ThemeUtils(getApplicationContext());
    }

    @Override
    public void onResume() {
        super.onResume();
        updateTheme();
    }

    public void updateTheme() {
        themeUtils.updateTheme();
        coloredNavBar = SP.getBoolean(getString(R.string.preference_colored_nav_bar), false);
        obscuredStatusBar = SP.getBoolean(getString(R.string.preference_translucent_status_bar), true);
        applyThemeImgAct = SP.getBoolean(getString(R.string.preference_apply_theme_pager), true);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        // NOTE: icons stuff
        super.attachBaseContext(IconicsContextWrapper.wrap(newBase));
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void setNavBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (isNavigationBarColored()) getWindow().setNavigationBarColor(getPrimaryColor());
            else
                getWindow().setNavigationBarColor(ContextCompat.getColor(getApplicationContext(), R.color.md_black_1000));
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    protected void setStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (isTranslucentStatusBar())
                getWindow().setStatusBarColor(ColorPaletteUtils.getObscuredColor(getPrimaryColor()));
            else
                getWindow().setStatusBarColor(getPrimaryColor());
        }
    }

    protected void setScrollViewColor(ScrollView scr) {
        themeUtils.setScrollViewColor(scr);
    }

    public void setCursorDrawableColor(EditText editText, int color) {
        // TODO: 02/08/16 remove this
        ThemeUtils.setCursorDrawableColor(editText, color);
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void setRecentApp(String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            BitmapDrawable drawable = ((BitmapDrawable) getDrawable(R.mipmap.ic_launcher));
            setTaskDescription(new ActivityManager.TaskDescription(text, drawable.getBitmap(), getPrimaryColor()));
        }
    }


    public boolean isNavigationBarColored() {
        return coloredNavBar;
    }

    public boolean isTranslucentStatusBar() {
        return obscuredStatusBar;
    }

    protected boolean isApplyThemeOnImgAct() {
        return applyThemeImgAct;
    }

    protected boolean isTransparencyZero() {
        return 255 - SP.getInt(getString(R.string.preference_transparency), 0) == 255;
    }

    public int getTransparency() {
        return 255 - SP.getInt(getString(R.string.preference_transparency), 0);
    }

    public void setBaseTheme(int baseTheme, boolean permanent) {
        themeUtils.setBaseTheme(baseTheme, permanent);
    }

    public void themeSeekBar(SeekBar bar) {
        themeUtils.themeSeekBar(bar);
    }

    public int getPrimaryColor() {
        return themeUtils.getPrimaryColor();
    }

    public int getAccentColor() {
        return themeUtils.getAccentColor();
    }

    public int getBaseTheme() {
        return themeUtils.getBaseTheme();
    }

    protected int getBackgroundColor() {
        return themeUtils.getBackgroundColor();
    }

    protected Drawable getPlaceHolder() {
        return themeUtils.getPlaceHolder();
    }

    protected int getInvertedBackgroundColor() {
        return themeUtils.getInvertedBackgroundColor();
    }

    public int getTextColor() {
        return themeUtils.getTextColor();
    }

    public int getSubTextColor() {
        return themeUtils.getSubTextColor();
    }

    public int getCardBackgroundColor() {
        return themeUtils.getCardBackgroundColor();
    }

    public int getIconColor() {
        return themeUtils.getIconColor();
    }

    protected int getDrawerBackground() {
        return themeUtils.getDrawerBackground();
    }

    public int getDialogStyle() {
        return themeUtils.getDialogStyle();
    }

    protected int getPopupToolbarStyle() {
        return themeUtils.getPopupToolbarStyle();
    }

    protected ArrayAdapter<String> getSpinnerAdapter(ArrayList<String> items) {
        return themeUtils.getSpinnerAdapter(items);
    }

    protected int getDefaultThemeToolbarColor3th() {
        return themeUtils.getDefaultThemeToolbarColor3th();
    }

    protected void updateRadioButtonColor(RadioButton radioButton) {
        themeUtils.updateRadioButtonColor(radioButton);
    }

    protected void setRadioTextButtonColor(RadioButton radioButton, int color) {
        themeUtils.setRadioTextButtonColor(radioButton, color);
    }

    public void updateSwitchColor(SwitchCompat sw, int color) {
        themeUtils.updateSwitchColor(sw, color);
    }

    public IconicsDrawable getToolbarIcon(IIcon icon) {
        return themeUtils.getToolbarIcon(icon);
    }


}
