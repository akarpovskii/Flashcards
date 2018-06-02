package com.gmail.ooad.symbolskeyboard.listeners;

import android.support.annotation.NonNull;

import com.gmail.ooad.symbolskeyboard.SymbolTextView;
import com.gmail.ooad.symbolskeyboard.model.ISymbol;

public interface OnSymbolLongClickListener {
  void onSymbolsLongClick(@NonNull SymbolTextView imageView, @NonNull ISymbol symbol);
}
