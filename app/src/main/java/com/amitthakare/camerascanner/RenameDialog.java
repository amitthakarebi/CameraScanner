package com.amitthakare.camerascanner;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class RenameDialog extends AppCompatDialogFragment {

    EditText editTextDocumentName;
    //Step2 : create the listener for the interface so that we can call and pass the string to the interface.
    RenameDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.create_document_dialog,null);
        builder.setView(view)
                .setTitle("Rename Document")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Rename", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String documentName = editTextDocumentName.getText().toString();
                        //Step4: it will call the appyText method through interface and pass the string value.
                        //Step5 : implement DocumentFolderDialog.DocumentFolderDialogListener to get the text in any activity.
                        listener.renameDocument(documentName);
                    }
                });

        editTextDocumentName = view.findViewById(R.id.createDocumentEditText);

        return builder.create();
    }

    //Step3 : here we will attach the context of activity (mainActivity) to the listerner so that
    //it will pass the text to the activity
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (RenameDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement DocumentFolderDialogListerner");
        }
    }

    //Step1 : here we just create the interface with method which wil we implement in activity to get the string
    public interface RenameDialogListener{
        void renameDocument(String documentName);
    }
}
