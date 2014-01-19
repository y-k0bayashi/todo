package jp.co.newral.sukima.dao;

/**
 * トランザクション操作。
 */
public interface Transaction {
	void begin();
	void success();
	void end();
}
