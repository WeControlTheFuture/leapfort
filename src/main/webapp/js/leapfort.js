var connectionId, doneResizing, evt, term;
connectionId = uuid();
// evt = null;
// term = null

$(function() {
	$.ajax({
		url : "/leapfort/host/get",
		success : function(obj) {
			$.each(obj, function(i, value) {
				var lihtml = '<li id="li-' + value.name
						+ '"><a><i class="fa fa-fw fa-' + value.name + '"></i>'
						+ value.name + '</a></li>';
				$('[class="nav navbar-nav side-nav"]').append(lihtml);
				$("#li-" + value.name).on(
						'click',
						function() {
							$("li").removeClass("active");
							$(this).attr("class", "active");
							$('[class="terminal"]').remove();
							initTerm();

							$.post("/leapfort/ssh/create/" + connectionId + "/"
									+ value.name);

							if (evt == null) {
								initEventSource();
								console.log("evt=====", evt);
							}

						});
			});
		}
	});

})

computeMaxTerminalSize = function() {
	rows = Math.round($(window).height() / 20);
	cols = Math.round($(window).width() / 10);
	return {
		cols : cols,
		rows : rows
	};
};

function initEventSource() {
	evt = new EventSource("/leapfort/ssh/stream/" + connectionId);
	evt.addEventListener('connectionId', function(event) {
		console.log("connectionId=======", event.data);
		connectionId = event.data;
		term.write("Connection established\r\n");
		// doneResizing();
	});
	evt.addEventListener('data', function(event) {
		console.log('data======', event);
		console.log(event.data);
		var edata = JSON.parse(event.data);
		term.write(edata.line + edata.end);
	});
	evt.addEventListener('exit', function() {
		evt.close();
		term.write("\r\nTerminal exited.");
	});
	/*
	 * evt.addEventListener('error', function(event) {
	 * console.log("error==========", event); evt.close(); //
	 * term.write("\r\nTerminal exited because error."); });
	 */
}

function initTerm() {
	var termSize = computeMaxTerminalSize()
	term = new Terminal({
		cols : termSize.cols,
		rows : termSize.rows,
		useStyle : true,
		screenKeys : true,
		cursorBlink : true
	});
	term.on('data', function(data) {
		if (evt != null) {
			$.post("/leapfort/ssh/send/" + connectionId, {
				data : data
			});
		}
	});

	term.on('title', function(title) {
		document.title = title;
	});
	term.open(document.getElementById("page-wrapper"));
	/*
	 * window.onresize = function(event) { clearTimeout(this.id); this.id =
	 * setTimeout(doneResizing, 500); }; doneResizing = function() { maxTermSize =
	 * computeMaxTerminalSize(); $.post("/api/v1/terminal/resize-window/" +
	 * connectionId, maxTermSize, function(data) { if (data && data.cols &&
	 * data.rows) { term.resize(data.cols, data.rows); } }); };
	 */
}

function uuid() {
	var s = [];
	var hexDigits = "0123456789abcdef";
	for (var i = 0; i < 36; i++) {
		s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
	}
	s[14] = "4"; // bits 12-15 of the time_hi_and_version field to 0010
	s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1); // bits 6-7 of the
	// clock_seq_hi_and_reserved
	// to 01
	s[8] = s[13] = s[18] = s[23] = "-";

	var uuid = s.join("");
	return uuid;
}