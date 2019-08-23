//Copyright (c) 2017. 章钦豪. All rights reserved.
package com.kunfei.bookshelf.help;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.provider.Settings;
import android.util.DisplayMetrics;

import com.kunfei.bookshelf.MApplication;
import com.kunfei.bookshelf.utils.BitmapUtil;

import org.antlr.v4.runtime.misc.IntegerList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.kunfei.bookshelf.widget.my_page.PageLoader.DEFAULT_MARGIN_WIDTH;

public class ReadBookControl {
    private static final int DEFAULT_BG = 0;
    private int textDrawableIndex = DEFAULT_BG;
    private List<Map<String, Integer>> textDrawable;
    private Bitmap bgBitmap;
    private int screenDirection;
    private int progressDisplay;
    private int speechRate;
    private boolean speechRateFollowSys;
    private int textSize;
    private int titleSize;
    private int textColor;
    private boolean bgIsColor;
    private int bgColor;
    private float lineMultiplier;
    private float paragraphSize;
    private int pageMode;
    private Boolean hideStatusBar;
    private Boolean hideNavigationBar;
    private String fontPath;
    private String  chapterFontPath;
    private int textConvert;
    private int navBarColor;
    private Boolean textBold;
    private Boolean canClickTurn;
    private Boolean canKeyTurn;
    private Boolean readAloudCanKeyTurn;
    private int clickSensitivity;
    private Boolean clickAllNext;
    private Boolean showTitle;
    private Boolean showTimeBattery;
    private Boolean showLine;
    private Boolean darkStatusIcon;
    private int indent;
    private int screenTimeOut;
    private int paddingLeft;
    private int paddingTop;
    private int paddingRight;
    private int paddingBottom;
    private int tipPaddingLeft;
    private int tipPaddingTop;
    private int tipPaddingRight;
    private int tipPaddingBottom;
    private float textLetterSpacing;
    private float boldSize;
    private int styleDefault;
    private Boolean noPicSearch;
    private  int speechPitch;

    private  int longPressSetting;

    private Boolean loadingAmin;

    private Boolean showReadingNav;

    private Boolean leftBrightness;

    private  float  firstPageMarginTop;

    private  float  firstPageMarginButtom;

    private SharedPreferences preferences;


    private Map<String,String> bookSpecStyle;

    private static ReadBookControl readBookControl;

    public static ReadBookControl getInstance() {
        if (readBookControl == null) {
            synchronized (ReadBookControl.class) {
                if (readBookControl == null) {
                    readBookControl = new ReadBookControl();
                }
            }
        }
        return readBookControl;
    }

    private ReadBookControl() {
        preferences = MApplication.getConfigPreferences();
        initTextDrawable();
        updateReaderSettings();
    }

    void updateReaderSettings() {
        this.hideStatusBar = preferences.getBoolean("hide_status_bar", false);//顶部信号，电池，时间等信息
        this.hideNavigationBar = preferences.getBoolean("hide_navigation_bar", true);//控制底部三个按钮是否在看书时显示，默认为不显示
        this.indent = preferences.getInt("indent", 2);
        this.textSize = preferences.getInt("textSize", 20);
        this.titleSize = preferences.getInt("titleSize", 20);

        this.canClickTurn = preferences.getBoolean("canClickTurn", true);
        this.canKeyTurn = preferences.getBoolean("canKeyTurn", true);
        this.readAloudCanKeyTurn = preferences.getBoolean("readAloudCanKeyTurn", false);
        this.lineMultiplier = preferences.getFloat("lineMultiplier", 10);//行间距
        this.paragraphSize = preferences.getFloat("paragraphSize", 20);//看书时的段间距
        this.clickSensitivity = preferences.getInt("clickSensitivity", 50) > 100
                ? 50 : preferences.getInt("clickSensitivity", 50);
        this.clickAllNext = preferences.getBoolean("clickAllNext", false);//点击时总是到下一页
        this.fontPath = preferences.getString("fontPath", null);
        this.chapterFontPath = preferences.getString("chapterFontPath", null);
        this.textConvert = preferences.getInt("textConvertInt", 0);
        this.textBold = preferences.getBoolean("textBold", false);
        this.speechRate = preferences.getInt("speechRate", 10);
        this.speechRateFollowSys = preferences.getBoolean("speechRateFollowSys", true);
        this.showTitle = preferences.getBoolean("showTitle", true);
        this.showTimeBattery = preferences.getBoolean("showTimeBattery", true);
        this.showLine = preferences.getBoolean("showLine", false);
        this.screenTimeOut = preferences.getInt("screenTimeOut", 0);
        this.paddingLeft = preferences.getInt("paddingLeft", DEFAULT_MARGIN_WIDTH);
        this.paddingTop = preferences.getInt("paddingTop", 0);
        this.paddingRight = preferences.getInt("paddingRight", DEFAULT_MARGIN_WIDTH);
        this.paddingBottom = preferences.getInt("paddingBottom", 0);
        this.tipPaddingLeft = preferences.getInt("tipPaddingLeft", DEFAULT_MARGIN_WIDTH);
        this.tipPaddingTop = preferences.getInt("tipPaddingTop", 0);
        this.tipPaddingRight = preferences.getInt("tipPaddingRight", DEFAULT_MARGIN_WIDTH);
        this.tipPaddingBottom = preferences.getInt("tipPaddingBottom", 0);
        this.pageMode = preferences.getInt("pageMode", 4);
        this.screenDirection = preferences.getInt("screenDirection", 0);
        this.navBarColor = preferences.getInt("navBarColorInt", 0);
        this.textLetterSpacing = preferences.getFloat("textLetterSpacing", 0);
        this.boldSize = preferences.getFloat("boldSize", 0);
        this.styleDefault = preferences.getInt("styleDefault", 1);
        this.progressDisplay = preferences.getInt("progressDisplay", 0);

        this.noPicSearch = preferences.getBoolean("noPicSearch", false);
        this.speechPitch = preferences.getInt("speechPitch", 10);

        this.longPressSetting  = preferences.getInt("longPressSetting", 3);//默认1500毫秒


        this.loadingAmin = preferences.getBoolean("loadingAmin", false);
        this.showReadingNav = preferences.getBoolean("showReadingNav", false);

        this.firstPageMarginTop = preferences.getFloat("firstPageMarginTop", 300);

        this.firstPageMarginButtom = preferences.getFloat("firstPageMarginButtom", 200);


        initTextDrawableIndex();
    }

