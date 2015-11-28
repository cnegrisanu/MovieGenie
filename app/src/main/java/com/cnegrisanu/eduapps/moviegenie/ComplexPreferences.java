package com.cnegrisanu.eduapps.moviegenie;
/*
* Copyright Felipe Silvestre.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
* */
import java.lang.reflect.Type;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ComplexPreferences {

    private static ComplexPreferences complexPreferences;
    private Context context;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private static Gson GSON = new Gson();
    Type typeOfObject = new TypeToken<Object>() {
    }.getType();

    private ComplexPreferences(Context context, String namePreferences, int mode) {
        this.context = context;
        if (namePreferences == null || namePreferences.equals("")) {
            namePreferences = "complex_preferences";
        }
        preferences = context.getSharedPreferences(namePreferences, mode);
        editor = preferences.edit();
        editor.apply();
    }

    public static ComplexPreferences getComplexPreferences(Context context,
                                                           String namePreferences) {

        if (complexPreferences == null) {
            complexPreferences = new ComplexPreferences(context,
                    namePreferences, Context.MODE_PRIVATE);
        }

        return complexPreferences;
    }

    public void putObject(String key, Object object) {
        if(object == null){
            throw new IllegalArgumentException("object is null");
        }

        if(key.equals("")){
            throw new IllegalArgumentException("key is empty or null");
        }

        editor.putString(key, GSON.toJson(object));
    }

    public Map<String,?> getAll() {
        return preferences.getAll();
    }

    public void remove(String key) {

        if(key.equals("")){
            throw new IllegalArgumentException("key is empty or null");
        }

        editor.remove(key);
    }

    public void commit() {
        editor.commit();
    }

    public <T> T getObject(String key, Class<T> a) {

        String gson = preferences.getString(key, null);
        if (gson == null) {
            return null;
        } else {
            try{
                return GSON.fromJson(gson, a);
            } catch (Exception e) {
                throw new IllegalArgumentException("Object storaged with key " + key + " is instanceof other class");
            }
        }
    }


}