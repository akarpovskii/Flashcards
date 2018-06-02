package com.gmail.ooad.symbolskeyboard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gmail.ooad.symbolskeyboard.listeners.OnSymbolBackspaceClickListener;
import com.gmail.ooad.symbolskeyboard.listeners.OnSymbolClickListener;
import com.gmail.ooad.symbolskeyboard.listeners.OnSymbolLongClickListener;
import com.gmail.ooad.symbolskeyboard.listeners.RepeatListener;
import com.gmail.ooad.symbolskeyboard.model.IRecentSymbolsManager;
import com.gmail.ooad.symbolskeyboard.model.ISymbolsPackagesProvider;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.util.concurrent.TimeUnit;


@SuppressLint("ViewConstructor")
class SymbolsView extends LinearLayout {
    private static final long INITIAL_INTERVAL = TimeUnit.SECONDS.toMillis(1) / 2;
    private static final int NORMAL_INTERVAL = 50;

    @ColorInt private final int mThemeIconColor;

    private final SymbolsPagerAdapter mSymbolsPagerAdapter;

    @Nullable private OnSymbolBackspaceClickListener onEmojiBackspaceClickListener;

    private int mSymbolsTabLastSelectedIndex = 0;

    @SuppressLint("ClickableViewAccessibility")
    public SymbolsView(final Context context, final ISymbolsPackagesProvider provider,
                       final OnSymbolClickListener onSymbolClickListener,
                       final OnSymbolLongClickListener onSymbolLongClickListener,
                       final IRecentSymbolsManager recentSymbolsManager) {
        super(context);

        View.inflate(context, R.layout.symbols_view, this);

        setOrientation(VERTICAL);
        setBackgroundColor(ContextCompat.getColor(context, R.color.symbol_background));

        mThemeIconColor = ContextCompat.getColor(context, R.color.symbol_icons);
        final TypedValue value = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.colorAccent, value, true);

        // backspace
        ImageButton backspace = findViewById(R.id.symbols_backspace);
        backspace.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_backspace));
        backspace.setColorFilter(mThemeIconColor, PorterDuff.Mode.SRC_IN);

        backspace.setOnTouchListener(new RepeatListener(INITIAL_INTERVAL, NORMAL_INTERVAL, view -> {
            if (onEmojiBackspaceClickListener != null) {
                onEmojiBackspaceClickListener.onSymbolBackspaceClick(view);
            }
        }));

        final ViewPager symbolsPager = findViewById(R.id.symbols_pager);

        mSymbolsPagerAdapter = new SymbolsPagerAdapter(provider, onSymbolClickListener,
                onSymbolLongClickListener, recentSymbolsManager);
        symbolsPager.setAdapter(mSymbolsPagerAdapter);

        final int startIndex = mSymbolsPagerAdapter.numberOfRecentSymbols() > 0 ? 0 : 1;
        symbolsPager.setCurrentItem(startIndex);

        final SmartTabLayout symbolsTabs = findViewById(R.id.symbols_tabs);
        symbolsTabs.setCustomTabView(mTabProvider);
        symbolsTabs.setOnPageChangeListener(mOnPageChangeListener);
        symbolsTabs.setViewPager(symbolsPager);
    }

    public void setOnSymbolBackspaceClickListener(@Nullable final OnSymbolBackspaceClickListener onSymbolBackspaceClickListener) {
        this.onEmojiBackspaceClickListener = onSymbolBackspaceClickListener;
    }

    @SuppressWarnings("FieldCanBeLocal")
    private final ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (mSymbolsTabLastSelectedIndex != position) {
                if (position == 0) {
                    mSymbolsPagerAdapter.invalidateRecentSymbols();
                }

                mSymbolsTabLastSelectedIndex = position;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @SuppressWarnings("FieldCanBeLocal")
    private final SmartTabLayout.TabProvider mTabProvider = new SmartTabLayout.TabProvider() {
        @Override
        public View createTabView(ViewGroup container, int position, PagerAdapter adapter) {
            if (position == 0) {
                ImageButton recent = (ImageButton) LayoutInflater.from(container.getContext()).
                        inflate(R.layout.symbols_category, container, false);
                recent.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_access_time));
                int padding = getResources().getDimensionPixelOffset(R.dimen.symbol_tab_recent_padding);
                recent.setPadding(padding, recent.getPaddingTop(), padding, recent.getPaddingBottom());
                recent.setColorFilter(mThemeIconColor, PorterDuff.Mode.SRC_IN);
                return recent;
            } else {
                return createDefaultTabView(adapter.getPageTitle(position));
            }
        }

        // Fetched from SmartTabLayout
        private TextView createDefaultTabView(CharSequence title) {
            TextView textView = new TextView(getContext());
            textView.setGravity(Gravity.CENTER);
            textView.setText(title);
            textView.setTextColor(ContextCompat.getColor(getContext(), R.color.symbol_icons));
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.symbols_tab_text_size));
            textView.setTypeface(Typeface.DEFAULT_BOLD);
            textView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));

            TypedValue outValue = new TypedValue();
            getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground,
                    outValue, true);
            textView.setBackgroundResource(outValue.resourceId);

            textView.setAllCaps(true);

            int tabViewTextHorizontalPadding = (int) getResources().getDimension(R.dimen.symbol_tab_text_horizontal_padding);
            textView.setPadding(
                    tabViewTextHorizontalPadding, 0,
                    tabViewTextHorizontalPadding, 0);

            return textView;
        }

    };
}

