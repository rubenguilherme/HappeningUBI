package pt.ubi.di.pdm.happeningubi;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import java.util.ArrayList;

public class EventAdapter extends RecyclerView.Adapter<ViewHolder> {

    public static final int TYPE_FEED = 0;
    public static final int TYPE_PROFILE = 1;
    public static final int TYPE_CREATED = 2;

    private Context context;
    private ArrayList<EventClass> events;
    private int type;

    public EventAdapter(Context c, ArrayList<EventClass> e, int t) {
        context = c;
        events = e;
        type  = t;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType == 1) { //For Header
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.viewholder_profile, parent, false);
            return  new ProfileViewHolder(view);
        }

        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.viewholder_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(holder instanceof EventViewHolder) {
            EventViewHolder vh = (EventViewHolder) holder;
            EventClass e = events.get(position - ((type == TYPE_PROFILE) ? 1 : 0));
            vh.userImage.setImageResource(R.drawable.account_default_icon);
            vh.userName.setText(e.getUser());
            vh.eventName.setText(e.getName());
            vh.eventDesc.setText(e.getDescription());
            vh.eventLoc.setText(e.getLocation());
            vh.eventImage.setImageResource(R.drawable.bifana);
            vh.eventGoing.setText(String.valueOf(e.getGoing().size()));
            vh.eventInterested.setText(String.valueOf(e.getInterested().size()));
            vh.addGoing.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    e.addGoing(1);
                    notifyDataSetChanged();
                }
            });
            vh.addInterested.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    e.addInterested(1);
                    notifyDataSetChanged();
                }
            });
            if ((position - 1) % 2 == 0) vh.eventImage.setVisibility(View.GONE);
        } else if(holder instanceof ProfileViewHolder) {
            ProfileViewHolder vh = (ProfileViewHolder) holder;
            vh.profileLinear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, EventsCreatedActivity.class);
                    intent.putExtra("events", events);
                    context.startActivity(intent);
                    }
            });
        }
    }

    @Override
    public int getItemCount() {
        return events.size() + ((type == TYPE_PROFILE) ? 1 : 0);
    }

    @Override
    public int getItemViewType(int position) {
        if (type == 1)
            if (position == 0) return 1;
        return super.getItemViewType(position);
    }

    public class EventViewHolder extends ViewHolder {

        ImageView userImage, eventImage;
        TextView userName,eventName,eventDesc,eventLoc, eventGoing, eventInterested;
        LinearLayout eventButtons;
        Button addGoing, addInterested;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            userImage = itemView.findViewById(R.id.event_user_image);
            userName = itemView.findViewById(R.id.event_user_name);
            eventName = itemView.findViewById(R.id.event_name);
            eventDesc = itemView.findViewById(R.id.event_desc);
            eventLoc = itemView.findViewById(R.id.event_location);
            eventImage = itemView.findViewById(R.id.event_image);
            eventButtons = itemView.findViewById(R.id.event_buttons);
            eventGoing = itemView.findViewById(R.id.event_going);
            eventInterested = itemView.findViewById(R.id.event_interested);
            addGoing = itemView.findViewById(R.id.event_add_going);
            addInterested = itemView.findViewById(R.id.event_add_interested);
        }
    }

    public class ProfileViewHolder extends ViewHolder {

        ImageView userImage;
        TextView createdText,createdNum;
        LinearLayout profileLinear;

        public ProfileViewHolder(@NonNull View itemView) {
            super(itemView);
            userImage = itemView.findViewById(R.id.profile_user_image);
            createdText = itemView.findViewById(R.id.profile_created);
            createdNum = itemView.findViewById(R.id.profile_event_count);
            profileLinear = itemView.findViewById(R.id.profile_linear);
        }
    }
}
