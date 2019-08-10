package fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.employeeattendence.R;
import com.example.employeeattendence.util.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import model.Attendence;



public class DashBoardFragment extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    MyAdapter adapter;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "DashBoardFragment";
    List<Attendence> attendenceList = new ArrayList<>();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference(Constants.ATTENDENCE);
    ListView listView;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ImageButton imageButton;
    String date1,date2;

    private OnFragmentInteractionListener mListener;

    public DashBoardFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static DashBoardFragment newInstance(String param1, String param2) {
        DashBoardFragment fragment = new DashBoardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        View view = inflater.inflate(R.layout.fragment_dash_board, container, false);
        listView = view.findViewById(R.id.list);
        final Button b1,b2;
        b1 = view.findViewById(R.id.b1);
        b2 = view.findViewById(R.id.b2);
        imageButton = view.findViewById(R.id.b3);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String month = String.format("%02d",(monthOfYear+1));
                        String day = String.format("%02d",dayOfMonth);
                        date1 = day + "-" + month + "-" + year;
                        b1.setText(date1);
                        date1 = year+month+day;
                    }
                };
                getDob(listener);
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String month = String.format("%02d",(monthOfYear+1));
                        String day = String.format("%02d",dayOfMonth);
                        date2 = day + "-" + month + "-" + year;
                        b2.setText(date2);
                        date2 = year+month+day;
                    }
                };
                getDob(listener);
            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(date1==null){
                    Toast.makeText(getContext(), "Please select From date", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(date2==null){
                    Toast.makeText(getContext(), "Please select To date", Toast.LENGTH_SHORT).show();
                    return;
                }
                getAllAttendByDate(date1,date2);
            }
        });
        adapter = new MyAdapter(getContext(),attendenceList);
        myRef.getRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG,"data : "+dataSnapshot.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG,databaseError.getMessage());
            }
        });
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
        getAllAttend();
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    class MyAdapter extends ArrayAdapter<String> {
        Context context;
        List<Attendence> attendenceList = new ArrayList<>();
        public MyAdapter(@NonNull Context context, List<Attendence> list) {
            super(context, R.layout.attendence_item);
            this.context = context;
            this.attendenceList = list;
        }

        @NonNull
        @Override
        public View getView(final int position, final View convertView, @NonNull final ViewGroup parent) {
            LayoutInflater layoutInflater =getActivity().getLayoutInflater();
            View view = layoutInflater.inflate(R.layout.attendence_item, parent,false);
            TextView t1,t2,t3;
            t1 = view.findViewById(R.id.textView2);
            t2 = view.findViewById(R.id.textView3);
            t3 = view.findViewById(R.id.textView4);
            Attendence attendence = attendenceList.get(position);

            t1.setText(getDate(attendence.getDate()));
            t2.setText(attendence.getTime());
            t3.setText(attendence.getSubject());
            return view;
        }

        @Override
        public int getCount() {
            return attendenceList.size();
        }
    }

    private void getDob(DatePickerDialog.OnDateSetListener listener){
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        int mon = Calendar.getInstance().get(Calendar.MONTH);

    }

    List<Attendence> filteredList = new ArrayList<>();
    private void getAllAttendByDate(String d1,String d2){
        Log.d("filterd",d1+" , "+d2);
        for(Attendence a : attendenceList){
            if(a.getDate().compareTo(d1)>=0 && a.getDate().compareTo(d2)<=0){
                filteredList.add(a);
                Log.d("filterd",a.toString());
            }
        }
        listView.setAdapter(null);
        MyAdapter adapter = new MyAdapter(getContext(),filteredList);
        listView.setAdapter(adapter);
    }
    private void getAllAttend(){
        String uid;
        uid = FirebaseAuth.getInstance().getUid();
        Query query = myRef.orderByChild("userId").equalTo(uid);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Log.d(TAG,dataSnapshot.toString());
                if(dataSnapshot.getValue()!=null){
                    for(DataSnapshot d : dataSnapshot.getChildren()){
                        Attendence attendence = d.getValue(Attendence.class);
                        attendenceList.add(attendence);
                        Log.d(TAG,attendence.toString());
                    }
                    Collections.sort(attendenceList);
                    adapter.notifyDataSetChanged();
                }else{

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG,databaseError.toString());
            }
        });
    }

    private String getDate(String date){
        String year,month,day;
        year = date.substring(0,4);
        month = date.substring(4,6);
        switch (month){
            case "01" : month = "Jan";break;
            case "02" : month = "Feb";break;
            case "03" : month = "Mar";break;
            case "04" : month = "Apr";break;
            case "05" : month = "May";break;
            case "06" : month = "Jun";break;
            case "07" : month = "Jul";break;
            case "08" : month = "Aug";break;
            case "09" : month = "Sep";break;
            case "10" : month = "Oct";break;
            case "11" : month = "Nov";break;
            case "12" : month = "Dec";break;
        }
        day = date.substring(6,8);
        return day+"-"+month+"-"+year;
    }
}

