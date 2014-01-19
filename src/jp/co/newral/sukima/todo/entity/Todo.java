package jp.co.newral.sukima.todo.entity;

import java.util.Date;

/**
 * Todo表のエンティティ。
 */
public class Todo {
	private long id;
	private String subject;
	private Date dueDate;
	private boolean done;

	@Override
	public String toString() {
		return "Todo [id=" + id + ", subject=" + subject + ", dueDate="
				+ dueDate + ", done=" + done + "]";
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public Date getDueDate() {
		return dueDate;
	}
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	public boolean isDone() {
		return done;
	}
	public void setDone(boolean done) {
		this.done = done;
	}
}
