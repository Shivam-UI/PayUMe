package com.lgt.paykredit.extras;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;

/*import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;*/
import com.lgt.paykredit.Activities.MainActivity;
import com.lgt.paykredit.R;

import java.io.IOException;
import java.io.InputStream;

public  class  Language  {

 /*public   static Translate translate;



    public static String convertLanguage(Context context,String text, String target_language) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try (InputStream is = context.getResources().openRawResource(R.raw.credentials)) {

            //Get credentials:
            final GoogleCredentials myCredentials = GoogleCredentials.fromStream(is);

            //Set credentials and get translate service:
            TranslateOptions translateOptions = TranslateOptions.newBuilder().setCredentials(myCredentials).build();
            translate = translateOptions.getService();

        } catch (IOException ioe) {
            ioe.printStackTrace();

        }
        return  translate(text,target_language);


    }


    public static String translate(String text,String target_language) {
        //Get input text to be translated:

        Translation translation = translate.translate(text, Translate.TranslateOption.targetLanguage(target_language), Translate.TranslateOption.model("base"));
        String  translatedText = translation.getTranslatedText();

        return translatedText;
    }

*/
}
