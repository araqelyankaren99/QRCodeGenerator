package am.lavr.admin.qrcodebarcodescannerandgenerator.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.regex.Matcher;

import am.lavr.admin.qrcodebarcodescannerandgenerator.R;

public class EmailFragment extends BaseFragment implements View.OnClickListener {

    private View view;
    private EditText edtEmail;
    private EditText edtSubject;
    private EditText edtMessage;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_email, container, false);
        findViews(view);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_download:
            case R.id.btn_create:
                if (isEmail(edtEmail.getText().toString())) {
                    strQR = messageToString();
                    super.onClick(v);
                }
                break;
            default:
                super.onClick(v);
        }
    }

    @Override
    void addContentView() {
        flEnterContent.removeAllViews();
        flEnterContent.addView(view);
    }

    private void findViews(View view) {
        edtEmail = (EditText) view.findViewById(R.id.edt_email_email);
        edtSubject = (EditText) view.findViewById(R.id.edt_email_subject);
        edtMessage = (EditText) view.findViewById(R.id.edt_email_message);
    }

    private boolean isEmail(String string) {
        Matcher m = Patterns.EMAIL_ADDRESS.matcher(string);
        if (m.find() && m.group().length() == string.length()){
            return true;
        }
        edtEmail.setError(getResources().getString(R.string.error_mail));
        return false;
    }

    private String messageToString() {
        StringBuilder builder = new StringBuilder();
        builder.append("mailto:")
                .append(edtEmail.getText().toString());
        if(edtSubject.getText().toString().length() > 0) {
            builder.append("?subject=")
                    .append(edtSubject.getText().toString());
            if(edtMessage.getText().toString().length() > 0) {
                builder.append("&body=")
                        .append(edtMessage.getText().toString());
            }
        } else if(edtMessage.getText().toString().length() > 0) {
            builder.append("?body=")
                    .append(edtMessage.getText().toString());
        }
        return builder.toString();
    }
}
