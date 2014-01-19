package jp.co.newral.sukima.todo;

import jp.co.newral.sukima.todo.container.Container;

import jp.co.newral.sukima.todo.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

/**
 * 最初に生成されるアクティビティ。
 * {@link Container}オブジェクトの生成のみを行う。
 */
public class MainActivity extends Activity {

	private final Container container = Container.getInstance(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Intent intent = TodoListActivity.newIntent(this);
		startActivityForResult(intent, 0);
	}

	@Override
	public void onDestroy() {
		container.destroy();
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		finish();
	}
}
