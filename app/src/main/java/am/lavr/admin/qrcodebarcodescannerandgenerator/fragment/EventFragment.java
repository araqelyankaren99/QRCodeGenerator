package am.lavr.admin.qrcodebarcodescannerandgenerator.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import am.lavr.admin.qrcodebarcodescannerandgenerator.R;

public class EventFragment extends BaseFragment implements View.OnClickListener {

    private View view;
    private EditText edtTitle;
    private EditText edtLocation;
    private TextView tvStartTime;
    private TextView tvEndTime;
    private Calendar mCalendar;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_event, container, false);
        context = view.getContext();
        findViews(view);
        setListeners();
        init();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_download:
            case R.id.btn_create:
                strQR = eventToString();
                super.onClick(v);
                break;
            case R.id.tv_event_start_time:
                openDatePickerDialog(tvStartTime);
                break;
            case R.id.tv_event_end_time:
                openDatePickerDialog(tvEndTime);
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
        edtTitle = (EditText) view.findViewById(R.id.edt_event_title);
        edtLocation = (EditText) view.findViewById(R.id.edt_event_location);
        tvStartTime = (TextView) view.findViewById(R.id.tv_event_start_time);
        tvEndTime = (TextView) view.findViewById(R.id.tv_event_end_time);
    }

    private void setListeners() {
        tvStartTime.setOnClickListener(this);
        tvEndTime.setOnClickListener(this);
    }

    private void init() {
        mCalendar = Calendar.getInstance();
        tvStartTime.setText(dateToString());
        tvEndTime.setText(dateToString());
    }

    private void openDatePickerDialog(final TextView textView) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mCalendar.set(Calendar.YEAR, year);
                mCalendar.set(Calendar.MONTH, month);
                mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                openTimePickerDialog(textView);
            }
        }, year, month, dayOfMonth);
        dialog.show();
    }

    private void openTimePickerDialog(final TextView textView) {
        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog dialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                mCalendar.set(Calendar.MINUTE, minute);
                mCalendar.set(Calendar.SECOND, 0);
                textView.setText(dateToString());
            }
        }, hourOfDay, minute, true);
        dialog.show();
    }

    private String dateToString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm");
        return dateFormat.format(mCalendar.getTime());
    }

    private String eventToString() {
        StringBuilder builder = new StringBuilder();
        builder.append("BEGIN:").append("VEVENT").append("\n")
                .append("SUMMARY:").append(edtTitle.getText().toString()).append("\n")
                .append("LOCATION:").append(edtLocation.getText().toString()).append("\n");
        String string = tvStartTime.getText().toString();
        string = string.replace(".", "");
        string = string.replace(":", "");
        string = string.replace(" ", "T");
        string = string + "00";
        builder.append("DTSTART:").append(string).append("\n");

        string = tvEndTime.getText().toString();
        string = string.replace(".", "");
        string = string.replace(":", "");
        string = string.replace(" ", "T");
        string = string + "00";
        builder.append("DTEND:").append(string).append("\n")
                .append("END:").append("VEVENT");
        return builder.toString();
    }
}
