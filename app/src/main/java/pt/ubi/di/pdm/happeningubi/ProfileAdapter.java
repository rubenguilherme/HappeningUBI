package pt.ubi.di.pdm.happeningubi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import java.util.ArrayList;

public class ProfileAdapter extends RecyclerView.Adapter<ViewHolder> {


    Context context;
    ArrayList<EventClass> events;

    public ProfileAdapter(Context c, ArrayList<EventClass> e) {
        context = c;
        events = e;
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
            EventClass e = events.get(position - 1);
            vh.userImage.setImageResource(R.drawable.account_default_icon);
            vh.userName.setText(e.getUser());
            vh.eventName.setText(e.getName());
            vh.eventDesc.setText(e.getDescription());
            vh.eventLoc.setText(e.getLocation());
            vh.eventImage.setImageResource(R.drawable.bifana);
            if ((position - 1) % 2 == 0) vh.eventImage.setVisibility(View.GONE);
        } else if(holder instanceof ProfileViewHolder) ;
    }

    @Override
    public int getItemCount() {
        return events.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            // This is where we'll add footer.
            return 1;
        }
        return super.getItemViewType(position);
    }

    public class EventViewHolder extends ViewHolder {

        ImageView userImage, eventImage;
        TextView userName,eventName,eventDesc,eventLoc;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            userImage = itemView.findViewById(R.id.feedRV_user_image);
            userName = itemView.findViewById(R.id.feedRV_user_name);
            eventName = itemView.findViewById(R.id.feedRV_event_name);
            eventDesc = itemView.findViewById(R.id.feedRV_event_desc);
            eventLoc = itemView.findViewById(R.id.feedRV_event_location);
            eventImage = itemView.findViewById(R.id.feedRV_event_image);
        }
    }

    public class ProfileViewHolder extends ViewHolder {

        ImageView userImage;
        TextView createdText,createdNum;

        public ProfileViewHolder(@NonNull View itemView) {
            super(itemView);
            userImage = itemView.findViewById(R.id.profile_user_image);
            createdText = itemView.findViewById(R.id.profile_created);
            createdNum = itemView.findViewById(R.id.profile_event_count);

        }
    }
}
