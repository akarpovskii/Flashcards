package com.gmail.ooad.flashcards.symbols;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.gmail.ooad.flashcards.R;
import com.gmail.ooad.flashcards.utils.ShareableEditableListViewActivity;

import java.util.ArrayList;

/*
 * Created by akarpovskii on 09.05.18.
 */
public class SymbolsPackagesListViewActivity extends ShareableEditableListViewActivity {

    private static final int SyncList = 100;

    protected ArrayList<ISymbolsPackageData> mPackages = new ArrayList<>();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SyncList:
                if (resultCode == RESULT_OK) {
                    syncListWitchDatabase();
                }
        }
    }

    @Override
    protected void syncListWitchDatabase() {
        mRecordAdapter.clear();
        mPackages.clear();

        ArrayList<SymbolsPackageData> packs = SymbolsController.GetInstance().getPackageList();
        mPackages.addAll(packs);

        for (ISymbolsPackageData pack:
                mPackages) {
            mRecordAdapter.add(pack.getName());
        }
    }

    @Override
    protected void onAddRecord() {
        View view = getLayoutInflater().inflate(R.layout.dialog_add_package_dialog, null);
        final TextView nameView = view.findViewById(R.id.package_name);

        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.add_symbols_package_title))
                .setView(view)
                .setPositiveButton(R.string.confirm, (dialog, id) -> {
                    String name = nameView.getText().toString();
                    SymbolsPackageData data = new SymbolsPackageData(name, null);

                    if (name.length() == 0) {
                        Toast.makeText(getApplicationContext(),
                                R.string.error_enter_the_name,
                                Toast.LENGTH_LONG).show();
                        return;
                    }

                    if (SymbolsController.GetInstance().hasPackage(name)) {
                        Toast.makeText(getApplicationContext(),
                                R.string.error_package_already_exists,
                                Toast.LENGTH_LONG).show();
                    } else {
                        SymbolsController.GetInstance().addPackage(data);
                        syncListWitchDatabase();
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    @Override
    protected void onEditRecord() {
        View view = getLayoutInflater().inflate(R.layout.dialog_add_package_dialog, null);
        final TextView nameView = view.findViewById(R.id.package_name);

        final ISymbolsPackageData pack = mPackages.get(mSelected.get(0));
        nameView.setText(pack.getName());

        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.add_symbols_package_title))
                .setView(view)
                .setPositiveButton(R.string.confirm, (dialog, id) -> {
                    String name = nameView.getText().toString();
                    SymbolsPackageData data = new SymbolsPackageData(name, pack.getSymbols());

                    if (name.length() == 0) {
                        Toast.makeText(getApplicationContext(),
                                R.string.error_enter_the_name,
                                Toast.LENGTH_LONG).show();
                        return;
                    }

                    String oldName = pack.getName();
                    SymbolsController.GetInstance().updatePackage(data, oldName);
                    syncListWitchDatabase();
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    @Override
    protected void onDeleteRecord() {
        String title = getResources().getString(R.string.menu_action_delete_message_pre);
        title += " " + String.valueOf(mSelected.size()) + " ";
        if (mSelected.size() == 1) {
            title += getResources().getString(R.string.menu_action_delete_symbols_packages_message_post_singular);
        } else {
            title += getResources().getString(R.string.menu_action_delete_symbols_packages_message_post_plural);
        }

        // Make a copy, because mSelected will be cleared after method return
        final ArrayList<Integer> selected = new ArrayList<>(mSelected);

        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.menu_action_delete_packages_title))
                .setMessage(title)
                .setPositiveButton(R.string.menu_action_delete_button_yes,
                        (dialog, which) -> {
                            for (int pos :
                                    selected) {
                                String packageName = mRecordAdapter.getItem(pos);
                                assert packageName != null;
                                SymbolsController.GetInstance().removePackage(packageName);
                            }
                            syncListWitchDatabase();
                        })
                .setNegativeButton(R.string.menu_action_delete_button_no, null)
                .show();
    }

    @Override
    protected void onViewRecord(int position) {
        String name = mRecordAdapter.getItem(position);
        assert name != null;
        ISymbolsPackageData data = mPackages.get(position);

        Intent intent = new Intent(this, SymbolsPackageViewActivity.class);
        intent.putExtra("package", data);
        startActivityForResult(intent, SyncList);
    }

    @Override
    protected int getColorAccent() {
        return getResources().getColor(R.color.colorAccent);
    }

    @Override
    protected CharSequence getCustomTitle() {
        return getString(R.string.symbols_title);
    }

    @Override
    protected void onShare() {

    }
}
