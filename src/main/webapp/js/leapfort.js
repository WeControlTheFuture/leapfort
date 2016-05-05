$(function() {
	$.ajax({
		url : "/leapfort/host/get",
		success : function(obj) {
			$.each(obj, function(i, value) {
				var lihtml = $('<li><a><i class="fa fa-fw fa-' + value.name
						+ '"></i>' + value.name + '</a></li>');
				console.log(lihtml);
				$('[class="nav navbar-nav side-nav"]').append(lihtml);
			});
		}
	});
})

function initTerm() {
	computeMaxTerminalSize = function() {
		rows = Math.round($(window).height() / 14);
		cols = Math.round($(window).width() / 6.8);
		return {
			cols : cols,
			rows : rows
		};
	};

	var connectionId, doneResizing, evt, term;
	connectionId = null;
	termSize = computeMaxTerminalSize()
	term = new Terminal({
		cols : termSize.cols,
		rows : termSize.rows,
		useStyle : true,
		screenKeys : true,
		cursorBlink : true
	});

	evt = new EventSource("/api/v1/terminal/stream/");
	evt.addEventListener('connectionId', function(event) {
		connectionId = event.data;
		term.write("Connection established\r\n");
		doneResizing();
	});
	evt.addEventListener('data', function(event) {
		console.log('data', event);
		term.write(JSON.parse(event.data));
	});
	evt.addEventListener('exit', function() {
		evt.close();
		term.write("\r\nTerminal exited.");
	});
	evt.addEventListener('error', function() {
		evt.close();
		term.write("\r\nTerminal exited.");
	});

	term.on('data', function(data) {
		if (evt.readyState === EventSource.OPEN) {
			$.post("/api/v1/terminal/send/" + connectionId, {
				data : data
			});
		}
	});
	term.on('title', function(title) {
		document.title = title;
	});
	term.open(document.body);

	window.onresize = function(event) {
		clearTimeout(this.id);
		this.id = setTimeout(doneResizing, 500);
	};
	doneResizing = function() {
		maxTermSize = computeMaxTerminalSize();
		$.post("/api/v1/terminal/resize-window/" + connectionId, maxTermSize,
				function(data) {
					if (data && data.cols && data.rows) {
						term.resize(data.cols, data.rows);
					}
				});
	};
}
