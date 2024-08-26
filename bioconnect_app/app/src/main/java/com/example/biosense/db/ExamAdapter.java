package com.example.biosense.db;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.biosense.R;

import java.util.List;

public class ExamAdapter extends RecyclerView.Adapter<ExamAdapter.ExamHolder> {

    private List<Exam> examsList;
    private ExamAdapter.MyOnclickAnalise myClickListener;

    public ExamAdapter(List<Exam> _list) {
        this.examsList = _list;
    }

    @NonNull
    @Override
    public ExamHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.line_recycler_history_activity, parent, false);
        return new ExamHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExamHolder holder, int position) {
        Exam exam = this.examsList.get(position);
        holder.getTv_patientCode().setText(exam.getExamId());
        holder.getTv_result().setText(exam.getResultString());
        holder.getTv_technique().setText(exam.getTechnique());

        holder.getSendButton().setOnClickListener(v -> {
            if (myClickListener != null) {
                myClickListener.onClick(exam.getFileName(), exam.getTechnique(), exam.getId());
            }
        });

        if (exam.getResult() != 2) {
            holder.getSendButton().setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return this.examsList.size();
    }

    public void setMyClickListener(MyOnclickAnalise myClickListener) {
        this.myClickListener = myClickListener;
    }

    public interface MyOnclickAnalise {
        public void onClick(String fileName, String technique, long id);
    }

    public class ExamHolder extends RecyclerView.ViewHolder {

        private TextView tv_result;
        private TextView tv_technique;
        private TextView tv_patientCode;

        private Button bt_analyse;

        public ExamHolder(@NonNull View itemView) {
            super(itemView);
            this.tv_technique = itemView.findViewById(R.id.textView_history_technique_line);
            this.tv_patientCode = itemView.findViewById(R.id.textView_history_patient_code_line);
            this.tv_result = itemView.findViewById(R.id.textView_history_result_line);
            this.bt_analyse = itemView.findViewById(R.id.button_history_send_line);
        }

        public TextView getTv_technique() {
            return tv_technique;
        }

        public TextView getTv_result() {
            return tv_result;
        }

        public TextView getTv_patientCode() {
            return tv_patientCode;
        }

        public Button getSendButton() {
            return this.bt_analyse;
        }
    }
}
