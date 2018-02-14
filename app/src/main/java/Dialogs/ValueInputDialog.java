package Dialogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment; // Крайне необходимо для диалогов и фрагментов!!
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.wordpress.ledrunning.currencyrates.R;

import Callbacks.ICustomCallback;

/**
 * Created by Ledrunner on 13.02.2018.
 */

public class ValueInputDialog extends DialogFragment {

    private ICustomCallback myCallback;
    private EditText editText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.enter_value_dialog, null);

        editText = (EditText)view.findViewById(R.id.etInputValue);
        view.findViewById(R.id.btnAccept).setOnClickListener(listener);
        view.findViewById(R.id.btnCancel).setOnClickListener(listener);



        return view;
    }

    View.OnClickListener listener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            switch(v.getId()) {

                case R.id.btnAccept:
                    try {
                        myCallback.resultValue(Double.parseDouble(editText.getText().toString()));
                    }
                    catch(NumberFormatException err) {
                        Toast.makeText(getContext(), "Введите число", Toast.LENGTH_LONG).show();
                        return;
                    }

                case R.id.btnCancel:
                    dismiss();
            }

        }
    };

    public void inputValueDialogInit(ICustomCallback myCallback) {

        this.myCallback = myCallback;
    }
}
