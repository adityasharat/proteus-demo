package com.flipkart.proteus.demo;

import android.content.Context;
import android.support.v7.widget.CardView;

import com.flipkart.android.proteus.view.ProteusView;
import com.flipkart.android.proteus.view.manager.ProteusViewManager;

/**
 * ProteusCardView
 *
 * @author aditya.sharat
 */

public class ProteusCardView extends CardView implements ProteusView {

    public ProteusCardView(Context context) {
        super(context);
    }

    private ProteusViewManager viewManager;

    @Override
    public ProteusViewManager getViewManager() {
        return viewManager;
    }

    @Override
    public void setViewManager(ProteusViewManager proteusViewManager) {
        this.viewManager = proteusViewManager;
    }
}