    //阅读背景
    private void initTextDrawable() {
        if (null == textDrawable) {
            textDrawable = new ArrayList<>();
            Map<String, Integer> temp1 = new HashMap<>();
            temp1.put("textColor", Color.parseColor("#000000"));
            temp1.put("bgIsColor", 1);
            temp1.put("textBackground", Color.parseColor("#ffffff"));
            temp1.put("darkStatusIcon", 1);
            textDrawable.add(temp1);

            Map<String, Integer> temp2 = new HashMap<>();
            temp2.put("textColor", Color.parseColor("#5E432E"));
            temp2.put("bgIsColor", 1);
            temp2.put("textBackground", Color.parseColor("#C6BAA1"));
            temp2.put("darkStatusIcon", 1);
            textDrawable.add(temp2);

            Map<String, Integer> temp3 = new HashMap<>();
            temp3.put("textColor", Color.parseColor("#22482C"));
            temp3.put("bgIsColor", 1);
            temp3.put("textBackground", Color.parseColor("#E1F1DA"));
            temp3.put("darkStatusIcon", 1);
            textDrawable.add(temp3);

            Map<String, Integer> temp4 = new HashMap<>();
            temp4.put("textColor", Color.parseColor("#FFFFFF"));
            temp4.put("bgIsColor", 1);
            temp4.put("textBackground", Color.parseColor("#015A86"));
            temp4.put("darkStatusIcon", 0);
            textDrawable.add(temp4);

            Map<String, Integer> temp5 = new HashMap<>();
            temp5.put("textColor", Color.parseColor("#808080"));
            temp5.put("bgIsColor", 1);
            temp5.put("textBackground", Color.parseColor("#000000"));
            temp5.put("darkStatusIcon", 0);
            textDrawable.add(temp5);
        }
    }

    public void initTextDrawableIndex() {


        if(bookSpecStyle!=null ){
            String str = DEFAULT_BG+"";
            if (getIsNightTheme()){
                if(bookSpecStyle.get("textDrawableIndexNight")!=null){
                    str = bookSpecStyle.get("textDrawableIndexNight").toString();
                }else{
                    str = "4";
                }
            }else{
                if(bookSpecStyle.get("textDrawableIndex")!=null){
                    str = bookSpecStyle.get("textDrawableIndex").toString();
                }else{
                    str = "1";
                }
            }

            if (str.indexOf(".") > 0) {
                str = str.replaceAll("0+?$", "");//去掉多余的0
                str = str.replaceAll("[.]$", "");//如最后一位是.则去掉
            }

            textDrawableIndex = Integer.parseInt(str);
        }else{
            if (getIsNightTheme()) {
                textDrawableIndex = preferences.getInt("textDrawableIndexNight", 4);
            } else {
                textDrawableIndex = preferences.getInt("textDrawableIndex", DEFAULT_BG);
            }
        }

        if (textDrawableIndex == -1) {
            textDrawableIndex = DEFAULT_BG;
        }


        initPageStyle();
        setTextDrawable();
    }

    @SuppressWarnings("ConstantConditions")
    private void initPageStyle() {
        if (getBgCustom(textDrawableIndex) == 2 && getBgPath(textDrawableIndex) != null) {
            bgIsColor = false;
            String bgPath = getBgPath(textDrawableIndex);
            Resources resources = MApplication.getInstance().getResources();
            DisplayMetrics dm = resources.getDisplayMetrics();
            int width = dm.widthPixels;
            int height = dm.heightPixels;
            bgBitmap = BitmapUtil.getFitSampleBitmap(bgPath, width, height);
            if (bgBitmap != null) {
                return;
            }
        } else if (getBgCustom(textDrawableIndex) == 1) {
            bgIsColor = true;
            bgColor = getBgColor(textDrawableIndex);
            return;
        }
        bgIsColor = true;
        bgColor = textDrawable.get(textDrawableIndex).get("textBackground");
    }

