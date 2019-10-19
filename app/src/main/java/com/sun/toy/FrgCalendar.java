package com.sun.toy;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sun.toy.widget.CalendarItemView;
import com.sun.toy.widget.CalendarView;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hnhariat on 2016-01-06.
 */ //어뎁터에 전달해 주기 위한 데이터
public class FrgCalendar extends Fragment {
    private int position;
    CalendarView calendarView;
    private long timeByMillis;
    private OnFragmentListener onFragmentListener;
    private View mRootView;

    public void setOnFragmentListener(OnFragmentListener onFragmentListener) {
        this.onFragmentListener = onFragmentListener;
    }

    public interface OnFragmentListener {
        void onFragmentListener(View view);
    }

    public static FrgCalendar newInstance(int position) {//정적 팩토리 메서드(팩토리 패턴)
        FrgCalendar frg = new FrgCalendar();//생성자
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        frg.setArguments(bundle);
        return frg;
    }

    public FrgCalendar() { } //반드시 써줄 필요는 없지만 생성자를 명시적으로 나타내었다.

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt("position");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_calendar, null);
        ButterKnife.bind(this, mRootView);
        initView();
        return mRootView;
    }
//달력 만들기.
    protected void initView() {
        calendarView = (CalendarView) mRootView.findViewById(R.id.calendarview);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeByMillis);
        calendar.set(Calendar.DATE, 1);
        // 1일의 요일
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        //이달의 마지막 날
        int maxDateOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendarView.initCalendar(dayOfWeek, maxDateOfMonth);
        for (int i = 0; i < maxDateOfMonth + 7; i++) {
            CalendarItemView child = new CalendarItemView(getActivity().getApplicationContext());
//            if (i == 20) {
//                child.setEvent(R.color.colorPrimaryDark);
//            }
            child.setDate(calendar.getTimeInMillis());
            if (i < 7) {
                child.setDayOfWeek(i);//날자: 일월화수목금토일
            } else {
                calendar.add(Calendar.DATE, 1);
            }
            calendarView.addView(child);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {//현재 보고 있는 Fragment를 알아내는 메소드
        if (isVisibleToUser && onFragmentListener != null && mRootView != null) {
            onFragmentListener.onFragmentListener(mRootView);
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getUserVisibleHint()) {

            mRootView.post(new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    onFragmentListener.onFragmentListener(mRootView);
                }
            });

        }
    }

    public void setTimeByMillis(long timeByMillis) {
        this.timeByMillis = timeByMillis;
    }
}
