package com.kunfei.bookshelf.data;

/**
 * Copyright (C) 2015 Ari C.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.widget.Filter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kunfei.bookshelf.DbHelper;
import com.kunfei.bookshelf.MApplication;
import com.kunfei.bookshelf.bean.BookInfoBean;
import com.kunfei.bookshelf.bean.BookShelfBean;
import com.kunfei.bookshelf.bean.BookSourceBean;
import com.kunfei.bookshelf.bean.MyFindKindGroupBean;
import com.kunfei.bookshelf.bean.SearchHistoryBean;
import com.kunfei.bookshelf.dao.BookInfoBeanDao;
import com.kunfei.bookshelf.dao.BookShelfBeanDao;
import com.kunfei.bookshelf.dao.BookSourceBeanDao;
import com.kunfei.bookshelf.model.BookSourceManager;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DataHelper {

    private static final String COLORS_FILE_NAME = "colors.json";

    private static List<ColorWrapper> sColorWrappers = new ArrayList<>();

    private static List<ColorSuggestion> sColorSuggestions =
            new ArrayList<>(Arrays.asList(
                    new ColorSuggestion("green"),
                    new ColorSuggestion("blue"),
                    new ColorSuggestion("pink"),
                    new ColorSuggestion("purple"),
                    new ColorSuggestion("brown"),
                    new ColorSuggestion("gray"),
                    new ColorSuggestion("Granny Smith Apple"),
                    new ColorSuggestion("Indigo"),
                    new ColorSuggestion("Periwinkle"),
                    new ColorSuggestion("Mahogany"),
                    new ColorSuggestion("Maize"),
                    new ColorSuggestion("Mahogany"),
                    new ColorSuggestion("Outer Space"),
                    new ColorSuggestion("Melon"),
                    new ColorSuggestion("Yellow"),
                    new ColorSuggestion("Orange"),
                    new ColorSuggestion("Red"),
                    new ColorSuggestion("Orchid")));

    public interface OnFindColorsListener {
        void onResults(List<ColorWrapper> results);
    }

    public interface OnFindSuggestionsListener {
        void onResults(List<ColorSuggestion> results);
    }

    public static List<ColorSuggestion> getHistory(Context context, int count) {

        List<ColorSuggestion> suggestionList = new ArrayList<>();
        ColorSuggestion colorSuggestion;
        for (int i = 0; i < sColorSuggestions.size(); i++) {
            colorSuggestion = sColorSuggestions.get(i);
            colorSuggestion.setIsHistory(true);
            suggestionList.add(colorSuggestion);
            if (suggestionList.size() == count) {
                break;
            }
        }
        return suggestionList;
    }

    public static List<ColorSuggestion> getsColorSuggestions() {
        return sColorSuggestions;
    }

    public static void setsColorSuggestions(List<ColorSuggestion> sColorSuggestions) {
        DataHelper.sColorSuggestions = sColorSuggestions;
    }

    public static List<ColorSuggestion> getKeyWordHistory(Context context, int count) {

        List<ColorSuggestion> suggestionList = new ArrayList<>();
        ColorSuggestion colorSuggestion;


        for (int i = 0; i < sColorSuggestions.size(); i++) {
            colorSuggestion = sColorSuggestions.get(i);
            colorSuggestion.setIsHistory(true);
            suggestionList.add(colorSuggestion);
            if (suggestionList.size() == count) {
                break;
            }
        }
        return suggestionList;
    }



    public static void resetSuggestionsHistory() {
        for (ColorSuggestion colorSuggestion : sColorSuggestions) {
            colorSuggestion.setIsHistory(false);
        }
    }

    public static void findSuggestions(Context context, String query, final int limit, final long simulatedDelay,
                                       final OnFindSuggestionsListener listener) {
        new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                try {
                    Thread.sleep(simulatedDelay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                DataHelper.resetSuggestionsHistory();
                List<ColorSuggestion> suggestionList = new ArrayList<>();
                if (!(constraint == null || constraint.length() == 0)) {

                    for (ColorSuggestion suggestion : sColorSuggestions) {
                        if (suggestion.getBody().toUpperCase()
                                .startsWith(constraint.toString().toUpperCase())) {

                            suggestionList.add(suggestion);
                            if (limit != -1 && suggestionList.size() == limit) {
                                break;
                            }
                        }
                    }
                }

                FilterResults results = new FilterResults();
                Collections.sort(suggestionList, new Comparator<ColorSuggestion>() {
                    @Override
                    public int compare(ColorSuggestion lhs, ColorSuggestion rhs) {
                        return lhs.getIsHistory() ? -1 : 0;
                    }
                });


                //查询书架
                List<BookShelfBean> bookShelfList = DbHelper.getDaoSession().getBookShelfBeanDao().queryBuilder()
                        .orderDesc(BookShelfBeanDao.Properties.FinalDate).limit(10).list();

                for (int i = 0; i < bookShelfList.size(); i++) {
                    BookInfoBean bookInfoBean = DbHelper.getDaoSession().getBookInfoBeanDao().queryBuilder()
                            .where(BookInfoBeanDao.Properties.NoteUrl.eq(bookShelfList.get(i).getNoteUrl()), BookInfoBeanDao.Properties.Name.like("%" + constraint.toString() + "%"))
                            .limit(1).build().unique();

                    if (bookInfoBean != null ) {
                        //bookShelfList.get(i).setBookInfoBean(bookInfoBean);
                        ColorSuggestion ee = new ColorSuggestion(bookInfoBean.getName());
                        ee.setmNoteUrl(bookInfoBean.getNoteUrl());
                        ee.setmIsShelf(true);
                        suggestionList.add(ee);
                    }
                }

                //查询发现
                boolean showAllFind = MApplication.getInstance().getConfigPreferences().getBoolean("showAllFind", true);
                List<BookSourceBean> sourceBeans = new ArrayList<BookSourceBean> ();
                if(showAllFind){
                    sourceBeans =  DbHelper.getDaoSession().getBookSourceBeanDao().queryBuilder()
                            .orderAsc(BookSourceBeanDao.Properties.SerialNumber)
                            .where(BookSourceBeanDao.Properties.BookSourceName.like("%" + constraint.toString() + "%"))
                            .limit(10)
                            .list();
                }else{
                    sourceBeans = DbHelper.getDaoSession().getBookSourceBeanDao().queryBuilder()
                            .where(BookSourceBeanDao.Properties.Enable.eq(true),BookSourceBeanDao.Properties.BookSourceName.like("%" + constraint.toString() + "%"))
                            .orderAsc(BookSourceBeanDao.Properties.SerialNumber)
                            .limit(10)
                            .list();
                }

                for (BookSourceBean sourceBean : sourceBeans) {
                    ColorSuggestion ee = new ColorSuggestion(sourceBean.getBookSourceName());
                    ee.setmTag(sourceBean.getBookSourceUrl());
                    ee.setmIsFind(true);
                    suggestionList.add(ee);
                }


                results.values = suggestionList;
                results.count = suggestionList.size();

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                if (listener != null) {
                    listener.onResults((List<ColorSuggestion>) results.values);
                }
            }
        }.filter(query);

    }


    public static void findColors(Context context, String query, final OnFindColorsListener listener) {
        initColorWrapperList(context);

        new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {


                List<ColorWrapper> suggestionList = new ArrayList<>();

                if (!(constraint == null || constraint.length() == 0)) {

                    for (ColorWrapper color : sColorWrappers) {
                        if (color.getName().toUpperCase()
                                .startsWith(constraint.toString().toUpperCase())) {

                            suggestionList.add(color);
                        }
                    }

                }

                FilterResults results = new FilterResults();
                results.values = suggestionList;
                results.count = suggestionList.size();

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                if (listener != null) {
                    listener.onResults((List<ColorWrapper>) results.values);
                }
            }
        }.filter(query);

    }

    private static void initColorWrapperList(Context context) {

        if (sColorWrappers.isEmpty()) {
            String jsonString = loadJson(context);
            sColorWrappers = deserializeColors(jsonString);
        }
    }

    private static String loadJson(Context context) {

        String jsonString;

        try {
            InputStream is = context.getAssets().open(COLORS_FILE_NAME);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            jsonString = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

        return jsonString;
    }

    private static List<ColorWrapper> deserializeColors(String jsonString) {

        Gson gson = new Gson();

        Type collectionType = new TypeToken<List<ColorWrapper>>() {
        }.getType();
        return gson.fromJson(jsonString, collectionType);
    }

}