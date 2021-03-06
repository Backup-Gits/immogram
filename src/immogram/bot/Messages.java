package immogram.bot;

import java.text.MessageFormat;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Locale;
import java.util.ResourceBundle;

import immogram.Exceptions;
import immogram.task.TaskManager.ManagedTask;

class Messages {

	private final ResourceBundle bundle;
	private final DateTimeFormatter dateFormatter;
	private final DateTimeFormatter timeFormatter;

	public Messages(Locale locale) {
		this.bundle = ResourceBundle.getBundle("immogram.bot.Messages", locale);
		this.dateFormatter = DateTimeFormatter.ofPattern(bundle.getString("dateFormat"), locale).withZone(ZoneId.systemDefault());
		this.timeFormatter = DateTimeFormatter.ofPattern(bundle.getString("timeFormat"), locale).withZone(ZoneId.systemDefault());
	}

	public String obeyingChat() {
		return bundle.getString("obeyingChat");
	}

	public String factoryListing() {
		return bundle.getString("factoryListing");
	}

	public String factoryRequestTerm() {
		return bundle.getString("factoryRequestTerm");
	}

	public String factoryTaskCreated(ManagedTask task) {
		var pattern = bundle.getString("factoryTaskCreated");
		return MessageFormat.format(pattern, task.alias());
	}

	public String taskListing() {
		return bundle.getString("taskListing");
	}

	public String taskBackToListing() {
		return bundle.getString("taskBackToListing");
	}

	public String taskBackToStatus() {
		return bundle.getString("taskBackToStatus");
	}

	public String taskScheduled(ManagedTask task) {
		var pattern = bundle.getString("taskScheduled");
		return MessageFormat.format(pattern, task.alias());
	}

	public String taskCancelled(ManagedTask task) {
		var pattern = bundle.getString("taskCancelled");
		return MessageFormat.format(pattern, task.alias());
	}

	public String taskDeleted(ManagedTask task) {
		var pattern = bundle.getString("taskDeleted");
		return MessageFormat.format(pattern, task.alias());
	}

	public String taskWithException(ManagedTask task) {
		var trace = Exceptions.stackTraceOf(task.lastRunException().get());
		var pattern = bundle.getString("taskWithException");
		return MessageFormat.format(pattern, task.alias(), trace);
	}

	public String taskWithoutException(ManagedTask task) {
		var pattern = bundle.getString("taskWithoutException");
		return MessageFormat.format(pattern, task.alias());
	}

	public String taskStatus(ManagedTask task) {
		var pattern = bundle.getString("taskStatus");
		return MessageFormat.format(pattern, task.alias(),
				taskIsScheduled(task),
				taskHasRunTimestamp(task),
				taskHasRunException(task));
	}

	public String taskScheduleOrCancel() {
		return bundle.getString("taskScheduleOrCancel");
	}

	public String taskLastRunException() {
		return bundle.getString("taskLastRunException");
	}

	private String taskIsScheduled(ManagedTask task) {
		var period = task.runPeriod();
		if (period.isEmpty()) {
			return bundle.getString("taskIsNotScheduled");
		} else {
			var pattern = bundle.getString("taskIsScheduled");
			return MessageFormat.format(pattern, period.get().toHours());
		}
	}

	private String taskHasRunTimestamp(ManagedTask task) {
		var timestamp = task.lastRunTimestamp();
		if (timestamp.isEmpty()) {
			return bundle.getString("taskHasNoRunTimestamp");
		} else {
			var pattern = bundle.getString("taskHasRunTimestamp");
			return MessageFormat.format(pattern, dateFormat(timestamp.get()), timeFormat(timestamp.get()));
		}
	}

	private String taskHasRunException(ManagedTask task) {
		var exception = task.lastRunException();
		if (exception.isEmpty()) {
			return bundle.getString("taskHasNoException");
		} else {
			return bundle.getString("taskHasException");
		}
	}

	private String dateFormat(TemporalAccessor temporal) {
		return dateFormatter.format(temporal);
	}

	private String timeFormat(TemporalAccessor temporal) {
		return timeFormatter.format(temporal);
	}

}
