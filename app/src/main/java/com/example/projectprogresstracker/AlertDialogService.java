package com.example.projectprogresstracker;

import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

class AlertDialogService {
    private static final AlertDialogService ourInstance = new AlertDialogService();

    static AlertDialogService getInstance() {
        return ourInstance;
    }

    private AlertDialogService() {

    }

    public void showAlertDialogToRename(Context context, String type, final TextView textViewToUpdate, final AlertDialogCallbacks callbacks) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        final EditText editText = new EditText(context);
        alert.setMessage("Enter the new name of the " + type);
        alert.setTitle("Rename Current " + type);

        alert.setView(editText);
        alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String text = editText.getText().toString();
                textViewToUpdate.setText(text);
                callbacks.onPositiveAlertDialogOption(text);
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                callbacks.onNegativeAlertDialogOption();
            }
        });

        alert.show();
    }
}
