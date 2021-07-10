package com.daesin.school;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.daesin.school.R;
import com.github.chrisbanes.photoview.PhotoView;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class ScheduleFragment extends Fragment {
    private static View view;
    private Context mContext;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_schedule, container, false);


        ScheduleInitAsyncTask task = new ScheduleInitAsyncTask();
        task.execute();

        return view;
    }

    class ScheduleInitAsyncTask extends AsyncTask<Void, Void, Void> {
        String imageUrl;
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Connection.Response response = null;
                response = Jsoup.connect("http://school.busanedu.net/daesin-m/cm/cntnts/cntntsView.do?mi=618569&cntntsId=5175")
                        .method(Connection.Method.GET)
                        .execute();
                Document document = response.parse();
                Elements imgurl = document.select("div.rsp_img img");
                System.out.println(imgurl);

                String[] res = imgurl.toString().split("\"");
                imageUrl = res[1];
                System.out.println(imageUrl);

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            PhotoView imageView = view.findViewById(R.id.scheduleFragment_imageView);
            Glide.with(mContext).load(imageUrl).into(imageView);
        }
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }
}
