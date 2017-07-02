package p2_vaio.topndownloader;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by p2_vaio on 7/1/2017.
 */

public class FeedAdapter <T extends FeedEntry> extends ArrayAdapter {
    private static final String TAG = "FeedAdapter";
    private final int layoutResource;
    private final LayoutInflater layoutInflater;
    private final List<T> list;

    public FeedAdapter(@NonNull Context context, @LayoutRes int resource, List<T> list) {
        super(context, resource);
        this.layoutResource = resource;
        this.layoutInflater = LayoutInflater.from(context);
        this.list = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//       class has been created to permanently store the views and dont find it again when getview is called
        ViewHolder viewHolder;

//        for reusing the scrolled up views
        if (convertView == null) {
            Log.d(TAG, "getView: called with null convertView");
            convertView = layoutInflater.inflate(layoutResource, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            Log.d(TAG, "getView: Provided convertView");
            viewHolder = (ViewHolder) convertView.getTag();
        }
//        View view = layoutInflater.inflate(layoutResource,parent , false);

/*        TextView tvname = (TextView)convertView.findViewById(R.id.title);
        TextView tvduration = (TextView)convertView.findViewById(R.id.duration);
        TextView tvrelease = (TextView)convertView.findViewById(R.id.release);
        TextView tvsummary = (TextView)convertView.findViewById(R.id.summary);*/

        T currentitem = list.get(position);

        viewHolder.tvname.setText(currentitem.getTitle());
        viewHolder.tvduration.setText(currentitem.getDuration());
        viewHolder.tvrelease.setText(currentitem.getReleasedate());
        viewHolder.tvsummary.setText(currentitem.getSummary());

        return convertView;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    private class ViewHolder {
        final TextView tvname;
        final TextView tvduration;
        final TextView tvrelease;
        final TextView tvsummary;

        public ViewHolder(View view) {
            this.tvname = (TextView) view.findViewById(R.id.title);
            this.tvduration = (TextView) view.findViewById(R.id.duration);
            this.tvrelease = (TextView) view.findViewById(R.id.release);
            this.tvsummary = (TextView) view.findViewById(R.id.summary);
        }
    }
}