    public void setTextDrawable() {
        darkStatusIcon = getDarkStatusIcon(textDrawableIndex);
        textColor = getTextColor(textDrawableIndex);
    }

    public Boolean getNoPicSearch() {
        return preferences.getBoolean("noPicSearch",false);
    }

    public void setNoPicSearch(Boolean noPicSearch) {

        this.noPicSearch = noPicSearch;
        preferences.edit()
                .putBoolean("noPicSearch", noPicSearch)
                .apply();

    }

    public int getSpeechPitch() {
        return preferences.getInt("speechPitch",10);
    }

    public void setSpeechPitch(int speechPitch) {

        this.speechPitch = speechPitch;
        preferences.edit()
                .putInt("speechPitch", speechPitch)
                .apply();

    }


    public Boolean getLoadingAmin() {
        return preferences.getBoolean("loadingAmin",false);
    }

    public void setLoadingAmin(Boolean loadingAmin) {
        this.loadingAmin = loadingAmin;
        preferences.edit()
                .putBoolean("loadingAmin", loadingAmin)
                .apply();
    }

    public Boolean getLeftBrightness() {
        return preferences.getBoolean("leftBrightness",false);
    }

    public void setLeftBrightness(Boolean leftBrightness) {
        this.leftBrightness = leftBrightness;
        preferences.edit()
                .putBoolean("leftBrightness", leftBrightness)
                .apply();
    }


    public Boolean getShowReadingNav() {
        return preferences.getBoolean("showReadingNav",false);
    }

    public void setShowReadingNav(Boolean showReadingNav) {
        this.showReadingNav = showReadingNav;
        preferences.edit()
                .putBoolean("showReadingNav", showReadingNav)
                .apply();
    }





    public int getLongPressSetting() {
        return preferences.getInt("longPressSetting",3);
    }

    public void setLongPressSetting(int longPressSetting) {
        this.longPressSetting = longPressSetting;
        preferences.edit()
                .putInt("longPressSetting", longPressSetting)
                .apply();
    }

    public int getTextColor(int textDrawableIndex) {

        if(bookSpecStyle!=null && bookSpecStyle.get("textColor" + textDrawableIndex)!=null){
            String str = bookSpecStyle.get("textColor" + textDrawableIndex).toString();
            if (str.indexOf(".") > 0) {
                str = str.replaceAll("0+?$", "");//去掉多余的0
                str = str.replaceAll("[.]$", "");//如最后一位是.则去掉
            }

            return Integer.parseInt(str);

        }

        if (preferences.getInt("textColor" + textDrawableIndex, 0) != 0) {
            return preferences.getInt("textColor" + textDrawableIndex, 0);
        } else {
            return getDefaultTextColor(textDrawableIndex);
        }

    }

    public void setTextColor(int textDrawableIndex, int textColor) {
        preferences.edit()
                .putInt("textColor" + textDrawableIndex, textColor)
                .apply();
    }

    @SuppressWarnings("ConstantConditions")
    public Drawable getBgDrawable(int textDrawableIndex, Context context, int width, int height) {
        int color;
        try {
            switch (getBgCustom(textDrawableIndex)) {
                case 2:
                    Bitmap bitmap = BitmapUtil.getFitSampleBitmap(getBgPath(textDrawableIndex), width, height);
                    if (bitmap != null) {
                        return new BitmapDrawable(context.getResources(), bitmap);
                    }
                    break;
                case 1:
                    color = getBgColor(textDrawableIndex);
                    return new ColorDrawable(color);
            }
            if (textDrawable.get(textDrawableIndex).get("bgIsColor") != 0) {
                color = textDrawable.get(textDrawableIndex).get("textBackground");
                return new ColorDrawable(color);
            } else {
                return getDefaultBgDrawable(textDrawableIndex, context);
            }
        } catch (Exception e) {
            if (textDrawable.get(textDrawableIndex).get("bgIsColor") != 0) {
                color = textDrawable.get(textDrawableIndex).get("textBackground");
                return new ColorDrawable(color);
            } else {
                return getDefaultBgDrawable(textDrawableIndex, context);
            }
        }
    }

    @SuppressWarnings("ConstantConditions")
    public Drawable getDefaultBgDrawable(int textDrawableIndex, Context context) {
        if (textDrawable.get(textDrawableIndex).get("bgIsColor") != 0) {
            return new ColorDrawable(textDrawable.get(textDrawableIndex).get("textBackground"));
        } else {
            return context.getResources().getDrawable(getDefaultBg(textDrawableIndex));
        }
    }

