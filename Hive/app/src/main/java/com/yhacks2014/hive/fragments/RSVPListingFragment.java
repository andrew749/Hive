package com.yhacks2014.hive.fragments;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.lorentzos.flingswipe.SwipeFlingAdapterView;
import com.melnykov.fab.FloatingActionButton;
import com.yhacks2014.hive.AddEventActivity;
import com.yhacks2014.hive.CardAdapter;
import com.yhacks2014.hive.DetailActivity;
import com.yhacks2014.hive.Event;
import com.yhacks2014.hive.HiveMainActivity;
import com.yhacks2014.hive.R;
import com.yhacks2014.hive.api.HiveCommunicator;

import java.util.ArrayList;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link com.yhacks2014.hive.fragments.RSVPListingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link com.yhacks2014.hive.fragments.RSVPListingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RSVPListingFragment extends Fragment {
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
    //ArrayAdapter adapter;
    ArrayList<Event> events=new ArrayList<Event>();
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EventListingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RSVPListingFragment newInstance(String param1, String param2) {
        RSVPListingFragment fragment = new RSVPListingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public RSVPListingFragment() {
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
        v = inflater.inflate(R.layout.fragment_rsvp_listing, container, false);
        errorText=(TextView)v.findViewById(R.id.errorText);
        //events will be an array of events
        layout= (GridView) v.findViewById(R.id.mainlayout);
//        layout.setAdapter(adapter);
        getCats cats=new getCats();
        cats.execute();

        /* FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
        FloatingActionButton fab_prof = (FloatingActionButton) v.findViewById(R.id.fab_prof);
        //fab.attachToListView(layout);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddEventActivity.class);
                //intent.putExtra("data", events.get(i));
                startActivity(intent);
            }
        }); */

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
            Location loc = new Location("dummyprovider");
            loc.setLongitude(-72.922343);
            loc.setLatitude(41.316324);
            try {
                events1 = communicator.getEvents(communicator.getNearEvents(
                        communicator.loginWithCredentials(communicator.email, communicator.password)
                        , loc, 1000));
                Log.d("creating event", "ww");
            }catch(NullPointerException e){
                e.printStackTrace();
            }
            //communicator.createEvent("Andrew",1,2,true,co);
            //communicator.deleteEntry("54548fa669b617fc392ba401");

            return events1;
        }

        @Override
        protected void onPostExecute(final ArrayList<Event> aVoid) {
            super.onPostExecute(aVoid);

            Location myLoc = ((HiveMainActivity)getActivity()).getLocation();
            events.addAll(aVoid);
            adapter = new CardAdapter(getActivity(), events,myLoc);
            layout.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            if(events.size()<=0)
                errorText.setVisibility(View.VISIBLE);
               errorText.setVisibility(View.VISIBLE);
            Log.d("Done", "a");
            // Optionally add an OnItemClickListener
            layout.setOnItemClickListener(new GridView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getActivity(), DetailActivity.class);
                    intent.putExtra("data", events.get(position));
                    startActivity(intent);

                }
            });

        }
    }


}
