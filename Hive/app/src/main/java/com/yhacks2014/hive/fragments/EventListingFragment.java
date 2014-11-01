package com.yhacks2014.hive.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.yhacks2014.hive.CardAdapter;
import com.yhacks2014.hive.Event;
import com.yhacks2014.hive.api.HiveCommunicator;
import com.yhacks2014.hive.R;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EventListingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EventListingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventListingFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
GridView layout;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView errorText;
    private OnFragmentInteractionListener mListener;
    private View v;
    CardAdapter adapter;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EventListingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EventListingFragment newInstance(String param1, String param2) {
        EventListingFragment fragment = new EventListingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public EventListingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_event_listing, container, false);
        errorText=(TextView)v.findViewById(R.id.errorText);
        ArrayList<Event> events=new ArrayList<Event>();

        //events will be an array of events
         layout= (GridView) v.findViewById(R.id.mainlayout);
        adapter=new CardAdapter(getActivity(), events);
        layout.setAdapter(adapter);
        HiveCommunicator communicator=new HiveCommunicator();
        getCats cats=new getCats();
        cats.execute();
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    class getCats extends AsyncTask<Void, Void, ArrayList<Event>> {
        HiveCommunicator communicator;

        @Override
        protected ArrayList<Event> doInBackground(Void... voids) {
            communicator = new HiveCommunicator();
            Log.d("result", "getting");
            ArrayList<Event> events1=new ArrayList<Event>();
            try {
                events1 = communicator.getEvents(communicator.getAllUserEvents(communicator.loginWithCredentials(communicator.email, communicator.password)));
                Log.d("creating event", "ww");
            }catch(NullPointerException e){
                e.printStackTrace();
            }
            //communicator.createEvent("Andrew",1,2,true,co);
            //communicator.deleteEntry("54548fa669b617fc392ba401");

            return events1;
        }

        @Override
        protected void onPostExecute(ArrayList<Event> aVoid) {
            super.onPostExecute(aVoid);

            adapter = new CardAdapter(getActivity(), aVoid);
            layout.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            if(aVoid.size()<=0)
                errorText.setVisibility(View.VISIBLE);
            Log.d("Done", "a");
        }
    }


}
