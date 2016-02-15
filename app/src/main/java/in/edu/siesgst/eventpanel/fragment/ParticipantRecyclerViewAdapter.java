package in.edu.siesgst.eventpanel.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import in.edu.siesgst.eventpanel.R;
import in.edu.siesgst.eventpanel.fragment.participant.ParticipantContent;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link in.edu.siesgst.eventpanel.fragment.participant.ParticipantContent.Participant} and makes a call to the
 * specified
 * TODO: Replace the implementation with code for your data type.
 */// {@link OnListFragmentInteractionListener}.
public class ParticipantRecyclerViewAdapter extends RecyclerView.Adapter<ParticipantRecyclerViewAdapter.ViewHolder> {

    private final List<ParticipantContent.Participant> mValues;
    private final ParticipantListFragment.OnListFragmentInteractionListener mListener;

    public ParticipantRecyclerViewAdapter(List<ParticipantContent.Participant> items, ParticipantListFragment.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_rowlayout_participant_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        /*holder.mItem = mValues.get(position);
        ParticipantContent.Participant data = mValues.get(position);
        holder.mParticipantName.setText(data.name);
        holder.mParticipantphone.setText(data.phone);
        holder.mParticipantPaymentStatus.setText(data.paymentStatus);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });*/
    }

    @Override
    public int getItemCount() {
        //return mValues.size();
        return 5;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mParticipantName;
        public final TextView mParticipantphone;
        public final TextView mParticipantPaymentStatus;
        public ParticipantContent.Participant mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mParticipantName = (TextView) view.findViewById(R.id.participant_list_participant_name);
            mParticipantphone = (TextView) view.findViewById(R.id.participant_list_participant_phone);
            mParticipantPaymentStatus = (TextView) view.findViewById(R.id.participant_list_participant_payment_status);
        }
    }
}
