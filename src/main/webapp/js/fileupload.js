
File.prototype.toXml = function(md5) {
		return "<file>"+
			"<name>"+this.name+"</name>"+
			"<lastModified>"+this.lastModifiedDate.toLocaleDateString()+"</lastModified>"+
			"<md5>"+this.md5+"</md5>"+
			"<size>"+this.size+"</size>"+
			"<contentType>"+this.type+"</contentType>"+
			"</file>";
}
var fileRef;

// document ready
$(function() {
	// Check for the various File API support.
	if (window.File && window.FileReader && window.FileList && window.Blob) {
		// Great success! All the File APIs are supported.
	} else {
		alert('This browser does not support the required JavaScript File APIs.  Please try a different browser.');
	}
	
	// Setup Accordion
	$("#accordion").accordion({
		header : "h3",
		autoHeight: "true",
		fillSpace: "true"
	});
	
	$('#progressbar').progressbar({
		
	});
	
	// Setup Handlers for Drag-N-Drop
	var $singledrop = $('#single-drop-zone');
	$singledrop.bind({
		dragover: 	dragoverHandler,
		dragleave:	dragleaveHandler,
		drop:		singledropHandler
	});
	
	var $multidrop = $('#multi-drop-zone');
	$multidrop.bind({
		dragover:	dragoverHandler,
		dragleave:	dragleaveHandler,
		drop:		multidropHandler
	});
	
	var timer;
	
	// Setup Handler for Single Upload Submit
	$("#singleUploadBtn").click(function() {
		
		// clear previous result
		$('#single-upload-result').val('');
		
		// clear timer
		clearInterval(timer);
		
		// only do this if fileRef exists (i.e. they drag/dropped a file)
		if(fileRef) {
			var formData = new FormData();
			formData.append("file-metadata", fileRef.toXml());
			formData.append("file-content", fileRef);

			var xhr = new XMLHttpRequest();
			xhr.onreadystatechange=function() {
				if(xhr.readyState==4 && xhr.status==200) {
					$('#single-upload-response').html(xhr.responseText);
				}
			}
			xhr.open("POST", "/rest/fileupload/upload");
			xhr.send(formData);
			timer = setInterval(pollProgress, 1000);
		}
		
	});
	
	// Setup Handler for Find Click
	$("#findBtn").click(function() {
		$.get(
			'/rest/fileupload/files',	
			function(data) {
				$('#xml-response').text(formatXml(data));
				prettyPrint();
			},
			'text'
		);
	});
});

function pollProgress() {
	$.get(
		'/rest/fileupload/upload/progress/'+fileRef.name,
		function(data) {
			console.log(data);
			$('#progressbar').progressbar({value:data});
		}
	);
}

// callback for drag over event
function dragoverHandler() {
	// add hover styling
	$(this).addClass('hover');
	return false;
}

// callback for drag leave event
function dragleaveHandler() {
	// remove hover styling
	$(this).removeClass('hover');
	return false;
}

// callback for single file drag drop event
// TODO: could merge some of this behavior with multi drop file handler
function singledropHandler(e) {
	e = e || window.event;
	e.preventDefault();
	
	// clear previous list
	$('#single-drop-list').html('<ul></ul>');
	
	// remove hover decoration
	$(this).removeClass('hover');

	e = e.originalEvent || e;
	var files = (e.files || e.dataTransfer.files);
	
	// even if they sent multiple files, we only take one
	var f = files[0];
	if(f) {
		var reader = new FileReader();
		// read handler
		reader.onload = function(event) {
			var md5 = hex_md5(event.target.result);
			var li = '<li><strong>' + f.name + '</strong> ' + '[' + f.type
			+ '] ' + f.size + ' bytes, last modified: '
			+ f.lastModifiedDate.toLocaleDateString() + ', md5: '
			+ md5 + '</li>';
			$('#single-drop-list ul').append(li);
			// we need to save a ref to this file
			fileRef = f;
			fileRef.md5 = md5;
		};
		// error handler
		reader.onerror = function(err) {
			$('#single-drop-list ul').append(
					'<li><strong>error</strong> could not read ' + f.name
							+ ' error code:' + err.target.error.code
							+ '</li>');
		};
		// read the file
		reader.readAsBinaryString(f);
	}
	
	return false;
}

// callback for multi file drag drop event
function multidropHandler(e) {
	e = e || window.event;
	e.preventDefault();
	
	// clear previous list
	$('#multi-drop-list').html('<ul></ul>');

	// remove hover decoration
	$(this).removeClass('hover');

	// jQuery wraps the original event, so we try to detect that here
	e = e.originalEvent || e;
	// using e.files with fallback b/c e.dataTransfer is immutable
	// and can't be overridden in Polyfills
	// (http://sandbox.knarly.com/js/dropfiles)
	// files is a FileList of File objects
	var files = (e.files || e.dataTransfer.files);

	for ( var i = 0; i < files.length; i++) {
		(function(i) {
			var f = files[i];
			var reader = new FileReader();
			// read handler
			reader.onload = function(event) {
				// calculate md5
				var md5 = hex_md5(event.target.result);
				var li = '<li><strong>' + f.name + '</strong> ' + '[' + f.type
						+ '] ' + f.size + ' bytes, last modified: '
						+ f.lastModifiedDate.toLocaleDateString() + ', md5: '
						+ md5 + '</li>';
				$('#multi-drop-list ul').append(li);
				// TODO: finishi this
			};
			// error handler
			reader.onerror = function(err) {
				$('#multi-drop-list ul').append(
						'<li><strong>error</strong> could not read ' + f.name
								+ ' error code:' + err.target.error.code
								+ '</li>');
			};
			// read the file
			reader.readAsBinaryString(f);
		})(i);
	}
	return false;
} // end drop function

// indents XML for us and adds newlines
function formatXml(xml) {
	var formatted = '';
	var reg = /(>)(<)(\/*)/g;
	xml = xml.replace(reg, '$1\r\n$2$3');
	var pad = 0;
	jQuery.each(xml.split('\r\n'), function(index, node) {
		var indent = 0;
		if (node.match(/.+<\/\w[^>]*>$/)) {
			indent = 0;
		} else if (node.match(/^<\/\w/)) {
			if (pad != 0) {
				pad -= 1;
			}
		} else if (node.match(/^<\w[^>]*[^\/]>.*$/)) {
			indent = 1;
		} else {
			indent = 0;
		}

		var padding = '';
		for ( var i = 0; i < pad; i++) {
			padding += '  ';
		}

		formatted += padding + node + '\r\n';
		pad += indent;
	});

	return formatted;
}