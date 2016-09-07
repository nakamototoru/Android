package buyer.app.hidezo.com.buyer;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by dezami on 2016/09/07.
 */
public class CustomSupplierAdapter extends ArrayAdapter<Supplier> {
    public CustomSupplierAdapter(Context context, ArrayList<Supplier> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        Supplier user = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_supplier, parent, false);
        }

        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        TextView tvHome = (TextView) convertView.findViewById(R.id.tvHometown);

        // Populate the data into the template view using the data object
        tvName.setText(user.name);
        tvHome.setText(user.hometown);

        // Return the completed view to render on screen
        return convertView;
    }

}
