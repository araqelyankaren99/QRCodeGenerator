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

public class PhoneFragment extends BaseFragment implements View.OnClickListener {

    private View view;
    private EditText edtPhone;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_phone, container, false);
        findViews(view);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_download:
            case R.id.btn_create:
                if (isPhone(edtPhone.getText().toString())) {
                    strQR = phoneToString();
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
        edtPhone = (EditText) view.findViewById(R.id.edt_phone_phone);
    }

    private boolean isPhone(String string) {
        Matcher m = Patterns.PHONE.matcher(string);
        if (m.find() && m.group().length() == string.length()){
            return true;
        }
        edtPhone.setError(getString(R.string.error_phone));
        return false;
    }

    private String phoneToString() {
        String string = "tel:" +
                edtPhone.getText().toString();
        return string;
    }
}