    public int getBgCustom(int textDrawableIndex) {

        if(bookSpecStyle!=null && bookSpecStyle.get("bgCustom" + textDrawableIndex)!=null){
            String str = bookSpecStyle.get("bgCustom" + textDrawableIndex).toString();
            if (str.indexOf(".") > 0) {
                str = str.replaceAll("0+?$", "");//去掉多余的0
                str = str.replaceAll("[.]$", "");//如最后一位是.则去掉
            }

            return Integer.parseInt(str);

        }

        return preferences.getInt("bgCustom" + textDrawableIndex, 0);
    }

    public void setBgCustom(int textDrawableIndex, int bgCustom) {
        preferences.edit()
                .putInt("bgCustom" + textDrawableIndex, bgCustom)
                .apply();
    }

    public String getBgPath(int textDrawableIndex) {
        if(bookSpecStyle!=null && bookSpecStyle.get("bgPath" + textDrawableIndex)!=null){
            String str = bookSpecStyle.get("bgPath" + textDrawableIndex).toString();
            return str;
        }

        return preferences.getString("bgPath" + textDrawableIndex, null);
    }

    public void setBgPath(int textDrawableIndex, String bgUri) {
        preferences.edit()
                .putString("bgPath" + textDrawableIndex, bgUri)
                .apply();
    }

    @SuppressWarnings("ConstantConditions")
    public int getDefaultTextColor(int textDrawableIndex) {
        return textDrawable.get(textDrawableIndex).get("textColor");
    }

    @SuppressWarnings("ConstantConditions")
    private int getDefaultBg(int textDrawableIndex) {
        return textDrawable.get(textDrawableIndex).get("textBackground");
    }

    public int getBgColor(int index) {

        if(bookSpecStyle!=null && bookSpecStyle.get("bgColor" + index)!=null){
            String str = bookSpecStyle.get("bgColor" + index).toString();
            if (str.indexOf(".") > 0) {
                str = str.replaceAll("0+?$", "");//去掉多余的0
                str = str.replaceAll("[.]$", "");//如最后一位是.则去掉
            }

            return Integer.parseInt(str);

        }
        return preferences.getInt("bgColor" + index, Color.parseColor("#1e1e1e"));
    }

    public void setBgColor(int index, int bgColor) {
        preferences.edit()
                .putInt("bgColor" + index, bgColor)
                .apply();
    }

    public boolean getIsNightTheme() {
        return MApplication.getInstance().isNightTheme();
    }

    public boolean getImmersionStatusBar() {
        return preferences.getBoolean("immersionStatusBar", true);
    }

    public void setImmersionStatusBar(boolean immersionStatusBar) {
        preferences.edit()
                .putBoolean("immersionStatusBar", immersionStatusBar)
                .apply();
    }

    public int getTextSize() {
        if(bookSpecStyle!=null&& bookSpecStyle.get("textSize")!=null){

            String str = bookSpecStyle.get("textSize").toString();
            if(str.indexOf(".") > 0){
                str = str.replaceAll("0+?$", "");//去掉多余的0
                str = str.replaceAll("[.]$", "");//如最后一位是.则去掉
            }

           return Integer.parseInt(str);
        }
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
        preferences.edit()
                .putInt("textSize", textSize)
                .apply();
    }


    public void setTitleSize(int titleSize) {
        this.titleSize = titleSize;
        preferences.edit()
                .putInt("titleSize", titleSize)
                .apply();
    }

    public int getTitleSize() {
        if(bookSpecStyle!=null&& bookSpecStyle.get("titleSize")!=null){

            String str = bookSpecStyle.get("titleSize").toString();
            if(str.indexOf(".") > 0){
                str = str.replaceAll("0+?$", "");//去掉多余的0
                str = str.replaceAll("[.]$", "");//如最后一位是.则去掉
            }

            return Integer.parseInt(str);
        }
        return titleSize;
    }




    public float getBoldSize() {
        if(bookSpecStyle!=null && bookSpecStyle.get("boldSize")!=null){

            String str = bookSpecStyle.get("boldSize").toString();
           // if(str.indexOf(".") > 0){
         //       str = str.replaceAll("0+?$", "");//去掉多余的0
          //      str = str.replaceAll("[.]$", "");//如最后一位是.则去掉
         //   }

            return Float.parseFloat(str);
        }

        return boldSize;
    }

    public void setBoldSize(float boldSize) {
        this.boldSize = boldSize;
        SharedPreferences.Editor editor = preferences.edit();
        editor.putFloat("boldSize", boldSize);
        editor.apply();
    }

    public int getTextColor() {
        return textColor;
    }

    public boolean bgIsColor() {
        return bgIsColor;
    }

    public Drawable getTextBackground(Context context) {
        if (bgIsColor) {
            return new ColorDrawable(bgColor);
        }
        return new BitmapDrawable(context.getResources(), bgBitmap);
    }

    public int getBgColor() {
        return bgColor;
    }

    public boolean bgBitmapIsNull() {
        return bgBitmap == null || bgBitmap.isRecycled();
    }

