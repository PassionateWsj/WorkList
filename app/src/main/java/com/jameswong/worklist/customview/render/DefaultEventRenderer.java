package com.jameswong.worklist.customview.render;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jameswong.worklist.R;
import com.jameswong.worklist.models.BaseCalendarEvent;


/**
 * Class helping to inflate our default layout in the AgendaAdapter
 * 默认的Event布局渲染类
 */
public class DefaultEventRenderer extends EventRenderer<BaseCalendarEvent> {

    // region class - EventRenderer

    @Override
    public void render(@NonNull View view, @NonNull BaseCalendarEvent event) {
        TextView alarmId = (TextView) view.findViewById(R.id.Alarm_id);
        alarmId.setText(event.getId() + "");
        TextView txtTitle = (TextView) view.findViewById(R.id.view_agenda_event_title);
        TextView txtLocation = (TextView) view.findViewById(R.id.view_agenda_event_location);
        TextView startAndEndTime = (TextView) view.findViewById(R.id.view_agenda_event_startAndEndTime);
        TextView description = (TextView) view.findViewById(R.id.view_agenda_event_description);
        LinearLayout descriptionContainer = (LinearLayout) view.findViewById(R.id.view_agenda_event_description_container);
        LinearLayout locationContainer = (LinearLayout) view.findViewById(R.id.view_agenda_event_location_container);
        TextView mTvAgendaStatus = (TextView) view.findViewById(R.id.tv_agenda_status);

        descriptionContainer.setVisibility(View.VISIBLE);
        txtTitle.setTextColor(view.getResources().getColor(android.R.color.white));
        txtTitle.setText(event.getTitle());
        txtLocation.setText(event.getLocation());
        startAndEndTime.setTextColor(view.getResources().getColor(android.R.color.white));
        startAndEndTime.setText(event.getmStartAndEndTime());
        description.setTextColor(view.getResources().getColor(android.R.color.white));
        description.setText(event.getDescription());
        if (event.getAgendaStatus() == 1) {
            mTvAgendaStatus.setTextColor(view.getResources().getColor(android.R.color.white));
            mTvAgendaStatus.setText("进行中");
        } else if (event.getAgendaStatus() == 2) {
            mTvAgendaStatus.setTextColor(view.getResources().getColor(R.color.luolelv));
            mTvAgendaStatus.setText("已完成");
        } else {
            mTvAgendaStatus.setTextColor(view.getResources().getColor(android.R.color.holo_red_light));
            mTvAgendaStatus.setText("未完成");
        }

        if (event.getLocation().length() > 0) {
            locationContainer.setVisibility(View.VISIBLE);
            txtLocation.setText(event.getLocation());
        } else {
            locationContainer.setVisibility(View.GONE);
        }

        if (event.getTitle().equals(view.getResources().getString(R.string.agenda_event_no_events))) {
            txtTitle.setTextColor(view.getResources().getColor(android.R.color.black));
        } else {
            txtTitle.setTextColor(view.getResources().getColor(android.R.color.white));
        }
        descriptionContainer.setBackgroundColor(event.getColor());
        txtLocation.setTextColor(view.getResources().getColor(android.R.color.white));
    }

    @Override
    public int getEventLayout() {
        return R.layout.agenda_view_event;
    }

    // endregion
}
