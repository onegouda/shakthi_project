package com.example.shakthi_project;

import android.content.Context;
import android.graphics.Rect;
import android.widget.Toast;

import com.google.firebase.ml.vision.text.FirebaseVisionText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

public class AadhaarProccessing {
    public HashMap<String, String> processExtractTextForFrontPic(FirebaseVisionText text, Context context) {
        List<FirebaseVisionText.TextBlock> blocks = text.getTextBlocks();

        if (blocks.size() == 0) {
            Toast.makeText(context, "No Text Found:", Toast.LENGTH_SHORT).show();
            return null;
        }
        TreeMap<String, String> map = new TreeMap<>();

        for (FirebaseVisionText.TextBlock block : text.getTextBlocks()) {
            for (FirebaseVisionText.Line line : block.getLines()) {
                Rect rect = line.getBoundingBox();
                String y = String.valueOf(rect.exactCenterY());
                String lineText = line.getText().toLowerCase();
                map.put(y, lineText);
            }
        }
        List<String> orderedData = new ArrayList<>(map.values());
        HashMap<String, String> dataMap = new HashMap<>();

        int i = 0;
        String regx = "\\d\\d\\d\\d([,\\s])?\\d\\d\\d\\d.*";
        for (i = 0; i < orderedData.size(); i++) {
            if (orderedData.get(i).matches(regx)) {
                dataMap.put("name", orderedData.get(i));
                break;
            }
        }

        //setting gender first
        for (i = 0; i < orderedData.size(); i++) {
            if (orderedData.get(i).contains("female")) {
                dataMap.put("gender", "Female");
                break;
            } else if (orderedData.get(i).contains("male")) {
                dataMap.put("gender", "Male");
                break;

            }
        }

        return dataMap;
    }
}
