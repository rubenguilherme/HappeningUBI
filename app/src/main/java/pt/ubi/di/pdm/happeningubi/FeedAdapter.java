package pt.ubi.di.pdm.happeningubi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.MyViewHolder> {

    Context context;
    ArrayList<EventClass> events;

    public FeedAdapter(Context c, ArrayList<EventClass> e) {
        context = c;
        events = e;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view  = inflater.inflate(R.layout.viewholder_event,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        EventClass e = events.get(position);
        holder.userImage.setImageResource(R.drawable.account_default_icon);
        holder.userName.setText(e.getUser());
        holder.eventName.setText(e.getName());
        holder.eventDesc.setText(e.getDescription());
        holder.eventLoc.setText(e.getLocation());
        holder.eventImage.setImageResource(R.drawable.bifana);
        if (position % 2 == 0) holder.eventImage.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView userImage, eventImage;
        TextView userName,eventName,eventDesc,eventLoc;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            userImage = itemView.findViewById(R.id.feedRV_user_image);
            userName = itemView.findViewById(R.id.feedRV_user_name);
            eventName = itemView.findViewById(R.id.feedRV_event_name);
            eventDesc = itemView.findViewById(R.id.feedRV_event_desc);
            eventImage = itemView.findViewById(R.id.feedRV_event_image);
            eventLoc = itemView.findViewById(R.id.feedRV_event_location);
        }
    }
}
