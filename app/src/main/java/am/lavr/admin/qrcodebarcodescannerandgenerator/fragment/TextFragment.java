package am.lavr.admin.qrcodebarcodescannerandgenerator.fragment;

import android.graphics.Bitmap;
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

public class TextFragment extends BaseFragment implements View.OnClickListener {

    private View view;
    private EditText edtText;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_text, container, false);
        findViews(view);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_download:
            case R.id.btn_create:
                if (edtText.getText().toString().length() > 0) {
                    strQR = edtText.getText().toString();
                    super.onClick(v);
                } else {
                    edtText.setError(getResources().getString(R.string.error_text));
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
        edtText = (EditText) view.findViewById(R.id.edt_text_text);
    }
}
