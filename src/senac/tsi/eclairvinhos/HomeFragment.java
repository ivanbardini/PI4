package senac.tsi.eclairvinhos;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class HomeFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
    	
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        
        Button button1 = (Button)view.findViewById(R.id.button1);
        
        button1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), LojaFragment.class);
				startActivity(intent);
			}
		});
        
        return view;
    }
}