    public Bitmap getBgBitmap() {
        return bgBitmap.copy(Bitmap.Config.ARGB_8888, true);
    }

    public int getTextDrawableIndex() {
        if(bookSpecStyle!=null ){
            String str = DEFAULT_BG+"";
            if (getIsNightTheme()){
                if(bookSpecStyle.get("textDrawableIndexNight")!=null){
                    str = bookSpecStyle.get("textDrawableIndexNight").toString();
                }
            }else{
                if(bookSpecStyle.get("textDrawableIndex")!=null){
                    str = bookSpecStyle.get("textDrawableIndex").toString();
                }
            }

            if (str.indexOf(".") > 0) {
                str = str.replaceAll("0+?$", "");//去掉多余的0
                str = str.replaceAll("[.]$", "");//如最后一位是.则去掉
            }

            return Integer.parseInt(str);
        }

        return textDrawableIndex;
    }

    public void setTextDrawableIndex1(int textDrawableIndex) {
        this.textDrawableIndex = textDrawableIndex;
        setTextDrawable();
    }

    public void setTextDrawableIndex(int textDrawableIndex) {
        this.textDrawableIndex = textDrawableIndex;
        if (getIsNightTheme()) {
            preferences.edit()
                    .putInt("textDrawableIndexNight", textDrawableIndex)
                    .apply();
        } else {
            preferences.edit()
                    .putInt("textDrawableIndex", textDrawableIndex)
                    .apply();
        }
        setTextDrawable();
    }

    public void setTextConvert(int textConvert) {
        this.textConvert = textConvert;
        preferences.edit()
                .putInt("textConvertInt", textConvert)
                .apply();
    }

    public void setNavBarColor(int navBarColor) {
        this.navBarColor = navBarColor;
        preferences.edit()
                .putInt("navBarColorInt", navBarColor)
                .apply();
    }

    public int getNavBarColor() {
        return navBarColor;
    }


    public void setTextBold(boolean textBold) {
        this.textBold = textBold;
        preferences.edit()
                .putBoolean("textBold", textBold)
                .apply();
    }

    public void setReadBookFont(String fontPath) {
        this.fontPath = fontPath;
        preferences.edit()
                .putString("fontPath", fontPath)
                .apply();
    }

    public String getFontPath() {
        if(bookSpecStyle!=null && bookSpecStyle.get("fontPath")!=null){
            String str = bookSpecStyle.get("fontPath").toString();
            return str;
        }
        return fontPath;
    }

    public void setChapterBookFont(String chapterFontPath) {
        this.chapterFontPath = chapterFontPath;
        preferences.edit()
                .putString("chapterFontPath", fontPath)
                .apply();
    }

    public String getChapterFontPath() {
        if(bookSpecStyle!=null && bookSpecStyle.get("chapterFontPath")!=null){
            String str = bookSpecStyle.get("chapterFontPath").toString();
            return str;
        }
        return chapterFontPath;
    }

    public int getTextConvert() {
        return textConvert == -1 ? 2 : textConvert;
    }

    public Boolean getTextBold() {
        return textBold;
    }

    public Boolean getCanKeyTurn(Boolean isPlay) {
        if (!canKeyTurn) {
            return false;
        } else if (readAloudCanKeyTurn) {
            return true;
        } else {
            return !isPlay;
        }
    }

    public Boolean getCanKeyTurn() {
        return canKeyTurn;
    }

    public void setCanKeyTurn(Boolean canKeyTurn) {
        this.canKeyTurn = canKeyTurn;
        preferences.edit()
                .putBoolean("canKeyTurn", canKeyTurn)
                .apply();
    }

    public Boolean getAloudCanKeyTurn() {
        return readAloudCanKeyTurn;
    }

    public void setAloudCanKeyTurn(Boolean canAloudKeyTurn) {
        this.readAloudCanKeyTurn = canAloudKeyTurn;
        preferences.edit()
                .putBoolean("readAloudCanKeyTurn", canAloudKeyTurn)
                .apply();
    }

    public Boolean getCanClickTurn() {
        return canClickTurn;
    }

    public void setCanClickTurn(Boolean canClickTurn) {
        this.canClickTurn = canClickTurn;
        preferences.edit()
                .putBoolean("canClickTurn", canClickTurn)
                .apply();
    }

    public float getTextLetterSpacing() {
        return textLetterSpacing;
    }

    public void setTextLetterSpacing(float textLetterSpacing) {
        this.textLetterSpacing = textLetterSpacing;
        preferences.edit()
                .putFloat("textLetterSpacing", textLetterSpacing)
                .apply();
    }

    public float getLineMultiplier() {
        if(bookSpecStyle!=null && bookSpecStyle.get("lineMultiplier")!=null){

            String str = bookSpecStyle.get("lineMultiplier").toString();
             if(str.indexOf(".") > 0){
                   str = str.replaceAll("0+?$", "");//去掉多余的0
                 str = str.replaceAll("[.]$", "");//如最后一位是.则去掉
              }

            return Float.parseFloat(str);
        }
        return lineMultiplier;
    }

