package com.example.moneyapp;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddingTabFragment1 extends Fragment implements View.OnClickListener {


    private String categoryMessage;

    public AddingTabFragment1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // inflate fragment
        View view = inflater.inflate(R.layout.adding_tab_fragment1, container, false);

        // define edittext xml element

        // define imageview xml element
        final ImageView iv1 = (ImageView)view.findViewById(R.id.xml_icon_food);
        final ImageView iv2 = (ImageView)view.findViewById(R.id.xml_icon_grocery);
        final ImageView iv3 = (ImageView)view.findViewById(R.id.xml_icon_transport);
        final AddingActivity activity = (AddingActivity)getActivity();

        // imageview = food, on click
        iv1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
//                Toast.makeText(getContext(),"clicked: " + getString(R.string.icon_food_label) , Toast.LENGTH_SHORT).show();;

                iv1.setImageResource(R.drawable.icon_food_yellow);
                iv2.setImageResource(R.drawable.icon_grocery_gray);
                iv3.setImageResource(R.drawable.icon_transport_gray);

                categoryMessage = getString(R.string.icon_food_label);

                activity.processCategory(categoryMessage);

            }
        });

        // imageview = grocery, on click
        iv2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
//                Toast.makeText(getContext(),"clicked: " + getString(R.string.icon_grocery_label), Toast.LENGTH_SHORT).show();;

                iv1.setImageResource(R.drawable.icon_food_gray);
                iv2.setImageResource(R.drawable.icon_grocery_yellow);
                iv3.setImageResource(R.drawable.icon_transport_gray);

                categoryMessage = getString(R.string.icon_grocery_label);
                activity.processCategory(categoryMessage);
            }
        });

        // imageview = transport, on click
        iv3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
//                Toast.makeText(getContext(),"clicked: " + getString(R.string.icon_transport_label), Toast.LENGTH_SHORT).show();;

                iv1.setImageResource(R.drawable.icon_food_gray);
                iv2.setImageResource(R.drawable.icon_grocery_gray);
                iv3.setImageResource(R.drawable.icon_transport_yellow);

                categoryMessage = getString(R.string.icon_transport_label);
                activity.processCategory(categoryMessage);

            }
        });

        return view;

    }


    @Override
    public void onClick(View v) {
    }

}
