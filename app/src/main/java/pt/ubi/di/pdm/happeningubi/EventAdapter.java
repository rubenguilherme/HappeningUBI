package pt.ubi.di.pdm.happeningubi;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class EventAdapter extends RecyclerView.Adapter<ViewHolder> {

    public static final int TYPE_FEED = 0;
    public static final int TYPE_PROFILE = 1;

    private Context context;
    private ArrayList<EventClass> events;
    private int type, userid;
    private StorageReference storageRef;
    private FirebaseFirestore db;
    private String userName;

    public EventAdapter(Context c, ArrayList<EventClass> e, int t, Long id) {
        storageRef = FirebaseStorage.getInstance().getReference();
        context = c;
        events = e;
        type  = t;
        userid = id.intValue();
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
            GlideApp.with(context).load(storageRef.child("userimages/" + e.getUserID() + ".jpg")).error(GlideApp.with(vh.userImage).load(R.drawable.account_default_icon)).into(vh.userImage);
            vh.userName.setText(e.getUser());
            vh.eventName.setText(e.getName());
            vh.eventDesc.setText(e.getDescription());
            vh.eventLoc.setText(e.getLocation());
            Calendar cal = Calendar.getInstance();
            cal.setTime(e.getDate());
            String d = cal.get(Calendar.DAY_OF_MONTH) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR) + "  ";
            int h = cal.get(Calendar.HOUR_OF_DAY), m = cal.get(Calendar.MINUTE);
            if (h < 10) d += "0" + h; else d += h;
            d += ":";
            if (m < 10) d += "0" + m; else d += m;
            vh.eventDate.setText(d);
            vh.eventLayout.setOnClickListener(v -> {
                Intent intent = new Intent(context, ShowEventActivity.class);
                intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("event",e);
                context.startActivity(intent);
            });
            //notifyDataSetChanged();
        } else if(holder instanceof ProfileViewHolder) {
            ProfileViewHolder vh = (ProfileViewHolder) holder;
            GlideApp.with(context).load(storageRef.child("userimages/" + userid + ".jpg")).error(GlideApp.with(vh.userImage).load(R.drawable.account_default_icon)).into(vh.userImage);

            db = FirebaseFirestore.getInstance();
            db.collection("users")
                    .whereEqualTo("id", userid)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Map m = document.getData();
                                    vh.userName.setText(m.get("name").toString());
                                }
                            } else {
                                Log.w("TAG", "Error getting documents.", task.getException());
                            }
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

        ImageView userImage;
        TextView userName,eventName,eventDesc,eventLoc, eventDate;
        LinearLayout eventLayout;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            userImage = itemView.findViewById(R.id.event_user_image);
            userName = itemView.findViewById(R.id.event_user_name);
            eventName = itemView.findViewById(R.id.event_name);
            eventDesc = itemView.findViewById(R.id.event_desc);
            eventLoc = itemView.findViewById(R.id.event_location);
            eventDate = itemView.findViewById(R.id.event_date);
            eventLayout = itemView.findViewById(R.id.event_layout);
        }
    }

    public class ProfileViewHolder extends ViewHolder {

        ImageView userImage;
        TextView userName;

        public ProfileViewHolder(@NonNull View itemView) {
            super(itemView);
            userImage = itemView.findViewById(R.id.profile_user_image);
            userName = itemView.findViewById(R.id.profile_user_name);
        }
    }
}
