package jp.co.newral.sukima.todo;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import jp.co.newral.sukima.todo.container.Container;
import jp.co.newral.sukima.todo.entity.Todo;
import jp.co.newral.sukima.todo.logic.TodoLogic;

import jp.co.newral.sukima.todo.R;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * {link @Todo}を編集するアクティビティ。
 */
public class TodoDetailActivity extends Activity {

	private static final String EXTRA_ID = "id";

	private final Container container = Container.getInstance();
	private final TodoLogic todoLogic = container.getTodoLogic();

	private String dateEmpty;
	private SimpleDateFormat dateFormat;
	private EditText editText_subject;
	private TextView editText_dueDate;
	private Button button_apply;
	private Button button_cancel;
	private Button button_delete;
	private int clickedButton;

	/** 処理対象の{@link Todo}識別子。{@code -1}の場合は新規作成。 */
	private long todoId;
	private Todo todo;

	public static Intent newIntentForCreate(Activity activity) {
		return new Intent(activity, TodoDetailActivity.class);
	}

	public static Intent newIntentForUpdate(Activity activity, long id) {
		Intent intent = new Intent(activity, TodoDetailActivity.class);
		intent.putExtra(EXTRA_ID, id);
		return intent;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo_detail);

		dateEmpty = getString(R.string.date_empty);
		dateFormat = new SimpleDateFormat(getString(R.string.date_format));
		editText_subject = (EditText)findViewById(R.id.editText_subject);
		editText_dueDate = (TextView)findViewById(R.id.editText_dueDate);
		button_apply = (Button)findViewById(R.id.button_apply);
		button_cancel = (Button)findViewById(R.id.button_cancel);
		button_delete = (Button)findViewById(R.id.button_delete);

		Intent intent = getIntent();
		todoId = intent.getLongExtra(EXTRA_ID, -1);

		if(todoId == -1) {
			todo = new Todo();
			button_delete.setEnabled(false);
		}
		else {
			todo = todoLogic.read(todoId);
			button_delete.setEnabled(true);
		}

		editText_subject.setText(todo.getSubject());

		if(todo.getDueDate() != null) {
			editText_dueDate.setText(dateFormat.format(todo.getDueDate()));
		}
		else {
			editText_dueDate.setText(dateEmpty);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.todo_detail, menu);
		return true;
	}

	public void onClickDueDate(View view) {
		Calendar cal = Calendar.getInstance();
		if(todo.getDueDate() != null) {
			cal.setTime(todo.getDueDate());
		}
		DatePickerDialog dialog = new DatePickerDialog(this, onDueDateSet, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
		dialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", onClickDatePicker);
		dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "CANCEL", onClickDatePicker);
		dialog.setButton(DialogInterface.BUTTON_NEUTRAL, "CLEAR", onClickDatePicker);
		dialog.show();
	}

	private final DialogInterface.OnClickListener onClickDatePicker = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			clickedButton = which;
		}
	};

	private final OnDateSetListener onDueDateSet = new OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
			switch (clickedButton) {
			case DialogInterface.BUTTON_NEUTRAL:
				todo.setDueDate(null);
				editText_dueDate.setText(dateEmpty);
			case DialogInterface.BUTTON_NEGATIVE:
				return;
			}
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.YEAR, year);
			cal.set(Calendar.MONTH, monthOfYear);
			cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			todo.setDueDate(cal.getTime());
			editText_dueDate.setText(dateFormat.format(cal.getTime()));
		}
	};

	public void onClickApply(View view) {
		todo.setSubject(editText_subject.getText().toString());
		try { todo.setDueDate(dateFormat.parse(editText_subject.getText().toString())); }
		catch(Exception e) {}

		if(todoId == -1) {
			todoLogic.create(todo);
		}
		else {
			todoLogic.update(todo);
		}

		setResult(RESULT_OK, null);
		finish();
	}

	public void onClickCancel(View view) {
		setResult(RESULT_CANCELED, null);
		finish();
	}

	public void onClickDelete(View view) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.confirm_delete);
		builder.setPositiveButton(R.string.ok, onClickAlertDialog);
		builder.setNegativeButton(R.string.cancel, null);
		builder.show();
	}

	private final DialogInterface.OnClickListener onClickAlertDialog = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			todoLogic.delete(todo);
			setResult(RESULT_OK, null);
			finish();
			Toast.makeText(TodoDetailActivity.this, String.format(getString(R.string.deleted), todo.getSubject()), Toast.LENGTH_LONG).show();
		}
	};
}
