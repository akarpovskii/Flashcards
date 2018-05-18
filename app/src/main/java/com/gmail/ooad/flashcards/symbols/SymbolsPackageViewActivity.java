package com.gmail.ooad.flashcards.symbols;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.gmail.ooad.flashcards.R;
import com.gmail.ooad.symbolskeyboard.SymbolsGridView;
import com.gmail.ooad.symbolskeyboard.model.ISymbol;

public class SymbolsPackageViewActivity extends AppCompatActivity {

    private ISymbolsPackageData mPackage;

    private SymbolsGridView mGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symbols_package_view);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        mPackage = intent.getParcelableExtra("package");

        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
        }

        setTitle(mPackage.getName());

        mGridView = findViewById(R.id.symbols_grid_view);
        mGridView.init(null, (symbolTextView, iSymbol) -> onDeleteSymbol(iSymbol),
                new KeyboardPackageAdapter(mPackage));

        FloatingActionButton add_record = findViewById(R.id.add_record);
        add_record.setOnClickListener(ignore -> onAddSymbol());
    }

    private void onDeleteSymbol(ISymbol symbol) {
        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.menu_action_delete_packages_title))
                .setMessage(getResources().getString(R.string.delete_symbol_message))
                .setPositiveButton(getResources().getString(R.string.menu_action_delete_button_yes),
                        (dialog, which) -> {
                            SymbolsController.GetInstance()
                                    .removeSymbol(mPackage.getName(), new Symbol(symbol.getUnicode()));
                            syncWitchDatabase();
                        })
                .setNegativeButton(getResources()
                        .getString(R.string.menu_action_delete_button_no), null)
                .show();
    }

    private void syncWitchDatabase() {
        mPackage = SymbolsController.GetInstance().getPackage(mPackage.getName());
        mGridView.updateSymbols(new KeyboardPackageAdapter(mPackage));
    }

    private void showAddDialog(final View view, final TextView nameView) {
        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.add_symbol_title))
                .setView(view)
                .setPositiveButton(R.string.confirm, (dialog, id) -> {
                    String symbolStr  = nameView.getText().toString();

                    if (symbolStr.length() == 0) {
                        Toast.makeText(getApplicationContext(),
                                "Please, enter the name",
                                Toast.LENGTH_LONG).show();
                        return;
                    }

                    SymbolsController.GetInstance().addSymbol(mPackage.getName(), new Symbol(symbolStr));

                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);

                    syncWitchDatabase();
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    protected void onAddSymbol() {
        View view = getLayoutInflater().inflate(R.layout.dialog_add_symbol_dialog, null);
        final TextView nameView = view.findViewById(R.id.symbol);

        showAddDialog(view, nameView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}
