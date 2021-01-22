package am.lavr.admin.qrcodebarcodescannerandgenerator.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import am.lavr.admin.qrcodebarcodescannerandgenerator.R;

public class WIFIFragment extends BaseFragment implements View.OnClickListener {

    private View view;
    private EditText edtSSID;
    private EditText edtPassword;
    private Spinner spEncryption;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_wifi, container, false);
        findViews(view);
        init();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_download:
            case R.id.btn_create:
                strQR = wifiToString();
                super.onClick(v);
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
        edtSSID = (EditText) view.findViewById(R.id.edt_wifi_ssid);
        edtPassword = (EditText) view.findViewById(R.id.edt_wifi_password);
        spEncryption = (Spinner) view.findViewById(R.id.sp_encryption);
    }

    private void init() {
        String[] data = {"no encryption", "WEP", "WPA/WPA2"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spEncryption.setAdapter(adapter);
        spEncryption.setSelection(0);
    }

    private String wifiToString() {
        StringBuilder builder = new StringBuilder();
        builder.append("WIFI:")
                .append("S:").append(edtSSID.getText().toString()).append(";")
                .append("T:");
        switch (spEncryption.getSelectedItemPosition()) {
            case 0:
                builder.append("nopass");
                break;
            case 1:
                builder.append("WEP");
                break;
            case 2:
                builder.append("WPA");
                break;
        }
        builder.append(";")
                .append("P:").append(edtPassword.getText().toString())
                .append(";;");
        return builder.toString();
    }
}
