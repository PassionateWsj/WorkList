package com.jameswong.worklist.pager;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.RadioGroup;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.jameswong.worklist.R;
import com.jameswong.worklist.database.AlarmDBSupport;
import com.jameswong.worklist.utils.TimeUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AboutMePager extends BasePager {


    @Bind(R.id.chart)
    PieChart mChart;
    @Bind(R.id.rg_chart)
    RadioGroup mRgChart;
    private AlarmDBSupport mDbSupport;
    private int mCompleteNum;
    private int mUnCompleteNum;

    public AboutMePager(Activity mActivity) {
        super(mActivity);
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.aboutme_pager, null);

        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void initData() {
        mDbSupport = new AlarmDBSupport(mActivity);
        mRgChart.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_all:
                        mCompleteNum = mDbSupport.getNumByEndDayAndStatus(0, 2);
                        mUnCompleteNum = mDbSupport.getNumByEndDayAndStatus(0, 0);
                        mChart.setCenterText("所有任务完成度");
                        if (mCompleteNum + mUnCompleteNum != 0) {
                            setData(mCompleteNum, mUnCompleteNum);
                        }
                        break;
                    case R.id.rb_this_month:
                        mCompleteNum = mDbSupport.getNumByEndDayAndStatus(TimeUtils.getMonthStartMillis(), 2);
                        mUnCompleteNum = mDbSupport.getNumByEndDayAndStatus(TimeUtils.getMonthStartMillis(), 0);
                        mChart.setCenterText("本月任务完成度");
                        if (mCompleteNum + mUnCompleteNum != 0) {
                            setData(mCompleteNum, mUnCompleteNum);
                        }
                        break;
                    case R.id.rb_this_week:
                        mCompleteNum = mDbSupport.getNumByEndDayAndStatus(TimeUtils.getWeekStartMillis(), 2);
                        mUnCompleteNum = mDbSupport.getNumByEndDayAndStatus(TimeUtils.getWeekStartMillis(), 0);
                        mChart.setCenterText("本周任务完成度");
                        if (mCompleteNum + mUnCompleteNum != 0) {
                            setData(mCompleteNum, mUnCompleteNum);
                        }
                        break;
                    case R.id.rb_today:
                        mCompleteNum = mDbSupport.getNumByEndDayAndStatus(TimeUtils.getTodayStartMillis(), 2);
                        mUnCompleteNum = mDbSupport.getNumByEndDayAndStatus(TimeUtils.getTodayStartMillis(), 0);
                        mChart.setCenterText("今日任务完成度");
                        if (mCompleteNum + mUnCompleteNum != 0) {
                            setData(mCompleteNum, mUnCompleteNum);
                        }
                        break;

                }
            }
        });

        mChart.setUsePercentValues(true);
        mChart.getDescription().setEnabled(false);
        mChart.setExtraOffsets(5, 10, 5, 5);

        mChart.setDragDecelerationFrictionCoef(0.95f);

//        mChart.setCenterTextTypeface(mTfLight);
//        mChart.setCenterText("任务完成度");

        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.WHITE);

        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);

        mChart.setHoleRadius(58f);
        mChart.setTransparentCircleRadius(61f);

        mChart.setDrawCenterText(true);

        mChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mChart.setRotationEnabled(true);
        mChart.setHighlightPerTapEnabled(true);

        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);

        // add a selection listener
//        mChart.setOnChartValueSelectedListener(this);

//        setData(4, 92.4f);

        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        // mChart.spin(2000, 0, 360);


        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // entry label styling
        mChart.setEntryLabelColor(Color.WHITE);
//        mChart.setEntryLabelTypeface(mTfRegular);
        mChart.setEntryLabelTextSize(12f);
        setData(0, 1);
        mRgChart.check(R.id.rb_all);
//        mCompleteNum = mDbSupport.getNumByEndDayAndStatus(0, 2);
//        mUnCompleteNum = mDbSupport.getNumByEndDayAndStatus(0, 0);
//        mChart.setCenterText("所有任务完成度");
//        if (mCompleteNum + mUnCompleteNum != 0) {
//            setData(mCompleteNum, mUnCompleteNum);
//        }

    }

    private void setData(int completeNum, int unCompleteNum) {

//        float mult = value;

        ArrayList<PieEntry> entries = new ArrayList<>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
//        for (int i = 0; i < count; i++) {
//            entries.add(new PieEntry((float) ((Math.random() * mult) + mult / 5),
//                    mParties[i % mParties.length],
//                    mActivity.getResources().getDrawable(android.R.drawable.ic_lock_idle_charging)));
//        }
        float value = (float) completeNum / (completeNum + unCompleteNum);
        entries.add(new PieEntry(value, "已完成" + completeNum));
        entries.add(new PieEntry(1 - value, "未完成" + unCompleteNum));


        PieDataSet dataSet = new PieDataSet(entries, "");

//        dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(3f);
//        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();

//        for (int c : ColorTemplate.VORDIPLOM_COLORS)
//            colors.add(c);
//
//        for (int c : ColorTemplate.JOYFUL_COLORS)
//            colors.add(c);

//        for (int c : ColorTemplate.COLORFUL_COLORS)
//            colors.add(c);

//        for (int c : ColorTemplate.LIBERTY_COLORS)
//            colors.add(c);

//        for (int c : ColorTemplate.PASTEL_COLORS)
//            colors.add(c);

        colors.add(Color.rgb(53, 177, 74));
        colors.add(Color.rgb(255, 68, 68));

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
//        data.setValueTypeface(mTfLight);
        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        mChart.invalidate();
    }

}
