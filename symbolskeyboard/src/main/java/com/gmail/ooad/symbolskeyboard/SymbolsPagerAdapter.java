package com.gmail.ooad.symbolskeyboard;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.gmail.ooad.symbolskeyboard.listeners.OnSymbolClickListener;
import com.gmail.ooad.symbolskeyboard.listeners.OnSymbolLongClickListener;
import com.gmail.ooad.symbolskeyboard.model.IRecentSymbolsManager;
import com.gmail.ooad.symbolskeyboard.model.ISymbolsPackage;
import com.gmail.ooad.symbolskeyboard.model.ISymbolsPackagesProvider;

import java.util.ArrayList;

/*
 * Created by akarpovskii on 13.05.18.
 */
public class SymbolsPagerAdapter extends PagerAdapter {
    private final OnSymbolClickListener mListener;

    private final OnSymbolLongClickListener mLongListener;

    private ArrayList<ISymbolsPackage> mPackages;

    private IRecentSymbolsManager mRecentSymbolsManager;
    private RecentSymbolsGridView mRecentSymbolsGridView;

    public SymbolsPagerAdapter(final ISymbolsPackagesProvider provider, final OnSymbolClickListener listener,
                               final OnSymbolLongClickListener longListener,
                               final IRecentSymbolsManager recentSymbolsManager) {
        mListener = listener;
        mLongListener = longListener;
        mPackages = provider.getPackages();
        mRecentSymbolsManager = recentSymbolsManager;
        mRecentSymbolsGridView = null;
    }


    @Override
    public int getCount() {
        return mPackages.size() + 1;    // +1 for recent
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        final View view;
        if (position == 0) {
            view = mRecentSymbolsGridView = new RecentSymbolsGridView(container.getContext())
                    .init(mListener, mLongListener, mRecentSymbolsManager);
        } else {
            view = new SymbolsGridView(container.getContext())
                    .init(mListener, mLongListener, mPackages.get(position - 1));
        }

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "Recent";
        } else {
            return mPackages.get(position - 1).getName();
        }
    }

    int numberOfRecentSymbols() {
        return mRecentSymbolsManager.getRecentSymbols().size();
    }

    void invalidateRecentSymbols() {
        if (mRecentSymbolsGridView != null) {
            mRecentSymbolsGridView.invalidateSymbols();
        }
    }
}
