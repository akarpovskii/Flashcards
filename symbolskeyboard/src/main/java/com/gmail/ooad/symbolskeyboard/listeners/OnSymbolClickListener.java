package com.gmail.ooad.symbolskeyboard.listeners;

import android.support.annotation.NonNull;

import com.gmail.ooad.symbolskeyboard.SymbolTextView;
import com.gmail.ooad.symbolskeyboard.model.ISymbol;

public interface OnSymbolClickListener {
  void onSymbolClick(@NonNull SymbolTextView imageView, @NonNull ISymbol symbol);
}
