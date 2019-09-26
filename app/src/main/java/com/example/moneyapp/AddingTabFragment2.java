package com.example.moneyapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddingTabFragment2 extends Fragment implements View.OnClickListener {

    private String categoryMessage;

    public AddingTabFragment2() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflate fragment
        View view = inflater.inflate(R.layout.adding_tab_fragment2, container, false);

        // define edittext xml element

        // define imageview xml element
        final ImageView iv1 = (ImageView)view.findViewById(R.id.xml_icon_cash);
        final ImageView iv2 = (ImageView)view.findViewById(R.id.xml_icon_atm);
        final AddingActivity activity = (AddingActivity)getActivity();

        // imageview = cash, on click
        iv1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
//                Toast.makeText(getContext(),"clicked: " + getString(R.string.icon_cash_label) , Toast.LENGTH_SHORT).show();;

                iv1.setImageResource(R.drawable.icon_cash_yellow);
                iv2.setImageResource(R.drawable.icon_grocery_gray);

                categoryMessage = getString(R.string.icon_cash_label);

                activity.processCategory(categoryMessage);

            }
        });

        // imageview = atm, on click
        iv2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
//                Toast.makeText(getContext(),"clicked: " + getString(R.string.icon_atm_label) , Toast.LENGTH_SHORT).show();;

                iv1.setImageResource(R.drawable.icon_cash_gray);
                iv2.setImageResource(R.drawable.icon_atm_yellow);

                categoryMessage = getString(R.string.icon_atm_label);

                activity.processCategory(categoryMessage);

            }
        });

        return view;
    }

    @Override
    public void onClick(View v) {

    }

}
