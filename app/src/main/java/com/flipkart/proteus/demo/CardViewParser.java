package com.flipkart.proteus.demo;

import android.content.res.ColorStateList;
import android.support.v7.widget.CardView;
import android.view.ViewGroup;

import com.flipkart.android.proteus.parser.Attributes;
import com.flipkart.android.proteus.parser.Parser;
import com.flipkart.android.proteus.parser.WrappableParser;
import com.flipkart.android.proteus.processor.ColorResourceProcessor;
import com.flipkart.android.proteus.processor.DimensionAttributeProcessor;
import com.flipkart.android.proteus.toolbox.Styles;
import com.flipkart.android.proteus.view.ProteusView;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * CardViewParser
 *
 * @author aditya.sharat
 */

public class CardViewParser extends WrappableParser<CardView> {

    public CardViewParser(Parser<CardView> wrappedParser) {
        super(wrappedParser);
    }

    @Override
    public ProteusView createView(ViewGroup parent, JsonObject layout, JsonObject data, Styles styles, int index) {
        return new ProteusCardView(parent.getContext());
    }

    @Override
    protected void prepareHandlers() {
        super.prepareHandlers();

        addHandler(new Attributes.Attribute("cardBackgroundColor"), new ColorResourceProcessor<CardView>() {
            @Override
            public void setColor(CardView view, int color) {
                view.setCardBackgroundColor(color);
            }

            @Override
            public void setColor(CardView view, ColorStateList colors) {

            }
        });

        addHandler(new Attributes.Attribute("cardElevation"), new DimensionAttributeProcessor<CardView>() {
            @Override
            public void setDimension(float dimension, CardView view, String key, JsonElement value) {
                view.setCardElevation(dimension);
            }
        });

        addHandler(new Attributes.Attribute("cardCornerRadius"), new DimensionAttributeProcessor<CardView>() {
            @Override
            public void setDimension(float dimension, CardView view, String key, JsonElement value) {
                view.setRadius(dimension);
            }
        });

        addHandler(new Attributes.Attribute("contentPadding"), new DimensionAttributeProcessor<CardView>() {
            @Override
            public void setDimension(float dimension, CardView view, String key, JsonElement value) {
                view.setContentPadding((int) dimension, (int) dimension, (int) dimension, (int) dimension);
            }
        });

    }
}