    public void setLineMultiplier(float lineMultiplier) {
        this.lineMultiplier = lineMultiplier;
        preferences.edit()
                .putFloat("lineMultiplier", lineMultiplier)
                .apply();
    }


   // private  float  firstPageMarginTop;

   // private  float  firstPageMarginButtom;

    public float getFirstPageMarginButtom() {
        if(bookSpecStyle!=null && bookSpecStyle.get("firstPageMarginButtom")!=null){

            String str = bookSpecStyle.get("firstPageMarginButtom").toString();
            if(str.indexOf(".") > 0){
                str = str.replaceAll("0+?$", "");//去掉多余的0
                str = str.replaceAll("[.]$", "");//如最后一位是.则去掉
            }

            return Float.parseFloat(str);
        }
        return firstPageMarginButtom;
    }

    public void setFirstPageMarginButtom(float firstPageMarginButtom) {
        this.firstPageMarginButtom = firstPageMarginButtom;
        preferences.edit()
                .putFloat("firstPageMarginButtom", firstPageMarginButtom)
                .apply();
    }

    public float getFirstPageMarginTop() {
        if(bookSpecStyle!=null && bookSpecStyle.get("firstPageMarginTop")!=null){

            String str = bookSpecStyle.get("firstPageMarginTop").toString();
            if(str.indexOf(".") > 0){
                str = str.replaceAll("0+?$", "");//去掉多余的0
                str = str.replaceAll("[.]$", "");//如最后一位是.则去掉
            }

            return Float.parseFloat(str);
        }
        return firstPageMarginTop;
    }

    public void setFirstPageMarginTop(float firstPageMarginTop) {
        this.firstPageMarginTop = firstPageMarginTop;
        preferences.edit()
                .putFloat("firstPageMarginTop", firstPageMarginTop)
                .apply();
    }

    public float getParagraphSize() {
        if(bookSpecStyle!=null && bookSpecStyle.get("paragraphSize")!=null){

            String str = bookSpecStyle.get("paragraphSize").toString();
            if(str.indexOf(".") > 0){
                str = str.replaceAll("0+?$", "");//去掉多余的0
                str = str.replaceAll("[.]$", "");//如最后一位是.则去掉
            }

            return Float.parseFloat(str);
        }
        return paragraphSize;
    }

    public void setParagraphSize(float paragraphSize) {
        this.paragraphSize = paragraphSize;
        preferences.edit()
                .putFloat("paragraphSize", paragraphSize)
                .apply();
    }

    public int getClickSensitivity() {
        return clickSensitivity;
    }

    public void setClickSensitivity(int clickSensitivity) {
        this.clickSensitivity = clickSensitivity;
        preferences.edit()
                .putInt("clickSensitivity", clickSensitivity)
                .apply();
    }

    public Boolean getClickAllNext() {
        return clickAllNext;
    }

    public void setClickAllNext(Boolean clickAllNext) {
        this.clickAllNext = clickAllNext;
        preferences.edit()
                .putBoolean("clickAllNext", clickAllNext)
                .apply();
    }

    public int getSpeechRate() {
        return speechRate;
    }

    public void setSpeechRate(int speechRate) {
        this.speechRate = speechRate;
        preferences.edit()
                .putInt("speechRate", speechRate)
                .apply();
    }

    public boolean isSpeechRateFollowSys() {
        return speechRateFollowSys;
    }

    public void setSpeechRateFollowSys(boolean speechRateFollowSys) {
        this.speechRateFollowSys = speechRateFollowSys;
        preferences.edit()
                .putBoolean("speechRateFollowSys", speechRateFollowSys)
                .apply();
    }

    public Boolean getShowTitle() {
        return showTitle;
    }

    public void setShowTitle(Boolean showTitle) {
        this.showTitle = showTitle;
        preferences.edit()
                .putBoolean("showTitle", showTitle)
                .apply();
    }

    public Boolean getShowTimeBattery() {
        return showTimeBattery;
    }

    public void setShowTimeBattery(Boolean showTimeBattery) {
        this.showTimeBattery = showTimeBattery;
        preferences.edit()
                .putBoolean("showTimeBattery", showTimeBattery)
                .apply();
    }

    public Boolean getHideStatusBar() {
        return hideStatusBar;
    }

    public void setHideStatusBar(Boolean hideStatusBar) {
        this.hideStatusBar = hideStatusBar;
        preferences.edit()
                .putBoolean("hide_status_bar", hideStatusBar)
                .apply();
    }

    public Boolean getHideNavigationBar() {
        return hideNavigationBar;
    }

    public void setHideNavigationBar(Boolean hideNavigationBar) {
        this.hideNavigationBar = hideNavigationBar;
        preferences.edit()
                .putBoolean("hide_navigation_bar", hideNavigationBar)
                .apply();
    }

    public Boolean getShowLine() {
        return showLine;
    }

    public void setShowLine(Boolean showLine) {
        this.showLine = showLine;
        preferences.edit()
                .putBoolean("showLine", showLine)
                .apply();
    }

