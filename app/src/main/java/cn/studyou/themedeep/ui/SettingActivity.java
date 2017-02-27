package cn.studyou.themedeep.ui;

import android.content.DialogInterface;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;

import cn.studyou.themedeep.R;
import cn.studyou.themedeep.base.ThemeActivity;
import cn.studyou.themedeep.utils.ColorPaletteUtils;
import cn.studyou.themedeep.utils.PreferenceUtils;
import uz.shift.colorpicker.LineColorPicker;
import uz.shift.colorpicker.OnColorChangedListener;

public class SettingActivity extends ThemeActivity {

    private SwitchCompat swNavBar;
    private LinearLayout swNavBarStatus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
    }

    private Toolbar toolbar;
    private PreferenceUtils SP;
    private Button changeThemeButton;
    private TextView toolbarTitle;
    private SwitchCompat setTranslucentStatusBar;
    private LinearLayout translucentStatusBarStatus;


    private void initView() {
        SP = PreferenceUtils.getInstance(getApplicationContext());
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        changeThemeButton = (Button) findViewById(R.id.changeTheme);
        setTranslucentStatusBar = (SwitchCompat) findViewById(R.id.set_translucent_status_bar);
        translucentStatusBarStatus = (LinearLayout) findViewById(R.id.translucent_status_bar_status);
        swNavBar = (SwitchCompat) findViewById(R.id.set_colored_navBar);
        swNavBarStatus = (LinearLayout) findViewById(R.id.set_colored_navBar_status);
        changeThemeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                primaryColorPiker();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            translucentStatusBarStatus.setVisibility(View.VISIBLE);
            swNavBarStatus.setVisibility(View.VISIBLE);
        }
        setTranslucentStatusBar.setChecked(SP.getBoolean(getString(R.string.preference_translucent_status_bar), true));
        setTranslucentStatusBar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SP.putBoolean(getString(R.string.preference_translucent_status_bar), isChecked);
                updateTheme();
                setStatusBarColor();
                updateSwitchColor(setTranslucentStatusBar, getAccentColor());
            }
        });

        toolbarTitle.setText(R.string.setting);
        toolbar.setNavigationIcon(getToolbarIcon(GoogleMaterial.Icon.gmd_arrow_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        swNavBar.setChecked(SP.getBoolean(getString(R.string.preference_colored_nav_bar), false));
        swNavBar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SP.putBoolean(getString(R.string.preference_colored_nav_bar), isChecked);
                updateTheme();
                updateSwitchColor(swNavBar, getAccentColor());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setNavigationBarColor(isNavigationBarColored() ? getPrimaryColor() : ContextCompat.getColor(getApplicationContext(), R.color.md_black_1000));
                }
            }
        });
    }


    private void primaryColorPiker() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this, getDialogStyle());

        final View dialogLayout = getLayoutInflater().inflate(R.layout.color_piker_primary, null);
        final LineColorPicker colorPicker = (LineColorPicker) dialogLayout.findViewById(R.id.color_picker_primary);
        final LineColorPicker colorPicker2 = (LineColorPicker) dialogLayout.findViewById(R.id.color_picker_primary_2);
        final TextView dialogTitle = (TextView) dialogLayout.findViewById(R.id.cp_primary_title);
        CardView dialogCardView = (CardView) dialogLayout.findViewById(R.id.cp_primary_card);
        dialogCardView.setCardBackgroundColor(getCardBackgroundColor());

        colorPicker.setColors(ColorPaletteUtils.getBaseColors(getApplicationContext()));
        for (int i : colorPicker.getColors())
            for (int i2 : ColorPaletteUtils.getColors(getBaseContext(), i))
                if (i2 == getPrimaryColor()) {
                    colorPicker.setSelectedColor(i);
                    colorPicker2.setColors(ColorPaletteUtils.getColors(getBaseContext(), i));
                    colorPicker2.setSelectedColor(i2);
                    break;
                }

        dialogTitle.setBackgroundColor(getPrimaryColor());

        colorPicker.setOnColorChangedListener(new OnColorChangedListener() {
            @Override
            public void onColorChanged(int c) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (isTranslucentStatusBar()) {
                        getWindow().setStatusBarColor(ColorPaletteUtils.getObscuredColor(getPrimaryColor()));
                    } else getWindow().setStatusBarColor(c);
                }

                toolbar.setBackgroundColor(c);
                dialogTitle.setBackgroundColor(c);
                colorPicker2.setColors(ColorPaletteUtils.getColors(getApplicationContext(), colorPicker.getColor()));
                colorPicker2.setSelectedColor(colorPicker.getColor());
            }
        });
        colorPicker2.setOnColorChangedListener(new OnColorChangedListener() {
            @Override
            public void onColorChanged(int c) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (isTranslucentStatusBar()) {
                        getWindow().setStatusBarColor(ColorPaletteUtils.getObscuredColor(c));
                    } else getWindow().setStatusBarColor(c);
                    if (isNavigationBarColored())
                        getWindow().setNavigationBarColor(c);
                    else
                        getWindow().setNavigationBarColor(ContextCompat.getColor(getApplicationContext(), R.color.md_black_1000));
                }
                toolbar.setBackgroundColor(c);
                dialogTitle.setBackgroundColor(c);
            }
        });
        dialogBuilder.setView(dialogLayout);

        dialogBuilder.setNeutralButton(getString(R.string.cancel).toUpperCase(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (isTranslucentStatusBar()) {
                        getWindow().setStatusBarColor(ColorPaletteUtils.getObscuredColor(getPrimaryColor()));
                    } else getWindow().setStatusBarColor(getPrimaryColor());
                }
                toolbar.setBackgroundColor(getPrimaryColor());
                dialog.cancel();
            }
        });

        dialogBuilder.setPositiveButton(getString(R.string.ok_action).toUpperCase(), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                SP.putInt(getString(R.string.preference_primary_color), colorPicker2.getColor());
                updateTheme();
                setNavBarColor();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (isTranslucentStatusBar()) {
                        getWindow().setStatusBarColor(ColorPaletteUtils.getObscuredColor(getPrimaryColor()));
                    } else {
                        getWindow().setStatusBarColor(getPrimaryColor());
                    }
                }
            }
        });

        dialogBuilder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (isTranslucentStatusBar()) {
                        getWindow().setStatusBarColor(ColorPaletteUtils.getObscuredColor(getPrimaryColor()));
                    } else getWindow().setStatusBarColor(getPrimaryColor());
                    if (isNavigationBarColored())
                        getWindow().setNavigationBarColor(getPrimaryColor());
                    else
                        getWindow().setNavigationBarColor(ContextCompat.getColor(getApplicationContext(), R.color.md_black_1000));
                }
                toolbar.setBackgroundColor(getPrimaryColor());

            }
        });
        dialogBuilder.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateNowTheme();
    }

    private void updateNowTheme() {
        toolbar.setPopupTheme(getPopupToolbarStyle());
        toolbar.setBackgroundColor(getPrimaryColor());
        toolbar.setTitle("");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (isTranslucentStatusBar()) {
                getWindow().setStatusBarColor(ColorPaletteUtils.getObscuredColor(getPrimaryColor()));
            } else {
                getWindow().setStatusBarColor(getPrimaryColor());
            }
            getWindow().setNavigationBarColor(isNavigationBarColored() ? getPrimaryColor() : ContextCompat.getColor(getApplicationContext(), R.color.md_black_1000));
        }
    }
}
