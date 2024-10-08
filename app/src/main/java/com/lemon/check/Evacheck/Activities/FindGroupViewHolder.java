package com.lemon.check.Evacheck.Activities;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lemon.check.Evacheck.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class FindGroupViewHolder  extends RecyclerView.ViewHolder  {
    CircleImageView GroupProfileImg;
    TextView GroupTitle,GroupDescription;

////
CircleImageView GrpProfl_Img;
TextView GrpTitle,CreatedBy,GrpDescrip;
Button btnSubcribe_group,BtnJoinGroup;

////
    public FindGroupViewHolder(@NonNull View itemView) {
        super(itemView);
        GroupProfileImg=itemView.findViewById(R.id.GroupProf);
        GroupTitle=itemView.findViewById(R.id.GroupTit);
        GroupDescription=itemView.findViewById(R.id.GroupDescri);

   /////
        GrpTitle=itemView.findViewById(R.id.GrpTitle);
        CreatedBy=itemView.findViewById(R.id.CreatedBy);
        GrpDescrip=itemView.findViewById(R.id.GrpDescrip);

        /////
    }
}