    public boolean getDarkStatusIcon() {
        return darkStatusIcon;
    }

    @SuppressWarnings("ConstantConditions")
    public boolean getDarkStatusIcon(int textDrawableIndex) {

        if(bookSpecStyle!=null && bookSpecStyle.get("darkStatusIcon" + textDrawableIndex)!=null){
            String  str =(bookSpecStyle.get("darkStatusIcon" + textDrawableIndex));
            if(str.equals("true")){
                return true;
            }
            return false;
        }

        return preferences.getBoolean("darkStatusIcon" + textDrawableIndex, textDrawable.get(textDrawableIndex).get("darkStatusIcon") != 0);
    }

    public void setDarkStatusIcon(int textDrawableIndex, Boolean darkStatusIcon) {
        preferences.edit()
                .putBoolean("darkStatusIcon" + textDrawableIndex, darkStatusIcon)
                .apply();
    }

    public int getScreenTimeOut() {
        return screenTimeOut;
    }

    public void setScreenTimeOut(int screenTimeOut) {
        this.screenTimeOut = screenTimeOut;
        preferences.edit()
                .putInt("screenTimeOut", screenTimeOut)
                .apply();
    }

    public int getPaddingLeft() {
        if (bookSpecStyle != null && bookSpecStyle.get("paddingLeft") != null) {
            String str = bookSpecStyle.get("paddingLeft").toString();
            if (str.indexOf(".") > 0) {
                str = str.replaceAll("0+?$", "");//去掉多余的0
                str = str.replaceAll("[.]$", "");//如最后一位是.则去掉
            }
            return Integer.parseInt(str);
        }
        return paddingLeft;
    }

    public void setPaddingLeft(int paddingLeft) {
        this.paddingLeft = paddingLeft;
        preferences.edit()
                .putInt("paddingLeft", paddingLeft)
                .apply();
    }

    public int getPaddingTop() {
        if (bookSpecStyle != null && bookSpecStyle.get("paddingTop") != null) {
            String str = bookSpecStyle.get("paddingTop").toString();
            if (str.indexOf(".") > 0) {
                str = str.replaceAll("0+?$", "");//去掉多余的0
                str = str.replaceAll("[.]$", "");//如最后一位是.则去掉
            }
            return Integer.parseInt(str);
        }
        return  paddingTop;
    }

    public void setPaddingTop(int paddingTop) {
        this.paddingTop = paddingTop;
        preferences.edit()
                .putInt("paddingTop", paddingTop)
                .apply();
    }

    public int getPaddingRight() {
        if (bookSpecStyle != null && bookSpecStyle.get("paddingRight") != null) {
            String str = bookSpecStyle.get("paddingRight").toString();
            if (str.indexOf(".") > 0) {
                str = str.replaceAll("0+?$", "");//去掉多余的0
                str = str.replaceAll("[.]$", "");//如最后一位是.则去掉
            }
            return Integer.parseInt(str);
        }
        return paddingRight;
    }

    public void setPaddingRight(int paddingRight) {
        this.paddingRight = paddingRight;
        preferences.edit()
                .putInt("paddingRight", paddingRight)
                .apply();
    }


    public int getProgressDisplay() {
        return progressDisplay;
    }

    public void setProgressDisplay(int progressDisplay) {
        this.progressDisplay = progressDisplay;
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("progressDisplay", progressDisplay);
        editor.apply();
    }

    public int getPaddingBottom() {
        if (bookSpecStyle != null && bookSpecStyle.get("paddingBottom") != null) {
            String str = bookSpecStyle.get("paddingBottom").toString();
            if (str.indexOf(".") > 0) {
                str = str.replaceAll("0+?$", "");//去掉多余的0
                str = str.replaceAll("[.]$", "");//如最后一位是.则去掉
            }
            return Integer.parseInt(str);
        }
        return paddingBottom;
    }

    public void setPaddingBottom(int paddingBottom) {
        this.paddingBottom = paddingBottom;
        preferences.edit()
                .putInt("paddingBottom", paddingBottom)
                .apply();
    }

    public int getTipPaddingLeft() {
        if (bookSpecStyle != null && bookSpecStyle.get("tipPaddingLeft") != null) {
            String str = bookSpecStyle.get("tipPaddingLeft").toString();
            if (str.indexOf(".") > 0) {
                str = str.replaceAll("0+?$", "");//去掉多余的0
                str = str.replaceAll("[.]$", "");//如最后一位是.则去掉
            }
            return Integer.parseInt(str);
        }
        return tipPaddingLeft;
    }

    public void setTipPaddingLeft(int tipPaddingLeft) {
        this.tipPaddingLeft = tipPaddingLeft;
        preferences.edit()
                .putInt("tipPaddingLeft", tipPaddingLeft)
                .apply();
    }

