package matt.housekeep;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class InviteMessage extends RelativeLayout{
    private String groupName;
    private String groupKey;
    View rootView;
    TextView inviteGroupName;
    View acceptInvite;
    View declineInvite;

    public InviteMessage(Context context, String groupName, String groupKey){
        super(context);
        init(context);
        this.groupName = groupName;
        this.groupKey = groupKey;
        inviteGroupName.setText(this.groupName);
    }

    private void init(Context context){
        rootView = inflate(context, R.layout.invitation_button, this);
        inviteGroupName = (TextView) rootView.findViewById(R.id.inviteButtonGroupName);

        acceptInvite = rootView.findViewById(R.id.confirmButton);
        declineInvite = rootView.findViewById(R.id.rejectButton);

        acceptInvite.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //TODO add group to the user's groups and set status to member.
                Toast.makeText(getContext(),"Accept", Toast.LENGTH_SHORT).show();
            }
        });

        declineInvite.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //TODO remove invite and remove member from group
                Toast.makeText(getContext(),"Decline", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public String getGroupName(){
        return groupName;
    }

    public String getGroupKey(){
        return groupKey;
    }

}

