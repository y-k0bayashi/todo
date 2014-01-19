package jp.co.newral.sukima.todo;

import java.util.ArrayList;
import java.util.Arrays;

import jp.co.newral.sukima.todo.container.Container;
import jp.co.newral.sukima.todo.entity.Todo;
import jp.co.newral.sukima.todo.logic.TodoLogic;

import jp.co.newral.sukima.todo.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;

/**
 * {@link Todo}を一覧表示するアクティビティ。
 */
public class TodoListActivity extends Activity implements OnCheckedChangeListener {

	private Container container = Container.getInstance();

	private TodoLogic todoLogic = container.getTodoLogic();
	private TodoListAdapter todoListAdapter;

	private ListView listView_todo;
	private Button button_add; 

	public static Intent newIntent(Activity activity) {
		return new Intent(activity, TodoListActivity.class);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo_list);

		listView_todo = (ListView)findViewById(R.id.listView_todo);
		button_add = (Button)findViewById(R.id.button_add);

		todoListAdapter = new TodoListAdapter(this, todoLogic.findAll());
		listView_todo.setAdapter(todoListAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.todo_list, menu);
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if(resultCode != RESULT_OK) return;
		todoListAdapter.clear();
		todoListAdapter.addAll(Arrays.asList(todoLogic.findAll()));
	}

	public void onClickAdd(View view) {
		Intent intent = TodoDetailActivity.newIntentForCreate(this);
		startActivityForResult(intent, 0);
	}

	public void onClickSubject(View view) {
		Todo todo = (Todo)view.getTag();
		Intent intent = TodoDetailActivity.newIntentForUpdate(this, todo.getId());
		startActivityForResult(intent, 0);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		Todo todo = (Todo)buttonView.getTag();
		todo.setDone(isChecked);
		todoLogic.update(todo);
	}
}

class TodoListAdapter extends ArrayAdapter<Todo> {

	TodoListAdapter(TodoListActivity context, Todo[] todoList) {
		super(context, R.layout.listview_todo_item, new ArrayList<Todo>(Arrays.asList(todoList)));
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(getContext());
			convertView = inflater.inflate(R.layout.listview_todo_item, null);
		}
		Todo todo = (Todo)getItem(position);

		CheckBox checkBox_done = (CheckBox)convertView.findViewById(R.id.checkBox_done);
		checkBox_done.setTag(todo);
		checkBox_done.setOnCheckedChangeListener(null);
		checkBox_done.setChecked(todo.isDone());
		checkBox_done.setOnCheckedChangeListener((TodoListActivity)getContext());

		TextView textView_subject = (TextView)convertView.findViewById(R.id.textView_subject);
		textView_subject.setTag(todo);
		textView_subject.setText(todo.getSubject());

		return convertView;
	}

	public void onClickSubject(View view) {
		((TodoListActivity)getContext()).onClickSubject(view);
	}
}