    public int getTipPaddingTop() {
        if (bookSpecStyle != null && bookSpecStyle.get("tipPaddingTop") != null) {
            String str = bookSpecStyle.get("tipPaddingTop").toString();
            if (str.indexOf(".") > 0) {
                str = str.replaceAll("0+?$", "");//去掉多余的0
                str = str.replaceAll("[.]$", "");//如最后一位是.则去掉
            }
            return Integer.parseInt(str);
        }
        return tipPaddingTop;
    }

    public void setTipPaddingTop(int tipPaddingTop) {
        this.tipPaddingTop = tipPaddingTop;
        preferences.edit()
                .putInt("tipPaddingTop", tipPaddingTop)
                .apply();
    }

    public int getTipPaddingRight() {
        if (bookSpecStyle != null && bookSpecStyle.get("tipPaddingRight") != null) {
            String str = bookSpecStyle.get("tipPaddingRight").toString();
            if (str.indexOf(".") > 0) {
                str = str.replaceAll("0+?$", "");//去掉多余的0
                str = str.replaceAll("[.]$", "");//如最后一位是.则去掉
            }
            return Integer.parseInt(str);
        }
        return tipPaddingRight;
    }

    public void setTipPaddingRight(int tipPaddingRight) {
        this.tipPaddingRight = tipPaddingRight;
        preferences.edit()
                .putInt("tipPaddingRight", tipPaddingRight)
                .apply();
    }

    public int getTipPaddingBottom() {
        if (bookSpecStyle != null && bookSpecStyle.get("tipPaddingBottom") != null) {
            String str = bookSpecStyle.get("tipPaddingBottom").toString();
            if (str.indexOf(".") > 0) {
                str = str.replaceAll("0+?$", "");//去掉多余的0
                str = str.replaceAll("[.]$", "");//如最后一位是.则去掉
            }
            return Integer.parseInt(str);
        }
        return tipPaddingBottom;
    }

    public void setTipPaddingBottom(int tipPaddingBottom) {
        this.tipPaddingBottom = tipPaddingBottom;
        preferences.edit()
                .putInt("tipPaddingBottom", tipPaddingBottom)
                .apply();
    }

    public int getPageMode() {
        if (bookSpecStyle != null && bookSpecStyle.get("pageMode") != null) {
            String str = bookSpecStyle.get("pageMode").toString();
            if (str.indexOf(".") > 0) {
                str = str.replaceAll("0+?$", "");//去掉多余的0
                str = str.replaceAll("[.]$", "");//如最后一位是.则去掉
            }
            return Integer.parseInt(str);
        }
        return pageMode;
    }

    public void setPageMode(int pageMode) {
        this.pageMode = pageMode;
        preferences.edit()
                .putInt("pageMode", pageMode)
                .apply();
    }

    public int getScreenDirection() {
        return screenDirection;
    }

    public void setScreenDirection(int screenDirection) {
        this.screenDirection = screenDirection;
        preferences.edit()
                .putInt("screenDirection", screenDirection)
                .apply();
    }

    public void setIndent(int indent) {
        this.indent = indent;
        preferences.edit()
                .putInt("indent", indent)
                .apply();
    }

    public int getIndent() {
        if (bookSpecStyle != null && bookSpecStyle.get("indent") != null) {
            String str = bookSpecStyle.get("indent").toString();
            if (str.indexOf(".") > 0) {
                str = str.replaceAll("0+?$", "");//去掉多余的0
                str = str.replaceAll("[.]$", "");//如最后一位是.则去掉
            }
            return Integer.parseInt(str);
        }
        return indent;
    }

    public int getLight() {
        return preferences.getInt("light", getScreenBrightness());
    }

    public void setLight(int light) {
        preferences.edit()
                .putInt("light", light)
                .apply();
    }

    public Boolean getLightFollowSys() {
        return preferences.getBoolean("lightFollowSys", true);
    }

    public void setLightFollowSys(boolean isFollowSys) {
        preferences.edit()
                .putBoolean("lightFollowSys", isFollowSys)
                .apply();
    }

    private int getScreenBrightness() {
        int value = 0;
        ContentResolver cr = MApplication.getInstance().getContentResolver();
        try {
            value = Settings.System.getInt(cr, Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException ignored) {
        }
        return value;
    }

    public boolean disableScrollClickTurn() {
        return preferences.getBoolean("disableScrollClickTurn", false);
    }


    public int getStyleDefault() {
        if(bookSpecStyle!=null && bookSpecStyle.get("styleDefault")!=null){

            String str = bookSpecStyle.get("styleDefault").toString();
            if (str.indexOf(".") > 0) {
                str = str.replaceAll("0+?$", "");//去掉多余的0
                str = str.replaceAll("[.]$", "");//如最后一位是.则去掉
            }

            return Integer.parseInt(str);
        }
        return styleDefault;
    }

    public void setStyleDefault(int styleDefault) {
        this.styleDefault = styleDefault;
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("styleDefault", styleDefault);
        editor.apply();
    }


    public void setBookSpecStyle(Map<String,String> m){
        this.bookSpecStyle = m;
    }

    public Map<String,String> getBookSpecStyle() {
        return bookSpecStyle;
    }


}
