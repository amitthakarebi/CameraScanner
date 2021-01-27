package com.amitthakare.camerascanner;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class PdfQualityDialog extends AppCompatDialogFragment {

    RadioButton poorQuality, normalQuality, bestQuality;
    String quality="Null";
    //step 2
    PdfQualityDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.pdf_quality_design,null);
        builder.setView(view)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (poorQuality.isChecked())
                        {
                            quality = "poor";
                        }else if (normalQuality.isChecked())
                        {
                            quality = "normal";
                        }else if (bestQuality.isChecked())
                        {
                            quality = "best";
                        }

                        listener.getQualityOfPdf(quality);

                    }
                });

        poorQuality = view.findViewById(R.id.poorQualityPDF);
        normalQuality = view.findViewById(R.id.normalQualityPDF);
        bestQuality = view.findViewById(R.id.bestQualityPDF);

        return builder.create();
    }

    //Step 3
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (PdfQualityDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement PdfQualityDialogListener");
        }
    }

    //1st Step
    public interface PdfQualityDialogListener{
        void getQualityOfPdf(String quality);
    }
}
